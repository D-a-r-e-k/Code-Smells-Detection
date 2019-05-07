/**
	 * Returns Secure setting for QuickServer
	 * @since 1.4.0
	 */
public Secure getSecure() {
    if (secure == null)
        secure = new Secure();
    return secure;
}
