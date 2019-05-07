////////////////////////////////////////////////////////////////////  
// Implementation of org.xml.sax.Locator  
////////////////////////////////////////////////////////////////////  
/**
     * Return the saved public identifier.
     *
     * @return The public identifier as a string, or null if none
     *         is available.
     * @see org.xml.sax.Locator#getPublicId
     * @see #setPublicId
     */
public String getPublicId() {
    return publicId;
}
