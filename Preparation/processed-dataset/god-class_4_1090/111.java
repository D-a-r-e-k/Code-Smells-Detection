//}}}  
//{{{ parseBufferLocalProperties() method  
protected void parseBufferLocalProperties() {
    int lastLine = Math.min(9, getLineCount() - 1);
    parseBufferLocalProperties(getSegment(0, getLineEndOffset(lastLine) - 1));
    // first line for last 10 lines, make sure not to overlap  
    // with the first 10  
    int firstLine = Math.max(lastLine + 1, getLineCount() - 10);
    if (firstLine < getLineCount()) {
        int length = getLineEndOffset(getLineCount() - 1) - (getLineStartOffset(firstLine) + 1);
        parseBufferLocalProperties(getSegment(getLineStartOffset(firstLine), length));
    }
}
