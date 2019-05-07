/** Update the image being displayed during compositing. */
private synchronized void updateFinalImage() {
    if (System.currentTimeMillis() - updateTime < 5000)
        return;
    imageSource.newPixels();
    listener.imageUpdated(img);
    updateTime = System.currentTimeMillis();
}
