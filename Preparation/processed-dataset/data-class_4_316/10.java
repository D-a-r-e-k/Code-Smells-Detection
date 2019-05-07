/**
     *  When the application provides a filter, the serializer will call out
     * to the filter before serializing each Node. Attribute nodes are never
     * passed to the filter. The filter implementation can choose to remove
     * the node from the stream or to terminate the serialization early.
     */
public void setFilter(LSSerializerFilter filter) {
    serializer.fDOMFilter = filter;
}
