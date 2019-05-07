/** Record a row of pixels into the image. */
protected void recordRow(PixelInfo pix[][], PixelInfo tempPixel, int row) {
    for (int i = 0; i < width; i++) {
        int x = i * 2 + 1;
        tempPixel.copy(pix[1][x]);
        tempPixel.add(pix[1][x + 1]);
        tempPixel.add(pix[2][x]);
        tempPixel.add(pix[2][x + 1]);
        if (antialiasLevel == 2) {
            tempPixel.add(tempPixel);
            tempPixel.add(pix[0][x]);
            tempPixel.add(pix[0][x + 1]);
            tempPixel.add(pix[3][x]);
            tempPixel.add(pix[3][x + 1]);
            tempPixel.add(pix[1][x - 1]);
            tempPixel.add(pix[2][x - 1]);
            tempPixel.add(pix[1][x + 2]);
            tempPixel.add(pix[2][x + 2]);
        }
        recordPixel(i, row, tempPixel);
        if (errorImage != null) {
            // If we only have one ray/subpixel, we need to estimate standard deviation from the differences between subpixels. 
            if (pix[1][x].raysSent + pix[1][x + 1].raysSent + pix[2][x].raysSent + pix[2][x + 1].raysSent == 4) {
                float ninvTotal = 1.0f / tempPixel.raysSent;
                PixelInfo p1 = pix[1][x];
                PixelInfo p2 = pix[1][x + 1];
                PixelInfo p3 = pix[2][x];
                PixelInfo p4 = pix[2][x + 1];
                float ninv1 = 1.0f / p1.raysSent;
                float ninv2 = 1.0f / p2.raysSent;
                float ninv3 = 1.0f / p3.raysSent;
                float ninv4 = 1.0f / p4.raysSent;
                float r = tempPixel.red * ninvTotal;
                float g = tempPixel.green * ninvTotal;
                float b = tempPixel.blue * ninvTotal;
                errorImage[i + row * width] = ((p1.red * ninv1 - r) * (p1.red * ninv1 - r) + (p1.green * ninv1 - g) * (p1.green * ninv1 - g) + (p1.blue * ninv1 - b) * (p1.blue * ninv1 - b) + (p2.red * ninv2 - r) * (p2.red * ninv2 - r) + (p2.green * ninv2 - g) * (p2.green * ninv2 - g) + (p2.blue * ninv2 - b) * (p2.blue * ninv2 - b) + (p3.red * ninv3 - r) * (p2.red * ninv3 - r) + (p3.green * ninv3 - g) * (p3.green * ninv3 - g) + (p3.blue * ninv3 - b) * (p3.blue * ninv3 - b) + (p4.red * ninv4 - r) * (p3.red * ninv4 - r) + (p4.green * ninv4 - g) * (p4.green * ninv4 - g) + (p4.blue * ninv4 - b) * (p4.blue * ninv4 - b)) / 12.0f;
            } else {
                int degreesOfFreedom = (antialiasLevel == 2 ? tempPixel.raysSent / 2 : tempPixel.raysSent);
                errorImage[i + row * width] = (tempPixel.getRedVariance() + tempPixel.getGreenVariance() + tempPixel.getBlueVariance()) / (3.0f * degreesOfFreedom);
            }
        }
    }
}
