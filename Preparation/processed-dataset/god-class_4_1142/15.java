// check whether we need to report an error against the given uri.  
// if we have reported an error, then we don't need to report again;  
// otherwise we reported the error, and remember this fact.  
private final boolean needReportTNSError(String uri) {
    if (fReportedTNS == null)
        fReportedTNS = new Vector();
    else if (fReportedTNS.contains(uri))
        return false;
    fReportedTNS.addElement(uri);
    return true;
}
