////////////////////////////////////////////////////////////////////  
// Setters for the properties (not in org.xml.sax.Locator)  
////////////////////////////////////////////////////////////////////  
/**
     * Set the public identifier for this locator.
     *
     * @param publicId The new public identifier, or null 
     *        if none is available.
     * @see #getPublicId
     */
public void setPublicId(String publicId) {
    this.publicId = publicId;
}
