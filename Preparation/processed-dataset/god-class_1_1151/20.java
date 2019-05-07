/**
    * Finds first available port starting at start_port and returns server socket. Sets srv_port
    */
protected ServerSocket createServerSocket(int start_port, int end_port) throws Exception {
    this.m_acceptSelector = Selector.open();
    m_serverSocketChannel = ServerSocketChannel.open();
    m_serverSocketChannel.configureBlocking(false);
    while (true) {
        try {
            SocketAddress sockAddr;
            if (bind_addr == null) {
                sockAddr = new InetSocketAddress(start_port);
                m_serverSocketChannel.socket().bind(sockAddr);
            } else {
                sockAddr = new InetSocketAddress(bind_addr, start_port);
                m_serverSocketChannel.socket().bind(sockAddr, backlog);
            }
        } catch (BindException bind_ex) {
            if (start_port == end_port)
                throw (BindException) ((new BindException("No available port to bind to (start_port=" + start_port + ")")).initCause(bind_ex));
            start_port++;
            continue;
        } catch (SocketException bind_ex) {
            if (start_port == end_port)
                throw (BindException) ((new BindException("No available port to bind to  (start_port=" + start_port + ")")).initCause(bind_ex));
            start_port++;
            continue;
        } catch (IOException io_ex) {
            if (log.isErrorEnabled())
                log.error("Attempt to bind serversocket failed, port=" + start_port + ", bind addr=" + bind_addr, io_ex);
            throw io_ex;
        }
        srv_port = start_port;
        break;
    }
    m_serverSocketChannel.register(this.m_acceptSelector, SelectionKey.OP_ACCEPT);
    return m_serverSocketChannel.socket();
}
