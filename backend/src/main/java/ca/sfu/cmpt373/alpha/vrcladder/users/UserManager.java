package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DuplicatePropertyException;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.logic.Email;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.Password;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcladder.util.CriterionConstants;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Provides an interface to perform create, read, update, and delete (CRUD) operations on
 * users in the database.
 *
 * TODO - Handle errors that can arise from User creation (i.e., non-unique email or user ID).
 */
public class UserManager extends DatabaseManager<User> {

    private static final Class USER_CLASS_TYPE = User.class;

    public UserManager(SessionManager sessionManager) {
        super(USER_CLASS_TYPE, sessionManager);
    }

    @Override
    public User create(User user) {
        return super.create(user);
    }

    public User create(UserId userId, UserRole userRole, String firstName, String lastName, EmailAddress emailAddress,
        PhoneNumber phoneNumber, Password password) {
        return create(userId, userRole, firstName, StringUtils.EMPTY, lastName, emailAddress, phoneNumber, password);
    }

    public User create(UserId userId, UserRole userRole, String firstName, String middleName, String lastName,
        EmailAddress emailAddress, PhoneNumber phoneNumber, Password password) {
        checkIfUserIdExists(userId);
        checkIfEmailExists(emailAddress);
        User createdUser = new UserBuilder()
            .setUserId(userId)
            .setUserRole(userRole)
            .setFirstName(firstName)
            .setMiddleName(middleName)
            .setLastName(lastName)
            .setEmailAddress(emailAddress)
            .setPhoneNumber(phoneNumber)
            .setPassword(password)
            .buildUser();
        create(createdUser);

        return createdUser;
    }

    public User update(UserId userId, String firstName, String middleName, String lastName, EmailAddress emailAddress,
        PhoneNumber phoneNumber, Password password) {
        User user = getById(userId);

        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setEmailAddress(emailAddress);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        return update(user);
    }

    public User updateAttempts(UserId userId, int attempts) {
        User user = getById(userId);
        user.setAttempts(attempts);
        return update(user);
    }

    @Override
    public User delete(User user) {
        if (user.getUserRole() == UserRole.PLAYER) {

            Session session = sessionManager.getSession();
            Transaction transaction = session.beginTransaction();

            List<Team> teams = getTeamsOfPlayer(user.getUserId());
            teams.forEach(session::delete);
            session.delete(user);

            transaction.commit();
            session.close();
        } else {
            super.delete(user);
        }

        return user;
    }

    @Override
    public User deleteById(IdType id) {
        User user = getById(id);
        delete(user);
        return user;
    }

    public List<User> getAllPlayers() {
        Session session = sessionManager.getSession();

        Criterion isPlayerCriterion = Restrictions.eq(CriterionConstants.USER_ROLE, UserRole.PLAYER);
        Criteria isPlayerCriteria = session.createCriteria(User.class).add(isPlayerCriterion);

        List<User> players = isPlayerCriteria.list();
        session.close();

        return players;
    }

    private List<Team> getTeamsOfPlayer(UserId playerId) {
        Session session = sessionManager.getSession();

        Criterion firstPlayerCriterion = Restrictions.eq(CriterionConstants.TEAM_FIRST_PLAYER_USER_ID_PROPERTY,
            playerId);
        Criterion secondPlayerCriterion = Restrictions.eq(CriterionConstants.TEAM_SECOND_PLAYER_USER_ID_PROPERTY,
            playerId);
        Criteria teamCriteria = session.createCriteria(Team.class)
            .add(Restrictions.or(firstPlayerCriterion, secondPlayerCriterion));

        List<Team> matchedTeams = teamCriteria.list();
        session.close();

        return matchedTeams;
    }

    private void checkIfUserIdExists(UserId userId) {
        Session session = sessionManager.getSession();

        Criterion sameUserIdCriterion = Restrictions.eq(CriterionConstants.USER_ID, userId);
        Criteria sameUserIdCriteria = session.createCriteria(User.class).add(sameUserIdCriterion);

        User existingUser = (User) sameUserIdCriteria.uniqueResult();
        session.close();

        if (existingUser != null) {
            throw new DuplicatePropertyException(PersistenceConstants.COLUMN_ID);
        }
    }

    private void checkIfEmailExists(EmailAddress emailAddress) {
        Session session = sessionManager.getSession();
        Criterion sameUserIdCriterion = Restrictions.eq(CriterionConstants.USER_EMAIL_ADDRESS, emailAddress);
        Criteria sameUserIdCriteria = session.createCriteria(User.class).add(sameUserIdCriterion);

        User existingUser = (User) sameUserIdCriteria.uniqueResult();
        session.close();

        if (existingUser != null) {
            throw new DuplicatePropertyException(PersistenceConstants.COLUMN_EMAIL_ADDRESS);
        }
    }

}
