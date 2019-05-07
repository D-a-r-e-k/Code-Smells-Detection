/**
   * This is a static calls which searches the given FolderInfo objects,
   * collects the results into a VirtualFolderInfo, and then displays
   * the results of the search in the UI.
   */
public static void searchFolders(Vector folderList, javax.mail.search.SearchTerm term) {
    final javax.mail.search.SearchTerm searchTerm = term;
    final Vector selectedFolders = folderList;
    Pooka.getSearchThread().addToQueue(new net.suberic.util.thread.ActionWrapper(new javax.swing.AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            Vector matchingValues = new Vector();
            Logger.getLogger("Pooka.debug").log(Level.FINE, "init:  matchingValues.size() = " + matchingValues.size());
            net.suberic.util.swing.ProgressDialog dialog = Pooka.getUIFactory().createProgressDialog(0, 100, 0, "Search", "Searching");
            dialog.show();
            boolean cancelled = dialog.isCancelled();
            for (int i = 0; !cancelled && i < selectedFolders.size(); i++) {
                Logger.getLogger("Pooka.debug").log(Level.FINE, "trying selected folder number " + i);
                try {
                    net.suberic.pooka.MessageInfo[] matches = ((FolderInfo) selectedFolders.elementAt(i)).search(searchTerm);
                    Logger.getLogger("Pooka.debug").log(Level.FINE, "matches.length = " + matches.length);
                    for (int j = 0; j < matches.length; j++) {
                        matchingValues.add(matches[j]);
                        Logger.getLogger("Pooka.debug").log(Level.FINE, "adding " + matches[j] + " to matchingValues.");
                    }
                } catch (MessagingException me) {
                    Logger.getLogger("Pooka.debug").log(Level.FINE, "caught exception " + me);
                } catch (OperationCancelledException oce) {
                }
                cancelled = dialog.isCancelled();
            }
            Logger.getLogger("Pooka.debug").log(Level.FINE, "got " + matchingValues.size() + " matches.");
            if (!cancelled) {
                FolderInfo[] parentFolders = new FolderInfo[selectedFolders.size()];
                for (int i = 0; i < selectedFolders.size(); i++) {
                    parentFolders[i] = (FolderInfo) selectedFolders.elementAt(i);
                }
                MessageInfo[] matchingMessages = new MessageInfo[matchingValues.size()];
                for (int i = 0; i < matchingValues.size(); i++) {
                    Logger.getLogger("Pooka.debug").log(Level.FINE, "matchingValues.elementAt(" + i + ") = " + matchingValues.elementAt(i));
                    matchingMessages[i] = (MessageInfo) matchingValues.elementAt(i);
                }
                final VirtualFolderInfo sfi = new VirtualFolderInfo(matchingMessages, parentFolders);
                Runnable runMe = new Runnable() {

                    public void run() {
                        FolderDisplayUI fdui = Pooka.getUIFactory().createFolderDisplayUI(sfi);
                        fdui.openFolderDisplay();
                    }
                };
                javax.swing.SwingUtilities.invokeLater(runMe);
            }
            dialog.dispose();
        }
    }, Pooka.getSearchThread()), new java.awt.event.ActionEvent(FolderInfo.class, 1, "search"));
}
