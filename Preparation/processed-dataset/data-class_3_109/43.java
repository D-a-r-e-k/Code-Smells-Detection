private void init() {
    initComponents();
    Iterator types = jdbcTypes.iterator();
    while (types.hasNext()) {
        String jdbcType = (String) types.next();
        jdbcTypeComboBox.addItem(jdbcType);
    }
}
