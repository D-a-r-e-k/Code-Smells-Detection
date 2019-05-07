/**
    * Acceptor thread. Continuously accept new connections and assign readhandler/writehandler
    * to them.
    */
public void run() {
    Connection conn;
    while (m_serverSocketChannel.isOpen() && !serverStopping) {
        int num;
        try {
            num = m_acceptSelector.select();
        } catch (IOException e) {
            if (log.isWarnEnabled())
                log.warn("Select operation on listening socket failed", e);
            continue;
        }
        if (num > 0) {
            Set<SelectionKey> readyKeys = m_acceptSelector.selectedKeys();
            for (Iterator<SelectionKey> i = readyKeys.iterator(); i.hasNext(); ) {
                SelectionKey key = i.next();
                i.remove();
                // We only deal with new incoming connections 
                ServerSocketChannel readyChannel = (ServerSocketChannel) key.channel();
                SocketChannel client_sock_ch;
                try {
                    client_sock_ch = readyChannel.accept();
                } catch (IOException e) {
                    if (log.isWarnEnabled())
                        log.warn("Attempt to accept new connection from listening socket failed", e);
                    // Give up this connection 
                    continue;
                }
                if (log.isTraceEnabled())
                    log.trace("accepted connection, client_sock=" + client_sock_ch.socket());
                try {
                    client_sock_ch.socket().setSendBufferSize(send_buf_size);
                } catch (IllegalArgumentException ex) {
                    if (log.isErrorEnabled())
                        log.error("exception setting send buffer size to " + send_buf_size + " bytes: ", ex);
                } catch (SocketException e) {
                    if (log.isErrorEnabled())
                        log.error("exception setting send buffer size to " + send_buf_size + " bytes: ", e);
                }
                try {
                    client_sock_ch.socket().setReceiveBufferSize(recv_buf_size);
                } catch (IllegalArgumentException ex) {
                    if (log.isErrorEnabled())
                        log.error("exception setting receive buffer size to " + send_buf_size + " bytes: ", ex);
                } catch (SocketException e) {
                    if (log.isErrorEnabled())
                        log.error("exception setting receive buffer size to " + recv_buf_size + " bytes: ", e);
                }
                conn = new Connection(client_sock_ch, null);
                try {
                    Address peer_addr = conn.readPeerAddress(client_sock_ch.socket());
                    conn.peer_addr = peer_addr;
                    synchronized (conns) {
                        Connection tmp = (Connection) conns.get(peer_addr);
                        if (tmp != null) {
                            if (peer_addr.compareTo(local_addr) > 0) {
                                if (log.isTraceEnabled())
                                    log.trace("peer's address (" + peer_addr + ") is greater than our local address (" + local_addr + "), replacing our existing connection");
                                // peer's address is greater, add peer's connection to ConnectionTable, destroy existing connection 
                                addConnection(peer_addr, conn);
                                tmp.destroy();
                                notifyConnectionOpened(peer_addr);
                            } else {
                                if (log.isTraceEnabled())
                                    log.trace("peer's address (" + peer_addr + ") is smaller than our local address (" + local_addr + "), rejecting peer connection request");
                                conn.destroy();
                                continue;
                            }
                        } else {
                            addConnection(peer_addr, conn);
                        }
                    }
                    notifyConnectionOpened(peer_addr);
                    client_sock_ch.configureBlocking(false);
                } catch (IOException e) {
                    if (log.isWarnEnabled())
                        log.warn("Attempt to configure non-blocking mode failed", e);
                    conn.destroy();
                    continue;
                } catch (Exception e) {
                    if (log.isWarnEnabled())
                        log.warn("Attempt to handshake with other peer failed", e);
                    conn.destroy();
                    continue;
                }
                int idx;
                synchronized (m_lockNextWriteHandler) {
                    idx = m_nextWriteHandler = (m_nextWriteHandler + 1) % m_writeHandlers.length;
                }
                conn.setupWriteHandler(m_writeHandlers[idx]);
                try {
                    synchronized (m_lockNextReadHandler) {
                        idx = m_nextReadHandler = (m_nextReadHandler + 1) % m_readHandlers.length;
                    }
                    m_readHandlers[idx].add(conn);
                } catch (InterruptedException e) {
                    if (log.isWarnEnabled())
                        log.warn("Attempt to configure read handler for accepted connection failed", e);
                    // close connection 
                    conn.destroy();
                }
            }
        }
    }
    // end of thread 
    if (m_serverSocketChannel.isOpen()) {
        try {
            m_serverSocketChannel.close();
        } catch (Exception e) {
            log.error("exception closing server listening socket", e);
        }
    }
    if (log.isTraceEnabled())
        log.trace("acceptor thread terminated");
}
