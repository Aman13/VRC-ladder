package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import java.util.ArrayList;
import java.util.List;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.teams.LadderPosition;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;

import org.junit.Before;
import org.junit.Test;

import javax.naming.SizeLimitExceededException;

import static org.junit.Assert.assertTrue;

public class LadderTest extends BaseTest {
	private final int ATTENDING = 0;
	private final int NOT_ATTENDING = 1;
	private final int TEST_CASE_COUNT = 7;
	private final int GENERATED_TEAMS = 30;
	private boolean initialized = false;

	//List of Lists of Teams, first index is Case, second is ATTENDING or NOT_ATTENDING
	private List<List<List<Team>>> testTeams;

    public LadderTest() {}

	private List<List<Team>> genTeamCase0() { //Case 0: All attending teams present
		List<List<Team>> caseTeams = new ArrayList<>();

		for(int attendance = ATTENDING;attendance <= NOT_ATTENDING;attendance++) {
			caseTeams.add(new ArrayList<>());
			for(int i = 0;i < GENERATED_TEAMS;i++) {
				Team team = MockTeamGenerator.generateTeam();

				if(attendance == ATTENDING) {
					team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.PRESENT);
					team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
				} else { //NOT_ATTENDING
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);
				}
				caseTeams.get(attendance).add(team);
			}
		}

		return caseTeams;
	}

	private List<List<Team>> genTeamCase1() { //Case 1: All attending teams late
		List<List<Team>> caseTeams = new ArrayList<>();

		for(int attendance = ATTENDING;attendance <= NOT_ATTENDING;attendance++) {
			caseTeams.add(new ArrayList<>());
			for(int i = 0;i < GENERATED_TEAMS;i++) {
				Team team = MockTeamGenerator.generateTeam();

				if(attendance == ATTENDING) {
					team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.LATE);
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
				} else { //NOT_ATTENDING
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);
				}
				caseTeams.get(attendance).add(team);
			}
		}

		return caseTeams;
	}

	private List<List<Team>> genTeamCase2() { //Case 2: All attending teams NoShow
		List<List<Team>> caseTeams = new ArrayList<>();

		for(int attendance = ATTENDING;attendance <= NOT_ATTENDING;attendance++) {
			caseTeams.add(new ArrayList<>());
			for(int i = 0;i < GENERATED_TEAMS;i++) {
				Team team = MockTeamGenerator.generateTeam();

				if(attendance == ATTENDING) {
					team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.NO_SHOW);
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
				} else { //NOT_ATTENDING
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);
				}
				caseTeams.get(attendance).add(team);
			}
		}

		return caseTeams;
	}

	private List<List<Team>> genTeamCase3() { //Case 3: Attending teams vary present late
		List<List<Team>> caseTeams = new ArrayList<>();

		for(int attendance = ATTENDING;attendance <= NOT_ATTENDING;attendance++) {
			caseTeams.add(new ArrayList<>());
			for(int i = 0;i < GENERATED_TEAMS;i++) {
				Team team = MockTeamGenerator.generateTeam();

				if(attendance == ATTENDING) {
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
                    int attendanceGenerator = i % 2;
                    if(attendanceGenerator == 0) {
                        team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.PRESENT);
                    } else {
                        team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.LATE);
                    }
				} else { //NOT_ATTENDING
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);
				}
				caseTeams.get(attendance).add(team);
			}
		}

		return caseTeams;
	}

	private List<List<Team>> genTeamCase4() { //Case 4: Attending teams vary NoShow late
		List<List<Team>> caseTeams = new ArrayList<>();

		for(int attendance = ATTENDING;attendance <= NOT_ATTENDING;attendance++) {
			caseTeams.add(new ArrayList<>());
			for(int i = 0;i < GENERATED_TEAMS;i++) {
				Team team = MockTeamGenerator.generateTeam();

				if(attendance == ATTENDING) {
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
                    int attendanceGenerator = i % 2;
                    if(attendanceGenerator == 0) {
                        team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.NO_SHOW);
                    } else {
                        team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.LATE);
                    }
				} else { //NOT_ATTENDING
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);
				}
				caseTeams.get(attendance).add(team);
			}
		}

		return caseTeams;
	}

	private List<List<Team>> genTeamCase5() { //Case 5: Attending teams vary present NoShow
		List<List<Team>> caseTeams = new ArrayList<>();

		for(int attendance = ATTENDING;attendance <= NOT_ATTENDING;attendance++) {
			caseTeams.add(new ArrayList<>());
			for(int i = 0;i < GENERATED_TEAMS;i++) {
				Team team = MockTeamGenerator.generateTeam();

				if(attendance == ATTENDING) {
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
                    int attendanceGenerator = i % 2;
                    if(attendanceGenerator == 0) {
                        team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.PRESENT);
                    } else {
                        team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.NO_SHOW);
                    }
				} else { //NOT_ATTENDING
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);
				}
				caseTeams.get(attendance).add(team);
			}
		}

		return caseTeams;
	}

	private List<List<Team>> genTeamCase6() { //Case 6: Attending teams vary present late NoShow
		List<List<Team>> caseTeams = new ArrayList<>();

		for(int attendance = ATTENDING;attendance <= NOT_ATTENDING;attendance++) {
			caseTeams.add(new ArrayList<>());
			for(int i = 0;i < GENERATED_TEAMS;i++) {
				Team team = MockTeamGenerator.generateTeam();

				if(attendance == ATTENDING) {
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
                    int attendanceGenerator = i % 3;
                    if(attendanceGenerator == 0) {
                        team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.PRESENT);
                    } else if(attendanceGenerator == 1) {
                        team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.LATE);
                    } else {
                        team.getAttendanceCard().setAttendanceStatus(AttendanceStatus.NO_SHOW);
                    }
				} else { //NOT_ATTENDING
                    team.getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);
				}
				caseTeams.get(attendance).add(team);
			}
		}

		return caseTeams;
	}

	@Before
	public void genTeams() {
		//Case 0: All attending teams present
		//Case 1: All attending teams late
		//Case 2: All attending teams NoShow
		//Case 3: Attending teams vary present late
		//Case 4: Attending teams vary NoShow late
		//Case 5: Attending teams vary present NoShow
		//Case 6: Attending teams vary present late NoShow

		if(!this.initialized) {
			this.testTeams = new ArrayList<>();
			testTeams.add(genTeamCase0());
			testTeams.add(genTeamCase1());
			testTeams.add(genTeamCase2());
			testTeams.add(genTeamCase3());
			testTeams.add(genTeamCase4());
			testTeams.add(genTeamCase5());
			testTeams.add(genTeamCase6());
		}
		this.initialized = true;
	}

	//First index is ATTENDING or NOT_ATTENDING
	private List<List<Team>> getTeams(int amount, int testCase) { //17, i
		List<List<Team>> casedList = this.testTeams.get(testCase);

		if(amount >= casedList.get(ATTENDING).size()) {
			throw new RuntimeException("Bad amount in GetTeams (" + amount + " >= " + casedList.size() + ")") ;
		}

        List<List<Team>> result = new ArrayList<>();

		if(amount * 2 >= casedList.get(ATTENDING).size()) {
			result.add(new ArrayList<>(casedList.get(ATTENDING).subList(amount, casedList.get(ATTENDING).size()))); //17, 2
			result.get(ATTENDING).addAll(new ArrayList<>(casedList.get(ATTENDING).subList(0, (amount * 2) - casedList.get(ATTENDING).size())));
			result.add(new ArrayList<>(casedList.get(NOT_ATTENDING).subList(amount, casedList.get(NOT_ATTENDING).size())));
			result.get(NOT_ATTENDING).addAll(new ArrayList<>(casedList.get(NOT_ATTENDING).subList(0, (amount * 2) - casedList.get(NOT_ATTENDING).size())));
		} else {
			result.add(new ArrayList<>(casedList.get(ATTENDING).subList(amount, amount * 2)));
			result.add(new ArrayList<>(casedList.get(NOT_ATTENDING).subList(amount, amount * 2)));
		}

        for(int attendance = this.ATTENDING;attendance <= this.NOT_ATTENDING;attendance++) {
            for(int generatedRank = 0; generatedRank < result.get(attendance).size(); generatedRank++) {
                result.get(attendance).get(generatedRank).setLadderPosition(new LadderPosition(generatedRank + 1));
            }
        }

        return result;
	}

	@Test
	public void groupSize3Adjacent() throws SizeLimitExceededException { //Case 1: Groups are directly beside one another, groups of 3 only
		int teamCount = 9;
		List<List<Team>> expectedResults = new ArrayList<>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
			List<Team> sourceResults = getTeams(teamCount, i).get(ATTENDING);
			expectedResults.add(new ArrayList<>());
			if(i < 3) {
				expectedResults.get(i).add(sourceResults.get(2));
				expectedResults.get(i).add(sourceResults.get(1));
				expectedResults.get(i).add(sourceResults.get(4));
				expectedResults.get(i).add(sourceResults.get(0));
				expectedResults.get(i).add(sourceResults.get(3));
				expectedResults.get(i).add(sourceResults.get(6));
				expectedResults.get(i).add(sourceResults.get(5));
				expectedResults.get(i).add(sourceResults.get(7));
				expectedResults.get(i).add(sourceResults.get(8));
			} else if(i == 3) {
                expectedResults.get(i).add(sourceResults.get(1));
                expectedResults.get(i).add(sourceResults.get(3));
                expectedResults.get(i).add(sourceResults.get(5));
                expectedResults.get(i).add(sourceResults.get(7));
                expectedResults.get(i).add(sourceResults.get(2));
                expectedResults.get(i).add(sourceResults.get(4));
                expectedResults.get(i).add(sourceResults.get(0));
                expectedResults.get(i).add(sourceResults.get(6));
                expectedResults.get(i).add(sourceResults.get(8));
			} else if(i == 4) {
                expectedResults.get(i).add(sourceResults.get(2));
                expectedResults.get(i).add(sourceResults.get(0));
                expectedResults.get(i).add(sourceResults.get(4));
                expectedResults.get(i).add(sourceResults.get(6));
                expectedResults.get(i).add(sourceResults.get(8));
                expectedResults.get(i).add(sourceResults.get(1));
                expectedResults.get(i).add(sourceResults.get(3));
                expectedResults.get(i).add(sourceResults.get(5));
                expectedResults.get(i).add(sourceResults.get(7));
            } else if(i == 5) {
                expectedResults.get(i).add(sourceResults.get(1));
                expectedResults.get(i).add(sourceResults.get(3));
                expectedResults.get(i).add(sourceResults.get(5));
                expectedResults.get(i).add(sourceResults.get(7));
                expectedResults.get(i).add(sourceResults.get(2));
                expectedResults.get(i).add(sourceResults.get(0));
                expectedResults.get(i).add(sourceResults.get(4));
                expectedResults.get(i).add(sourceResults.get(6));
                expectedResults.get(i).add(sourceResults.get(8));
            } else {
                expectedResults.get(i).add(sourceResults.get(0));
                expectedResults.get(i).add(sourceResults.get(3));
                expectedResults.get(i).add(sourceResults.get(6));
                expectedResults.get(i).add(sourceResults.get(1));
                expectedResults.get(i).add(sourceResults.get(4));
                expectedResults.get(i).add(sourceResults.get(7));
                expectedResults.get(i).add(sourceResults.get(2));
                expectedResults.get(i).add(sourceResults.get(5));
                expectedResults.get(i).add(sourceResults.get(8));
            }
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			List<List<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			List<Team> ladderTestTeams = ladderTestTeamsSeg.get(ATTENDING);

			List<MatchGroup> ladderTestGroups = MatchGroupGenerator.generateMatchGroupings(ladderTestTeams, new ArrayList<>(), 3);

			//The following sets the 3 groups final scores to: 3-2-1, 2-1-3, 1-2-3
            List<Team> matchResults = new ArrayList<>();

            matchResults.add(ladderTestGroups.get(0).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(0));
            ladderTestGroups.get(0).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(1).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(2));
            ladderTestGroups.get(1).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(2).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(2));
            ladderTestGroups.get(2).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups);

			assertTrue(expectedResults.get(testCase).equals(testLadder.getLadder()));
		}
	}

	@Test
	public void groupSize4Adjacent() throws SizeLimitExceededException { //Case 2: Groups are directly beside one another, groups of 4 only
		int teamCount = 12;
		List<List<Team>> expectedResults = new ArrayList<>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
			List<Team> sourceResults = getTeams(teamCount, i).get(ATTENDING);
			expectedResults.add(new ArrayList<>());
			if(i < 3) {
				expectedResults.get(i).add(sourceResults.get(0));
				expectedResults.get(i).add(sourceResults.get(1));
				expectedResults.get(i).add(sourceResults.get(2));
				expectedResults.get(i).add(sourceResults.get(6));
				expectedResults.get(i).add(sourceResults.get(3));
				expectedResults.get(i).add(sourceResults.get(7));
				expectedResults.get(i).add(sourceResults.get(5));
				expectedResults.get(i).add(sourceResults.get(9));
				expectedResults.get(i).add(sourceResults.get(4));
				expectedResults.get(i).add(sourceResults.get(8));
				expectedResults.get(i).add(sourceResults.get(10));
				expectedResults.get(i).add(sourceResults.get(11));
			} else if(i == 3) {
                expectedResults.get(i).add(sourceResults.get(0));
                expectedResults.get(i).add(sourceResults.get(2));
                expectedResults.get(i).add(sourceResults.get(6));
                expectedResults.get(i).add(sourceResults.get(4));
                expectedResults.get(i).add(sourceResults.get(8));
                expectedResults.get(i).add(sourceResults.get(1));
                expectedResults.get(i).add(sourceResults.get(10));
                expectedResults.get(i).add(sourceResults.get(3));
                expectedResults.get(i).add(sourceResults.get(7));
                expectedResults.get(i).add(sourceResults.get(5));
                expectedResults.get(i).add(sourceResults.get(9));
                expectedResults.get(i).add(sourceResults.get(11));
            } else if(i == 4) {
                expectedResults.get(i).add(sourceResults.get(1));
                expectedResults.get(i).add(sourceResults.get(3));
                expectedResults.get(i).add(sourceResults.get(7));
                expectedResults.get(i).add(sourceResults.get(5));
                expectedResults.get(i).add(sourceResults.get(9));
                expectedResults.get(i).add(sourceResults.get(11));
                expectedResults.get(i).add(sourceResults.get(0));
                expectedResults.get(i).add(sourceResults.get(2));
                expectedResults.get(i).add(sourceResults.get(6));
                expectedResults.get(i).add(sourceResults.get(4));
                expectedResults.get(i).add(sourceResults.get(8));
                expectedResults.get(i).add(sourceResults.get(10));
			} else if(i == 5) {
                expectedResults.get(i).add(sourceResults.get(0));
                expectedResults.get(i).add(sourceResults.get(2));
                expectedResults.get(i).add(sourceResults.get(6));
                expectedResults.get(i).add(sourceResults.get(4));
                expectedResults.get(i).add(sourceResults.get(8));
                expectedResults.get(i).add(sourceResults.get(10));
                expectedResults.get(i).add(sourceResults.get(1));
                expectedResults.get(i).add(sourceResults.get(3));
                expectedResults.get(i).add(sourceResults.get(7));
                expectedResults.get(i).add(sourceResults.get(5));
                expectedResults.get(i).add(sourceResults.get(9));
                expectedResults.get(i).add(sourceResults.get(11));
            } else if(i == 6) {
                expectedResults.get(i).add(sourceResults.get(0));
                expectedResults.get(i).add(sourceResults.get(3));
                expectedResults.get(i).add(sourceResults.get(6));
                expectedResults.get(i).add(sourceResults.get(9));
                expectedResults.get(i).add(sourceResults.get(1));
                expectedResults.get(i).add(sourceResults.get(7));
                expectedResults.get(i).add(sourceResults.get(4));
                expectedResults.get(i).add(sourceResults.get(10));
                expectedResults.get(i).add(sourceResults.get(2));
                expectedResults.get(i).add(sourceResults.get(5));
                expectedResults.get(i).add(sourceResults.get(8));
                expectedResults.get(i).add(sourceResults.get(11));
            }
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			List<List<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			List<Team> ladderTestTeams = ladderTestTeamsSeg.get(ATTENDING);
			List<Team> sourceResults = getTeams(teamCount, testCase).get(ATTENDING);

			List<MatchGroup> ladderTestGroups = new ArrayList<>();
			ladderTestGroups.add(new MatchGroup(sourceResults.get(0), sourceResults.get(1), sourceResults.get(2), sourceResults.get(3)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(4), sourceResults.get(5), sourceResults.get(6), sourceResults.get(7)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(8), sourceResults.get(9), sourceResults.get(10), sourceResults.get(11)));

			//The following sets the 3 groups final scores to: 1-2-3-4, 3-4-2-1, 2-1-3-4
            List<Team> matchResults = new ArrayList<>();

            matchResults.add(ladderTestGroups.get(0).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(3));
            ladderTestGroups.get(0).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(1).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(3));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(0));
            ladderTestGroups.get(1).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(2).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(3));
            ladderTestGroups.get(2).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups);

            assertTrue(expectedResults.get(testCase).equals(testLadder.getLadder()));
		}
	}

	@Test
	public void groupSize34Adjacent() throws SizeLimitExceededException { //Case 3: Groups are directly beside one another, groups of 3/4, not predictably interchanging (At least one of each)

		int teamCount = 17;
		List<List<Team>> expectedResults = new ArrayList<>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
			List<Team> sourceResults = getTeams(teamCount, i).get(ATTENDING);
			expectedResults.add(new ArrayList<>());
			if(i < 3) {
				expectedResults.get(i).add(sourceResults.get(1));
				expectedResults.get(i).add(sourceResults.get(3));
				expectedResults.get(i).add(sourceResults.get(0));
				expectedResults.get(i).add(sourceResults.get(6));
				expectedResults.get(i).add(sourceResults.get(2));
				expectedResults.get(i).add(sourceResults.get(4));
				expectedResults.get(i).add(sourceResults.get(9));
				expectedResults.get(i).add(sourceResults.get(5));
				expectedResults.get(i).add(sourceResults.get(8));
				expectedResults.get(i).add(sourceResults.get(13));
				expectedResults.get(i).add(sourceResults.get(7));
				expectedResults.get(i).add(sourceResults.get(12));
				expectedResults.get(i).add(sourceResults.get(11));
				expectedResults.get(i).add(sourceResults.get(15));
				expectedResults.get(i).add(sourceResults.get(10));
				expectedResults.get(i).add(sourceResults.get(14));
				expectedResults.get(i).add(sourceResults.get(16));
			} else if(i == 3) {
				expectedResults.get(i).add(sourceResults.get(1));
				expectedResults.get(i).add(sourceResults.get(3));
				expectedResults.get(i).add(sourceResults.get(9));
				expectedResults.get(i).add(sourceResults.get(5));
				expectedResults.get(i).add(sourceResults.get(13));
				expectedResults.get(i).add(sourceResults.get(7));
				expectedResults.get(i).add(sourceResults.get(0));
				expectedResults.get(i).add(sourceResults.get(6));
				expectedResults.get(i).add(sourceResults.get(2));
				expectedResults.get(i).add(sourceResults.get(4));
				expectedResults.get(i).add(sourceResults.get(11));
				expectedResults.get(i).add(sourceResults.get(15));
				expectedResults.get(i).add(sourceResults.get(8));
				expectedResults.get(i).add(sourceResults.get(12));
				expectedResults.get(i).add(sourceResults.get(10));
				expectedResults.get(i).add(sourceResults.get(14));
				expectedResults.get(i).add(sourceResults.get(16));
			} else if(i == 4) {
                expectedResults.get(i).add(sourceResults.get(0));
                expectedResults.get(i).add(sourceResults.get(2));
                expectedResults.get(i).add(sourceResults.get(6));
                expectedResults.get(i).add(sourceResults.get(4));
                expectedResults.get(i).add(sourceResults.get(8));
                expectedResults.get(i).add(sourceResults.get(12));
                expectedResults.get(i).add(sourceResults.get(10));
                expectedResults.get(i).add(sourceResults.get(14));
                expectedResults.get(i).add(sourceResults.get(16));
                expectedResults.get(i).add(sourceResults.get(1));
                expectedResults.get(i).add(sourceResults.get(3));
                expectedResults.get(i).add(sourceResults.get(5));
                expectedResults.get(i).add(sourceResults.get(9));
                expectedResults.get(i).add(sourceResults.get(7));
                expectedResults.get(i).add(sourceResults.get(13));
                expectedResults.get(i).add(sourceResults.get(11));
                expectedResults.get(i).add(sourceResults.get(15));
			} else if(i == 5) {
				expectedResults.get(i).add(sourceResults.get(1));
				expectedResults.get(i).add(sourceResults.get(3));
				expectedResults.get(i).add(sourceResults.get(5));
				expectedResults.get(i).add(sourceResults.get(9));
				expectedResults.get(i).add(sourceResults.get(7));
				expectedResults.get(i).add(sourceResults.get(13));
				expectedResults.get(i).add(sourceResults.get(11));
				expectedResults.get(i).add(sourceResults.get(15));
				expectedResults.get(i).add(sourceResults.get(0));
				expectedResults.get(i).add(sourceResults.get(2));
				expectedResults.get(i).add(sourceResults.get(6));
				expectedResults.get(i).add(sourceResults.get(4));
				expectedResults.get(i).add(sourceResults.get(8));
				expectedResults.get(i).add(sourceResults.get(12));
				expectedResults.get(i).add(sourceResults.get(10));
				expectedResults.get(i).add(sourceResults.get(14));
				expectedResults.get(i).add(sourceResults.get(16));
			} else {
                expectedResults.get(i).add(sourceResults.get(1));
                expectedResults.get(i).add(sourceResults.get(4));
                expectedResults.get(i).add(sourceResults.get(7));
                expectedResults.get(i).add(sourceResults.get(13));
                expectedResults.get(i).add(sourceResults.get(10));
                expectedResults.get(i).add(sourceResults.get(2));
                expectedResults.get(i).add(sourceResults.get(16));
                expectedResults.get(i).add(sourceResults.get(5));
                expectedResults.get(i).add(sourceResults.get(8));
                expectedResults.get(i).add(sourceResults.get(11));
                expectedResults.get(i).add(sourceResults.get(14));
                expectedResults.get(i).add(sourceResults.get(3));
                expectedResults.get(i).add(sourceResults.get(0));
                expectedResults.get(i).add(sourceResults.get(6));
                expectedResults.get(i).add(sourceResults.get(9));
                expectedResults.get(i).add(sourceResults.get(12));
                expectedResults.get(i).add(sourceResults.get(15));
            }
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			List<List<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			List<Team> ladderTestTeams = ladderTestTeamsSeg.get(ATTENDING);
			List<Team> sourceResults = getTeams(teamCount, testCase).get(ATTENDING);

			List<MatchGroup> ladderTestGroups = new ArrayList<>();
			ladderTestGroups.add(new MatchGroup(sourceResults.get(0), sourceResults.get(1), sourceResults.get(2), sourceResults.get(3)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(4), sourceResults.get(5), sourceResults.get(6)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(7), sourceResults.get(8), sourceResults.get(9)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(10), sourceResults.get(11), sourceResults.get(12), sourceResults.get(13)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(14), sourceResults.get(15), sourceResults.get(16)));

			//The following sets the 5 groups final scores to: 2-4-1-3, 3-1-2, 3-2-1, 4-3-2-1, 2-1-3
            List<Team> matchResults = new ArrayList<>();

            matchResults.add(ladderTestGroups.get(0).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(3));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(2));
            ladderTestGroups.get(0).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(1).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(1));
            ladderTestGroups.get(1).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(2).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(0));
            ladderTestGroups.get(2).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(3).getTeams().get(3));
            matchResults.add(ladderTestGroups.get(3).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(3).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(3).getTeams().get(0));
            ladderTestGroups.get(3).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(4).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(4).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(4).getTeams().get(2));
            ladderTestGroups.get(4).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            System.out.println("Test Case #" + testCase + ":");
            for(int i = 0;i < expectedResults.get(testCase).size();i++) {
                User firstPlayer = expectedResults.get(testCase).get(i).getFirstPlayer();
                User secondPlayer = expectedResults.get(testCase).get(i).getSecondPlayer();
                System.out.println(i + ": " + firstPlayer.getFirstName() + " " + firstPlayer.getLastName() + ", " + secondPlayer.getFirstName() + " " + secondPlayer.getLastName());
            }

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups);

            assertTrue(expectedResults.get(testCase).equals(testLadder.getLadder()));
		}
	}

	@Test
	public void groupSize3NonAdjacent() throws SizeLimitExceededException { //Case 4: Groups have non-attending teams between them, groups of 3 only
		int teamCount = 6;
		List<List<Team>> expectedResults = new ArrayList<>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
			List<Team> sourceResults1 = getTeams(teamCount, i).get(ATTENDING);
			List<Team> sourceResults2 = getTeams(teamCount, i).get(NOT_ATTENDING);
			expectedResults.add(new ArrayList<>());
			switch(i) {
				case(0):
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults2.get(2));
                    break;
				case(1):
					expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    break;
				case(2):
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(5));
                    break;
				case(3):
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    break;
				case(4):
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    break;
				case(5):
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(5));
                    break;
				case(6):
					expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    break;
			}
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			List<List<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			List<Team> ladderTestTeams = new ArrayList<>();

			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(4));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(5));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(2));

			List<MatchGroup> ladderTestGroups = MatchGroupGenerator.generateMatchGroupings(ladderTestTeams, new ArrayList<>(), 2);

			//The following sets the 2 groups final scores to: 3-2-1, 1-2-3
            List<Team> matchResults = new ArrayList<>();

            matchResults.add(ladderTestGroups.get(0).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(0));
            ladderTestGroups.get(0).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(1).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(2));
            ladderTestGroups.get(1).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            System.out.println("Test Case #" + testCase + ":");
            for(int i = 0;i < expectedResults.get(testCase).size();i++) {
                User firstPlayer = expectedResults.get(testCase).get(i).getFirstPlayer();
                User secondPlayer = expectedResults.get(testCase).get(i).getSecondPlayer();
                System.out.println(i + ": " + firstPlayer.getFirstName() + " " + firstPlayer.getLastName() + ", " + secondPlayer.getFirstName() + " " + secondPlayer.getLastName());
            }

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups);

            System.out.println("\nResults:");
            System.out.println(testLadder.toString());

            assertTrue(expectedResults.get(testCase).equals(testLadder.getLadder()));
		}
	}

	@Test
	public void groupSize4NonAdjacent() throws SizeLimitExceededException { //Case 5: Groups have non-attending teams between them, groups of 4 only
        int teamCount = 8;
		List<List<Team>> expectedResults = new ArrayList<>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
			List<Team> sourceResults1 = getTeams(teamCount, i).get(ATTENDING);
			List<Team> sourceResults2 = getTeams(teamCount, i).get(NOT_ATTENDING);
			expectedResults.add(new ArrayList<>());
			switch(i) {
				case(0):
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
                    break;
				case(1):
					expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    expectedResults.get(i).add(sourceResults1.get(7));
                    expectedResults.get(i).add(sourceResults1.get(6));
                    break;
				case(2):
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(6));
                    break;
				case(3):
					expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults1.get(6));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    expectedResults.get(i).add(sourceResults1.get(7));
                    break;
				case(4):
					expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    expectedResults.get(i).add(sourceResults1.get(7));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults1.get(6));
                    break;
				case(5):
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(7));
                    break;
				case(6):
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults1.get(7));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(6));
                    break;
			}
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			List<List<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			List<Team> ladderTestTeams = new ArrayList<>();

			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(4));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(5));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(6));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(7));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(3));

			List<MatchGroup> ladderTestGroups = MatchGroupGenerator.generateMatchGroupings(ladderTestTeams, new ArrayList<>(), 2);

			//The following sets the 2 groups final scores to: 3-1-2-4, 1-2-4-3
            List<Team> matchResults = new ArrayList<>();

            matchResults.add(ladderTestGroups.get(0).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(3));
            ladderTestGroups.get(0).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(1).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(3));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(2));
            ladderTestGroups.get(1).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            System.out.println("Test Case #" + testCase + ":");
            for(int i = 0;i < expectedResults.get(testCase).size();i++) {
                User firstPlayer = expectedResults.get(testCase).get(i).getFirstPlayer();
                User secondPlayer = expectedResults.get(testCase).get(i).getSecondPlayer();
                System.out.println(i + ": " + firstPlayer.getFirstName() + " " + firstPlayer.getLastName() + ", " + secondPlayer.getFirstName() + " " + secondPlayer.getLastName());
            }

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups);

            System.out.println("\nResults:");
            System.out.println(testLadder.toString());

            assertTrue(expectedResults.get(testCase).equals(testLadder.getLadder()));
		}
	}

	@Test
	public void groupSize3SingleWithFriends() throws SizeLimitExceededException { //Case 6: Single group of 3, other non-attending teams around
		int teamCount = 5;
		List<List<Team>> expectedResults = new ArrayList<>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
			List<Team> sourceResults1 = getTeams(teamCount, i).get(ATTENDING);
			List<Team> sourceResults2 = getTeams(teamCount, i).get(NOT_ATTENDING);
			expectedResults.add(new ArrayList<>());
			switch(i) {
				case(0):
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    break;
				case(1):
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    break;
				case(2):
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    break;
				case(3):
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    break;
				case(4):
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    break;
				case(5):
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    break;
				case(6):
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    break;
			}
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			List<List<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			List<Team> ladderTestTeams = new ArrayList<>();
			List<Team> sourceResults = getTeams(teamCount, testCase).get(ATTENDING);

			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(4));

			List<MatchGroup> ladderTestGroups = new ArrayList<>();
			ladderTestGroups.add(new MatchGroup(sourceResults.get(0), sourceResults.get(1), sourceResults.get(2)));

			//The following sets the group final scores to: 2-1-3
            List<Team> matchResults = new ArrayList<>();

            matchResults.add(ladderTestGroups.get(0).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(2));
            ladderTestGroups.get(0).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

			Ladder testLadder = new Ladder(ladderTestTeams);
            testLadder.updateLadder(ladderTestGroups);

            assertTrue(expectedResults.get(testCase).equals(testLadder.getLadder()));
		}
	}

	@Test
	public void groupSize4SingleWithFriends() throws SizeLimitExceededException { //Case 7: Single group of 4, other non-attending teams around
		int teamCount = 7;
		List<List<Team>> expectedResults = new ArrayList<>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
			List<Team> sourceResults1 = getTeams(teamCount, i).get(ATTENDING);
			List<Team> sourceResults2 = getTeams(teamCount, i).get(NOT_ATTENDING);
			expectedResults.add(new ArrayList<>());
			switch(i) {
				case(0):
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    break;
				case(1):
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    break;
				case(2):
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    break;
				case(3):
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    break;
				case(4):
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    break;
				case(5):
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    break;
				case(6):
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    break;
			}
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			List<List<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			List<Team> ladderTestTeams = new ArrayList<>();
			List<Team> sourceResults = getTeams(teamCount, testCase).get(ATTENDING);

			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(4));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(5));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(6));

			List<MatchGroup> ladderTestGroups = new ArrayList<>();
			ladderTestGroups.add(new MatchGroup(sourceResults.get(0), sourceResults.get(1), sourceResults.get(2), sourceResults.get(3)));

			//The following sets the groups final score to: 3-4-2-1
            List<Team> matchResults = new ArrayList<>();

            matchResults.add(ladderTestGroups.get(0).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(3));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(0));
            ladderTestGroups.get(0).getScoreCard().setRankedTeams(matchResults);
            for(int i = 0;i < ladderTestGroups.get(0).getScoreCard().getRankedTeams().size();i++) {
                User firstPlayer = ladderTestGroups.get(0).getScoreCard().getRankedTeams().get(i).getFirstPlayer();
                User secondPlayer = ladderTestGroups.get(0).getScoreCard().getRankedTeams().get(i).getSecondPlayer();
                System.out.println(i + ": " + firstPlayer.getFirstName() + " " + firstPlayer.getLastName() + ", " + secondPlayer.getFirstName() + " " + secondPlayer.getLastName());
            }
            matchResults.clear();

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups);

            assertTrue(expectedResults.get(testCase).equals(testLadder.getLadder()));
		}
	}

	@Test
	public void groupSize3NonAdjacentAllIntersecting() throws SizeLimitExceededException { //Case 8: Groups have non-attending teams beside and inside them, groups of 3 only, all overlapping
		int teamCount = 13;
		List<List<Team>> expectedResults = new ArrayList<>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
			List<Team> sourceResults1 = getTeams(teamCount, i).get(ATTENDING);
			List<Team> sourceResults2 = getTeams(teamCount, i).get(NOT_ATTENDING);
			expectedResults.add(new ArrayList<>());
			switch(i) {
				case (0):
					expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults1.get(7));
                    expectedResults.get(i).add(sourceResults1.get(6));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults2.get(7));
                    expectedResults.get(i).add(sourceResults1.get(8));
                    expectedResults.get(i).add(sourceResults2.get(8));
                    expectedResults.get(i).add(sourceResults2.get(9));
                    expectedResults.get(i).add(sourceResults2.get(10));
                    expectedResults.get(i).add(sourceResults2.get(11));
                    expectedResults.get(i).add(sourceResults2.get(12));
                    break;
				case (1):
					expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults2.get(7));
                    expectedResults.get(i).add(sourceResults2.get(8));
                    expectedResults.get(i).add(sourceResults1.get(7));
                    expectedResults.get(i).add(sourceResults2.get(9));
                    expectedResults.get(i).add(sourceResults2.get(10));
                    expectedResults.get(i).add(sourceResults2.get(11));
                    expectedResults.get(i).add(sourceResults1.get(6));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults2.get(12));
                    expectedResults.get(i).add(sourceResults1.get(8));
                    break;
				case (2):
					expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults2.get(7));
                    expectedResults.get(i).add(sourceResults2.get(8));
                    expectedResults.get(i).add(sourceResults2.get(9));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(10));
                    expectedResults.get(i).add(sourceResults2.get(11));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    expectedResults.get(i).add(sourceResults2.get(12));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults1.get(7));
                    expectedResults.get(i).add(sourceResults1.get(6));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults1.get(8));
                    break;
				case (3):
					expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults2.get(7));
                    expectedResults.get(i).add(sourceResults2.get(8));
                    expectedResults.get(i).add(sourceResults1.get(7));
                    expectedResults.get(i).add(sourceResults2.get(9));
                    expectedResults.get(i).add(sourceResults2.get(10));
                    expectedResults.get(i).add(sourceResults2.get(11));
                    expectedResults.get(i).add(sourceResults1.get(6));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults2.get(12));
                    expectedResults.get(i).add(sourceResults1.get(8));
                    break;
				case (4):
					expectedResults.get(i).add(sourceResults2.get(0));
                    expectedResults.get(i).add(sourceResults2.get(1));
                    expectedResults.get(i).add(sourceResults2.get(2));
                    expectedResults.get(i).add(sourceResults2.get(3));
                    expectedResults.get(i).add(sourceResults1.get(4));
                    expectedResults.get(i).add(sourceResults2.get(4));
                    expectedResults.get(i).add(sourceResults2.get(5));
                    expectedResults.get(i).add(sourceResults2.get(6));
                    expectedResults.get(i).add(sourceResults1.get(2));
                    expectedResults.get(i).add(sourceResults2.get(7));
                    expectedResults.get(i).add(sourceResults1.get(0));
                    expectedResults.get(i).add(sourceResults1.get(1));
                    expectedResults.get(i).add(sourceResults2.get(8));
                    expectedResults.get(i).add(sourceResults2.get(9));
                    expectedResults.get(i).add(sourceResults2.get(10));
                    expectedResults.get(i).add(sourceResults2.get(11));
                    expectedResults.get(i).add(sourceResults1.get(3));
                    expectedResults.get(i).add(sourceResults2.get(12));
                    expectedResults.get(i).add(sourceResults1.get(6));
                    expectedResults.get(i).add(sourceResults1.get(8));
                    expectedResults.get(i).add(sourceResults1.get(7));
                    expectedResults.get(i).add(sourceResults1.get(5));
                    break;
				case (5):
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(11));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults2.get(12));
                    break;
				case (6):
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults2.get(11));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(12));
                    break;
			}
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			List<List<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			List<Team> ladderTestTeams = new ArrayList<>();
			List<Team> sourceResults = getTeams(teamCount, testCase).get(ATTENDING);

			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(4));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(4));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(5));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(5));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(6));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(7));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(6));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(7));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(8));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(8));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(9));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(10));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(11));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(12));

			List<MatchGroup> ladderTestGroups = new ArrayList<>();
			ladderTestGroups.add(new MatchGroup(sourceResults.get(0), sourceResults.get(1), sourceResults.get(4)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(3), sourceResults.get(5), sourceResults.get(7)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(2), sourceResults.get(6), sourceResults.get(8)));

			//The following sets the 3 groups final scores to: 3-2-1, 1-3-2, 1-2-3
            List<Team> matchResults = new ArrayList<>();

            matchResults.add(ladderTestGroups.get(0).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(0));
            ladderTestGroups.get(0).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(1).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(1));
            ladderTestGroups.get(1).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(2).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(2));
            ladderTestGroups.get(2).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            System.out.println("Test Case #" + testCase + ":");
            for(int i = 0;i < expectedResults.get(testCase).size();i++) {
                User firstPlayer = expectedResults.get(testCase).get(i).getFirstPlayer();
                User secondPlayer = expectedResults.get(testCase).get(i).getSecondPlayer();
                System.out.println(i + ": " + firstPlayer.getFirstName() + " " + firstPlayer.getLastName() + ", " + secondPlayer.getFirstName() + " " + secondPlayer.getLastName());
            }

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups);

            System.out.println("\nResults:");
            System.out.println(testLadder.toString());

            assertTrue(expectedResults.get(testCase).equals(testLadder.getLadder()));
		}
	}

	@Test
	public void groupSize4NonAdjacentAllIntersecting() throws SizeLimitExceededException { //Case 9: Groups have non-attending teams beside and inside them, groups of 4 only, all overlapping
		int teamCount = 13;
		List<List<Team>> expectedResults = new ArrayList<>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
			List<Team> sourceResults1 = getTeams(teamCount, i).get(ATTENDING);
			List<Team> sourceResults2 = getTeams(teamCount, i).get(NOT_ATTENDING);
			expectedResults.add(new ArrayList<>());
			switch(i) {
				case (0):
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults1.get(10));
					expectedResults.get(i).add(sourceResults1.get(11));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults2.get(11));
					expectedResults.get(i).add(sourceResults2.get(12));
                    break;
				case (1):
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults2.get(11));
					expectedResults.get(i).add(sourceResults1.get(10));
					expectedResults.get(i).add(sourceResults1.get(11));
					expectedResults.get(i).add(sourceResults2.get(12));
					expectedResults.get(i).add(sourceResults1.get(8));
                    break;
				case (2):
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(10));
					expectedResults.get(i).add(sourceResults1.get(11));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults2.get(11));
					expectedResults.get(i).add(sourceResults2.get(12));
                    break;
				case (3):
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(10));
					expectedResults.get(i).add(sourceResults1.get(11));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults2.get(11));
					expectedResults.get(i).add(sourceResults2.get(12));
                    break;
				case (4):
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(10));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults1.get(11));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(11));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults2.get(12));
                    break;
				case (5):
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(10));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults1.get(11));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(11));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults2.get(12));
                    break;
				case (6):
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults1.get(11));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults1.get(10));
					expectedResults.get(i).add(sourceResults2.get(11));
					expectedResults.get(i).add(sourceResults2.get(12));
					expectedResults.get(i).add(sourceResults1.get(8));
                    break;
			}
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			List<List<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			List<Team> ladderTestTeams = new ArrayList<>();
			List<Team> sourceResults = getTeams(teamCount, testCase).get(ATTENDING);

			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(4));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(5));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(4));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(6));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(5));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(6));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(7));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(7));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(8));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(8));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(9));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(9));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(10));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(11));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(10));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(11));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(12));

			List<MatchGroup> ladderTestGroups = new ArrayList<>();
			ladderTestGroups.add(new MatchGroup(sourceResults.get(0), sourceResults.get(4), sourceResults.get(6), sourceResults.get(7)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(1), sourceResults.get(9), sourceResults.get(10), sourceResults.get(11)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(2), sourceResults.get(3), sourceResults.get(5), sourceResults.get(8)));

			//The following sets the 3 groups final scores to: 4-1-2-3, 2-3-4-1, 4-2-3-1
            List<Team> matchResults = new ArrayList<>();

            matchResults.add(ladderTestGroups.get(0).getTeams().get(3));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(2));
            ladderTestGroups.get(0).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(1).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(3));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(0));
            ladderTestGroups.get(1).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(2).getTeams().get(3));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(0));
            ladderTestGroups.get(2).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            System.out.println("Test Case #" + testCase + ":");
            for(int i = 0;i < expectedResults.get(testCase).size();i++) {
                User firstPlayer = expectedResults.get(testCase).get(i).getFirstPlayer();
                User secondPlayer = expectedResults.get(testCase).get(i).getSecondPlayer();
                System.out.println(i + ": " + firstPlayer.getFirstName() + " " + firstPlayer.getLastName() + ", " + secondPlayer.getFirstName() + " " + secondPlayer.getLastName());
            }

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups);

            System.out.println("\nResults:");
            System.out.println(testLadder.toString());

            assertTrue(expectedResults.get(testCase).equals(testLadder.getLadder()));
		}
	}

	@Test
	public void groupSize34NonAdjacentAllIntersecting() throws SizeLimitExceededException { //Case 10: Groups have non-attending teams beside and inside them, groups of 3/4, all overlapping, not predictably interchanging (At least one of each)
		int teamCount = 11;
		List<List<Team>> expectedResults = new ArrayList<>();
		for(int i = 0;i < TEST_CASE_COUNT;i++) {
			List<Team> sourceResults1 = getTeams(teamCount, i).get(ATTENDING);
			List<Team> sourceResults2 = getTeams(teamCount, i).get(NOT_ATTENDING);
			expectedResults.add(new ArrayList<>());
			switch(i) {
				case (0):
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
                    break;
				case (1):
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(8));
                    break;
				case (2):
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
                    break;
				case (3):
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
                    break;
				case (4):
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(10));
                    break;
				case (5):
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(8));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults2.get(10));
                    break;
				case (6):
					expectedResults.get(i).add(sourceResults1.get(1));
					expectedResults.get(i).add(sourceResults2.get(0));
					expectedResults.get(i).add(sourceResults1.get(7));
					expectedResults.get(i).add(sourceResults1.get(5));
					expectedResults.get(i).add(sourceResults1.get(0));
					expectedResults.get(i).add(sourceResults2.get(1));
					expectedResults.get(i).add(sourceResults1.get(3));
					expectedResults.get(i).add(sourceResults1.get(9));
					expectedResults.get(i).add(sourceResults2.get(2));
					expectedResults.get(i).add(sourceResults2.get(3));
					expectedResults.get(i).add(sourceResults2.get(4));
					expectedResults.get(i).add(sourceResults2.get(5));
					expectedResults.get(i).add(sourceResults1.get(6));
					expectedResults.get(i).add(sourceResults2.get(6));
					expectedResults.get(i).add(sourceResults2.get(7));
					expectedResults.get(i).add(sourceResults2.get(8));
					expectedResults.get(i).add(sourceResults1.get(2));
					expectedResults.get(i).add(sourceResults2.get(9));
					expectedResults.get(i).add(sourceResults2.get(10));
					expectedResults.get(i).add(sourceResults1.get(4));
					expectedResults.get(i).add(sourceResults1.get(8));
                    break;
			}
		}

		for(int testCase = 0;testCase < TEST_CASE_COUNT;testCase++) {
			List<List<Team>> ladderTestTeamsSeg = getTeams(teamCount, testCase);
			List<Team> ladderTestTeams = new ArrayList<>();
			List<Team> sourceResults = getTeams(teamCount, testCase).get(ATTENDING);

			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(0));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(1));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(4));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(2));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(3));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(4));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(5));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(5));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(6));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(6));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(7));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(7));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(8));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(8));
			ladderTestTeams.add(ladderTestTeamsSeg.get(ATTENDING).get(9));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(9));
			ladderTestTeams.add(ladderTestTeamsSeg.get(NOT_ATTENDING).get(10));

			List<MatchGroup> ladderTestGroups = new ArrayList<>();
			ladderTestGroups.add(new MatchGroup(sourceResults.get(0), sourceResults.get(1), sourceResults.get(7)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(2), sourceResults.get(5), sourceResults.get(6), sourceResults.get(9)));
			ladderTestGroups.add(new MatchGroup(sourceResults.get(3), sourceResults.get(4), sourceResults.get(8)));

			//The following sets the 3 groups final scores to: 1-2-3, 1-4-3-2, 3-1-2
            List<Team> matchResults = new ArrayList<>();

            matchResults.add(ladderTestGroups.get(0).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(1));
            matchResults.add(ladderTestGroups.get(0).getTeams().get(2));
            ladderTestGroups.get(0).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(1).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(3));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(1).getTeams().get(1));
            ladderTestGroups.get(1).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            matchResults.add(ladderTestGroups.get(2).getTeams().get(2));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(0));
            matchResults.add(ladderTestGroups.get(2).getTeams().get(1));
            ladderTestGroups.get(2).getScoreCard().setRankedTeams(matchResults);
            matchResults.clear();

            System.out.println("Test Case #" + testCase + ":");
            for(int i = 0;i < expectedResults.get(testCase).size();i++) {
                User firstPlayer = expectedResults.get(testCase).get(i).getFirstPlayer();
                User secondPlayer = expectedResults.get(testCase).get(i).getSecondPlayer();
                System.out.println(i + ": " + firstPlayer.getFirstName() + " " + firstPlayer.getLastName() + ", " + secondPlayer.getFirstName() + " " + secondPlayer.getLastName());
            }

			Ladder testLadder = new Ladder(ladderTestTeams);
			testLadder.updateLadder(ladderTestGroups);

            System.out.println("\nResults:");
            System.out.println(testLadder.toString());

            assertTrue(expectedResults.get(testCase).equals(testLadder.getLadder()));
		}
	}
}