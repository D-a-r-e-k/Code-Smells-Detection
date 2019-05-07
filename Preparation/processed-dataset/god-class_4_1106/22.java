/** This routine sends out a new ray, starting from the viewpoint and passing through
      pixel (i, j).  Number indicates which ray this is within the pixel, and is used for
      distribution ray tracing.  The light color is returned in color[0], and the
      transparency in transparency[0]. */
protected double spawnEyeRay(RaytracerContext rt, int i, int j, int number, int outOf) {
    Ray ray = rt.ray[0];
    Vec3 orig = ray.getOrigin(), dir = ray.getDirection();
    double h = i - rtWidth * 0.5 + 0.5, v = j - rtHeight * 0.5 + 0.5;
    if (antialiasLevel > 0) {
        int rows = FastMath.ceil(Math.sqrt(outOf));
        int cols = outOf / rows;
        int num = number % outOf;
        int row = num / cols;
        int col = num - row * cols;
        h += (col + rt.random.nextDouble()) / cols - 0.5;
        v += (row + rt.random.nextDouble()) / rows - 0.5;
    }
    double dof1 = 0.0, dof2 = 0.0;
    if (depth) {
        dof1 = 0.25 * (rt.random.nextDouble() + distrib1[number & 15]);
        dof2 = 0.25 * (rt.random.nextDouble() + distrib2[number & 15]);
    }
    sceneCamera.getRayFromCamera(h / rtHeight, v / rtHeight, dof1, dof2, orig, dir);
    theCamera.getCameraCoordinates().fromLocal().transform(orig);
    theCamera.getCameraCoordinates().fromLocal().transformDirection(dir);
    ray.newID();
    rt.rayIntensity[0].setRGB(1.0f, 1.0f, 1.0f);
    rt.firstObjectHit = null;
    double distScale = 1.0 / dir.dot(theCamera.getCameraCoordinates().getZDirection());
    OctreeNode node = cameraNode;
    if (node == null)
        node = rootNode.findFirstNode(ray);
    if (node == null) {
        RGBColor color = rt.color[0];
        TextureSpec surfSpec = rt.surfSpec[0];
        if (transparentBackground) {
            rt.transparency[0] = 1.0;
            color.setRGB(0.0f, 0.0f, 0.0f);
            return Float.MAX_VALUE;
        }
        if (envMode == Scene.ENVIRON_SOLID) {
            color.copy(envColor);
            return Float.MAX_VALUE;
        }
        envMapping.getTextureSpec(ray.direction, surfSpec, 1.0, smoothScale, time, envParamValue);
        if (envMode == Scene.ENVIRON_DIFFUSE)
            color.copy(surfSpec.diffuse);
        else
            color.copy(surfSpec.emissive);
        return Float.MAX_VALUE;
    }
    if (!rt.materialAtCameraIsFixed) {
        rt.materialAtCamera = getMaterialAtPoint(rt, orig, node);
        rt.materialAtCameraIsFixed = !depth;
    }
    if (rt.materialAtCamera == null)
        return distScale * spawnRay(rt, 0, node, null, null, null, null, null, number, 0.0, true, false);
    return distScale * spawnRay(rt, 0, node, null, rt.materialAtCamera.getMaterialMapping(), null, rt.materialAtCamera.toLocal(), null, number, 0.0, true, false);
}
