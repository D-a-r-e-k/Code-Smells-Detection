/**
   * Construct a new mesh, beveling all selected faces as a group.
   *
   * @param height      the extrude height
   * @param width       the bevel width
   */
private TriangleMesh bevelFacesAsGroup(double height, double width) {
    mesh = (TriangleMesh) origMesh.duplicate();
    if (width == 0.0 && height == 0.0) {
        newSelection = selected;
        return mesh;
    }
    Vertex v[] = (Vertex[]) mesh.getVertices();
    Edge e[] = mesh.getEdges();
    Face f1, f2, f[] = mesh.getFaces();
    int i, j, k, m, n, newface[][], vertFace[][], numVertFaces[], tempFace[], bev[];
    int group[], touchCount[], groupCount;
    Vec3 temp = new Vec3();
    Vector face = new Vector(), vert = new Vector(), bevel = new Vector();
    boolean someSelected[] = new boolean[v.length], allSelected[] = new boolean[v.length];
    boolean touching[][], inGroup[], beveled[] = new boolean[e.length];
    double coeff[][] = new double[3][3], rhs[] = new double[3];
    newIndex = new Vector();
    findEdgeInsets(height, width);
    // First copy over the old faces and vertices.  (Some of these will be modified later.) 
    for (i = 0; i < v.length; i++) vert.addElement(v[i]);
    for (i = 0; i < f.length; i++) face.addElement(new int[] { f[i].v1, f[i].v2, f[i].v3, i });
    // Each vertex goes into one of three categories: NONE of the faces touching it are 
    // selected, ALL of the faces touching it are selected, or (the complicated case) SOME 
    // of the faces touching it are selected. 
    for (i = 0; i < v.length; i++) allSelected[i] = true;
    for (i = 0; i < f.length; i++) {
        if (selected[i])
            someSelected[f[i].v1] = someSelected[f[i].v2] = someSelected[f[i].v3] = true;
        else
            allSelected[f[i].v1] = allSelected[f[i].v2] = allSelected[f[i].v3] = false;
    }
    // For each vertex, build a list of all the selected faces that touch it. 
    vertFace = new int[v.length][];
    numVertFaces = new int[v.length];
    for (i = 0; i < f.length; i++) if (selected[i]) {
        numVertFaces[f[i].v1]++;
        numVertFaces[f[i].v2]++;
        numVertFaces[f[i].v3]++;
    }
    for (i = 0; i < v.length; i++) {
        vertFace[i] = new int[numVertFaces[i]];
        numVertFaces[i] = 0;
    }
    for (i = 0; i < f.length; i++) if (selected[i]) {
        vertFace[f[i].v1][numVertFaces[f[i].v1]++] = i;
        vertFace[f[i].v2][numVertFaces[f[i].v2]++] = i;
        vertFace[f[i].v3][numVertFaces[f[i].v3]++] = i;
    }
    // If ALL or NONE of the faces touching a vertex are selected, everything is simple. 
    // If SOME but not all are selected, things get complicated.  We will need to keep the 
    // original vertex, but also create a displaced one.  Furthermore, the selected faces 
    // may not all touch each other (that is, share edges).  In this case, we need to sort 
    // the selected faces into groups which touch each other, and there will be a different 
    // displaced vertex for each group. 
    for (i = 0; i < v.length; i++) {
        if (allSelected[i]) {
            // Find the new position for this vertex.  It is not on the boundary of the 
            // selection, so we don't need to worry about insets.  Ideally, we would like  
            // it to be the correct height from every one of the faces, but in general  
            // this is not possible.  We choose the position to minimize the sum of the 
            // squared height deviations. 
            for (j = 0; j < 3; j++) rhs[j] = coeff[j][0] = coeff[j][1] = coeff[j][2] = 0.0;
            for (j = 0; j < numVertFaces[i]; j++) {
                coeff[0][0] += faceNormal[vertFace[i][j]].x * faceNormal[vertFace[i][j]].x;
                coeff[0][1] += faceNormal[vertFace[i][j]].x * faceNormal[vertFace[i][j]].y;
                coeff[0][2] += faceNormal[vertFace[i][j]].x * faceNormal[vertFace[i][j]].z;
                coeff[1][1] += faceNormal[vertFace[i][j]].y * faceNormal[vertFace[i][j]].y;
                coeff[1][2] += faceNormal[vertFace[i][j]].y * faceNormal[vertFace[i][j]].z;
                coeff[2][2] += faceNormal[vertFace[i][j]].z * faceNormal[vertFace[i][j]].z;
                rhs[0] += height * faceNormal[vertFace[i][j]].x;
                rhs[1] += height * faceNormal[vertFace[i][j]].y;
                rhs[2] += height * faceNormal[vertFace[i][j]].z;
                newIndex.addElement(new Integer(vertFace[i][j]));
            }
            coeff[1][0] = coeff[0][1];
            coeff[2][0] = coeff[0][2];
            coeff[2][1] = coeff[1][2];
            SVD.solve(coeff, rhs, 1e-3);
            temp.set(rhs[0], rhs[1], rhs[2]);
            vert.setElementAt(offsetVertex(mesh, (Vertex) vert.elementAt(i), temp), i);
        } else if (someSelected[i]) {
            // Find which faces directly touch each other. 
            touching = new boolean[numVertFaces[i]][numVertFaces[i]];
            for (j = 1; j < numVertFaces[i]; j++) for (k = 0; k < j; k++) {
                f1 = f[vertFace[i][j]];
                f2 = f[vertFace[i][k]];
                if (f1.e1 == f2.e1 || f1.e1 == f2.e2 || f1.e1 == f2.e3 || f1.e2 == f2.e1 || f1.e2 == f2.e2 || f1.e2 == f2.e3 || f1.e3 == f2.e1 || f1.e3 == f2.e2 || f1.e3 == f2.e3)
                    touching[j][k] = touching[k][j] = true;
            }
            // Count the number of other faces each face is touching. 
            touchCount = new int[numVertFaces[i]];
            for (j = 0; j < numVertFaces[i]; j++) for (k = 0; k < numVertFaces[i]; k++) touchCount[j] += touching[j][k] ? 1 : 0;
            // Find the groups. 
            inGroup = new boolean[numVertFaces[i]];
            group = new int[numVertFaces[i]];
            while (true) {
                groupCount = 0;
                // Find the first face in the new group. 
                for (j = 0; j < numVertFaces[i] && (inGroup[j] == true || touchCount[j] > 1); j++) ;
                if (j == numVertFaces[i])
                    break;
                inGroup[j] = true;
                group[0] = j;
                groupCount = 1;
                // Find the rest of the faces in the group. 
                while (touchCount[j] > (groupCount == 1 ? 0 : 1)) for (j = 0; j < numVertFaces[i]; j++) if (!inGroup[j] && touching[group[groupCount - 1]][j]) {
                    group[groupCount++] = j;
                    inGroup[j] = true;
                    break;
                }
                // We also will need to add two faces to define the beveled edge at the 
                // start of the group, and another two at the end of the group.  We can't 
                // actually add them yet, since not all vertices have been determined, so 
                // instead we record the edges which need this. 
                if (groupCount == 1) {
                    // If the group consists of only one face, then simply find the two edges 
                    // which share this vertex. 
                    f1 = f[vertFace[i][group[0]]];
                    m = n = -1;
                    if (e[f1.e1].v1 == i || e[f1.e1].v2 == i) {
                        bevel.addElement(new int[] { vertFace[i][group[0]], f1.e1 });
                        m = 0;
                    }
                    if (e[f1.e2].v1 == i || e[f1.e2].v2 == i) {
                        bevel.addElement(new int[] { vertFace[i][group[0]], f1.e2 });
                        if (m == -1)
                            m = 1;
                        else
                            n = 1;
                    }
                    if (e[f1.e3].v1 == i || e[f1.e3].v2 == i) {
                        bevel.addElement(new int[] { vertFace[i][group[0]], f1.e3 });
                        n = 2;
                    }
                } else {
                    // We must find the edge of the first face which 1) shares this vertex, 
                    // and 2) is not shared by the second face. 
                    f1 = f[vertFace[i][group[0]]];
                    f2 = f[vertFace[i][group[1]]];
                    if ((e[f1.e1].v1 == i || e[f1.e1].v2 == i) && f2.e1 != f1.e1 && f2.e2 != f1.e1 && f2.e3 != f1.e1) {
                        bevel.addElement(new int[] { vertFace[i][group[0]], f1.e1 });
                        m = 0;
                    } else if ((e[f1.e2].v1 == i || e[f1.e2].v2 == i) && f2.e1 != f1.e2 && f2.e2 != f1.e2 && f2.e3 != f1.e2) {
                        bevel.addElement(new int[] { vertFace[i][group[0]], f1.e2 });
                        m = 1;
                    } else {
                        bevel.addElement(new int[] { vertFace[i][group[0]], f1.e3 });
                        m = 2;
                    }
                    // Similarly for the last face. 
                    f1 = f[vertFace[i][group[groupCount - 1]]];
                    f2 = f[vertFace[i][group[groupCount - 2]]];
                    if ((e[f1.e1].v1 == i || e[f1.e1].v2 == i) && f2.e1 != f1.e1 && f2.e2 != f1.e1 && f2.e3 != f1.e1) {
                        bevel.addElement(new int[] { vertFace[i][group[groupCount - 1]], f1.e1 });
                        n = 0;
                    } else if ((e[f1.e2].v1 == i || e[f1.e2].v2 == i) && f2.e1 != f1.e2 && f2.e2 != f1.e2 && f2.e3 != f1.e2) {
                        bevel.addElement(new int[] { vertFace[i][group[groupCount - 1]], f1.e2 });
                        n = 1;
                    } else {
                        bevel.addElement(new int[] { vertFace[i][group[groupCount - 1]], f1.e3 });
                        n = 2;
                    }
                }
                // Find the displaced vertex for this group.  Ideally, we would like to 
                // satisfy both the inset (width) and extrude (height) constraints for both 
                // the first and last triangles in the group, but in general this is not 
                // possible.  We settle for getting the insets, and the average of the two 
                // heights. 
                coeff[0][0] = faceInsets[vertFace[i][group[0]]][m].x;
                coeff[0][1] = faceInsets[vertFace[i][group[0]]][m].y;
                coeff[0][2] = faceInsets[vertFace[i][group[0]]][m].z;
                coeff[1][0] = faceInsets[vertFace[i][group[groupCount - 1]]][n].x;
                coeff[1][1] = faceInsets[vertFace[i][group[groupCount - 1]]][n].y;
                coeff[1][2] = faceInsets[vertFace[i][group[groupCount - 1]]][n].z;
                coeff[2][0] = faceNormal[vertFace[i][group[0]]].x + faceNormal[vertFace[i][group[groupCount - 1]]].x;
                coeff[2][1] = faceNormal[vertFace[i][group[0]]].y + faceNormal[vertFace[i][group[groupCount - 1]]].y;
                coeff[2][2] = faceNormal[vertFace[i][group[0]]].z + faceNormal[vertFace[i][group[groupCount - 1]]].z;
                rhs[0] = rhs[1] = width;
                rhs[2] = 2.0 * height;
                SVD.solve(coeff, rhs, 1e-3);
                temp.set(rhs[0], rhs[1], rhs[2]);
                vert.addElement(offsetVertex(mesh, (Vertex) vert.elementAt(i), temp));
                // Modify the faces to use the new vertex. 
                k = vert.size() - 1;
                for (j = 0; j < groupCount; j++) {
                    tempFace = (int[]) face.elementAt(vertFace[i][group[j]]);
                    f1 = f[vertFace[i][group[j]]];
                    if (f1.v1 == i)
                        tempFace[0] = k;
                    else if (f1.v2 == i)
                        tempFace[1] = k;
                    else
                        tempFace[2] = k;
                    newIndex.addElement(new Integer(vertFace[i][group[j]]));
                }
            }
        }
    }
    // Whew!  Done with that.  Now we need to add two faces for each edge which is at the 
    // end of a group. 
    for (i = 0; i < bevel.size(); i++) {
        bev = (int[]) bevel.elementAt(i);
        if (beveled[bev[1]])
            continue;
        beveled[bev[1]] = true;
        j = e[bev[1]].v1;
        k = e[bev[1]].v2;
        if ((f[bev[0]].v1 == k && f[bev[0]].v2 == j) || (f[bev[0]].v2 == k && f[bev[0]].v3 == j) || (f[bev[0]].v3 == k && f[bev[0]].v1 == j)) {
            m = j;
            j = k;
            k = m;
        }
        tempFace = (int[]) face.elementAt(bev[0]);
        if (f[bev[0]].v1 == j)
            m = tempFace[0];
        else if (f[bev[0]].v2 == j)
            m = tempFace[1];
        else
            m = tempFace[2];
        if (f[bev[0]].v1 == k)
            n = tempFace[0];
        else if (f[bev[0]].v2 == k)
            n = tempFace[1];
        else
            n = tempFace[2];
        face.addElement(new int[] { j, k, m, bev[0] });
        face.addElement(new int[] { m, k, n, bev[0] });
    }
    // There!  Nothing left to do but construct the new mesh. 
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
