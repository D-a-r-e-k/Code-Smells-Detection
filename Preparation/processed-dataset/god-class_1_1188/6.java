/**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
public boolean isValid() {
    try {
        validate();
    } catch (org.exolab.castor.xml.ValidationException vex) {
        return false;
    }
    return true;
}
