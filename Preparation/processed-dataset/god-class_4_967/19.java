/******************************************************************************/
protected boolean assertDouble(String text, String name, ArrayList errList) {
    String errMsg = name + " is NOT a double value!";
    try {
        Double.parseDouble(text);
    } catch (NumberFormatException e) {
        //            JOptionPane.showMessageDialog(this, errMsg, "Error", JOptionPane.INFORMATION_MESSAGE); 
        errList.add(errMsg);
        return false;
    }
    return true;
}
