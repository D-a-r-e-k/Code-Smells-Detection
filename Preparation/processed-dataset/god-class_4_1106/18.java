/** Main method in which the image is rendered. */
public void run() {
    long updateTime = System.currentTimeMillis();
    Thread thisThread = Thread.currentThread();
    listener.statusChanged(Translate.text("Processing Scene"));
    buildScene(theScene, theCamera);
    if (renderThread != thisThread)
        return;
    buildTree();
    buildPhotonMap();
    listener.statusChanged(Translate.text("Rendering"));
    for (int i = 0; i < pixel.length; i++) pixel[i] = 0;
    int maxRaysInUse = maxRays;
    int minRaysInUse = minRays;
    if (antialiasLevel == 0)
        minRaysInUse = maxRaysInUse = 1;
    smoothScale = smoothing * 2.0 * Math.tan(sceneCamera.getFieldOfView() * Math.PI / 360.0) / height;
    // If we are only using one ray/pixel, everything is simple. 
    if (maxRaysInUse == 1) {
        rtWidth = width;
        rtHeight = height;
        final int currentRow[] = new int[1];
        ThreadManager threads = new ThreadManager(width, new ThreadManager.Task() {

            public void execute(int index) {
                RaytracerContext context = (RaytracerContext) threadContext.get();
                PixelInfo pixel = context.tempPixel;
                pixel.clear();
                pixel.depth = (float) spawnEyeRay(context, index, currentRow[0], 0, 1);
                pixel.object = context.firstObjectHit;
                pixel.add(context.color[0], (float) context.transparency[0]);
                recordPixel(index, currentRow[0], pixel);
            }

            public void cleanup() {
                ((RaytracerContext) threadContext.get()).cleanup();
            }
        });
        for (currentRow[0] = 0; currentRow[0] < height; currentRow[0]++) {
            threads.run();
            if (renderThread != thisThread) {
                threads.finish();
                return;
            }
            if (System.currentTimeMillis() - updateTime > 5000) {
                imageSource.newPixels();
                listener.imageUpdated(img);
                updateTime = System.currentTimeMillis();
            }
        }
        imageSource.newPixels();
        threads.finish();
        finish();
        return;
    }
    // We need to adaptively decide how many rays to use for each pixel.  To save memory, 
    // we only deal with six rows at a time.  Begin by sending minRays for each pixel.  If 
    // the results are not sufficiently converged for a given pixel, double the number of 
    // rays for that pixel, and every adjacent pixel.  Repeat until everything converges, 
    // or we reach maxRays. 
    PixelInfo tempPixel = new PixelInfo();
    rtWidth = 2 * width + 2;
    rtHeight = 2 * height + 2;
    smoothScale *= 0.5;
    final PixelInfo pix[][] = new PixelInfo[6][rtWidth];
    for (int i = 0; i < pix.length; i++) for (int j = 0; j < pix[i].length; j++) pix[i][j] = new PixelInfo();
    int minPerSubpixel = minRaysInUse / 4, maxPerSubpixel = maxRaysInUse / 4;
    final int currentRow[] = new int[1];
    final int currentCount[] = new int[1];
    ThreadManager threads = new ThreadManager(rtWidth, new ThreadManager.Task() {

        public void execute(int index) {
            RaytracerContext context = (RaytracerContext) threadContext.get();
            PixelInfo tempPixel = context.tempPixel;
            for (int m = 0; m < 6; m++) {
                PixelInfo thisPixel = pix[m][index];
                thisPixel.converged = true;
                if (thisPixel.needsMore) {
                    tempPixel.clear();
                    int baseNum = (m & 1) * 8 + (index & 1) * 4;
                    int numNeeded = currentCount[0] - thisPixel.raysSent;
                    for (int k = thisPixel.raysSent; k < currentCount[0]; k++) {
                        float dist = (float) spawnEyeRay(context, index, 2 * currentRow[0] + m, baseNum + k, numNeeded);
                        if (k < currentCount[0] / 2) {
                            thisPixel.add(context.color[0], (float) context.transparency[0]);
                            if (dist < thisPixel.depth) {
                                thisPixel.depth = dist;
                                thisPixel.object = context.firstObjectHit;
                            }
                        } else {
                            tempPixel.add(context.color[0], (float) context.transparency[0]);
                            if (dist < tempPixel.depth) {
                                tempPixel.depth = dist;
                                tempPixel.object = context.firstObjectHit;
                            }
                        }
                    }
                    if (currentCount[0] > 1)
                        thisPixel.converged = thisPixel.matches(tempPixel, COLOR_THRESH_ABS, COLOR_THRESH_REL);
                    thisPixel.add(tempPixel);
                }
            }
        }

        public void cleanup() {
            ((RaytracerContext) threadContext.get()).cleanup();
        }
    });
    for (currentRow[0] = 0; currentRow[0] < height - 1; currentRow[0]++) {
        // Keep refining the pixels in the current set of six rows until they converge, or 
        // we reach maxRays. 
        boolean done = false;
        for (currentCount[0] = minPerSubpixel; currentCount[0] <= maxPerSubpixel && !done; currentCount[0] *= 2) {
            // Send out more rays through any pixels which are marked as needing it. 
            threads.run();
            if (renderThread != thisThread) {
                threads.finish();
                return;
            }
            // If we have only sent out one ray per subpixel, we cannot yet judge the convergence of 
            // each one.  Instead, compare each pixel to its neighbors and use that to decide where 
            // we need more. 
            if (currentCount[0] == 1)
                for (int m = 0; m < 5; m++) for (int j = 0; j < rtWidth - 1; j++) {
                    if (!pix[m][j].matches(pix[m + 1][j], COLOR_THRESH_ABS, COLOR_THRESH_REL))
                        pix[m][j].converged = pix[m + 1][j].converged = false;
                    if (!pix[m][j].matches(pix[m][j + 1], COLOR_THRESH_ABS, COLOR_THRESH_REL))
                        pix[m][j].converged = pix[m][j + 1].converged = false;
                }
            // If a pixel has not yet converged, mark that pixel and all of its neighbors 
            // to get more rays. 
            for (int m = 0; m < 6; m++) for (int j = 0; j < rtWidth; j++) pix[m][j].needsMore = false;
            done = true;
            for (int m = 0; m < 6; m++) for (int j = 0; j < rtWidth; j++) if (!pix[m][j].converged) {
                done = false;
                pix[m][j].needsMore = true;
                if (m > 0)
                    pix[m - 1][j].needsMore = true;
                if (m < 5)
                    pix[m + 1][j].needsMore = true;
                if (j > 0)
                    pix[m][j - 1].needsMore = true;
                if (j < rtWidth - 1)
                    pix[m][j + 1].needsMore = true;
            }
        }
        // Copy the colors into the image, and update the image if enough time has elapsed. 
        recordRow(pix, tempPixel, currentRow[0]);
        if (System.currentTimeMillis() - updateTime > 5000) {
            imageSource.newPixels();
            listener.imageUpdated(img);
            updateTime = System.currentTimeMillis();
        }
        // Rotate the temporary pixel buffer by two rows. 
        PixelInfo temp1[] = pix[0], temp2[] = pix[1];
        for (int j = 0; j < 4; j++) pix[j] = pix[j + 2];
        pix[4] = temp1;
        pix[5] = temp2;
        for (int j = 0; j < rtWidth; j++) {
            pix[4][j].clear();
            pix[5][j].clear();
        }
    }
    // Copy the final row of pixels into the image. 
    recordRow(pix, tempPixel, height - 1);
    // All done.  Send the final image. 
    imageSource.newPixels();
    threads.finish();
    finish();
}
