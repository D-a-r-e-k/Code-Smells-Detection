public Protocol setupProtocolStack(ProtocolStack copySource) throws Exception {
    Vector<Protocol> protocols = copySource.copyProtocols(stack);
    Collections.reverse(protocols);
    return connectProtocols(protocols);
}
