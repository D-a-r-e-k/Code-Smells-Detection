/**
	 * Loads KeyManagers. KeyManagers are responsible for managing 
	 * the key material which is used to authenticate the local 
	 * SSLSocket to its peer. Can return null.
	 */
public KeyManager[] loadKeyManagers(QuickServerConfig config) throws GeneralSecurityException, IOException {
    Secure secure = config.getSecure();
    SecureStore secureStore = secure.getSecureStore();
    if (secureStore == null) {
        logger.fine("SecureStore configuration not set! " + "So returning null for KeyManager");
        return null;
    }
    KeyStoreInfo keyStoreInfo = secureStore.getKeyStoreInfo();
    if (keyStoreInfo == null) {
        logger.fine("KeyStoreInfo configuration not set! " + "So returning null for KeyManager");
        return null;
    }
    logger.finest("Loading KeyManagers");
    KeyStore ks = getKeyStoreForKey(secureStore.getType(), secureStore.getProvider());
    char storepass[] = null;
    if (keyStoreInfo.getStorePassword() != null) {
        logger.finest("KeyStore: Store password was present!");
        storepass = keyStoreInfo.getStorePassword().toCharArray();
    } else {
        logger.finest("KeyStore: Store password was not set.. so asking!");
        if (sensitiveInput == null) {
            sensitiveInput = new SensitiveInput(config.getName() + " - Input Prompt");
        }
        storepass = sensitiveInput.getInput("Store password for KeyStore");
        if (storepass == null) {
            logger.finest("No password entered.. will pass null");
        }
    }
    InputStream keyStoreStream = null;
    try {
        if (keyStoreInfo.getStoreFile().equalsIgnoreCase("none") == false) {
            logger.finest("KeyStore location: " + ConfigReader.makeAbsoluteToConfig(keyStoreInfo.getStoreFile(), config));
            keyStoreStream = new FileInputStream(ConfigReader.makeAbsoluteToConfig(keyStoreInfo.getStoreFile(), config));
        }
        ks.load(keyStoreStream, storepass);
        logger.finest("KeyStore loaded");
    } finally {
        if (keyStoreStream != null) {
            keyStoreStream.close();
            keyStoreStream = null;
        }
    }
    char keypass[] = null;
    if (keyStoreInfo.getKeyPassword() != null) {
        logger.finest("KeyStore: key password was present!");
        keypass = keyStoreInfo.getKeyPassword().toCharArray();
    } else {
        logger.finest("KeyStore: Key password was not set.. so asking!");
        if (sensitiveInput == null) {
            sensitiveInput = new SensitiveInput(config.getName() + " - Input Prompt");
        }
        keypass = sensitiveInput.getInput("Key password for KeyStore");
        if (keypass == null) {
            logger.finest("No password entered.. will pass blank");
            keypass = "".toCharArray();
        }
    }
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(secureStore.getAlgorithm());
    kmf.init(ks, keypass);
    storepass = "               ".toCharArray();
    storepass = null;
    keypass = "               ".toCharArray();
    keypass = null;
    return kmf.getKeyManagers();
}
