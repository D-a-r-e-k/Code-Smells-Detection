public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    Entry entry = (Entry) value;
    entryRenderer.setLook(isSelected, index);
    if (entry != Entry.PROTOTYPE) {
        entryRenderer.setDateFormat(AccountEntriesPanel.this.userDate);
        entryRenderer.setAccount(AccountEntriesPanel.this.account);
        entryRenderer.setModel(entry);
        entryRenderer.setBalance(balances[index + 1]);
    }
    return entryRenderer;
}
