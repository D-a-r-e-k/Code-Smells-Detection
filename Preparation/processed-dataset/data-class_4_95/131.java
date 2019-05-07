/**
	 * Returns a SSLSocketFactory object to be used for creating SSLSockets. 
	 * Secure socket protocol will be picked from the Secure configuring.
	 * @see #setSecure
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @since 1.4.0
	 */
public SSLSocketFactory getSSLSocketFactory() throws IOException, NoSuchAlgorithmException, KeyManagementException {
    if (sslc == null)
        loadSSLContext();
    return secureStoreManager.getSocketFactory(getSSLContext());
}
