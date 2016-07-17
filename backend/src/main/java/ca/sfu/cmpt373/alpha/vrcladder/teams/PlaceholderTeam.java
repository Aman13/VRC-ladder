package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.CallToPlaceholderObjectException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;

public class PlaceholderTeam extends Team {
    public PlaceholderTeam() {
        super();
    }

    @Override
    public GeneratedId getId() {
        throw new CallToPlaceholderObjectException();
    }

    @Override
    public AttendanceCard getAttendanceCard() {
        throw new CallToPlaceholderObjectException();
    }

    @Override
    public User getFirstPlayer() {
        throw new CallToPlaceholderObjectException();
    }

    @Override
    public User getSecondPlayer() {
        throw new CallToPlaceholderObjectException();
    }

    @Override
    public LadderPosition getLadderPosition() {
        throw new CallToPlaceholderObjectException();
    }

    @Override
    public void setLadderPosition(LadderPosition ladderPosition) {
        throw new CallToPlaceholderObjectException();
    }

    @Override
    public boolean equals(Object otherObj) {
        throw new CallToPlaceholderObjectException();
    }

    @Override
    public int hashCode() {
        throw new CallToPlaceholderObjectException();
    }
}
