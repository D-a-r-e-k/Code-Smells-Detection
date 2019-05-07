/**
	 * Returns a SSLSocketFactory object to be used for creating SSLSockets. 
	 * @see #setSecure
	 * @param protocol the standard name of the requested protocol. If 
	 * <code>null</code> will use the protocol set in secure configuration 
	 * of the server.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @since 1.4.0
	 */
public SSLSocketFactory getSSLSocketFactory(String protocol) throws IOException, NoSuchAlgorithmException, KeyManagementException {
    if (sslc == null)
        loadSSLContext();
    return secureStoreManager.getSocketFactory(getSSLContext(protocol));
}
