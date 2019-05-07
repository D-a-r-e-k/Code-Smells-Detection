/** Render a displacement mapped triangle mesh by recursively subdividing the triangles
     until they are sufficiently small. */
private void renderMeshDisplaced(RenderingMesh mesh, Vec3 viewdir, double tol, boolean cullBackfaces, boolean bumpMap, ObjectMaterialInfo material, RasterContext context) {
    Vec3 vert[] = mesh.vert, norm[] = mesh.norm;
    Mat4 toView = context.camera.getObjectToView(), toScreen = context.camera.getObjectToScreen();
    int v1, v2, v3, n1, n2, n3;
    double dist1, dist2, dist3;
    RenderingTriangle tri;
    for (int i = mesh.triangle.length - 1; i >= 0; i--) {
        tri = mesh.triangle[i];
        v1 = tri.v1;
        v2 = tri.v2;
        v3 = tri.v3;
        n1 = tri.n1;
        n2 = tri.n2;
        n3 = tri.n3;
        dist1 = vert[v1].distance(vert[v2]);
        dist2 = vert[v2].distance(vert[v3]);
        dist3 = vert[v3].distance(vert[v1]);
        // Calculate the gradient vectors for u and v. 
        context.tempVec[0].set(vert[v1].x - vert[v3].x, vert[v1].y - vert[v3].y, vert[v1].z - vert[v3].z);
        context.tempVec[1].set(vert[v3].x - vert[v2].x, vert[v3].y - vert[v2].y, vert[v3].z - vert[v2].z);
        Vec3 vgrad = context.tempVec[0].cross(mesh.faceNorm[i]);
        Vec3 ugrad = context.tempVec[1].cross(mesh.faceNorm[i]);
        vgrad.scale(-1.0 / vgrad.dot(context.tempVec[1]));
        ugrad.scale(1.0 / ugrad.dot(context.tempVec[0]));
        DisplacedVertex dv1 = new DisplacedVertex(tri, vert[v1], norm[n1], 1.0, 0.0, toView, toScreen, context);
        DisplacedVertex dv2 = new DisplacedVertex(tri, vert[v2], norm[n2], 0.0, 1.0, toView, toScreen, context);
        DisplacedVertex dv3 = new DisplacedVertex(tri, vert[v3], norm[n3], 0.0, 0.0, toView, toScreen, context);
        renderDisplacedTriangle(tri, dv1, dist1, dv2, dist2, dv3, dist3, viewdir, ugrad, vgrad, tol, cullBackfaces, bumpMap, material, context);
    }
}
