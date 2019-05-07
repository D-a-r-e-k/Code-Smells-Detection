/**
   * Removes old file list and creates a new file list
   * with the updated MRU list.
   */
protected void updateMRUList() {
    JMenu menu = _logMonitorFrame.getJMenuBar().getMenu(0);
    menu.removeAll();
    menu.add(createOpenMI());
    menu.add(createOpenURLMI());
    menu.addSeparator();
    menu.add(createCloseMI());
    createMRUFileListMI(menu);
    menu.addSeparator();
    menu.add(createExitMI());
}
