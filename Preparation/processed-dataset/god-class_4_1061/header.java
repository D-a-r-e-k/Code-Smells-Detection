void method0() { 
/*************************************************************************
     * NOTE: below constants MUST NOT be changed IN ALL CASES,
     *       or it will break the compatibility
     *************************************************************************/
/**
     * The default value means this message has NOT been read
     */
public static final int MESSAGE_READ_STATUS_DEFAULT = 0;
/**
     * This value means this message has been read
     */
public static final int MESSAGE_READ_STATUS_READ = 1;
/**
     * The default value means this message type is normal
     */
public static final int MESSAGE_TYPE_DEFAULT = 0;
/**
     * This value means this message has been mark as Quote [Marco]
     */
public static final int MESSAGE_TYPE_QUOTE = 1;
/**
     * This value means this message is a public message
     */
public static final int MESSAGE_TYPE_PUBLIC = 2;
private int messageID;
private String folderName;
private int memberID;
private int messageSenderID;
private String messageSenderName;
private String messageToList;
private String messageCcList;
private String messageBccList;
private String messageTopic;
private String messageBody;
private int messageType;
private int messageOption;
private int messageStatus;
private int messageReadStatus;
private int messageNotify;
private String messageIcon;
private int messageAttachCount;
private String messageIP;
private Timestamp messageCreationDate;
/************************************************
     * Customized methods come below
     ************************************************/
private MemberBean memberBean = null;
private Collection attachmentBeans = null;
}
