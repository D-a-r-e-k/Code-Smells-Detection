/** Update the image being displayed. */
private synchronized void updateImage() {
    if (System.currentTimeMillis() - updateTime < 5000)
        return;
    RGBColor frontColor = new RGBColor();
    for (int i1 = 0, i2 = 0; i1 < imageHeight; i1++, i2 += samplesPerPixel) for (int j1 = 0, j2 = 0; j1 < imageWidth; j1++, j2 += samplesPerPixel) {
        fragment[i2 * width + j2].getAdditiveColor(frontColor);
        imagePixel[i1 * imageWidth + j1] = frontColor.getARGB();
    }
    imageSource.newPixels();
    listener.imageUpdated(img);
    updateTime = System.currentTimeMillis();
}
