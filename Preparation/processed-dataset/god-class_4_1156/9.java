// Main functionality, create the map. 
/**
     * Creates a <code>Map</code> for the given <code>Game</code>.
     *
     * The <code>Map</code> is added to the <code>Game</code> after
     * it is created.
     *
     * @param game The <code>Game</code> to add the map to.
     * @param landMap Determines whether there should be land or ocean
     *     on a given tile.  This array also specifies the size of the
     *     map that is going to be created.
     * @see Map
     */
public void createMap(Game game, boolean[][] landMap) {
    createMap(game, null, landMap);
}
