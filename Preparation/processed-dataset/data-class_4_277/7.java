/** Trace a photon through the scene, and record where it is absorbed.
      @param r                  the ray to trace
      @param color              the photon color
      @param treeDepth          the depth of this ray within the ray tree
      @param node               the first octree node which the ray intersects
      @param first              the first object which the ray intersects, or null if this is not known
      @param currentMaterial    the MaterialMapping at the ray's origin (may be null)
      @param prevMaterial       the MaterialMapping the ray was passing through before entering currentMaterial
      @param currentMatTrans    the transform to local coordinates for the current material
      @param prevMatTrans       the transform to local coordinates for the previous material
      @param totalDist          the distance traveled from the viewpoint
      @param diffuse            true if this ray has been diffusely reflected since leaving the eye
      @param caustic            true if this ray has been specularly reflected or refracted since leaving the eye
  */
private void tracePhoton(Ray r, RGBColor color, int treeDepth, OctreeNode node, RTObject first, MaterialMapping currentMaterial, MaterialMapping prevMaterial, Mat4 currentMatTrans, Mat4 prevMatTrans, double totalDist, boolean diffuse, boolean caustic) {
    RTObject second = null;
    double dist, truedot, n = 1.0, beta = 0.0, d;
    RaytracerContext context = (RaytracerContext) getRaytracer().threadContext.get();
    Vec3 intersectionPoint = context.pos[treeDepth], norm = context.normal[treeDepth], trueNorm = context.trueNormal[treeDepth];
    TextureSpec spec = context.surfSpec[treeDepth];
    MaterialMapping nextMaterial, oldMaterial;
    Mat4 nextMatTrans, oldMatTrans = null;
    OctreeNode nextNode;
    // Find whether it hits anything. 
    SurfaceIntersection intersect = SurfaceIntersection.NO_INTERSECTION;
    if (first != null)
        intersect = r.findIntersection(first);
    if (intersect != SurfaceIntersection.NO_INTERSECTION) {
        intersect.intersectionPoint(0, intersectionPoint);
        nextNode = rt.rootNode.findNode(intersectionPoint);
    } else {
        nextNode = rt.traceRay(r, node);
        if (nextNode == null)
            return;
        first = context.intersect.first;
        second = context.intersect.second;
        intersect = context.lastRayResult[first.index];
        intersect.intersectionPoint(0, intersectionPoint);
    }
    // Get the surface properties at the point of intersection. 
    dist = intersect.intersectionDist(0);
    totalDist += dist;
    intersect.trueNormal(trueNorm);
    truedot = trueNorm.dot(r.getDirection());
    double texSmoothing = (diffuse ? rt.smoothScale * rt.extraGISmoothing : rt.smoothScale);
    if (truedot > 0.0)
        intersect.intersectionProperties(spec, norm, r.getDirection(), totalDist * texSmoothing * 3.0 / (2.0 + truedot), rt.time);
    else
        intersect.intersectionProperties(spec, norm, r.getDirection(), totalDist * texSmoothing * 3.0 / (2.0 - truedot), rt.time);
    // Reduce the photon intensity based on the current material or fog. 
    if (currentMaterial != null) {
        if (includeVolume && currentMaterial.isScattering()) {
            // See whether a photon gets stored in the material. 
            propagateRay(r, nextNode, second, dist, currentMaterial, prevMaterial, currentMatTrans, prevMatTrans, color, treeDepth, totalDist, caustic, diffuse);
        } else {
            RGBColor emissive = new RGBColor();
            // This will be ignored. 
            rt.propagateRay(r, nextNode, dist, currentMaterial, prevMaterial, currentMatTrans, prevMatTrans, emissive, color, treeDepth, totalDist);
        }
    } else if (rt.fog)
        color.scale((float) Math.exp(-dist / rt.fogDist));
    if (color.getRed() + color.getGreen() + color.getBlue() < rt.minRayIntensity)
        return;
    // The photon color is too dim to matter. 
    // Decide whether to store a photon here. 
    if (!includeVolume) {
        if ((includeDirect && treeDepth == 0) || (includeIndirect && diffuse) || (includeCaustics && caustic)) {
            if (spec.diffuse.getRed() + spec.diffuse.getGreen() + spec.diffuse.getBlue() + spec.hilight.getRed() + spec.hilight.getGreen() + spec.hilight.getBlue() > rt.minRayIntensity)
                addPhoton(intersectionPoint, r.getDirection(), color);
        }
    }
    // Decide whether to spawn reflected and/or transmitted photons. 
    if (treeDepth == rt.maxRayDepth - 1)
        return;
    boolean spawnSpecular = false, spawnTransmitted = false, spawnDiffuse = false;
    if (includeCaustics || includeVolume) {
        if (spec.specular.getRed() + spec.specular.getGreen() + spec.specular.getBlue() > rt.minRayIntensity)
            spawnSpecular = true;
    }
    if (includeCaustics || includeVolume) {
        if (spec.transparent.getRed() + spec.transparent.getGreen() + spec.transparent.getBlue() > rt.minRayIntensity)
            spawnTransmitted = true;
    }
    if (includeIndirect && !includeVolume) {
        if (spec.diffuse.getRed() + spec.diffuse.getGreen() + spec.diffuse.getBlue() > rt.minRayIntensity)
            spawnDiffuse = true;
    }
    // Spawn additional photons. 
    double dot = norm.dot(r.getDirection());
    RGBColor col = context.rayIntensity[treeDepth + 1];
    boolean totalReflect = false;
    if (spawnTransmitted) {
        // Spawn a transmitted photon. 
        col.copy(color);
        col.multiply(spec.transparent);
        context.ray[treeDepth + 1].getOrigin().set(intersectionPoint);
        Vec3 temp = context.ray[treeDepth + 1].getDirection();
        if (first.getMaterialMapping() == null) {
            // Not a solid object, so the bulk material does not change. 
            temp.set(r.getDirection());
            nextMaterial = currentMaterial;
            nextMatTrans = currentMatTrans;
            oldMaterial = prevMaterial;
            oldMatTrans = prevMatTrans;
        } else if (dot < 0.0) {
            // Entering an object. 
            nextMaterial = first.getMaterialMapping();
            nextMatTrans = first.toLocal();
            oldMaterial = currentMaterial;
            oldMatTrans = currentMatTrans;
            if (currentMaterial == null)
                n = nextMaterial.indexOfRefraction() / 1.0;
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
                d += Raytracer.TOL;
                temp.x -= d * trueNorm.x;
                temp.y -= d * trueNorm.y;
                temp.z -= d * trueNorm.z;
                temp.normalize();
            }
            context.ray[treeDepth + 1].newID();
            if (rt.gloss)
                randomizeDirection(temp, norm, spec.cloudiness);
            boolean newCaustic = (caustic || n != 1.0);
            tracePhoton(context.ray[treeDepth + 1], col, treeDepth + 1, nextNode, second, nextMaterial, oldMaterial, nextMatTrans, oldMatTrans, totalDist, diffuse, newCaustic);
        }
    }
    if (spawnSpecular || totalReflect) {
        // Spawn a reflection ray. 
        col.copy(spec.specular);
        if (totalReflect)
            col.add(spec.transparent.getRed(), spec.transparent.getGreen(), spec.transparent.getBlue());
        col.multiply(color);
        Vec3 temp = context.ray[treeDepth + 1].getDirection();
        temp.set(norm);
        temp.scale(-2.0 * dot);
        temp.add(r.getDirection());
        d = (truedot > 0.0 ? temp.dot(trueNorm) : -temp.dot(trueNorm));
        if (d >= 0.0) {
            // Make sure it comes out the correct side. 
            d += Raytracer.TOL;
            temp.x += d * trueNorm.x;
            temp.y += d * trueNorm.y;
            temp.z += d * trueNorm.z;
            temp.normalize();
        }
        context.ray[treeDepth + 1].getOrigin().set(intersectionPoint);
        context.ray[treeDepth + 1].newID();
        if (rt.gloss)
            randomizeDirection(temp, norm, spec.roughness);
        tracePhoton(context.ray[treeDepth + 1], col, treeDepth + 1, nextNode, second, currentMaterial, prevMaterial, currentMatTrans, prevMatTrans, totalDist, diffuse, true);
    }
    if (spawnDiffuse) {
        // Spawn a diffusely reflected ray. 
        col.copy(spec.diffuse);
        col.multiply(color);
        Vec3 temp = context.ray[treeDepth + 1].getDirection();
        do {
            temp.set(0.0, 0.0, 0.0);
            randomizePoint(temp, 1.0);
            temp.normalize();
            d = temp.dot(trueNorm) * (truedot > 0.0 ? 1.0 : -1.0);
        } while (random.nextDouble() > (d < 0.0 ? -d : d));
        if (d > 0.0) {
            // Make sure it comes out the correct side. 
            temp.scale(-1.0);
        }
        context.ray[treeDepth + 1].getOrigin().set(intersectionPoint);
        context.ray[treeDepth + 1].newID();
        tracePhoton(context.ray[treeDepth + 1], col, treeDepth + 1, nextNode, second, currentMaterial, prevMaterial, currentMatTrans, prevMatTrans, totalDist, true, caustic);
    }
}
