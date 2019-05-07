/**
	 * Returns the <code>SSLContext</code> from Secure configuring.
	 * @see #loadSSLContext
	 * @since 1.4.0
	 */
public SSLContext getSSLContext() throws IOException, NoSuchAlgorithmException, KeyManagementException {
    return getSSLContext(null);
}
