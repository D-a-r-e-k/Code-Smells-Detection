public void setHeaderManager(HeaderManager value) {
    HeaderManager mgr = getHeaderManager();
    if (mgr != null) {
        value = mgr.merge(value, true);
        if (log.isDebugEnabled()) {
            log.debug("Existing HeaderManager '" + mgr.getName() + "' merged with '" + value.getName() + "'");
            for (int i = 0; i < value.getHeaders().size(); i++) {
                log.debug("    " + value.getHeader(i).getName() + "=" + value.getHeader(i).getValue());
            }
        }
    }
    setProperty(new TestElementProperty(HEADER_MANAGER, value));
}
