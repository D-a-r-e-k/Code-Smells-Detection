private void showAdvancedWindow(WidgetEvent ev) {
    // Record the current settings. 
    smoothing = smoothField.getValue();
    adaptive = adaptiveBox.getState();
    hideBackfaces = hideBackfaceBox.getState();
    generateHDR = hdrBox.getState();
    // Show the window. 
    WindowWidget parent = UIUtilities.findWindow(ev.getWidget());
    ComponentsDialog dlg = new ComponentsDialog(parent, Translate.text("advancedOptions"), new Widget[] { smoothField, adaptiveBox, hideBackfaceBox, hdrBox }, new String[] { Translate.text("texSmoothing"), null, null, null });
    if (!dlg.clickedOk()) {
        // Reset the components. 
        smoothField.setValue(smoothing);
        adaptiveBox.setState(adaptive);
        hideBackfaceBox.setState(hideBackfaces);
        hdrBox.setState(generateHDR);
    }
}
