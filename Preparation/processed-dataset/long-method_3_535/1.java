private void renderBucket(Display display, int bx, int by, int threadID, IntersectionState istate) {
    // pixel sized extents  
    int x0 = bx * bucketSize;
    int y0 = by * bucketSize;
    int bw = Math.min(bucketSize, imageWidth - x0);
    int bh = Math.min(bucketSize, imageHeight - y0);
    // prepare bucket  
    display.imagePrepare(x0, y0, bw, bh, threadID);
    Color[] bucketRGB = new Color[bw * bh];
    // subpixel extents  
    int sx0 = x0 * subPixelSize - fs;
    int sy0 = y0 * subPixelSize - fs;
    int sbw = bw * subPixelSize + fs * 2;
    int sbh = bh * subPixelSize + fs * 2;
    // round up to align with maximum step size  
    sbw = (sbw + (maxStepSize - 1)) & (~(maxStepSize - 1));
    sbh = (sbh + (maxStepSize - 1)) & (~(maxStepSize - 1));
    // extra padding as needed  
    if (maxStepSize > 1) {
        sbw++;
        sbh++;
    }
    // allocate bucket memory  
    ImageSample[] samples = new ImageSample[sbw * sbh];
    // allocate samples and compute jitter offsets  
    float invSubPixelSize = 1.0f / subPixelSize;
    for (int y = 0, index = 0; y < sbh; y++) {
        for (int x = 0; x < sbw; x++, index++) {
            int sx = sx0 + x;
            int sy = sy0 + y;
            int j = sx & (sigma.length - 1);
            int k = sy & (sigma.length - 1);
            int i = j * sigma.length + sigma[k];
            float dx = useJitter ? (float) sigma[k] / (float) sigma.length : 0.5f;
            float dy = useJitter ? (float) sigma[j] / (float) sigma.length : 0.5f;
            float rx = (sx + dx) * invSubPixelSize;
            float ry = (sy + dy) * invSubPixelSize;
            ry = imageHeight - ry - 1;
            samples[index] = new ImageSample(rx, ry, i);
        }
    }
    for (int x = 0; x < sbw - 1; x += maxStepSize) for (int y = 0; y < sbh - 1; y += maxStepSize) refineSamples(samples, sbw, x, y, maxStepSize, thresh, istate);
    if (dumpBuckets) {
        UI.printInfo(Module.BCKT, "Dumping bucket [%d, %d] to file ...", bx, by);
        Bitmap bitmap = new Bitmap(sbw, sbh, true);
        for (int y = sbh - 1, index = 0; y >= 0; y--) for (int x = 0; x < sbw; x++, index++) bitmap.setPixel(x, y, samples[index].c.copy().toNonLinear());
        bitmap.save(String.format("bucket_%04d_%04d.png", bx, by));
    }
    if (displayAA) {
        // color coded image of what is visible  
        float invArea = invSubPixelSize * invSubPixelSize;
        for (int y = 0, index = 0; y < bh; y++) {
            for (int x = 0; x < bw; x++, index++) {
                int sampled = 0;
                for (int i = 0; i < subPixelSize; i++) {
                    for (int j = 0; j < subPixelSize; j++) {
                        int sx = x * subPixelSize + fs + i;
                        int sy = y * subPixelSize + fs + j;
                        int s = sx + sy * sbw;
                        sampled += samples[s].sampled() ? 1 : 0;
                    }
                }
                bucketRGB[index] = new Color(sampled * invArea);
            }
        }
    } else {
        // filter samples into pixels  
        float cy = imageHeight - 1 - (y0 + 0.5f);
        for (int y = 0, index = 0; y < bh; y++, cy--) {
            float cx = x0 + 0.5f;
            for (int x = 0; x < bw; x++, index++, cx++) {
                Color c = Color.black();
                float weight = 0.0f;
                for (int j = -fs, sy = y * subPixelSize; j <= fs; j++, sy++) {
                    for (int i = -fs, sx = x * subPixelSize, s = sx + sy * sbw; i <= fs; i++, sx++, s++) {
                        float dx = samples[s].rx - cx;
                        if (Math.abs(dx) > fhs)
                            continue;
                        float dy = samples[s].ry - cy;
                        if (Math.abs(dy) > fhs)
                            continue;
                        float f = filter.get(dx, dy);
                        c.madd(f, samples[s].c);
                        weight += f;
                    }
                }
                c.mul(1.0f / weight);
                bucketRGB[index] = c;
            }
        }
    }
    // update pixels  
    display.imageUpdate(x0, y0, bw, bh, bucketRGB);
}
