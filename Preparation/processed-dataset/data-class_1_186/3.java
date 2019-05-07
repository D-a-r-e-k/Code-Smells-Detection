/**
   * Creates a PookaTrustManager.
   */
public PookaTrustManager createPookaTrustManager(javax.net.ssl.TrustManager[] pTrustManagers, String fileName) {
    return new PookaTrustManager(pTrustManagers, null, false);
}
