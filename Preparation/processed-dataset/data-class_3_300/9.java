/**
     * Extracts all the required authorization for that particular URL request
     * and sets it in the <code>HttpMethod</code> passed in.
     *
     * @param client the HttpClient object
     *
     * @param u
     *            <code>URL</code> of the URL request
     * @param authManager
     *            the <code>AuthManager</code> containing all the authorisations for
     *            this <code>UrlConfig</code>
     */
private void setConnectionAuthorization(HttpClient client, URL u, AuthManager authManager) {
    HttpState state = client.getState();
    if (authManager != null) {
        HttpClientParams params = client.getParams();
        Authorization auth = authManager.getAuthForURL(u);
        if (auth != null) {
            String username = auth.getUser();
            String realm = auth.getRealm();
            String domain = auth.getDomain();
            if (log.isDebugEnabled()) {
                log.debug(username + " >  D=" + username + " D=" + domain + " R=" + realm);
            }
            state.setCredentials(new AuthScope(u.getHost(), u.getPort(), realm.length() == 0 ? null : realm, AuthScope.ANY_SCHEME), // NT Includes other types of Credentials 
            new NTCredentials(username, auth.getPass(), localHost, domain));
            // We have credentials - should we set pre-emptive authentication? 
            if (canSetPreEmptive) {
                log.debug("Setting Pre-emptive authentication");
                params.setAuthenticationPreemptive(true);
            }
        } else {
            state.clearCredentials();
            if (canSetPreEmptive) {
                params.setAuthenticationPreemptive(false);
            }
        }
    } else {
        state.clearCredentials();
    }
}
