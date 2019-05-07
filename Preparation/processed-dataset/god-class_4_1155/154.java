/**
     * Starts the GUI by creating and displaying the GUI-objects.
     */
public void startGUI(Dimension innerWindowSize, final boolean sound, final boolean showOpeningVideo, final boolean loadGame) {
    final ClientOptions opts = freeColClient.getClientOptions();
    // Prepare the sound system. 
    if (sound) {
        final AudioMixerOption amo = (AudioMixerOption) opts.getOption(ClientOptions.AUDIO_MIXER);
        final PercentageOption volume = (PercentageOption) opts.getOption(ClientOptions.AUDIO_VOLUME);
        try {
            this.soundPlayer = new SoundPlayer(amo, volume);
        } catch (Exception e) {
            // #3168279 reports an undocumented NPE thrown by 
            // AudioSystem.getMixer(null).  Workaround this and other 
            // such failures by just disabling sound. 
            this.soundPlayer = null;
            logger.log(Level.WARNING, "Sound disabled", e);
        }
    } else {
        this.soundPlayer = null;
    }
    if (GraphicsEnvironment.isHeadless()) {
        logger.info("It seems that the GraphicsEnvironment is headless!");
    }
    this.gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    if (!isWindowed()) {
        if (!gd.isFullScreenSupported()) {
            String fullscreenNotSupported = "\nIt seems that full screen mode is not fully supported for this" + "\nGraphicsDevice. Please try the \"--windowed\" option if you\nexperience" + "any graphical problems while running FreeCol.";
            logger.info(fullscreenNotSupported);
            System.out.println(fullscreenNotSupported);
        }
        Rectangle bounds = gd.getDefaultConfiguration().getBounds();
        innerWindowSize = new Dimension(bounds.width - bounds.x, bounds.height - bounds.y);
    }
    // Work around a Java 2D bug that seems to be X11 specific. 
    // According to: 
    //   http://www.oracle.com/technetwork/java/javase/index-142560.html 
    // 
    //   ``The use of pixmaps typically results in better 
    //     performance. However, in certain cases, the opposite is true.'' 
    // 
    // The standard workaround is to use -Dsun.java2d.pmoffscreen=false, 
    // but this is too hard for some users, so provide an option to 
    // do it easily.  However respect the initial value if present. 
    // 
    // Remove this if Java 2D is ever fixed.  DHYB. 
    // 
    final String pmoffscreen = "sun.java2d.pmoffscreen";
    BooleanOption usePixmaps = (BooleanOption) opts.getOption(ClientOptions.USE_PIXMAPS);
    String pmoffscreenValue = System.getProperty(pmoffscreen);
    if (pmoffscreenValue == null) {
        System.setProperty(pmoffscreen, usePixmaps.getValue().toString());
        logger.info(pmoffscreen + " using client option: " + usePixmaps.getValue().toString());
    } else {
        usePixmaps.setValue(new Boolean(pmoffscreenValue));
        logger.info(pmoffscreen + " overrides client option: " + pmoffscreenValue);
    }
    usePixmaps.addPropertyChangeListener(new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent e) {
            String newValue = e.getNewValue().toString();
            System.setProperty(pmoffscreen, newValue);
            logger.info("Set " + pmoffscreen + " to: " + newValue);
        }
    });
    this.mapViewer = new MapViewer(freeColClient, this, innerWindowSize, imageLibrary);
    this.canvas = new Canvas(freeColClient, this, innerWindowSize, mapViewer);
    this.colonyTileGUI = new MapViewer(freeColClient, this, innerWindowSize, imageLibrary);
    changeWindowedMode(isWindowed());
    frame.setIconImage(ResourceManager.getImage("FrameIcon.image"));
    // Now that there is a canvas, prepare for language changes. 
    LanguageOption o = (LanguageOption) freeColClient.getClientOptions().getOption(ClientOptions.LANGUAGE);
    if (o != null) {
        o.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                if (((Language) e.getNewValue()).getKey().equals(LanguageOption.AUTO)) {
                    showInformationMessage("autodetectLanguageSelected");
                } else {
                    Locale l = ((Language) e.getNewValue()).getLocale();
                    Messages.setMessageBundle(l);
                    Messages.setModMessageBundle(l);
                    showInformationMessage(StringTemplate.template("newLanguageSelected").addName("%language%", l.getDisplayName()));
                }
            }
        });
    }
    // run opening video or main panel 
    if (showOpeningVideo && !loadGame) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                canvas.showOpeningVideoPanel();
            }
        });
    } else {
        if (!loadGame) {
            showMainPanel();
        }
        playSound("sound.intro.general");
    }
    mapViewer.startCursorBlinking();
}
