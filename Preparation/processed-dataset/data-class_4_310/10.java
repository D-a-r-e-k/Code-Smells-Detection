//  
// DocumentRange methods  
//  
/**
     */
public Range createRange() {
    if (ranges == null) {
        ranges = new LinkedList();
        rangeReferenceQueue = new ReferenceQueue();
    }
    Range range = new RangeImpl(this);
    removeStaleRangeReferences();
    ranges.add(new WeakReference(range, rangeReferenceQueue));
    return range;
}
