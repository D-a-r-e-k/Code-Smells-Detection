/******************************************************************************/
/**
 * Retrieves the Cells that are directly connected to the given Cell and
 * member of the given list.
 * @param list Only relatives from this List are allowed
 * @param view Relatives from this view are requested
 * @return Relatives from view that are in the list
 * @see #getRelatives(CellView)
 */
protected ArrayList getRelativesFrom(ArrayList list, CellView view) {
    ArrayList relatives = getRelatives(view);
    ArrayList result = new ArrayList();
    for (int i = 0; i < relatives.size(); i++) if (list.contains(relatives.get(i)))
        result.add(relatives.get(i));
    return result;
}
