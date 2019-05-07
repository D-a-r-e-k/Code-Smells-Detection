// setPublicId(String)  
/** 
     * Sets the system identifier. 
     *
     * @param systemId The new system identifier.
     */
public void setSystemId(String systemId) {
    super.setSystemId(systemId);
    if (fInputSource == null) {
        fInputSource = new InputSource();
    }
    fInputSource.setSystemId(systemId);
}
