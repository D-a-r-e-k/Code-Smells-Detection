void storeLocations(String sLocation, String nsLocation) {
    if (sLocation != null) {
        if (!XMLSchemaLoader.tokenizeSchemaLocationStr(sLocation, fLocationPairs)) {
            // error!  
            fXSIErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, "SchemaLocation", new Object[] { sLocation }, XMLErrorReporter.SEVERITY_WARNING);
        }
    }
    if (nsLocation != null) {
        XMLSchemaLoader.LocationArray la = ((XMLSchemaLoader.LocationArray) fLocationPairs.get(XMLSymbols.EMPTY_STRING));
        if (la == null) {
            la = new XMLSchemaLoader.LocationArray();
            fLocationPairs.put(XMLSymbols.EMPTY_STRING, la);
        }
        la.addLocation(nsLocation);
    }
}
