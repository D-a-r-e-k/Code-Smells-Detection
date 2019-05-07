/** 
         * Scans a real attribute. 
         *
         * @param attributes The list of attributes.
         * @param empty      Is used for a second return value to indicate 
         *                   whether the start element tag is empty 
         *                   (e.g. "/&gt;").
         */
protected boolean scanAttribute(XMLAttributesImpl attributes, boolean[] empty) throws IOException {
    return scanAttribute(attributes, empty, '/');
}
