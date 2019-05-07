/**
   * Adjust the additive and multiplicative colors for a pixel based on the material it is
   * passing through.
   */
private void adjustColorsForMaterial(ObjectMaterialInfo material, int x, int y, float startDepth, float endDepth, RGBColor addColor, RGBColor multColor, CompositingContext context) {
    if (material == null) {
        // It is passing through empty space, so taking fog into account. 
        if (fog) {
            float fract1 = (float) Math.exp((startDepth - endDepth) / fogDist), fract2 = 1.0f - fract1;
            multColor.setRGB(fract1, fract1, fract1);
            addColor.setRGB(fract1 * addColor.getRed() + fract2 * fogColor.getRed(), fract1 * addColor.getGreen() + fract2 * fogColor.getGreen(), fract1 * addColor.getBlue() + fract2 * fogColor.getBlue());
        } else {
            addColor.setRGB(0.0f, 0.0f, 0.0f);
            multColor.setRGB(1.0f, 1.0f, 1.0f);
        }
        return;
    }
    if (material.getMapping() instanceof UniformMaterialMapping) {
        // A uniform material, so we can calculate the effect exactly. 
        material.getMapping().getMaterialSpec(context.tempVec[0], context.matSpec, 0.0, time);
        RGBColor trans = context.matSpec.transparency, blend = context.matSpec.color;
        double dist = endDepth - startDepth;
        float rs = (float) Math.pow(trans.getRed(), dist);
        float gs = (float) Math.pow(trans.getGreen(), dist);
        float bs = (float) Math.pow(trans.getBlue(), dist);
        multColor.setRGB(rs, gs, bs);
        addColor.setRGB(rs * addColor.getRed() + (1.0f - rs) * blend.getRed(), gs * addColor.getGreen() + (1.0f - gs) * blend.getGreen(), bs * addColor.getBlue() + (1.0f - bs) * blend.getBlue());
        return;
    }
    // Step through the material and add up the contribution at each point. 
    Vec2 imagePos = new Vec2(x, y);
    Vec3 startPoint = context.camera.convertScreenToWorld(imagePos, startDepth, false);
    Vec3 endPoint = context.camera.convertScreenToWorld(imagePos, endDepth, false);
    double distToPoint = context.camera.getCameraCoordinates().getOrigin().distance(startPoint);
    material.getToLocal().transform(startPoint);
    material.getToLocal().transform(endPoint);
    double dist = startPoint.distance(endPoint);
    double stepSize = material.getMapping().getStepSize();
    double distToScreen = context.camera.getDistToScreen();
    if (distToPoint > distToScreen)
        stepSize *= distToPoint / distToScreen;
    int steps = FastMath.ceil(dist / stepSize);
    stepSize = dist / steps;
    multColor.setRGB(1.0f, 1.0f, 1.0f);
    addColor.setRGB(0.0f, 0.0f, 0.0f);
    for (int i = 0; i < steps; i++) {
        double fract2 = (0.5 + i) / steps, fract1 = 1.0 - fract2;
        context.tempVec[0].set(fract1 * startPoint.x + fract2 * endPoint.x, fract1 * startPoint.y + fract2 * endPoint.y, fract1 * startPoint.z + fract2 * endPoint.z);
        material.getMapping().getMaterialSpec(context.tempVec[0], context.matSpec, stepSize, time);
        RGBColor trans = context.matSpec.transparency, blend = context.matSpec.color;
        float rs = (float) Math.pow(trans.getRed(), stepSize);
        float gs = (float) Math.pow(trans.getGreen(), stepSize);
        float bs = (float) Math.pow(trans.getBlue(), stepSize);
        multColor.multiply(rs, gs, bs);
        addColor.setRGB(multColor.getRed() * addColor.getRed() + (1.0f - multColor.getRed()) * blend.getRed(), multColor.getGreen() * addColor.getGreen() + (1.0f - multColor.getGreen()) * blend.getGreen(), multColor.getBlue() * addColor.getBlue() + (1.0f - multColor.getBlue()) * blend.getBlue());
        if (multColor.getMaxComponent() < INTENSITY_CUTOFF) {
            // Nothing beyond this point is visible, so stop now. 
            multColor.setRGB(0.0f, 0.0f, 0.0f);
            return;
        }
    }
}
