package net.suberic.pooka.resource;

import net.suberic.util.*;
import net.suberic.pooka.*;
import net.suberic.pooka.ssl.*;

import javax.activation.*;
import java.io.*;
import java.util.*;
import java.net.*;

/**
 * A PookaResourceManager which uses a JDBC connection via the Preferences API.
 */
public class JDBCResourceManager extends ResourceManager {

  /**
   * Creates a VariableBundle that uses a JDBC connection.
   */
  public VariableBundle createVariableBundle(String fileName, VariableBundle defaults) {
    System.err.println("using jdbc.");
    //Thread.currentThread().dumpStack();
    //System.setProperty("JDBCPreferences.driverName", "com.mysql.jdbc.Driver");
    //System.setProperty("JDBCPreferences.url", "jdbc:mysql://localhost:3306/dbname");
    //System.setProperty("JDBCPreferences.user", "");
    //System.setProperty("JDBCPreferences.password", "");
    System.setProperty("java.util.prefs.PreferencesFactory", "net.suberic.util.prefs.JDBCPreferencesFactory");

    /*
    System.out.println("driver: " + System.getProperty("JDBCPreferences.driverName"));
    System.out.println("url: " + System.getProperty("JDBCPreferences.url"));
    System.out.println("user: " + System.getProperty("JDBCPreferences.user"));
    System.out.println("password: " + System.getProperty("JDBCPreferences.password"));
    */
    try {
      return new net.suberic.util.PreferencesVariableBundle(Class.forName("net.suberic.pooka.Pooka"), defaults);
    } catch (Exception e) {
      e.printStackTrace();
      return defaults;
    }
  }

  /**
   * Creates a MailcapCommandMap to be used.
   */
  public MailcapCommandMap createMailcap(String fileName) {
    return new FullMailcapCommandMap();
  }

  /**
   * Creates a PookaTrustManager.
   */
  public PookaTrustManager createPookaTrustManager(javax.net.ssl.TrustManager[] pTrustManagers, String fileName) {
    return new PookaTrustManager(pTrustManagers, null, false);
  }

  /**
   * Creates an output file which includes only resources that are appropriate
   * to a Diskless client.
   */
  public static void exportResources(File pOutputFile, boolean pIncludePasswords) throws IOException {
    VariableBundle sourceBundle = Pooka.getResources();

    pOutputFile.createNewFile();
    VariableBundle newWritableProperties = new FileVariableBundle(pOutputFile, null);

    // first go through and edit out the inappropriate stores.

    List allStores = Pooka.getStoreManager().getStoreList();
    List toRemoveList = new ArrayList();
    List keepList = new ArrayList();

    Iterator iter = allStores.iterator();
    while (iter.hasNext()) {
      // if they're not imap, exclude them.  if they are imap, set them not
      // to cache.
      StoreInfo current = (StoreInfo) iter.next();

      if (current.getProtocol() != null && current.getProtocol().toLowerCase().startsWith("imap")) {
        newWritableProperties.setProperty(current.getStoreProperty() + ".cachingEnabled", "false");
        keepList.add(current.getStoreID());
      } else {
        toRemoveList.add(current.getStoreID());
      }
    }

    //Enumeration names = newWritableProperties.propertyNames();
    //Enumeration names = sourceBundle.getWritableProperties().propertyNames();
    Enumeration names = sourceBundle.getProperties().propertyNames();

    while (names.hasMoreElements()) {
      String current = (String) names.nextElement();

      boolean keep = true;
      if (current.startsWith("Store")) {
        if ((! pIncludePasswords) && current.endsWith("password")) {
          keep = false;
        } else if (current.endsWith("cachingEnabled")) {
          keep = false;
        }

        for (int i = 0; keep && i < toRemoveList.size(); i++) {
          if (current.startsWith("Store." + (String) toRemoveList.get(i))) {
            keep = false;
          }
        }
      }

      if (keep) {
        newWritableProperties.setProperty(current, sourceBundle.getProperty(current));
      }

    }

    // don't use local files.
    newWritableProperties.setProperty("Pooka.useLocalFiles", "false");

    // put only the kept stores in the store list.
    newWritableProperties.setProperty("Store", VariableBundle.convertToString(keepList));

    //FileOutputStream outStream = new FileOutputStream(pOutputFile);

    //newWritableProperties.setSaveFile(pOutputFile);
    newWritableProperties.saveProperties();

    //outStream.close();


  }

  /**
   * Gets a resource for reading.  pFileName could be a URL or a file name
   * or some similar identifier that the
   */
  public java.io.InputStream getInputStream(String pFileName)
    throws java.io.IOException {
    try {
      URL url = new URL(pFileName);
      return url.openStream();
    } catch (MalformedURLException mue) {
      throw new IOException("Error opening URL:  " + mue.getMessage());
    }
  }

  public java.io.OutputStream getOutputStream(String pFileName)
    throws java.io.IOException {
    // no writing to streams in this one.
    throw new IOException("Diskless mode:  no file modification available.");
  }

  /**
   * Creates an appropriate FolderInfo for the given StoreInfo.
   */
  public FolderInfo createFolderInfo(StoreInfo pStore, String pName) {
    String storeProperty = pStore.getStoreProperty();
    if (pStore.isPopStore() && pName.equalsIgnoreCase("INBOX")) {
      return new PopInboxFolderInfo(pStore, pName);
    } else if (Pooka.getProperty(storeProperty + ".protocol", "mbox").equalsIgnoreCase("imap")) {
      return  new UIDFolderInfo(pStore, pName);
    } else {
      return new FolderInfo(pStore, pName);
    }
  }

}
