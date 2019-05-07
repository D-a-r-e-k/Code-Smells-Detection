protected void setMaxRecordConfiguration() {
    LogFactor5InputDialog inputDialog = new LogFactor5InputDialog(getBaseFrame(), "Set Max Number of Records", "", 10);
    String temp = inputDialog.getText();
    if (temp != null) {
        try {
            setMaxNumberOfLogRecords(Integer.parseInt(temp));
        } catch (NumberFormatException e) {
            LogFactor5ErrorDialog error = new LogFactor5ErrorDialog(getBaseFrame(), "'" + temp + "' is an invalid parameter.\nPlease try again.");
            setMaxRecordConfiguration();
        }
    }
}
