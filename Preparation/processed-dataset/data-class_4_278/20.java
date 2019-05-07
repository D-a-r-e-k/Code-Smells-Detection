/** Record a single pixel into the image. */
protected void recordPixel(int x, int y, PixelInfo pix) {
    int index = x + y * width;
    pixel[index] = pix.calcARGB();
    if (floatImage != null) {
        float ninv = 1.0f / pix.raysSent;
        floatImage[0][index] = pix.red * ninv;
        floatImage[1][index] = pix.green * ninv;
        floatImage[2][index] = pix.blue * ninv;
        floatImage[3][index] = 1.0f - pix.transparency * ninv;
    }
    if (depthImage != null)
        depthImage[index] = pix.depth;
    if (objectImage != null)
        objectImage[index] = (pix.object == null ? 0.0f : Float.intBitsToFloat(pix.object.getObject().hashCode()));
}
