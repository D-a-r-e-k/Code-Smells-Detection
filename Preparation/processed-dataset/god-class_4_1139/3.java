//  
// DocumentTraversal methods  
//  
/**
     * NON-DOM extension:
     * Create and return a NodeIterator. The NodeIterator is
     * added to a list of NodeIterators so that it can be
     * removed to free up the DOM Nodes it references.
     *
     * @param root The root of the iterator.
     * @param whatToShow The whatToShow mask.
     * @param filter The NodeFilter installed. Null means no filter.
     */
public NodeIterator createNodeIterator(Node root, short whatToShow, NodeFilter filter) {
    return createNodeIterator(root, whatToShow, filter, true);
}
