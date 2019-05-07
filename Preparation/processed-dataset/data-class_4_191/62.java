/**
   * This removes all the messages in the folder, if the folder is a
   * TrashFolder.
   */
public void emptyTrash() {
    if (isTrashFolder()) {
        try {
            Message[] allMessages = getFolder().getMessages();
            getFolder().setFlags(allMessages, new Flags(Flags.Flag.DELETED), true);
            getFolder().expunge();
        } catch (MessagingException me) {
            String m = Pooka.getProperty("error.trashFolder.EmptyTrashError", "Error emptying Trash:") + "\n" + me.getMessage();
            if (getFolderDisplayUI() != null)
                getFolderDisplayUI().showError(m);
            else
                folderLog(Level.FINE, m);
        }
    }
}
