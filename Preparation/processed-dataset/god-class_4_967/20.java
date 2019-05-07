/******************************************************************************/
protected boolean assertInteger(String text, String name, ArrayList errList) {
    String errMsg = name + " is NOT a integer value!";
    try {
        Integer.parseInt(text);
    } catch (NumberFormatException e) {
        //            JOptionPane.showMessageDialog(this, errMsg, "Error", JOptionPane.INFORMATION_MESSAGE); 
        errList.add(errMsg);
        return false;
    }
    return true;
}
