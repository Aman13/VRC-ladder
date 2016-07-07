package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import java.util.Comparator;

/**
 * A helper class for Ladder that stores useful information
 * about Teams and their Ladder positions to help apply
 * Team Penalties when updating the Ladder
 */
class TeamIndexPenaltyTuple {

    private Team team;
    private int originalIndex;
    private int penalty;

    TeamIndexPenaltyTuple(Team team, int originalIndex, int penalty) {
        this.team = team;
        this.originalIndex = originalIndex;
        this.penalty = penalty;
    }

    public Team getTeam() {
        return team;
    }

    public int getPenalty() {
        return penalty;
    }

    public int getNewIndex() {
        return originalIndex + penalty;
    }

    public static Comparator<TeamIndexPenaltyTuple> getNewIndexComparator() {
        return (TeamIndexPenaltyTuple tuple1,
                TeamIndexPenaltyTuple tuple2)
                -> tuple1.getNewIndex() - tuple2.getNewIndex();
    }

    public static Comparator<TeamIndexPenaltyTuple> getPenaltyComparator() {
        return (TeamIndexPenaltyTuple tuple1,
                TeamIndexPenaltyTuple tuple2)
                -> tuple1.getPenalty() - tuple2.getPenalty();
    }
}