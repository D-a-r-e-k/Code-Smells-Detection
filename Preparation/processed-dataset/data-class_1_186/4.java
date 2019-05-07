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
            if ((!pIncludePasswords) && current.endsWith("password")) {
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
}
