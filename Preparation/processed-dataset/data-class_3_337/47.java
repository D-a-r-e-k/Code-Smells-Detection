/** Re-create the wrapped positions in the hashmap, update the wrapped position, and add them to the list.
    * @param whm weakly-linked hashmap of wrapped positions and their offsets
    */
public void setWrappedPositionOffsets(WeakHashMap<WrappedPosition, Integer> whm) throws BadLocationException {
    synchronized (_wrappedPosListLock) {
        if (_wrappedPosList == null) {
            _wrappedPosList = new LinkedList<WeakReference<WrappedPosition>>();
        }
        _wrappedPosList.clear();
        for (Map.Entry<WrappedPosition, Integer> entry : whm.entrySet()) {
            if (entry.getKey() != null) {
                // hasn't been garbage-collected yet 
                WrappedPosition wp = entry.getKey();
                wp.setWrapped(createUnwrappedPosition(entry.getValue()));
                _wrappedPosList.add(new WeakReference<WrappedPosition>(wp));
            }
        }
    }
}
