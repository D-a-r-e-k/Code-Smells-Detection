protected void runRequest(Address addr, ByteBuffer buf) throws InterruptedException {
    m_requestProcessors.execute(new ExecuteTask(addr, buf));
}
