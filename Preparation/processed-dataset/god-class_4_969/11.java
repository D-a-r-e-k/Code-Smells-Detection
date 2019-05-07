/******************************************************************************/
/**
 * Initialises the Algorithm. This is the step right before running the 
 * Algorithm. Letting this method set the initial Positions for all cells is
 * only necessary when the Algorithm makes a normal run. Otherwise the initial
 * Positions are allready set, by  
 * {@link #arrangeLayoutUpdateInsertPlacement(CellView[]) 
 * arrangeLayoutUpdateInsertPlacement(...)}.
 * @param setInitPositions Determines, if the initial Positions of the cells 
 * should be set or not. Initial Positions are calculated by random.
 */
private void init(boolean setInitPositions) {
    if (setInitPositions) {
        for (int i = 0; i < applyCellList.size(); i++) if (!((CellView) applyCellList.get(i)).getAttributes().containsKey(KEY_POSITION))
            setPosition(i, (Math.random() * bounds.getWidth()) + bounds.getX(), (Math.random() * bounds.getHeight()) + bounds.getY());
        for (int i = 0; i < cellList.size(); i++) if (!((CellView) cellList.get(i)).getAttributes().containsKey(KEY_POSITION))
            setPosition((CellView) cellList.get(i), (Math.random() * bounds.getWidth()) + bounds.getX(), (Math.random() * bounds.getHeight()) + bounds.getY());
    }
    temperature = initTemperature;
    maxRounds = Math.min(100 * applyCellList.size(), getMaxRoundsByTemperature(temperature));
    round = 0;
}
