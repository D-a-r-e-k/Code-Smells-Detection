/******************************************************************************/
/**
 * Returns all Cells, that have a direct connection with the given cell and are
 * a member of the given list.
 * 
 * @param list List of some cells, that should contain some relatives from view
 * @param view Cell, the relatives are requested from
 * @return List of all relatives that are in list.
 * @see #getRelatives(CellView)
 */
private ArrayList getRelativesFrom(ArrayList list, CellView view) {
    ArrayList relatives = getRelatives(view);
    ArrayList result = new ArrayList();
    for (int i = 0; i < relatives.size(); i++) if (list.contains(relatives.get(i)))
        result.add(relatives.get(i));
    return result;
}
