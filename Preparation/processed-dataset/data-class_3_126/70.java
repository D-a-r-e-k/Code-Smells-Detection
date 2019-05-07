/**
   * Creates a Most Recently Used file list to be
   * displayed in the File menu
   */
protected void createMRUFileListMI(JMenu menu) {
    String[] files = _mruFileManager.getMRUFileList();
    if (files != null) {
        menu.addSeparator();
        for (int i = 0; i < files.length; i++) {
            JMenuItem result = new JMenuItem((i + 1) + " " + files[i]);
            result.setMnemonic(i + 1);
            result.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    requestOpenMRU(e);
                }
            });
            menu.add(result);
        }
    }
}
