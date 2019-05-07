package net.suberic.pooka;

import javax.mail.*;
import javax.mail.event.*;
import javax.swing.event.EventListenerList;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import net.suberic.pooka.gui.*;
import net.suberic.util.ValueChangeListener;
import net.suberic.util.thread.ActionThread;
import net.suberic.util.VariableBundle;
import net.suberic.util.Item;

/**
 * This class does all of the work for a Store.  It keeps track of the
 * StoreNode for the Store, as well as keeping the children of the store
 * and the properties of the Store.
 */

public class StoreInfo implements ValueChangeListener, Item, NetworkConnectionListener {

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

  /**
   * Creates a new StoreInfo from a Store ID.
   */
  public StoreInfo(String sid) {
    setStoreID(sid);

    configureStore();
  }

  /**
   * This configures the store from the property information.
   */
  public void configureStore() {
    connected = false;
    available = false;

    protocol = Pooka.getProperty("Store." + storeID + ".protocol", "");

    if (protocol.equalsIgnoreCase("pop3")) {
      user = "";
      password = "";
      server = "localhost";
      if (Pooka.getProperty(getStoreProperty() + ".useMaildir", "unset").equalsIgnoreCase("true"))
        protocol = "maildir";
      else
        protocol = "mbox";
      port = -1;
      popStore = true;
    } else {
      popStore = false;
      user = Pooka.getProperty("Store." + storeID + ".user", "");
      password = Pooka.getProperty("Store." + storeID + ".password", "");
      String portValue = Pooka.getProperty("Store." + storeID + ".port", "");
      port = -1;
      if (!portValue.equals("")) {
        try {
          port = Integer.parseInt(portValue);
        } catch (Exception e) {
        }
      }
      if (!password.equals(""))
        password = net.suberic.util.gui.propedit.PasswordEditorPane.descrambleString(password);
      server = Pooka.getProperty("Store." + storeID + ".server", "");

      sslSetting = Pooka.getProperty(getStoreProperty() + ".SSL", "none");
      if (sslSetting.equalsIgnoreCase("true")) {
        Pooka.setProperty(getStoreProperty() + ".SSL", "ssl");
        sslSetting = "ssl";
      } else if (sslSetting.equalsIgnoreCase("false")) {
        Pooka.setProperty(getStoreProperty() + ".SSL", "none");
        sslSetting = "none";
      }

      if (sslSetting.equals("ssl")) {
        if (protocol.equals("imap"))
          protocol = "imaps";
      }
    }


    Properties p = loadProperties();

    if (protocol.equalsIgnoreCase("maildir")) {
      url = new URLName(protocol, server, port, p.getProperty("mail.store.maildir.baseDir"), user, password);
    } else {
      url = new URLName(protocol, server, port, "", user, password);
    }

    getLogger().fine("creating authenticator");
    mAuthenticator = Pooka.getUIFactory().createAuthenticatorUI();

    try {
      mSession = Session.getInstance(p, mAuthenticator);

      updateSessionDebug();

      store = mSession.getStore(url);
      available=true;
    } catch (NoSuchProviderException nspe) {
      Pooka.getUIFactory().showError(Pooka.getProperty("error.loadingStore", "Unable to load Store ") + getStoreID(), nspe);
      available=false;
    }

    // don't allow a StoreInfo to get created with an empty folderList.

    if (Pooka.getProperty("Store." + storeID + ".folderList", "").equals(""))
      Pooka.setProperty("Store." + storeID + ".folderList", "INBOX");

    // check to see if we're using the subscribed property.
    useSubscribed = Pooka.getProperty(getStoreProperty() + ".useSubscribed", "false").equalsIgnoreCase("true");

    Pooka.getResources().addValueChangeListener(this, getStoreProperty());
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".folderList");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".defaultProfile");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".protocol");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".user");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".password");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".server");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".port");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".connection");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".SSL");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".useSubscribed");
    Pooka.getResources().addValueChangeListener(this, getStoreProperty() + ".sessionDebug.level");

    // tell the log manager to listen to these settings.
    Pooka.getLogManager().addLogger(getStoreProperty());
    Pooka.getLogManager().addLogger(getStoreProperty() + ".sessionDebug");

    if (available) {
      connectionListener = new ConnectionListener() {

          public void disconnected(ConnectionEvent e) {
            getLogger().log(Level.FINE, "Store " + getStoreID() + " disconnected.");
            // if we think we're connected, then call disconnectStore.
            if (isConnected()) {
              try {
                if (Pooka.getUIFactory().getMessageNotificationManager() != null) {
                  Pooka.getUIFactory().getMessageNotificationManager().displayMessage("Disconnected", "Disconnected from store " + getStoreID(), net.suberic.pooka.gui.MessageNotificationManager.WARNING_MESSAGE_TYPE);
                }
                disconnectStore();
              } catch (MessagingException me) {
                getLogger().log(Level.FINE, "error disconnecting Store:  " + me.getMessage());
              }
            }

          }

          public void closed(ConnectionEvent e) {
            getLogger().log(Level.FINE, "Store " + getStoreID() + " closed.");

            // if we think we're connected, then call disconnectStore.
            if (isConnected()) {
              if (Pooka.getUIFactory().getMessageNotificationManager() != null) {
                Pooka.getUIFactory().getMessageNotificationManager().displayMessage("Disconnected", "Disconnected from store " + getStoreID(), net.suberic.pooka.gui.MessageNotificationManager.WARNING_MESSAGE_TYPE);
              }
              try {
                disconnectStore();
              } catch (MessagingException me) {
                getLogger().log(Level.FINE, "error disconnecting Store:  " + me.getMessage());
              }
            }

          }

          public void opened(ConnectionEvent e) {
            getLogger().log(Level.FINE, "Store " + getStoreID() + " opened.");
          }
        };

      store.addConnectionListener(connectionListener);

    }

    if (storeThread == null) {
      storeThread = new ActionThread(this.getStoreID() + " - ActionThread");
      storeThread.start();
    }

    String defProfileString = Pooka.getProperty(getStoreProperty() + ".defaultProfile", "");
    if (defProfileString.length() < 1 || defProfileString.equalsIgnoreCase(UserProfile.S_DEFAULT_PROFILE_KEY)) {
      defaultProfile = null;
    } else {
      defaultProfile = Pooka.getPookaManager().getUserProfileManager().getProfile(defProfileString);
    }

    connection = Pooka.getConnectionManager().getConnection(Pooka.getProperty(getStoreProperty() + ".connection", ""));
    if (connection == null) {
      connection = Pooka.getConnectionManager().getDefaultConnection();
    }

    if (connection != null) {
      connection.addConnectionListener(this);
    }

    updateChildren();

    String trashFolderName = Pooka.getProperty(getStoreProperty() + ".trashFolder", "");
    if (trashFolderName.length() > 0) {
      trashFolder = getChild(trashFolderName);
      if (trashFolder != null)
        trashFolder.setTrashFolder(true);
    }
  }

  /**
   * This loads in the default session properties for this Store's
   * Session.
   */
  public Properties loadProperties() {
    Properties p = new Properties(System.getProperties());

    String realProtocol = Pooka.getProperty("Store." + storeID + ".protocol", "");

    if (realProtocol.equalsIgnoreCase("imap")) {
      loadImapProperties(p);
    } else if (realProtocol.equalsIgnoreCase("pop3")) {
      loadPop3Properties(p);
      String useMaildir = Pooka.getProperty(getStoreProperty() + ".useMaildir", "unset");

      if (useMaildir.equals("unset")) {
        Pooka.setProperty(getStoreProperty() + ".useMaildir", "false");
        useMaildir="false";
      }

      if ( useMaildir.equalsIgnoreCase("false")) {
        loadMboxProperties(p);
      } else {
        loadMaildirProperties(p);
      }
    } else if (realProtocol.equalsIgnoreCase("maildir")) {
      loadMaildirProperties(p);
    } else if (realProtocol.equalsIgnoreCase("mbox")) {
      loadMboxProperties(p);
    }
    return p;
  }

  /**
   * Load all IMAP properties.
   */
  void loadImapProperties(Properties p) {
    p.setProperty("mail.imap.timeout", Pooka.getProperty(getStoreProperty() + ".timeout", Pooka.getProperty("Pooka.timeout", "-1")));
    p.setProperty("mail.imap.connectiontimeout", Pooka.getProperty(getStoreProperty() + ".connectionTimeout", Pooka.getProperty("Pooka.connectionTimeout", "-1")));

    p.setProperty("mail.imaps.timeout", Pooka.getProperty(getStoreProperty() + ".timeout", Pooka.getProperty("Pooka.timeout", "-1")));
    p.setProperty("mail.imaps.connectiontimeout", Pooka.getProperty(getStoreProperty() + ".connectionTimeout", Pooka.getProperty("Pooka.connectionTimeout", "-1")));

    // set up ssl
    if (sslSetting.equals("ssl")) {
      p.setProperty("mail.imaps.socketFactory.fallback", Pooka.getProperty(getStoreProperty() + ".SSL.fallback", "false"));
    } else if (sslSetting.equals("tlsrequired")) {
      p.setProperty("mail.imap.starttls.enable", "true");
    } else if (sslSetting.equals("tls")) {
      // failover is implemented in the connectStore() method.
      p.setProperty("mail.imap.starttls.enable", "true");
    }

    // use a dedicated store connection.
    p.setProperty("mail.imap.separatestoreconnection", "true");
    p.setProperty("mail.imaps.separatestoreconnection", "true");
  }

  /**
   * Load all POP3 properties.
   */
  void loadPop3Properties(Properties p) {
    if (Pooka.getProperty(getStoreProperty() + ".SSL", "false").equalsIgnoreCase("true")) {
      //p.setProperty("mail.pop3s.socketFactory.class", "net.suberic.pooka.ssl.PookaSSLSocketFactory");
      p.setProperty("mail.pop3s.socketFactory.fallback", Pooka.getProperty(getStoreProperty() + ".SSL.fallback", "false"));
      //p.setProperty("mail.pop3.socketFactory.port", Pooka.getProperty(getStoreProperty() + ".SSL.port", "995"));
    }
  }

  /**
   * Load all Maildir properties.
   */
  void loadMaildirProperties(Properties p) {

    String mailHome = Pooka.getProperty(getStoreProperty() + ".mailDir", "");
    if (mailHome.equals("")) {
      mailHome = Pooka.getProperty("Pooka.defaultMailSubDir", "");
      if (mailHome.equals(""))
        mailHome = Pooka.getPookaManager().getPookaRoot().getAbsolutePath() + java.io.File.separator + ".pooka";

      mailHome = mailHome + java.io.File.separator + storeID;
    }

    String userHomeName = Pooka.getPookaManager().getResourceManager().translateName(mailHome + java.io.File.separator + Pooka.getProperty("Pooka.subFolderName", "folders"));

    //p.setProperty("mail.store.maildir.imapEmulation", "true");
    p.setProperty("mail.store.maildir.baseDir", userHomeName);
    p.setProperty("mail.store.maildir.autocreatedir", "true");
  }

  /**
   * Load all Mbox properties.
   */
  void loadMboxProperties(Properties p) {
    /*
     * set the properties for mbox folders, and for the mbox backend of
     * a pop3 mailbox.  properties set are:
     *
     * mail.mbox.inbox:  the location of the INBOX for this mail store. for
     *   pop3 stores, this is the location of the local copy of the inbox.
     *   for mbox stores, this should be the local inbox file.
     * mail.mbox.userhome:  the location of all subfolders.
     */
    String mailHome = Pooka.getProperty(getStoreProperty() + ".mailDir", "");
    if (mailHome.equals("")) {
      mailHome = Pooka.getProperty("Pooka.defaultMailSubDir", "");
      if (mailHome.equals(""))
        mailHome = Pooka.getPookaManager().getPookaRoot().getAbsolutePath() + java.io.File.separator + ".pooka";

      mailHome = mailHome + java.io.File.separator + storeID;
    }

    mailHome = Pooka.getPookaManager().getResourceManager().translateName(mailHome);

    String inboxFileName;
    if (Pooka.getProperty(getStoreProperty() + ".protocol", "imap").equalsIgnoreCase("pop3")) {
      inboxFileName = mailHome + java.io.File.separator + Pooka.getProperty("Pooka.inboxName", "INBOX");
    } else {
      inboxFileName = Pooka.getProperty(getStoreProperty() + ".inboxLocation", "/var/spool/mail/" + System.getProperty("user.name"));
    }

    String userHomeName = mailHome + java.io.File.separator + Pooka.getProperty("Pooka.subFolderName", "folders");

    getLogger().log(Level.FINE, "for store " + getStoreID() + ", inboxFileName = " + inboxFileName + "; userhome = " + userHomeName);

    p.setProperty("mail.mbox.inbox", inboxFileName);
    p.setProperty("mail.mbox.userhome", userHomeName);
  }

  /**
   * This updates the children of the current store.  Generally called
   * when the folderList property is changed.
   */

  public void updateChildren() {

    Vector<FolderInfo> newChildren = new Vector<FolderInfo>();

    List<String> newChildNames = Pooka.getResources().getPropertyAsList(getStoreProperty() + ".folderList", "INBOX");

    for (String newFolderName: newChildNames) {
      FolderInfo childFolder = getChild(newFolderName);
      if (childFolder == null) {
        childFolder = Pooka.getResourceManager().createFolderInfo(this, newFolderName);
      }

      newChildren.add(0, childFolder);
    }

    children = newChildren;
    getLogger().log(Level.FINE, getStoreID() + ":  in configureStore.  children.size() = " + children.size());

    if (storeNode != null)
      storeNode.loadChildren();
  }

  /**
   * This goes through the list of children of this store and
   * returns the FolderInfo for the given childName, if one exists.
   * If none exists, or if the children Vector has not been loaded
   * yet, or if this is a leaf node, then this method returns null.
   */
  public FolderInfo getChild(String childName) {
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

      for (int i = 0; i < children.size(); i++)
        if (((FolderInfo)children.elementAt(i)).getFolderName().equals(folderName))
          childFolder = (FolderInfo)children.elementAt(i);
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
   * This handles the event that the StoreInfo is removed from Pooka.
   */
  public void remove() {
    // FIXME need to do a lot here.
    try {
      disconnectStore();
    } catch (Exception e) {
    }
    cleanup();
    // FIXME
    // Pooka.getResources().removePropertyTree(getStoreProperty())

  }

  /**
   * Cleans up all references to this StoreInfo.
   */
  public void cleanup() {
    Pooka.getResources().removeValueChangeListener(this);
    Pooka.getLogManager().removeLogger(getStoreProperty());

    if (children != null && children.size() > 0) {
      for (int i = 0; i < children.size(); i++)
        ((FolderInfo)children.elementAt(i)).cleanup();
    }

    if (store != null) {
      store.removeConnectionListener(connectionListener);
    }
    if (getStoreThread() != null) {
      getStoreThread().setStop(true);
    }
  }

  /**
   * This handles the changes if the source property is modified.
   *
   * As defined in net.suberic.util.ValueChangeListener.
   */

  public void valueChanged(String pChangedValue) {
    final String changedValue = pChangedValue;
    javax.swing.AbstractAction valueChangedAction = new javax.swing.AbstractAction() {
        public void actionPerformed(java.awt.event.ActionEvent ae) {
          // check to make sure that we still exist.
          List<String> storeList = Pooka.getResources().getPropertyAsList("Store", "");
          if (storeList.contains(getStoreID())) {
            if (changedValue.equals(getStoreProperty() + ".folderList")) {
              updateChildren();
            } else if (changedValue.equals(getStoreProperty() + ".defaultProfile")) {
              String defProfileString = Pooka.getProperty(getStoreProperty() + ".defaultProfile", "");
              if (defProfileString.length() < 1 || defProfileString.equalsIgnoreCase(UserProfile.S_DEFAULT_PROFILE_KEY)) {
                defaultProfile = null;
              } else {
                defaultProfile = Pooka.getPookaManager().getUserProfileManager().getProfile(defProfileString);
              }
            } else if (changedValue.equals(getStoreProperty() + ".protocol") || changedValue.equals(getStoreProperty() + ".user") || changedValue.equals(getStoreProperty() + ".password") || changedValue.equals(getStoreProperty() + ".server") || changedValue.equals(getStoreProperty() + ".port") || changedValue.equals(getStoreProperty() + ".SSL") ) {

              if (storeNode != null) {
                Enumeration childEnum = storeNode.children();
                Vector v = new Vector();
                while (childEnum.hasMoreElements())
                  v.add(childEnum.nextElement());

                storeNode.removeChildren(v);
              }

              children = null;

              try {
                disconnectStore();
              } catch (Exception e) { }

              getLogger().log(Level.FINE, "calling configureStore()");

              configureStore();
            } else if (changedValue.equals(getStoreProperty() + ".connection")) {
              connection.removeConnectionListener(StoreInfo.this);

              connection = Pooka.getConnectionManager().getConnection(Pooka.getProperty(getStoreProperty() + ".connection", ""));
              if (connection == null) {
                connection = Pooka.getConnectionManager().getDefaultConnection();
              }

              if (connection != null) {
                connection.addConnectionListener(StoreInfo.this);
              }
            } else if (changedValue.equals(getStoreProperty() + ".useSubscribed")) {
              useSubscribed = Pooka.getProperty(getStoreProperty() + ".useSubscribed", "false").equalsIgnoreCase("true");
            } else if (changedValue.equals(getStoreProperty() + ".sessionDebug.level")) {
              updateSessionDebug();
            }
          }
        }
      };

    // if we don't do the update synchronously on the store thread,
    // then subscribing to subfolders breaks.
    java.awt.event.ActionEvent actionEvent =  new java.awt.event.ActionEvent(this, 0, "value-changed");
    if (Thread.currentThread() == getStoreThread()) {
      valueChangedAction.actionPerformed(actionEvent);
    } else {
      getStoreThread().addToQueue(valueChangedAction, actionEvent);
    }

  }


  /**
   * Called when the status of the NetworkConnection changes.
   */
  public void connectionStatusChanged(NetworkConnection connection, int newStatus) {
    // mbox folders still don't care.
    if (! (protocol.equalsIgnoreCase("mbox") || protocol.equalsIgnoreCase("maildir"))) {
      if (newStatus == NetworkConnection.CONNECTED) {
        // we've connected.
        // we probably don't care.

      } else if (newStatus == NetworkConnection.DISCONNECTED) {
        // we're being disconnected.  close all the connections.
        try {
          disconnectStore();
        } catch (MessagingException me) {
          getLogger().log(Level.FINE, "Caught exception disconnecting Store " + getStoreID() + ":  " + me);
          if (getLogger().isLoggable(Level.FINE))
            me.printStackTrace();
          // else ignore
        }

      } else {
        // we've been cut off.  note it.
        try {
          disconnectStore();
        } catch (MessagingException me) {
          getLogger().log(Level.FINE, "Caught exception disconnecting Store " + getStoreID() + ":  " + me);
          if (getLogger().isLoggable(Level.FINE))
            me.printStackTrace();
          // else ignore
        }
      }
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
    Vector folderNames = Pooka.getResources().getPropertyAsVector(getStoreProperty() + ".folderList", "");

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

    Pooka.setProperty(getStoreProperty() + ".folderList", newValue.toString());
  }

  /**
   * This adds the given folderString to the folderList property.
   */
  void addToFolderList(String addFolderName) {
    String folderName;
    Vector folderNames = Pooka.getResources().getPropertyAsVector(getStoreProperty() + ".folderList", "");

    boolean found = false;

    for (int i = 0; i < folderNames.size(); i++) {
      folderName = (String) folderNames.elementAt(i);

      if (folderName.equals(addFolderName)) {
        found=true;
      }

    }

    if (!found) {
      String currentValue = Pooka.getProperty(getStoreProperty() + ".folderList");
      if (currentValue.equals(""))
        Pooka.setProperty(getStoreProperty() + ".folderList", addFolderName);
      else
        Pooka.setProperty(getStoreProperty() + ".folderList", currentValue + ":" + addFolderName);
    }

  }

  /**
   * This creates a folder if it doesn't exist already.  If it does exist,
   * but is not of the right type, or if there is a problem in creating the
   * folder, throws an error.
   */
  public void createSubFolder(String subFolderName, int type) throws MessagingException {
    Folder folder = store.getDefaultFolder();

    if (folder != null) {
      Folder subFolder = folder.getFolder(subFolderName);

      if (subFolder == null) {
        throw new MessagingException("Store returned null for subfolder " + subFolderName);
      }

      if (! subFolder.exists())
        subFolder.create(type);

      subscribeFolder(subFolderName);
    } else {
      throw new MessagingException("Failed to open store " + getStoreID() + " to create subfolder " + subFolderName);

    }
  }

  /**
   * This subscribes the Folder described by the given String to this
   * StoreInfo.
   */
  public void subscribeFolder(String folderName) {
    getLogger().log(Level.FINE, "subscribing folder " + folderName);

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

    getLogger().log(Level.FINE, "store " + getStoreID() + " subscribing folder " + childFolderName + "; sending " + subFolderName + " to child for subscription.");

    this.addToFolderList(childFolderName);

    FolderInfo childFolder = getChild(childFolderName);

    getLogger().log(Level.FINE, "got child folder '" + childFolder + "' for " + childFolderName);

    if (childFolder != null && subFolderName != null) {
      childFolder.subscribeFolder(subFolderName);
    }
  }

  /**
   * This method connects the Store, and sets the StoreInfo to know that
   * the Store should be connected.  You should use this method instead of
   * calling getStore().connect(), because if you use this method, then
   * the StoreInfo will try to keep the Store connected, and will try to
   * reconnect the Store if it gets disconnected before
   * disconnectStore is called.
   *
   * This method also calls updateChildren() to load the children of
   * the Store, if the children vector has not been loaded yet.
   */
  public void connectStore() throws MessagingException, OperationCancelledException {
    getLogger().log(Level.FINE, "trying to connect store " + getStoreID());

    if (store.isConnected()) {
      getLogger().log(Level.FINE, "store " + getStoreID() + " is already connected.");
      connected=true;
      return;
    } else {
      // test the connection and execute the precommand, if any.
      testConnection();
      executePrecommand();

      getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  doing store.connect()");
      boolean connectSucceeded = false;
      while (! connectSucceeded) {
        try {
          getLogger().fine("running store.connect()");
          store.connect();
          connectSucceeded = true;
          mPreferredStatus = FolderInfo.CONNECTED;
          getLogger().fine("done with store.connect().");
          // if authentication is necessary, then the authenticator will
          // show, so will need to be closed.
          mAuthenticator.disposeAuthenticator();
        } catch (MessagingException me) {
          getLogger().fine("caught exception.");
          // cases here:
          // 1)  authenticator cancelled.
          if (mAuthenticator.isCancelled()) {
            getLogger().fine("operation was cancelled.");
            mAuthenticator.disposeAuthenticator();
            mPreferredStatus = FolderInfo.DISCONNECTED;
            throw new OperationCancelledException();
          }

          // 2) Interrupted IO exception -- try again.
          Exception nextEx = me.getNextException();

          if (nextEx != null && nextEx instanceof java.io.InterruptedIOException) {
            getLogger().fine("retrying--interruptedioexception");
          }  else {

            // 3) TLS exception -- fall back.
            if (nextEx != null && nextEx.toString().contains("SunCertPathBuilderException") && "tls".equalsIgnoreCase(sslSetting)) {
              getLogger().fine("falling back to no tls.");
              // fall back.
              Properties p = mSession.getProperties();
              p.setProperty("mail.imap.starttls.enable", "false");

              store = mSession.getStore(url);
            } else {
              // 4)  auth failed.  show error and retry.
              if (mAuthenticator.isShowing()) {
                mAuthenticator.setErrorMessage(me.getMessage(), me);
              } else {
                // 5) must have been some other error.  throw it.
                if (nextEx != null) {
                  if (nextEx instanceof java.net.UnknownHostException) {
                    throw new MessagingException(Pooka.getResources().formatMessage("error.login.unknownHostException", nextEx.getMessage()), me);
                    //} else if (nextEx instanceof java.io.UnknownHostException) {

                  }
                }
                throw me;
              }
            }
          }
        }
      }

      getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  connection succeeded; connected = true.");
      connected=true;

      if (useSubscribed && protocol.equalsIgnoreCase("imap")) {
        synchSubscribed();
      }

      if (Pooka.getProperty("Pooka.openFoldersOnConnect", "true").equalsIgnoreCase("true")) {
        for (int i = 0; i < children.size(); i++) {
          doOpenFolders((FolderInfo) children.elementAt(i));
        }
      }
    }

  }

  /**
   * Tests the NetworkConnection status for this store.
   */
  private void testConnection() throws MessagingException {
    // don't test for connections for mbox providers.
    if (! (protocol.equalsIgnoreCase("mbox") || protocol.equalsIgnoreCase("maildir"))) {
      NetworkConnection currentConnection = getConnection();
      getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  checking connection.");

      if (currentConnection != null) {
        if (currentConnection.getStatus() == NetworkConnection.DISCONNECTED) {
          getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  connection not up.  trying to connect it..");

          currentConnection.connect(true, true);
        }

        if (connection.getStatus() != NetworkConnection.CONNECTED) {
          throw new MessagingException(Pooka.getProperty("error.connectionDown", "Connection down for Store:  ") + getItemID());
        } else {
          getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  successfully opened connection.");

        }
      }
    }
  }

  /**
   * Execute the precommand if there is one.
   */
  private void executePrecommand() {
    String preCommand = Pooka.getProperty(getStoreProperty() + ".precommand", "");
    if (preCommand.length() > 0) {
      getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  executing precommand.");

      try {
        Process p = Runtime.getRuntime().exec(preCommand);
        p.waitFor();
      } catch (Exception ex) {
        getLogger().log(Level.FINE, "Could not run precommand:");
          ex.printStackTrace();
      }
    }
  }

  /**
   *
   */
  private void doOpenFolders(FolderInfo fi) {
    if (Pooka.getProperty("Pooka.openFoldersInBackground", "false").equalsIgnoreCase("true")) {
      final FolderInfo current = fi;
      javax.swing.AbstractAction openFoldersAction = new javax.swing.AbstractAction() {
          public void actionPerformed(java.awt.event.ActionEvent e) {
            current.openAllFolders(Folder.READ_WRITE);
          }
        };

      openFoldersAction.putValue(javax.swing.Action.NAME, "file-open");
      openFoldersAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "file-open on folder " + fi.getFolderID());
      getStoreThread().addToQueue(openFoldersAction, new java.awt.event.ActionEvent(this, 0, "open-all"), ActionThread.PRIORITY_LOW);
    } else {
      fi.openAllFolders(Folder.READ_WRITE);
    }
  }

  /**
   * Opens the given folders in the UI.
   */
  public void openFolders(List<FolderInfo> folderList) {
    try {
      connectStore();
      for (FolderInfo fInfo: folderList) {
        final FolderNode fNode = fInfo.getFolderNode();

        fNode.openFolder(false, false);
      }
    } catch (MessagingException me) {
      // on failure still open caching folders.
      boolean showError = false;
      for (FolderInfo fInfo: folderList) {
        if (fInfo instanceof net.suberic.pooka.cache.CachingFolderInfo) {
          if (! (((net.suberic.pooka.cache.CachingFolderInfo) fInfo).getCacheHeadersOnly())) {
            final FolderNode fNode = fInfo.getFolderNode();

            fNode.openFolder(false, false);
          }
        } else {
          showError = true;
        }
      }
      if (showError) {
        Pooka.getUIFactory().showError(Pooka.getResources().formatMessage("error.Store.connecton.failed", getStoreID()), me);
      }
    } catch (OperationCancelledException oce) {
      // on failure still open caching folders.
      for (FolderInfo fInfo: folderList) {
        if (fInfo instanceof net.suberic.pooka.cache.CachingFolderInfo) {
          if (! (((net.suberic.pooka.cache.CachingFolderInfo) fInfo).getCacheHeadersOnly())) {
            final FolderNode fNode = fInfo.getFolderNode();

            fNode.openFolder(false, false);
          }
        }
      }
    }
  }

  /**
   * This method disconnects the Store.  If you connect to the Store using
   * connectStore() (which you should), then you should use this method
   * instead of calling getStore.disconnect().  If you don't, then the
   * StoreInfo will try to reconnect the store.
   */
  public void disconnectStore() throws MessagingException {
    getLogger().log(Level.FINE, "disconnecting store " + getStoreID());

    MessagingException storeException = null;
    if (!(store.isConnected())) {
      connected=false;
      closeAllFolders(false, false);
      return;
    } else {
      try {
        try {
          closeAllFolders(false, false);
        } catch (MessagingException folderMe) {
          storeException = folderMe;
        }
        store.close();
      } catch (MessagingException me) {
        if (storeException != null) {
          me.setNextException(storeException);
        }
        storeException = me;
        throw storeException;
      } finally {
        connected=false;
      }

      if (storeException != null)
        throw storeException;
    }
  }

  /**
   * Closes all of the Store's children.
   */
  public void closeAllFolders(boolean expunge, boolean shuttingDown) throws MessagingException {
    if (getStoreThread() != null && ! getStoreThread().getStopped()) {
      // if the store thread has exited, assume we're exiting, too.
      synchronized(getStoreThread().getRunLock()) {
        getLogger().log(Level.FINE, "closing all folders of store " + getStoreID());
        Vector folders = getChildren();
        if (folders != null) {
          for (int i = 0; i < folders.size(); i++) {
            ((FolderInfo) folders.elementAt(i)).closeAllFolders(expunge, shuttingDown);
          }
        }
      }
    }
  }

  /**
   * Stops the store thread.
   */
  public void stopStoreThread() {
    if (storeThread != null) {
      storeThread.setStop(true);
      //storeThread = null;
    }
  }

  /**
   * Gets all of the children folders of this StoreInfo which are both
   * Open and can contain Messages.
   */
  public Vector getAllFolders() {
    Vector returnValue = new Vector();
    Vector subFolders = getChildren();
    for (int i = 0; i < subFolders.size(); i++) {
      returnValue.addAll(((FolderInfo) subFolders.elementAt(i)).getAllFolders());
    }
    return returnValue;
  }

  /**
   * Synchronizes the locally stored subscribed folders list to the subscribed
   * folder information from the IMAP server.
   */
  public void synchSubscribed() throws MessagingException, OperationCancelledException {
    // require the inbox.  this is to work around a bug in which the inbox
    // doesn't show up in certain conditions.

    boolean foundInbox=false;

    Folder[] subscribedFolders = store.getDefaultFolder().list();

    ArrayList subscribedNames = new ArrayList();

    for (int i = 0; subscribedFolders != null && i < subscribedFolders.length; i++) {
      // sometimes listSubscribed() doesn't work.
      // and sometimes list() returns duplicate entries for some reason.
      String folderName = subscribedFolders[i].getName();
      if (folderName.equalsIgnoreCase("inbox")) {
        if (!foundInbox) {
          foundInbox=true;
          subscribedNames.add(folderName);
        }
      } else if (subscribedFolders[i].isSubscribed()) {
        if (! subscribedNames.contains(folderName))
          subscribedNames.add(folderName);
      }
    }

    // add subscribed namespaces.
    List tmpChildren = getChildren();
    if (tmpChildren != null) {
      // go through each.  check to see if it's a namespace.  if so,
      // add it, too.

      Iterator it = tmpChildren.iterator();
      while (it.hasNext()) {
        FolderInfo fi = (FolderInfo) it.next();
        String folderName = fi.getFolderName();
        if (fi.isNamespace() && ! subscribedNames.contains(folderName))
          subscribedNames.add(folderName);
      }
    }
    Collections.sort(subscribedNames);

    // keep the existing order when possible.
    List<String> currentSubscribed = Pooka.getResources().getPropertyAsList(getStoreProperty() + ".folderList", "");
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
    Pooka.setProperty(getStoreProperty() + ".folderList", VariableBundle.convertToString(currentSubscribed));

    for (int i = 0; children != null && i < children.size(); i++) {
      FolderInfo fi = (FolderInfo) children.get(i);
      fi.synchSubscribed();
    }
  }

  // last connection check.
  long lastConnectionCheck = 0;

  /**
   * Checks connection for this store.
   */
  public boolean checkConnection() {
    // if we don't think we're connected, don't check.
    if (! isConnected())
      return false;

    // don't check if we've checked very recently.
    if (System.currentTimeMillis() - lastConnectionCheck > 20000) {
      getLogger().log(Level.FINER, "Checking connection for store " + getStoreID());
      Store realStore = getStore();

      if (realStore != null) {
        if (! realStore.isConnected()) {
          getLogger().log(Level.FINER, getStoreID() + ":  isConnected() returns false.  returning false.");
          return false;
        } else {
          return true;
        }
      } else {
        return false;
      }
    } else {
      return isConnected();
    }
  }

  /**
   * Shows the current status for this store and its thread.
   */
  public void showStatus() {
    StringBuffer statusBuffer = new StringBuffer();
    getLogger().log(Level.INFO, "Status for store " + getStoreID());
    statusBuffer.append("Status for store " + getStoreID() + "\r\n");
    boolean infoIsConnected = isConnected();
    getLogger().log(Level.INFO, "Connected:  " + infoIsConnected);
    statusBuffer.append("Connected:  " + infoIsConnected + "\r\n");

    if (storeThread != null) {
      String currentAction = storeThread.getCurrentActionName();
      getLogger().log(Level.INFO, "Current Action:  " + currentAction);
      statusBuffer.append("Current Action:  " + currentAction + "\r\n");
      int queueSize = storeThread.getQueueSize();
      getLogger().log(Level.INFO, "Action Queue Size:  " + queueSize);
      statusBuffer.append("Action Queue Size:  " + queueSize +"\r\n");
      if (storeThread.getQueueSize() > 0) {
        System.out.println("Queue:");
        java.util.List queue = storeThread.getQueue();
        for (int i = 0; i < queue.size(); i++) {
          net.suberic.util.thread.ActionThread.ActionEventPair current = (net.suberic.util.thread.ActionThread.ActionEventPair) queue.get(i);
          String queueString = "  queue[" + i + "]:  ";
          String entryDescription = (String) current.action.getValue(javax.swing.Action.SHORT_DESCRIPTION);
          if (entryDescription == null)
            entryDescription = (String) current.action.getValue(javax.swing.Action.NAME);
          if (entryDescription == null)
            entryDescription = "Unknown action";

          queueString = queueString + entryDescription;
          System.out.println(queueString);
          statusBuffer.append(queueString + "\r\n");
        }
      }
      statusBuffer.append("Stack Trace:\r\n");
      StackTraceElement[] stackTrace = storeThread.getStackTrace();
      for (StackTraceElement stackLine: stackTrace) {
        statusBuffer.append("  " + stackLine + "\r\n");
      }

    } else {
      getLogger().log(Level.INFO, "No Action Thread.");
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      for (StackTraceElement stackLine: stackTrace) {
        statusBuffer.append("  " + stackLine + "\r\n");
      }
    }

    Pooka.getUIFactory().showMessage(statusBuffer.toString(), "Status for " + getStoreID());
  }

  // Accessor methods.

  public Store getStore() {
    return store;
  }

  private void setStore(Store newValue) {
    store=newValue;
  }

  /**
   * This returns the StoreID.
   */
  public String getStoreID() {
    return storeID;
  }

  /**
   * This returns the ItemID, which in this case is the StoreID.
   */
  public String getItemID() {
    return getStoreID();
  }

  /**
   * This sets the storeID.
   */
  private void setStoreID(String newValue) {
    storeID=newValue;
  }

  /**
   * This returns the property which defines this StoreInfo, such as
   * "Store.myStore".
   */
  public String getStoreProperty() {
    return "Store." + getStoreID();
  }

  /**
   * This returns the item property, which in this case is the same as
   * the storeProperty.
   */
  public String getItemProperty() {
    return getStoreProperty();
  }

  /**
   * Returns the protocol we're using.
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Returns true if this is a pop3 store.
   */
  public boolean isPopStore() {
    return popStore;
  }

  public Vector getChildren() {
    return children;
  }

  public StoreNode getStoreNode() {
    return storeNode;
  }

  public void setStoreNode(StoreNode newValue) {
    storeNode = newValue;
  }

  public boolean isConnected() {
    return connected;
  }

  public boolean isAvailable() {
    return available;
  }

  public UserProfile getDefaultProfile() {
    return defaultProfile;
  }

  public NetworkConnection getConnection() {
    return connection;
  }

  public ActionThread getStoreThread() {
    return storeThread;
  }

  public void setStoreThread(ActionThread newValue) {
    storeThread=newValue;
  }

  public FolderInfo getTrashFolder() {
    return trashFolder;
  }

  /**
   * Returns the preferredStatus of this StoreInfo.
   */
  public int getPreferredStatus() {
    return mPreferredStatus;
  }

  /**
   * Sets the preferredStatus of this StoreInfo.
   */
  public void setPreferredStatus(int pPreferredStatus) {
    mPreferredStatus = pPreferredStatus;
  }

  /**
   * This returns whether or not this Store is set up to use the
   * TrashFolder.  If StoreProperty.useTrashFolder is set, return that as
   * a boolean.  Otherwise, return Pooka.useTrashFolder as a boolean.
   */
  public boolean useTrashFolder() {
    if (getTrashFolder() == null)
      return false;

    String prop = Pooka.getProperty(getStoreProperty() + ".useTrashFolder", "");
    if (!prop.equals(""))
      return (! prop.equalsIgnoreCase("false"));
    else
      return (! Pooka.getProperty("Pooka.useTrashFolder", "true").equalsIgnoreCase("true"));

  }


  public void setTrashFolder(FolderInfo newValue) {
    trashFolder = newValue;
  }

  /**
   * Returns the logger for this Store.
   */
  public Logger getLogger() {
    if (mLogger == null) {
      mLogger = Logger.getLogger(getStoreProperty());
    }
    return mLogger;
  }

  /**
   * Updates the debug status on the session.
   */
  void updateSessionDebug() {
    if (Pooka.getProperty("Pooka.sessionDebug", "false").equalsIgnoreCase("true") || (! Pooka.getProperty(getStoreProperty() + ".sessionDebug.logLevel", "OFF").equalsIgnoreCase("OFF"))) {
      mSession.setDebug(true);
    } else {
      mSession.setDebug(false);
    }
  }
}
