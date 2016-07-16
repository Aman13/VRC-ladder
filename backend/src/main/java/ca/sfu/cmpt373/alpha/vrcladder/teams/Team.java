package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = PersistenceConstants.TABLE_TEAM)
public class Team {

    @EmbeddedId
    private GeneratedId id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = PersistenceConstants.COLUMN_ATTENDANCE_CARD_ID, nullable = false)
    private AttendanceCard attendanceCard;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = PersistenceConstants.COLUMN_FIRST_PLAYER_ID, nullable = false)
    private User firstPlayer;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = PersistenceConstants.COLUMN_SECOND_PLAYER_ID, nullable = false)
    private User secondPlayer;

    @Embedded
    private LadderPosition ladderPosition;

    protected Team() {
        // Required by Hibernate and used by PlaceholderTeam
    }

    public Team(User firstPlayer, User secondPlayer, LadderPosition ladderPosition) {
        this.id = new GeneratedId();
        this.attendanceCard = new AttendanceCard();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.ladderPosition = ladderPosition;
    }

    public GeneratedId getId() {
        return id;
    }

    public AttendanceCard getAttendanceCard() {
        return attendanceCard;
    }

    public User getFirstPlayer() {
        return firstPlayer;
    }

    public User getSecondPlayer() {
        return secondPlayer;
    }

    public LadderPosition getLadderPosition() {
        return ladderPosition;
    }

    public void setLadderPosition(LadderPosition ladderPosition) {
        this.ladderPosition = ladderPosition;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        Team otherTeam = (Team) otherObj;

        return id.equals(otherTeam.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}