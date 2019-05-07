/**
   * As defined in javax.mail.MessageCountListener.
   *
   * This runs when we get a notification that messages have been
   * removed from the mail server.
   *
   * This implementation just moves the handling of the event to the
   * FolderThread, where it runs runMessagesRemoved().
   */
public void messagesRemoved(MessageCountEvent e) {
    folderLog(Level.FINE, "Messages Removed.");
    if (Thread.currentThread() == getFolderThread()) {
        runMessagesRemoved(e);
    } else {
        getFolderThread().addToQueue(new net.suberic.util.thread.ActionWrapper(new javax.swing.AbstractAction() {

            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                runMessagesRemoved((MessageCountEvent) actionEvent.getSource());
            }
        }, getFolderThread()), new java.awt.event.ActionEvent(e, 1, "messages-removed"));
    }
}
