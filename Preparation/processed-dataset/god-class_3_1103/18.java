/** Get a rendering mesh representing the surface of this object at the
      specified accuracy. */
public RenderingMesh getRenderingMesh(double tol, boolean interactive, ObjectInfo info) {
    if (interactive && cachedMesh != null)
        return cachedMesh;
    Vector vert = new Vector(), norm = new Vector(), face = new Vector(), param = new Vector();
    subdivideSurface(tol, vert, norm, face, param);
    Vec3 v[] = new Vec3[vert.size()];
    for (int i = 0; i < v.length; i++) v[i] = ((MeshVertex) vert.elementAt(i)).r;
    Vec3 n[] = new Vec3[vert.size()];
    norm.copyInto(n);
    int numnorm = norm.size();
    RenderingTriangle tri[] = new RenderingTriangle[face.size()];
    for (int i = 0; i < tri.length; i++) {
        int f[] = (int[]) face.elementAt(i);
        if (f[0] >= numnorm || f[1] >= numnorm || f[2] >= numnorm)
            tri[i] = texMapping.mapTriangle(f[0], f[1], f[2], numnorm, numnorm, numnorm, v);
        else
            tri[i] = texMapping.mapTriangle(f[0], f[1], f[2], f[0], f[1], f[2], v);
    }
    RenderingMesh rend = new RenderingMesh(v, n, tri, texMapping, matMapping);
    if (paramValue != null) {
        ParameterValue tubeParamValue[] = new ParameterValue[paramValue.length];
        for (int i = 0; i < paramValue.length; i++) {
            if (paramValue[i] instanceof VertexParameterValue) {
                double val[] = new double[v.length];
                for (int j = 0; j < val.length; j++) val[j] = ((double[]) param.elementAt(j))[i];
                tubeParamValue[i] = new VertexParameterValue(val);
            } else
                tubeParamValue[i] = paramValue[i];
        }
        rend.setParameters(tubeParamValue);
    }
    if (interactive)
        cachedMesh = rend;
    return rend;
}
