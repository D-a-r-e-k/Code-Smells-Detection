/* Registry.java */

package org.quilt.reg;

import java.util.*;

/**
 * <p>A registry for storing Quilt run-time options.  The registry
 * stores key-value pairs, where the key takes the form of an array
 * of Strings.  The registry is sorted, but XXX there is currently
 * no support for sequential access or partial-key access.</p>
 *
 * <p>This class is not thread-safe.</p>
 *
 * @author <a href="jddixon@users.sourceforge.net">Jim Dixon</a>
 */

public class Registry {

    // PRIVATE VARIABLES, CLASS, AND CONSTRUCTOR ////////////////////

    private SortedMap registry = null;  // key = String[], value = Object

    /** Comparator for String arrays. */
    private class CmpArrays implements Comparator {
        /**
         * Compare String arrays.  The arrays are compared element
         * by element starting at [0].  If any such pair differ,
         * then the result of the String comparison is returned.
         * If they match up to and including the last element of the
         * shorter array of the two, then the shorter is deemed to sort
         * before the longer.  The two arrays are equal if and only if
         * they have the same number of elements and each pair of
         * elements is equal.
         *
         * @param o1 First String array.
         * @param o2 Second.
         * @return -1 if o1 &lt; o2, 0 if o1 == o2, 1 if o1 &gt; o2
         */
        public int compare (final Object o1, final Object o2) {
            String str1[] = (String[]) o1;
            String str2[] = (String[]) o2;
            int depth1 = str1.length;   // number of elements in array
            int depth2 = str2.length;
            int depth = (depth1 < depth2) ? depth1 : depth2;

            int i;
            for (i = 0; i < depth ; i++) {
                int rel = str1[i].compareTo(str2[i]);
                if ( rel < 0 ) {
                    return -1;
                } else if ( rel > 0 ) {
                    return 1;
                }
                // otherwise equal, so continue
            }
            // the first n == depth strings are the same
            if (depth1 < depth2) {
                return -1;              // shorter
            } else if (depth1 > depth2) {
                return 1;               // longer
            }
            // depth1 == depth2
            return 0;                   // a perfect match
        }
    }

    /** No-arg constructor. */
    public Registry() {
        registry   = new TreeMap(new CmpArrays() );
        registry.clear();   // should not be necessary, doesn't solve problem
    }
    
    // OTHER METHODS ////////////////////////////////////////////////

    /** Remove all elements from the registry. */
    final public void clear () {
        registry.clear();
    }
    /** Return comparator used in sorting keys. */
    final Comparator comparator() {
        return new CmpArrays();
    }
    /** @return True if there is an item under this key in the registry. */
    final public boolean containsKey(String [] key) {
        if (key == null) {
            throw new IllegalArgumentException("null key");
        }
        return registry.containsKey(key);
    }
    /**
     * Get an object from the registry.
     *
     * @param key    Array of strings, the unique key.
     * @return value Object stored under the key, or null if none.
     */
    final public Object get (String [] key) {
        if (key == null) {
            throw new IllegalArgumentException("null key");
        }
        if ( registry.containsKey (key) ) {
            return registry.get (key);
        } else {
            return null;
        }
    }
    /** @return True if there are no elements in the registry. */
    final public boolean isEmpty() {
        return registry.isEmpty();
    }
    /** 
     * XXX An overly powerful method.  
     * @todo Replace with first/next/last access.
     * @return The (editable) set of keys. 
     */
    public Set keySet() {
        return registry.keySet();
    }
    /**
     * Add an object to the registry, overwriting any existing value.
     *
     * @param key   Array of Strings, the unique key.  Must not be null.
     * @param value Object to be stored under this key.
     * @return      Old value if there was one, null otherwise.
     */
    final public Object put (String [] key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException ("null key");
        }
        // make a copy of the key
        String keyCopy[] = new String [ key.length ];
        for (int i = 0; i < key.length; i++) {
            keyCopy[i] = key[i];
        }
        return registry.put (keyCopy, value);
    }

    /**
     * Remove an item from the registry.
     *
     * @param key   Array of Strings.
     * @return      The value associated with the key, or null if none.
     */
    final public Object remove (String [] key) {
        if (key == null) {
            throw new IllegalArgumentException("null key");
        }
        return registry.remove(key);
    }
    /** @return Number of items in the registry. */
    final public int size () {
        return registry.size();
    }
    /**
     * Convert a class or method name into a String array.
     * 
     * @return A string array containing the fields of the name.
     */
    final public String[] splitClassName( String name ) {
        return name.split(".");
    }
}
