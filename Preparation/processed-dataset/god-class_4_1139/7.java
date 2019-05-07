//  
// Not DOM Level 2. Support DocumentTraversal methods.  
//  
/** 
     * This is not called by the developer client. The
     * developer client uses the detach() function on the
     * NodeIterator itself. <p>
     *
     * This function is called from the NodeIterator#detach().
     */
void removeNodeIterator(NodeIterator nodeIterator) {
    if (nodeIterator == null)
        return;
    if (iterators == null)
        return;
    removeStaleIteratorReferences();
    Iterator i = iterators.iterator();
    while (i.hasNext()) {
        Object iterator = ((Reference) i.next()).get();
        if (iterator == nodeIterator) {
            i.remove();
            return;
        } else if (iterator == null) {
            i.remove();
        }
    }
}
