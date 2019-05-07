/******************************************************************************/
/**
 * Arranges the initial cositions for the inserted cells. This is done, by 
 * placing the inserted cells in the barycenter of their relatives.
 * (possible with errors ... might be fixed soon) 
 */
private void arrangePlacement(CellView[] views) {
    for (int i = 0; i < cellList.size(); i++) initPosition((CellView) cellList.get(i));
    if (views != null) {
        if (views.length > 0) {
            ArrayList cellLevelList = new ArrayList();
            for (int i = 0; i < views.length; i++) {
                if (views[i] instanceof VertexView) {
                    ArrayList relatives = getRelativesFrom(cellList, views[i]);
                    if (relatives.size() > 0) {
                        if (views[i].getAttributes() == null)
                            views[i].changeAttributes(new AttributeMap());
                        views[i].getAttributes().put(KEY_POSITION, computeBarycenter(relatives));
                        cellLevelList.add(views[i]);
                    }
                }
            }
            for (int i = 0; i < cellLevelList.size(); i++) cellList.add(cellLevelList.get(i));
            int childViewCount = 0;
            CellView[] possibleChildViews = new CellView[views.length - cellLevelList.size()];
            for (int i = 0; i < views.length; i++) if (!cellLevelList.contains(views[i]))
                possibleChildViews[childViewCount++] = views[i];
            arrangePlacement(possibleChildViews);
        }
    }
}
