/**@param  toRight <tt>true</tt> = try to move the currentWrapper to right; <tt>false</tt> = try to move the currentWrapper to left;
	 * @param  currentLevel List which contains the CellWrappers for the current level
	 * @param  currentIndexInTheLevel
	 * @param  currentPriority
	 * @param  currentWrapper The Wrapper
	 *
	 * @return The free GridPosition or -1 is position is not free.
	 */
protected boolean move(boolean toRight, List currentLevel, int currentIndexInTheLevel, int currentPriority) {
    CellWrapper currentWrapper = (CellWrapper) currentLevel.get(currentIndexInTheLevel);
    boolean moved = false;
    int neighborIndexInTheLevel = currentIndexInTheLevel + (toRight ? 1 : -1);
    int newGridPosition = currentWrapper.getGridPosition() + (toRight ? 1 : -1);
    // is the grid position possible? 
    if (0 > newGridPosition || newGridPosition >= gridAreaSize) {
        return false;
    }
    // if the node is the first or the last we can move 
    if (toRight && currentIndexInTheLevel == currentLevel.size() - 1 || !toRight && currentIndexInTheLevel == 0) {
        moved = true;
    } else {
        // else get the neighbor and ask his gridposition 
        // if he has the requested new grid position 
        // check the priority 
        CellWrapper neighborWrapper = (CellWrapper) currentLevel.get(neighborIndexInTheLevel);
        int neighborPriority = neighborWrapper.getPriority();
        if (neighborWrapper.getGridPosition() == newGridPosition) {
            if (neighborPriority >= currentPriority) {
                return false;
            } else {
                moved = move(toRight, currentLevel, neighborIndexInTheLevel, currentPriority);
            }
        } else {
            moved = true;
        }
    }
    if (moved) {
        currentWrapper.setGridPosition(newGridPosition);
    }
    return moved;
}
