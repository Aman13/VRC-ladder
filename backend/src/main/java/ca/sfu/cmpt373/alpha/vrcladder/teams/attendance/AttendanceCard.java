package ca.sfu.cmpt373.alpha.vrcladder.teams.attendance;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = PersistenceConstants.TABLE_ATTENDANCE_CARD)
public class AttendanceCard {

    public static final int NOT_ATTENDING_PENALTY = 2;

    private static final PlayTime DEFAULT_PLAYTIME = PlayTime.NONE;
    private static final AttendanceStatus DEFAULT_ATTENDANCE_STATUS = AttendanceStatus.PRESENT;

    @EmbeddedId
    private GeneratedId id;

    @Enumerated(EnumType.STRING)
    @Column(name = PersistenceConstants.COLUMN_PLAY_TIME, nullable = false)
    private PlayTime preferredPlayTime;

    //TODO: add this to the database
    @Enumerated(EnumType.STRING)
    @Column(name = PersistenceConstants.COLUMN_ATTENDANCE_STATUS, nullable = false)
    private AttendanceStatus attendanceStatus;


    public AttendanceCard() {
        this.id = new GeneratedId();
        this.preferredPlayTime = DEFAULT_PLAYTIME;
        this.attendanceStatus = DEFAULT_ATTENDANCE_STATUS;
    }

    public GeneratedId getId() {
        return id;
    }

    public PlayTime getPreferredPlayTime() {
        return preferredPlayTime;
    }

    public void setPreferredPlayTime(PlayTime playTime) {
        preferredPlayTime = playTime;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public boolean isAttending() {
        return  (preferredPlayTime != PlayTime.NONE);
    }

    /**
     * @return whether or not the team did show up
     */
    public boolean isPresent() {
        return isAttending() && (attendanceStatus == AttendanceStatus.PRESENT ||
                attendanceStatus == AttendanceStatus.LATE);
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        AttendanceCard otherAttendanceCard = (AttendanceCard) otherObj;

        return id.equals(otherAttendanceCard.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}