// copies all settings that could have been modified  
// by calls to LSSerializer methods from one serializer to another.  
// IMPORTANT:  if new methods are implemented or more settings of  
// the serializer are made alterable, this must be  
// reflected in this method!  
private void copySettings(XMLSerializer src, XMLSerializer dest) {
    dest.fDOMErrorHandler = fErrorHandler;
    dest._format.setEncoding(src._format.getEncoding());
    dest._format.setLineSeparator(src._format.getLineSeparator());
    dest.fDOMFilter = src.fDOMFilter;
}
