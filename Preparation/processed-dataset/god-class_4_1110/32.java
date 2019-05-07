/** Render a displacement mapeed triangle by recursively subdividing it. */
private void renderDisplacedTriangle(RenderingTriangle tri, DisplacedVertex dv1, double dist1, DisplacedVertex dv2, double dist2, DisplacedVertex dv3, double dist3, Vec3 viewdir, Vec3 ugrad, Vec3 vgrad, double tol, boolean cullBackfaces, boolean bumpMap, ObjectMaterialInfo material, RasterContext context) {
    Mat4 toView = context.camera.getObjectToView(), toScreen = context.camera.getObjectToScreen();
    DisplacedVertex midv1 = null, midv2 = null, midv3 = null;
    double halfdist1 = 0, halfdist2 = 0, halfdist3 = 0;
    boolean split1 = dist1 > tol, split2 = dist2 > tol, split3 = dist3 > tol;
    int shading = (bumpMap ? PHONG : shadingMode), count = 0;
    if (split1) {
        midv1 = new DisplacedVertex(tri, new Vec3(0.5 * (dv1.vert.x + dv2.vert.x), 0.5 * (dv1.vert.y + dv2.vert.y), 0.5 * (dv1.vert.z + dv2.vert.z)), new Vec3(0.5 * (dv1.norm.x + dv2.norm.x), 0.5 * (dv1.norm.y + dv2.norm.y), 0.5 * (dv1.norm.z + dv2.norm.z)), 0.5 * (dv1.u + dv2.u), 0.5 * (dv1.v + dv2.v), toView, toScreen, context);
        halfdist1 = 0.5 * dist1;
        count++;
    }
    if (split2) {
        midv2 = new DisplacedVertex(tri, new Vec3(0.5 * (dv2.vert.x + dv3.vert.x), 0.5 * (dv2.vert.y + dv3.vert.y), 0.5 * (dv2.vert.z + dv3.vert.z)), new Vec3(0.5 * (dv2.norm.x + dv3.norm.x), 0.5 * (dv2.norm.y + dv3.norm.y), 0.5 * (dv2.norm.z + dv3.norm.z)), 0.5 * (dv2.u + dv3.u), 0.5 * (dv2.v + dv3.v), toView, toScreen, context);
        halfdist2 = 0.5 * dist2;
        count++;
    }
    if (split3) {
        midv3 = new DisplacedVertex(tri, new Vec3(0.5 * (dv3.vert.x + dv1.vert.x), 0.5 * (dv3.vert.y + dv1.vert.y), 0.5 * (dv3.vert.z + dv1.vert.z)), new Vec3(0.5 * (dv3.norm.x + dv1.norm.x), 0.5 * (dv3.norm.y + dv1.norm.y), 0.5 * (dv3.norm.z + dv1.norm.z)), 0.5 * (dv3.u + dv1.u), 0.5 * (dv3.v + dv1.v), toView, toScreen, context);
        halfdist3 = 0.5 * dist3;
        count++;
    }
    // If any side is still too large, subdivide the triangle further. 
    if (count == 1) {
        // Split it into two triangles. 
        if (split1) {
            double d = dv3.vert.distance(midv1.vert);
            renderDisplacedTriangle(tri, dv1, halfdist1, midv1, d, dv3, dist3, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
            renderDisplacedTriangle(tri, midv1, halfdist1, dv2, dist2, dv3, d, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
        } else if (split2) {
            double d = dv1.vert.distance(midv2.vert);
            renderDisplacedTriangle(tri, dv2, halfdist2, midv2, d, dv1, dist1, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
            renderDisplacedTriangle(tri, midv2, halfdist2, dv3, dist3, dv1, d, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
        } else {
            double d = dv1.vert.distance(midv3.vert);
            renderDisplacedTriangle(tri, dv3, halfdist3, midv3, d, dv2, dist2, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
            renderDisplacedTriangle(tri, midv3, halfdist3, dv1, dist1, dv2, d, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
        }
        return;
    }
    if (count == 2) {
        // Split it into three triangles. 
        if (!split1) {
            double d1 = midv2.vert.distance(dv1.vert), d2 = midv2.vert.distance(midv3.vert);
            renderDisplacedTriangle(tri, dv1, dist1, dv2, halfdist2, midv2, d1, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
            renderDisplacedTriangle(tri, dv1, d1, midv2, d2, midv3, halfdist3, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
            renderDisplacedTriangle(tri, dv3, halfdist3, midv3, d2, midv2, halfdist2, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
        } else if (!split2) {
            double d1 = midv3.vert.distance(dv2.vert), d2 = midv3.vert.distance(midv1.vert);
            renderDisplacedTriangle(tri, dv2, dist2, dv3, halfdist3, midv3, d1, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
            renderDisplacedTriangle(tri, dv2, d1, midv3, d2, midv1, halfdist1, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
            renderDisplacedTriangle(tri, dv1, halfdist1, midv1, d2, midv3, halfdist3, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
        } else {
            double d1 = midv1.vert.distance(dv3.vert), d2 = midv1.vert.distance(midv2.vert);
            renderDisplacedTriangle(tri, dv3, dist3, dv1, halfdist1, midv1, d1, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
            renderDisplacedTriangle(tri, dv3, d1, midv1, d2, midv2, halfdist2, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
            renderDisplacedTriangle(tri, dv2, halfdist2, midv2, d2, midv1, halfdist1, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
        }
        return;
    }
    if (count == 3) {
        // Split it into four triangles. 
        double d1 = midv1.vert.distance(midv2.vert), d2 = midv2.vert.distance(midv3.vert), d3 = midv3.vert.distance(midv1.vert);
        renderDisplacedTriangle(tri, dv1, halfdist1, midv1, d3, midv3, halfdist3, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
        renderDisplacedTriangle(tri, dv2, halfdist2, midv2, d1, midv1, halfdist1, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
        renderDisplacedTriangle(tri, dv3, halfdist3, midv3, d2, midv2, halfdist2, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
        renderDisplacedTriangle(tri, midv1, d1, midv2, d2, midv3, d3, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
        return;
    }
    // The triangle is small enough that it does not need to be split any more, so render it. 
    float clip = (float) context.camera.getClipDistance();
    if (dv1.z < clip && dv2.z < clip && dv3.z < clip)
        return;
    if (dv1.z <= 0.0f || dv2.z < 0.0f || dv3.z < 0.0f)
        return;
    boolean backface = ((dv2.pos.x - dv1.pos.x) * (dv3.pos.y - dv1.pos.y) - (dv2.pos.y - dv1.pos.y) * (dv3.pos.x - dv1.pos.x) > 0.0);
    if (cullBackfaces && backface)
        return;
    if (dv1.dispnorm == null)
        dv1.prepareToRender(tri, viewdir, ugrad, vgrad, shading, context);
    if (dv2.dispnorm == null)
        dv2.prepareToRender(tri, viewdir, ugrad, vgrad, shading, context);
    if (dv3.dispnorm == null)
        dv3.prepareToRender(tri, viewdir, ugrad, vgrad, shading, context);
    Vec3 closestNorm;
    if (dv1.z < dv2.z && dv1.z < dv3.z)
        closestNorm = dv1.dispnorm;
    else if (dv2.z < dv1.z && dv2.z < dv3.z)
        closestNorm = dv2.dispnorm;
    else
        closestNorm = dv3.dispnorm;
    if (shading == GOURAUD)
        renderTriangleGouraud(dv1.pos, dv1.z, dv1.u, dv1.v, dv1.diffuse, dv1.specular, dv2.pos, dv2.z, dv2.u, dv2.v, dv2.diffuse, dv2.specular, dv3.pos, dv3.z, dv3.u, dv3.v, dv3.diffuse, dv3.specular, tri, clip, viewdir.dot(closestNorm), backface, material, context);
    else if (shading == HYBRID)
        renderTriangleHybrid(dv1.pos, dv1.z, dv1.dispvert, dv1.dispnorm, dv1.u, dv1.v, dv1.diffuse, dv2.pos, dv2.z, dv2.dispvert, dv2.dispnorm, dv2.u, dv2.v, dv2.diffuse, dv3.pos, dv3.z, dv3.dispvert, dv3.dispnorm, dv3.u, dv3.v, dv3.diffuse, tri, viewdir, closestNorm, clip, viewdir.dot(closestNorm), backface, material, context);
    else
        renderTrianglePhong(dv1.pos, dv1.z, dv1.dispvert, dv1.dispnorm, dv1.u, dv1.v, dv2.pos, dv2.z, dv2.dispvert, dv2.dispnorm, dv2.u, dv2.v, dv3.pos, dv3.z, dv3.dispvert, dv3.dispnorm, dv3.u, dv3.v, tri, viewdir, closestNorm, clip, bumpMap, backface, material, context);
}
