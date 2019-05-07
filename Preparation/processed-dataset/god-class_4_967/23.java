/******************************************************************************/
protected boolean assertDoublePositiveSign(String value, boolean includeZero, String name, ArrayList errList) {
    double val = Double.parseDouble(value);
    boolean isOK = true;
    if (includeZero) {
        isOK = val >= 0.0;
    } else {
        isOK = val > 0.0;
    }
    if (!isOK) {
        String errMsg = name;
        if (includeZero) {
            errMsg += " has to be equal or bigger than 0.0";
        } else {
            errMsg += " has to be bigger than 0.0";
        }
        //            JOptionPane.showMessageDialog(this, errMsg, "Error", JOptionPane.INFORMATION_MESSAGE); 
        errList.add(errMsg);
        return false;
    }
    return isOK;
}
