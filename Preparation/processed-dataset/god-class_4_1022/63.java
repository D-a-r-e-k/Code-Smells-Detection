/**
   * This resets the defaultActions.  Useful when this goes to and from
   * being a trashFolder, since only trash folders have emptyTrash
   * actions.
   */
public void resetDefaultActions() {
    if (isTrashFolder()) {
        defaultActions = new Action[] { new net.suberic.util.thread.ActionWrapper(new UpdateCountAction(), getFolderThread()), new net.suberic.util.thread.ActionWrapper(new EmptyTrashAction(), getFolderThread()), new EditPropertiesAction() };
    } else if (isOutboxFolder()) {
        defaultActions = new Action[] { new net.suberic.util.thread.ActionWrapper(new UpdateCountAction(), getFolderThread()), new net.suberic.util.thread.ActionWrapper(new SendAllAction(), getFolderThread()), new EditPropertiesAction() };
    } else {
        defaultActions = new Action[] { new net.suberic.util.thread.ActionWrapper(new UpdateCountAction(), getFolderThread()), new EditPropertiesAction() };
    }
}
