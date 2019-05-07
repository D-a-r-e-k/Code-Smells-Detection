/**
   * Synchronizes the locally stored subscribed folders list to the subscribed
   * folder information from the IMAP server.
   */
public void synchSubscribed() throws MessagingException, OperationCancelledException {
    // require the inbox.  this is to work around a bug in which the inbox 
    // doesn't show up in certain conditions. 
    boolean foundInbox = false;
    Folder[] subscribedFolders = store.getDefaultFolder().list();
    ArrayList subscribedNames = new ArrayList();
    for (int i = 0; subscribedFolders != null && i < subscribedFolders.length; i++) {
        // sometimes listSubscribed() doesn't work. 
        // and sometimes list() returns duplicate entries for some reason. 
        String folderName = subscribedFolders[i].getName();
        if (folderName.equalsIgnoreCase("inbox")) {
            if (!foundInbox) {
                foundInbox = true;
                subscribedNames.add(folderName);
            }
        } else if (subscribedFolders[i].isSubscribed()) {
            if (!subscribedNames.contains(folderName))
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
            if (fi.isNamespace() && !subscribedNames.contains(folderName))
                subscribedNames.add(folderName);
        }
    }
    Collections.sort(subscribedNames);
    // keep the existing order when possible. 
    List<String> currentSubscribed = Pooka.getResources().getPropertyAsList(getStoreProperty() + ".folderList", "");
    Iterator<String> currentIter = currentSubscribed.iterator();
    while (currentIter.hasNext()) {
        String folder = currentIter.next();
        if (!subscribedNames.contains(folder)) {
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
