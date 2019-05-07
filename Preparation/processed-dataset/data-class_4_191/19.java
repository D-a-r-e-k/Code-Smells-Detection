/**
   * Synchronizes the locally stored subscribed folders list to the subscribed
   * folder information from the IMAP server.
   */
public void synchSubscribed() throws MessagingException, OperationCancelledException {
    // if we're a namespace, then ignore. 
    if (mNamespace)
        return;
    // at this point we should get folder objects. 
    if (!isLoaded())
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
        Pooka.setProperty(getFolderProperty() + ".folderList", VariableBundle.convertToString(currentSubscribed));
        for (int i = 0; children != null && i < children.size(); i++) {
            FolderInfo fi = (FolderInfo) children.elementAt(i);
            fi.synchSubscribed();
        }
    }
}
