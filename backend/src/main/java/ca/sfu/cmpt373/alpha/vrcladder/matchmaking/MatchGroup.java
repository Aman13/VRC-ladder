package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.TeamNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.scores.ScoreCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A class for sorting teams into groups that will play against each other in matches
 * Groups are strictly limited to 3 or 4 teams
 */
@Entity
@Table(name = PersistenceConstants.TABLE_MATCH_GROUP)
public class MatchGroup implements Comparable<MatchGroup> {

    public static final int MIN_NUM_TEAMS = 3;
    public static final int MAX_NUM_TEAMS = 4;

    private static final String ERROR_NUM_TEAMS = "Teams list contains an invalid number of teams";
    private static final String ERROR_DUPLICATE_TEAMS = "MatchGroup cannot contain duplicate teams";
    private static final String ERROR_NO_TEAM_EXISTS = "The team does not exist in this MatchGroup";
    private static final String ERROR_TEAM_ALREADY_EXISTS = "The team already exists in this MatchGroup";
    private static final String ERROR_NOT_ENOUGH_TEAMS = "There are not enough teams in the MatchGroup to remove a team";
    private static final String ERROR_TOO_MANY_TEAMS = "There are too many teams in the MatchGroup to add another Team";

    @EmbeddedId
    private GeneratedId id;

    @OneToMany(fetch = FetchType.EAGER)
    @OrderColumn
    private List<Team> teams;

    @OneToOne (cascade = CascadeType.ALL)
    private ScoreCard scoreCard;

    private MatchGroup () {
        this.teams = new ArrayList<>();
        init();
    }

    public MatchGroup(Team team1, Team team2, Team team3) {
        this.teams = new ArrayList<>(Arrays.asList(team1, team2, team3));
        init();
    }

    public MatchGroup(Team team1, Team team2, Team team3, Team team4) {
        this.teams = new ArrayList<>(Arrays.asList(team1, team2, team3, team4));
        init();
    }

    /**
     * @throws IllegalStateException if the list contains more or less than the min/max amount of permissible teams
     */
    public MatchGroup(List<Team> teams) {
        if (teams.size() < MIN_NUM_TEAMS || teams.size() > MAX_NUM_TEAMS) {
            throw new IllegalStateException(ERROR_NUM_TEAMS);
        }
        this.teams = new ArrayList<>(teams);
        init();
    }

    private void init() {
        checkDuplicateTeams();
        id = new GeneratedId();
        scoreCard = new ScoreCard(this);
        Collections.sort(teams, getTeamLadderPositionComparator());
    }

    private void checkDuplicateTeams() {
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                if (teams.get(i).equals(teams.get(j))) {
                    throw new IllegalStateException(ERROR_DUPLICATE_TEAMS);
                }
            }
        }
    }

    public GeneratedId getId() {
        return id;
    }

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    public ScoreCard getScoreCard() {
        return this.scoreCard;
    }

    /**
     * @return the @{@link PlayTime} that the majority of the group teams want to play at.
     * In the case of a tie, it will return the preferred play time of the highest ranked player.
     */
    public PlayTime getPreferredGroupPlayTime() {
        Map<PlayTime, Integer> preferredTimeCounts = new HashMap<>();

        // Initialize counters to zero
        for (Team team : teams) {
            PlayTime playTime = team.getAttendanceCard().getPreferredPlayTime();
            preferredTimeCounts.put(playTime, 0);
        }

        // Count the 'votes' of preferred time slots for each team in a group.
        for (Team team : teams) {
            PlayTime playTime = team.getAttendanceCard().getPreferredPlayTime();
            preferredTimeCounts.replace(playTime, preferredTimeCounts.get(playTime) + 1);
        }

        // Find the time slot with the max votes.
        PlayTime votedPlayTime = null;
        int maxVoteCount = 0;
        boolean tie = false;
        for (PlayTime playTime : preferredTimeCounts.keySet()) {
            int playTimeVotes = preferredTimeCounts.get(playTime);
            if (playTimeVotes == maxVoteCount) {
                tie = true;
            }
            if (playTimeVotes > maxVoteCount) {
                votedPlayTime = playTime;
                maxVoteCount = playTimeVotes;
                tie = false;
            }
        }

        // If there's a tie in votes, choose the highest ranked player's preference.
        if (tie) {
            votedPlayTime = getTeam1().getAttendanceCard().getPreferredPlayTime();
        }

        return votedPlayTime;
    }

    public Team getTeam1(){
        int firstTeamIndex = 0;
        return teams.get(firstTeamIndex);
    }

    public Team getTeam2(){
        int secondTeamIndex = 1;
        return teams.get(secondTeamIndex);
    }

    public Team getTeam3(){
        int thirdTeamIndex = 2;
        return teams.get(thirdTeamIndex);
    }

    public int getTeamCount() {
        return teams.size();
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        MatchGroup otherMatchGroup = (MatchGroup) otherObj;

        return id.equals(otherMatchGroup.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    void setTeams(List<Team> newTeams) {
        if(newTeams.size() < MIN_NUM_TEAMS) {
            throw new IllegalStateException(ERROR_NOT_ENOUGH_TEAMS);
        } else if(newTeams.size() > MAX_NUM_TEAMS) {
            throw new IllegalStateException(ERROR_TOO_MANY_TEAMS);
        } else {
            this.teams = newTeams;
            Collections.sort(this.teams, getTeamLadderPositionComparator());
        }
    }

    void tradeTeams(Team teamToRemove, MatchGroup otherMatchGroup, Team teamToAdd) {
        if(this.teams.contains(teamToRemove) && otherMatchGroup.teams.contains(teamToAdd)) {
            //A new list is created to ensure that this.teams ALWAYS contains a valid number of teams
            ArrayList<Team> thisMatchGroupTeams = new ArrayList<>(this.teams);
            ArrayList<Team> otherMatchGroupTeams = new ArrayList<>(otherMatchGroup.teams);
            thisMatchGroupTeams.remove(teamToRemove);
            otherMatchGroupTeams.remove(teamToAdd);
            thisMatchGroupTeams.add(teamToAdd);
            otherMatchGroupTeams.add(teamToRemove);
            this.teams = thisMatchGroupTeams;
            otherMatchGroup.teams = otherMatchGroupTeams;

            Collections.sort(this.teams, getTeamLadderPositionComparator());
            Collections.sort(otherMatchGroup.teams, getTeamLadderPositionComparator());
        } else {
            throw new TeamNotFoundException();
        }
    }

    private Comparator<Team> getTeamLadderPositionComparator() {
        return (team1, team2) -> team1.getLadderPosition().compareTo(team2.getLadderPosition());
    }

    @Override
    public int compareTo(MatchGroup otherMatchGroup) {
        Team thisLowestRankedTeam = teams.get(teams.size() - 1);
        Team otherLowestRankedTeam = otherMatchGroup.teams.get(otherMatchGroup.teams.size() - 1);
        return thisLowestRankedTeam.getLadderPosition().compareTo(otherLowestRankedTeam.getLadderPosition());
    }

}
