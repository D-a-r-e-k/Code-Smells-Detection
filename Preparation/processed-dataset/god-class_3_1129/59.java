public void setAuthManager(AuthManager value) {
    AuthManager mgr = getAuthManager();
    if (mgr != null) {
        log.warn("Existing AuthManager " + mgr.getName() + " superseded by " + value.getName());
    }
    setProperty(new TestElementProperty(AUTH_MANAGER, value));
}
