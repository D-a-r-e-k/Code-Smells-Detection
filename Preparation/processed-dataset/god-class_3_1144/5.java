/** 
     * Sets the public identifier. 
     *
     * @param publicId The new public identifier.
     */
public void setPublicId(String publicId) {
    super.setPublicId(publicId);
    if (fInputSource == null) {
        fInputSource = new InputSource();
    }
    fInputSource.setPublicId(publicId);
}
