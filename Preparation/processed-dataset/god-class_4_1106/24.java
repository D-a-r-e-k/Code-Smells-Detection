/** This routine is called recursively to spawn new rays.  It traces the ray, spawning
      still other rays as necessary, and returns the total light incident on the ray origin
      from the specified direction.  The appropriate Ray object should be set up before 
      calling this method, and the light color is returned in the appropriate RGBColor 
      object.

      @param rt                 contains information for the thread currently being executed
      @param treeDepth          the depth of this ray within the ray tree
      @param node               the first octree node which the ray intersects
      @param first              the first object which the ray intersects, or null if this is not known
      @param currentMaterial    the MaterialMapping at the ray's origin (may be null)
      @param prevMaterial       the MaterialMapping the ray was passing through before entering currentMaterial
      @param currentMatTrans    the transform to local coordinates for the current material
      @param prevMatTrans       the transform to local coordinates for the previous material
      @param rayNumber          the number of the ray within the pixel (for distribution ray tracing)
      @param totalDist          the distance traveled from the viewpoint
      @param transmitted        true if this ray has only been transmitted (not reflected) since leaving the eye
      @param diffuse            true if this ray has been diffusely reflected since leaving the eye
      @return the distance to the first object hit by the ray
  */
protected double spawnRay(RaytracerContext rt, int treeDepth, OctreeNode node, RTObject first, MaterialMapping currentMaterial, MaterialMapping prevMaterial, Mat4 currentMatTrans, Mat4 prevMatTrans, int rayNumber, double totalDist, boolean transmitted, boolean diffuse) {
    RTObject second = null;
    double dist, dot, truedot, n, beta = 0.0, d;
    Vec3 intersectionPoint = rt.pos[treeDepth], norm = rt.normal[treeDepth], trueNorm = rt.trueNormal[treeDepth], temp;
    boolean totalReflect = false;
    Ray r = rt.ray[treeDepth];
    TextureSpec spec = rt.surfSpec[treeDepth];
    MaterialMapping nextMaterial, oldMaterial;
    Mat4 nextMatTrans, oldMatTrans = null;
    RGBColor color = rt.color[treeDepth], rayIntensity = rt.rayIntensity[treeDepth];
    OctreeNode nextNode;
    // Find the intersection between the ray and the first object it hits. 
    rt.transparency[treeDepth] = 0.0;
    SurfaceIntersection intersection = SurfaceIntersection.NO_INTERSECTION;
    if (first != null) {
        intersection = r.findIntersection(first);
        if (intersection == SurfaceIntersection.NO_INTERSECTION) {
            // If the intersection is very close to the ray origin, findIntersection() may have 
            // ignored it.  Move back a tiny bit. 
            Ray r2 = rt.ray[treeDepth + 1];
            r2.origin.set(r.origin);
            r2.direction.set(r.direction);
            r2.origin.x -= TOL * r.direction.x;
            r2.origin.y -= TOL * r.direction.y;
            r2.origin.z -= TOL * r.direction.z;
            intersection = r2.findIntersection(first);
        }
    }
    if (intersection != SurfaceIntersection.NO_INTERSECTION) {
        intersection.intersectionPoint(0, intersectionPoint);
        nextNode = rootNode.findNode(intersectionPoint);
    } else {
        nextNode = traceRay(r, node);
        if (nextNode == null) {
            if (transmitted && transparentBackground) {
                color.setRGB(0.0f, 0.0f, 0.0f);
                rt.transparency[treeDepth] = Math.min(Math.min(rayIntensity.getRed(), rayIntensity.getGreen()), rayIntensity.getBlue());
                return Float.MAX_VALUE;
            }
            if (envMode == Scene.ENVIRON_SOLID) {
                color.copy(envColor);
                color.multiply(rayIntensity);
                return Float.MAX_VALUE;
            }
            double envSmoothing = (diffuse ? smoothScale * extraGIEnvSmoothing : smoothScale);
            envMapping.getTextureSpec(r.direction, spec, 1.0, smoothing * envSmoothing, time, envParamValue);
            if (envMode == Scene.ENVIRON_DIFFUSE)
                color.copy(spec.diffuse);
            else
                color.copy(spec.emissive);
            color.multiply(rayIntensity);
            return Float.MAX_VALUE;
        }
        first = rt.intersect.first;
        second = rt.intersect.second;
        intersection = rt.lastRayResult[first.index];
        intersection.intersectionPoint(0, intersectionPoint);
    }
    if (treeDepth == 0)
        rt.firstObjectHit = first;
    dist = intersection.intersectionDist(0);
    totalDist += dist;
    intersection.trueNormal(trueNorm);
    truedot = trueNorm.dot(r.getDirection());
    double texSmoothing = (diffuse ? smoothScale * extraGISmoothing : smoothScale);
    if (truedot > 0.0)
        intersection.intersectionProperties(spec, norm, r.getDirection(), totalDist * texSmoothing * 3.0 / (2.0 + truedot), time);
    else
        intersection.intersectionProperties(spec, norm, r.getDirection(), totalDist * texSmoothing * 3.0 / (2.0 - truedot), time);
    // Get the direct lighting contribution, and adjust the ray intensity based on the  
    // material it is passing through. 
    getDirectLight(rt, intersectionPoint, norm, (truedot < 0.0), r.getDirection(), treeDepth, nextNode, rayNumber, totalDist, currentMaterial, prevMaterial, currentMatTrans, prevMatTrans, diffuse);
    if (currentMaterial != null) {
        propagateRay(r, node, dist, currentMaterial, prevMaterial, currentMatTrans, prevMatTrans, rt.tempColor, rayIntensity, treeDepth, totalDist);
        color.multiply(rayIntensity);
        color.add(rt.tempColor);
    } else if (fog) {
        float fract = (float) Math.exp(-dist / fogDist);
        color.scale(fract);
        rt.tempColor.copy(fogColor);
        rt.tempColor.scale(1.0f - fract);
        color.add(rt.tempColor);
        color.multiply(rayIntensity);
        rayIntensity.scale(fract);
    } else
        color.multiply(rayIntensity);
    // Determine which types of rays to spawn. 
    if (treeDepth == maxRayDepth - 1)
        return dist;
    if (giMode == GI_AMBIENT_OCCLUSION && diffuse)
        return dist;
    // This ray was sampling ambient occlusion, so don't send out any more rays. 
    boolean spawnSpecular = false, spawnTransmitted = false, spawnDiffuse = false;
    float specularScale = 1.0f, transmittedScale = 1.0f, diffuseScale = 1.0f;
    if (roulette) {
        // Russian Roulette sampling is enabled: randomly decide whether to spawn a 
        // ray of each type. 
        float prob = (rayIntensity.getRed() * spec.specular.getRed() + rayIntensity.getGreen() * spec.specular.getGreen() + rayIntensity.getBlue() * spec.specular.getBlue()) / 3.0f;
        if (prob > rt.random.nextFloat()) {
            spawnSpecular = true;
            specularScale = 1.0f / prob;
        }
        prob = (rayIntensity.getRed() * spec.transparent.getRed() + rayIntensity.getGreen() * spec.transparent.getGreen() + rayIntensity.getBlue() * spec.transparent.getBlue()) / 3.0f;
        if (prob > rt.random.nextFloat()) {
            spawnTransmitted = true;
            transmittedScale = 1.0f / prob;
        }
        if (giMode == GI_MONTE_CARLO || giMode == GI_AMBIENT_OCCLUSION || (giMode == GI_HYBRID && !diffuse)) {
            prob = (rayIntensity.getRed() * spec.diffuse.getRed() + rayIntensity.getGreen() * spec.diffuse.getGreen() + rayIntensity.getBlue() * spec.diffuse.getBlue()) / 3.0f;
            if (prob > rt.random.nextFloat()) {
                spawnDiffuse = true;
                diffuseScale = 1.0f / prob;
            }
        }
    } else {
        // Russian Roulette sampling is disabled.  Always spawn rays whenever appropriate. 
        spawnSpecular = (rayIntensity.getRed() * spec.specular.getRed() > minRayIntensity || rayIntensity.getGreen() * spec.specular.getGreen() > minRayIntensity || rayIntensity.getBlue() * spec.specular.getBlue() > minRayIntensity);
        spawnTransmitted = (rayIntensity.getRed() * spec.transparent.getRed() > minRayIntensity || rayIntensity.getGreen() * spec.transparent.getGreen() > minRayIntensity || rayIntensity.getBlue() * spec.transparent.getBlue() > minRayIntensity);
        if (giMode == GI_MONTE_CARLO || giMode == GI_AMBIENT_OCCLUSION || (giMode == GI_HYBRID && !diffuse))
            spawnDiffuse = (rayIntensity.getRed() * spec.diffuse.getRed() > minRayIntensity || rayIntensity.getGreen() * spec.diffuse.getGreen() > minRayIntensity || rayIntensity.getBlue() * spec.diffuse.getBlue() > minRayIntensity);
    }
    // Now spawn the rays. 
    dot = norm.dot(r.getDirection());
    RGBColor col = rt.rayIntensity[treeDepth + 1];
    if (spawnTransmitted) {
        // Spawn a transmitted ray. 
        col.copy(rayIntensity);
        col.multiply(spec.transparent);
        col.scale(transmittedScale);
        rt.ray[treeDepth + 1].getOrigin().set(intersectionPoint);
        temp = rt.ray[treeDepth + 1].getDirection();
        if (first.getMaterialMapping() == null) {
            // Not a solid object, so the bulk material does not change. 
            temp.set(r.getDirection());
            nextMaterial = currentMaterial;
            nextMatTrans = currentMatTrans;
            oldMaterial = prevMaterial;
            oldMatTrans = prevMatTrans;
        } else if (truedot < 0.0) {
            // Entering an object. 
            nextMaterial = first.getMaterialMapping();
            nextMatTrans = first.toLocal();
            oldMaterial = currentMaterial;
            oldMatTrans = currentMatTrans;
            if (currentMaterial == null)
                n = nextMaterial.indexOfRefraction();
            else
                n = nextMaterial.indexOfRefraction() / currentMaterial.indexOfRefraction();
            beta = -(dot + Math.sqrt(n * n - 1.0 + dot * dot));
            temp.set(norm);
            temp.scale(beta);
            temp.add(r.getDirection());
            temp.scale(1.0 / n);
        } else {
            // Exiting an object. 
            if (currentMaterial == first.getMaterialMapping()) {
                nextMaterial = prevMaterial;
                nextMatTrans = prevMatTrans;
                oldMaterial = null;
                if (nextMaterial == null)
                    n = 1.0 / currentMaterial.indexOfRefraction();
                else
                    n = nextMaterial.indexOfRefraction() / currentMaterial.indexOfRefraction();
            } else {
                nextMaterial = currentMaterial;
                nextMatTrans = currentMatTrans;
                if (prevMaterial == first.getMaterialMapping())
                    oldMaterial = null;
                else {
                    oldMaterial = prevMaterial;
                    oldMatTrans = prevMatTrans;
                }
                n = 1.0;
            }
            beta = dot - Math.sqrt(n * n - 1.0 + dot * dot);
            temp.set(norm);
            temp.scale(-beta);
            temp.add(r.getDirection());
            temp.scale(1.0 / n);
        }
        if (Double.isNaN(beta))
            totalReflect = true;
        else {
            d = (truedot > 0.0 ? temp.dot(trueNorm) : -temp.dot(trueNorm));
            if (d < 0.0) {
                // Make sure it comes out the correct side. 
                d += TOL;
                temp.x -= d * trueNorm.x;
                temp.y -= d * trueNorm.y;
                temp.z -= d * trueNorm.z;
                temp.normalize();
            }
            rt.ray[treeDepth + 1].newID();
            if (gloss)
                randomizeDirection(temp, norm, rt.random, spec.cloudiness, rayNumber + treeDepth + 1);
            spawnRay(rt, treeDepth + 1, nextNode, second, nextMaterial, oldMaterial, nextMatTrans, oldMatTrans, rayNumber, totalDist, transmitted, diffuse);
            color.add(rt.color[treeDepth + 1]);
            if (transmitted && transparentBackground)
                rt.transparency[treeDepth] = rt.transparency[treeDepth + 1];
        }
    }
    if (spawnSpecular || totalReflect) {
        // Spawn a reflection ray. 
        col.copy(spec.specular);
        col.scale(specularScale);
        if (totalReflect)
            col.add(spec.transparent.getRed() * transmittedScale, spec.transparent.getGreen() * transmittedScale, spec.transparent.getBlue() * transmittedScale);
        col.multiply(rayIntensity);
        temp = rt.ray[treeDepth + 1].getDirection();
        temp.set(norm);
        temp.scale(-2.0 * dot);
        temp.add(r.getDirection());
        d = (truedot > 0.0 ? temp.dot(trueNorm) : -temp.dot(trueNorm));
        if (d >= 0.0) {
            // Make sure it comes out the correct side. 
            d += TOL;
            temp.x += d * trueNorm.x;
            temp.y += d * trueNorm.y;
            temp.z += d * trueNorm.z;
            temp.normalize();
        }
        rt.ray[treeDepth + 1].getOrigin().set(intersectionPoint);
        rt.ray[treeDepth + 1].newID();
        if (gloss)
            randomizeDirection(temp, norm, rt.random, spec.roughness, rayNumber + treeDepth + 1);
        spawnRay(rt, treeDepth + 1, nextNode, null, currentMaterial, prevMaterial, currentMatTrans, prevMatTrans, rayNumber, totalDist, false, diffuse);
        color.add(rt.color[treeDepth + 1]);
    }
    if (spawnDiffuse) {
        // Spawn a diffusely reflected ray. 
        int numRays = (diffuse ? 1 : diffuseRays);
        col.copy(spec.diffuse);
        col.multiply(rayIntensity);
        col.scale(diffuseScale);
        temp = rt.ray[treeDepth + 1].getDirection();
        for (int i = 0; i < numRays; i++) {
            do {
                temp.set(0.0, 0.0, 0.0);
                randomizePoint(temp, rt.random, 1.0, rayNumber + treeDepth + 1 + i);
                temp.normalize();
                d = temp.dot(trueNorm) * (truedot > 0.0 ? 1.0 : -1.0);
            } while (rt.random.nextDouble() > (d < 0.0 ? -d : d));
            if (d > 0.0) {
                // Make sure it comes out the correct side. 
                temp.scale(-1.0);
            }
            rt.ray[treeDepth + 1].getOrigin().set(intersectionPoint);
            rt.ray[treeDepth + 1].newID();
            spawnRay(rt, treeDepth + 1, nextNode, null, currentMaterial, prevMaterial, currentMatTrans, prevMatTrans, rayNumber, totalDist, false, true);
            rt.color[treeDepth + 1].scale(1.0f / numRays);
            color.add(rt.color[treeDepth + 1]);
        }
    }
    return dist;
}
