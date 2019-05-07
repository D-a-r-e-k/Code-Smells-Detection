/** Setup the final mesh. */
private void prepareMesh(TriangleMesh mesh, Vector face, Vector vert, int vertIndex[]) {
    int newface[][] = new int[face.size()][];
    Vertex newvert[] = new Vertex[vert.size()];
    for (int i = 0; i < face.size(); i++) {
        int f[] = (int[]) face.elementAt(i);
        newface[i] = new int[] { f[0], f[1], f[2] };
    }
    for (int i = 0; i < vert.size(); i++) newvert[i] = (Vertex) vert.elementAt(i);
    mesh.setShape(newvert, newface);
    ParameterValue oldParam[] = mesh.getParameterValues();
    ParameterValue newParam[] = new ParameterValue[oldParam.length];
    for (int i = 0; i < oldParam.length; i++) {
        if (oldParam[i] instanceof VertexParameterValue) {
            double oldval[] = ((VertexParameterValue) oldParam[i]).getValue();
            double newval[] = new double[vert.size()];
            double defaultVal = mesh.getParameters()[i].defaultVal;
            for (int j = 0; j < newval.length; j++) newval[j] = defaultVal;
            if (vertIndex != null)
                for (int j = 0; j < vertIndex.length; j++) newval[vertIndex[j]] = oldval[j];
            else
                for (int j = 0; j < oldval.length; j++) newval[j] = oldval[j];
            newParam[i] = new VertexParameterValue(newval);
        } else if (oldParam[i] instanceof FaceParameterValue) {
            double oldval[] = ((FaceParameterValue) oldParam[i]).getValue();
            double newval[] = new double[face.size()];
            double defaultVal = mesh.getParameters()[i].defaultVal;
            for (int j = 0; j < newval.length; j++) {
                int f[] = (int[]) face.elementAt(j);
                if (f[3] == -1)
                    newval[j] = defaultVal;
                else
                    newval[j] = oldval[f[3]];
            }
            newParam[i] = new FaceParameterValue(newval);
        } else if (oldParam[i] instanceof FaceVertexParameterValue) {
            FaceVertexParameterValue fvpv = (FaceVertexParameterValue) oldParam[i];
            double newval[][] = new double[face.size()][3];
            double defaultVal = mesh.getParameters()[i].defaultVal;
            for (int j = 0; j < face.size(); j++) {
                int f[] = (int[]) face.elementAt(j);
                if (f[3] == -1) {
                    newval[j][0] = defaultVal;
                    newval[j][1] = defaultVal;
                    newval[j][2] = defaultVal;
                } else {
                    newval[j][0] = fvpv.getValue(f[3], 0);
                    newval[j][1] = fvpv.getValue(f[3], 1);
                    newval[j][2] = fvpv.getValue(f[3], 2);
                }
            }
            newParam[i] = new FaceVertexParameterValue(newval);
        } else
            newParam[i] = oldParam[i];
    }
    mesh.setParameterValues(newParam);
}
