/**
   * Construct a new mesh, beveling each selected edges.
   *
   * @param height      the extrude height
   * @param width       the bevel width
   */
private TriangleMesh bevelEdges(double height, double width) {
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
    int extraVertIndex[][] = new int[v.length][];
    int faceVertIndex[][] = new int[v.length][];
    boolean forward[] = new boolean[v.length];
    Vector face = new Vector(), vert = new Vector();
    // Find the bevel and extrude directions for every edge. 
    Vec3 edgeDir[] = new Vec3[e.length];
    Vec3 bevelDir[] = new Vec3[e.length];
    Vec3 extrudeDir[] = new Vec3[e.length];
    for (int i = 0; i < e.length; i++) {
        edgeDir[i] = v[e[i].v2].r.minus(v[e[i].v1].r);
        if (selected[i]) {
            Vec3 avgNorm = norm[e[i].v1].plus(norm[e[i].v2]);
            bevelDir[i] = edgeDir[i].cross(avgNorm);
            bevelDir[i].normalize();
            extrudeDir[i] = bevelDir[i].cross(edgeDir[i]);
            extrudeDir[i].normalize();
        }
    }
    // Count the selected edges touching each vertex. 
    int vertEdgeIndex[][] = new int[v.length][];
    int vertEdgeCount[] = new int[v.length];
    for (int i = 0; i < v.length; i++) {
        vertEdgeIndex[i] = v[i].getEdges();
        for (int j = 0; j < vertEdgeIndex[i].length; j++) if (selected[vertEdgeIndex[i][j]])
            vertEdgeCount[i]++;
    }
    // Record the faces touching each vertex. 
    int vertFaceIndex[][] = new int[v.length][];
    for (int i = 0; i < v.length; i++) {
        vertFaceIndex[i] = new int[vertEdgeIndex[i].length];
        int e0 = vertEdgeIndex[i][0];
        int e1 = vertEdgeIndex[i][1];
        int prev = e[e0].f1;
        if (f[prev].e1 == e1 || f[prev].e2 == e1 || f[prev].e3 == e1)
            prev = e[e0].f2;
        for (int j = 0; j < vertFaceIndex[i].length; j++) {
            Edge ed = e[vertEdgeIndex[i][j]];
            vertFaceIndex[i][j] = (ed.f1 == prev ? ed.f2 : ed.f1);
            prev = vertFaceIndex[i][j];
        }
    }
    // Create the vertices of the new mesh. 
    for (int i = 0; i < v.length; i++) {
        if (vertEdgeCount[i] == 0) {
            // Just copy over this vertex. 
            vertIndex[i] = vert.size();
            vert.addElement(v[i]);
            continue;
        }
        // Look at the edges intersecting this vertex, and determine how far along them to bevel. 
        int edges[] = vertEdgeIndex[i];
        double offsetDist[] = new double[edges.length];
        for (int j = 0; j < edges.length; j++) if (selected[edges[j]]) {
            // Calculate the offsets based on beveling this particular edge. 
            Vec3 offsetDir = extrudeDir[edges[j]];
            double dot[] = new double[edges.length];
            double minDot = 1.0;
            for (int k = 0; k < edges.length; k++) if (!selected[edges[k]]) {
                Vec3 dir = edgeDir[vertEdgeIndex[i][k]];
                dot[k] = Math.abs(offsetDir.dot(dir));
                if (dot[k] < minDot)
                    minDot = dot[k];
            }
            double bevelDist = width / Math.tan(Math.acos(minDot));
            for (int k = 0; k < edges.length; k++) if (!selected[edges[k]]) {
                double dist = (dot[k] == 0.0 ? width : bevelDist / dot[k]);
                if (dist > offsetDist[k])
                    offsetDist[k] = dist;
            }
        }
        // Position the beveled vertices. 
        extraVertIndex[i] = new int[edges.length];
        for (int j = 0; j < edges.length; j++) {
            if (selected[edges[j]]) {
                extraVertIndex[i][j] = -1;
                continue;
            }
            extraVertIndex[i][j] = vert.size();
            double dist = offsetDist[j];
            if (e[edges[j]].v2 == i)
                dist = -dist;
            vert.addElement(offsetVertex(mesh, v[i], edgeDir[edges[j]].times(dist)));
        }
        // If two adjacent edges were both beveled, we need to create a new vertex between them. 
        faceVertIndex[i] = new int[edges.length];
        for (int j = 0; j < edges.length; j++) {
            int next = j + 1;
            if (next == edges.length)
                next = (e[edges[0]].f2 == -1 ? -1 : 0);
            if (next == -1 || (!selected[edges[j]] && !selected[edges[next]]))
                continue;
            if (!selected[edges[j]]) {
                faceVertIndex[i][j] = extraVertIndex[i][j];
                continue;
            }
            if (!selected[edges[next]]) {
                faceVertIndex[i][j] = extraVertIndex[i][next];
                continue;
            }
            faceVertIndex[i][j] = vert.size();
            // Find the vertex position. 
            double dist1 = offsetDist[j];
            if (e[edges[j]].v2 == i)
                dist1 = -dist1;
            double dist2 = offsetDist[next];
            if (e[edges[next]].v2 == i)
                dist2 = -dist2;
            double edgeDot = edgeDir[edges[j]].dot(edgeDir[edges[next]]);
            double m[][] = new double[][] { { 1.0, edgeDot }, { edgeDot, 1.0 } };
            double b[] = new double[] { dist1, dist2 };
            SVD.solve(m, b);
            Vec3 offset = edgeDir[edges[j]].times(b[0]).plus(edgeDir[edges[next]].times(b[1]));
            vert.addElement(offsetVertex(mesh, v[i], offset));
        }
        // Determine which way the vertices are ordered. 
        Edge e0 = e[edges[0]];
        Edge e1 = e[edges[1]];
        Face fc = f[e0.f1];
        int v0 = (e0.v1 == i ? e0.v2 : e0.v1);
        int v1 = (e1.v1 == i ? e1.v2 : e1.v1);
        forward[i] = ((fc.v1 == v0 && fc.v3 == v1) || (fc.v2 == v0 && fc.v1 == v1) || (fc.v3 == v0 && fc.v2 == v1));
    }
    // For each end of each beveled edge, calculate an "ideal position" which is midway between the 
    // vertices on either side of it. 
    Vec3 idealEndPos[][] = new Vec3[e.length][];
    for (int i = 0; i < e.length; i++) {
        if (!selected[i])
            continue;
        // Find the vertices surrounding this edge. 
        int faceList[] = new int[e[i].f2 == -1 ? 1 : 2];
        faceList[0] = e[i].f1;
        if (faceList.length > 1)
            faceList[1] = e[i].f2;
        int vi[] = new int[4];
        for (int j = 0; j < faceList.length; j++) {
            int v1 = -1, v2 = -1;
            for (int k = 0; k < vertFaceIndex[e[i].v1].length && v1 == -1; k++) if (vertFaceIndex[e[i].v1][k] == faceList[j])
                v1 = faceVertIndex[e[i].v1][k];
            for (int k = 0; k < vertFaceIndex[e[i].v2].length && v2 == -1; k++) if (vertFaceIndex[e[i].v2][k] == faceList[j])
                v2 = faceVertIndex[e[i].v2][k];
            vi[j * 2] = v1;
            vi[j * 2 + 1] = v2;
        }
        // Calculate the ideal end positions. 
        idealEndPos[i] = new Vec3[2];
        if (faceList.length == 1) {
            Vec3 delta;
            double d;
            delta = ((Vertex) vert.elementAt(vi[0])).r.minus(v[e[i].v1].r);
            d = delta.dot(extrudeDir[i]);
            idealEndPos[i][0] = v[e[i].v1].r.plus(extrudeDir[i].times(d));
            delta = ((Vertex) vert.elementAt(vi[1])).r.minus(v[e[i].v2].r);
            d = delta.dot(extrudeDir[i]);
            idealEndPos[i][1] = v[e[i].v2].r.plus(extrudeDir[i].times(d));
        } else {
            idealEndPos[i][0] = ((Vertex) vert.elementAt(vi[0])).r.plus(((Vertex) vert.elementAt(vi[2])).r).times(0.5);
            idealEndPos[i][1] = ((Vertex) vert.elementAt(vi[1])).r.plus(((Vertex) vert.elementAt(vi[3])).r).times(0.5);
        }
    }
    // If a vertex is touched by multiple beveled vertices, there may be several "ideal positions" for 
    // it.  Find a single position which is a best compromise between them. 
    for (int i = 0; i < v.length; i++) {
        if (vertEdgeCount[i] == 0)
            continue;
        Vec3 ideal[] = new Vec3[vertEdgeCount[i]];
        int index[] = new int[vertEdgeCount[i]];
        int num = 0;
        int edges[] = vertEdgeIndex[i];
        // Find all the ideal positions (as offsets from the current vertex position) and edge indices. 
        for (int j = 0; j < edges.length; j++) {
            int ej = edges[j];
            if (selected[ej]) {
                ideal[num] = (e[ej].v1 == i ? idealEndPos[ej][0] : idealEndPos[ej][1]);
                ideal[num].subtract(v[i].r);
                ideal[num].add(extrudeDir[ej].times(height));
                index[num] = ej;
                num++;
            }
        }
        // Combine them to find a single position. 
        vertIndex[i] = vert.size();
        if (ideal.length == 1) {
            vert.addElement(offsetVertex(mesh, v[i], ideal[0]));
            continue;
        }
        double m[][] = new double[2 * ideal.length][];
        double b[] = new double[2 * ideal.length];
        for (int j = 0; j < ideal.length; j++) {
            Vec3 dir;
            dir = extrudeDir[index[j]];
            m[2 * j] = new double[] { dir.x, dir.y, dir.z };
            b[2 * j] = dir.dot(ideal[j]);
            dir = bevelDir[index[j]];
            m[2 * j + 1] = new double[] { dir.x, dir.y, dir.z };
            b[2 * j + 1] = dir.dot(ideal[j]);
        }
        SVD.solve(m, b);
        vert.addElement(offsetVertex(mesh, v[i], new Vec3(b[0], b[1], b[2])));
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
        for (int j = 0; j < origEdge.length; j++) {
            int index = -1;
            int orig = origVert[j];
            if (extraVertIndex[orig] != null) {
                // A beveled edge touches the vertex. 
                for (int k = 0; k < vertEdgeIndex[orig].length; k++) if (vertEdgeIndex[orig][k] == origEdge[j]) {
                    if (extraVertIndex[orig][k] == -1) {
                        // Two adjacent edges were beveled. 
                        for (int m = 0; m < vertFaceIndex[orig].length; m++) if (vertFaceIndex[orig][m] == i) {
                            index = faceVertIndex[orig][m];
                            break;
                        }
                    } else
                        index = extraVertIndex[orig][k];
                    break;
                }
            } else
                index = vertIndex[origVert[j]];
            if (index > -1 && (numVert == 0 || (index != newVert[numVert - 1] && index != newVert[0])))
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
    // Next create the faces capping the vertices at the ends of the beveled edges. 
    for (int i = 0; i < v.length; i++) {
        if (extraVertIndex[i] == null)
            continue;
        for (int j = 0; j < extraVertIndex[i].length; j++) {
            int prev = (j == 0 ? vertEdgeIndex[i].length - 1 : j - 1);
            int v1 = extraVertIndex[i][j];
            int v2 = extraVertIndex[i][prev];
            if (v1 == -1 || v2 == -1)
                continue;
            if (forward[i])
                face.addElement(new int[] { v1, v2, vertIndex[i], -1 });
            else
                face.addElement(new int[] { v2, v1, vertIndex[i], -1 });
        }
    }
    // Finally, create the faces capping the beveled edges themselves. 
    for (int i = 0; i < e.length; i++) {
        if (!selected[i])
            continue;
        int faceList[] = new int[e[i].f2 == -1 ? 1 : 2];
        faceList[0] = e[i].f1;
        if (faceList.length > 1)
            faceList[1] = e[i].f2;
        for (int j = 0; j < faceList.length; j++) {
            int v0, v1, v2, v3;
            v0 = vertIndex[e[i].v1];
            v3 = vertIndex[e[i].v2];
            v1 = v2 = -1;
            for (int k = 0; k < vertFaceIndex[e[i].v1].length && v1 == -1; k++) if (vertFaceIndex[e[i].v1][k] == faceList[j])
                v1 = faceVertIndex[e[i].v1][k];
            for (int k = 0; k < vertFaceIndex[e[i].v2].length && v2 == -1; k++) if (vertFaceIndex[e[i].v2][k] == faceList[j])
                v2 = faceVertIndex[e[i].v2][k];
            Face fc = f[faceList[j]];
            if ((fc.v1 == e[i].v1 && fc.v3 == e[i].v2) || (fc.v2 == e[i].v1 && fc.v1 == e[i].v2) || (fc.v3 == e[i].v1 && fc.v2 == e[i].v2)) {
                face.addElement(new int[] { v0, v1, v2, -1 });
                face.addElement(new int[] { v2, v3, v0, -1 });
            } else {
                face.addElement(new int[] { v1, v0, v2, -1 });
                face.addElement(new int[] { v3, v2, v0, -1 });
            }
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
    // Record which edges should be selected. 
    newSelection = new boolean[newe.length];
    for (int i = 0; i < e.length; i++) if (selected[i]) {
        int v1 = vertIndex[e[i].v1];
        int v2 = vertIndex[e[i].v2];
        for (int j = 0; j < newe.length; j++) if ((v1 == newe[j].v1 && v2 == newe[j].v2) || (v1 == newe[j].v2 && v2 == newe[j].v1)) {
            newSelection[j] = true;
            break;
        }
    }
    return mesh;
}
