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
            found = true;
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
