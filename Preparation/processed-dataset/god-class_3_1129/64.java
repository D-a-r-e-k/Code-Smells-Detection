public void setCookieManager(CookieManager value) {
    CookieManager mgr = getCookieManager();
    if (mgr != null) {
        log.warn("Existing CookieManager " + mgr.getName() + " superseded by " + value.getName());
    }
    setCookieManagerProperty(value);
}
