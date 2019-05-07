/**
     *  DOM Level 3 Core CR - Experimental.
     * 
     *  The list of the parameters supported by this 
     * <code>DOMConfiguration</code> object and for which at least one value 
     * can be set by the application. Note that this list can also contain 
     * parameter names defined outside this specification. 
     */
public DOMStringList getParameterNames() {
    if (fRecognizedParameters == null) {
        ArrayList parameters = new ArrayList();
        //Add DOM recognized parameters  
        //REVISIT: Would have been nice to have a list of   
        //recognized parameters.  
        parameters.add(Constants.DOM_NAMESPACES);
        parameters.add(Constants.DOM_SPLIT_CDATA);
        parameters.add(Constants.DOM_DISCARD_DEFAULT_CONTENT);
        parameters.add(Constants.DOM_XMLDECL);
        parameters.add(Constants.DOM_CANONICAL_FORM);
        parameters.add(Constants.DOM_VALIDATE_IF_SCHEMA);
        parameters.add(Constants.DOM_VALIDATE);
        parameters.add(Constants.DOM_CHECK_CHAR_NORMALIZATION);
        parameters.add(Constants.DOM_DATATYPE_NORMALIZATION);
        parameters.add(Constants.DOM_FORMAT_PRETTY_PRINT);
        parameters.add(Constants.DOM_NORMALIZE_CHARACTERS);
        parameters.add(Constants.DOM_WELLFORMED);
        parameters.add(Constants.DOM_INFOSET);
        parameters.add(Constants.DOM_NAMESPACE_DECLARATIONS);
        parameters.add(Constants.DOM_ELEMENT_CONTENT_WHITESPACE);
        parameters.add(Constants.DOM_ENTITIES);
        parameters.add(Constants.DOM_CDATA_SECTIONS);
        parameters.add(Constants.DOM_COMMENTS);
        parameters.add(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS);
        parameters.add(Constants.DOM_ERROR_HANDLER);
        //parameters.add(Constants.DOM_SCHEMA_LOCATION);  
        //parameters.add(Constants.DOM_SCHEMA_TYPE);  
        //Add recognized xerces features and properties  
        fRecognizedParameters = new DOMStringListImpl(parameters);
    }
    return fRecognizedParameters;
}
