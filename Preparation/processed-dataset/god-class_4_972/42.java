/******************************************************************************/
/**
 * Returns <code><b>true</b></code> when a cell is a cluster, else 
 * <code<b>false</b></code>. A cell is a cluster when it has under it's 
 * attributes a attribute with the boolean value <code><b>true</b></code> under
 * the key {@link #KEY_IS_CLUSTER}.
 * 
 * @param cell cell, that should be researched wheather it is a cluster or not.
 * @return <code><b>true</b></code> if cell is a cluster, else 
 * <code><b>false</b></code>.
 */
protected boolean isCluster(CellView cell) {
    if (cell.getAttributes().containsKey(KEY_IS_CLUSTER)) {
        if (isTrue((String) cell.getAttributes().get(KEY_IS_CLUSTER))) {
            return true;
        } else {
            System.err.println("FATAL ERROR: CELL CANNOT CLEARLY BE IDENTIFIED AS A CLUSTER!!!");
            return false;
        }
    } else
        return false;
}
