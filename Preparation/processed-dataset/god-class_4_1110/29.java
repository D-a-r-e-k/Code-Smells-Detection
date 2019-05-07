/** Render a triangle mesh with Phong shading. */
private void renderMeshPhong(RenderingMesh mesh, Vec3 viewdir, boolean cullBackfaces, boolean bumpMap, ObjectMaterialInfo material, RasterContext context) {
    Vec3 vert[] = mesh.vert, norm[] = mesh.norm, clipNorm[] = new Vec3[4];
    Vec2 pos[] = new Vec2[vert.length];
    float z[] = new float[vert.length], clip = (float) context.camera.getClipDistance(), clipz[] = new float[4];
    double clipu[] = new double[4], clipv[] = new double[4];
    Mat4 toView = context.camera.getObjectToView(), toScreen = context.camera.getObjectToScreen();
    RenderingTriangle tri;
    int i, v1, v2, v3, n1, n2, n3;
    boolean backface;
    for (i = 0; i < 4; i++) clipNorm[i] = new Vec3();
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
        if (z[v1] < clip || z[v2] < clip || z[v3] < clip) {
            Vec3 clipPos[] = clipTriangle(vert[v1], vert[v2], vert[v3], z[v1], z[v2], z[v3], clipz, clipu, clipv, context);
            Vec2 clipPos2D[] = new Vec2[clipPos.length];
            for (int j = clipPos.length - 1; j >= 0; j--) {
                clipPos2D[j] = toScreen.timesXY(clipPos[j]);
                double u = clipu[j], v = clipv[j], w = 1.0 - u - v;
                clipNorm[j].set(norm[n1].x * u + norm[n2].x * v + norm[n3].x * w, norm[n1].y * u + norm[n2].y * v + norm[n3].y * w, norm[n1].z * u + norm[n2].z * v + norm[n3].z * w);
                clipNorm[j].normalize();
            }
            renderTrianglePhong(clipPos2D[0], clipz[0], clipPos[0], clipNorm[0], clipu[0], clipv[0], clipPos2D[1], clipz[1], clipPos[1], clipNorm[1], clipu[1], clipv[1], clipPos2D[2], clipz[2], clipPos[2], clipNorm[2], clipu[2], clipv[2], tri, viewdir, mesh.faceNorm[i], clip, bumpMap, backface, material, context);
            if (clipPos.length == 4)
                renderTrianglePhong(clipPos2D[1], clipz[1], clipPos[1], clipNorm[1], clipu[1], clipv[1], clipPos2D[2], clipz[2], clipPos[2], clipNorm[2], clipu[2], clipv[2], clipPos2D[3], clipz[3], clipPos[3], clipNorm[3], clipu[3], clipv[3], tri, viewdir, mesh.faceNorm[i], clip, bumpMap, backface, material, context);
        } else {
            if (cullBackfaces && backface)
                continue;
            renderTrianglePhong(pos[v1], z[v1], vert[v1], norm[n1], 1.0, 0.0, pos[v2], z[v2], vert[v2], norm[n2], 0.0, 1.0, pos[v3], z[v3], vert[v3], norm[n3], 0.0, 0.0, tri, viewdir, mesh.faceNorm[i], clip, bumpMap, backface, material, context);
        }
    }
}
