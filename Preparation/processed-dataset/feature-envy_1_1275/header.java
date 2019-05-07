void method0() { 
/**
     * The default initial number of table slots for this table (32).
     * Used when not otherwise specified in constructor.
     **/
public static final int DEFAULT_INITIAL_CAPACITY = 32;
/**
     * The minimum capacity.
     * Used if a lower value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two.
     */
private static final int MINIMUM_CAPACITY = 4;
/**
     * The maximum capacity.
     * Used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
private static final int MAXIMUM_CAPACITY = 1 << 30;
/**
     * The default load factor for this table.
     * Used when not otherwise specified in constructor, the default is 0.75f.
     **/
public static final float DEFAULT_LOAD_FACTOR = 0.75f;
//OpenSymphony BEGIN (pretty long!)  
protected static final String NULL = "_nul!~";
private static final Log log = LogFactory.getLog(AbstractConcurrentReadCache.class);
/*
      The basic strategy is an optimistic-style scheme based on
      the guarantee that the hash table and its lists are always
      kept in a consistent enough state to be read without locking:

      * Read operations first proceed without locking, by traversing the
         apparently correct list of the apparently correct bin. If an
         entry is found, but not invalidated (value field null), it is
         returned. If not found, operations must recheck (after a memory
         barrier) to make sure they are using both the right list and
         the right table (which can change under resizes). If
         invalidated, reads must acquire main update lock to wait out
         the update, and then re-traverse.

      * All list additions are at the front of each bin, making it easy
         to check changes, and also fast to traverse.  Entry next
         pointers are never assigned. Remove() builds new nodes when
         necessary to preserve this.

      * Remove() (also clear()) invalidates removed nodes to alert read
         operations that they must wait out the full modifications.

    */
/**
     * Lock used only for its memory effects. We use a Boolean
     * because it is serializable, and we create a new one because
     * we need a unique object for each cache instance.
     **/
protected final Boolean barrierLock = new Boolean(true);
/**
     * field written to only to guarantee lock ordering.
     **/
protected transient Object lastWrite;
/**
     * The hash table data.
     */
protected transient Entry[] table;
/**
     * The total number of mappings in the hash table.
     */
protected transient int count;
/**
     * Persistence listener.
     */
protected transient PersistenceListener persistenceListener = null;
/**
     * Use memory cache or not.
     */
protected boolean memoryCaching = true;
/**
     * Use unlimited disk caching.
     */
protected boolean unlimitedDiskCache = false;
/**
     * The load factor for the hash table.
     *
     * @serial
     */
protected float loadFactor;
/**
     * Default cache capacity (number of entries).
     */
protected final int DEFAULT_MAX_ENTRIES = 100;
/**
     * Max number of element in cache when considered unlimited.
     */
protected final int UNLIMITED = 2147483646;
protected transient Collection values = null;
/**
     * A HashMap containing the group information.
     * Each entry uses the group name as the key, and holds a
     * <code>Set</code> of containing keys of all
     * the cache entries that belong to that particular group.
     */
protected HashMap groups = new HashMap();
protected transient Set entrySet = null;
// Views  
protected transient Set keySet = null;
/**
     * Cache capacity (number of entries).
     */
protected int maxEntries = DEFAULT_MAX_ENTRIES;
/**
     * The table is rehashed when its size exceeds this threshold.
     * (The value of this field is always (int)(capacity * loadFactor).)
     *
     * @serial
     */
protected int threshold;
/**
     * Use overflow persistence caching.
     */
private boolean overflowPersistence = false;
}
