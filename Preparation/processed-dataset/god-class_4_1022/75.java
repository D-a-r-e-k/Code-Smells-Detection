/**
   * This updates the TableInfo on the changed messages.
   *
   * As defined by java.mail.MessageChangedListener.
   */
public void messageChanged(MessageChangedEvent e) {
    // blech.  we really have to do this on the action thread. 
    if (Thread.currentThread() == getFolderThread())
        runMessageChanged(e);
    else
        getFolderThread().addToQueue(new net.suberic.util.thread.ActionWrapper(new javax.swing.AbstractAction() {

            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                runMessageChanged((MessageChangedEvent) actionEvent.getSource());
            }
        }, getFolderThread()), new java.awt.event.ActionEvent(e, 1, "message-changed"));
}
