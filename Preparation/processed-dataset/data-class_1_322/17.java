protected void init() throws Exception {
    // use directExector if max thread pool size is less than or equal to zero. 
    if (getProcessorMaxThreads() <= 0) {
        m_requestProcessors = new Executor() {

            public void execute(Runnable command) {
                command.run();
            }
        };
    } else {
        // Create worker thread pool for processing incoming buffers 
        ThreadPoolExecutor requestProcessors = new ThreadPoolExecutor(getProcessorMinThreads(), getProcessorMaxThreads(), getProcessorKeepAliveTime(), TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(getProcessorQueueSize()));
        requestProcessors.setThreadFactory(new ThreadFactory() {

            public Thread newThread(Runnable runnable) {
                Thread new_thread = new Thread(thread_group, runnable);
                new_thread.setDaemon(true);
                new_thread.setName("ConnectionTableNIO.Thread");
                m_backGroundThreads.add(new_thread);
                return new_thread;
            }
        });
        requestProcessors.setRejectedExecutionHandler(new ShutdownRejectedExecutionHandler(requestProcessors.getRejectedExecutionHandler()));
        m_requestProcessors = requestProcessors;
    }
    m_writeHandlers = WriteHandler.create(getThreadFactory(), getWriterThreads(), thread_group, m_backGroundThreads, log);
    m_readHandlers = ReadHandler.create(getThreadFactory(), getReaderThreads(), this, thread_group, m_backGroundThreads, log);
}
