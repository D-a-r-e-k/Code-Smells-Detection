/**
	 * Loads the <code>SSLContext</code> from Secure configuring if set.
	 * @see #setSecure
	 * @since 1.4.0
	 */
public void loadSSLContext() throws IOException {
    if (getSecure().isLoad() == false) {
        throw new IllegalStateException("Secure setting is not yet enabled for loading!");
    }
    logger.info("Loading Secure Context..");
    km = null;
    tm = null;
    try {
        String ssManager = "org.quickserver.security.SecureStoreManager";
        if (getSecure().getSecureStore() != null)
            ssManager = getSecure().getSecureStore().getSecureStoreManager();
        Class secureStoreManagerClass = getClass(ssManager, true);
        secureStoreManager = (SecureStoreManager) secureStoreManagerClass.newInstance();
        km = secureStoreManager.loadKeyManagers(getConfig());
        logger.fine("KeyManager got");
        tm = secureStoreManager.loadTrustManagers(getConfig());
        logger.fine("TrustManager got");
        sslc = secureStoreManager.getSSLContext(getConfig().getSecure().getProtocol());
        sslc.init(km, tm, null);
        logger.fine("SSLContext loaded");
    } catch (KeyStoreException e) {
        logger.warning("KeyStoreException : " + e);
        throw new IOException("Error creating secure socket : " + e.getMessage());
    } catch (NoSuchAlgorithmException e) {
        logger.warning("NoSuchAlgorithmException : " + e);
        throw new IOException("Error creating secure socket : " + e.getMessage());
    } catch (NoSuchProviderException e) {
        logger.warning("NoSuchProviderException : " + e);
        throw new IOException("Error creating secure socket : " + e.getMessage());
    } catch (UnrecoverableKeyException e) {
        logger.warning("UnrecoverableKeyException : " + e);
        throw new IOException("Error creating secure socket : " + e.getMessage());
    } catch (CertificateException e) {
        logger.warning("CertificateException : " + e);
        throw new IOException("Error creating secure socket : " + e.getMessage());
    } catch (KeyManagementException e) {
        logger.warning("KeyManagementException : " + e);
        throw new IOException("Error creating secure socket : " + e.getMessage());
    } catch (GeneralSecurityException e) {
        logger.warning("GeneralSecurityException : " + e);
        throw new IOException("Error creating secure socket : " + e.getMessage());
    } catch (ClassNotFoundException e) {
        logger.warning("ClassNotFoundException : " + e);
        throw new IOException("Error creating secure socket : " + e.getMessage());
    } catch (InstantiationException e) {
        logger.warning("InstantiationException : " + e);
        throw new IOException("Error creating secure socket : " + e.getMessage());
    } catch (IllegalAccessException e) {
        logger.warning("IllegalAccessException : " + e);
        throw new IOException("Error creating secure socket : " + e.getMessage());
    }
}
