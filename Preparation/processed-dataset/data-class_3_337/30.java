/** Goes to a particular line in the document. */
public void gotoLine(int line) {
    int dist;
    if (line < 0)
        return;
    int actualLine = 1;
    int len = getLength();
    setCurrentLocation(0);
    for (int i = 1; (i < line) && (_currentLocation < len); i++) {
        dist = _reduced.getDistToNextNewline();
        if (_currentLocation + dist < len)
            dist++;
        actualLine++;
        move(dist);
    }
}
