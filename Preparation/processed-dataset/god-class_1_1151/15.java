/**
    * Try to obtain correct Connection (or create one if not yet existent)
    */
ConnectionTable.Connection getConnection(Address dest) throws Exception {
    Connection conn;
    SocketChannel sock_ch;
    synchronized (conns) {
        conn = (Connection) conns.get(dest);
        if (conn == null) {
            InetSocketAddress destAddress = new InetSocketAddress(((IpAddress) dest).getIpAddress(), ((IpAddress) dest).getPort());
            sock_ch = SocketChannel.open(destAddress);
            sock_ch.socket().setTcpNoDelay(tcp_nodelay);
            conn = new Connection(sock_ch, dest);
            conn.sendLocalAddress(local_addr);
            // This outbound connection is ready 
            sock_ch.configureBlocking(false);
            try {
                if (log.isTraceEnabled())
                    log.trace("About to change new connection send buff size from " + sock_ch.socket().getSendBufferSize() + " bytes");
                sock_ch.socket().setSendBufferSize(send_buf_size);
                if (log.isTraceEnabled())
                    log.trace("Changed new connection send buff size to " + sock_ch.socket().getSendBufferSize() + " bytes");
            } catch (IllegalArgumentException ex) {
                if (log.isErrorEnabled())
                    log.error("exception setting send buffer size to " + send_buf_size + " bytes: " + ex);
            }
            try {
                if (log.isTraceEnabled())
                    log.trace("About to change new connection receive buff size from " + sock_ch.socket().getReceiveBufferSize() + " bytes");
                sock_ch.socket().setReceiveBufferSize(recv_buf_size);
                if (log.isTraceEnabled())
                    log.trace("Changed new connection receive buff size to " + sock_ch.socket().getReceiveBufferSize() + " bytes");
            } catch (IllegalArgumentException ex) {
                if (log.isErrorEnabled())
                    log.error("exception setting receive buffer size to " + send_buf_size + " bytes: " + ex);
            }
            int idx;
            synchronized (m_lockNextWriteHandler) {
                idx = m_nextWriteHandler = (m_nextWriteHandler + 1) % m_writeHandlers.length;
            }
            conn.setupWriteHandler(m_writeHandlers[idx]);
            // Put the new connection to the queue 
            try {
                synchronized (m_lockNextReadHandler) {
                    idx = m_nextReadHandler = (m_nextReadHandler + 1) % m_readHandlers.length;
                }
                m_readHandlers[idx].add(conn);
            } catch (InterruptedException e) {
                if (log.isWarnEnabled())
                    log.warn("Thread (" + Thread.currentThread().getName() + ") was interrupted, closing connection", e);
                // What can we do? Remove it from table then. 
                conn.destroy();
                throw e;
            }
            // Add connection to table 
            addConnection(dest, conn);
            notifyConnectionOpened(dest);
            if (log.isTraceEnabled())
                log.trace("created socket to " + dest);
        }
        return conn;
    }
}
