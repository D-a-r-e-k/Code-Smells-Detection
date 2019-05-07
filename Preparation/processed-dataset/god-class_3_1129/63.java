// private method to allow AsyncSample to reset the value without performing checks 
private void setCookieManagerProperty(CookieManager value) {
    setProperty(new TestElementProperty(COOKIE_MANAGER, value));
}
