public void setActiveUnit(Unit unitToActivate) {
    mapViewer.setActiveUnit(unitToActivate);
    if (unitToActivate != null && !freeColClient.getMyPlayer().owns(unitToActivate)) {
        canvas.repaint(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
