package ca.sfu.cmpt373.alpha.vrcladder.db;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserName;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trevor on 5/26/2016.
 * A placeholder for the actual database to provide dummy data for implementation testing purposes
 */
public class MockDatabase {


    //TODO: make a User factory object to create instances of users so that we don't have to write code with lots of params and constructors like this!
    private static List<User> generateMockUsers (int numUsers) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < numUsers; i++) {
            users.add(new User(new UserId(i),
                    UserRole.PLAYER,
                    new UserName("FirstName" + i, "MiddleName" + i, "LastName" + 1),
                    new EmailAddress("Email" + i),
                    new PhoneNumber("" + i))
            );
        }
        return users;
    }

    private static List<Team> generateMockTeams(List<User> users) {
        List<Team> teams = new ArrayList<>();
        User previousUser = null;
        //ranking is just incremented after a team is made
        int mockRanking = 0;
        for (User user : users) {
            if (previousUser == null) {
                previousUser = user;
            } else {
                teams.add(new Team(new IdType(), user, mockRanking, previousUser, new AttendanceCard(new IdType(), PlayTime.TIME_SLOT_A)));
                previousUser = null;
                mockRanking++;
            }
        }

        if (previousUser != null) {
            System.out.println("Odd number of users. One user not sorted into a team");
        }

        return teams;
    }

    /**
     * @return  a list of teams in the order corresponding to team rankings on the ladder
     */
    public static List<Team> getRankedLadderTeams(int numTeams) {
        //number of users should be twice the number of teams since each team needs two users
        int numUsers = numTeams * 2;
        return generateMockTeams(generateMockUsers(numUsers));
    }
}