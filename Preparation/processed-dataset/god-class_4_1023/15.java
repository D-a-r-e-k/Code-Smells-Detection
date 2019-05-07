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
            found = true;
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
