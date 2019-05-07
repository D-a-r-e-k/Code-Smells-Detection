/** Factory method for created WrappedPositions. Stores the created Position instance so it can be linked to a 
    * different DefinitionsDocument later. 
    */
public Position createPosition(final int offset) throws BadLocationException {
    /* The following attempt to defer document loading did not work because offset became stale.  Postions must be
     * created eagerly. */
    //    WrappedPosition wp = new WrappedPosition(new LazyPosition(new Suspension<Position>() { 
    //      public Position eval() { 
    //        try { return createUnwrappedPosition(offset); } 
    //        catch(BadLocationException e) { throw new UnexpectedException(e); } 
    //      } 
    //    })); 
    WrappedPosition wp = new WrappedPosition(createUnwrappedPosition(offset));
    synchronized (_wrappedPosListLock) {
        if (_wrappedPosList == null)
            _wrappedPosList = new LinkedList<WeakReference<WrappedPosition>>();
        _wrappedPosList.add(new WeakReference<WrappedPosition>(wp));
    }
    return wp;
}
