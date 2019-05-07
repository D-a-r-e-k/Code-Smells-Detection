@Override
public void addTestElement(TestElement el) {
    if (el instanceof CookieManager) {
        setCookieManager((CookieManager) el);
    } else if (el instanceof CacheManager) {
        setCacheManager((CacheManager) el);
    } else if (el instanceof HeaderManager) {
        setHeaderManager((HeaderManager) el);
    } else if (el instanceof AuthManager) {
        setAuthManager((AuthManager) el);
    } else {
        super.addTestElement(el);
    }
}
