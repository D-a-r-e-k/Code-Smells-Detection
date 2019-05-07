/** Render a triangle mesh with Gouraud shading. */
private void renderMeshGouraud(RenderingMesh mesh, Vec3 viewdir, boolean cullBackfaces, ObjectMaterialInfo material, RasterContext context) {
    Vec3 vert[] = mesh.vert, norm[] = mesh.norm;
    Vec2 pos[] = new Vec2[vert.length];
    float z[] = new float[vert.length], clip = (float) context.camera.getClipDistance(), clipz[] = new float[4];
    double clipu[] = new double[4], clipv[] = new double[4];
    double distToScreen = context.camera.getDistToScreen(), tol = smoothScale;
    RGBColor diffuse[] = new RGBColor[4], specular[] = new RGBColor[4], highlight[] = new RGBColor[4];
    Mat4 toView = context.camera.getObjectToView(), toScreen = context.camera.getObjectToScreen();
    RenderingTriangle tri;
    int i, v1, v2, v3, n1, n2, n3;
    boolean backface;
    for (i = 0; i < 4; i++) {
        diffuse[i] = new RGBColor();
        specular[i] = new RGBColor();
        highlight[i] = new RGBColor();
    }
    for (i = vert.length - 1; i >= 0; i--) {
        pos[i] = toScreen.timesXY(vert[i]);
        z[i] = (float) toView.timesZ(vert[i]);
    }
    for (i = mesh.triangle.length - 1; i >= 0; i--) {
        tri = mesh.triangle[i];
        v1 = tri.v1;
        v2 = tri.v2;
        v3 = tri.v3;
        n1 = tri.n1;
        n2 = tri.n2;
        n3 = tri.n3;
        if (z[v1] < clip && z[v2] < clip && z[v3] < clip)
            continue;
        backface = ((pos[v2].x - pos[v1].x) * (pos[v3].y - pos[v1].y) - (pos[v2].y - pos[v1].y) * (pos[v3].x - pos[v1].x) > 0.0);
        double viewdot = viewdir.dot(mesh.faceNorm[i]);
        if (z[v1] < clip || z[v2] < clip || z[v3] < clip) {
            Vec3 clipPos[] = clipTriangle(vert[v1], vert[v2], vert[v3], z[v1], z[v2], z[v3], clipz, clipu, clipv, context);
            Vec2 clipPos2D[] = new Vec2[clipPos.length];
            for (int j = clipPos.length - 1; j >= 0; j--) {
                clipPos2D[j] = toScreen.timesXY(clipPos[j]);
                double u = clipu[j], v = clipv[j], w = 1.0 - u - v;
                tri.getTextureSpec(context.surfSpec, viewdot, u, v, 1.0 - u - v, tol, time);
                context.tempVec[2].set(norm[n1].x * u + norm[n2].x * v + norm[n3].x * w, norm[n1].y * u + norm[n2].y * v + norm[n3].y * w, norm[n1].z * u + norm[n2].z * v + norm[n3].z * w);
                context.tempVec[2].normalize();
                calcLight(clipPos[j], context.tempVec[2], viewdir, mesh.faceNorm[i], context.surfSpec.roughness, diffuse[j], specular[j], highlight[j], context);
                specular[j].add(highlight[j]);
            }
            renderTriangleGouraud(clipPos2D[0], clipz[0], clipu[0], clipv[0], diffuse[0], specular[0], clipPos2D[1], clipz[1], clipu[1], clipv[1], diffuse[1], specular[1], clipPos2D[2], clipz[2], clipu[2], clipv[2], diffuse[2], specular[2], tri, clip, viewdot, backface, material, context);
            if (clipPos.length == 4)
                renderTriangleGouraud(clipPos2D[1], clipz[1], clipu[1], clipv[1], diffuse[1], specular[1], clipPos2D[2], clipz[2], clipu[2], clipv[2], diffuse[2], specular[2], clipPos2D[3], clipz[3], clipu[3], clipv[3], diffuse[3], specular[3], tri, clip, viewdot, backface, material, context);
        } else {
            if (cullBackfaces && backface)
                continue;
            if (z[v1] > distToScreen)
                tol = smoothScale * z[v1];
            tri.getTextureSpec(context.surfSpec, viewdot, 1.0, 0.0, 0.0, tol, time);
            calcLight(vert[v1], norm[n1], viewdir, mesh.faceNorm[i], context.surfSpec.roughness, diffuse[0], specular[0], highlight[0], context);
            specular[0].add(highlight[0]);
            if (z[v2] > distToScreen)
                tol = smoothScale * z[v2];
            tri.getTextureSpec(context.surfSpec, viewdot, 0.0, 1.0, 0.0, tol, time);
            calcLight(vert[v2], norm[n2], viewdir, mesh.faceNorm[i], context.surfSpec.roughness, diffuse[1], specular[1], highlight[1], context);
            specular[1].add(highlight[1]);
            if (z[v3] > distToScreen)
                tol = smoothScale * z[v3];
            tri.getTextureSpec(context.surfSpec, viewdot, 0.0, 0.0, 1.0, tol, time);
            calcLight(vert[v3], norm[n3], viewdir, mesh.faceNorm[i], context.surfSpec.roughness, diffuse[2], specular[2], highlight[2], context);
            specular[2].add(highlight[2]);
            renderTriangleGouraud(pos[v1], z[v1], 1.0, 0.0, diffuse[0], specular[0], pos[v2], z[v2], 0.0, 1.0, diffuse[1], specular[1], pos[v3], z[v3], 0.0, 0.0, diffuse[2], specular[2], tri, clip, viewdot, backface, material, context);
        }
    }
}
