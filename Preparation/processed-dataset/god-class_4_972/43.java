/******************************************************************************/
private void colorizeClusters(ArrayList clusterList) {
    Color[] colorList = new Color[] { Color.black, Color.magenta, Color.yellow, Color.blue, Color.green, Color.gray, Color.cyan, Color.red, Color.darkGray, Color.lightGray, Color.orange, Color.pink };
    for (int i = 0; i < clusterList.size(); i++) if (i < colorList.length) {
        ArrayList clusteredVertices = (ArrayList) ((CellView) clusterList.get(i)).getAttributes().get(KEY_CLUSTERED_VERTICES);
        showCellList(clusteredVertices, colorList[i]);
    }
}
