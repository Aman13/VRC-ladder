package ca.sfu.cmpt373.alpha.vrcladder.teams.attendance;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = PersistenceConstants.TABLE_ATTENDANCE_CARD)
public class AttendanceCard {

    private IdType id;
    private PlayTime preferredPlayTime;

    public AttendanceCard() {
        // Required by Hibernate
    }

    public AttendanceCard(PlayTime playTime) {
        this.id = new IdType();
        this.preferredPlayTime = playTime;
    }

    public AttendanceCard(IdType id, PlayTime playTime) {
        this.id = id;
        this.preferredPlayTime = playTime;
    }

    @Id
    @Column(name = PersistenceConstants.COLUMN_ID)
    public String getId() {
        return id.toString();
    }

    public void setId(String newId) {
        id = new IdType(newId);
    }

    @Enumerated(EnumType.STRING)
    @Column(name = PersistenceConstants.COLUMN_PLAY_TIME, nullable = false)
    public PlayTime getPreferredPlayTime() {
        return preferredPlayTime;
    }

    public void setPreferredPlayTime(PlayTime playTime) {
        preferredPlayTime = playTime;
    }

    @Transient
    public boolean isAttending() {
        return  (preferredPlayTime != PlayTime.NONE);
    }

}
