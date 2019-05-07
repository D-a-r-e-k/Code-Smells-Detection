/**
   * Shows a status message, using the given FolderDisplayUI if not null.
   */
public void showStatusMessage(net.suberic.pooka.gui.FolderDisplayUI pUI, String message) {
    if (pUI != null)
        pUI.showStatusMessage(message);
    else
        Pooka.getUIFactory().showStatusMessage(message);
}
