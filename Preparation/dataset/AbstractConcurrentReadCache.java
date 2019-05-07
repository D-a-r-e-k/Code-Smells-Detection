/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
/*
        File: AbstractConcurrentReadCache

        Written by Doug Lea. Adapted from JDK1.2 HashMap.java and Hashtable.java
        which carries the following copyright:

                 * Copyright 1997 by Sun Microsystems, Inc.,
                 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
                 * All rights reserved.
                 *
                 * This software is the confidential and proprietary information
                 * of Sun Microsystems, Inc. ("Confidential Information").  You
                 * shall not disclose such Confidential Information and shall use
                 * it only in accordance with the terms of the license agreement
                 * you entered into with Sun.

        This class is a modified version of ConcurrentReaderHashMap, which was written
        by Doug Lea (http://gee.cs.oswego.edu/dl/). The modifications where done
        by Pyxis Technologies. This is a base class for the OSCache module of the
        openSymphony project (www.opensymphony.com).

        History:
        Date       Who                What
        28oct1999  dl               Created
        14dec1999  dl               jmm snapshot
        19apr2000  dl               use barrierLock
        12jan2001  dl               public release
        Oct2001    abergevin@pyxis-tech.com
                                                                Integrated persistence and outer algorithm support
*/
package com.opensymphony.oscache.base.algorithm;


/** OpenSymphony BEGIN */
import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.base.persistence.CachePersistenceException;
import com.opensymphony.oscache.base.persistence.PersistenceListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Serializable;

import java.util.*;

/**
 * A version of Hashtable that supports mostly-concurrent reading, but exclusive writing.
 * Because reads are not limited to periods
 * without writes, a concurrent reader policy is weaker than a classic
 * reader/writer policy, but is generally faster and allows more
 * concurrency. This class is a good choice especially for tables that
 * are mainly created by one thread during the start-up phase of a
 * program, and from then on, are mainly read (with perhaps occasional
 * additions or removals) in many threads.  If you also need concurrency
 * among writes, consider instead using ConcurrentHashMap.
 * <p>
 *
 * Successful retrievals using get(key) and containsKey(key) usually
 * run without locking. Unsuccessful ones (i.e., when the key is not
 * present) do involve brief synchronization (locking).  Also, the
 * size and isEmpty methods are always synchronized.
 *
 * <p> Because retrieval operations can ordinarily overlap with
 * writing operations (i.e., put, remove, and their derivatives),
 * retrievals can only be guaranteed to return the results of the most
 * recently <em>completed</em> operations holding upon their
 * onset. Retrieval operations may or may not return results
 * reflecting in-progress writing operations.  However, the retrieval
 * operations do always return consistent results -- either those
 * holding before any single modification or after it, but never a
 * nonsense result.  For aggregate operations such as putAll and
 * clear, concurrent reads may reflect insertion or removal of only
 * some entries. In those rare contexts in which you use a hash table
 * to synchronize operations across threads (for example, to prevent
 * reads until after clears), you should either encase operations
 * in synchronized blocks, or instead use java.util.Hashtable.
 *
 * <p>
 *
 * This class also supports optional guaranteed
 * exclusive reads, simply by surrounding a call within a synchronized
 * block, as in <br>
 * <code>AbstractConcurrentReadCache t; ... Object v; <br>
 * synchronized(t) { v = t.get(k); } </code> <br>
 *
 * But this is not usually necessary in practice. For
 * example, it is generally inefficient to write:
 *
 * <pre>
 *   AbstractConcurrentReadCache t; ...            // Inefficient version
 *   Object key; ...
 *   Object value; ...
 *   synchronized(t) {
 *     if (!t.containsKey(key))
 *       t.put(key, value);
 *       // other code if not previously present
 *     }
 *     else {
 *       // other code if it was previously present
 *     }
 *   }
 *</pre>
 * Instead, just take advantage of the fact that put returns
 * null if the key was not previously present:
 * <pre>
 *   AbstractConcurrentReadCache t; ...                // Use this instead
 *   Object key; ...
 *   Object value; ...
 *   Object oldValue = t.put(key, value);
 *   if (oldValue == null) {
 *     // other code if not previously present
 *   }
 *   else {
 *     // other code if it was previously present
 *   }
 *</pre>
 * <p>
 *
 * Iterators and Enumerations (i.e., those returned by
 * keySet().iterator(), entrySet().iterator(), values().iterator(),
 * keys(), and elements()) return elements reflecting the state of the
 * hash table at some point at or since the creation of the
 * iterator/enumeration.  They will return at most one instance of
 * each element (via next()/nextElement()), but might or might not
 * reflect puts and removes that have been processed since they were
 * created.  They do <em>not</em> throw ConcurrentModificationException.
 * However, these iterators are designed to be used by only one
 * thread at a time. Sharing an iterator across multiple threads may
 * lead to unpredictable results if the table is being concurrently
 * modified.  Again, you can ensure interference-free iteration by
 * enclosing the iteration in a synchronized block.  <p>
 *
 * This class may be used as a direct replacement for any use of
 * java.util.Hashtable that does not depend on readers being blocked
 * during updates. Like Hashtable but unlike java.util.HashMap,
 * this class does NOT allow <tt>null</tt> to be used as a key or
 * value.  This class is also typically faster than ConcurrentHashMap
 * when there is usually only one thread updating the table, but
 * possibly many retrieving values from it.
 * <p>
 *
 * Implementation note: A slightly faster implementation of
 * this class will be possible once planned Java Memory Model
 * revisions are in place.
 *
 * <p>[<a href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html"> Introduction to this package. </a>]
 **/
public abstract class AbstractConcurrentReadCache extends AbstractMap implements Map, Cloneable, Serializable {
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

    /**
     * Constructs a new, empty map with the specified initial capacity and the specified load factor.
     *
     * @param initialCapacity the initial capacity
     *  The actual initial capacity is rounded to the nearest power of two.
     * @param loadFactor  the load factor of the AbstractConcurrentReadCache
     * @throws IllegalArgumentException  if the initial maximum number
     *               of elements is less
     *               than zero, or if the load factor is nonpositive.
     */
    public AbstractConcurrentReadCache(int initialCapacity, float loadFactor) {
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("Illegal Load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;

        int cap = p2capacity(initialCapacity);
        table = new Entry[cap];
        threshold = (int) (cap * loadFactor);
    }

    /**
     * Constructs a new, empty map with the specified initial capacity and default load factor.
     *
     * @param   initialCapacity   the initial capacity of the
     *                            AbstractConcurrentReadCache.
     * @throws    IllegalArgumentException if the initial maximum number
     *              of elements is less
     *              than zero.
     */
    public AbstractConcurrentReadCache(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a new, empty map with a default initial capacity and load factor.
     */
    public AbstractConcurrentReadCache() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a new map with the same mappings as the given map.
     * The map is created with a capacity of twice the number of mappings in
     * the given map or 11 (whichever is greater), and a default load factor.
     */
    public AbstractConcurrentReadCache(Map t) {
        this(Math.max(2 * t.size(), 11), DEFAULT_LOAD_FACTOR);
        putAll(t);
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings.
     */
    public synchronized boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns a set of the cache keys that reside in a particular group.
     *
     * @param   groupName The name of the group to retrieve.
     * @return  a set containing all of the keys of cache entries that belong
     * to this group, or <code>null</code> if the group was not found.
     * @exception  NullPointerException if the groupName is <code>null</code>.
     */
    public Set getGroup(String groupName) {
        if (log.isDebugEnabled()) {
            log.debug("getGroup called (group=" + groupName + ")");
        }

        Set groupEntries = null;

        if (memoryCaching && (groups != null)) {
            groupEntries = (Set) getGroupForReading(groupName);
        }

        if (groupEntries == null) {
            // Not in the map, try the persistence layer
            groupEntries = persistRetrieveGroup(groupName);
        }

        return groupEntries;
    }

    /**
     * Set the cache capacity
     */
    public void setMaxEntries(int newLimit) {
        if (newLimit > 0) {
            maxEntries = newLimit;

            synchronized (this) { // because remove() isn't synchronized

                while (size() > maxEntries) {
                    remove(removeItem(), false, false);
                }
            }
        } else {
            // Capacity must be at least 1
            throw new IllegalArgumentException("Cache maximum number of entries must be at least 1");
        }
    }

    /**
     * Retrieve the cache capacity (number of entries).
     */
    public int getMaxEntries() {
        return maxEntries;
    }

    /**
     * Sets the memory caching flag.
     */
    public void setMemoryCaching(boolean memoryCaching) {
        this.memoryCaching = memoryCaching;
    }

    /**
     * Check if memory caching is used.
     */
    public boolean isMemoryCaching() {
        return memoryCaching;
    }

    /**
     * Set the persistence listener to use.
     */
    public void setPersistenceListener(PersistenceListener listener) {
        this.persistenceListener = listener;
    }

    /**
     * Get the persistence listener.
     */
    public PersistenceListener getPersistenceListener() {
        return persistenceListener;
    }

    /**
     * Sets the unlimited disk caching flag.
     */
    public void setUnlimitedDiskCache(boolean unlimitedDiskCache) {
        this.unlimitedDiskCache = unlimitedDiskCache;
    }

    /**
     * Check if we use unlimited disk cache.
     */
    public boolean isUnlimitedDiskCache() {
        return unlimitedDiskCache;
    }

    /**
     * Check if we use overflowPersistence
     *
     * @return Returns the overflowPersistence.
     */
    public boolean isOverflowPersistence() {
        return this.overflowPersistence;
    }

    /**
     * Sets the overflowPersistence flag
     *
     * @param overflowPersistence The overflowPersistence to set.
     */
    public void setOverflowPersistence(boolean overflowPersistence) {
        this.overflowPersistence = overflowPersistence;
    }

    /**
     * Return the number of slots in this table.
     **/
    public synchronized int capacity() {
        return table.length;
    }

    /**
     * Removes all mappings from this map.
     */
    public synchronized void clear() {
        Entry[] tab = table;

        for (int i = 0; i < tab.length; ++i) {
            // must invalidate all to force concurrent get's to wait and then retry
            for (Entry e = tab[i]; e != null; e = e.next) {
                e.value = null;

                /** OpenSymphony BEGIN */
                itemRemoved(e.key);

                /** OpenSymphony END */
            }

            tab[i] = null;
        }

        // Clean out the entire disk cache
        persistClear();

        count = 0;
        recordModification(tab);
    }

    /**
     * Returns a shallow copy of this.
     * <tt>AbstractConcurrentReadCache</tt> instance: the keys and
     * values themselves are not cloned.
     *
     * @return a shallow copy of this map.
     */
    public synchronized Object clone() {
        try {
            AbstractConcurrentReadCache t = (AbstractConcurrentReadCache) super.clone();
            t.keySet = null;
            t.entrySet = null;
            t.values = null;

            Entry[] tab = table;
            t.table = new Entry[tab.length];

            Entry[] ttab = t.table;

            for (int i = 0; i < tab.length; ++i) {
                Entry first = tab[i];

                if (first != null) {
                    ttab[i] = (Entry) (first.clone());
                }
            }

            return t;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * Tests if some key maps into the specified value in this table.
     * This operation is more expensive than the <code>containsKey</code>
     * method.<p>
     *
     * Note that this method is identical in functionality to containsValue,
     * (which is part of the Map interface in the collections framework).
     *
     * @param      value   a value to search for.
     * @return     <code>true</code> if and only if some key maps to the
     *             <code>value</code> argument in this table as
     *             determined by the <tt>equals</tt> method;
     *             <code>false</code> otherwise.
     * @exception  NullPointerException  if the value is <code>null</code>.
     * @see        #containsKey(Object)
     * @see        #containsValue(Object)
     * @see           Map
     */
    public boolean contains(Object value) {
        return containsValue(value);
    }

    /**
     * Tests if the specified object is a key in this table.
     *
     * @param   key   possible key.
     * @return  <code>true</code> if and only if the specified object
     *          is a key in this table, as determined by the
     *          <tt>equals</tt> method; <code>false</code> otherwise.
     * @exception  NullPointerException  if the key is
     *               <code>null</code>.
     * @see     #contains(Object)
     */
    public boolean containsKey(Object key) {
        return get(key) != null;

        /** OpenSymphony BEGIN */

        // TODO: Also check the persistence?

        /** OpenSymphony END */
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value. Note: This method requires a full internal
     * traversal of the hash table, and so is much slower than
     * method <tt>containsKey</tt>.
     *
     * @param value value whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map maps one or more keys to the
     * specified value.
     * @exception  NullPointerException  if the value is <code>null</code>.
     */
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }

        Entry[] tab = getTableForReading();

        for (int i = 0; i < tab.length; ++i) {
            for (Entry e = tab[i]; e != null; e = e.next) {
                Object v = e.value;

                if ((v != null) && value.equals(v)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns an enumeration of the values in this table.
     * Use the Enumeration methods on the returned object to fetch the elements
     * sequentially.
     *
     * @return  an enumeration of the values in this table.
     * @see     java.util.Enumeration
     * @see     #keys()
     * @see        #values()
     * @see        Map
     */
    public Enumeration elements() {
        return new ValueIterator();
    }

    /**
     * Returns a collection view of the mappings contained in this map.
     * Each element in the returned collection is a <tt>Map.Entry</tt>.  The
     * collection is backed by the map, so changes to the map are reflected in
     * the collection, and vice-versa.  The collection supports element
     * removal, which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations.
     * It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the mappings contained in this map.
     */
    public Set entrySet() {
        Set es = entrySet;

        if (es != null) {
            return es;
        } else {
            return entrySet = new AbstractSet() {
                        public Iterator iterator() {
                            return new HashIterator();
                        }

                        public boolean contains(Object o) {
                            if (!(o instanceof Map.Entry)) {
                                return false;
                            }

                            Map.Entry entry = (Map.Entry) o;
                            Object key = entry.getKey();
                            Object v = AbstractConcurrentReadCache.this.get(key);

                            return (v != null) && v.equals(entry.getValue());
                        }

                        public boolean remove(Object o) {
                            if (!(o instanceof Map.Entry)) {
                                return false;
                            }

                            return AbstractConcurrentReadCache.this.findAndRemoveEntry((Map.Entry) o);
                        }

                        public int size() {
                            return AbstractConcurrentReadCache.this.size();
                        }

                        public void clear() {
                            AbstractConcurrentReadCache.this.clear();
                        }
                    };
        }
    }

    /**
     * Returns the value to which the specified key is mapped in this table.
     *
     * @param   key   a key in the table.
     * @return  the value to which the key is mapped in this table;
     *          <code>null</code> if the key is not mapped to any value in
     *          this table.
     * @exception  NullPointerException  if the key is
     *               <code>null</code>.
     * @see     #put(Object, Object)
     */
    public Object get(Object key) {
        if (log.isDebugEnabled()) {
            log.debug("get called (key=" + key + ")");
        }

        // throw null pointer exception if key null
        int hash = hash(key);

        /*
           Start off at the apparently correct bin.  If entry is found, we
           need to check after a barrier anyway.  If not found, we need a
           barrier to check if we are actually in right bin. So either
           way, we encounter only one barrier unless we need to retry.
           And we only need to fully synchronize if there have been
           concurrent modifications.
        */
        Entry[] tab = table;
        int index = hash & (tab.length - 1);
        Entry first = tab[index];
        Entry e = first;

        for (;;) {
            if (e == null) {
                // If key apparently not there, check to
                // make sure this was a valid read
                tab = getTableForReading();

                if (first == tab[index]) {
                    /** OpenSymphony BEGIN */

                    /* Previous code
                    return null;*/

                    // Not in the table, try persistence
                    Object value = persistRetrieve(key);

                    if (value != null) {
                        // Update the map, but don't persist the data
                        put(key, value, false);
                    }

                    return value;

                    /** OpenSymphony END */
                } else {
                    // Wrong list -- must restart traversal at new first
                    e = first = tab[index = hash & (tab.length - 1)];
                }
            }
            // checking for pointer equality first wins in most applications
            else if ((key == e.key) || ((e.hash == hash) && key.equals(e.key))) {
                Object value = e.value;

                if (value != null) {
                    /** OpenSymphony BEGIN */

                    /* Previous code
                    return value;*/
                    if (NULL.equals(value)) {
                        // Memory cache disable, use disk
                        value = persistRetrieve(e.key);

                        if (value != null) {
                            itemRetrieved(key);
                        }

                        return value; // fix [CACHE-13]
                    } else {
                        itemRetrieved(key);

                        return value;
                    }

                    /** OpenSymphony END */
                }

                // Entry was invalidated during deletion. But it could
                // have been re-inserted, so we must retraverse.
                // To avoid useless contention, get lock to wait out modifications
                // before retraversing.
                synchronized (this) {
                    tab = table;
                }

                e = first = tab[index = hash & (tab.length - 1)];
            } else {
                e = e.next;
            }
        }
    }

    /**
     * Returns a set view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are reflected in the set, and
     * vice-versa.  The set supports element removal, which removes the
     * corresponding mapping from this map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and
     * <tt>clear</tt> operations.  It does not support the <tt>add</tt> or
     * <tt>addAll</tt> operations.
     *
     * @return a set view of the keys contained in this map.
     */
    public Set keySet() {
        Set ks = keySet;

        if (ks != null) {
            return ks;
        } else {
            return keySet = new AbstractSet() {
                        public Iterator iterator() {
                            return new KeyIterator();
                        }

                        public int size() {
                            return AbstractConcurrentReadCache.this.size();
                        }

                        public boolean contains(Object o) {
                            return AbstractConcurrentReadCache.this.containsKey(o);
                        }

                        public boolean remove(Object o) {
                            return AbstractConcurrentReadCache.this.remove(o) != null;
                        }

                        public void clear() {
                            AbstractConcurrentReadCache.this.clear();
                        }
                    };
        }
    }

    /**
     * Returns an enumeration of the keys in this table.
     *
     * @return  an enumeration of the keys in this table.
     * @see     Enumeration
     * @see     #elements()
     * @see        #keySet()
     * @see        Map
     */
    public Enumeration keys() {
        return new KeyIterator();
    }

    /**
     * Return the load factor
     **/
    public float loadFactor() {
        return loadFactor;
    }

    /**
     * Maps the specified <code>key</code> to the specified <code>value</code> in this table.
     * Neither the key nor the
     * value can be <code>null</code>. <p>
     *
     * The value can be retrieved by calling the <code>get</code> method
     * with a key that is equal to the original key.
     *
     * @param      key     the table key.
     * @param      value   the value.
     * @return     the previous value of the specified key in this table,
     *             or <code>null</code> if it did not have one.
     * @exception  NullPointerException  if the key or value is
     *               <code>null</code>.
     * @see     Object#equals(Object)
     * @see     #get(Object)
     */
    /** OpenSymphony BEGIN */
    public Object put(Object key, Object value) {
        // Call the internal put using persistance
        return put(key, value, true);
    }

    /**
     * Copies all of the mappings from the specified map to this one.
     *
     * These mappings replace any mappings that this map had for any of the
     * keys currently in the specified Map.
     *
     * @param t Mappings to be stored in this map.
     */
    public synchronized void putAll(Map t) {
        for (Iterator it = t.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            put(key, value);
        }
    }

    /**
     * Removes the key (and its corresponding value) from this table.
     * This method does nothing if the key is not in the table.
     *
     * @param   key   the key that needs to be removed.
     * @return  the value to which the key had been mapped in this table,
     *          or <code>null</code> if the key did not have a mapping.
     */
    /** OpenSymphony BEGIN */
    public Object remove(Object key) {
        return remove(key, true, false);
    }

    /**
     * Like <code>remove(Object)</code>, but ensures that the entry will be removed from the persistent store, too,
     * even if overflowPersistence or unlimitedDiskcache are true.
     *
     * @param   key   the key that needs to be removed.
     * @return  the value to which the key had been mapped in this table,
     *          or <code>null</code> if the key did not have a mapping.
     */
    public Object removeForce(Object key) {
      return remove(key, true, true);
    }

    /**
     * Returns the total number of cache entries held in this map.
     *
     * @return the number of key-value mappings in this map.
     */
    public synchronized int size() {
        return count;
    }

    /**
     * Returns a collection view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are reflected in
     * the collection, and vice-versa.  The collection supports element
     * removal, which removes the corresponding mapping from this map, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations.
     * It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map.
     */
    public Collection values() {
        Collection vs = values;

        if (vs != null) {
            return vs;
        } else {
            return values = new AbstractCollection() {
                        public Iterator iterator() {
                            return new ValueIterator();
                        }

                        public int size() {
                            return AbstractConcurrentReadCache.this.size();
                        }

                        public boolean contains(Object o) {
                            return AbstractConcurrentReadCache.this.containsValue(o);
                        }

                        public void clear() {
                            AbstractConcurrentReadCache.this.clear();
                        }
                    };
        }
    }

    /**
     * Get ref to group.
     * CACHE-127 Synchronized copying of the group entry set since
     * the new HashSet(Collection c) constructor uses the iterator.
     * This may slow things down but it is better than a
     * ConcurrentModificationException.  We might have to revisit the
     * code if performance is too adversely impacted.
     **/
    protected synchronized final Set getGroupForReading(String groupName) {
        Set group = (Set) getGroupsForReading().get(groupName);
        if (group == null) return null;
        return new HashSet(group);
    }

    /**
     * Get ref to groups.
     * The reference and the cells it
     * accesses will be at least as fresh as from last
     * use of barrierLock
     **/
    protected final Map getGroupsForReading() {
        synchronized (barrierLock) {
            return groups;
        }
    }

    /**
     * Get ref to table; the reference and the cells it
     * accesses will be at least as fresh as from last
     * use of barrierLock
     **/
    protected final Entry[] getTableForReading() {
        synchronized (barrierLock) {
            return table;
        }
    }

    /**
     * Force a memory synchronization that will cause
     * all readers to see table. Call only when already
     * holding main synch lock.
     **/
    protected final void recordModification(Object x) {
        synchronized (barrierLock) {
            lastWrite = x;
        }
    }

    /**
     * Helper method for entrySet remove.
     **/
    protected synchronized boolean findAndRemoveEntry(Map.Entry entry) {
        Object key = entry.getKey();
        Object v = get(key);

        if ((v != null) && v.equals(entry.getValue())) {
            remove(key);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove an object from the persistence.
     * @param key The key of the object to remove
     */
    protected void persistRemove(Object key) {
        if (log.isDebugEnabled()) {
            log.debug("PersistRemove called (key=" + key + ")");
        }

        if (persistenceListener != null) {
            try {
                persistenceListener.remove((String) key);
            } catch (CachePersistenceException e) {
                log.error("[oscache] Exception removing cache entry with key '" + key + "' from persistence", e);
            }
        }
    }

    /**
     * Removes a cache group using the persistence listener.
     * @param groupName The name of the group to remove
     */
    protected void persistRemoveGroup(String groupName) {
        if (log.isDebugEnabled()) {
            log.debug("persistRemoveGroup called (groupName=" + groupName + ")");
        }

        if (persistenceListener != null) {
            try {
                persistenceListener.removeGroup(groupName);
            } catch (CachePersistenceException e) {
                log.error("[oscache] Exception removing group " + groupName, e);
            }
        }
    }

    /**
     * Retrieve an object from the persistence listener.
     * @param key The key of the object to retrieve
     */
    protected Object persistRetrieve(Object key) {
        if (log.isDebugEnabled()) {
            log.debug("persistRetrieve called (key=" + key + ")");
        }

        Object entry = null;

        if (persistenceListener != null) {
            try {
                entry = persistenceListener.retrieve((String) key);
            } catch (CachePersistenceException e) {
                /**
                 * It is normal that we get an exception occasionally.
                 * It happens when the item is invalidated (written or removed)
                 * during read. The logic is constructed so that read is retried.
                 */
            }
        }

        return entry;
    }

    /**
     * Retrieves a cache group using the persistence listener.
     * @param groupName The name of the group to retrieve
     */
    protected Set persistRetrieveGroup(String groupName) {
        if (log.isDebugEnabled()) {
            log.debug("persistRetrieveGroup called (groupName=" + groupName + ")");
        }

        if (persistenceListener != null) {
            try {
                return persistenceListener.retrieveGroup(groupName);
            } catch (CachePersistenceException e) {
                log.error("[oscache] Exception retrieving group " + groupName, e);
            }
        }

        return null;
    }

    /**
     * Store an object in the cache using the persistence listener.
     * @param key The object key
     * @param obj The object to store
     */
    protected void persistStore(Object key, Object obj) {
        if (log.isDebugEnabled()) {
            log.debug("persistStore called (key=" + key + ")");
        }

        if (persistenceListener != null) {
            try {
                persistenceListener.store((String) key, obj);
            } catch (CachePersistenceException e) {
                log.error("[oscache] Exception persisting " + key, e);
            }
        }
    }

    /**
     * Creates or Updates a cache group using the persistence listener.
     * @param groupName The name of the group to update
     * @param group The entries for the group
     */
    protected void persistStoreGroup(String groupName, Set group) {
        if (log.isDebugEnabled()) {
            log.debug("persistStoreGroup called (groupName=" + groupName + ")");
        }

        if (persistenceListener != null) {
            try {
                if ((group == null) || group.isEmpty()) {
                    persistenceListener.removeGroup(groupName);
                } else {
                    persistenceListener.storeGroup(groupName, group);
                }
            } catch (CachePersistenceException e) {
                log.error("[oscache] Exception persisting group " + groupName, e);
            }
        }
    }

    /**
     * Removes the entire cache from persistent storage.
     */
    protected void persistClear() {
        if (log.isDebugEnabled()) {
            log.debug("persistClear called");
            ;
        }

        if (persistenceListener != null) {
            try {
                persistenceListener.clear();
            } catch (CachePersistenceException e) {
                log.error("[oscache] Exception clearing persistent cache", e);
            }
        }
    }

    /**
     * Notify the underlying implementation that an item was put in the cache.
     *
     * @param key The cache key of the item that was put.
     */
    protected abstract void itemPut(Object key);

    /**
     * Notify any underlying algorithm that an item has been retrieved from the cache.
     *
     * @param key The cache key of the item that was retrieved.
     */
    protected abstract void itemRetrieved(Object key);

    /**
     * Notify the underlying implementation that an item was removed from the cache.
     *
     * @param key The cache key of the item that was removed.
     */
    protected abstract void itemRemoved(Object key);

    /**
     * The cache has reached its cacpacity and an item needs to be removed.
     * (typically according to an algorithm such as LRU or FIFO).
     *
     * @return The key of whichever item was removed.
     */
    protected abstract Object removeItem();

    /**
     * Reconstitute the <tt>AbstractConcurrentReadCache</tt>.
     * instance from a stream (i.e.,
     * deserialize it).
     */
    private synchronized void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        // Read in the threshold, loadfactor, and any hidden stuff
        s.defaultReadObject();

        // Read in number of buckets and allocate the bucket array;
        int numBuckets = s.readInt();
        table = new Entry[numBuckets];

        // Read in size (number of Mappings)
        int size = s.readInt();

        // Read the keys and values, and put the mappings in the table
        for (int i = 0; i < size; i++) {
            Object key = s.readObject();
            Object value = s.readObject();
            put(key, value);
        }
    }

    /**
     * Rehashes the contents of this map into a new table with a larger capacity.
     * This method is called automatically when the
     * number of keys in this map exceeds its capacity and load factor.
     */
    protected void rehash() {
        Entry[] oldMap = table;
        int oldCapacity = oldMap.length;

        if (oldCapacity >= MAXIMUM_CAPACITY) {
            return;
        }

        int newCapacity = oldCapacity << 1;
        Entry[] newMap = new Entry[newCapacity];
        threshold = (int) (newCapacity * loadFactor);

        /*
          We need to guarantee that any existing reads of oldMap can
          proceed. So we cannot yet null out each oldMap bin.

          Because we are using power-of-two expansion, the elements
          from each bin must either stay at same index, or move
          to oldCapacity+index. We also minimize new node creation by
          catching cases where old nodes can be reused because their
          .next fields won't change. (This is checked only for sequences
          of one and two. It is not worth checking longer ones.)
        */
        for (int i = 0; i < oldCapacity; ++i) {
            Entry l = null;
            Entry h = null;
            Entry e = oldMap[i];

            while (e != null) {
                int hash = e.hash;
                Entry next = e.next;

                if ((hash & oldCapacity) == 0) {
                    // stays at newMap[i]
                    if (l == null) {
                        // try to reuse node
                        if ((next == null) || ((next.next == null) && ((next.hash & oldCapacity) == 0))) {
                            l = e;

                            break;
                        }
                    }

                    l = new Entry(hash, e.key, e.value, l);
                } else {
                    // moves to newMap[oldCapacity+i]
                    if (h == null) {
                        if ((next == null) || ((next.next == null) && ((next.hash & oldCapacity) != 0))) {
                            h = e;

                            break;
                        }
                    }

                    h = new Entry(hash, e.key, e.value, h);
                }

                e = next;
            }

            newMap[i] = l;
            newMap[oldCapacity + i] = h;
        }

        table = newMap;
        recordModification(newMap);
    }

    /**
     * Continuation of put(), called only when synch lock is
     * held and interference has been detected.
     **/
    /** OpenSymphony BEGIN */

    /* Previous code
    protected Object sput(Object key, Object value, int hash) {*/
    protected Object sput(Object key, Object value, int hash, boolean persist) {
        /** OpenSymphony END */
        Entry[] tab = table;
        int index = hash & (tab.length - 1);
        Entry first = tab[index];
        Entry e = first;

        for (;;) {
            if (e == null) {
                /** OpenSymphony BEGIN */

                // Previous code
                //  		Entry newEntry = new Entry(hash, key, value, first);
                Entry newEntry;

                if (memoryCaching) {
                    newEntry = new Entry(hash, key, value, first);
                } else {
                    newEntry = new Entry(hash, key, NULL, first);
                }

                itemPut(key);

                // Persist if required
                if (persist && !overflowPersistence) {
                    persistStore(key, value);
                }

                // If we have a CacheEntry, update the group lookups
                if (value instanceof CacheEntry) {
                    updateGroups(null, (CacheEntry) value, persist);
                }

                /**        OpenSymphony END */
                tab[index] = newEntry;

                if (++count >= threshold) {
                    rehash();
                } else {
                    recordModification(newEntry);
                }

                return null;
            } else if ((key == e.key) || ((e.hash == hash) && key.equals(e.key))) {
                Object oldValue = e.value;

                /** OpenSymphony BEGIN */

                /* Previous code
                e.value = value; */
                if (memoryCaching) {
                    e.value = value;
                }

                // Persist if required
                if (persist && overflowPersistence) {
                    persistRemove(key);
                } else if (persist) {
                    persistStore(key, value);
                }

                updateGroups(oldValue, value, persist);

                itemPut(key);

                /** OpenSymphony END */
                return oldValue;
            } else {
                e = e.next;
            }
        }
    }

    /**
     * Continuation of remove(), called only when synch lock is
     * held and interference has been detected.
     **/
    /** OpenSymphony BEGIN */

    /* Previous code
    protected Object sremove(Object key, int hash) { */
    protected Object sremove(Object key, int hash, boolean invokeAlgorithm) {
        /** OpenSymphony END */
        Entry[] tab = table;
        int index = hash & (tab.length - 1);
        Entry first = tab[index];
        Entry e = first;

        for (;;) {
            if (e == null) {
                return null;
            } else if ((key == e.key) || ((e.hash == hash) && key.equals(e.key))) {
                Object oldValue = e.value;
                if (persistenceListener != null && (oldValue == NULL)) {
                  oldValue = persistRetrieve(key);
                }

                e.value = null;
                count--;

                /** OpenSymphony BEGIN */
                if (!unlimitedDiskCache && !overflowPersistence) {
                    persistRemove(e.key);
                    // If we have a CacheEntry, update the groups
                    if (oldValue instanceof CacheEntry) {
                      CacheEntry oldEntry = (CacheEntry)oldValue;
                      removeGroupMappings(oldEntry.getKey(),
                          oldEntry.getGroups(), true);
                }
                } else {
                  // only remove from memory groups
                  if (oldValue instanceof CacheEntry) {
                    CacheEntry oldEntry = (CacheEntry)oldValue;
                    removeGroupMappings(oldEntry.getKey(),
                        oldEntry.getGroups(), false);
                  }
                }

                if (overflowPersistence && ((size() + 1) >= maxEntries)) {
                    persistStore(key, oldValue);
                    // add key to persistent groups but NOT to the memory groups
                    if (oldValue instanceof CacheEntry) {
                      CacheEntry oldEntry = (CacheEntry)oldValue;
                      addGroupMappings(oldEntry.getKey(), oldEntry.getGroups(), true, false);
                    }
                }

                if (invokeAlgorithm) {
                    itemRemoved(key);
                }

                /** OpenSymphony END */
                Entry head = e.next;

                for (Entry p = first; p != e; p = p.next) {
                    head = new Entry(p.hash, p.key, p.value, head);
                }

                tab[index] = head;
                recordModification(head);

                return oldValue;
            } else {
                e = e.next;
            }
        }
    }

    /**
     * Save the state of the <tt>AbstractConcurrentReadCache</tt> instance to a stream.
     * (i.e., serialize it).
     *
     * @serialData The <i>capacity</i> of the
     * AbstractConcurrentReadCache (the length of the
     * bucket array) is emitted (int), followed  by the
     * <i>size</i> of the AbstractConcurrentReadCache (the number of key-value
     * mappings), followed by the key (Object) and value (Object)
     * for each key-value mapping represented by the AbstractConcurrentReadCache
     * The key-value mappings are emitted in no particular order.
     */
    private synchronized void writeObject(java.io.ObjectOutputStream s) throws IOException {
        // Write out the threshold, loadfactor, and any hidden stuff
        s.defaultWriteObject();

        // Write out number of buckets
        s.writeInt(table.length);

        // Write out size (number of Mappings)
        s.writeInt(count);

        // Write out keys and values (alternating)
        for (int index = table.length - 1; index >= 0; index--) {
            Entry entry = table[index];

            while (entry != null) {
                s.writeObject(entry.key);
                s.writeObject(entry.value);
                entry = entry.next;
            }
        }
    }

    /**
     * Return hash code for Object x.
     * Since we are using power-of-two
     * tables, it is worth the effort to improve hashcode via
     * the same multiplicative scheme as used in IdentityHashMap.
     */
    private static int hash(Object x) {
        int h = x.hashCode();

        // Multiply by 127 (quickly, via shifts), and mix in some high
        // bits to help guard against bunching of codes that are
        // consecutive or equally spaced.
        return ((h << 7) - h + (h >>> 9) + (h >>> 17));
    }

    /**
     * Add this cache key to the groups specified groups.
     * We have to treat the
     * memory and disk group mappings seperately so they remain valid for their
     * corresponding memory/disk caches. (eg if mem is limited to 100 entries
     * and disk is unlimited, the group mappings will be different).
     *
     * @param key The cache key that we are ading to the groups.
     * @param newGroups the set of groups we want to add this cache entry to.
     * @param persist A flag to indicate whether the keys should be added to
     * the persistent cache layer.
     * @param memory A flag to indicate whether the key should be added to
     * the memory groups (important for overflow-to-disk)
     */
    private void addGroupMappings(String key, Set newGroups, boolean persist, boolean memory) {
        if (newGroups == null) {
            return;
        }
        
        // Add this CacheEntry to the groups that it is now a member of
        for (Iterator it = newGroups.iterator(); it.hasNext();) {
            String groupName = (String) it.next();

            // Update the in-memory groups
            if (memoryCaching && memory) {
                if (groups == null) {
                    groups = new HashMap();
                }

                Set memoryGroup = (Set) groups.get(groupName);

                if (memoryGroup == null) {
                    memoryGroup = new HashSet();
                    groups.put(groupName, memoryGroup);
                }

                memoryGroup.add(key);
            }

            // Update the persistent group maps
            if (persist) {
                Set persistentGroup = persistRetrieveGroup(groupName);

                if (persistentGroup == null) {
                    persistentGroup = new HashSet();
                }

                persistentGroup.add(key);
                persistStoreGroup(groupName, persistentGroup);
            }
        }
    }

    /** OpenSymphony END (pretty long!) */
    /**
     * Returns the appropriate capacity (power of two) for the specified
     * initial capacity argument.
     */
    private int p2capacity(int initialCapacity) {
        int cap = initialCapacity;

        // Compute the appropriate capacity
        int result;

        if ((cap > MAXIMUM_CAPACITY) || (cap < 0)) {
            result = MAXIMUM_CAPACITY;
        } else {
            result = MINIMUM_CAPACITY;

            while (result < cap) {
                result <<= 1;
            }
        }

        return result;
    }

    /* Previous code
    public Object put(Object key, Object value)*/
    private Object put(Object key, Object value, boolean persist) {
        /** OpenSymphony END */
        if (value == null) {
            throw new NullPointerException();
        }

        int hash = hash(key);
        Entry[] tab = table;
        int index = hash & (tab.length - 1);
        Entry first = tab[index];
        Entry e = first;

        for (;;) {
            if (e == null) {
                synchronized (this) {
                    tab = table;

                    /** OpenSymphony BEGIN */

                    // Previous code

                    /*                                        if (first == tab[index]) {
                                                                    //  Add to front of list
                                                                    Entry newEntry = new Entry(hash, key, value, first);
                                                                    tab[index] = newEntry;
                                                                    if (++count >= threshold) rehash();
                                                                    else recordModification(newEntry);
                                                                    return null; */

                    Object oldValue = null;

                    // Remove an item if the cache is full
                    if (size() >= maxEntries) {
                        // part of fix CACHE-255: method should return old value
                        oldValue = remove(removeItem(), false, false);
                    }

                    if (first == tab[index]) {
                        //  Add to front of list
                        Entry newEntry = null;

                        if (memoryCaching) {
                            newEntry = new Entry(hash, key, value, first);
                        } else {
                            newEntry = new Entry(hash, key, NULL, first);
                        }

                        tab[index] = newEntry;
                        itemPut(key);

                        // Persist if required
                        if (persist && !overflowPersistence) {
                            persistStore(key, value);
                        }

                        // If we have a CacheEntry, update the group lookups
                        if (value instanceof CacheEntry) {
                            updateGroups(null, (CacheEntry) value, persist);
                        }

                        if (++count >= threshold) {
                            rehash();
                        } else {
                            recordModification(newEntry);
                        }
                        
                        return oldValue;

                        /** OpenSymphony END  */
                    } else {
                        // wrong list -- retry

                        /** OpenSymphony BEGIN */

                        /* Previous code
                        return sput(key, value, hash);*/
                        return sput(key, value, hash, persist);

                        /** OpenSymphony END */
                    }
                }
            } else if ((key == e.key) || ((e.hash == hash) && key.equals(e.key))) {
                // synch to avoid race with remove and to
                // ensure proper serialization of multiple replaces
                synchronized (this) {
                    tab = table;

                    Object oldValue = e.value;

                    // [CACHE-118] - get the old cache entry even if there's no memory cache
                    if (persist && (oldValue == NULL)) {
                        oldValue = persistRetrieve(key);
                    }

                    if ((first == tab[index]) && (oldValue != null)) {
                        /** OpenSymphony BEGIN */

                        /* Previous code
                        e.value = value;
                        return oldValue; */
                        if (memoryCaching) {
                            e.value = value;
                        }

                        // Persist if required
                        if (persist && overflowPersistence) {
                            persistRemove(key);
                        } else if (persist) {
                            persistStore(key, value);
                        }

                        updateGroups(oldValue, value, persist);
                        itemPut(key);

                        return oldValue;

                        /**        OpenSymphony END */
                    } else {
                        // retry if wrong list or lost race against concurrent remove

                        /** OpenSymphony BEGIN */

                        /* Previous code
                        return sput(key, value, hash);*/
                        return sput(key, value, hash, persist);

                        /** OpenSymphony END */
                    }
                }
            } else {
                e = e.next;
            }
        }
    }

    private synchronized Object remove(Object key, boolean invokeAlgorithm, boolean forcePersist)
    /* Previous code
    public Object remove(Object key) */

    /** OpenSymphony END */  {
        /*
          Strategy:

          Find the entry, then
            1. Set value field to null, to force get() to retry
            2. Rebuild the list without this entry.
               All entries following removed node can stay in list, but
               all preceeding ones need to be cloned.  Traversals rely
               on this strategy to ensure that elements will not be
              repeated during iteration.
        */

        /** OpenSymphony BEGIN */
        if (key == null) {
            return null;
        }

        /** OpenSymphony END */
        int hash = hash(key);
        Entry[] tab = table;
        int index = hash & (tab.length - 1);
        Entry first = tab[index];
        Entry e = first;

        for (;;) {
            if (e == null) {
                tab = getTableForReading();

                if (first == tab[index]) {
                    return null;
                } else {
                    // Wrong list -- must restart traversal at new first

                    /** OpenSymphony BEGIN */

                    /* Previous Code
                    return sremove(key, hash); */
                    return sremove(key, hash, invokeAlgorithm);

                    /** OpenSymphony END */
                }
            } else if ((key == e.key) || ((e.hash == hash) && key.equals(e.key))) {
                synchronized (this) {
                    tab = table;

                    Object oldValue = e.value;
                    if (persistenceListener != null && (oldValue == NULL)) {
                      oldValue = persistRetrieve(key);
                    }

                    // re-find under synch if wrong list
                    if ((first != tab[index]) || (oldValue == null)) {
                        /** OpenSymphony BEGIN */

                        /* Previous Code
                        return sremove(key, hash); */
                        return sremove(key, hash, invokeAlgorithm);
                    }

                    /** OpenSymphony END */
                    e.value = null;
                    count--;

                    /** OpenSymphony BEGIN */
                    if (forcePersist || (!unlimitedDiskCache && !overflowPersistence)) {
                        persistRemove(e.key);
                        // If we have a CacheEntry, update the group lookups
                        if (oldValue instanceof CacheEntry) {
                          CacheEntry oldEntry = (CacheEntry) oldValue;
                            removeGroupMappings(oldEntry.getKey(),
                                oldEntry.getGroups(), true);
                        }
                    } else {
						// only remove from memory groups
						if (oldValue instanceof CacheEntry) {
							CacheEntry oldEntry = (CacheEntry) oldValue;
							removeGroupMappings(oldEntry.getKey(), oldEntry
									.getGroups(), false);
						}
                    }

                    if (!forcePersist && overflowPersistence && ((size() + 1) >= maxEntries)) {
                        persistStore(key, oldValue);
                        // add key to persistent groups but NOT to the memory groups
                        if (oldValue instanceof CacheEntry) {
                        	CacheEntry oldEntry = (CacheEntry) oldValue;
                        	addGroupMappings(oldEntry.getKey(), oldEntry.getGroups(), true, false);
                        }
                    }

                    if (invokeAlgorithm) {
                        itemRemoved(key);
                    }

                    // introduced to fix bug CACHE-255 
                    if (oldValue instanceof CacheEntry) {
                    	CacheEntry oldEntry = (CacheEntry) oldValue;
                    	oldValue = oldEntry.getContent();
                    }

                    /** OpenSymphony END */
                    Entry head = e.next;

                    for (Entry p = first; p != e; p = p.next) {
                        head = new Entry(p.hash, p.key, p.value, head);
                    }

                    tab[index] = head;
                    recordModification(head);

                    return oldValue;
                }
            } else {
                e = e.next;
            }
        }
    }

    /**
     * Remove this CacheEntry from the groups it no longer belongs to.
     * We have to treat the memory and disk group mappings separately so they remain
     * valid for their corresponding memory/disk caches. (eg if mem is limited
     * to 100 entries and disk is unlimited, the group mappings will be
     * different).
     *
     * @param key The cache key that we are removing from the groups.
     * @param oldGroups the set of groups we want to remove the cache entry
     * from.
     * @param persist A flag to indicate whether the keys should be removed
     * from the persistent cache layer.
     */
    private void removeGroupMappings(String key, Set oldGroups, boolean persist) {
        if (oldGroups == null) {
          return;
        }

        for (Iterator it = oldGroups.iterator(); it.hasNext();) {
            String groupName = (String) it.next();

            // Update the in-memory groups
            if (memoryCaching && (this.groups != null)) {
                Set memoryGroup = (Set) groups.get(groupName);

                if (memoryGroup != null) {
                    memoryGroup.remove(key);

                    if (memoryGroup.isEmpty()) {
                        groups.remove(groupName);
                    }
                }
            }

            // Update the persistent group maps
            if (persist) {
                Set persistentGroup = persistRetrieveGroup(groupName);

                if (persistentGroup != null) {
                    persistentGroup.remove(key);

                    if (persistentGroup.isEmpty()) {
                        persistRemoveGroup(groupName);
                    } else {
                        persistStoreGroup(groupName, persistentGroup);
                    }
                }
            }
        }
    }

    /**
     * Updates the groups to reflect the differences between the old and new
     * cache entries. Either of the old or new values can be <code>null</code>
     * or contain a <code>null</code> group list, in which case the entry's
     * groups will all be added or removed respectively.
     *
     * @param oldValue The old CacheEntry that is being replaced.
     * @param newValue The new CacheEntry that is being inserted.
     */
    private void updateGroups(Object oldValue, Object newValue, boolean persist) {
        // If we have/had a CacheEntry, update the group lookups
        boolean oldIsCE = oldValue instanceof CacheEntry;
        boolean newIsCE = newValue instanceof CacheEntry;

        if (newIsCE && oldIsCE) {
            updateGroups((CacheEntry) oldValue, (CacheEntry) newValue, persist);
        } else if (newIsCE) {
            updateGroups(null, (CacheEntry) newValue, persist);
        } else if (oldIsCE) {
            updateGroups((CacheEntry) oldValue, null, persist);
        }
    }

    /**
     * Updates the groups to reflect the differences between the old and new cache entries.
     * Either of the old or new values can be <code>null</code>
     * or contain a <code>null</code> group list, in which case the entry's
     * groups will all be added or removed respectively.
     *
     * @param oldValue The old CacheEntry that is being replaced.
     * @param newValue The new CacheEntry that is being inserted.
     */
    private void updateGroups(CacheEntry oldValue, CacheEntry newValue, boolean persist) {
        Set oldGroups = null;
        Set newGroups = null;

        if (oldValue != null) {
            oldGroups = oldValue.getGroups();
        }

        if (newValue != null) {
            newGroups = newValue.getGroups();
        }

        // Get the names of the groups to remove
        if (oldGroups != null) {
            Set removeFromGroups = new HashSet();

            for (Iterator it = oldGroups.iterator(); it.hasNext();) {
                String groupName = (String) it.next();

                if ((newGroups == null) || !newGroups.contains(groupName)) {
                    // We need to remove this group
                    removeFromGroups.add(groupName);
                }
            }

            removeGroupMappings(oldValue.getKey(), removeFromGroups, persist);
        }

        // Get the names of the groups to add
        if (newGroups != null) {
            Set addToGroups = new HashSet();

            for (Iterator it = newGroups.iterator(); it.hasNext();) {
                String groupName = (String) it.next();

                if ((oldGroups == null) || !oldGroups.contains(groupName)) {
                    // We need to add this group
                    addToGroups.add(groupName);
                }
            }

            addGroupMappings(newValue.getKey(), addToGroups, persist, true);
        }
    }

    /**
     * AbstractConcurrentReadCache collision list entry.
     */
    protected static class Entry implements Map.Entry {
        protected final Entry next;
        protected final Object key;

        /*
           The use of volatile for value field ensures that
           we can detect status changes without synchronization.
           The other fields are never changed, and are
           marked as final.
        */
        protected final int hash;
        protected volatile Object value;

        Entry(int hash, Object key, Object value, Entry next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
            this.value = value;
        }

        // Map.Entry Ops
        public Object getKey() {
            return key;
        }

        /**
         * Set the value of this entry.
         * Note: In an entrySet or
         * entrySet.iterator), unless the set or iterator is used under
         * synchronization of the table as a whole (or you can otherwise
         * guarantee lack of concurrent modification), <tt>setValue</tt>
         * is not strictly guaranteed to actually replace the value field
         * obtained via the <tt>get</tt> operation of the underlying hash
         * table in multithreaded applications.  If iterator-wide
         * synchronization is not used, and any other concurrent
         * <tt>put</tt> or <tt>remove</tt> operations occur, sometimes
         * even to <em>other</em> entries, then this change is not
         * guaranteed to be reflected in the hash table. (It might, or it
         * might not. There are no assurances either way.)
         *
         * @param      value   the new value.
         * @return     the previous value, or null if entry has been detectably
         * removed.
         * @exception  NullPointerException  if the value is <code>null</code>.
         *
         **/
        public Object setValue(Object value) {
            if (value == null) {
                throw new NullPointerException();
            }

            Object oldValue = this.value;
            this.value = value;

            return oldValue;
        }

        /**
         * Get the value.
         * Note: In an entrySet or entrySet.iterator,
         * unless the set or iterator is used under synchronization of the
         * table as a whole (or you can otherwise guarantee lack of
         * concurrent modification), <tt>getValue</tt> <em>might</em>
         * return null, reflecting the fact that the entry has been
         * concurrently removed. However, there are no assurances that
         * concurrent removals will be reflected using this method.
         *
         * @return     the current value, or null if the entry has been
         * detectably removed.
         **/
        public Object getValue() {
            return value;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            Map.Entry e = (Map.Entry) o;

            if (!key.equals(e.getKey())) {
                return false;
            }

            Object v = value;

            return (v == null) ? (e.getValue() == null) : v.equals(e.getValue());
        }

        public int hashCode() {
            Object v = value;

            return hash ^ ((v == null) ? 0 : v.hashCode());
        }

        public String toString() {
            return key + "=" + value;
        }

        protected Object clone() {
            return new Entry(hash, key, value, ((next == null) ? null : (Entry) next.clone()));
        }
    }

    protected class HashIterator implements Iterator, Enumeration {
        protected final Entry[] tab; // snapshot of table
        protected Entry entry = null; // current node of slot
        protected Entry lastReturned = null; // last node returned by next
        protected Object currentKey; // key for current node
        protected Object currentValue; // value for current node
        protected int index; // current slot

        protected HashIterator() {
            tab = AbstractConcurrentReadCache.this.getTableForReading();
            index = tab.length - 1;
        }

        public boolean hasMoreElements() {
            return hasNext();
        }

        public boolean hasNext() {
            /*
              currentkey and currentValue are set here to ensure that next()
              returns normally if hasNext() returns true. This avoids
              surprises especially when final element is removed during
              traversal -- instead, we just ignore the removal during
              current traversal.
            */
            for (;;) {
                if (entry != null) {
                    Object v = entry.value;

                    if (v != null) {
                        currentKey = entry.key;
                        currentValue = v;

                        return true;
                    } else {
                        entry = entry.next;
                    }
                }

                while ((entry == null) && (index >= 0)) {
                    entry = tab[index--];
                }

                if (entry == null) {
                    currentKey = currentValue = null;

                    return false;
                }
            }
        }

        public Object next() {
            if ((currentKey == null) && !hasNext()) {
                throw new NoSuchElementException();
            }

            Object result = returnValueOfNext();
            lastReturned = entry;
            currentKey = currentValue = null;
            entry = entry.next;

            return result;
        }

        public Object nextElement() {
            return next();
        }

        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            AbstractConcurrentReadCache.this.remove(lastReturned.key);
        }

        protected Object returnValueOfNext() {
            return entry;
        }
    }

    protected class KeyIterator extends HashIterator {
        protected Object returnValueOfNext() {
            return currentKey;
        }
    }

    protected class ValueIterator extends HashIterator {
        protected Object returnValueOfNext() {
            return currentValue;
        }
    }
}
