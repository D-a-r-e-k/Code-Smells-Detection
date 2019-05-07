/** Propagate a light ray through a Material, and determine how much light is removed.

      @param r          the ray being traced
      @param node       the octree node containing the point at which to start propagating
      @param startDist  the distance along the ray at which to start propagating
      @param endDist    the distance along the ray at which to stop propagating
      @param material   the MaterialMapping through which the ray is passing
      @param filter     on exit, this is multiplied by the attenuation factor
      @param toLocal    the transformation from world coordinates to the material's local coordinates.
      @param totalDist  the distance traveled from the viewpoint */
protected void propagateLightRay(Ray r, OctreeNode node, double startDist, double endDist, MaterialMapping material, RGBColor filter, Mat4 toLocal, double totalDist) {
    float rf = filter.getRed(), gf = filter.getGreen(), bf = filter.getBlue();
    MaterialSpec matSpec = r.rt.matSpec;
    if (material instanceof UniformMaterialMapping) {
        // The effects of the material can be computed exactly. 
        material.getMaterialSpec(r.origin, matSpec, 0.0, time);
        RGBColor trans = matSpec.transparency;
        float d = (float) (endDist - startDist);
        if (trans.getRed() != 1.0f)
            rf *= (float) Math.pow(trans.getRed(), d);
        if (trans.getGreen() != 1.0f)
            gf *= (float) Math.pow(trans.getGreen(), d);
        if (trans.getBlue() != 1.0f)
            bf *= (float) Math.pow(trans.getBlue(), d);
    } else {
        // Integrate the material properties by stepping along the ray. 
        Vec3 v = r.rt.ray[maxRayDepth].origin;
        double x = startDist, newx, dx, distToScreen = theCamera.getDistToScreen(), step;
        double origx, origy, origz, dirx, diry, dirz;
        // Find the ray origin and direction in the object's local coordinates. 
        v.set(r.origin);
        toLocal.transform(v);
        origx = v.x;
        origy = v.y;
        origz = v.z;
        v.set(r.direction);
        toLocal.transformDirection(v);
        dirx = v.x;
        diry = v.y;
        dirz = v.z;
        // Do the integration. 
        step = stepSize * material.getStepSize();
        do {
            // Find the new point along the ray. 
            dx = step * (1.5 * r.rt.random.nextDouble());
            if (adaptive && totalDist > distToScreen)
                dx *= totalDist / distToScreen;
            newx = x + dx;
            if (newx > endDist) {
                dx = endDist - x;
                x = endDist;
            } else
                x = newx;
            totalDist += dx;
            v.set(origx + dirx * x, origy + diry * x, origz + dirz * x);
            // Find the material properties at that point. 
            material.getMaterialSpec(v, matSpec, dx, time);
            RGBColor trans = matSpec.transparency;
            // Update the total emission and transmission. 
            if (trans.getRed() != 1.0f)
                rf *= (float) Math.pow(trans.getRed(), dx);
            if (trans.getGreen() != 1.0f)
                gf *= (float) Math.pow(trans.getGreen(), dx);
            if (trans.getBlue() != 1.0f)
                bf *= (float) Math.pow(trans.getBlue(), dx);
            if (rf < minRayIntensity && gf < minRayIntensity && bf < minRayIntensity) {
                // Everything beyond this point makes an insignificant contribution, so 
                // just stop now. 
                rf = gf = bf = 0.0f;
                break;
            }
        } while (x < endDist);
    }
    // Set the output colors and return. 
    filter.setRGB(rf, gf, bf);
}
