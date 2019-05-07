public static void stateChanged(boolean updateTree) {
    setFileNeedsSavingIndicator(true);
    if (updateTree) {
        jagGenerator.tree.updateUI();
    }
}
