package net.suberic.pooka;

import javax.mail.*;
import javax.mail.event.*;
import javax.mail.search.*;
import javax.swing.event.EventListenerList;
import java.util.*;
import java.util.logging.*;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import net.suberic.pooka.gui.*;
import net.suberic.pooka.thread.*;
import net.suberic.pooka.event.*;
import net.suberic.util.ValueChangeListener;
import net.suberic.util.VariableBundle;
import net.suberic.util.thread.ActionThread;

/**
 * This class does all of the work for a Folder.  If a FolderTableModel,
 * FolderWindow, Message/Row-to-MessageInfo map, or FolderTreeNode exist
 * for a Folder, the FolderInfo object has a reference to it.
 */

public class FolderInfo implements MessageCountListener, ValueChangeListener, UserProfileContainer, MessageChangedListener, ConnectionListener {

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

  // the thread for connections to this folder.
  //protected ActionThread mFolderThread;

  /**
   * For subclasses.
   */
  protected FolderInfo() {
  }

  /**
   * Creates a new FolderInfo from a parent FolderInfo and a Folder
   * name.
   */

  public FolderInfo(FolderInfo parent, String fname) {
    parentFolder = parent;
    setFolderID(parent.getFolderID() + "." + fname);
    mFolderName = fname;

    try {
      if (parent.isLoaded())
        loadFolder();
    } catch (OperationCancelledException oce) {
      // cancelled--ignore.
    } catch (MessagingException me) {
      // if we get an exception loading the folder while creating the folder
      // object, just ignore it.
      if (getLogger().isLoggable(Level.FINE)) {
        folderLog(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  caught messaging exception from parentStore getting folder: " + me);
        me.printStackTrace();

      }
    }

    updateChildren();

    createFilters();

    resetDefaultActions();

    if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesMain", "").equalsIgnoreCase("false"))
      setNotifyNewMessagesMain(true);
    else
      setNotifyNewMessagesMain(false);

    if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesNode", "").equalsIgnoreCase("false")) {
      setNotifyNewMessagesNode(true);
    } else {
      setNotifyNewMessagesNode(false);
    }
  }



  /**
   * Creates a new FolderInfo from a parent StoreInfo and a Folder
   * name.
   */

  public FolderInfo(StoreInfo parent, String fname) {
    parentStore = parent;
    setFolderID(parent.getStoreID() + "." + fname);
    mFolderName = fname;

    mNamespace = Pooka.getProperty(getFolderID() + "._namespace", "false").equalsIgnoreCase("true");

    try {
      if (parent.isConnected())
        loadFolder();
    } catch (OperationCancelledException oce) {
      // cancelled--ignore.
    } catch (MessagingException me) {
      // if we get an exception loading the folder while creating the folder
      // object, just ignore it.
      if (getLogger().isLoggable(Level.FINE)) {
        folderLog(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  caught messaging exception from parentStore getting folder: " + me);
        me.printStackTrace();

      }
    }

    updateChildren();

    createFilters();

    resetDefaultActions();

    if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesMain", "").equalsIgnoreCase("false"))
      setNotifyNewMessagesMain(true);
    else
      setNotifyNewMessagesMain(false);

    if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesNode", "").equalsIgnoreCase("false"))
      setNotifyNewMessagesNode(true);
    else
      setNotifyNewMessagesNode(false);
  }

  /**
   * This actually loads up the Folder object itself.  This is used so
   * that we can have a FolderInfo even if we're not connected to the
   * parent Store.
   *
   * Before we load the folder, the FolderInfo has the state of NOT_LOADED.
   * Once the parent store is connected, we can try to load the folder.
   * If the load is successful, we go to a CLOSED state.  If it isn't,
   * then we can either return to NOT_LOADED, or INVALID.
   */
  public void loadFolder() throws MessagingException, OperationCancelledException {
    loadFolder(true);
  }

  /**
   * This actually loads up the Folder object itself.  This is used so
   * that we can have a FolderInfo even if we're not connected to the
   * parent Store.
   *
   * Before we load the folder, the FolderInfo has the state of NOT_LOADED.
   * Once the parent store is connected, we can try to load the folder.
   * If the load is successful, we go to a CLOSED state.  If it isn't,
   * then we can either return to NOT_LOADED, or INVALID.
   */
  public void loadFolder(boolean pConnectStore) throws MessagingException, OperationCancelledException {
    boolean parentIsConnected = false;

    if (isLoaded() || (loading && children == null))
      return;

    Folder[] tmpFolder = null;
    Folder tmpParentFolder;

    try {
      loading = true;

      if (parentStore != null) {
        folderLog(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  checking parent store connection.");

        if (! parentStore.isAvailable())
          throw new MessagingException();

        if (!parentStore.isConnected()) {
          if (pConnectStore) {
            parentStore.connectStore();
          } else {
            return;
          }
        }


        Store store = parentStore.getStore();

        // first see if we're a namespace
        try {
          folderLog(Level.FINE, "checking to see if " + getFolderID() + " is a shared folder.");

          Folder[] sharedFolders = store.getSharedNamespaces();

          if (sharedFolders != null && sharedFolders.length > 0) {
            for (int i = 0; ( tmpFolder == null || tmpFolder.length == 0 ) && i < sharedFolders.length; i++) {
              if (sharedFolders[i].getName().equalsIgnoreCase(mFolderName)) {
                if (!mNamespace) {
                  Pooka.setProperty(getFolderID() + "._namespace", "true");
                  mNamespace = true;
                }
                tmpFolder = new Folder[1];
                tmpFolder[0] =  sharedFolders[i] ;
              }
            }
          }
        } catch (Exception e) {
          // if we get a not supported exception or some such here,
          // just ignore it.
        }

        if (tmpFolder == null || tmpFolder.length == 0) {
          // not a shared namespace
          tmpParentFolder = store.getDefaultFolder();
          folderLog(Level.FINE, "got " + tmpParentFolder + " as Default Folder for store.");
          folderLog(Level.FINE, "doing a list on default folder " + tmpParentFolder + " for folder " + mFolderName);
          tmpFolder = tmpParentFolder.list(mFolderName);
        }

        folderLog(Level.FINE, "got " + tmpFolder + " as Folder for folder " + getFolderID() + ".");

      } else {
        if (!parentFolder.isLoaded())
          parentFolder.loadFolder();
        if (!parentFolder.isLoaded()) {
          tmpFolder = null;
        } else {
          tmpParentFolder = parentFolder.getFolder();
          if (tmpParentFolder != null) {
            parentIsConnected = true;
            folderLog(Level.FINE, "running list (" + mFolderName + ") on parent folder " + tmpParentFolder);
            tmpFolder = tmpParentFolder.list(mFolderName);
          } else {
            tmpFolder = null;
          }
        }
      }

      if (tmpFolder != null && tmpFolder.length > 0) {
        setFolder(tmpFolder[0]);
        if (! getFolder().isSubscribed())
          getFolder().setSubscribed(true);

        type = getFolder().getType();
        setStatus(CLOSED);
      } else {
        folderLog(Level.FINE, "folder " + mFolderName + " does not exist; setting as INVALID.");
        if (parentIsConnected)
          setStatus(INVALID);
        setFolder(null);
      }
    } finally {
      loading = false;
    }

    initializeFolderInfo();

  }

  /**
   * This adds this a listener to the Folder.
   */
  protected void addFolderListeners() {
    if (folder != null) {
      folder.addMessageChangedListener(this);
      folder.addMessageCountListener(this);
      folder.addConnectionListener(this);
    }
  }

  /**
   * This removes this as a listener to the Folder.
   */
  protected void removeFolderListeners() {
    if (folder != null) {
      folder.removeMessageChangedListener(this);
      folder.removeMessageCountListener(this);
      folder.removeConnectionListener(this);
    }
  }

  /**
   * this is called by loadFolders if a proper Folder object
   * is returned.
   */
  protected void initializeFolderInfo() {
    addFolderListeners();

    Pooka.getResources().addValueChangeListener(this, getFolderProperty());
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".folderList");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".defaultProfile");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".displayFilters");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".backendFilters");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".notifyNewMessagesMain");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".notifyNewMessagesNode");

    Pooka.getLogManager().addLogger(getFolderProperty());

    String defProfile = Pooka.getProperty(getFolderProperty() + ".defaultProfile", "");
    if ((!defProfile.equals("")) && (!defProfile.equals(UserProfile.S_DEFAULT_PROFILE_KEY)))
      defaultProfile = Pooka.getPookaManager().getUserProfileManager().getProfile(defProfile);

    // if we got to this point, we should assume that the open worked.

    if (getFolderTracker() == null) {
      FolderTracker tracker = Pooka.getFolderTracker();
      if (tracker != null) {
        tracker.addFolder(this);
        this.setFolderTracker(tracker);
      } else {
        if (Pooka.sStartupManager.isShuttingDown()) {
          getLogger().fine("No FolderTracker available.");
        } else {
          getLogger().warning("Error:  No FolderTracker available for folder " + getFolderID());
        }
      }
    }
  }

  public void closed(ConnectionEvent e) {

    synchronized(this) {

      folderLog(Level.FINE, "Folder " + getFolderID() + " closed:  " + e);
      // if this happened accidentally, check it.
      if (getStatus() != CLOSED && getStatus() != DISCONNECTED) {

        getFolderThread().addToQueue(new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
              // check to see if the parent store is still open.

              StoreInfo parentStoreInfo = getParentStore();
              if (parentStoreInfo != null) {
                if (parentStoreInfo.isConnected())
                  parentStoreInfo.checkConnection();
              }
            }
          }, new java.awt.event.ActionEvent(this, 0, "folder-closed"), ActionThread.PRIORITY_HIGH);

        if (getFolderDisplayUI() != null) {
          getFolderDisplayUI().showStatusMessage(Pooka.getProperty(disconnectedMessage, "Lost connection to folder..."));
        }

        if (status == CONNECTED) {
          setStatus(LOST_CONNECTION);
        }
      }

    }
    fireConnectionEvent(e);

  }

  public void disconnected(ConnectionEvent e) {
    synchronized(this) {
      if (getLogger().isLoggable(Level.FINE)) {
        folderLog(Level.FINE, "Folder " + getFolderID() + " disconnected.");
        Thread.dumpStack();
      }

      // if this happened accidentally, check it.
      if (getStatus() != CLOSED) {
        getFolderThread().addToQueue(new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
              // check to see if the parent store is still open.

              StoreInfo parentStoreInfo = getParentStore();
              if (parentStoreInfo != null && parentStoreInfo.isConnected()) {
                parentStoreInfo.checkConnection();
              }
            }
          }, new java.awt.event.ActionEvent(this, 0, "folder-closed"), ActionThread.PRIORITY_HIGH);

        if (getFolderDisplayUI() != null) {
          getFolderDisplayUI().showStatusMessage(Pooka.getProperty("error.UIDFolder.disconnected", "Lost connection to folder..."));
        }

        if (status == CONNECTED) {
          setStatus(LOST_CONNECTION);
        }
      }

    }

    fireConnectionEvent(e);
  }


  /**
   * Invoked when a Store/Folder/Transport is opened.
   *
   * As specified in javax.mail.event.ConnectionListener.
   */
  public void opened (ConnectionEvent e) {
    fireConnectionEvent(e);
  }

  /**
   * Invoked when a Store/Folder/Transport is opened.
   *
   * As specified in javax.mail.event.ConnectionListener.
   */
  public void connected (ConnectionEvent e) {
    fireConnectionEvent(e);
  }

  /**
   * This method opens the Folder, and sets the FolderInfo to know that
   * the Folder should be open.  You should use this method instead of
   * calling getFolder().open(), because if you use this method, then
   * the FolderInfo will try to keep the Folder open, and will try to
   * reopen the Folder if it gets closed before closeFolder is called.
   *
   * This method can also be used to reset the mode of an already
   * opened folder.
   */
  public void openFolder(int mode) throws MessagingException, OperationCancelledException {
    openFolder(mode, true);
  }

  /**
   * This method opens the Folder, and sets the FolderInfo to know that
   * the Folder should be open.  You should use this method instead of
   * calling getFolder().open(), because if you use this method, then
   * the FolderInfo will try to keep the Folder open, and will try to
   * reopen the Folder if it gets closed before closeFolder is called.
   *
   * This method can also be used to reset the mode of an already
   * opened folder.
   */
  public void openFolder(int mode, boolean pConnectStore) throws MessagingException, OperationCancelledException {
    //System.err.println("timing:  opening folder " + getFolderID());
    //long currentTime = System.currentTimeMillis();

    folderLog(Level.FINE, this + ":  checking parent store.");

    if (!getParentStore().isConnected() && pConnectStore) {
      folderLog(Level.FINE, this + ":  parent store isn't connected.  trying connection.");
      getParentStore().connectStore();
    }

    folderLog(Level.FINE, this + ":  loading folder.");

    if (! isLoaded() && status != CACHE_ONLY)
      loadFolder(pConnectStore);

    folderLog(Level.FINE, this + ":  folder loaded.  status is " + status);

    folderLog(Level.FINE, this + ":  checked on parent store.  trying isLoaded() and isAvailable().");

    if (status == CLOSED || status == LOST_CONNECTION || status == DISCONNECTED) {
      folderLog(Level.FINE, this + ":  isLoaded() and isAvailable().");
      if (folder.isOpen()) {
        if (folder.getMode() == mode)
          return;
        else {
          folder.close(false);
          openFolder(mode);
          updateFolderOpenStatus(true);
          resetMessageCounts();
        }
      } else {
        folder.open(mode);
        updateFolderOpenStatus(true);
        resetMessageCounts();
      }
    } else if (status == INVALID) {
      throw new MessagingException(Pooka.getProperty("error.folderInvalid", "Error:  folder is invalid.  ") + getFolderID());
    }

    //System.err.println("timing:  opening folder " + getFolderID() + " took " + (System.currentTimeMillis() - currentTime) + " milliseconds.");

  }

  /**
   * Actually records that the folder has been opened or closed.
   * This is separated out so that subclasses can override it more
   * easily.
   */
  protected void updateFolderOpenStatus(boolean isNowOpen) {
    if (isNowOpen) {
      setStatus(CONNECTED);
    } else {
      setStatus(CLOSED);
    }
  }

  /**
   * This method calls openFolder() on this FolderInfo, and then, if
   * this FolderInfo has any children, calls openFolder() on them,
   * also.
   *
   * This is usually called by StoreInfo.connectStore() if
   * Pooka.openFoldersOnConnect is set to true.
   */

  public void openAllFolders(int mode) {
    try {
      openFolder(mode, false);
    } catch (MessagingException me) {
    } catch (OperationCancelledException oce) {
    }

    if (children != null) {
      for (int i = 0; i < children.size(); i++) {
        doOpenFolders((FolderInfo) children.elementAt(i), mode);
      }
    }
  }

  /**
   * Handles the threading of doing an openAllFolders.
   */
  private void doOpenFolders(FolderInfo fi, int mode) {
    if (Pooka.getProperty("Pooka.openFoldersInBackground", "false").equalsIgnoreCase("true")) {
      final FolderInfo current = fi;
      final int finalMode = mode;

      javax.swing.AbstractAction openFoldersAction =new javax.swing.AbstractAction() {
          public void actionPerformed(java.awt.event.ActionEvent e) {
            current.openAllFolders(finalMode);
          }
        };

      openFoldersAction.putValue(javax.swing.Action.NAME, "file-open");
      openFoldersAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "file-open on folder " + fi.getFolderID());
      getFolderThread().addToQueue(openFoldersAction, new java.awt.event.ActionEvent(this, 0, "open-all"), ActionThread.PRIORITY_LOW);
    } else {
      fi.openAllFolders(mode);
    }
  }

  /**
   * This method closes the Folder.  If you open the Folder using
   * openFolder (which you should), then you should use this method
   * instead of calling getFolder.close().  If you don't, then the
   * FolderInfo will try to reopen the folder.
   */
  public void closeFolder(boolean expunge, boolean closeDisplay) throws MessagingException {

    if (closeDisplay) {
      unloadAllMessages();

      if (getFolderDisplayUI() != null)
        getFolderDisplayUI().closeFolderDisplay();

      setFolderDisplayUI(null);
    }

    if (getFolderTracker() != null) {
      getFolderTracker().removeFolder(this);
      setFolderTracker(null);
    }

    if (isLoaded() && isValid()) {
      setStatus(CLOSED);
      try {
        folder.close(expunge);
      } catch (java.lang.IllegalStateException ise) {
        throw new MessagingException(ise.getMessage(), ise);
      }
    }

  }

  public void closeFolder(boolean expunge) throws MessagingException {
    closeFolder(expunge, true);
  }

  /**
   * This closes the current Folder as well as all subfolders.
   */
  public void closeAllFolders(boolean expunge, boolean shuttingDown) throws MessagingException {
    /*
      if (shuttingDown && loaderThread != null) {
      loaderThread.stopThread();
      }
    */

    if (shuttingDown && mMessageLoader != null) {
      mMessageLoader.stopLoading();
    }

    synchronized(getFolderThread().getRunLock()) {
      MessagingException otherException = null;
      Vector folders = getChildren();
      if (folders != null) {
        for (int i = 0; i < folders.size(); i++) {
          try {
            ((FolderInfo) folders.elementAt(i)).closeAllFolders(expunge, shuttingDown);
          } catch (MessagingException me) {
            if (otherException == null)
              otherException = me;
          } catch (Exception e) {
            MessagingException newMe = new MessagingException (e.getMessage(), e);
            if (otherException == null)
              otherException = newMe;
          }
        }
      }

      closeFolder(expunge, false);

      if (otherException != null)
        throw otherException;
    }
  }

  /**
   * Gets all of the children folders of this FolderInfo which are
   * both Open and can contain Messages.  The return value should include
   * the current FolderInfo, if it is Open and can contain Messages.
   */
  public Vector getAllFolders() {
    Vector returnValue = new Vector();
    if (children != null) {
      for (int i = 0 ; i < children.size(); i++)
        returnValue.addAll(((FolderInfo) children.elementAt(i)).getAllFolders());
    }

    if (isSortaOpen() && (getType() & Folder.HOLDS_MESSAGES) != 0)
      returnValue.add(this);

    return returnValue;
  }

  /**
   * Synchronizes the locally stored subscribed folders list to the subscribed
   * folder information from the IMAP server.
   */
  public void synchSubscribed() throws MessagingException, OperationCancelledException {
    // if we're a namespace, then ignore.
    if (mNamespace)
      return;

    // at this point we should get folder objects.
    if (! isLoaded())
      loadFolder();

    if (status < NOT_LOADED) {
      Folder[] subscribedFolders = folder.list();

      List<String> subscribedNames = new ArrayList<String>();

      for (int i = 0; subscribedFolders != null && i < subscribedFolders.length; i++) {
        // sometimes listSubscribed() doesn't work.
        if (subscribedFolders[i].isSubscribed() || subscribedFolders[i].getName().equalsIgnoreCase("INBOX")) {
          String folderName = subscribedFolders[i].getName();
          subscribedNames.add(folderName);
        }
      }
      Collections.sort(subscribedNames);

      // keep the existing order when possible.
      List<String> currentSubscribed = Pooka.getResources().getPropertyAsList(getFolderProperty() + ".folderList", "");
      Iterator<String> currentIter = currentSubscribed.iterator();
      while(currentIter.hasNext()) {
        String folder = currentIter.next();
        if (! subscribedNames.contains(folder)) {
          currentSubscribed.remove(folder);
        } else {
          subscribedNames.remove(folder);
        }
      }

      currentSubscribed.addAll(subscribedNames);

      // this will update our children vector.
      Pooka.setProperty(getFolderProperty() + ".folderList", VariableBundle.convertToString(currentSubscribed));

      for (int i = 0; children != null && i < children.size(); i++) {
        FolderInfo fi = (FolderInfo) children.elementAt(i);
        fi.synchSubscribed();
      }
    }
  }

  /**
   * Loads the column names and sizes.
   */
  protected FetchProfile createColumnInformation() {
    String tableType;

    if (isSentFolder())
      tableType="SentFolderTable";
    //else if (this instanceof VirtualFolderInfo)
    //    tableType="SearchResultsTable";
    else if (isOutboxFolder())
      tableType="SentFolderTable";
    else
      tableType="FolderTable";

    FetchProfile fp = new FetchProfile();
    fp.add(FetchProfile.Item.FLAGS);
    if (columnValues == null) {
      List<String> colIds = Pooka.getResources().getPropertyAsList(tableType, "");
      Vector colvals = new Vector();
      Vector<String> colnames = new Vector<String>();
      Vector<String> colsizes = new Vector<String>();

      for (String tmp: colIds) {
        String type = Pooka.getProperty(tableType + "." + tmp + ".type", "");
        if (type.equalsIgnoreCase("Multi")) {
          SearchTermIconManager stm = new SearchTermIconManager(tableType + "." + tmp);
          colvals.addElement(stm);
          Vector toFetch = Pooka.getResources().getPropertyAsVector(tableType + "." + tmp + ".profileItems", "");
          if (toFetch != null) {
            for (int z = 0; z < toFetch.size(); z++) {
              String profileDef = (String) toFetch.elementAt(z);
              if (profileDef.equalsIgnoreCase("Flags")) {
                folderLog(Level.FINE, "adding FLAGS to FetchProfile.");
                fp.add(FetchProfile.Item.FLAGS);
              } else if (profileDef.equalsIgnoreCase("Envelope")) {
                folderLog(Level.FINE, "adding ENVELOPE to FetchProfile.");
                fp.add(FetchProfile.Item.ENVELOPE);
              } else if (profileDef.equalsIgnoreCase("Content_Info")) {
                folderLog(Level.FINE, "adding CONTENT_INFO to FetchProfile.");
                fp.add(FetchProfile.Item.CONTENT_INFO);
              } else {
                folderLog(Level.FINE, "adding " + profileDef + " to FetchProfile.");
                fp.add(profileDef);
              }
            }
          }
        } else if (type.equalsIgnoreCase("RowCounter")) {
          colvals.addElement(RowCounter.getInstance());
        } else {
          String value = Pooka.getProperty(tableType + "." + tmp + ".value", tmp);
          colvals.addElement(value);
          String fpValue = Pooka.getProperty(tableType + "." + tmp + ".profileItems", value);
          fp.add(fpValue);
        }

        colnames.addElement(Pooka.getProperty(tableType + "." + tmp + ".label", tmp));
        String value = Pooka.getProperty(getFolderProperty() + ".columnsize." + tmp + ".value", Pooka.getProperty(tableType + "." + tmp + ".value", tmp));
        colsizes.addElement(Pooka.getProperty(getFolderProperty() + ".columnsize." + tmp + ".value", Pooka.getProperty(tableType + "." + tmp + ".size", tmp)));
      }
      setColumnNames(colnames);
      setColumnValues(colvals);
      setColumnSizes(colsizes);
      setColumnIds(colIds);
    }

    // if we've already loaded the filters, then add those in, too.
    if (filterHeaders != null) {
      for (int i = 0; i < filterHeaders.size(); i++) {
        fp.add((String) filterHeaders.get(i));
      }
    }

    if (getLogger().isLoggable(Level.FINE)) {
      folderLog(Level.FINE, "created fetch profile.");
      String[] headers = fp.getHeaderNames();
      if (headers != null) {
        for (int i = 0; i < headers.length; i++) {
          folderLog(Level.FINE, "headers["+i+"]=" + headers[i]);
        }
      }
      folderLog(Level.FINE, "headers done.");
    }

    return fp;
  }

  /**
   * During loadAllMessages, updates the display to say that we're loading
   * messages.
   */
  protected void updateDisplay(boolean start) {
    if (getFolderDisplayUI() != null) {
      if (start) {
        getFolderDisplayUI().setBusy(true);
        getFolderDisplayUI().showStatusMessage(Pooka.getProperty("messages.Folder.loading.starting", "Loading messages."));
      } else {
        getFolderDisplayUI().setBusy(false);
        getFolderDisplayUI().showStatusMessage(Pooka.getProperty("messages.Folder.loading.finished", "Done loading messages."));
      }
    }
  }

  /**
   * While loading messages, attempts to update the folder status.
   */
  protected void updateFolderStatusForLoading() throws MessagingException, OperationCancelledException {
    if (! isConnected() ) {
      openFolder(Folder.READ_WRITE);
    }
  }

  /**
   * Notifies the FolderNode that it needs to be updated.
   */
  protected void updateNode() {
    if (getFolderNode() != null)
      getFolderNode().updateNode();
  }

  /**
   * Loads the MessageInfos and MesageProxies.  Returns a List of
   * newly created MessageProxies.
   */
  protected List createInfosAndProxies() throws MessagingException {
    int fetchBatchSize = 50;
    try {
      fetchBatchSize = Integer.parseInt(Pooka.getProperty("Pooka.fetchBatchSize", "50"));
    } catch (NumberFormatException nfe) {
    }

    Vector messageProxies = new Vector();

    Message[] msgs = folder.getMessages();

    Message[] toFetch = msgs;

    // go ahead and fetch the first set of messages; the rest will be
    // taken care of by the loaderThread.
    if (msgs.length > fetchBatchSize) {
      toFetch = new Message[fetchBatchSize];
      System.arraycopy(msgs, msgs.length - fetchBatchSize, toFetch, 0, fetchBatchSize);
    }

    folder.fetch(toFetch, fetchProfile);

    int firstFetched = Math.max(msgs.length - fetchBatchSize, 0);

    MessageInfo mi;

    for (int i = 0; i < msgs.length; i++) {
      mi = new MessageInfo(msgs[i], this);

      if ( i >= firstFetched)
        mi.setFetched(true);

      messageProxies.add(new MessageProxy(getColumnValues() , mi));
      messageToInfoTable.put(msgs[i], mi);
    }

    return messageProxies;
  }

  /**
   * Applies message filters to the new messages.
   */
  public void runFilters(List proxies) throws MessagingException {
    if (isConnected()) {
      Folder current = getFolder();
      if (current != null && current.isOpen()) {
        int newCount = current.getNewMessageCount();

        if (newCount > 0) {
          int numProxies = proxies.size();
          List newProxies = new ArrayList();
          for (int i = 0; i < newCount; i++) {
            newProxies.add(proxies.get((numProxies - newCount) + i));
          }
          proxies.removeAll(applyFilters(newProxies));
        }
      }
    }

  }

  /**
   * Updates any caching information, if necessary.
   */
  protected void updateCache() throws MessagingException {
    // no-op by default.
  }

  /**
   * Loads all Messages into a new FolderTableModel, sets this
   * FolderTableModel as the current FolderTableModel, and then returns
   * said FolderTableModel.  This is the basic way to populate a new
   * FolderTableModel.
   */
  public synchronized void loadAllMessages() throws MessagingException, OperationCancelledException {
    if (folderTableModel == null) {
      updateDisplay(true);

      if (! isLoaded())
        loadFolder();

      fetchProfile = createColumnInformation();

      /*
        if (loaderThread == null)
        loaderThread = createLoaderThread();
      */
      if (mMessageLoader == null)
        mMessageLoader = createMessageLoader();

      try {
        updateFolderStatusForLoading();

        List messageProxies = createInfosAndProxies();

        runFilters(messageProxies);

        FolderTableModel ftm = new FolderTableModel(messageProxies, getColumnNames(), getColumnSizes(), getColumnValues(), getColumnIds());

        setFolderTableModel(ftm);

        updateCache();

        Vector loadImmediately = null;

        int loadBatchSize = 25;

        if (messageProxies.size() > loadBatchSize) {
          // get the first unread.
          int firstUnread = messageProxies.size();
          if (Pooka.getProperty("Pooka.autoSelectFirstUnread", "true").equalsIgnoreCase("true")) {
            firstUnread = getFirstUnreadMessage();
          }

          int lastLoaded = messageProxies.size() - 1;
          int firstLoaded = messageProxies.size() - loadBatchSize - 1;

          if (firstUnread > -1) {
            if (firstUnread < firstLoaded) {
              firstLoaded = Math.max(0, firstUnread - 5);
              lastLoaded = firstLoaded + loadBatchSize;
            }
          }

          loadImmediately = new Vector();
          for (int i = lastLoaded; i >= firstLoaded; i--) {
            loadImmediately.add(messageProxies.get(i));
          }
        } else {
          loadImmediately = new Vector(messageProxies);
        }

        loadMessageTableInfos(loadImmediately);

        /*
          loaderThread.loadMessages(messageProxies);

          if (!loaderThread.isAlive())
          loaderThread.start();
        */
        mMessageLoader.loadMessages(messageProxies);


      } finally {
        updateDisplay(false);
      }

    }
  }

  /**
   * Loads the FolderTableInfo objects for the given messages.
   */
  public void loadMessageTableInfos(Vector messages) {
    int numMessages = messages.size();
    MessageProxy mp;

    int updateCounter = 0;

    if (numMessages > 0) {

      int fetchBatchSize = 25;
      int loadBatchSize = 25;
      try {
        fetchBatchSize = Integer.parseInt(Pooka.getProperty("Pooka.fetchBatchSize", "50"));
      } catch (NumberFormatException nfe) {
      }

      FetchProfile fetchProfile = getFetchProfile();

      int i = numMessages - 1;
      while ( i >= 0 ) {
        for (int batchCount = 0; i >=0 && batchCount < loadBatchSize; batchCount++) {
          mp=(MessageProxy)messages.elementAt(i);

          if (! mp.getMessageInfo().hasBeenFetched()) {
            try {
              int fetchCount = 0;
              Vector fetchVector = new Vector();
              for (int j = i; fetchCount < fetchBatchSize && j >= 0; j--) {
                MessageInfo fetchInfo = ((MessageProxy) messages.elementAt(j)).getMessageInfo();
                if (! fetchInfo.hasBeenFetched()) {
                  fetchVector.add(fetchInfo);
                  fetchInfo.setFetched(true);
                }
              }

              MessageInfo[] toFetch = new MessageInfo[fetchVector.size()];
              toFetch = (MessageInfo[]) fetchVector.toArray(toFetch);
              this.fetch(toFetch, fetchProfile);
            } catch(MessagingException me) {
              folderLog(Level.FINE, "caught error while fetching for folder " + getFolderID() + ":  " + me);
              me.printStackTrace();
            }

          }
          try {
            if (! mp.isLoaded())
              mp.loadTableInfo();
            if (mp.needsRefresh())
              mp.refreshMessage();
            else if (! mp.matchedFilters()) {
              mp.matchFilters();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          i--;
        }

      }
    }
  }

  /**
   * Fetches the information for the given messages using the given
   * FetchProfile.
   */
  public void fetch(MessageInfo[] messages, FetchProfile profile) throws MessagingException  {
    Message[] realMsgs = new Message[messages.length];
    for (int i = 0; i < realMsgs.length; i++) {
      realMsgs[i] = messages[i].getMessage();
    }
    getFolder().fetch(realMsgs, profile);

    for (int i = 0 ; i < messages.length; i++) {
      messages[i].setFetched(true);
    }
  }

  /**
   * Unloads all messages.  This should be run if ever the current message
   * information becomes out of date, as can happen when the connection
   * to the folder goes down.
   */
  public void unloadAllMessages() {
    folderTableModel = null;
  }


  /**
   * Unloads all of the tableInfos of the MessageInfo objects.  This
   * should be used either when the message information is stale, or when
   * the display rules have changed.
   */
  public void unloadTableInfos() {
    if (folderTableModel != null) {
      List allProxies = folderTableModel.getAllProxies();
      for (int i = 0; i < allProxies.size(); i++) {
        MessageProxy mp = (MessageProxy) allProxies.get(i);
        mp.unloadTableInfo();
      }

      /*
        if (loaderThread != null)
        loaderThread.loadMessages(allProxies);
      */

      if (mMessageLoader != null)
        mMessageLoader.loadMessages(allProxies);

    }
  }

  /**
   * Unloads the matching filters.
   */
  public void unloadMatchingFilters() {
    if (folderTableModel != null) {
      List allProxies = folderTableModel.getAllProxies();
      for (int i = 0; i < allProxies.size(); i++) {
        MessageProxy mp = (MessageProxy) allProxies.get(i);
        mp.clearMatchedFilters();
      }

      /*
        if (loaderThread != null)
        loaderThread.loadMessages(allProxies);
      */

      if (mMessageLoader != null)
        mMessageLoader.loadMessages(allProxies);

    }
  }

  /**
   * Refreshes the headers for the given MessageInfo.
   */
  public void refreshHeaders(MessageInfo mi) throws MessagingException {
    // no-op for default; only really used by UIDFolderInfos.
  }

  /**
   * Refreshes the flags for the given MessageInfo.
   */
  public void refreshFlags(MessageInfo mi) throws MessagingException {
    // no-op for default; only really used by UIDFolderInfos.
  }


  /**
   * This just checks to see if we can get a NewMessageCount from the
   * folder.  As a brute force method, it also accesses the folder
   * at every check.  It's nasty, but it _should_ keep the Folder open..
   */
  public void checkFolder() throws javax.mail.MessagingException, OperationCancelledException {
    folderLog(Level.FINE, "checking folder " + getFolderID());

    // i'm taking this almost directly from ICEMail; i don't know how
    // to keep the stores/folders open, either.  :)

    if (isConnected()) {
      Folder current = getFolder();
      if (current != null && current.isOpen()) {
        current.getNewMessageCount();
        current.getUnreadMessageCount();
      }
      resetMessageCounts();
    }
  }

  /**
   * Gets the row number of the first unread message.  Returns -1 if
   * there are no unread messages, or if the FolderTableModel is not
   * set or empty.
   */
  public int getFirstUnreadMessage() {
    folderLog(Level.FINE, "getting first unread message");

    if (! tracksUnreadMessages())
      return -1;

    if (getFolderTableModel() == null)
      return -1;

    try {
      int countUnread = 0;
      int i;
      if (unreadCount > 0) {

        // one part brute, one part force, one part ignorance.

        Message[] messages = getFolder().getMessages();
        int lastUnreadFound = -1;
        for (i = messages.length - 1; ( i >= 0 && countUnread < unreadCount) ; i--) {
          if (!(messages[i].isSet(Flags.Flag.SEEN))) {
            lastUnreadFound = i;
            countUnread++;
          }
        }
        if (lastUnreadFound != -1) {
          folderLog(Level.FINE, "Returning " + (lastUnreadFound + 1));
          return lastUnreadFound;
        } else {
          folderLog(Level.FINE, "unreads detected, but none found.");
          return -1;
        }

      } else {
        folderLog(Level.FINE, "Returning -1");
        return -1;
      }
    } catch (MessagingException me) {
      folderLog(Level.FINE, "Messaging Exception.  Returning -1");
      return -1;
    }
  }

  /**
   * This updates the children of the current folder.  Generally called
   * when the folderList property is changed.
   *
   * Should be called on the folder thread.
   */
  public void updateChildren() {
    Vector<FolderInfo> newChildren = new Vector();

    List<String> newChildNames = Pooka.getResources().getPropertyAsList(getFolderProperty() + ".folderList", "");
    for (String newFolderName: newChildNames) {
      FolderInfo childFolder = getChild(newFolderName);
      if (childFolder == null) {
        childFolder = createChildFolder(newFolderName);
      }
      newChildren.add(0, childFolder);

      children = newChildren;
    }

    if (folderNode != null)
      folderNode.loadChildren();
  }

  /**
   * Creates a child folder.
   */
  protected FolderInfo createChildFolder(String newFolderName) {
    return new FolderInfo(this, newFolderName);
  }

  /**
   * This goes through the list of children of this folder and
   * returns the FolderInfo for the given childName, if one exists.
   * If none exists, or if the children Vector has not been loaded
   * yet, or if this is a leaf node, then this method returns null.
   */
  public FolderInfo getChild(String childName) {
    folderLog(Level.FINE, "folder " + getFolderID() + " getting child " + childName);

    FolderInfo childFolder = null;
    String folderName  = null, subFolderName = null;

    if (children != null) {
      int divider = childName.indexOf('/');
      if (divider > 0) {
        folderName = childName.substring(0, divider);
        if (divider < childName.length() - 1)
          subFolderName = childName.substring(divider + 1);
      } else
        folderName = childName;

      folderLog(Level.FINE, "getting direct child " + folderName);

      for (int i = 0; i < children.size(); i++)
        if (((FolderInfo)children.elementAt(i)).getFolderName().equals(folderName))
          childFolder = (FolderInfo)children.elementAt(i);
    } else {
      folderLog(Level.FINE, "children of " + getFolderID() + " is null.");
    }

    if (childFolder != null && subFolderName != null)
      return childFolder.getChild(subFolderName);
    else
      return childFolder;
  }

  /**
   * This goes through the list of children of this store and
   * returns the FolderInfo that matches this folderID.
   * If none exists, or if the children Vector has not been loaded
   * yet, or if this is a leaf node, then this method returns null.
   */
  public FolderInfo getFolderById(String folderID) {
    FolderInfo childFolder = null;

    if (getFolderID().equals(folderID))
      return this;

    if (children != null) {
      for (int i = 0; i < children.size(); i++) {
        FolderInfo possibleMatch = ((FolderInfo)children.elementAt(i)).getFolderById(folderID);
        if (possibleMatch != null) {
          return possibleMatch;
        }
      }
    }

    return null;
  }

  /**
   * creates the loader thread.
   */
  public LoadMessageThread createLoaderThread() {
    LoadMessageThread lmt = new LoadMessageThread(this);
    return lmt;
  }

  /**
   * creates the MessageLoader.
   */
  public MessageLoader createMessageLoader() {
    MessageLoader ml = new MessageLoader(this);
    return ml;
  }

  /**
   * Returns the MessageLoader for this FolderInfo.
   */
  public MessageLoader getMessageLoader() {
    return mMessageLoader;
  }


  /**
   * gets the 'real' message for the given MessageInfo.
   */
  public Message getRealMessage(MessageInfo mi) throws MessagingException {
    return mi.getMessage();
  }

  /**
   * This sets the given Flag for all the MessageInfos given.
   */
  public void setFlags(MessageInfo[] msgs, Flags flag, boolean value) throws MessagingException {
    Message[] m = new Message[msgs.length];
    for (int i = 0; i < msgs.length; i++) {
      m[i] = msgs[i].getRealMessage();
    }

    getFolder().setFlags(m, flag, value);
  }

  /**
   * This copies the given messages to the given FolderInfo.
   */
  public void copyMessages(MessageInfo[] msgs, FolderInfo targetFolder) throws MessagingException, OperationCancelledException {
    if (targetFolder == null) {
      throw new MessagingException(Pooka.getProperty("error.null", "Error: null folder"));
    } else if (targetFolder.getStatus() == INVALID) {
      throw new MessagingException(Pooka.getProperty("error.folderInvalid", "Error:  folder is invalid.  ") + targetFolder.getFolderID());
    }

    if (! targetFolder.isAvailable())
      targetFolder.loadFolder();

    synchronized(targetFolder.getFolderThread().getRunLock()) {
      Folder target = targetFolder.getFolder();
      if (target != null) {
        Message[] m = new Message[msgs.length];
        for (int i = 0; i < msgs.length; i++) {
          m[i] = msgs[i].getRealMessage();
        }

        getFolder().copyMessages(m, target);

        // if we do a copy, we'll probably need to do a refresh on the target
        // folder, also.

        targetFolder.checkFolder();

      } else {
        targetFolder.appendMessages(msgs);
      }
    }
  }

  /**
   * This appends the given message to the given FolderInfo.
   */
  public void appendMessages(MessageInfo[] msgs) throws MessagingException, OperationCancelledException {
    if (! isSortaOpen())
      openFolder(Folder.READ_WRITE);
    Message[] m = new Message[msgs.length];
    for (int i = 0; i < msgs.length; i++) {
      m[i] = msgs[i].getRealMessage();
    }

    getFolder().appendMessages(m);
  }

  /**
   * This expunges the deleted messages from the Folder.
   */
  public void expunge() throws MessagingException, OperationCancelledException {
    getFolder().expunge();
  }

  /**
   * This handles the MessageLoadedEvent.
   *
   * As defined in interface net.suberic.pooka.event.MessageLoadedListener.
   */

  public void fireMessageChangedEvent(MessageChangedEvent mce) {
    // from the EventListenerList javadoc, including comments.

    // Guaranteed to return a non-null array
    Object[] listeners = eventListeners.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==MessageChangedListener.class) {
        ((MessageChangedListener)listeners[i+1]).messageChanged(mce);
      }
    }
  }

  public void addConnectionListener(ConnectionListener newListener) {
    eventListeners.add(ConnectionListener.class, newListener);
  }

  public void removeConnectionListener(ConnectionListener oldListener) {
    eventListeners.remove(ConnectionListener.class, oldListener);
  }


  /**
   * This handles the distributions of any Connection events.
   *
   * As defined in interface net.suberic.pooka.event.MessageLoadedListener.
   */
  public void fireConnectionEvent(ConnectionEvent e) {
    // from the EventListenerList javadoc, including comments.

    // Guaranteed to return a non-null array
    Object[] listeners = eventListeners.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==ConnectionListener.class) {
        ConnectionListener listener = (ConnectionListener) listeners[i+1];
        if (e.getType() == ConnectionEvent.CLOSED)
          listener.closed(e);
        else if (e.getType() == ConnectionEvent.DISCONNECTED)
          listener.disconnected(e);
        else if (e.getType() == ConnectionEvent.OPENED)
          listener.opened(e);
      }
    }
  }

  /**
   * This handles the changes if the source property is modified.
   *
   * As defined in net.suberic.util.ValueChangeListener.
   */
  public void valueChanged(String changedValue) {
    if (changedValue.equals(getFolderProperty() + ".folderList")) {
      final Runnable runMe = new  Runnable() {
          public void run() {
            ((javax.swing.tree.DefaultTreeModel)(((FolderPanel)folderNode.getParentContainer()).getFolderTree().getModel())).nodeStructureChanged(folderNode);
          }
        };
      // if we don't do the update synchronously on the folder thread,
      // then subscribing to subfolders breaks.
      if (Thread.currentThread() != getFolderThread()) {
        getFolderThread().addToQueue(new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
              updateChildren();
              if (folderNode != null) {
                javax.swing.SwingUtilities.invokeLater(runMe);
              }
            }
          } , new java.awt.event.ActionEvent(this, 0, "open-all"));
      } else {
        updateChildren();
        if (folderNode != null) {
          javax.swing.SwingUtilities.invokeLater(runMe);
        }
      }
    } else if (changedValue.equals(getFolderProperty() + ".defaultProfile")) {
      String newProfileValue = Pooka.getProperty(changedValue, "");
      if (newProfileValue.length() < 1 || newProfileValue.equals(UserProfile.S_DEFAULT_PROFILE_KEY))
        defaultProfile = null;
      else
        defaultProfile = Pooka.getPookaManager().getUserProfileManager().getProfile(newProfileValue);
    } else if (changedValue.equals(getFolderProperty() + ".backendFilters")) {
      createFilters();

    } else if (changedValue.equals(getFolderProperty() + ".displayFilters")) {
      createFilters();
      unloadMatchingFilters();
    } else if (changedValue.equals(getFolderProperty() + ".notifyNewMessagesMain") || changedValue.equals(getFolderProperty() + ".notifyNewMessagesNode")) {
      setNotifyNewMessagesMain(!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesMain", "").equalsIgnoreCase("false"));

      setNotifyNewMessagesNode(!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesNode", "").equalsIgnoreCase("false"));
    }
  }

  /**
   * This creates a folder if it doesn't exist already.  If it does exist,
   * but is not of the right type, or if there is a problem in creating the
   * folder, throws an error.
   */
  public void createSubFolder(String subFolderName, int type) throws MessagingException, OperationCancelledException {
    if ( ! isLoaded()) {
      loadFolder();
    }

    if (folder != null) {
      Folder subFolder = folder.getFolder(subFolderName);

      if (subFolder == null) {
        throw new MessagingException("Store returned null for subfolder " + subFolderName + " of folder " + getFolderName());
      }

      if (! subFolder.exists())
        subFolder.create(type);

      subscribeFolder(subFolderName);
    } else {
      throw new MessagingException("Failed to open folder " + getFolderName() + " to create subfolder " + subFolderName);
    }
  }

  /**
   * This subscribes to the FolderInfo indicated by the given String.
   * If this defines a subfolder, then that subfolder is added to this
   * FolderInfo, if it doesn't already exist.
   */
  public void subscribeFolder(String folderName) {
    folderLog(Level.FINE, "Folder " + getFolderID() + " subscribing subfolder " + folderName);

    String subFolderName = null;
    String childFolderName = null;
    int firstSlash = folderName.indexOf('/');
    while (firstSlash == 0) {
      folderName = folderName.substring(1);
      firstSlash = folderName.indexOf('/');
    }

    if (firstSlash > 0) {
      childFolderName = folderName.substring(0, firstSlash);
      if (firstSlash < folderName.length() -1)
        subFolderName = folderName.substring(firstSlash +1);
    } else
      childFolderName = folderName;

    folderLog(Level.FINE, "Folder " + getFolderID() + " subscribing folder " + childFolderName + ", plus subfolder " + subFolderName);

    this.addToFolderList(childFolderName);

    FolderInfo childFolder = getChild(childFolderName);

    folderLog(Level.FINE, "got child folder " + childFolder + " from childFolderName " + childFolderName);


    if (childFolder != null && subFolderName != null) {
      childFolder.subscribeFolder(subFolderName);
    }

    try {
      if (childFolder != null && childFolder.isLoaded() == false)
        childFolder.loadFolder();
    } catch (MessagingException me) {
      // if we get an exception loading a child folder while subscribing a
      // folder object, just ignore it.
      folderLog(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  caught messaging exception from parentStore getting folder: " + me);
      if (getLogger().isLoggable(Level.FINE))
        me.printStackTrace();

    } catch (OperationCancelledException me) {
      // if we get an exception loading a child folder while subscribing a
      // folder object, just ignore it.
    }

    updateChildren();
  }

  /**
   * This adds the given folderString to the folderList of this
   * FolderInfo.
   */
  void addToFolderList(String addFolderName) {
    Vector folderNames = Pooka.getResources().getPropertyAsVector(getFolderProperty() + ".folderList", "");

    boolean found = false;

    for (int i = 0; i < folderNames.size(); i++) {
      String folderName = (String) folderNames.elementAt(i);

      if (folderName.equals(addFolderName)) {
        found=true;
      }

    }

    if (!found) {
      String currentValue = Pooka.getProperty(getFolderProperty() + ".folderList", "");
      if (currentValue.equals(""))
        Pooka.setProperty(getFolderProperty() + ".folderList", addFolderName);
      else
        Pooka.setProperty(getFolderProperty() + ".folderList", currentValue + ":" + addFolderName);
    }

  }

  /**
   * Remove the given String from the folderList property.
   *
   * Note that because this is also a ValueChangeListener to the
   * folderList property, this will also result in the FolderInfo being
   * removed from the children Vector.
   */
  void removeFromFolderList(String removeFolderName) {
    Vector folderNames = Pooka.getResources().getPropertyAsVector(getFolderProperty() + ".folderList", "");

    boolean first = true;
    StringBuffer newValue = new StringBuffer();
    String folderName;

    for (int i = 0; i < folderNames.size(); i++) {
      folderName = (String) folderNames.elementAt(i);

      if (! folderName.equals(removeFolderName)) {
        if (!first)
          newValue.append(":");

        newValue.append(folderName);
        first = false;
      }

    }

    Pooka.setProperty(getFolderProperty() + ".folderList", newValue.toString());
  }

  /**
   * This unsubscribes this FolderInfo and all of its children, if
   * applicable.
   *
   * This implementation just removes the defining properties from
   * the Pooka resources.
   */
  public void unsubscribe() {

    cleanup();

    if (parentFolder != null)
      parentFolder.removeFromFolderList(getFolderName());
    else if (parentStore != null)
      parentStore.removeFromFolderList(getFolderName());

    try {
      if (folder != null)
        folder.setSubscribed(false);
    } catch (MessagingException me) {
      Pooka.getUIFactory().showError(Pooka.getProperty("error.folder.unsubscribe", "Error unsubscribing on server from folder ") + getFolderID(), me);
    }
  }

  /**
   * This deletes the underlying Folder.
   */
  public void delete() throws MessagingException, OperationCancelledException {
    if (! isLoaded())
      loadFolder();

    Folder f = getFolder();
    if (f == null)
      throw new MessagingException("No folder.");

    unsubscribe();

    if (f.isOpen())
      f.close(true);
    f.delete(true);
  }

  /**
   * Cleans up all references to this folder.
   */
  protected void cleanup() {

    Pooka.getResources().removeValueChangeListener(this);
    Folder f = getFolder();
    if (f != null) {
      removeFolderListeners();
    }

    if (children != null && children.size() > 0) {
      for (int i = 0; i < children.size(); i++)
        ((FolderInfo)children.elementAt(i)).cleanup();
    }

    Pooka.getLogManager().removeLogger(getFolderProperty());

    if (getFolderDisplayUI() != null)
      getFolderDisplayUI().closeFolderDisplay();

  }

  /**
   * This returns whether or not this Folder is set up to use the
   * TrashFolder for the Store.  If this is a Trash Folder itself,
   * then return false.  If FolderProperty.useTrashFolder is set,
   * return that.  else go up the tree, until, in the end,
   * Pooka.useTrashFolder is returned.
   */
  public boolean useTrashFolder() {
    if (isTrashFolder())
      return false;

    String prop = Pooka.getProperty(getFolderProperty() + ".useTrashFolder", "");
    if (!prop.equals(""))
      return (! prop.equalsIgnoreCase("false"));

    if (getParentFolder() != null)
      return getParentFolder().useTrashFolder();
    else if (getParentStore() != null)
      return getParentStore().useTrashFolder();
    else
      return (! Pooka.getProperty("Pooka.useTrashFolder", "true").equalsIgnoreCase("true"));
  }

  /**
   * This removes all the messages in the folder, if the folder is a
   * TrashFolder.
   */
  public void emptyTrash() {
    if (isTrashFolder()) {
      try {
        Message[] allMessages = getFolder().getMessages();
        getFolder().setFlags(allMessages, new Flags(Flags.Flag.DELETED), true);
        getFolder().expunge();
      } catch (MessagingException me) {
        String m = Pooka.getProperty("error.trashFolder.EmptyTrashError", "Error emptying Trash:") +"\n" + me.getMessage();
        if (getFolderDisplayUI() != null)
          getFolderDisplayUI().showError(m);
        else
          folderLog(Level.FINE, m);
      }
    }
  }

  /**
   * This resets the defaultActions.  Useful when this goes to and from
   * being a trashFolder, since only trash folders have emptyTrash
   * actions.
   */
  public void resetDefaultActions() {
    if (isTrashFolder()) {
      defaultActions = new Action[] {
        new net.suberic.util.thread.ActionWrapper(new UpdateCountAction(), getFolderThread()),
        new net.suberic.util.thread.ActionWrapper(new EmptyTrashAction(), getFolderThread()),
        new EditPropertiesAction()
      };
    } else if (isOutboxFolder()) {
      defaultActions = new Action[] {
        new net.suberic.util.thread.ActionWrapper(new UpdateCountAction(), getFolderThread()),
        new net.suberic.util.thread.ActionWrapper(new SendAllAction(), getFolderThread()),
        new EditPropertiesAction()
      };

    } else {
      defaultActions = new Action[] {
        new net.suberic.util.thread.ActionWrapper(new UpdateCountAction(), getFolderThread()),
        new EditPropertiesAction()
      };
    }
  }

  // semi-accessor methods.

  public MessageProxy getMessageProxy(int rowNumber) {
    return getFolderTableModel().getMessageProxy(rowNumber);
  }

  public MessageInfo getMessageInfo(Message m) {
    return (MessageInfo)messageToInfoTable.get(m);
  }

  public void addMessageCountListener(MessageCountListener newListener) {
    eventListeners.add(MessageCountListener.class, newListener);
  }

  public void removeMessageCountListener(MessageCountListener oldListener) {
    eventListeners.remove(MessageCountListener.class, oldListener);
  }

  public void fireMessageCountEvent(MessageCountEvent mce) {

    // from the EventListenerList javadoc, including comments.

    // Guaranteed to return a non-null array
    Object[] listeners = eventListeners.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event

    if (mce.getType() == MessageCountEvent.ADDED) {
      for (int i = listeners.length-2; i>=0; i-=2) {
        if (listeners[i]==MessageCountListener.class) {
          ((MessageCountListener)listeners[i+1]).messagesAdded(mce);
        }
      }
    } else if (mce.getType() == MessageCountEvent.REMOVED) {
      for (int i = listeners.length-2; i>=0; i-=2) {
        if (listeners[i]==MessageCountListener.class) {

          ((MessageCountListener)listeners[i+1]).messagesRemoved(mce);
        }
      }

    }
  }

  public void addMessageChangedListener(MessageChangedListener newListener) {
    eventListeners.add(MessageChangedListener.class, newListener);
  }

  public void removeMessageChangedListener(MessageChangedListener oldListener) {
    eventListeners.remove(MessageChangedListener.class, oldListener);
  }

  // as defined in javax.mail.event.MessageCountListener

  public void messagesAdded(MessageCountEvent e) {
    folderLog(Level.FINE, "Messages added.");

    if (Thread.currentThread() == getFolderThread() )
      runMessagesAdded(e);
    else
      getFolderThread().addToQueue(new net.suberic.util.thread.ActionWrapper(new javax.swing.AbstractAction() {

          public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            runMessagesAdded((MessageCountEvent)actionEvent.getSource());
          }
        }, getFolderThread()), new java.awt.event.ActionEvent(e, 1, "message-count-changed"));

  }

  protected void runMessagesAdded(MessageCountEvent mce) {
    folderLog(Level.FINE, "running messagesAdded on FolderInfo.");

    if (folderTableModel != null) {
      Message[] addedMessages = mce.getMessages();

      MessageInfo mp;
      Vector addedProxies = new Vector();
      folderLog(Level.FINE, "running messagesAdded: creating " + addedMessages.length + " proxies/MessageInfos.");

      for (int i = 0; i < addedMessages.length; i++) {
        mp = new MessageInfo(addedMessages[i], FolderInfo.this);
        addedProxies.add(new MessageProxy(getColumnValues(), mp));
        messageToInfoTable.put(addedMessages[i], mp);
      }
      folderLog(Level.FINE, "filtering proxies.");

      addedProxies.removeAll(applyFilters(addedProxies));
      if (addedProxies.size() > 0) {
        folderLog(Level.FINE, "filters run; adding " + addedProxies.size() + " messages.");

        getFolderTableModel().addRows(addedProxies);
        setNewMessages(true);
        resetMessageCounts();

        // notify the message loaded thread.
        MessageProxy[] addedArray = (MessageProxy[]) addedProxies.toArray(new MessageProxy[0]);
        mMessageLoader.loadMessages(addedArray, MessageLoader.HIGH);

        fireMessageCountEvent(mce);
      }
    }

  }

  /**
   * As defined in javax.mail.MessageCountListener.
   *
   * This runs when we get a notification that messages have been
   * removed from the mail server.
   *
   * This implementation just moves the handling of the event to the
   * FolderThread, where it runs runMessagesRemoved().
   */
  public void messagesRemoved(MessageCountEvent e) {
    folderLog(Level.FINE, "Messages Removed.");

    if (Thread.currentThread() == getFolderThread() ) {
      runMessagesRemoved(e);
    } else {
      getFolderThread().addToQueue(new net.suberic.util.thread.ActionWrapper(new javax.swing.AbstractAction() {
          public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            runMessagesRemoved((MessageCountEvent)actionEvent.getSource());
          }
        }, getFolderThread()), new java.awt.event.ActionEvent(e, 1, "messages-removed"));
    }
  }

  /**
   * This does the real work when messages are removed.  This can be
   * overridden by subclasses.
   */
  protected void runMessagesRemoved(MessageCountEvent mce) {
    folderLog(Level.FINE, "running MessagesRemoved on " + getFolderID());

    if (folderTableModel != null) {
      Message[] removedMessages = mce.getMessages();
      folderLog(Level.FINE, "removedMessages was of size " + removedMessages.length);
      MessageInfo mi;
      Vector removedProxies=new Vector();

      if (getLogger().isLoggable(Level.FINE)) {
        folderLog(Level.FINE, "message in info table:");
        Iterator<Message> keyIter = messageToInfoTable.keySet().iterator();
        while (keyIter.hasNext()) {
          folderLog(Level.FINE, keyIter.next().toString());
        }
      }

      for (int i = 0; i < removedMessages.length; i++) {
        folderLog(Level.FINE, "checking for existence of message " + removedMessages[i]);
        mi = getMessageInfo(removedMessages[i]);
        if (mi != null) {
          if (mi.getMessageProxy() != null)
            mi.getMessageProxy().close();

          folderLog(Level.FINE, "message exists--removing");
          removedProxies.add(mi.getMessageProxy());
          messageToInfoTable.remove(removedMessages[i]);
        }
      }
      if (getFolderDisplayUI() != null) {
        if (removedProxies.size() > 0)
          getFolderDisplayUI().removeRows(removedProxies);
        resetMessageCounts();
        fireMessageCountEvent(mce);
      } else {
        resetMessageCounts();
        fireMessageCountEvent(mce);
        if (removedProxies.size() > 0)
          getFolderTableModel().removeRows(removedProxies);
      }
    } else {
      resetMessageCounts();
      fireMessageCountEvent(mce);
    }
  }

  /**
   * This updates the TableInfo on the changed messages.
   *
   * As defined by java.mail.MessageChangedListener.
   */
  public void messageChanged(MessageChangedEvent e) {
    // blech.  we really have to do this on the action thread.

    if (Thread.currentThread() == getFolderThread() )
      runMessageChanged(e);
    else
      getFolderThread().addToQueue(new net.suberic.util.thread.ActionWrapper(new javax.swing.AbstractAction() {
          public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            runMessageChanged((MessageChangedEvent)actionEvent.getSource());
          }
        }, getFolderThread()), new java.awt.event.ActionEvent(e, 1, "message-changed"));
  }


  protected void runMessageChanged(MessageChangedEvent mce) {
    // if the message is getting deleted, then we don't
    // really need to update the table info.  for that
    // matter, it's likely that we'll get MessagingExceptions
    // if we do, anyway.
    boolean updateInfo = false;
    try {
      updateInfo = (!mce.getMessage().isSet(Flags.Flag.DELETED) || ! Pooka.getProperty("Pooka.autoExpunge", "true").equalsIgnoreCase("true"));
    } catch (MessagingException me) {
      // if we catch a MessagingException, it just means
      // that the message has already been expunged.  in
      // that case, assume it's ok if we don't update; it'll
      // happen in the messagesRemoved().
    }

    if (updateInfo) {
      try {
        MessageInfo mi = getMessageInfo(mce.getMessage());
        MessageProxy mp = mi.getMessageProxy();
        if (mp != null) {
          mp.unloadTableInfo();
          mp.loadTableInfo();
          if (mce.getMessageChangeType() == MessageChangedEvent.FLAGS_CHANGED)
            mi.refreshFlags();
          else if (mce.getMessageChangeType() == MessageChangedEvent.ENVELOPE_CHANGED)
            mi.refreshHeaders();
        }
      } catch (MessagingException me) {
        // if we catch a MessagingException, it just means
        // that the message has already been expunged.
      }

      // if we're not just a tableinfochanged event, do a resetmessagecouts.
      // don't do this if we're just a delete.
      if (! (mce instanceof net.suberic.pooka.event.MessageTableInfoChangedEvent)) {
        resetMessageCounts();
      }
    }

    fireMessageChangedEvent(mce);
  }

  /**
   * This puts up the gui for the Search.
   */
  public void showSearchFolder() {
    Pooka.getUIFactory().showSearchForm(new FolderInfo[] { this });
  }

  /**
   * This is a static calls which searches the given FolderInfo objects,
   * collects the results into a VirtualFolderInfo, and then displays
   * the results of the search in the UI.
   */
  public static void searchFolders(Vector folderList, javax.mail.search.SearchTerm term) {
    final javax.mail.search.SearchTerm searchTerm = term;
    final Vector selectedFolders = folderList;

    Pooka.getSearchThread().addToQueue(new net.suberic.util.thread.ActionWrapper(new javax.swing.AbstractAction() {
        public void actionPerformed(ActionEvent e) {
          Vector matchingValues = new Vector();
          Logger.getLogger("Pooka.debug").log(Level.FINE, "init:  matchingValues.size() = " + matchingValues.size());

          net.suberic.util.swing.ProgressDialog dialog = Pooka.getUIFactory().createProgressDialog(0,100,0,"Search","Searching");
          dialog.show();
          boolean cancelled = dialog.isCancelled();

          for (int i = 0; ! cancelled && i < selectedFolders.size(); i++) {
            Logger.getLogger("Pooka.debug").log(Level.FINE, "trying selected folder number " + i);
            try {
              net.suberic.pooka.MessageInfo[] matches = ((FolderInfo) selectedFolders.elementAt(i)).search(searchTerm);
              Logger.getLogger("Pooka.debug").log(Level.FINE, "matches.length = " + matches.length);
              for (int j = 0; j < matches.length; j++) {
                matchingValues.add(matches[j]);
                Logger.getLogger("Pooka.debug").log(Level.FINE, "adding " + matches[j] + " to matchingValues.");
              }

            } catch (MessagingException me) {
              Logger.getLogger("Pooka.debug").log(Level.FINE, "caught exception " + me);
            } catch (OperationCancelledException oce) {
            }
            cancelled = dialog.isCancelled();
          }

          Logger.getLogger("Pooka.debug").log(Level.FINE, "got " + matchingValues.size() + " matches.");

          if (! cancelled) {
            FolderInfo[] parentFolders = new FolderInfo[selectedFolders.size()];
            for (int i = 0; i < selectedFolders.size(); i++) {
              parentFolders[i] = (FolderInfo) selectedFolders.elementAt(i);
            }

            MessageInfo[] matchingMessages = new MessageInfo[matchingValues.size()];
            for (int i = 0; i < matchingValues.size(); i++) {
              Logger.getLogger("Pooka.debug").log(Level.FINE, "matchingValues.elementAt(" + i + ") = " + matchingValues.elementAt(i));
              matchingMessages[i] = (MessageInfo) matchingValues.elementAt(i);
            }

            final VirtualFolderInfo sfi = new VirtualFolderInfo(matchingMessages, parentFolders);

            Runnable runMe = new Runnable() {
                public void run() {
                  FolderDisplayUI fdui = Pooka.getUIFactory().createFolderDisplayUI(sfi);
                  fdui.openFolderDisplay();
                }
              };

            javax.swing.SwingUtilities.invokeLater(runMe);
          }

          dialog.dispose();
        }
      }, Pooka.getSearchThread()), new java.awt.event.ActionEvent(FolderInfo.class, 1, "search"));

  }

  /**
   * Searches for messages in this folder which match the given
   * SearchTerm.
   *
   * Basically wraps the call to Folder.search(), and then wraps the
   * returned Message objects as MessageInfos.
   */
  public MessageInfo[] search(javax.mail.search.SearchTerm term) throws MessagingException, OperationCancelledException {
    if (folderTableModel == null)
      loadAllMessages();

    Message[] matchingMessages = folder.search(term);
    MessageInfo returnValue[] = new MessageInfo[matchingMessages.length];
    for (int i = 0; i < matchingMessages.length; i++) {
      folderLog(Level.FINE, "match " + i + " = " + matchingMessages[i]);
      MessageInfo info = getMessageInfo(matchingMessages[i]);
      folderLog(Level.FINE, "messageInfo " + i + " = " + info);
      returnValue[i] = info;
    }
    folderLog(Level.FINE, "got " + returnValue.length + " results.");
    return returnValue;
  }

  /**
   * The resource for the default display filters.
   */
  protected String getDefaultDisplayFiltersResource() {
    if (isSentFolder())
      return "FolderInfo.sentFolderDefaultDisplayFilters";
    else
      return "FolderInfo.defaultDisplayFilters";
  }

  List filterHeaders = null;

  /**
   * This takes the FolderProperty.backendFilters and
   * FolderProperty.displayFilters properties and uses them to populate
   * the backendMessageFilters and displayMessageFilters arrays.
   */
  public void createFilters() {
    BackendMessageFilter[] tmpBackendFilters = null;
    MessageFilter[] tmpDisplayFilters = null;
    Vector backendFilterNames=Pooka.getResources().getPropertyAsVector(getFolderProperty() + ".backendFilters", "");

    if (backendFilterNames != null && backendFilterNames.size() > 0) {

      tmpBackendFilters = new BackendMessageFilter[backendFilterNames.size()];
      for (int i = 0; i < backendFilterNames.size(); i++) {
        tmpBackendFilters[i] = new BackendMessageFilter(getFolderProperty() + ".backendFilters." + (String) backendFilterNames.elementAt(i));
      }

      backendFilters = tmpBackendFilters;
    }

    Vector foundFilters = new Vector();
    Vector defaultFilterNames = Pooka.getResources().getPropertyAsVector(getDefaultDisplayFiltersResource(), "");

    for (int i = 0; i < defaultFilterNames.size(); i++) {
      foundFilters.add(new MessageFilter("FolderInfo.defaultDisplayFilters." + (String) defaultFilterNames.elementAt(i)));
    }

    Vector displayFilterNames=Pooka.getResources().getPropertyAsVector(getFolderProperty() + ".displayFilters", "");
    for (int i = 0; i < displayFilterNames.size(); i++) {
      foundFilters.add(new MessageFilter(getFolderProperty() + ".displayFilters." + (String) displayFilterNames.elementAt(i)));
    }

    tmpDisplayFilters = new MessageFilter[foundFilters.size()];
    for (int i = 0; i < foundFilters.size(); i++)
      tmpDisplayFilters[i] = (MessageFilter) foundFilters.elementAt(i);

    displayFilters = tmpDisplayFilters;

    filterHeaders = new LinkedList();
    // update the fetch profile with the headers from the display filters.
    for (int i = 0; i < tmpDisplayFilters.length; i++) {
      javax.mail.search.SearchTerm filterTerm = tmpDisplayFilters[i].getSearchTerm();
      if (filterTerm != null) {
        List headers = getHeaders(filterTerm);
        filterHeaders.addAll(headers);
      }
    }

    if (fetchProfile != null) {
      for (int i = 0; i < filterHeaders.size(); i++) {
        fetchProfile.add((String) filterHeaders.get(i));
      }
    }
  }

  /**
   * Gets all of the header strings for the given search term.
   */
  private List getHeaders(SearchTerm term) {
    List returnValue = new LinkedList();
    if (term instanceof HeaderTerm) {
      String headerName = ((HeaderTerm) term).getHeaderName();
      returnValue.add(headerName);
    } else if (term instanceof AndTerm) {
      SearchTerm[] terms = ((AndTerm)term).getTerms();
      for (int i = 0; i < terms.length; i++) {
        returnValue.addAll(getHeaders(terms[i]));
      }
    } else if (term instanceof OrTerm) {
      SearchTerm[] terms = ((OrTerm)term).getTerms();
      for (int i = 0; i < terms.length; i++) {
        returnValue.addAll(getHeaders(terms[i]));
      }
    } else if (term instanceof NotTerm) {
      SearchTerm otherTerm = ((NotTerm)term).getTerm();
      returnValue.addAll(getHeaders(otherTerm));
    } else if (term instanceof FromTerm || term instanceof FromStringTerm) {
      returnValue.add("From");
    } else if (term instanceof RecipientTerm || term instanceof RecipientStringTerm) {
      Message.RecipientType type;
      if (term instanceof RecipientTerm)
        type = ((RecipientTerm) term).getRecipientType();
      else
        type = ((RecipientStringTerm) term).getRecipientType();
      if (type == Message.RecipientType.TO)
        returnValue.add("To");
      else if (type == Message.RecipientType.CC)
        returnValue.add("Cc");
      else if (type == Message.RecipientType.BCC)
        returnValue.add("Bcc");
    }

    return returnValue;
  }

  /**
   * This applies each MessageFilter in filters array on the given
   * MessageProxy objects.
   *
   * @return a Vector containing the removed MessageProxy objects.
   */
  public Vector applyFilters(List messages) {
    return applyFilters(messages, null);
  }

  /**
   * This applies each MessageFilter in filters array on the given
   * MessageProxy objects.
   *
   * @return a Vector containing the removed MessageProxy objects.
   */
  public Vector applyFilters(List messages, net.suberic.util.swing.ProgressDialog pd) {
    Vector notRemovedYet = new Vector(messages);
    Vector removed = new Vector();
    if (backendFilters != null)
      for (int i = 0; i < backendFilters.length; i++) {
        if (backendFilters[i] != null) {
          List justRemoved = backendFilters[i].filterMessages(notRemovedYet, pd);
          removed.addAll(justRemoved);
          notRemovedYet.removeAll(justRemoved);
        }
      }

    if (removed.size() > 0) {
      try {
        expunge();
      } catch (OperationCancelledException oce) {
      } catch (MessagingException me) {
        me.printStackTrace();
      }
    }

    return removed;
  }

  // Accessor methods.

  /**
   * Returns the backend filters for this folder.
   */
  public BackendMessageFilter[] getBackendFilters() {
    return backendFilters;
  }

  public Action[] getActions() {
    return defaultActions;
  }

  public Folder getFolder() {
    return folder;
  }

  protected void setFolder(Folder newValue) {
    folder=newValue;
  }

  /**
   * This returns the FolderID, such as "myStore.INBOX".
   */
  public String getFolderID() {
    return folderID;
  }

  /**
   * This sets the folderID.
   */
  private void setFolderID(String newValue) {
    folderID=newValue;
  }

  /**
   * This returns the simple folderName, such as "INBOX".
   */
  public String getFolderName() {
    return mFolderName;
  }

  /**
   * This returns the folder display name, usually the FolderName plus
   * the store id.
   */
  public String getFolderDisplayName() {
    return mFolderName + " - " + getParentStore().getStoreID();
  }

  /**
   * This returns the property which defines this FolderInfo, such as
   * "Store.myStore.INBOX".
   */
  public String getFolderProperty() {
    return "Store." + getFolderID();
  }

  public Vector getChildren() {
    return children;
  }

  public FolderNode getFolderNode() {
    return folderNode;
  }

  public void setFolderNode(FolderNode newValue) {
    folderNode = newValue;
  }

  public FolderTableModel getFolderTableModel() {
    return folderTableModel;
  }

  public void setFolderTableModel(FolderTableModel newValue) {
    folderTableModel = newValue;
  }

  public List getColumnValues() {
    return columnValues;
  }

  public void setColumnValues(List newValue) {
    columnValues = newValue;
  }

  public List<String> getColumnIds() {
    return columnIds;
  }

  public void setColumnIds(List<String> newColumnIds) {
    columnIds = newColumnIds;
  }

  public List<String> getColumnNames() {
    return columnNames;
  }

  public void setColumnNames(List<String> newValue) {
    columnNames = newValue;
  }

  public List<String> getColumnSizes() {
    return columnSizes;
  }

  public void setColumnSizes(List<String> newValue) {
    columnSizes = newValue;
  }

  public FolderDisplayUI getFolderDisplayUI() {
    return folderDisplayUI;
  }

  protected void removeFromListeners(FolderDisplayUI display) {
    if (display != null) {
      removeMessageChangedListener(display);
      removeMessageCountListener(display);
      //getFolder().removeConnectionListener(display);
    }
  }

  protected void addToListeners(FolderDisplayUI display) {
    if (display != null) {
      addMessageChangedListener(display);
      addMessageCountListener(display);
      //getFolder().addConnectionListener(display);
    }
  }

  /**
   * This sets the given FolderDisplayUI to be the UI for this
   * FolderInfo.
   *
   * It automatically registers that FolderDisplayUI to be a listener
   * to MessageCount, MessageChanged, and Connection events.
   */
  public void setFolderDisplayUI(FolderDisplayUI newValue) {
    removeFromListeners(folderDisplayUI);
    folderDisplayUI = newValue;
    addToListeners(folderDisplayUI);
  }

  public int getType() {
    return type;
  }

  public boolean isConnected() {
    return (status == CONNECTED);
  }

  public boolean shouldBeConnected() {
    return (status < PASSIVE);
  }

  public boolean isSortaOpen() {
    return (status < CLOSED);
  }

  public boolean isAvailable() {
    return (status < NOT_LOADED);
  }

  public boolean isLoaded() {
    return (folder != null);
  }

  public boolean isValid() {
    return (status != INVALID);
  }

  public boolean hasUnread() {
    return (tracksUnreadMessages() && unreadCount > 0);
  }

  public int getUnreadCount() {
    if (!tracksUnreadMessages())
      return 0;
    else
      return unreadCount;
  }

  public int getMessageCount() {
    return messageCount;
  }

  public boolean hasNewMessages() {
    return newMessages;
  }

  public void setNewMessages(boolean newValue) {
    newMessages = newValue;
  }

  public FolderTracker getFolderTracker() {
    return folderTracker;
  }

  public void setFolderTracker(FolderTracker newTracker) {
    folderTracker = newTracker;
  }

  public boolean isTrashFolder() {
    return trashFolder;
  }

  /**
   * This sets the trashFolder value.  it also resets the defaultAction
   * list and erases the FolderNode's popupMenu, if there is one.
   */
  public void setTrashFolder(boolean newValue) {
    trashFolder = newValue;
    if (newValue) {
      setNotifyNewMessagesMain(false);
      setNotifyNewMessagesNode(false);
    } else {
      if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesMain", "").equalsIgnoreCase("false"))
        setNotifyNewMessagesMain(true);
      else
        setNotifyNewMessagesMain(false);

      if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesNode", "").equalsIgnoreCase("false"))
        setNotifyNewMessagesNode(true);
      else
        setNotifyNewMessagesNode(false);
    }

    resetDefaultActions();
    if (getFolderNode() != null)
      getFolderNode().popupMenu = null;
  }


  public boolean isSentFolder() {
    return sentFolder;
  }


  public void setSentFolder(boolean newValue) {
    sentFolder = newValue;
    if (newValue) {
      setNotifyNewMessagesMain(false);
      setNotifyNewMessagesNode(false);
    } else {
      if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesMain", "").equalsIgnoreCase("false"))
        setNotifyNewMessagesMain(true);
      else
        setNotifyNewMessagesMain(false);

      if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesNode", "").equalsIgnoreCase("false"))
        setNotifyNewMessagesNode(true);
      else
        setNotifyNewMessagesNode(false);
    }
    setTracksUnreadMessages (! newValue);
    createFilters();
  }

  /**
   * Returns whether or not this is an Outbox for an OutgoingMailServer.
   */
  public boolean isOutboxFolder() {
    return (mailServer != null);
  }

  /**
   * Sets this as an Outbox for the given OutgoingMailServer.  If this
   * is getting removed as an outbox, set the server to null.
   */
  public void setOutboxFolder(OutgoingMailServer newServer) {
    mailServer = newServer;
    if (newServer != null) {
      setNotifyNewMessagesMain(false);
      setNotifyNewMessagesNode(false);
    } else {
      if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesMain", "").equalsIgnoreCase("false"))
        setNotifyNewMessagesMain(true);
      else
        setNotifyNewMessagesMain(false);

      if (!Pooka.getProperty(getFolderProperty() + ".notifyNewMessagesNode", "").equalsIgnoreCase("false"))
        setNotifyNewMessagesNode(true);
      else
        setNotifyNewMessagesNode(false);
    }

    resetDefaultActions();
  }

  public boolean notifyNewMessagesMain() {
    return notifyNewMessagesMain;
  }

  public void setNotifyNewMessagesMain(boolean newValue) {
    notifyNewMessagesMain = newValue;
  }

  public boolean notifyNewMessagesNode() {
    return notifyNewMessagesNode;
  }

  public void setNotifyNewMessagesNode(boolean newValue) {
    notifyNewMessagesNode = newValue;
  }

  public void setTracksUnreadMessages(boolean newValue) {
    tracksUnreadMessages = newValue;
  }

  public boolean tracksUnreadMessages() {
    return tracksUnreadMessages;
  }

  public MessageFilter[] getDisplayFilters() {
    return displayFilters;
  }

  /**
   * This forces an update of both the total and unread message counts.
   */
  public void resetMessageCounts() {
    try {
      if (getFolder() != null)
        folderLog(Level.FINE, "running resetMessageCounts.  unread message count is " + getFolder().getUnreadMessageCount());
      else
        folderLog(Level.FINE, "running resetMessageCounts.  getFolder() is null.");

      if (tracksUnreadMessages()) {
        unreadCount = getFolder().getUnreadMessageCount();
      }

      messageCount = getFolder().getMessageCount();
    } catch (MessagingException me) {
      // if we lose the connection to the folder, we'll leave the old
      // messageCount and set the unreadCount to zero.
      unreadCount = 0;
    }
    updateNode();
  }

  /**
   * This returns the parentFolder.  If this FolderInfo is a direct
   * child of a StoreInfo, this method will return null.
   */
  public FolderInfo getParentFolder() {
    return parentFolder;
  }

  /**
   * This method actually returns the parent StoreInfo.  If this
   * particular FolderInfo is a child of another FolderInfo, this
   * method will call getParentStore() on that FolderInfo.
   */
  public StoreInfo getParentStore() {
    if (parentStore == null)
      return parentFolder.getParentStore();
    else
      return parentStore;
  }

  public UserProfile getDefaultProfile() {
    if (defaultProfile != null) {
      return defaultProfile;
    } else if (parentFolder != null) {
      return parentFolder.getDefaultProfile();
    } else if (parentStore != null) {
      return parentStore.getDefaultProfile();
    } else {
      return null;
    }
  }

  /**
   * sets the status.
   */
  public void setStatus(int newStatus) {
    synchronized(this) {
      status = newStatus;
    }
  }

  /**
   * gets the status.
   */
  public int getStatus() {
    return status;
  }

  public ActionThread getFolderThread() {
    return getParentStore().getStoreThread();
  }

  public FolderInfo getTrashFolder() {
    return getParentStore().getTrashFolder();
  }

  public FetchProfile getFetchProfile() {
    return fetchProfile;
  }

  /**
   * Returns whether or not this folder is actually a namespace.
   */
  public boolean isNamespace() {
    return mNamespace;
  }

  /**
   * Shows a status message, using the given FolderDisplayUI if not null.
   */
  public void showStatusMessage(net.suberic.pooka.gui.FolderDisplayUI pUI, String message) {
    if (pUI != null)
      pUI.showStatusMessage(message);
    else
      Pooka.getUIFactory().showStatusMessage(message);
  }

  /**
   * Clears the status message.
   */
  public void clearStatusMessage(net.suberic.pooka.gui.FolderDisplayUI pUI) {
    if (pUI != null)
      pUI.clearStatusMessage();
    else
      Pooka.getUIFactory().clearStatus();
  }

  /**
   * Returns the logger for this Folder.
   */
  public Logger getLogger() {
    if (mLogger == null) {
      mLogger = Logger.getLogger(getFolderProperty());
    }
    return mLogger;
  }

  /**
   * Logs a message for this folder.
   */
  public void folderLog(Level l, String message) {
    getLogger().log(l, getFolderID() + ":  " + message);
  }

  class EditPropertiesAction extends AbstractAction {

    EditPropertiesAction() {
      super("file-edit");
    }

    public void actionPerformed(ActionEvent e) {
      Pooka.getUIFactory().showEditorWindow(getFolderProperty(), getFolderProperty(), "Folder.editor");
    }
  }

  class UpdateCountAction extends AbstractAction {

    UpdateCountAction() {
      super("folder-update");
    }

    public void actionPerformed(ActionEvent e) {
      // should always be on the Folder thread.
      try {
        checkFolder();
      } catch (OperationCancelledException oce) {
      } catch (MessagingException me) {
        final MessagingException me2 = me;

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              if (getFolderDisplayUI() != null)
                getFolderDisplayUI().showError(Pooka.getProperty("error.updatingFolder", "Error updating Folder ") + getFolderID(), me2);
              else
                Pooka.getUIFactory().showError(Pooka.getProperty("error.updatingFolder", "Error updating Folder ") + getFolderID(), me2);

            }
          });

      }
    }
  }

  class EmptyTrashAction extends AbstractAction {

    EmptyTrashAction() {
      super("folder-empty");
    }

    public void actionPerformed(ActionEvent e) {
      emptyTrash();
    }
  }


  class SendAllAction extends AbstractAction {

    SendAllAction() {
      super("folder-send");
    }

    public void actionPerformed(ActionEvent e) {
      if (isOutboxFolder())
        mailServer.sendAll();
    }
  }

}
