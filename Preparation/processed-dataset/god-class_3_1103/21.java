/** Get a triangle mesh which approximates the surface of this object at
      the specified accuracy. */
public TriangleMesh convertToTriangleMesh(double tol) {
    // Subdivide the surface and create the triangle mesh. 
    Vector vert = new Vector(), norm = new Vector(), face = new Vector(), param = new Vector();
    subdivideSurface(tol, vert, norm, face, param);
    Vec3 v[] = new Vec3[vert.size()];
    for (int i = 0; i < v.length; i++) v[i] = ((MeshVertex) vert.elementAt(i)).r;
    Vec3 n[] = new Vec3[vert.size()];
    norm.copyInto(n);
    int f[][] = new int[face.size()][];
    face.copyInto(f);
    int numnorm = norm.size();
    TriangleMesh mesh = new TriangleMesh(v, f);
    // Copy information on textures, materials, and parameters. 
    mesh.copyTextureAndMaterial(this);
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
        mesh.setParameterValues(tubeParamValue);
    }
    // Set the smoothness values of edges. 
    TriangleMesh.Edge ed[] = mesh.getEdges();
    TriangleMesh.Face fc[] = mesh.getFaces();
    for (int i = 0; i < fc.length; i++) {
        if (fc[i].v1 >= numnorm)
            ed[fc[i].e2].smoothness = 0.0f;
        if (fc[i].v2 >= numnorm)
            ed[fc[i].e3].smoothness = 0.0f;
        if (fc[i].v3 >= numnorm)
            ed[fc[i].e1].smoothness = 0.0f;
    }
    return mesh;
}
