void method0() { 
private FolderInfo folderInfo;
private List columnValues;
private List loadQueue = new LinkedList();
private List priorityLoadQueue = new LinkedList();
private List messageLoadedListeners = new LinkedList();
private int updateCheckMilliseconds = 60000;
private int updateMessagesCount = 10;
private boolean sleeping = false;
private boolean stopped = false;
public static int NORMAL = 5;
public static int HIGH = 10;
}
