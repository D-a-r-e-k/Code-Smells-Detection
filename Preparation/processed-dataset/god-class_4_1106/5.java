protected void showAdvancedOptionsWindow(WidgetEvent ev) {
    // Layout the window. 
    FormContainer content = new FormContainer(2, 10);
    content.setColumnWeight(0, 0.0);
    LayoutInfo leftLayout = new LayoutInfo(LayoutInfo.EAST, LayoutInfo.NONE, new Insets(0, 0, 0, 5), null);
    LayoutInfo rightLayout = new LayoutInfo(LayoutInfo.WEST, LayoutInfo.HORIZONTAL, null, null);
    content.add(Translate.label("maxRayTreeDepth"), 0, 0, leftLayout);
    content.add(Translate.label("minRayIntensity"), 0, 1, leftLayout);
    content.add(Translate.label("matStepSize"), 0, 3, leftLayout);
    content.add(Translate.label("texSmoothing"), 0, 4, leftLayout);
    content.add(rayDepthField, 1, 0, rightLayout);
    content.add(rayCutoffField, 1, 1, rightLayout);
    content.add(stepSizeField, 1, 3, rightLayout);
    content.add(smoothField, 1, 4, rightLayout);
    content.add(Translate.label("extraGISmoothing"), 0, 5, 2, 1, rightLayout);
    RowContainer row = new RowContainer();
    content.add(row, 0, 6, 2, 1);
    row.add(new BLabel(Translate.text("Textures") + ":"));
    row.add(extraGIField);
    row.add(new BLabel(Translate.text("environment") + ":"));
    row.add(extraGIEnvField);
    content.add(adaptiveBox, 0, 7, 2, 1, rightLayout);
    content.add(reducedMemoryBox, 0, 8, 2, 1, rightLayout);
    content.add(rouletteBox, 0, 9, 2, 1, rightLayout);
    // Record the current settings. 
    maxRayDepth = (int) rayDepthField.getValue();
    minRayIntensity = (float) rayCutoffField.getValue();
    stepSize = stepSizeField.getValue();
    smoothing = smoothField.getValue();
    extraGISmoothing = extraGIField.getValue();
    extraGIEnvSmoothing = extraGIEnvField.getValue();
    adaptive = adaptiveBox.getState();
    roulette = rouletteBox.getState();
    // Show the window. 
    WindowWidget parent = UIUtilities.findWindow(ev.getWidget());
    PanelDialog dlg = new PanelDialog(parent, Translate.text("advancedOptions"), content);
    if (!dlg.clickedOk()) {
        // Reset the components. 
        rayDepthField.setValue(maxRayDepth);
        rayCutoffField.setValue(minRayIntensity);
        stepSizeField.setValue(stepSize);
        smoothField.setValue(smoothing);
        extraGIField.setValue(extraGISmoothing);
        extraGIEnvField.setValue(extraGIEnvSmoothing);
        adaptiveBox.setState(adaptive);
        rouletteBox.setState(roulette);
        reducedMemoryBox.setState(reducedMemory);
    }
}
