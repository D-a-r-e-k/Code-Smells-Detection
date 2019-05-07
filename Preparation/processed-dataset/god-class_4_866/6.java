// getEncoding():String 
/** Returns the public identifier. */
public String getPublicId() {
    return fCurrentEntity != null ? fCurrentEntity.publicId : null;
}
