void method0() { 
// logger 
Logger mLogger = null;
// the actual connection information 
private Store store;
private Session mSession;
// The is the store ID. 
private String storeID;
// Information for the StoreNode 
private StoreNode storeNode;
private Vector<FolderInfo> children;
// the status indicators 
private boolean connected = false;
private boolean available = false;
private int mPreferredStatus = FolderInfo.CONNECTED;
// if this is a pop mailbox. 
private boolean popStore = false;
private UserProfile defaultProfile;
private NetworkConnection connection;
// the connection information. 
private String user;
private String password;
private String server;
private String protocol;
private int port;
private URLName url;
private String sslSetting = "none";
// the Thread for connections to this Store. 
private ActionThread storeThread;
// the Trash folder for this Store, if any. 
private FolderInfo trashFolder;
// whether or not this store synchronizes with the subscribed folders 
// automatically 
private boolean useSubscribed = false;
// the connection listener for this store. 
private ConnectionListener connectionListener;
// the Authenticator to use for this Store. 
private AuthenticatorUI mAuthenticator;
// last connection check. 
long lastConnectionCheck = 0;
}
