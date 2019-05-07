/**
   * Construct a new mesh, beveling each selected vertex.
   *
   * @param height      the extrude height
   * @param width       the bevel width
   */
private TriangleMesh bevelVertices(double height, double width) {
    mesh = (TriangleMesh) origMesh.duplicate();
    if (width == 0.0) {
        newSelection = selected;
        return mesh;
    }
    Vertex v[] = (Vertex[]) mesh.getVertices();
    Edge e[] = mesh.getEdges();
    Face f[] = mesh.getFaces();
    Vec3 norm[] = mesh.getNormals();
    int vertIndex[] = new int[v.length];
    int extraVertIndex[][] = new int[v.length][], vertEdgeIndex[][] = new int[v.length][];
    boolean forward[] = new boolean[v.length];
    Vector face = new Vector(), vert = new Vector();
    // Create the vertices of the new mesh. 
    for (int i = 0; i < v.length; i++) {
        if (!selected[i]) {
            // Just copy over this vertex. 
            vertIndex[i] = vert.size();
            vert.addElement(v[i]);
            continue;
        }
        // Look at the edges intersecting this vertex, and determine how far along them to bevel. 
        int edges[] = v[i].getEdges();
        vertEdgeIndex[i] = edges;
        Vec3 edgeDir[] = new Vec3[edges.length];
        double dot[] = new double[edges.length];
        double minDot = 1.0;
        for (int j = 0; j < edges.length; j++) {
            edgeDir[j] = v[e[edges[j]].v2].r.minus(v[e[edges[j]].v1].r);
            edgeDir[j].normalize();
            if (e[edges[j]].v2 == i)
                edgeDir[j].scale(-1.0);
            dot[j] = Math.abs(norm[i].dot(edgeDir[j]));
            if (dot[j] < minDot)
                minDot = dot[j];
        }
        double bevelDist = width / Math.tan(Math.acos(minDot));
        // Position the beveled vertices. 
        extraVertIndex[i] = new int[edges.length];
        for (int j = 0; j < edges.length; j++) {
            extraVertIndex[i][j] = vert.size();
            double dist = (dot[j] == 0.0 ? width : bevelDist / dot[j]);
            vert.addElement(offsetVertex(mesh, v[i], edgeDir[j].times(dist)));
        }
        // Position the central vertex. 
        boolean convex = (norm[i].dot(edgeDir[0]) < 0.0);
        vertIndex[i] = vert.size();
        vert.addElement(offsetVertex(mesh, v[i], norm[i].times(convex ? height - bevelDist : height + bevelDist)));
        // Determine which way the vertices are ordered. 
        Edge e0 = e[edges[0]];
        Edge e1 = e[edges[1]];
        Face fc = f[e0.f1];
        int v0 = (e0.v1 == i ? e0.v2 : e0.v1);
        int v1 = (e1.v1 == i ? e1.v2 : e1.v1);
        forward[i] = ((fc.v1 == v0 && fc.v3 == v1) || (fc.v2 == v0 && fc.v1 == v1) || (fc.v3 == v0 && fc.v2 == v1));
    }
    // We now have all the vertices.  Next, create the faces.  We begin with the ones 
    // corresponding to the faces of the original mesh. 
    for (int i = 0; i < f.length; i++) {
        Face fc = f[i];
        // After beveling, the three vertices of the original face may have turned into as many 
        // as six vertices.  Find them, in order. 
        int origVert[] = new int[] { fc.v1, fc.v2, fc.v2, fc.v3, fc.v3, fc.v1 };
        int origEdge[] = new int[] { fc.e1, fc.e1, fc.e2, fc.e2, fc.e3, fc.e3 };
        int newVert[] = new int[6];
        int numVert = 0;
        for (int j = 0; j < origVert.length; j++) {
            int index = vertIndex[origVert[j]];
            if (selected[origVert[j]]) {
                int orig = origVert[j];
                for (int k = 0; k < vertEdgeIndex[orig].length; k++) if (vertEdgeIndex[orig][k] == origEdge[j]) {
                    index = extraVertIndex[orig][k];
                    break;
                }
            }
            if (numVert == 0 || (index != newVert[numVert - 1] && index != newVert[0]))
                newVert[numVert++] = index;
        }
        // We now need to triangulate the vertices and create new faces. 
        if (numVert == 3)
            face.addElement(new int[] { newVert[0], newVert[1], newVert[2], i });
        else if (numVert == 4) {
            face.addElement(new int[] { newVert[0], newVert[1], newVert[2], i });
            face.addElement(new int[] { newVert[0], newVert[2], newVert[3], i });
        } else
            for (int step = 1; 2 * step < numVert; step *= 2) {
                int start;
                for (start = 0; start + 2 * step < numVert; start += 2 * step) face.addElement(new int[] { newVert[start], newVert[start + step], newVert[start + 2 * step], i });
                if (start + step < numVert)
                    face.addElement(new int[] { newVert[start], newVert[start + step], newVert[0], i });
            }
    }
    // Next create the faces capping the vertices that have just been beveled. 
    for (int i = 0; i < v.length; i++) {
        if (!selected[i])
            continue;
        for (int j = 0; j < extraVertIndex[i].length; j++) {
            int prev = (j == 0 ? vertEdgeIndex[i].length - 1 : j - 1);
            if (forward[i])
                face.addElement(new int[] { extraVertIndex[i][j], extraVertIndex[i][prev], vertIndex[i], -1 });
            else
                face.addElement(new int[] { extraVertIndex[i][prev], extraVertIndex[i][j], vertIndex[i], -1 });
        }
    }
    // Create the mesh. 
    prepareMesh(mesh, face, vert, vertIndex);
    // Copy over smoothness values for edges. 
    Edge newe[] = mesh.getEdges();
    for (int i = 0; i < e.length; i++) {
        int v1 = vertIndex[e[i].v1];
        int v2 = vertIndex[e[i].v2];
        for (int j = 0; j < newe.length; j++) if ((v1 == newe[j].v1 && v2 == newe[j].v2) || (v1 == newe[j].v2 && v2 == newe[j].v1)) {
            newe[j].smoothness = e[i].smoothness;
            break;
        }
    }
    // Record which vertices should be selected. 
    newSelection = new boolean[mesh.getVertices().length];
    for (int i = 0; i < vertIndex.length; i++) if (selected[i])
        newSelection[vertIndex[i]] = true;
    return mesh;
}
