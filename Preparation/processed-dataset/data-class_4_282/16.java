/** Create the final version of the image. */
private ComplexImage createFinalImage(final Vec3 center, final Vec3 orig, final Vec3 hvec, final Vec3 vvec) {
    final Thread thisThread = Thread.currentThread();
    if (renderThread != thisThread)
        return null;
    final int n = samplesPerPixel * samplesPerPixel;
    final float hdrImage[][] = (generateHDR ? new float[3][imageWidth * imageHeight] : null);
    ThreadManager threads = new ThreadManager(imageHeight, new ThreadManager.Task() {

        public void execute(int i1) {
            CompositingContext context = (CompositingContext) threadCompositingContext.get();
            Vec3 dir = context.tempVec[1];
            RGBColor totalColor = context.totalColor;
            RGBColor totalTransparency = context.totalTransparency;
            RGBColor addColor = context.addColor;
            RGBColor multColor = context.multColor;
            RGBColor subpixelColor = context.subpixelColor;
            RGBColor subpixelMult = context.subpixelMult;
            ArrayList<ObjectMaterialInfo> materialStack = context.materialStack;
            TextureSpec surfSpec = context.surfSpec;
            int i2 = i1 * samplesPerPixel;
            for (int j1 = 0, j2 = 0; j1 < imageWidth; j1++, j2 += samplesPerPixel) {
                totalColor.setRGB(0.0f, 0.0f, 0.0f);
                totalTransparency.setRGB(0.0f, 0.0f, 0.0f);
                for (int k = 0; k < samplesPerPixel; k++) {
                    int base = width * (i2 + k) + j2;
                    for (int m = 0; m < samplesPerPixel; m++) {
                        // Find the overall color of this subpixel. 
                        subpixelColor.setRGB(0.0f, 0.0f, 0.0f);
                        subpixelMult.setRGB(1.0f, 1.0f, 1.0f);
                        Fragment f = fragment[base + m];
                        float lastDepth = 0;
                        while (true) {
                            // Factor in materials. 
                            ObjectMaterialInfo fragmentMaterial = f.getMaterialMapping();
                            ObjectMaterialInfo currentMaterial = null;
                            if (materialStack.size() > 0)
                                currentMaterial = materialStack.get(materialStack.size() - 1);
                            adjustColorsForMaterial(currentMaterial, j2 + m, i2 + k, lastDepth, f.getDepth(), addColor, context.multColor, context);
                            addColor.multiply(subpixelMult);
                            subpixelColor.add(addColor);
                            subpixelMult.multiply(multColor);
                            if (fragmentMaterial != null) {
                                if (f.isEntering())
                                    materialStack.add(fragmentMaterial);
                                else
                                    materialStack.remove(fragmentMaterial);
                            }
                            lastDepth = f.getDepth();
                            // If we've reached the end, factor in the background. 
                            if (f == BACKGROUND_FRAGMENT) {
                                if (transparentBackground) {
                                    // Just make it invisible. 
                                    addColor.setRGB(0.0f, 0.0f, 0.0f);
                                } else if (envMode == Scene.ENVIRON_SOLID)
                                    addColor.copy(envColor);
                                else {
                                    // Find the background color. 
                                    double h = j2 + k - width / 2.0, v = i2 + m - height / 2.0;
                                    dir.x = center.x + h * hvec.x + v * vvec.x;
                                    dir.y = center.y + h * hvec.y + v * vvec.y;
                                    dir.z = center.z + h * hvec.z + v * vvec.z;
                                    dir.subtract(orig);
                                    dir.normalize();
                                    envMapping.getTextureSpec(dir, surfSpec, 1.0, smoothScale, time, envParamValue);
                                    if (envMode == Scene.ENVIRON_DIFFUSE)
                                        addColor.copy(surfSpec.diffuse);
                                    else
                                        addColor.copy(surfSpec.emissive);
                                }
                            } else
                                f.getAdditiveColor(addColor);
                            // Factor in the fragment color. 
                            addColor.multiply(subpixelMult);
                            subpixelColor.add(addColor);
                            if (f.isOpaque()) {
                                if (f != BACKGROUND_FRAGMENT || !transparentBackground)
                                    subpixelMult.setRGB(0.0f, 0.0f, 0.0f);
                                break;
                            }
                            f.getMultiplicativeColor(multColor);
                            subpixelMult.multiply(multColor);
                            f = f.getNextFragment();
                        }
                        totalColor.add(subpixelColor);
                        totalTransparency.add(subpixelMult);
                        materialStack.clear();
                    }
                }
                totalColor.scale(1.0f / n);
                totalTransparency.scale(1.0f / n);
                imagePixel[i1 * imageWidth + j1] = calcARGB(totalColor, totalTransparency);
                if (generateHDR) {
                    hdrImage[0][i1 * imageWidth + j1] = totalColor.getRed();
                    hdrImage[1][i1 * imageWidth + j1] = totalColor.getGreen();
                    hdrImage[2][i1 * imageWidth + j1] = totalColor.getBlue();
                }
            }
            if (renderThread != thisThread)
                return;
            if (System.currentTimeMillis() - updateTime > 5000)
                updateFinalImage();
        }

        public void cleanup() {
            ((CompositingContext) threadCompositingContext.get()).cleanup();
        }
    });
    threads.run();
    threads.finish();
    // Create the ComplexImage. 
    imageSource.newPixels();
    ComplexImage image = new ComplexImage(img);
    if (generateHDR) {
        image.setComponentValues(ComplexImage.RED, hdrImage[0]);
        image.setComponentValues(ComplexImage.GREEN, hdrImage[1]);
        image.setComponentValues(ComplexImage.BLUE, hdrImage[2]);
    }
    if (depthNeeded) {
        float imageZbuffer[] = new float[imageWidth * imageHeight];
        for (int i1 = 0, i2 = 0; i1 < imageHeight; i1++, i2 += samplesPerPixel) for (int j1 = 0, j2 = 0; j1 < imageWidth; j1++, j2 += samplesPerPixel) {
            float minDepth = Float.MAX_VALUE;
            for (int k = 0; k < samplesPerPixel; k++) {
                int base = width * (i2 + k) + j2;
                for (int m = 0; m < samplesPerPixel; m++) {
                    float z = fragment[base + m].getDepth();
                    if (z < minDepth)
                        minDepth = z;
                }
            }
            imageZbuffer[i1 * imageWidth + j1] = minDepth;
        }
        image.setComponentValues(ComplexImage.DEPTH, imageZbuffer);
    }
    return image;
}
