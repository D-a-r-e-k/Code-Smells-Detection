protected void showIlluminationWindow(WidgetEvent ev) {
    // Layout the window. 
    ColumnContainer content = new ColumnContainer();
    LayoutInfo indent0 = new LayoutInfo(LayoutInfo.WEST, LayoutInfo.NONE, null, null);
    LayoutInfo indent1 = new LayoutInfo(LayoutInfo.WEST, LayoutInfo.NONE, new Insets(0, 20, 0, 0), null);
    RowContainer row;
    content.add(row = new RowContainer(), indent0);
    row.add(Translate.label("globalIllumination"));
    row.add(giModeChoice);
    content.add(row = new RowContainer(), indent1);
    row.add(Translate.label("raysToSampleEnvironment"));
    row.add(diffuseRaysChoice);
    content.add(row = new RowContainer(), indent1);
    row.add(Translate.label("totalPhotons"));
    row.add(globalPhotonsField);
    row.add(Translate.label("numToEstimateLight"));
    row.add(globalNeighborPhotonsField);
    content.add(causticsBox, indent0);
    content.add(row = new RowContainer(), indent1);
    row.add(Translate.label("totalPhotons"));
    row.add(causticsPhotonsField);
    row.add(Translate.label("numToEstimateLight"));
    row.add(causticsNeighborPhotonsField);
    content.add(row = new RowContainer(), indent0);
    row.add(Translate.label("materialScattering"));
    row.add(scatterModeChoice);
    content.add(row = new RowContainer(), indent1);
    row.add(Translate.label("totalPhotons"));
    row.add(volumePhotonsField);
    row.add(Translate.label("numToEstimateLight"));
    row.add(volumeNeighborPhotonsField);
    causticsBox.dispatchEvent(new ValueChangedEvent(causticsBox));
    // Record the current settings. 
    giMode = giModeChoice.getSelectedIndex();
    diffuseRays = Integer.parseInt((String) diffuseRaysChoice.getSelectedValue());
    globalPhotons = (int) globalPhotonsField.getValue();
    globalNeighborPhotons = (int) globalNeighborPhotonsField.getValue();
    caustics = causticsBox.getState();
    causticsPhotons = (int) causticsPhotonsField.getValue();
    causticsNeighborPhotons = (int) causticsNeighborPhotonsField.getValue();
    scatterMode = scatterModeChoice.getSelectedIndex();
    volumePhotons = (int) volumePhotonsField.getValue();
    volumeNeighborPhotons = (int) volumeNeighborPhotonsField.getValue();
    // Show the window. 
    WindowWidget parent = UIUtilities.findWindow(ev.getWidget());
    PanelDialog dlg = new PanelDialog(parent, Translate.text("illuminationOptions"), content);
    if (!dlg.clickedOk()) {
        // Reset the components. 
        giModeChoice.setSelectedIndex(giMode);
        diffuseRaysChoice.setSelectedValue(Integer.toString(diffuseRays));
        globalPhotonsField.setValue(globalPhotons);
        globalNeighborPhotonsField.setValue(globalNeighborPhotons);
        causticsBox.setState(caustics);
        causticsPhotonsField.setValue(causticsPhotons);
        causticsNeighborPhotonsField.setValue(causticsNeighborPhotons);
        scatterModeChoice.setSelectedIndex(scatterMode);
        volumePhotonsField.setValue(volumePhotons);
        volumeNeighborPhotonsField.setValue(volumeNeighborPhotons);
    }
}
