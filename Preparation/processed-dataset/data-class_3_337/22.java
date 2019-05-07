/** Returns the offset corresponding to the first character of the given line number, or -1 if the lineNum is not
    * found.  Line number counting begins with 1 not 0.  Assumes read lock is already held.
    * @param lineNum the line number for which to calculate the offset.
    * @return the offset of the first character in the given line number
    */
public int _getOffset(int lineNum) {
    if (lineNum <= 0)
        return -1;
    if (lineNum == 1)
        return 0;
    //    synchronized(_reduced) { 
    final int origPos = getCurrentLocation();
    try {
        final int docLen = getLength();
        setCurrentLocation(0);
        // _currentLocation is candidate offset to return 
        int i;
        for (i = 1; (i < lineNum) && (_currentLocation < docLen); i++) {
            int dist = _reduced.getDistToNextNewline();
            // or end of doc 
            if (_currentLocation + dist < docLen)
                dist++;
            // skip newline 
            move(dist);
        }
        if (i == lineNum)
            return _currentLocation;
        else
            return -1;
    } finally {
        setCurrentLocation(origPos);
    }
}
