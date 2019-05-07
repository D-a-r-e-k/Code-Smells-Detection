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
public int compare(final Object o1, final Object o2) {
    String str1[] = (String[]) o1;
    String str2[] = (String[]) o2;
    int depth1 = str1.length;
    // number of elements in array 
    int depth2 = str2.length;
    int depth = (depth1 < depth2) ? depth1 : depth2;
    int i;
    for (i = 0; i < depth; i++) {
        int rel = str1[i].compareTo(str2[i]);
        if (rel < 0) {
            return -1;
        } else if (rel > 0) {
            return 1;
        }
    }
    // the first n == depth strings are the same 
    if (depth1 < depth2) {
        return -1;
    } else if (depth1 > depth2) {
        return 1;
    }
    // depth1 == depth2 
    return 0;
}
