private void showDetails(ColopediaTreeItem nodeItem) {
    detailPanel.removeAll();
    if (nodeItem.getPanelType() != null && nodeItem.getId() != null) {
        nodeItem.getPanelType().buildDetail(nodeItem.getId(), detailPanel);
    }
    detailPanel.revalidate();
    detailPanel.repaint();
}
