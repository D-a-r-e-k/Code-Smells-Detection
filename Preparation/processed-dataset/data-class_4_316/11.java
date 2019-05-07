// this initializes a newly-created serializer  
private void initSerializer(XMLSerializer ser) {
    ser.fNSBinder = new NamespaceSupport();
    ser.fLocalNSBinder = new NamespaceSupport();
    ser.fSymbolTable = new SymbolTable();
}
