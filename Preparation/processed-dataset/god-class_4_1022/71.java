// as defined in javax.mail.event.MessageCountListener 
public void messagesAdded(MessageCountEvent e) {
    folderLog(Level.FINE, "Messages added.");
    if (Thread.currentThread() == getFolderThread())
        runMessagesAdded(e);
    else
        getFolderThread().addToQueue(new net.suberic.util.thread.ActionWrapper(new javax.swing.AbstractAction() {

            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                runMessagesAdded((MessageCountEvent) actionEvent.getSource());
            }
        }, getFolderThread()), new java.awt.event.ActionEvent(e, 1, "message-count-changed"));
}
