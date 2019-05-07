package net.suberic.pooka.cache;
import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.MimeMessage;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.ConnectionEvent;
import java.util.Vector;
import java.util.List;
import java.util.logging.*;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.io.File;
import net.suberic.pooka.*;
import net.suberic.pooka.gui.MessageProxy;
import net.suberic.pooka.gui.FolderTableModel;
import net.suberic.util.thread.ActionThread;

/**
 * A FolderInfo which caches its messages in a MessageCache.
 * @author Allen Petersen
 * @version $Revision: 1718 $
 */

public class CachingFolderInfo extends net.suberic.pooka.UIDFolderInfo {
  private MessageCache cache = null;

  // the resource for the folder disconnected message
  protected static String disconnectedMessage = "error.CachingFolder.disconnected";

  boolean autoCache = false;

  public CachingFolderInfo(StoreInfo parent, String fname) {
    super(parent, fname);

    if (! getCacheHeadersOnly()) {
      autoCache =  Pooka.getProperty(getFolderProperty() + ".autoCache", Pooka.getProperty(getParentStore().getStoreProperty() + ".autoCache", Pooka.getProperty("Pooka.autoCache", "false"))).equalsIgnoreCase("true");
    }

    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".autoCache");
  }

  public CachingFolderInfo(FolderInfo parent, String fname) {
    super(parent, fname);

    if (! getCacheHeadersOnly()) {
      autoCache =  Pooka.getProperty(getFolderProperty() + ".autoCache", Pooka.getProperty(getParentStore().getStoreProperty() + ".autoCache", Pooka.getProperty("Pooka.autoCache", "false"))).equalsIgnoreCase("true");
    }

    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".autoCache");
    Pooka.getResources().addValueChangeListener(this, getFolderProperty() + ".cacheHeadersOnly");
    Pooka.getResources().addValueChangeListener(this, getParentStore().getStoreProperty() + ".autoCache");
    Pooka.getResources().addValueChangeListener(this, getParentStore().getStoreProperty() + ".cacheHeadersOnly");
  }

  /**
   * Loads the column names and sizes.
   */
  protected FetchProfile createColumnInformation() {
    FetchProfile fp = super.createColumnInformation();
    // we need to get the full headers for the CachingFolderInfo.
    fp = new FetchProfile();
    fp.add(FetchProfile.Item.FLAGS);
    fp.add(com.sun.mail.imap.IMAPFolder.FetchProfileItem.HEADERS);
    return fp;
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
  public void loadFolder(boolean pConnectStore) throws OperationCancelledException {
    if (cache == null) {
      try {
        this.cache = new SimpleFileCache(this, getCacheDirectory());
        type =  type | Folder.HOLDS_MESSAGES;
        setStatus(DISCONNECTED);
      } catch (java.io.IOException ioe) {
        System.out.println("Error creating cache!");
        ioe.printStackTrace();
        return;
      }
    }

    if (isLoaded() || (loading && children == null))
      return;

    Folder[] tmpFolder = null;
    Folder tmpParentFolder;

    try {
      loading = true;
      if (getParentStore().isConnected()) {
        if (getParentFolder() == null) {
          try {
            if (getLogger().isLoggable(Level.FINE))
              getLogger().log(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  checking parent store connection.");

            Store store = getParentStore().getStore();
            // first see if we're a namespace
            try {
              if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, "checking to see if " + getFolderID() + " is a shared folder.");
              }

              Folder[] sharedFolders = store.getSharedNamespaces();

              if (sharedFolders != null && sharedFolders.length > 0) {
                for (int i = 0; ( tmpFolder == null || tmpFolder.length == 0 ) && i < sharedFolders.length; i++) {
                  if (sharedFolders[i].getName().equalsIgnoreCase(getFolderName())) {

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
              if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, "got " + tmpParentFolder + " as Default Folder for store.");
                getLogger().log(Level.FINE, "doing a list on default folder " + tmpParentFolder + " for folder " + getFolderName());
              }

              tmpFolder = tmpParentFolder.list(getFolderName());
            }

            if (getLogger().isLoggable(Level.FINE))
              getLogger().log(Level.FINE, "got " + tmpFolder + " as Folder for folder " + getFolderID() + ".");

          } catch (MessagingException me) {
            me.printStackTrace();

            if (getLogger().isLoggable(Level.FINE)) {
              getLogger().log(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  caught messaging exception from parentStore getting folder: " + me);
              me.printStackTrace();
            }
            tmpFolder =null;
          }
        } else {
          if (!getParentFolder().isLoaded())
            getParentFolder().loadFolder();
          if (!getParentFolder().isLoaded()) {
            tmpFolder = null;
          } else {
            tmpParentFolder = getParentFolder().getFolder();
            if (tmpParentFolder != null) {
              tmpFolder = tmpParentFolder.list(getFolderName());
            } else {
              tmpFolder = null;
            }
          }
        }
        if (tmpFolder != null && tmpFolder.length > 0) {
          setFolder(tmpFolder[0]);
          if (! getFolder().isSubscribed())
            getFolder().setSubscribed(true);
          setStatus(CLOSED);
          getFolder().addMessageChangedListener(this);
        } else {
          if (cache != null)
            setStatus(CACHE_ONLY);
          else
            setStatus(INVALID);
          setFolder(new FolderProxy(getFolderName()));
        }
      } else {
        setFolder(new FolderProxy(getFolderName()));
      }

    } catch (MessagingException me) {
      if (getLogger().isLoggable(Level.FINE)) {
        getLogger().log(Level.FINE, Thread.currentThread() + "loading folder " + getFolderID() + ":  caught messaging exception; setting loaded to false:  " + me.getMessage() );
        me.printStackTrace();
      }
      setStatus(NOT_LOADED);
      setFolder(new FolderProxy(getFolderName()));
    } finally {
      initializeFolderInfo();
      loading = false;
    }
  }

  /**
   * called when the folder is opened.
   */
  public void opened (ConnectionEvent e) {
    super.opened(e);
    rematchFilters();
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
    try {
      getLogger().log(Level.FINE, this + ":  checking parent store.");

      if (!getParentStore().isConnected() && pConnectStore && getParentStore().getPreferredStatus() == FolderInfo.CONNECTED) {
        getLogger().log(Level.FINE, this + ":  parent store isn't connected.  trying connection.");
        try {
          getParentStore().connectStore();
        } catch (OperationCancelledException oce) {
          getLogger().log(Level.INFO, "Login cancelled.");
        }
      }

      getLogger().log(Level.FINE, this + ":  loading folder.");

      if (! isLoaded() && status != CACHE_ONLY)
        loadFolder();

      getLogger().log(Level.FINE, this + ":  folder loaded.  status is " + status + "; checked on parent store.  trying isLoaded() and isAvailable().");

      if (status == CLOSED || status == LOST_CONNECTION || status == DISCONNECTED) {
        getLogger().log(Level.FINE, this + ":  isLoaded() and isAvailable().");
        if (getFolder().isOpen()) {
          if (getFolder().getMode() == mode)
            return;
          else {
            getFolder().close(false);
            openFolder(mode);
          }
        } else {
          Folder f = getFolder();
          getFolder().open(mode);
          updateFolderOpenStatus(true);
          resetMessageCounts();
        }
      } else if (status == INVALID) {
        throw new MessagingException(Pooka.getProperty("error.folderInvalid", "Error:  folder is invalid.  ") + getFolderID());
      }
    } catch (MessagingException me) {
      setStatus(DISCONNECTED);
      throw me;
    } finally {
      resetMessageCounts();
    }
  }


  /**
   * Called when the store in disconnected.
   */
  public void disconnected(ConnectionEvent e) {
    super.disconnected(e);
    rematchFilters();
  }

  /**
   * Called when the folder is closed.
   */
  public void closed(ConnectionEvent e) {
    super.closed(e);
    rematchFilters();
  }

  /**
   * gets all of the message proxies associated with this folder info
   * and notifies them that they need to rematch their filters.
   */
  protected void rematchFilters() {
    if (folderTableModel != null) {
      List allProxies = folderTableModel.getAllProxies();
      for (int i = 0; i < allProxies.size(); i++) {
        ((MessageProxy) allProxies.get(i)).clearMatchedFilters();
      }
      //loaderThread.loadMessages(allProxies);
      mMessageLoader.loadMessages(allProxies);
    }
  }

  /**
   * During loadAllMessages, updates the display to say that we're loading
   * messages.
   */
  protected void updateDisplay(boolean start) {
    if (getFolderDisplayUI() != null) {
      if (start) {
        getFolderDisplayUI().setBusy(true);
        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("messages.CachingFolder.loading.starting", "Loading messages."));
      } else {
        getFolderDisplayUI().setBusy(false);
        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("messages.CachingFolder.loading.finished", "Done loading messages."));
      }
    }
  }

  /**
   * While loading messages, attempts to update the folder status.
   */
  protected void updateFolderStatusForLoading() throws MessagingException, OperationCancelledException {
    if (preferredStatus < DISCONNECTED && !(isConnected() && getParentStore().getConnection().getStatus() == NetworkConnection.CONNECTED )) {
      try {
        openFolder(Folder.READ_WRITE);
      } catch (MessagingException me) {
        uidValidity = cache.getUIDValidity();
        preferredStatus = DISCONNECTED;
      }
    }
  }

  /**
   * Loads the MessageInfos and MesageProxies.  Returns a List of
   * newly created MessageProxies.
   */
  protected List createInfosAndProxies() throws MessagingException {

    List messageProxies = new Vector();

    if (getStatus() > CONNECTED) {
      uidValidity = cache.getUIDValidity();
    }

    if (isConnected()) {
      try {
        // load the list of uid's.

        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.CachingFolder.synchronizing.writingChanges", "Writing local changes to server..."));

        // first write all the changes that we made back to the server.
        getCache().writeChangesToServer(getFolder());

        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing.loading", "Loading messages from folder..."));

        FetchProfile uidFetchProfile = new FetchProfile();
        uidFetchProfile.add(UIDFolder.FetchProfileItem.UID);
        getLogger().log(Level.FINE, "getting messages.");

        Message[] messages = getFolder().getMessages();
        getLogger().log(Level.FINE, "fetching messages.");
        getFolder().fetch(messages, uidFetchProfile);
        getLogger().log(Level.FINE, "done fetching messages.  getting uid's");

        long[] uids = new long[messages.length];

        for (int i = 0; i < messages.length; i++) {
          uids[i] = getUID(messages[i]);
        }

        MessageInfo mi;

        for (int i = 0; i < uids.length; i++) {
          Message m = new CachingMimeMessage(this, uids[i]);
          mi = new MessageInfo(m, this);

          messageProxies.add(new MessageProxy(getColumnValues() , mi));
          messageToInfoTable.put(m, mi);
          uidToInfoTable.put(new Long(uids[i]), mi);
        }

        return messageProxies;
      } catch (Exception e) {
        final Exception fe = e;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              if (getFolderDisplayUI() != null) {
                getFolderDisplayUI().showError(Pooka.getProperty("error.CachingFolder.synchronzing", "Error synchronizing with folder"), Pooka.getProperty("error.CachingFolder.synchronzing.title", "Error synchronizing with folder"), fe);
              } else {
                Pooka.getUIFactory().showError(Pooka.getProperty("error.CachingFolder.synchronzing", "Error synchronizing with folder"), Pooka.getProperty("error.CachingFolder.synchronzing.title", "Error synchronizing with folder"), fe);

              }
            }
          });
      }
    }

    long[] uids = cache.getMessageUids();
    MessageInfo mi;

    for (int i = 0; i < uids.length; i++) {
      Message m = new CachingMimeMessage(this, uids[i]);
      mi = new MessageInfo(m, this);
      MessageProxy mp = new MessageProxy(getColumnValues() , mi);
      mp.setRefresh(true);
      messageProxies.add(mp);
      messageToInfoTable.put(m, mi);
      uidToInfoTable.put(new Long(uids[i]), mi);
    }

    return messageProxies;
  }

  /**
   * Updates any caching information, if necessary.
   */
  protected void updateCache() throws MessagingException {
    if (isConnected()) {
      synchronizeCache();
    }
  }

  /**
   * Fetches the information for the given messages using the given
   * FetchProfile.
   */
  /* original version
     public void fetch(MessageInfo[] messages, FetchProfile profile) throws MessagingException {
     // if we're connected, go ahead and fetch these.

     if (getLogger().isLoggable(Level.FINE)) {
     if (messages == null)
     getLogger().log(Level.FINE, "cachingFolderInfo:  fetching with null messages.");
     else
     getLogger().log(Level.FINE, "cachingFolderInfo:  fetching " + messages.length + " messages.");
     }

     int cacheStatus = -1;
     boolean doFlags = profile.contains(FetchProfile.Item.FLAGS);
     boolean doHeaders = (profile.contains(FetchProfile.Item.ENVELOPE) || profile.contains(FetchProfile.Item.CONTENT_INFO));

     if (doFlags && doHeaders) {
     cacheStatus = SimpleFileCache.FLAGS_AND_HEADERS;
     } else if (doFlags) {
     cacheStatus = SimpleFileCache.FLAGS;
     } else if (doHeaders) {
     cacheStatus = SimpleFileCache.HEADERS;
     }

     if (isConnected()) {

     if (getLogger().isLoggable(Level.FINE)) {
     getLogger().log(Level.FINE, "CachingFolderInfo:  running fetch against folder.");
     }
     super.fetch(messages, profile);

     if (cacheStatus != -1) {
     for (int i = 0; i < messages.length; i++) {
     Message m = messages[i].getRealMessage();
     if (m != null) {
     long uid = getUID(m);
     getCache().cacheMessage((MimeMessage)m, uid, cache.getUIDValidity(), cacheStatus);
     }
     }
     }
     } else {
     // if we're not connected, then go ahead and preload the cache for
     // these.
     for (int i = 0; i < messages.length; i++) {
     Message current = messages[i].getMessage();
     if (current != null && current instanceof UIDMimeMessage) {
     long uid = ((UIDMimeMessage) current).getUID();
     if (cacheStatus == SimpleFileCache.FLAGS_AND_HEADERS || cacheStatus == SimpleFileCache.FLAGS) {
     getCache().getFlags(uid, cache.getUIDValidity());
     }

     if (cacheStatus == SimpleFileCache.FLAGS_AND_HEADERS || cacheStatus == SimpleFileCache.HEADERS) {
     getCache().getHeaders(uid, cache.getUIDValidity());
     }
     }

     messages[i].setFetched(true);
     }
     }

     }
  */
  // new version
  public void fetch(MessageInfo[] messages, FetchProfile profile) throws MessagingException {
    // if we're connected, go ahead and fetch these.

    if (getLogger().isLoggable(Level.FINE)) {
      if (messages == null)
        getLogger().log(Level.FINE, "cachingFolderInfo:  fetching with null messages.");
      else
        getLogger().log(Level.FINE, "cachingFolderInfo:  fetching " + messages.length + " messages.");
    }

    int cacheStatus = -1;
    boolean doFlags = profile.contains(FetchProfile.Item.FLAGS);
    String[] headers = profile.getHeaderNames();
    boolean doHeaders = (profile.contains(FetchProfile.Item.ENVELOPE) || profile.contains(FetchProfile.Item.CONTENT_INFO) || profile.contains(com.sun.mail.imap.IMAPFolder.FetchProfileItem.HEADERS) || (headers != null && headers.length > 0));

    if (doFlags && doHeaders) {
      cacheStatus = SimpleFileCache.FLAGS_AND_HEADERS;
    } else if (doFlags) {
      cacheStatus = SimpleFileCache.FLAGS;
    } else if (doHeaders) {
      cacheStatus = SimpleFileCache.HEADERS;
    }

    if (isConnected()) {

      if (getLogger().isLoggable(Level.FINE)) {
        getLogger().log(Level.FINE, "CachingFolderInfo:  connected.  checking for already-cached messages.");
      }

      if (doHeaders) {
        // assume that headers don't change.
        FetchProfile fp = new FetchProfile();
        if (doFlags) {
          fp.add(FetchProfile.Item.FLAGS);
        }

        java.util.LinkedList flagsOnly = new java.util.LinkedList();
        java.util.LinkedList headersAndFlags = new java.util.LinkedList();

        for (int i = 0 ; i < messages.length; i++) {
          Message current = messages[i].getMessage();
          if (current != null && current instanceof UIDMimeMessage) {
            long uid = ((UIDMimeMessage) current).getUID();
            if (getCache().getCacheStatus(((UIDMimeMessage) current).getUID()) >= MessageCache.HEADERS) {
              flagsOnly.add(messages[i]);
            } else {
              headersAndFlags.add(messages[i]);
            }
          }
        }

        if (getLogger().isLoggable(Level.FINE)) {
          getLogger().log(Level.FINE, "CachingFolderInfo:  running fetch against " + headersAndFlags.size() + " full messages, plus " + flagsOnly.size() + " flags-only messages");
        }

        MessageInfo[] headersAndFlagsArray = (MessageInfo[]) headersAndFlags.toArray(new MessageInfo[0]);
        MessageInfo[] flagsOnlyArray = (MessageInfo[]) flagsOnly.toArray(new MessageInfo[0]);
        super.fetch(headersAndFlagsArray, profile);
        super.fetch(flagsOnlyArray, fp);

        if (cacheStatus != -1) {
          for (int i = 0; i < headersAndFlagsArray.length; i++) {
            Message m = headersAndFlagsArray[i].getRealMessage();
            if (m != null) {
              long uid = getUID(m);
              getCache().cacheMessage((MimeMessage)m, uid, cache.getUIDValidity(), cacheStatus);
            }
          }

          for (int i = 0; i < flagsOnlyArray.length; i++) {
            Message m = flagsOnlyArray[i].getRealMessage();
            if (m != null) {
              long uid = getUID(m);
              getCache().cacheMessage((MimeMessage)m, uid, cache.getUIDValidity(), MessageCache.FLAGS);
            }
          }
        }
      } else {
        if (getLogger().isLoggable(Level.FINE)) {
          getLogger().log(Level.FINE, "CachingFolderInfo:  running fetch against folder.");
        }
        super.fetch(messages, profile);

        if (cacheStatus != -1) {
          for (int i = 0; i < messages.length; i++) {
            Message m = messages[i].getRealMessage();
            if (m != null) {
              long uid = getUID(m);
              getCache().cacheMessage((MimeMessage)m, uid, cache.getUIDValidity(), cacheStatus);
            }
          }
        }

      }
    } else {
      // if we're not connected, then go ahead and preload the cache for
      // these.
      for (int i = 0; i < messages.length; i++) {
        Message current = messages[i].getMessage();
        if (current != null && current instanceof UIDMimeMessage) {
          long uid = ((UIDMimeMessage) current).getUID();
          if (cacheStatus == SimpleFileCache.FLAGS_AND_HEADERS || cacheStatus == SimpleFileCache.FLAGS) {
            getCache().getFlags(uid, cache.getUIDValidity());
          }

          if (cacheStatus == SimpleFileCache.FLAGS_AND_HEADERS || cacheStatus == SimpleFileCache.HEADERS) {
            getCache().getHeaders(uid, cache.getUIDValidity());
          }
        }

        messages[i].setFetched(true);
      }
    }

  }
  // end new version
  /**
   * Refreshes the headers for the given MessageInfo.
   */
  public void refreshHeaders(MessageInfo mi) throws MessagingException {
    cacheMessage(mi, SimpleFileCache.HEADERS);
  }

  /**
   * Refreshes the flags for the given MessageInfo.
   */
  public void refreshFlags(MessageInfo mi) throws MessagingException {
    if (isConnected())
      cacheMessage(mi, SimpleFileCache.FLAGS);
  }


  /**
   * Gets the row number of the first unread message.  Returns -1 if
   * there are no unread messages, or if the FolderTableModel is not
   * set or empty.
   */

  public int getFirstUnreadMessage() {
    // one part brute, one part force, one part ignorance.

    if (getLogger().isLoggable(Level.FINE))
      getLogger().log(Level.FINE, "getting first unread message");

    if (! tracksUnreadMessages())
      return -1;

    if (getFolderTableModel() == null)
      return -1;

    if (isConnected()) {
      return super.getFirstUnreadMessage();
    } else {
      try {
        int countUnread = 0;
        int i;

        int unreadCount = cache.getUnreadMessageCount();

        if (unreadCount > 0) {
          long[] uids = getCache().getMessageUids();

          for (i = uids.length - 1; ( i >= 0 && countUnread < unreadCount) ; i--) {
            MessageInfo mi = getMessageInfoByUid(uids[i]);

            if (! mi.getFlags().contains(Flags.Flag.SEEN))
              countUnread++;
          }
          if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "Returning " + i);

          return i + 1;
        } else {
          if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "Returning -1");
          return -1;
        }
      } catch (MessagingException me) {
        if (getLogger().isLoggable(Level.FINE))
          getLogger().log(Level.FINE, "Messaging Exception.  Returning -1");
        return -1;
      }
    }

  }

  public boolean hasUnread() {
    if (! tracksUnreadMessages())
      return false;
    else
      return (getUnreadCount() > 0);
  }

  /*
    public int getUnreadCount() {
    if (! tracksUnreadMessages())
    return -1;
    else {
    try {
    if (getCache() != null)
    unreadCount = getCache().getUnreadMessageCount();
    } catch (MessagingException me) {

    }
    return unreadCount;
    }
    }

    public int getMessageCount() {
    try {
    if (getCache() != null)
    messageCount = getCache().getMessageCount();
    } catch (MessagingException me) {
    }
    return messageCount;
    }
  */

  /**
   * This forces an update of both the total and unread message counts.
   */
  public void resetMessageCounts() {
    try {
      if (getLogger().isLoggable(Level.FINE)) {
        if (getFolder() != null)
          getLogger().log(Level.FINE, "running resetMessageCounts.  unread message count is " + getFolder().getUnreadMessageCount());
        else
          getLogger().log(Level.FINE, "running resetMessageCounts.  getFolder() is null.");
      }

      if (isConnected()) {
        if (tracksUnreadMessages())
          unreadCount = getFolder().getUnreadMessageCount();
        messageCount = getFolder().getMessageCount();
      } else if (getCache() != null) {
        messageCount = getCache().getMessageCount();
        if (tracksUnreadMessages())
          unreadCount = getCache().getUnreadMessageCount();
      } else {
        // if there's no cache and no connection, don't do anything.
      }
    } catch (MessagingException me) {
      // if we lose the connection to the folder, we'll leave the old
      // messageCount and set the unreadCount to zero.
      unreadCount = 0;
    }
    updateNode();
  }

  /**
   * This synchronizes the cache with the new information from the
   * Folder.
   */
  public void synchronizeCache() throws MessagingException {

    if (getLogger().isLoggable(Level.FINE))
      getLogger().log(Level.FINE, "synchronizing cache.");

    try {
      showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing", "Re-synchronizing with folder..."));
      if (getFolderDisplayUI() != null) {
        getFolderDisplayUI().setBusy(true);
      }

      long cacheUidValidity = getCache().getUIDValidity();

      if (uidValidity != cacheUidValidity) {
        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("error.UIDFolder.validityMismatch", "Error:  validity not correct.  reloading..."));

        getCache().invalidateCache();
        getCache().setUIDValidity(uidValidity);
        cacheUidValidity = uidValidity;
      }


      showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.CachingFolder.synchronizing.writingChanges", "Writing local changes to server..."));

      // first write all the changes that we made back to the server.
      showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronzing.writingChanges", "Writing local changes to server"));

      getCache().writeChangesToServer(getFolder());

      showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing.loading", "Loading messages from folder..."));

      // load the list of uid's.

      FetchProfile fp = new FetchProfile();
      fp.add(UIDFolder.FetchProfileItem.UID);
      // adding FLAGS to make getFirstUnreadMessage() more efficient
      fp.add(FetchProfile.Item.FLAGS);

      if (getLogger().isLoggable(Level.FINE))
        getLogger().log(Level.FINE, "getting messages.");

      Message[] messages = getFolder().getMessages();

      if (getLogger().isLoggable(Level.FINE))
        getLogger().log(Level.FINE, "fetching messages.");

      String messageCount = messages == null ? "null" : Integer.toString(messages.length);

      showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing.fetchingMessages", "Fetching") + " " + messageCount + " " + Pooka.getProperty("message.UIDFolder.synchronizing.messages", "messages."));

      getFolder().fetch(messages, fp);
      if (getLogger().isLoggable(Level.FINE))
        getLogger().log(Level.FINE, "done fetching messages.  getting uid's");

      long[] uids = new long[messages.length];

      for (int i = 0; i < messages.length; i++) {
        uids[i] = getUID(messages[i]);
      }

      if (getLogger().isLoggable(Level.FINE))
        getLogger().log(Level.FINE, "synchronizing--uids.length = " + uids.length);

      showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing", "Comparing new messages to current list..."));

      long[] addedUids = cache.getAddedMessages(uids, uidValidity);

      if (getLogger().isLoggable(Level.FINE))
        getLogger().log(Level.FINE, "synchronizing--addedUids.length = " + addedUids.length);

      if (addedUids.length > 0) {
        Message[] addedMsgs = ((UIDFolder)getFolder()).getMessagesByUID(addedUids);
        MessageCountEvent mce = new MessageCountEvent(getFolder(), MessageCountEvent.ADDED, false, addedMsgs);

        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing.loadingMessages", "Loading") + " " + addedUids.length + " " + Pooka.getProperty("message.UIDFolder.synchronizing.messages", "messages."));

        messagesAdded(mce);
      }

      long[] removedUids = cache.getRemovedMessages(uids, uidValidity);
      if (getLogger().isLoggable(Level.FINE))
        getLogger().log(Level.FINE, "synchronizing--removedUids.length = " + removedUids.length);

      if (removedUids.length > 0) {
        Message[] removedMsgs = new Message[removedUids.length];
        for (int i = 0 ; i < removedUids.length; i++) {

          MessageInfo mi =  getMessageInfoByUid(removedUids[i]);
          if (mi != null)
            removedMsgs[i] = mi.getMessage();

          if (removedMsgs[i] == null) {
            removedMsgs[i] = new CachingMimeMessage(this, removedUids[i]);
          }
        }
        MessageCountEvent mce = new MessageCountEvent(getFolder(), MessageCountEvent.REMOVED, false, removedMsgs);

        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing.removingMessages", "Removing") + " " + removedUids.length + " " + Pooka.getProperty("message.UIDFolder.synchronizing.messages", "messages."));

        messagesRemoved(mce);
      }

      updateFlags(uids, messages, cacheUidValidity);

    } finally {
      if (getFolderDisplayUI() != null) {
        getFolderDisplayUI().clearStatusMessage();
        getFolderDisplayUI().setBusy(false);
      } else
        Pooka.getUIFactory().clearStatus();
    }

  }

  protected void runMessagesAdded(MessageCountEvent mce) {
    if (folderTableModel != null) {
      Message[] addedMessages = mce.getMessages();

      int fetchBatchSize = 25;
      int loadBatchSize = 25;
      try {
        fetchBatchSize = Integer.parseInt(Pooka.getProperty("Pooka.fetchBatchSize", "50"));
      } catch (NumberFormatException nfe) {
      }

      MessageInfo mi;
      Vector addedProxies = new Vector();
      List addedInfos = new java.util.ArrayList();
      for (int i = 0; i < addedMessages.length; i++) {
        if (addedMessages[i] instanceof CachingMimeMessage) {
          long uid = ((CachingMimeMessage) addedMessages[i]).getUID();
          mi = getMessageInfoByUid(uid);
          if (mi != null) {
            addedInfos.add(mi);
            if (getLogger().isLoggable(Level.FINE))
              getLogger().log(Level.FINE, getFolderID() + ":  this is a duplicate.  not making a new messageinfo for it.");
          } else {
            mi = new MessageInfo(addedMessages[i], CachingFolderInfo.this);
            addedInfos.add(mi);
            addedProxies.add(new MessageProxy(getColumnValues(), mi));
            messageToInfoTable.put(addedMessages[i], mi);
            uidToInfoTable.put(new Long(((CachingMimeMessage) addedMessages[i]).getUID()), mi);
          }

          // we need to autoCache either way.
          /*
            try {
            if (autoCache) {
            showStatusMessage(getFolderDisplayUI(), "caching " + i + " of " + addedMessages.length + " messages....");
            getCache().cacheMessage((MimeMessage)addedMessages[i], ((CachingMimeMessage)addedMessages[i]).getUID(), getUIDValidity(), SimpleFileCache.MESSAGE, false);
            } else {
            getCache().cacheMessage((MimeMessage)addedMessages[i], ((CachingMimeMessage)addedMessages[i]).getUID(), getUIDValidity(), SimpleFileCache.FLAGS_AND_HEADERS, false);
            }
            } catch (MessagingException me) {
            getLogger().log(Level.FINE, "caught exception:  " + me);
            me.printStackTrace();
            }
          */
        } else {
          // it's a 'real' message from the server.

          long uid = -1;
          try {
            uid = getUID(addedMessages[i]);
          } catch (MessagingException me) {
          }

          mi = getMessageInfoByUid(uid);
          if (mi != null) {
            addedInfos.add(mi);
            if (getLogger().isLoggable(Level.FINE))
              getLogger().log(Level.FINE, getFolderID() + ":  this is a duplicate.  not making a new messageinfo for it.");

            // but we still need to autocache it if we're autocaching.
            if (autoCache) {
              mMessageLoader.cacheMessages(new MessageProxy[] { getMessageInfoByUid(uid).getMessageProxy()});
            }
          } else {
            CachingMimeMessage newMsg = new CachingMimeMessage(CachingFolderInfo.this, uid);
            mi = new MessageInfo(newMsg, CachingFolderInfo.this);
            addedInfos.add(mi);
            addedProxies.add(new MessageProxy(getColumnValues(), mi));
            messageToInfoTable.put(newMsg, mi);
            uidToInfoTable.put(new Long(uid), mi);
          }

          /*
            try {
            if (autoCache) {
            showStatusMessage(getFolderDisplayUI(), getFolderID() + ":  " + Pooka.getProperty("info.UIDFolder.synchronizing.cachingMessages", "Caching") + " " + i + " " + Pooka.getProperty("info.UIDFolder.synchronizing.of", "of") + " " + addedMessages.length + Pooka.getProperty("info.UIDFolder.synchronizing.messages", "messages") + "....");
            getCache().cacheMessage((MimeMessage)addedMessages[i], uid, getUIDValidity(), SimpleFileCache.MESSAGE, false);
            } else {
            getCache().cacheMessage((MimeMessage)addedMessages[i], uid, getUIDValidity(), SimpleFileCache.FLAGS_AND_HEADERS, false);
            }
            } catch (MessagingException me) {
            getLogger().log(Level.FINE, "caught exception:  " + me);
            me.printStackTrace();
            }
          */
        }
      }

      try {
        List preloadMessages = addedInfos;
        if (addedInfos.size() > fetchBatchSize) {
          preloadMessages = addedInfos.subList(0, fetchBatchSize);
        }
        MessageInfo[] preloadArray = new MessageInfo[preloadMessages.size()];
        for (int i = 0; i < preloadMessages.size(); i++) {
          preloadArray[i] = (MessageInfo) preloadMessages.get(i);
        }
        fetch(preloadArray, fetchProfile);
      } catch (MessagingException me) {
        getLogger().warning("error prefetching messages:  " + me.toString());
      }
      /*
        for (int i = 0; i < preloadMessages.length; i++) {
        long uid = -1;
        try {
        uid = getUID(preloadMessages[i]);
        } catch (MessagingException me) {
        }
        try {
        // FIXME
        getCache().cacheMessage((MimeMessage)preloadMessages[i], uid, getUIDValidity(), SimpleFileCache.FLAGS_AND_HEADERS, false);
        } catch (Exception e) {

        }
        }
      */

      getCache().writeMsgFile();

      clearStatusMessage(getFolderDisplayUI());

      addedProxies.removeAll(applyFilters(addedProxies));
      if (addedProxies.size() > 0) {
        if (getFolderTableModel() != null)
          getFolderTableModel().addRows(addedProxies);
        setNewMessages(true);
        resetMessageCounts();

        // notify the message loaded thread.
        MessageProxy[] addedArray = (MessageProxy[]) addedProxies.toArray(new MessageProxy[0]);
        //loaderThread.loadMessages(addedArray, net.suberic.pooka.thread.LoadMessageThread.HIGH);
        mMessageLoader.loadMessages(addedArray, net.suberic.pooka.thread.MessageLoader.HIGH);

        if (autoCache) {
          mMessageLoader.cacheMessages(addedArray);
        }

        // change the Message objects in the MessageCountEvent to
        // our UIDMimeMessages.
        Message[] newMsgs = new Message[addedProxies.size()];
        for (int i = 0; i < addedProxies.size(); i++) {
          newMsgs[i] = ((MessageProxy)addedProxies.elementAt(i)).getMessageInfo().getMessage();
        }
        MessageCountEvent newMce = new MessageCountEvent(getFolder(), mce.getType(), mce.isRemoved(), newMsgs);
        fireMessageCountEvent(newMce);
      }

    }
  }

  /**
   * This does the real work when messages are removed.
   *
   * This method should always be run on the FolderThread.
   */
  protected void runMessagesRemoved(MessageCountEvent mce) {
    Message[] removedMessages = mce.getMessages();
    Message[] removedCachingMessages = new Message[removedMessages.length];

    if (getLogger().isLoggable(Level.FINE))
      getLogger().log(Level.FINE, "removedMessages was of size " + removedMessages.length);
    MessageInfo mi;
    Vector removedProxies=new Vector();

    for (int i = 0; i < removedMessages.length; i++) {
      if (getLogger().isLoggable(Level.FINE))
        getLogger().log(Level.FINE, "checking for existence of message.");

      if (removedMessages[i] != null && removedMessages[i] instanceof CachingMimeMessage) {
        removedCachingMessages[i] = removedMessages[i];
        long uid = ((CachingMimeMessage) removedMessages[i]).getUID();
        mi = getMessageInfoByUid(uid);

        if (mi != null) {
          if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "message exists--removing");
          if ( mi.getMessageProxy() != null) {
            mi.getMessageProxy().close();
            removedProxies.add(mi.getMessageProxy());
          }
          messageToInfoTable.remove(mi);
          uidToInfoTable.remove(new Long(((CachingMimeMessage) removedMessages[i]).getUID()));
        }

        getCache().invalidateCache(((CachingMimeMessage) removedMessages[i]).getUID(), SimpleFileCache.MESSAGE);

      } else {
        // not a CachingMimeMessage.
        long uid = -1;
        try {
          uid = getUID(removedMessages[i]);
        } catch (MessagingException me) {
        }

        mi = getMessageInfoByUid(uid);
        if (mi != null) {
          removedCachingMessages[i] = mi.getMessage();
          if (mi.getMessageProxy() != null)
            mi.getMessageProxy().close();

          if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "message exists--removing");

          Message localMsg = mi.getMessage();
          removedProxies.add(mi.getMessageProxy());
          messageToInfoTable.remove(localMsg);
          uidToInfoTable.remove(new Long(uid));
        } else {
          removedCachingMessages[i] = removedMessages[i];
        }
        getCache().invalidateCache(uid, SimpleFileCache.MESSAGE);
      }
    }

    MessageCountEvent newMce = new MessageCountEvent(getFolder(), mce.getType(), mce.isRemoved(), removedCachingMessages);

    if (getFolderDisplayUI() != null) {
      if (removedProxies.size() > 0) {
        getFolderDisplayUI().removeRows(removedProxies);
      }
      resetMessageCounts();
      fireMessageCountEvent(newMce);
    } else {
      resetMessageCounts();
      fireMessageCountEvent(newMce);
      if (removedProxies.size() > 0)
        getFolderTableModel().removeRows(removedProxies);
    }
  }

  /**
   * This updates the TableInfo on the changed messages.
   *
   * As defined by java.mail.MessageChangedListener.
   */

  public void runMessageChanged(MessageChangedEvent mce) {
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
        Message msg = mce.getMessage();
        long uid = -1;
        uid = getUID(msg);

        if (msg != null){
          if (mce.getMessageChangeType() == MessageChangedEvent.FLAGS_CHANGED)
            getCache().cacheMessage((MimeMessage)msg, uid, uidValidity, SimpleFileCache.FLAGS);
          else if (mce.getMessageChangeType() == MessageChangedEvent.ENVELOPE_CHANGED)
            getCache().cacheMessage((MimeMessage)msg, uid, uidValidity, SimpleFileCache.HEADERS);
        }

        MessageInfo mi = getMessageInfoByUid(uid);
        if (mi != null) {
          MessageProxy mp = mi.getMessageProxy();
          if (mp != null) {
            mp.unloadTableInfo();
            mp.loadTableInfo();
          }
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
   * This sets the given Flag for all the MessageInfos given.
   */
  public void setFlags(MessageInfo[] msgs, Flags flag, boolean value) throws MessagingException {
    // no optimization here.
    for (int i = 0; i < msgs.length; i++) {
      msgs[i].getRealMessage().setFlags(flag, value);
    }
  }

  /**
   * This copies the given messages to the given FolderInfo.
   */
  public void copyMessages(MessageInfo[] msgs, FolderInfo targetFolder) throws MessagingException, OperationCancelledException {
    if (isConnected())
      super.copyMessages(msgs, targetFolder);
    else
      targetFolder.appendMessages(msgs);
  }

  /**
   * This appends the given message to the given FolderInfo.
   */
  public void appendMessages(MessageInfo[] msgs) throws MessagingException, OperationCancelledException {
    if (isAvailable()) {
      if (isConnected()) {
        super.appendMessages(msgs);
      } else {
        // make sure we've loaded
        if (! isLoaded())
          loadFolder();
        getCache().appendMessages(msgs);
      }
    } else {
      throw new MessagingException("cannot append messages to an unavailable folder.");
    }
  }

  /**
   * This expunges the deleted messages from the Folder.
   */
  public void expunge() throws MessagingException, OperationCancelledException {
    if (isConnected())
      getFolder().expunge();
    else if (shouldBeConnected()) {
      openFolder(Folder.READ_WRITE);
      getFolder().expunge();
    } else {
      getCache().expungeMessages();
    }
  }

  /**
   * This attempts to cache the message represented by this MessageInfo.
   */
  public void cacheMessage (MessageInfo info, int cacheStatus) throws MessagingException {
    if (status == CONNECTED) {
      Message m = info.getMessage();
      if (m instanceof CachingMimeMessage) {
        long uid = ((CachingMimeMessage)m).getUID();
        MimeMessage realMessage = getRealMessageById(uid);
        getCache().cacheMessage(realMessage, uid, uidValidity, cacheStatus);
      } else if (m instanceof MimeMessage) {
        long uid = getUID(m);
        getCache().cacheMessage((MimeMessage)m, uid, uidValidity, cacheStatus);
      } else {
        throw new MessagingException(Pooka.getProperty("error.CachingFolderInfo.unknownMessageType", "Error:  unknownMessageType."));
      }
    } else {
      throw new MessagingException(Pooka.getProperty("error.CachingFolderInfo.cacheWhileDisconnected", "Error:  You cannot cache messages unless you\nare connected to the folder."));
    }
  }


  /**
   * Creates a child folder.
   */
  protected FolderInfo createChildFolder(String newFolderName) {
    if (! Pooka.getProperty(getFolderProperty() + "." + newFolderName + ".cacheMessages", "true").equalsIgnoreCase("false")) {
      return new CachingFolderInfo(this, newFolderName);
    } else if (Pooka.getProperty(getParentStore().getStoreProperty() + ".protocol", "mbox").equalsIgnoreCase("imap")) {
      return new UIDFolderInfo(this, newFolderName);
    } else {
      return new FolderInfo(this, newFolderName);
    }
  }

  /**
   * This method closes the Folder.  If you open the Folder using
   * openFolder (which you should), then you should use this method
   * instead of calling getFolder.close().  If you don't, then the
   * FolderInfo will try to reopen the folder.
   */
  public void closeFolder(boolean expunge) throws MessagingException {
    closeFolder(expunge, false);
  }

  /**
   * This method closes the Folder.  If you open the Folder using
   * openFolder (which you should), then you should use this method
   * instead of calling getFolder.close().  If you don't, then the
   * FolderInfo will try to reopen the folder.
   */
  public void closeFolder(boolean expunge, boolean closeDisplay) throws MessagingException {

    if (closeDisplay && getFolderDisplayUI() != null)
      getFolderDisplayUI().closeFolderDisplay();

    if (isLoaded() && isAvailable()) {
      if (isConnected()) {
        try {
          getFolder().close(expunge);
        } catch (java.lang.IllegalStateException ise) {
          throw new MessagingException(ise.getMessage(), ise);
        }
      }

      if (getCache() != null) {
        setStatus(DISCONNECTED);
      } else {
        setStatus(CLOSED);
      }
    }

  }

  /**
   * This unsubscribes this FolderInfo and all of its children, if
   * applicable.
   *
   * For the CachingFolderInfo, this calls super.unsubscribe() and
   * getCache().invalidateCache().
   */
  public void unsubscribe() {
    super.unsubscribe();
    getCache().invalidateCache();
  }

  /**
   * Searches for messages in this folder which match the given
   * SearchTerm.
   *
   * Basically wraps the call to Folder.search(), and then wraps the
   * returned Message objects as MessageInfos.
   */
  public MessageInfo[] search(javax.mail.search.SearchTerm term)
    throws MessagingException, OperationCancelledException {
    if (isConnected()) {
      return super.search(term);
    } else {
      return getCache().search(term);
    }
  }

  /**
   * The resource for the default display filters.
   */
  protected String getDefaultDisplayFiltersResource() {
    if (getCacheHeadersOnly()) {
      return super.getDefaultDisplayFiltersResource();
    } else if (isSentFolder())
      return "CachingFolderInfo.sentFolderDefaultDisplayFilters";
    else
      return "CachingFolderInfo.defaultDisplayFilters";
  }

  /**
   * Returns whether or not a given message is fully cached.
   */
  public boolean isCached(long uid) {
    return getCache().isFullyCached(uid);
  }

  /**
   * This returns the MessageCache associated with this FolderInfo,
   * if any.
   */
  public MessageCache getCache() {
    return cache;
  }

  /**
   * Returns whether or not we should be showing cache information in
   * the FolderDisplay.  Uses the FolderProperty.showCacheInfo property
   * to determine--if this is set to true, we will show the cache info.
   * Otherwise, if we're connected, don't show the info, and if we're
   * not connected, do.
   */
  public boolean showCacheInfo() {
    if (Pooka.getProperty(getFolderProperty() + ".showCacheInfo", "false").equalsIgnoreCase("true"))
      return true;
    else {
      if (getStatus() == CONNECTED) {
        return false;
      } else
        return true;
    }
  }

  /**
   * Returns the cache directory for this FolderInfo.
   */
  public String getCacheDirectory() {
    String localDir = Pooka.getResourceManager().translateName(Pooka.getProperty(getFolderProperty() + ".cacheDir", ""));
    if (!localDir.equals(""))
      return localDir;

    localDir = Pooka.getProperty("Pooka.defaultMailSubDir", "");
    if (localDir.equals(""))
      localDir = Pooka.getPookaManager().getPookaRoot().getAbsolutePath() + File.separator + ".pooka";

    localDir = Pooka.getResourceManager().translateName(localDir);

    localDir = localDir + File.separatorChar + "cache";
    FolderInfo currentFolder = this;
    StringBuffer subDir = new StringBuffer();
    subDir.insert(0, currentFolder.getFolderName());
    subDir.insert(0, File.separatorChar);
    while (currentFolder.getParentFolder() != null) {
      currentFolder = currentFolder.getParentFolder();
      subDir.insert(0, currentFolder.getFolderName());
      subDir.insert(0, File.separatorChar);
    }

    subDir.insert(0, currentFolder.getParentStore().getStoreID());
    subDir.insert(0, File.separatorChar);

    return localDir + subDir.toString();
  }

  public boolean isLoaded() {
    return (getFolder() != null && ( ! (getFolder() instanceof FolderProxy)) && cache != null);
  }

  /**
   * Gets the UID for the given Message.
   */
  public long getUID(Message m) throws MessagingException {
    if (m instanceof SimpleFileCache.LocalMimeMessage) {
      return ((SimpleFileCache.LocalMimeMessage) m).getUID();
    } else {
      return super.getUID(m);
    }
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
   * This handles the changes if the source property is modified.
   *
   * As defined in net.suberic.util.ValueChangeListener.
   */

  public void valueChanged(String changedValue) {
    if (changedValue.equals(getFolderProperty() + ".autoCache") || changedValue.equals(getParentStore().getStoreProperty() + ".autoCache")) {
      if (! getCacheHeadersOnly()) {
        autoCache =  Pooka.getProperty(getFolderProperty() + ".autoCache", Pooka.getProperty(getFolderProperty() + ".autoCache", Pooka.getProperty(getParentStore().getStoreProperty() + ".autoCache", Pooka.getProperty("Pooka.autoCache", "false")))).equalsIgnoreCase("true");
      }
    } else if (changedValue.equals(getFolderProperty() + ".cacheHeadersOnly") || changedValue.equals(getParentStore().getStoreProperty() + ".cacheHeadersOnly")) {
      if (! getCacheHeadersOnly()) {
        autoCache =  Pooka.getProperty(getFolderProperty() + ".autoCache", Pooka.getProperty(getParentStore().getStoreProperty() + ".autoCache", Pooka.getProperty("Pooka.autoCache", "false"))).equalsIgnoreCase("true");
      }
      createFilters();
      if (getFolderNode() != null)
        getFolderNode().popupMenu = null;
    } else {
      super.valueChanged(changedValue);
    }
  }

  /**
   * Returns if we're caching only headers.
   */
  public boolean getCacheHeadersOnly() {
    return Pooka.getProperty(getFolderProperty() + ".cacheHeadersOnly", Pooka.getProperty(getParentStore().getStoreProperty() + ".cacheHeadersOnly", "false")).equalsIgnoreCase("true");
  }
}

