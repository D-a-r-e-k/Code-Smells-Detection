/** This routine is called when rendering is finished.  It sets variables to null and
     runs a garbage collection. */
protected void finish() {
    sceneObject = null;
    light = null;
    rootNode = null;
    cameraNode = null;
    lightNode = null;
    theScene = null;
    theCamera = null;
    envMapping = null;
    renderThread = null;
    globalMap = null;
    causticsMap = null;
    volumeMap = null;
    RenderListener rl = listener;
    ComplexImage im = new ComplexImage(img);
    if (rl != null) {
        if (floatImage != null) {
            im.setComponentValues(ComplexImage.RED, floatImage[0]);
            im.setComponentValues(ComplexImage.GREEN, floatImage[1]);
            im.setComponentValues(ComplexImage.BLUE, floatImage[2]);
            im.setComponentValues(ComplexImage.ALPHA, floatImage[3]);
        }
        if (depthImage != null)
            im.setComponentValues(ComplexImage.DEPTH, depthImage);
        if (objectImage != null)
            im.setComponentValues(ComplexImage.OBJECT, objectImage);
        if (errorImage != null)
            im.setComponentValues(ComplexImage.NOISE, errorImage);
        listener = null;
    }
    img = null;
    imageSource = null;
    pixel = null;
    floatImage = null;
    depthImage = null;
    errorImage = null;
    objectImage = null;
    System.gc();
    if (rl != null)
        rl.imageComplete(im);
}
