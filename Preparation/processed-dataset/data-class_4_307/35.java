//  
// Private methods  
//  
/**
     * Normalize the attribute value of a non CDATA attributes collapsing
     * sequences of space characters (x20)
     *
     * @param attributes The list of attributes
     * @param index The index of the attribute to normalize
     */
private boolean normalizeAttrValue(XMLAttributes attributes, int index) {
    // vars  
    boolean leadingSpace = true;
    boolean spaceStart = false;
    boolean readingNonSpace = false;
    int count = 0;
    int eaten = 0;
    String attrValue = attributes.getValue(index);
    char[] attValue = new char[attrValue.length()];
    fBuffer.setLength(0);
    attrValue.getChars(0, attrValue.length(), attValue, 0);
    for (int i = 0; i < attValue.length; i++) {
        if (attValue[i] == ' ') {
            // now the tricky part  
            if (readingNonSpace) {
                spaceStart = true;
                readingNonSpace = false;
            }
            if (spaceStart && !leadingSpace) {
                spaceStart = false;
                fBuffer.append(attValue[i]);
                count++;
            } else {
                if (leadingSpace || !spaceStart) {
                    eaten++;
                }
            }
        } else {
            readingNonSpace = true;
            spaceStart = false;
            leadingSpace = false;
            fBuffer.append(attValue[i]);
            count++;
        }
    }
    // check if the last appended character is a space.  
    if (count > 0 && fBuffer.charAt(count - 1) == ' ') {
        fBuffer.setLength(count - 1);
    }
    String newValue = fBuffer.toString();
    attributes.setValue(index, newValue);
    return !attrValue.equals(newValue);
}
