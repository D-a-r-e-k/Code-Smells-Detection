protected void makeLogTableListenToCategoryExplorer() {
    ActionListener listener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _table.getFilteredLogTableModel().refresh();
            updateStatusLabel();
        }
    };
    _categoryExplorerTree.getExplorerModel().addActionListener(listener);
}
