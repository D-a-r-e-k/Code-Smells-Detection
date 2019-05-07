public synchronized void renderScene(Scene theScene, Camera theCamera, RenderListener rl, SceneCamera sceneCamera) {
    Dimension dim = theCamera.getSize();
    listener = rl;
    this.theScene = theScene;
    this.theCamera = theCamera;
    if (sceneCamera == null) {
        sceneCamera = new SceneCamera();
        sceneCamera.setDepthOfField(0.0);
        sceneCamera.setFocalDistance(theCamera.getDistToScreen());
    }
    this.sceneCamera = sceneCamera;
    time = theScene.getTime();
    width = dim.width;
    height = dim.height;
    pixel = new int[width * height];
    imageSource = new MemoryImageSource(width, height, pixel, 0, width);
    imageSource.setAnimated(true);
    img = Toolkit.getDefaultToolkit().createImage(imageSource);
    int requiredComponents = sceneCamera.getComponentsForFilters();
    if (generateHDR || (requiredComponents & (ComplexImage.RED + ComplexImage.GREEN + ComplexImage.BLUE)) != 0)
        floatImage = new float[4][width * height];
    if ((requiredComponents & ComplexImage.DEPTH) != 0)
        depthImage = new float[width * height];
    if ((requiredComponents & ComplexImage.NOISE) != 0)
        errorImage = new float[width * height];
    if ((requiredComponents & ComplexImage.OBJECT) != 0)
        objectImage = new float[width * height];
    renderThread = new Thread(this, "Raytracer main thread");
    renderThread.setPriority(Thread.NORM_PRIORITY);
    renderThread.start();
}
