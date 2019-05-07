// scanAttribute(XMLAttributesImpl,boolean[]):boolean 
/** 
         * Scans a pseudo attribute. 
         *
         * @param attributes The list of attributes.
         */
protected boolean scanPseudoAttribute(XMLAttributesImpl attributes) throws IOException {
    return scanAttribute(attributes, fSingleBoolean, '?');
}
