/**
   * Construct a new mesh, beveling each face individually.
   *
   * @param height      the extrude height
   * @param width       the bevel width
   */
private TriangleMesh bevelIndividualFaces(double height, double width) {
    mesh = (TriangleMesh) origMesh.duplicate();
    if (width == 0.0 && height == 0.0) {
        newSelection = selected;
        return mesh;
    }
    Vertex v[] = (Vertex[]) mesh.getVertices();
    Edge e[] = mesh.getEdges();
    Face f[] = mesh.getFaces();
    int i, j;
    Vector face = new Vector(), vert = new Vector();
    newIndex = new Vector();
    findVertexInsets(height, width);
    // All old vertices will be in the new mesh, so first copy them over. 
    for (i = 0; i < v.length; i++) vert.addElement(v[i]);
    // Create the new list of faces. 
    for (i = 0; i < f.length; i++) {
        if (selected[i]) {
            j = vert.size();
            vert.addElement(offsetVertex(mesh, v[f[i].v1], faceInsets[i][0]));
            vert.addElement(offsetVertex(mesh, v[f[i].v2], faceInsets[i][1]));
            vert.addElement(offsetVertex(mesh, v[f[i].v3], faceInsets[i][2]));
            newIndex.addElement(new Integer(face.size()));
            face.addElement(new int[] { j, j + 1, j + 2, i });
            face.addElement(new int[] { f[i].v1, f[i].v2, j, i });
            face.addElement(new int[] { j, f[i].v2, j + 1, i });
            face.addElement(new int[] { f[i].v2, f[i].v3, j + 1, i });
            face.addElement(new int[] { j + 1, f[i].v3, j + 2, i });
            face.addElement(new int[] { f[i].v3, f[i].v1, j + 2, i });
            face.addElement(new int[] { j + 2, f[i].v1, j, i });
        } else
            face.addElement(new int[] { f[i].v1, f[i].v2, f[i].v3, i });
    }
    prepareMesh(mesh, face, vert, null);
    // Copy over smoothness values for edges. 
    Edge newe[] = mesh.getEdges();
    for (i = 0; i < e.length; i++) for (j = 0; j < newe.length; j++) if ((e[i].v1 == newe[j].v1 && e[i].v2 == newe[j].v2) || (e[i].v1 == newe[j].v2 && e[i].v2 == newe[j].v1)) {
        newe[j].smoothness = e[i].smoothness;
        break;
    }
    // Record which faces should be selected. 
    newSelection = new boolean[mesh.getFaces().length];
    for (i = 0; i < newIndex.size(); i++) newSelection[((Integer) newIndex.elementAt(i)).intValue()] = true;
    return mesh;
}
