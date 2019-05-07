private void initLookAndFeel() {
    try {
        UIManager.setLookAndFeel(userProperties.getLookAndFeel());
    } catch (Exception ex) {
        System.err.println("Invalid/missing Look&Feel: " + userProperties.getLookAndFeel());
    }
    updateUIs();
}
