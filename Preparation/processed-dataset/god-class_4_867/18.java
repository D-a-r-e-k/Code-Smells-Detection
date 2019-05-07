// scanEndElement() 
// 
// Private methods 
// 
/**
         * Returns true if the given element has an end-tag.
         */
private boolean isEnded(String ename) {
    String content = new String(fCurrentEntity.buffer, fCurrentEntity.offset, fCurrentEntity.length - fCurrentEntity.offset);
    return content.toLowerCase().indexOf("</" + ename.toLowerCase() + ">") != -1;
}
