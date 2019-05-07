void method0() { 
/**
     * IMPLEMENTATION NOTES:
     * 
     * An object is considered persistent only if it was queried or created
     * within the context of this transaction. An object is not persistent if it
     * was queried or created by another transactions. An object is not
     * persistent if it was queried with read-only access.
     * 
     * A read lock is implicitly obtained on any object that is queried, and a
     * write lock on any object that is queried in exclusive mode, created or
     * deleted in this transaction. The lock can be upgraded to a write lock.
     * 
     * The validity of locks is dependent on the underlying persistence engine
     * the transaction mode. Without persistence engine locks provide a strong
     * locking mechanism, preventing objects from being deleted or modified in
     * one transaction while another transaction is looking at them. With a
     * persistent engine in exclusive mode, locks exhibit the same behavior.
     * With a persistent engine in read/write mode or a persistent engine that
     * does not support locking (e.g. LDAP) an object may be deleted or modified
     * concurrently through a direct access mechanism.
     */
/** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
private static final Log LOG = LogFactory.getLog(AbstractTransactionContext.class);
/** Default timeout of transaction and waiting for a lock. */
private static final int DEFAULT_TIMEOUT = 30;
/** The ObjectTracker for this transaction, tracking all of the various bits
     *  of state and co-located information (Engine, OID, etc.) for this entry.
     *  See ObjectTracker javadocs for more details. */
private final ObjectTracker _tracker = new ObjectTracker();
/** Set while transaction is waiting for a lock. */
private ObjectLock _waitOnLock;
/** The transaction status. See {@link Status}for list of valid values. */
private int _status = -1;
/** The timeout waiting to acquire a new lock. Specified in seconds. */
private int _lockTimeout = DEFAULT_TIMEOUT;
/** The timeout of this transaction. Specified in seconds. */
private int _txTimeout = DEFAULT_TIMEOUT;
/** The database to which this transaction belongs. */
private Database _db;
/** True if user prefer all reachable object to be stored automatically.
     *  False if user want only dependent object to be stored. */
private boolean _autoStore;
/** The default callback interceptor for the data object in this transaction. */
private CallbackInterceptor _callback;
/** The instance factory to that creates new instances of data object. */
private InstanceFactory _instanceFactory;
/** A list of listeners which will be informed about various transaction states. */
private ArrayList<TxSynchronizable> _synchronizeList = new ArrayList<TxSynchronizable>();
/** Lists all the connections opened for particular database engines
     *  used in the lifetime of this transaction. The database engine
     *  is used as the key to an open/transactional connection. */
private Hashtable<LockEngine, Connection> _conns = new Hashtable<LockEngine, Connection>();
/** Meta-data related to the RDBMS used. */
private DbMetaInfo _dbInfo;
}
