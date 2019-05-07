private void update() {
    BufferedImage image = null;
    try {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Toolkit nativeToolkit = Tools.getDefaultToolkit();
        if (toolkit != nativeToolkit) {
            GraphicsDevice screenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            Robot robot = new Robot(screenDevice);
            RobotPeer peer = ((ComponentFactory) nativeToolkit).createRobot(robot, screenDevice);
            Dimension screenSize = nativeToolkit.getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            DataBufferInt buffer;
            WritableRaster raster;
            DirectColorModel screenCapCM = screenCapCM = new DirectColorModel(24, /* red mask */
            0x00FF0000, /* green mask */
            0x0000FF00, /* blue mask */
            0x000000FF);
            int pixels[];
            int[] bandmasks = new int[3];
            pixels = peer.getRGBPixels(screenRect);
            buffer = new DataBufferInt(pixels, pixels.length);
            bandmasks[0] = screenCapCM.getRedMask();
            bandmasks[1] = screenCapCM.getGreenMask();
            bandmasks[2] = screenCapCM.getBlueMask();
            raster = Raster.createPackedRaster(buffer, screenRect.width, screenRect.height, screenRect.width, bandmasks, null);
            image = new BufferedImage(screenCapCM, raster, false, null);
        } else {
            Dimension screenSize = toolkit.getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            // create screen shot 
            Robot robot = new Robot();
            image = robot.createScreenCapture(screenRect);
        }
    } catch (Exception ex) {
        Tools.logException(Desktop.class, ex);
    }
    try {
        setPainting(false);
        if (image != null && getApp().getContext() != null) {
            if (image.getWidth() > 640 || image.getHeight() > 480) {
                BufferedImage scaled = ImageManipulator.getScaledImage(image, 640, 480);
                image.flush();
                image = null;
                getNormal().setResource(createImage(scaled));
                scaled.flush();
                scaled = null;
            } else {
                getNormal().setResource(createImage(image), RSRC_IMAGE_BESTFIT);
                image.flush();
                image = null;
            }
        }
    } finally {
        setPainting(true);
    }
    getNormal().flush();
}
