//}}}  
//{{{ getFoldLevel() method  
/**
	 * Returns the fold level of the specified line.
	 * @param line A physical line index
	 * @since jEdit 3.1pre1
	 */
public int getFoldLevel(int line) {
    if (line < 0 || line >= lineMgr.getLineCount())
        throw new ArrayIndexOutOfBoundsException(line);
    if (foldHandler instanceof DummyFoldHandler)
        return 0;
    int firstInvalidFoldLevel = lineMgr.getFirstInvalidFoldLevel();
    if (firstInvalidFoldLevel == -1 || line < firstInvalidFoldLevel) {
        return lineMgr.getFoldLevel(line);
    } else {
        if (Debug.FOLD_DEBUG)
            Log.log(Log.DEBUG, this, "Invalid fold levels from " + firstInvalidFoldLevel + " to " + line);
        int newFoldLevel = 0;
        boolean changed = false;
        int firstUpdatedFoldLevel = firstInvalidFoldLevel;
        for (int i = firstInvalidFoldLevel; i <= line; i++) {
            Segment seg = new Segment();
            newFoldLevel = foldHandler.getFoldLevel(this, i, seg);
            if (newFoldLevel != lineMgr.getFoldLevel(i)) {
                if (Debug.FOLD_DEBUG)
                    Log.log(Log.DEBUG, this, i + " fold level changed");
                changed = true;
                // Update preceding fold levels if necessary  
                if (i == firstInvalidFoldLevel) {
                    List<Integer> precedingFoldLevels = foldHandler.getPrecedingFoldLevels(this, i, seg, newFoldLevel);
                    if (precedingFoldLevels != null) {
                        int j = i;
                        for (Integer foldLevel : precedingFoldLevels) {
                            j--;
                            lineMgr.setFoldLevel(j, foldLevel.intValue());
                        }
                        if (j < firstUpdatedFoldLevel)
                            firstUpdatedFoldLevel = j;
                    }
                }
            }
            lineMgr.setFoldLevel(i, newFoldLevel);
        }
        if (line == lineMgr.getLineCount() - 1)
            lineMgr.setFirstInvalidFoldLevel(-1);
        else
            lineMgr.setFirstInvalidFoldLevel(line + 1);
        if (changed) {
            if (Debug.FOLD_DEBUG)
                Log.log(Log.DEBUG, this, "fold level changed: " + firstUpdatedFoldLevel + ',' + line);
            fireFoldLevelChanged(firstUpdatedFoldLevel, line);
        }
        return newFoldLevel;
    }
}
