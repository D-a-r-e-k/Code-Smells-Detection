/**
   * Clears the status message.
   */
public void clearStatusMessage(net.suberic.pooka.gui.FolderDisplayUI pUI) {
    if (pUI != null)
        pUI.clearStatusMessage();
    else
        Pooka.getUIFactory().clearStatus();
}
