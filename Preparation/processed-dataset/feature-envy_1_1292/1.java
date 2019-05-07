/**
     * Create a new MT940
     */
public MT940(MainFrame parent, AccountChooser ac) {
    mainFrame = parent;
    accountChooser = ac;
    number.setMinimumFractionDigits(2);
    number.setMaximumFractionDigits(2);
}
