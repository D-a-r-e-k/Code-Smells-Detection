public PoolElement getPoolElement(int retrys, ConnectionBuffer cb) throws Exception {
    // try [retrys] times to retrieve PoolElement  
    for (int i = 0; i < retrys; i++) {
        if ((cb != null && !cb.isValid()) || Thread.currentThread().isInterrupted())
            throw new CanceledRequestException("ConnectionBuffer has been invalidated");
        PoolElement el = null;
        try {
            el = this.getPoolElement(cb);
            if (el != null)
                // if el isn't null, return it (it was checked for validity)  
                return el;
        } catch (CanceledRequestException cre) {
            throw cre;
        } catch (Exception e) {
            Server.debug(Thread.currentThread(), this.toString() + "getPoolElement: ", e, Server.MSG_AUTH, Server.LVL_MAJOR);
            el = null;
        }
    }
    if (Server.srv.isRunning())
        throw new Exception("Unable to get available PoolElement");
    else
        throw new Exception("Unable to get available PoolElement - Server shutting down");
}
