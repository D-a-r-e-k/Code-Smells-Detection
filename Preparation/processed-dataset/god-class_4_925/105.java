/**
	 * Sets the SecurityManager class
	 * @param securityManagerClass the fully qualified name of the class 
	 * that extends {@link java.lang.SecurityManager}.
	 * @see #getSecurityManagerClass
	 * @since 1.3.3
	 */
public void setSecurityManagerClass(String securityManagerClass) {
    if (securityManagerClass != null)
        this.securityManagerClass = securityManagerClass;
}
