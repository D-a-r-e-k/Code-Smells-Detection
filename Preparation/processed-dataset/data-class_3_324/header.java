void method0() { 
/* -----------------------------------------    Properties     -------------------------------------------------- */
@Property(description = "Max number of bytes to send per receiver until an ack must be received to proceed. Default is 2000000 bytes")
private long max_credits = 2000000;
@Property(description = "Max time (in milliseconds) to block. Default is 5000 msec")
private long max_block_time = 5000;
private Long MAX_CREDITS;
private static final Long ZERO_CREDITS = new Long(0);
/** Current number of credits available to send */
@GuardedBy("lock")
private long curr_credits_available;
/** Map which keeps track of bytes received from senders */
@GuardedBy("received_lock")
private final Map<Address, Long> received = new HashMap<Address, Long>(12);
/** Set of members which have requested credits but from whom we have not yet received max_credits bytes */
@GuardedBy("received_lock")
private final Set<Address> pending_requesters = new HashSet<Address>();
/** Set of members from whom we haven't yet received credits */
@GuardedBy("lock")
private final Set<Address> pending_creditors = new HashSet<Address>();
private final Lock lock = new ReentrantLock();
/** Lock protecting access to received and pending_requesters */
private final Lock received_lock = new ReentrantLock();
/** Used to wait for and signal when credits become available again */
private final Condition credits_available = lock.newCondition();
/** Last time a thread woke up from blocking and had to request credit */
private long last_blocked_request = 0L;
private final List<Address> members = new LinkedList<Address>();
private boolean running = true;
private boolean frag_size_received = false;
@GuardedBy("lock")
long start, stop;
// ---------------------- Management information ----------------------- 
long num_blockings = 0;
long num_bytes_sent = 0;
long num_credit_requests_sent = 0;
long num_credit_requests_received = 0;
long num_replenishments_received = 0;
long num_replenishments_sent = 0;
long total_block_time = 0;
final BoundedList<Long> blockings = new BoundedList<Long>(50);
}
