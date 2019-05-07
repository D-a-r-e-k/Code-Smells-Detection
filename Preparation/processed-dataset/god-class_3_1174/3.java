public String getClassNameFromUri(String uri) {
    String className = (String) _extensionNamespaceTable.get(uri);
    if (className != null)
        return className;
    else {
        if (uri.startsWith(JAVA_EXT_XSLTC)) {
            int length = JAVA_EXT_XSLTC.length() + 1;
            return (uri.length() > length) ? uri.substring(length) : EMPTYSTRING;
        } else if (uri.startsWith(JAVA_EXT_XALAN)) {
            int length = JAVA_EXT_XALAN.length() + 1;
            return (uri.length() > length) ? uri.substring(length) : EMPTYSTRING;
        } else if (uri.startsWith(JAVA_EXT_XALAN_OLD)) {
            int length = JAVA_EXT_XALAN_OLD.length() + 1;
            return (uri.length() > length) ? uri.substring(length) : EMPTYSTRING;
        } else {
            int index = uri.lastIndexOf('/');
            return (index > 0) ? uri.substring(index + 1) : uri;
        }
    }
}
