/******************************************************************************/
/**
 * This is a rating method for the cells. It is used during 
 * {@link #computeImpulse(CellView)} scale some forces. 
 * 
 * @param view Cell, the weight is of interest.
 */
//TODO: method doesn't work right for clusters 
private double getNodeWeight(CellView view) {
    if (view.getAttributes().containsKey(KEY_MASSINDEX))
        return ((Double) view.getAttributes().get(KEY_MASSINDEX)).doubleValue();
    int childCount = getRelatives(view).size();
    double massIndex = (childCount + 1) / 2.0;
    view.getAttributes().put(KEY_MASSINDEX, new Double(massIndex));
    return massIndex;
}
