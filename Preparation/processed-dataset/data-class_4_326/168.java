public void updateMapControlsInCanvas() {
    if (mapControls != null && mapControls.isShowing()) {
        mapControls.removeFromComponent(canvas);
        mapControls.addToComponent(canvas);
    }
}
