void method0() { 
private ServerSocketChannel m_serverSocketChannel;
private Selector m_acceptSelector;
private WriteHandler[] m_writeHandlers;
private int m_nextWriteHandler = 0;
private final Object m_lockNextWriteHandler = new Object();
private ReadHandler[] m_readHandlers;
private int m_nextReadHandler = 0;
private final Object m_lockNextReadHandler = new Object();
// thread pool for processing read requests 
private Executor m_requestProcessors;
private volatile boolean serverStopping = false;
private final List<Thread> m_backGroundThreads = new LinkedList<Thread>();
// Collection of all created threads 
private int m_reader_threads = 3;
private int m_writer_threads = 3;
private int m_processor_threads = 5;
// PooledExecutor.createThreads() 
private int m_processor_minThreads = 5;
// PooledExecutor.setMinimumPoolSize() 
private int m_processor_maxThreads = 5;
// PooledExecutor.setMaxThreads() 
private int m_processor_queueSize = 100;
// Number of queued requests that can be pending waiting 
// for a background thread to run the request. 
private long m_processor_keepAliveTime = Long.MAX_VALUE;
private static final NullCallable NULLCALL = new NullCallable();
}
