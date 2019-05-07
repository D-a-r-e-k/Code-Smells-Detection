protected void closeAfterConfirm() {
    StringBuffer message = new StringBuffer();
    if (_callSystemExitOnClose == false) {
        message.append("Are you sure you want to close the logging ");
        message.append("console?\n");
        message.append("(Note: This will not shut down the Virtual Machine,\n");
        message.append("or the Swing event thread.)");
    } else {
        message.append("Are you sure you want to exit?\n");
        message.append("This will shut down the Virtual Machine.\n");
    }
    String title = "Are you sure you want to dispose of the Logging Console?";
    if (_callSystemExitOnClose == true) {
        title = "Are you sure you want to exit?";
    }
    int value = JOptionPane.showConfirmDialog(_logMonitorFrame, message.toString(), title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null);
    if (value == JOptionPane.OK_OPTION) {
        dispose();
    }
}
