void method0() { 
/*************************************************************************
 * NOTE: below constants MUST NOT be changed IN ALL CASES,
 *       or it will break the compatibility
 *************************************************************************/
/** Guest/anonymous site visitor. */
public static final int MEMBER_ID_OF_GUEST = 0;
/** System administrator. */
public static final int MEMBER_ID_OF_ADMIN = 1;
/**
     * The highest reserved MemberID.
     * All IDs from 0 through this value should not be used for "regular" members.
     */
public static final int LAST_RESERVED_MEMBER_ID = 1;
/**
     * This is a hard code constant and CANNOT be changed in any case.
     * it could be use to store memberName in the table mvnforumPost, mvnforumThread if
     * it is a guest's post
     */
public static final String MEMBER_NAME_OF_GUEST = "guest";
/* IMPORTANT: When we have a group without group owner, GroupOwnerID is set to 0.
     * Similiar is for other IDs in the database - 0 means there is no reference.
     * Also, the other reason why MemberID=0 should not be used for Guest is
     * that DBMS could refuse to insert a record with 0 in that field, since it's
     * marked as non-null autoincrement primary key.
     */
/** Unused GroupID. */
public static final int GROUP_ID_UNUSED0 = 0;
/**
     * Unused GroupID. In the previous versions of mvnForum it was used for some
     * special purposes, but should not be used anymore.
     */
public static final int GROUP_ID_OF_GUEST = 1;
/** "Registered Members" virtual group. All members are listed in this group. */
public static final int GROUP_ID_OF_REGISTERED_MEMBERS = 2;
/**
     * The highest reserved GroupID.
     * All IDs from 0 through this value should not be used for "regular" groups.
     */
public static final int LAST_RESERVED_GROUP_ID = 2;
/** "Inbox" message folder created by default for each member. */
public static final String MESSAGE_FOLDER_INBOX = "Inbox";
/** "Sent" message folder created by default for each member. */
public static final String MESSAGE_FOLDER_SENT = "Sent";
/** "Draft" message folder created by default for each member. */
public static final String MESSAGE_FOLDER_DRAFT = "Draft";
/** "Trash" message folder created by default for each member. */
public static final String MESSAGE_FOLDER_TRASH = "Trash";
public static final String dtdschemaDecl = "<!DOCTYPE mvnforum SYSTEM \"http://www.mvnforum.com/dtd/mvnforum_1_0_rc2.dtd\">";
public static final String VN_TYPER_MODE = "mvnforum.vntypermode";
public static final String EVENT_LOG_MAIN_MODULE = "mvnForum";
public static final String EVENT_LOG_SUB_MODULE_USER = "User";
public static final String EVENT_LOG_SUB_MODULE_ADMIN = "Admin";
}
