/** This routine is called when rendering is finished. */
private void finish(ComplexImage finalImage) {
    light = null;
    theScene = null;
    theCamera = null;
    envMapping = null;
    img = null;
    imagePixel = null;
    fragment = null;
    RenderListener rl = listener;
    listener = null;
    renderThread = null;
    if (rl != null && finalImage != null)
        rl.imageComplete(finalImage);
}
