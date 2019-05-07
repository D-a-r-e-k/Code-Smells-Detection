/**
	 * Returns the <code>SSLContext</code> object that implements the specified 
	 * secure socket protocol from Secure configuring.
	 * @see #loadSSLContext
	 * @param protocol the standard name of the requested protocol. If <code>null</code> will use the protocol set in secure configuration of the server.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @since 1.4.0
	 */
public SSLContext getSSLContext(String protocol) throws IOException, NoSuchAlgorithmException, KeyManagementException {
    if (sslc == null)
        loadSSLContext();
    if (protocol != null && secureStoreManager != null) {
        SSLContext _sslc = secureStoreManager.getSSLContext(protocol);
        _sslc.init(km, tm, null);
        return _sslc;
    }
    return sslc;
}
