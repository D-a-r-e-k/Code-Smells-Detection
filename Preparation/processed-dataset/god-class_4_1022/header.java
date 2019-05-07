void method0() { 
// logger 
Logger mLogger = null;
// folder is currently open and available. 
public static int CONNECTED = 0;
// folder is disconnected, but should be open; try to reopen at first 
// opportunity 
public static int LOST_CONNECTION = 5;
// folder is available, but only should be accessed during the checkFolder 
// phase. 
public static int PASSIVE = 10;
// folder is running in disconnected mode; only act on the cached 
// messages. 
public static int DISCONNECTED = 15;
// Folder doesn't seem to exist on server, but exists in cache. 
public static int CACHE_ONLY = 18;
// folder is just simply closed. 
public static int CLOSED = 20;
// folder is not yet loaded. 
public static int NOT_LOADED = 25;
// folder does not exist on server or in cache. 
public static int INVALID = 30;
// shows the current status of the FolderInfo. 
protected int status = NOT_LOADED;
// shows the type of this folder. 
protected int type = 0;
// shows the preferred state of the FolderInfo.  should be CONNECTED, 
// PASSIVE, DISCONNECTED, or CLOSED. 
protected int preferredStatus = CONNECTED;
// the resource for the folder disconnected message 
protected static String disconnectedMessage = "error.Folder.disconnected";
// the Folder wrapped by this FolderInfo. 
private Folder folder;
// The is the folder ID: storeName.parentFolderName.folderName 
private String folderID;
// This is just the simple folderName, such as "INBOX" 
private String mFolderName;
private EventListenerList eventListeners = new EventListenerList();
// Information for the FolderNode 
protected FolderNode folderNode;
protected Vector children;
// Information for the FolderTable. 
protected FolderTableModel folderTableModel;
protected HashMap<Message, MessageInfo> messageToInfoTable = new HashMap<Message, MessageInfo>();
private List columnValues;
private List<String> columnNames;
private List<String> columnSizes;
private List<String> columnIds;
// GUI information. 
private FolderDisplayUI folderDisplayUI;
private Action[] defaultActions;
//filters 
protected BackendMessageFilter[] backendFilters = null;
protected MessageFilter[] displayFilters = null;
//protected LoadMessageThread loaderThread; 
protected MessageLoader mMessageLoader = null;
private FolderTracker folderTracker = null;
protected boolean loading = false;
protected int unreadCount = 0;
protected int messageCount = 0;
private boolean newMessages = false;
private FolderInfo parentFolder = null;
private StoreInfo parentStore = null;
private UserProfile defaultProfile = null;
private boolean sentFolder = false;
private boolean trashFolder = false;
private boolean notifyNewMessagesMain = true;
private boolean notifyNewMessagesNode = true;
private boolean tracksUnreadMessages = true;
protected FetchProfile fetchProfile = null;
protected OutgoingMailServer mailServer = null;
// whether or not this is a namespace 
protected boolean mNamespace = false;
List filterHeaders = null;
}
