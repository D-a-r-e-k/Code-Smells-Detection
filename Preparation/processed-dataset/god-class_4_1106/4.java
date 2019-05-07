public Widget getConfigPanel() {
    if (configPanel == null) {
        configPanel = new ColumnContainer();
        FormContainer choicesPanel = new FormContainer(2, 4);
        configPanel.add(choicesPanel);
        LayoutInfo leftLayout = new LayoutInfo(LayoutInfo.EAST, LayoutInfo.NONE, new Insets(0, 0, 0, 5), null);
        LayoutInfo rightLayout = new LayoutInfo(LayoutInfo.WEST, LayoutInfo.NONE, null, null);
        choicesPanel.add(Translate.label("surfaceAccuracy"), 0, 0, leftLayout);
        choicesPanel.add(new BLabel(Translate.text("Antialiasing") + ":"), 0, 1, leftLayout);
        choicesPanel.add(Translate.label("minRaysPixel"), 0, 2, leftLayout);
        choicesPanel.add(Translate.label("maxRaysPixel"), 0, 3, leftLayout);
        choicesPanel.add(errorField = new ValueField(surfaceError, ValueField.POSITIVE, 6), 1, 0, rightLayout);
        choicesPanel.add(aliasChoice = new BComboBox(new String[] { Translate.text("none"), Translate.text("Medium"), Translate.text("Maximum") }), 1, 1, rightLayout);
        choicesPanel.add(minRaysChoice = new BComboBox(), 1, 2, rightLayout);
        choicesPanel.add(maxRaysChoice = new BComboBox(), 1, 3, rightLayout);
        for (int i = 4; i <= 1024; i *= 2) {
            minRaysChoice.add(Integer.toString(i));
            maxRaysChoice.add(Integer.toString(i));
        }
        ColumnContainer boxes = new ColumnContainer();
        configPanel.add(boxes);
        boxes.setDefaultLayout(new LayoutInfo(LayoutInfo.WEST, LayoutInfo.NONE, null, null));
        boxes.add(depthBox = new BCheckBox(Translate.text("depthOfField"), depth));
        boxes.add(glossBox = new BCheckBox(Translate.text("glossTranslucency"), gloss));
        boxes.add(shadowBox = new BCheckBox(Translate.text("softShadows"), penumbra));
        // Create components for the Illumination Options window. 
        RowContainer buttons = new RowContainer();
        configPanel.add(buttons);
        buttons.add(Translate.button("illumination", this, "showIlluminationWindow"));
        giModeChoice = new BComboBox(new String[] { Translate.text("none"), Translate.text("ambientOcclusion"), Translate.text("monteCarlo"), Translate.text("photonMappingDirect"), Translate.text("photonMappingFinalGather") });
        scatterModeChoice = new BComboBox(new String[] { Translate.text("singleScattering"), Translate.text("photonMapping"), Translate.text("Both") });
        diffuseRaysChoice = new BComboBox();
        diffuseRaysChoice.add("1");
        for (int i = 4; i <= 64; i *= 2) diffuseRaysChoice.add(Integer.toString(i));
        globalPhotonsField = new ValueField(globalPhotons, ValueField.POSITIVE + ValueField.INTEGER, 7);
        globalNeighborPhotonsField = new ValueField(globalNeighborPhotons, ValueField.POSITIVE + ValueField.INTEGER, 4);
        causticsPhotonsField = new ValueField(causticsPhotons, ValueField.POSITIVE + ValueField.INTEGER, 7);
        causticsNeighborPhotonsField = new ValueField(causticsNeighborPhotons, ValueField.POSITIVE + ValueField.INTEGER, 4);
        volumePhotonsField = new ValueField(volumePhotons, ValueField.POSITIVE + ValueField.INTEGER, 7);
        volumeNeighborPhotonsField = new ValueField(volumeNeighborPhotons, ValueField.POSITIVE + ValueField.INTEGER, 4);
        causticsBox = new BCheckBox(Translate.text("useCausticsMap"), caustics);
        // Create components for the Output Options window. 
        buttons.add(Translate.button("output", this, "showOutputOptionsWindow"));
        transparentBox = new BCheckBox(Translate.text("transparentBackground"), transparentBackground);
        hdrBox = new BCheckBox(Translate.text("generateHDR"), generateHDR);
        // Create components for the Advanced Options window. 
        buttons.add(Translate.button("advanced", this, "showAdvancedOptionsWindow"));
        rayDepthField = new ValueField(maxRayDepth, ValueField.POSITIVE + ValueField.INTEGER);
        rayCutoffField = new ValueField(minRayIntensity, ValueField.NONNEGATIVE);
        smoothField = new ValueField(smoothing, ValueField.NONNEGATIVE);
        extraGIField = new ValueField(extraGISmoothing, ValueField.POSITIVE);
        extraGIEnvField = new ValueField(extraGIEnvSmoothing, ValueField.POSITIVE);
        stepSizeField = new ValueField(stepSize, ValueField.POSITIVE);
        adaptiveBox = new BCheckBox(Translate.text("reduceAccuracyForDistant"), adaptive);
        rouletteBox = new BCheckBox(Translate.text("russianRoulette"), roulette);
        reducedMemoryBox = new BCheckBox(Translate.text("useLessMemory"), reducedMemory);
        // Set up listeners for components. 
        Object raysListener = new Object() {

            void processEvent(WidgetEvent ev) {
                boolean multi = (aliasChoice.getSelectedIndex() > 0);
                depthBox.setEnabled(multi);
                glossBox.setEnabled(multi);
                shadowBox.setEnabled(multi);
                minRaysChoice.setEnabled(multi);
                maxRaysChoice.setEnabled(multi);
                if (minRaysChoice.getSelectedIndex() > maxRaysChoice.getSelectedIndex()) {
                    if (ev.getWidget() == maxRaysChoice)
                        minRaysChoice.setSelectedIndex(maxRaysChoice.getSelectedIndex());
                    else
                        maxRaysChoice.setSelectedIndex(minRaysChoice.getSelectedIndex());
                }
            }
        };
        aliasChoice.addEventLink(ValueChangedEvent.class, raysListener);
        minRaysChoice.addEventLink(ValueChangedEvent.class, raysListener);
        maxRaysChoice.addEventLink(ValueChangedEvent.class, raysListener);
        aliasChoice.dispatchEvent(new ValueChangedEvent(aliasChoice));
        Object illumListener = new Object() {

            void processEvent() {
                int mode = giModeChoice.getSelectedIndex();
                UIUtilities.setEnabled(diffuseRaysChoice.getParent(), mode == GI_MONTE_CARLO || mode == GI_HYBRID || mode == GI_AMBIENT_OCCLUSION);
                UIUtilities.setEnabled(globalPhotonsField.getParent(), mode == GI_PHOTON || mode == GI_HYBRID);
                UIUtilities.setEnabled(causticsPhotonsField.getParent(), causticsBox.getState());
                UIUtilities.setEnabled(volumePhotonsField.getParent(), scatterModeChoice.getSelectedIndex() > 0);
            }
        };
        giModeChoice.addEventLink(ValueChangedEvent.class, illumListener);
        causticsBox.addEventLink(ValueChangedEvent.class, illumListener);
        scatterModeChoice.addEventLink(ValueChangedEvent.class, illumListener);
    }
    if (needCopyToUI)
        copyConfigurationToUI();
    return configPanel;
}
