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
        if (!folderName.equals(removeFolderName)) {
            if (!first)
                newValue.append(":");
            newValue.append(folderName);
            first = false;
        }
    }
    Pooka.setProperty(getFolderProperty() + ".folderList", newValue.toString());
}
