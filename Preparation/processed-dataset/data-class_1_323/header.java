void method0() { 
/**
         * The name of the bucket being listed.  Null if request fails.
         */
public String name = null;
/**
         * The prefix echoed back from the request.  Null if request fails.
         */
public String prefix = null;
/**
         * The marker echoed back from the request.  Null if request fails.
         */
public String marker = null;
/**
         * The delimiter echoed back from the request.  Null if not specified in
         * the request, or if it fails.
         */
public String delimiter = null;
/**
         * The maxKeys echoed back from the request if specified.  0 if request fails.
         */
public int maxKeys = 0;
/**
         * Indicates if there are more results to the list.  True if the current
         * list results have been truncated.  false if request fails.
         */
public boolean isTruncated = false;
/**
         * Indicates what to use as a marker for subsequent list requests in the event
         * that the results are truncated.  Present only when a delimiter is specified.
         * Null if request fails.
         */
public String nextMarker = null;
/**
         * A List of ListEntry objects representing the objects in the given bucket.
         * Null if the request fails.
         */
public List entries = null;
/**
         * A List of CommonPrefixEntry objects representing the common prefixes of the
         * keys that matched up to the delimiter.  Null if the request fails.
         */
public List commonPrefixEntries = null;
}
