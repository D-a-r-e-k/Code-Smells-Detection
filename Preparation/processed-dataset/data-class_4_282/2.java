public synchronized void renderScene(Scene theScene, Camera camera, RenderListener rl, SceneCamera sceneCamera) {
    Dimension dim = camera.getSize();
    listener = rl;
    this.theScene = theScene;
    theCamera = camera.duplicate();
    if (sceneCamera == null) {
        sceneCamera = new SceneCamera();
        sceneCamera.setDepthOfField(0.0);
        sceneCamera.setFocalDistance(theCamera.getDistToScreen());
    }
    focalDist = sceneCamera.getFocalDistance();
    depthNeeded = ((sceneCamera.getComponentsForFilters() & ComplexImage.DEPTH) != 0);
    time = theScene.getTime();
    if (imagePixel == null || imageWidth != dim.width || imageHeight != dim.height) {
        imageWidth = dim.width;
        imageHeight = dim.height;
        imagePixel = new int[imageWidth * imageHeight];
        imageSource = new MemoryImageSource(imageWidth, imageHeight, imagePixel, 0, imageWidth);
        imageSource.setAnimated(true);
        img = Toolkit.getDefaultToolkit().createImage(imageSource);
    }
    width = imageWidth * samplesPerPixel;
    height = imageHeight * samplesPerPixel;
    fragment = new Fragment[width * height];
    Arrays.fill(fragment, BACKGROUND_FRAGMENT);
    theCamera.setScreenTransform(sceneCamera.getScreenTransform(width, height), width, height);
    lock = new RowLock[height];
    for (int i = 0; i < lock.length; i++) lock[i] = new RowLock();
    renderThread = new Thread(this, "Raster Renderer Main Thread");
    renderThread.start();
}
