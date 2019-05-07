// TODO: use a compilation flag to use table assist here instead of binary search  
// BETTER YET: use binsearch for online mode and table assist for offline [when memory is not an issue]  
/**
     * Returns the maximum index 'i' such that (values[i] <= x). values[]
     * contains distinct non-negative integers in increasing order. values[0] is 0,
     * 'x' is non-negative.
     * 
     * Edge case:
     *  returns values.length-1 if values [values.length - 1] < x
     */
private static int lowbound(final int[] values, final int x) {
    int low = 0, high = values.length - 1;
    // assertion: lb is in [low, high]  
    while (low <= high) {
        final int m = (low + high) >> 1;
        final int v = values[m];
        if (v == x)
            return m;
        else if (v < x)
            low = m + 1;
        else
            // v > x  
            high = m - 1;
    }
    return high;
}
