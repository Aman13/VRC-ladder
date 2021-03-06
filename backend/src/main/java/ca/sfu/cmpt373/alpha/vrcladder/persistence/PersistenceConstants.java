package ca.sfu.cmpt373.alpha.vrcladder.persistence;

public class PersistenceConstants {

    public static final String COLUMN_ID = "ID";

    public static final String TABLE_USER = "USER";
    public static final String COLUMN_USER_ROLE = "USER_ROLE";
    public static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    public static final String COLUMN_MIDDLE_NAME = "MIDDLE_NAME";
    public static final String COLUMN_LAST_NAME = "LAST_NAME";
    public static final String COLUMN_EMAIL_ADDRESS = "EMAIL_ADDRESS";
    public static final String COLUMN_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String COLUMN_PASSWORD_HASH = "PASSWORD_HASH";
    public static final String COLUMN_FAILED_ATTEMPTS = "ATTEMPTS";

    public static final String TABLE_TEAM = "TEAM";
    public static final String COLUMN_ATTENDANCE_CARD_ID = "ATTENDANCE_CARD_ID";
    public static final String COLUMN_FIRST_PLAYER_ID = "FIRST_PLAYER_ID";
    public static final String COLUMN_SECOND_PLAYER_ID = "SECOND_PLAYER_ID";
    public static final String COLUMN_LADDER_POSITION = "LADDER_POSITION";
    public static final String COLUMN_ATTENDANCE_STATUS = "ATTENDANCE_STATUS";

    public static final int MAX_NAME_LENGTH = 40;
    public static final String TABLE_ATTENDANCE_CARD = "ATTENDANCE_CARD";
    public static final String COLUMN_PLAY_TIME = "PLAY_TIME";

    public static final String TABLE_MATCH_GROUP = "MATCH_GROUP";

    public static final String TABLE_COURT = "COURT";
    public static final String INVALID_PROPERTY_VALUE = "invalid";
    public static final String EXISTING_USER_ID = "User ID already exists";
    public static final String EXISTING_USER_EMAIL = "Email Address already exists";
    public static final String CONSTRAINT_CONFLICT_ID = "PUBLIC.USER(ID)";
    public static final String CONSTRAINT_CONFLICT_EMAIL = "PUBLIC.USER(EMAIL_ADDRESS)";

    public static final String PDF_GENERATED = "PDF Generated?: ";
    public static final String NOTIFICATION = "Notify";

}
