public Widget getConfigPanel() {
    if (configPanel == null) {
        configPanel = new FormContainer(3, 5);
        LayoutInfo leftLayout = new LayoutInfo(LayoutInfo.EAST, LayoutInfo.NONE, new Insets(0, 0, 0, 5), null);
        LayoutInfo rightLayout = new LayoutInfo(LayoutInfo.WEST, LayoutInfo.NONE, null, null);
        configPanel.add(Translate.label("surfaceAccuracy"), 0, 0, leftLayout);
        configPanel.add(Translate.label("shadingMethod"), 0, 1, leftLayout);
        configPanel.add(Translate.label("supersampling"), 0, 2, leftLayout);
        configPanel.add(errorField = new ValueField(surfaceError, ValueField.POSITIVE, 6), 1, 0, rightLayout);
        configPanel.add(shadeChoice = new BComboBox(new String[] { Translate.text("gouraud"), Translate.text("hybrid"), Translate.text("phong") }), 1, 1, rightLayout);
        configPanel.add(aliasChoice = new BComboBox(new String[] { Translate.text("none"), Translate.text("Edges"), Translate.text("Everything") }), 1, 2, rightLayout);
        configPanel.add(sampleChoice = new BComboBox(new String[] { "2x2", "3x3" }), 2, 2, rightLayout);
        sampleChoice.setEnabled(false);
        configPanel.add(transparentBox = new BCheckBox(Translate.text("transparentBackground"), transparentBackground), 0, 3, 3, 1);
        configPanel.add(Translate.button("advanced", this, "showAdvancedWindow"), 0, 4, 3, 1);
        smoothField = new ValueField(smoothing, ValueField.NONNEGATIVE);
        adaptiveBox = new BCheckBox(Translate.text("reduceAccuracyForDistant"), adaptive);
        hideBackfaceBox = new BCheckBox(Translate.text("eliminateBackfaces"), hideBackfaces);
        hdrBox = new BCheckBox(Translate.text("generateHDR"), generateHDR);
        aliasChoice.addEventLink(ValueChangedEvent.class, new Object() {

            void processEvent() {
                sampleChoice.setEnabled(aliasChoice.getSelectedIndex() > 0);
            }
        });
    }
    if (needCopyToUI)
        copyConfigurationToUI();
    return configPanel;
}
