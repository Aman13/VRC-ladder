package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;


public class Ladder {
    private static final String ERROR_DUPLICATE_TEAM = "The Ladder already contains this team. The ladder may not hold duplicate elements";

    private List<Team> rankedTeams;
    public Ladder(List<Team> teams) {
        this.rankedTeams = new ArrayList<>();
        for(Team team : teams) {
            if(this.rankedTeams.contains(team)) {
                throw new IllegalStateException(ERROR_DUPLICATE_TEAM);
            }
            this.rankedTeams.add(team);
        }
    }

    public List<Team> getLadder() {
        return Collections.unmodifiableList(this.rankedTeams);
    }

    public int rankOfTeam(Team team) throws NoSuchElementException {
        int teamIndex = this.rankedTeams.indexOf(team);
        if(teamIndex == -1) {
            throw new NoSuchElementException();
        }
        //return the actual ranking of the team, not the index
        return teamIndex + 1;
    }

    public int getTeamCount() {
        return this.rankedTeams.size();
    }

    private void swapTeams(Team team1, Team team2) {
        try {
            int team1Rank = rankOfTeam(team1);
            int team2Rank = rankOfTeam(team2);

            swapTeams(team1Rank, team2Rank);
        } catch(NoSuchElementException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void swapTeams(int team1Rank, int team2Rank) {
        int team1Index = team1Rank - 1;
        int team2Index = team2Rank - 1;
        Collections.swap(this.rankedTeams, team1Index, team2Index);
    }

    public void updateLadder(List<MatchGroup> matchGroups) {
        for(MatchGroup matchGroup : matchGroups) {
            applyRankingsWithinMatchGroup(matchGroup);
        }

        swapTeamsBetweenMatchGroup(matchGroups);

        //applyPenalties();
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
        assert((matchGroup.getTeamCount() == rankIndices.size()) && (rankIndices.size() == rankedTeams.size()));
        for (int i = 0;i < rankIndices.size();i++) {
            this.rankedTeams.set(rankIndices.get(i), rankedTeams.get(i));
        }
    }

    private void swapTeamsBetweenMatchGroup(List<MatchGroup> matchGroups) {
        for(int i = 0;i < matchGroups.size() - 1;i++) {
            List<Team> rankedMatchGroupTeams1 = matchGroups.get(i).getScoreCard().getRankedTeams();
            List<Team> rankedMatchGroupTeams2 = matchGroups.get(i + 1).getScoreCard().getRankedTeams();

            Team lastPlaceTeamInMatchGroup1 = rankedMatchGroupTeams1.get(rankedMatchGroupTeams1.size() - 1);
            Team firstPlaceTeamInMatchGroup2 = rankedMatchGroupTeams2.get(0);

            swapTeams(lastPlaceTeamInMatchGroup1, firstPlaceTeamInMatchGroup2);
        }
    }

    /*private void applyPenalties() {
        List<TeamIndexPenaltyTuple> teamsToApplyPenaltiesTo = getTeamsToApplyPenaltiesTo();
        removePenalizedTeams(teamsToApplyPenaltiesTo);

        //sort so that highest ranked teams are re-added first
        //this way, we don't have to worry about the list indices shifting around
        //which guarantees that a team has shifted down the correct amount
        Collections.sort(teamsToApplyPenaltiesTo, TeamIndexPenaltyTuple.getNewIndexComparator());

        //re-add penalized teams at their new indices
        int prevNewIndex = 0;
        int prevConflictOffset = 0;
        for (int i = 0; i < teamsToApplyPenaltiesTo.size(); i++) {
            TeamIndexPenaltyTuple team = teamsToApplyPenaltiesTo.get(i);
            List<TeamIndexPenaltyTuple> teamsWithSameNewIndex = findTeamsWithSameNewIndex(teamsToApplyPenaltiesTo, team);
            boolean isConflictCompensationNeeded = prevConflictOffset + prevNewIndex >= team.getNewIndex();
            if (isConflictCompensationNeeded) {
                insertTeamsAtSameNewIndex(teamsWithSameNewIndex, prevConflictOffset);
            } else {
                insertTeamsAtSameNewIndex(teamsWithSameNewIndex, 0);
                prevConflictOffset = 0;
            }
            teamsWithSameNewIndex
                    .stream()
                    .forEach(teamsToApplyPenaltiesTo::remove);
            //roll back an index since we just removed the team at the current index
            i--;

            //if there was more than one team at an index, then record the conflict's offset
            //so that the next group of teams to be added can compensate appropriately
            prevConflictOffset += teamsWithSameNewIndex.size() - 1;
            prevNewIndex = team.getNewIndex();
        }
    }

    private List<TeamIndexPenaltyTuple> getTeamsToApplyPenaltiesTo() {
        List<TeamIndexPenaltyTuple> teamsToApplyPenaltiesTo = new ArrayList<>();

        for(int i = 0;i < this.rankedTeams.size();i++) {
            Team team = this.rankedTeams.get(i);
            AttendanceCard attendanceCard = team.getAttendanceCard();
            if(!attendanceCard.isAttending()) {
                teamsToApplyPenaltiesTo.add(new TeamIndexPenaltyTuple(team, i, AttendanceCard.NOT_ATTENDING_PENALTY));
            } else if(attendanceCard.getAttendanceStatus() == AttendanceStatus.LATE) {
                teamsToApplyPenaltiesTo.add(new TeamIndexPenaltyTuple(team, i, AttendanceStatus.LATE.getPenalty()));
            } else if(attendanceCard.getAttendanceStatus() == AttendanceStatus.NO_SHOW) {
                teamsToApplyPenaltiesTo.add(new TeamIndexPenaltyTuple(team, i, AttendanceStatus.NO_SHOW.getPenalty()));
            }
        }

        return teamsToApplyPenaltiesTo;
    }

    private void removePenalizedTeams(List<TeamIndexPenaltyTuple> teamsToApplyPenalties) {
        for (TeamIndexPenaltyTuple team : teamsToApplyPenalties) {
            this.rankedTeams.remove(team.getTeam());
        }
    }

    private List<TeamIndexPenaltyTuple> findTeamsWithSameNewIndex(List<TeamIndexPenaltyTuple> teams, TeamIndexPenaltyTuple indexTeam) {
        List<TeamIndexPenaltyTuple> teamsWithSameNewIndex = new ArrayList<>();
        for (TeamIndexPenaltyTuple currTeam : teams) {
            if (indexTeam.getNewIndex() == currTeam.getNewIndex()) {
                teamsWithSameNewIndex.add(currTeam);
            }
        }
        return teamsWithSameNewIndex;
    }

    private void insertTeamsAtSameNewIndex(List<TeamIndexPenaltyTuple> teamsWithSameNewIndex, int previousConflictOffset) {
        //sort teams by penalty so that for conflicting teams, the teams with the lowest penalty get priority
        Collections.sort(teamsWithSameNewIndex, TeamIndexPenaltyTuple.getPenaltyComparator());

        for (int conflictOffset = 0; conflictOffset < teamsWithSameNewIndex.size(); conflictOffset++) {
            TeamIndexPenaltyTuple currTeam = teamsWithSameNewIndex.get(conflictOffset);
            // for each following team, the index must be offset by i to
            // compensate for prior teams in the tuples list being added to the ladder
            // additionally, previousConflictOffset is needed if a previous conflict occurred, and the
            // current teams being re-added need to shift down to compensate for the previous conflict
            int newOffsetIndex = currTeam.getNewIndex() + previousConflictOffset + conflictOffset;
            if (newOffsetIndex < this.rankedTeams.size()) {
                this.rankedTeams.add(newOffsetIndex, currTeam.getTeam());
            } else {
                //if the new index is beyond the ladder bounds, add the element to the end of the list
                this.rankedTeams.add(currTeam.getTeam());
            }
        }
    }*/

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");

        for(int i = 0;i < this.rankedTeams.size();i++) {
            stringBuilder.append(i + ": ");
            stringBuilder.append(this.rankedTeams.get(i).toString() + "\n ");
        }

        return stringBuilder.toString();
    }
}
