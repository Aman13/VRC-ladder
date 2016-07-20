package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.PlaceholderTeam;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;


public class Ladder {
    private static final String ERROR_DUPLICATE_TEAM = "The Ladder already contains this team. The ladder may not hold duplicate elements";
    private static final Team PLACEHOLDER_TEAM = new PlaceholderTeam();
    private static final int ABSENT_PENALTY = 2;
    private static final int NO_SHOW_PENALTY = 10;
    private static final int LATE_PENALTY = 4;

    private List<Team> allTeams;
    private List<Team> attendingTeams;
    private List<Team> absentTeams;

    public Ladder(List<Team> teams) {
        this.allTeams = new ArrayList<>();
        for(Team team : teams) {
            if(this.allTeams.contains(team)) {
                throw new IllegalStateException(ERROR_DUPLICATE_TEAM);
            }
            this.allTeams.add(team);
        }
    }

    public List<Team> getLadder() {
        return Collections.unmodifiableList(this.allTeams);
    }

    public int rankOfTeam(Team team) throws NoSuchElementException {
        int teamIndex = this.allTeams.indexOf(team);
        if(teamIndex == -1) {
            throw new NoSuchElementException();
        }
        //return the actual ranking of the team, not the index
        return teamIndex + 1;
    }

    public int getTeamCount() {
        return this.allTeams.size();
    }

    private void swapTeams(Team team1, Team team2) {
        try {
            int team1Index = rankOfTeam(team1) - 1;
            int team2Index = rankOfTeam(team2) - 1;

            Collections.swap(this.allTeams, team1Index, team2Index);
        } catch(NoSuchElementException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateLadder(List<MatchGroup> matchGroups) {
        for(MatchGroup matchGroup : matchGroups) {
            this.applyRankingsWithinMatchGroup(matchGroup);
        }
        this.swapTeamsBetweenMatchGroups(matchGroups);

        this.splitLadder();

        this.applyNonAttendancePenalties();
        this.applyAttendingPenalties(AttendanceStatus.LATE, this.LATE_PENALTY);
        this.applyAttendingPenalties(AttendanceStatus.NO_SHOW, this.NO_SHOW_PENALTY);

        this.mergeLadder();
    }

    private void splitLadder() {
        this.attendingTeams = new ArrayList<>();
        this.absentTeams = new ArrayList<>();

        for(Team team : this.allTeams) {
            if(team.getAttendanceCard().isAttending()) {
                this.attendingTeams.add(team);
                this.absentTeams.add(this.PLACEHOLDER_TEAM);
            } else {
                this.attendingTeams.add(this.PLACEHOLDER_TEAM);
                this.absentTeams.add(team);
            }
        }
    }

    private void mergeLadder() {
        int attendingTeamIndex = 0;
        for(int teamIndex = 0;teamIndex < this.allTeams.size();teamIndex++) {
            if(this.absentTeams.get(teamIndex) != this.PLACEHOLDER_TEAM) {
                this.allTeams.set(teamIndex, this.absentTeams.get(teamIndex));
            } else {
                while(this.attendingTeams.get(attendingTeamIndex) == PLACEHOLDER_TEAM && attendingTeamIndex < this.attendingTeams.size()) {
                    attendingTeamIndex++;
                }
                assert(attendingTeamIndex < this.attendingTeams.size());
                this.allTeams.set(teamIndex, this.attendingTeams.get(attendingTeamIndex));
                attendingTeamIndex++;
            }
        }

        this.attendingTeams.clear();
        this.absentTeams.clear();
    }

    private void applyRankingsWithinMatchGroup(MatchGroup matchGroup) {
        //find the ladder indices for 1st, 2nd, 3rd, 4th... positions
        //these need to be found before, because we will be overwriting teams below
        List<Integer> rankIndices = new ArrayList<>();
        for(Team team : matchGroup.getTeams()) {
            rankIndices.add(rankOfTeam(team) - 1);
        }

        //overwrite the team in each position with the teams specified in the ScoreCard
        List<Team> rankedTeams = matchGroup.getScoreCard().getRankedTeams();
        assert(matchGroup.getTeamCount() == rankIndices.size() && rankIndices.size() == rankedTeams.size());
        for (int i = 0;i < rankIndices.size();i++) {
            this.allTeams.set(rankIndices.get(i), rankedTeams.get(i));
        }
    }

    private void swapTeamsBetweenMatchGroups(List<MatchGroup> matchGroups) {
        for(int i = 0;i < matchGroups.size() - 1;i++) {
            List<Team> rankedMatchGroupTeams1 = matchGroups.get(i).getScoreCard().getRankedTeams();
            List<Team> rankedMatchGroupTeams2 = matchGroups.get(i + 1).getScoreCard().getRankedTeams();

            Team lastPlaceTeamInMatchGroup1 = rankedMatchGroupTeams1.get(rankedMatchGroupTeams1.size() - 1);
            Team firstPlaceTeamInMatchGroup2 = rankedMatchGroupTeams2.get(0);

            swapTeams(lastPlaceTeamInMatchGroup1, firstPlaceTeamInMatchGroup2);
        }
    }

    private void applyNonAttendancePenalties() {
        List<Boolean> penalizedTeamsStatus = new ArrayList<>(Collections.nCopies(this.absentTeams.size(), false));

        for(int teamIndex = this.absentTeams.size() - 1;teamIndex > -1;teamIndex--) {
            //It is necessary to loop backwards, else the penalties will "cross over" each other. Hard to explain, just try doing it on a piece of paper and you'll see.
            //Additionally, if a penalty reaches the end of the list, the penalties directly above it will be applied differently
            Team team = this.absentTeams.get(teamIndex);
            if(team != this.PLACEHOLDER_TEAM) {
                this.penalizeAbsentTeam(penalizedTeamsStatus, team);
            }
        }
    }

    private void applyAttendingPenalties(AttendanceStatus attendanceStatus, int penalty) {
        List<Boolean> penalizedTeamsStatus = new ArrayList<>(Collections.nCopies(this.attendingTeams.size(), false));

        for(int teamIndex = this.attendingTeams.size() - 1;teamIndex > -1;teamIndex--) {
            //It is necessary to loop backwards, else the penalties will "cross over" each other. Hard to explain, just try doing it on a piece of paper and you'll see.
            //Additionally, if a penalty reaches the end of the list, the penalties directly above it will be applied differently
            Team team = this.attendingTeams.get(teamIndex);
            if(team != this.PLACEHOLDER_TEAM && team.getAttendanceCard().getAttendanceStatus() == attendanceStatus) {
                this.penalizeAttendingTeam(penalizedTeamsStatus, team, penalty);
            }
        }
    }

    private void penalizeAttendingTeam(List<Boolean> penalizedTeamsStatus, Team team, int penalty) {
        int teamIndex = this.attendingTeams.indexOf(team);
        this.attendingTeams.set(teamIndex, this.PLACEHOLDER_TEAM);
        teamIndex += penalty;

        if(teamIndex >= this.absentTeams.size()) {
            teamIndex = this.absentTeams.size() - 1;
        }

        assert(this.absentTeams.size() == penalizedTeamsStatus.size());

        while(penalizedTeamsStatus.get(teamIndex)) {
            teamIndex--;
        }

        penalizedTeamsStatus.set(teamIndex, true);

        if(this.absentTeams.get(teamIndex) != this.PLACEHOLDER_TEAM) {
            int nextPlaceHolderIndex;
            for(nextPlaceHolderIndex = teamIndex - 1;this.absentTeams.get(nextPlaceHolderIndex) != this.PLACEHOLDER_TEAM && nextPlaceHolderIndex > -1;nextPlaceHolderIndex--);
            assert(nextPlaceHolderIndex > -1);
            this.absentTeams.remove(nextPlaceHolderIndex);
            this.absentTeams.add(teamIndex, team);
        } else {
            this.absentTeams.set(teamIndex, team);
        }
    }

    private void penalizeAbsentTeam(List<Boolean> penalizedTeamsStatus, Team team) {
        int teamIndex = this.absentTeams.indexOf(team);
        this.absentTeams.remove(team);
        teamIndex += this.ABSENT_PENALTY;

        if(teamIndex >= this.absentTeams.size()) {
            teamIndex = this.absentTeams.size();
        }

        assert(this.absentTeams.size() + 1 == penalizedTeamsStatus.size());

        while(penalizedTeamsStatus.get(teamIndex)) {
            teamIndex--;
        }

        penalizedTeamsStatus.set(teamIndex, true);
        this.absentTeams.add(teamIndex, team);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");

        for(int i = 0;i < this.allTeams.size();i++) {
            User firstPlayer = this.allTeams.get(i).getFirstPlayer();
            User secondPlayer = this.allTeams.get(i).getSecondPlayer();
            stringBuilder.append(i + ": ");
            stringBuilder.append(firstPlayer.getFirstName() + " " + firstPlayer.getLastName() + ", " + secondPlayer.getFirstName() + " " + secondPlayer.getLastName() + "\n ");
        }

        return stringBuilder.toString();
    }
}
