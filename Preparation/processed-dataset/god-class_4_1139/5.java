/**
     * NON-DOM extension:
     * Create and return a TreeWalker.
     *
     * @param root The root of the iterator.
     * @param whatToShow The whatToShow mask.
     * @param filter The NodeFilter installed. Null means no filter.
     */
public TreeWalker createTreeWalker(Node root, short whatToShow, NodeFilter filter) {
    return createTreeWalker(root, whatToShow, filter, true);
}
