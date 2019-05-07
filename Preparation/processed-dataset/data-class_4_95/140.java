/**
	 * Register the given channel for the given operations. This adds the request
	 * to a list and will be processed after selector select wakes up.
	 * @return boolean flag to indicate if new entry was added to the list to register.
	 * @since 1.4.5
	 */
public boolean registerChannel(SocketChannel channel, int ops, Object att) throws IOException, ClosedChannelException {
    if (getSelector() == null) {
        throw new IllegalStateException("Selector is not open!");
    }
    if (channel == null) {
        throw new IllegalArgumentException("Can't register a null channel!");
    }
    if (channel.isConnected() == false) {
        throw new ClosedChannelException();
    }
    RegisterChannelRequest req = new RegisterChannelRequest(channel, ops, att);
    RegisterChannelRequest reqOld = null;
    synchronized (registerChannelRequestMap) {
        reqOld = (RegisterChannelRequest) registerChannelRequestMap.get(channel);
        if (reqOld == null) {
            registerChannelRequestMap.put(channel, req);
            getSelector().wakeup();
            return true;
        } else {
            if (reqOld.equals(req) == false) {
                reqOld.setOps(reqOld.getOps() | req.getOps());
                reqOld.setAtt(req.getAtt());
                return true;
            }
            return false;
        }
    }
}
