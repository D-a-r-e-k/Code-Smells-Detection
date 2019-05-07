/**
     *  Goes through the current element stack and pops all elements until this
     *  element is found - this essentially "closes" and element.
     *
     *  @param s
     *  @return The new current element, or null, if there was no such element in the entire stack.
     */
private Element popElement(String s) {
    int flushedBytes = flushPlainText();
    Element currEl = m_currentElement;
    while (currEl.getParentElement() != null) {
        if (currEl.getName().equals(s) && !currEl.isRootElement()) {
            m_currentElement = currEl.getParentElement();
            // 
            //  Check if it's okay for this element to be empty.  Then we will 
            //  trick the JDOM generator into not generating an empty element, 
            //  by putting an empty string between the tags.  Yes, it's a kludge 
            //  but what'cha gonna do about it. :-) 
            // 
            if (flushedBytes == 0 && Arrays.binarySearch(EMPTY_ELEMENTS, s) < 0) {
                currEl.addContent("");
            }
            return m_currentElement;
        }
        currEl = currEl.getParentElement();
    }
    return null;
}
