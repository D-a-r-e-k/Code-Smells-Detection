/** Remove all positions that have been garbage-collected from the list of positions, then return a weakly-linked
    * hashmap with positions and their current offsets.
    * @return list of weak references to all positions that have been created and that have not been garbage-collected yet.
    */
public WeakHashMap<WrappedPosition, Integer> getWrappedPositionOffsets() {
    LinkedList<WeakReference<WrappedPosition>> newList = new LinkedList<WeakReference<WrappedPosition>>();
    synchronized (_wrappedPosListLock) {
        if (_wrappedPosList == null) {
            _wrappedPosList = new LinkedList<WeakReference<WrappedPosition>>();
        }
        WeakHashMap<WrappedPosition, Integer> ret = new WeakHashMap<WrappedPosition, Integer>(_wrappedPosList.size());
        for (WeakReference<WrappedPosition> wr : _wrappedPosList) {
            if (wr.get() != null) {
                // hasn't been garbage-collected yet 
                newList.add(wr);
                ret.put(wr.get(), wr.get().getOffset());
            }
        }
        _wrappedPosList.clear();
        _wrappedPosList = newList;
        return ret;
    }
}
