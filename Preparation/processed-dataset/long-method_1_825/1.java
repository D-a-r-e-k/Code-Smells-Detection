public ServletLogger(WebMailServer parent, Storage st) throws WebMailException {
    this.store = st;
    if (!(parent instanceof WebMailServlet)) {
        throw new WebMailException("ServletLogger can only be used by a Servlet!");
    } else {
        this.parent = (WebMailServlet) parent;
    }
    parent.getConfigScheme().configRegisterIntegerKey(this, "LOGLEVEL", "5", "How much debug output will be written in the logfile");
    initLog();
}
