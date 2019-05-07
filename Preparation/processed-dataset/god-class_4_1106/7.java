protected void showOutputOptionsWindow(WidgetEvent ev) {
    // Record the current settings. 
    transparentBackground = transparentBox.getState();
    generateHDR = hdrBox.getState();
    // Show the window. 
    WindowWidget parent = UIUtilities.findWindow(ev.getWidget());
    ComponentsDialog dlg = new ComponentsDialog(parent, Translate.text("outputOptions"), new Widget[] { transparentBox, hdrBox }, new String[] { "", "" });
    if (!dlg.clickedOk()) {
        // Reset the components. 
        transparentBox.setState(transparentBackground);
        hdrBox.setState(generateHDR);
    }
}
