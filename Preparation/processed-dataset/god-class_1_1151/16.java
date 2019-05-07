public final void start() throws Exception {
    super.start();
    init();
    srv_sock = createServerSocket(srv_port, max_port);
    if (external_addr != null)
        local_addr = new IpAddress(external_addr, srv_sock.getLocalPort());
    else if (bind_addr != null)
        local_addr = new IpAddress(bind_addr, srv_sock.getLocalPort());
    else
        local_addr = new IpAddress(srv_sock.getLocalPort());
    if (log.isDebugEnabled())
        log.debug("server socket created on " + local_addr);
    //Roland Kurmann 4/7/2003, put in thread_group 
    acceptor = getThreadFactory().newThread(thread_group, this, "ConnectionTable.AcceptorThread");
    acceptor.setDaemon(true);
    acceptor.start();
    m_backGroundThreads.add(acceptor);
    // start the connection reaper - will periodically remove unused connections 
    if (use_reaper && reaper == null) {
        reaper = new Reaper();
        reaper.start();
    }
}
