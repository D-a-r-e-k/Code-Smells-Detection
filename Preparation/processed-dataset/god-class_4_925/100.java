/**
	 * Sets next client has a trusted client. 
	 * <p>This will skip any authentication and will not set any timout.</p>
	 * @since 1.3.2
	 */
public void nextClientIsTrusted() {
    setSkipValidation(true);
}
