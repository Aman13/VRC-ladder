package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchScheduler;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LadderMethodsTest {
    private List<MatchGroup> threeTeamMatchGroups;
    private List<MatchGroup> fourTeamMatchGroups;
    private static final int MATCHGROUP_COUNT = 3;

    @Before
    public void startUp() {
        threeTeamMatchGroups = new ArrayList<>();
        fourTeamMatchGroups = new ArrayList<>();
        for (int i = 0; i < MATCHGROUP_COUNT; i++) {
            threeTeamMatchGroups.add(MockMatchGroupGenerator.generateThreeTeamMatchGroup());
        }

        for (int i = 0; i < MATCHGROUP_COUNT; i++) {
            fourTeamMatchGroups.add(MockMatchGroupGenerator.generateFourTeamMatchGroup());
        }

        setAllMatchGroupTeamsPresent(threeTeamMatchGroups);
        setAllMatchGroupTeamsPresent(fourTeamMatchGroups);
    }

    private void setAllMatchGroupTeamsPresent(List<MatchGroup> matchGroups) {
        setAllTeamsPresent(convertMatchGroupListToTeamList(matchGroups));
    }
    private void setAllTeamsPresent(List<Team> teams) {
        for (Team team : teams) {
            AttendanceCard attendanceCard = team.getAttendanceCard();
            attendanceCard.setPreferredPlayTime(PlayTime.TIME_SLOT_A);
            attendanceCard.setAttendanceStatus(AttendanceStatus.PRESENT);
        }
    }

    private List<Team> convertMatchGroupListToTeamList(List<MatchGroup> matchGroups) {
        List<Team> teams = new ArrayList<>();
        for (MatchGroup matchGroup : matchGroups) {
            teams.addAll(matchGroup.getTeams());
        }
        return teams;
    }

    @Test
    public void testApplySinglePenalty() {
        testApplySinglePenalty(threeTeamMatchGroups);
        testApplySinglePenalty(fourTeamMatchGroups);
    }

    private void testApplySinglePenalty(List<MatchGroup> matchGroups) {
        List<Team> matchGroupTeamList = convertMatchGroupListToTeamList(matchGroups);
        for (int i = 0; i < matchGroups.size(); i++) {
            testApplySingleNotAttendingPenalty(
                    convertMatchGroupListToTeamList(matchGroups),
                    matchGroupTeamList.get(i));
            testApplySinglePenalty(
                    convertMatchGroupListToTeamList(matchGroups),
                    matchGroupTeamList.get(i),
                    AttendanceStatus.NO_SHOW);
            testApplySinglePenalty(
                    convertMatchGroupListToTeamList(matchGroups),
                    matchGroupTeamList.get(i),
                    AttendanceStatus.LATE);
        }
    }

    private void testApplySingleNotAttendingPenalty(List<Team> teamsList, Team penalizedTeam) {
        setAllTeamsPresent(teamsList);

        AttendanceCard penalizedTeamAttendanceCard = penalizedTeam.getAttendanceCard();
        penalizedTeamAttendanceCard.setPreferredPlayTime(PlayTime.NONE);

        testApplySinglePenalty(teamsList, penalizedTeam, AttendanceCard.NOT_ATTENDING_PENALTY);
    }

    private void testApplySinglePenalty(List<Team> teamsList, Team penalizedTeam, AttendanceStatus penaltyToApply) {
        setAllTeamsPresent(teamsList);

        AttendanceCard penalizedTeamAttendanceCard = penalizedTeam.getAttendanceCard();
        penalizedTeamAttendanceCard.setAttendanceStatus(penaltyToApply);

        testApplySinglePenalty(teamsList, penalizedTeam, penaltyToApply.getPenalty());
    }

    private void testApplySinglePenalty(List<Team> teamsList, Team penalizedTeam, int penalty) {
        Ladder ladder = new Ladder(teamsList);

        int originalLadderPosition = ladder.rankOfTeam(penalizedTeam);

        // test with no MatchGroups to isolate attendance penalties
        ladder.updateLadder(new ArrayList<>());

        int newLadderPosition = ladder.rankOfTeam(penalizedTeam);
        boolean isTeamPenaltyWithinLadderBounds =
                (originalLadderPosition + penalty <= ladder.getTeamCount());
        if (isTeamPenaltyWithinLadderBounds) {
            Assert.assertEquals(newLadderPosition, originalLadderPosition + penalty);
        } else {
            int lastPosition = ladder.getTeamCount();
            Assert.assertEquals(lastPosition, newLadderPosition);
        }
    }

    @Test
    public void testApplyMultiplePenalties() {
        List<Team> teamTestData = new ArrayList<>();
        int teamCount = 9;
        for (int i = 0; i < teamCount; i++) {
            Team team = MockTeamGenerator.generateTeam();
            team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
            teamTestData.add(team);
        }
        teamTestData.get(0).getAttendanceCard().setAttendanceStatus(AttendanceStatus.LATE);
        teamTestData.get(2).getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);
        teamTestData.get(4).getAttendanceCard().setAttendanceStatus(AttendanceStatus.LATE);

        //TODO: add more test data
        testApplyMultiplePenalties(teamTestData, new Integer[] {1, 3, 5, 6, 2, 0, 7, 8, 4});
    }

    private void testApplyMultiplePenalties(List<Team> inputTeams, Integer[] resultsIndices) {
        List<Team> expectedResults = new ArrayList<>();
        for (Integer resultIndex : resultsIndices) {
            expectedResults.add(inputTeams.get(resultIndex));
        }

        // test with no MatchGroups to isolate attendance penalties
        Ladder ladder = new Ladder(inputTeams);
        ladder.updateLadder(new ArrayList<>());

        for (int i = 0; i < inputTeams.size(); i++) {
            Assert.assertEquals(expectedResults.get(i), ladder.getLadder().get(i));
        }
    }

    @Test
    public void testRankingsWithinMatchGroups() {
        testRankingsWithinMatchGroup(MockMatchGroupGenerator.generateThreeTeamMatchGroup());
        testRankingsWithinMatchGroup(MockMatchGroupGenerator.generateFourTeamMatchGroup());
    }

    private void testRankingsWithinMatchGroup(MatchGroup matchGroup) {
        List<MatchGroup> matchGroups = new ArrayList<>();
        matchGroups.add(matchGroup);

        //create a new list of the teams in the MatchGroup that's modifiable
        List<Team> matchGroupTeams = new ArrayList<>();
        matchGroupTeams.addAll(matchGroup.getTeams());

        //make sure everyone's set to attend
        for (Team team : matchGroupTeams) {
            team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
        }

        List<List<Team>> permutations = generatePermutations(matchGroupTeams);

        for (List<Team> teamPermutation : permutations) {
            Ladder ladder = new Ladder(matchGroup.getTeams());
            matchGroup.getScoreCard().setRankedTeams(teamPermutation);
            ladder.updateLadder(matchGroups);
            Assert.assertEquals(ladder.getLadder(), teamPermutation);
        }
    }

   private List<List<Team>> generatePermutations(List<Team> original) {
        if (original.size() == 0) {
            List<List<Team>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        Team firstElement = original.remove(0);
        List<List<Team>> returnValue = new ArrayList<>();
        List<List<Team>> permutations = generatePermutations(original);
        for (List<Team> smallerPermutated : permutations) {
            for (int index=0; index <= smallerPermutated.size(); index++) {
                List<Team> temp = new ArrayList<
                        >(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

    @Test
    public void testSwapTeamsBetweenMatchGroups() {
        testSwapTeamsBetweenMatchGroups(threeTeamMatchGroups);
        testSwapTeamsBetweenMatchGroups(fourTeamMatchGroups);
    }

    private void testSwapTeamsBetweenMatchGroups(List<MatchGroup> matchGroups) {
        Ladder ladder = new Ladder(convertMatchGroupListToTeamList(matchGroups));

        for (MatchGroup matchGroup : matchGroups) {
            matchGroup.getScoreCard().setRankedTeams(matchGroup.getTeams());
        }

        List<Pair<Integer, Integer>> originalLastAndFirstTeamPositions = new ArrayList<>();
        for (int i = 0; i < matchGroups.size() - 1; i++) {
            MatchGroup currMatchGroup = matchGroups.get(i);
            MatchGroup nextMatchGroup = matchGroups.get(i + 1);
            originalLastAndFirstTeamPositions.add(getLastAndFirstTeamPositions(ladder, currMatchGroup, nextMatchGroup));
        }

        ladder.updateLadder(matchGroups);

        for (int i = 0; i < matchGroups.size() - 1; i++) {
            MatchGroup currMatchGroup = matchGroups.get(i);
            MatchGroup nextMatchGroup = matchGroups.get(i + 1);
            Pair<Integer, Integer> newLastAndFirstTeamPositions = getLastAndFirstTeamPositions(ladder, currMatchGroup, nextMatchGroup);

            Assert.assertEquals(originalLastAndFirstTeamPositions.get(i).getLeft(), newLastAndFirstTeamPositions.getRight());
            Assert.assertEquals(originalLastAndFirstTeamPositions.get(i).getRight(), newLastAndFirstTeamPositions.getLeft());
        }
    }

    /**
     * @return the current ladder positions of the lowest ranked team in the firstMatchGroup,
     * and the highest ranked team in the nextMatchGroup
     */
    private Pair<Integer, Integer> getLastAndFirstTeamPositions(Ladder ladder, MatchGroup firstMatchGroup, MatchGroup nextMatchGroup) {
        List<Team> firstMatchGroupRankedTeams = firstMatchGroup.getScoreCard().getRankedTeams();
        int positionLastTeamOfFirstMatchGroup = ladder.rankOfTeam(firstMatchGroupRankedTeams.get(firstMatchGroupRankedTeams.size() - 1));

        List<Team> nextMatchGroupRankedTeams = nextMatchGroup.getScoreCard().getRankedTeams();
        int positionFirstTeamOfNextMatchGroup = ladder.rankOfTeam(nextMatchGroupRankedTeams.get(0));

        return new ImmutablePair<>(positionLastTeamOfFirstMatchGroup, positionFirstTeamOfNextMatchGroup);
    }

    /**
     * A Testcase example provided by the vrc
     */
    @Test
    public void testUpdateLadder() {
        int numTeams = 19;
        MockTeamGenerator.resetTeamCount();
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < numTeams; i++) {
            teams.add(MockTeamGenerator.generateTeam());
        }

        int lastAttendingTeamIndex = 19;
        for (int i = 0; i < lastAttendingTeamIndex; i++) {
            teams.get(i).getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
        }

        int thirdTeamIndex = 2;
        teams.get(thirdTeamIndex).getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);

        int[] noShowTeamIndices = new int[] {0, 4};
        for (int noShowTeamIndex : noShowTeamIndices) {
            teams.get(noShowTeamIndex).getAttendanceCard().setAttendanceStatus(AttendanceStatus.NO_SHOW);
        }

        //Note that our algorithm applies Penalties differently than in the VRC example, so the expected results have
        //been changed to reflect this. In our penalties algorithm, teams actually end up moving down the correct number
        //of spots (from their original position) that their penalties enforce. In the vrc's example, teams
        //don't always get moved down the correct number of spots from their original position (ex: in the vrc's example,
        //team 3 only moved one spot down from its original position).
        int[] expectedTeamIndices = new int[] {1, 3, 5, 6, 2, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 16, 17, 4, 18};
        List<Team> expectedTeams = new ArrayList<>();
        for (int expectedTeamIndex : expectedTeamIndices) {
            expectedTeams.add(teams.get(expectedTeamIndex));
        }

        //This situation would never occur in real scenarios since the MatchGroupGenerator would generate
        //MatchGroups for all attending teams. Even if it was generating MatchGroups for just the first 9 teams,
        // (with 1 team away), it would generate 2 MatchGroups of 4 Teams each
        List<MatchGroup> matchGroups = new ArrayList<>();
        matchGroups.add(new MatchGroup(
                teams.get(0),
                teams.get(1),
                teams.get(3)));
        matchGroups.add(new MatchGroup(
                teams.get(4),
                teams.get(5),
                teams.get(6),
                teams.get(7)));
        for (MatchGroup matchGroup : matchGroups) {
            matchGroup.getScoreCard().setRankedTeams(matchGroup.getTeams());
        }

        Ladder ladder = new Ladder(teams);
        ladder.updateLadder(matchGroups);
        for (int i = 0; i < ladder.getLadder().size(); i++) {
            Assert.assertEquals(ladder.getLadder().get(i), expectedTeams.get(i));
        }
    }
}
