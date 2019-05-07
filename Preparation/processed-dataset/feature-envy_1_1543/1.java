/** Apply the Distortion, and return a transformed mesh. */
public Mesh transform(Mesh obj) {
    if (previous != null)
        obj = previous.transform(obj);
    Mesh newmesh = (Mesh) obj.duplicate();
    MeshVertex[] vert = newmesh.getVertices();
    Vec3 newvert[] = new Vec3[vert.length];
    Vec3 origin = new Vec3();
    for (int i = 0; i < newvert.length; i++) {
        newvert[i] = vert[i].r;
        if (preTransform != null)
            preTransform.transform(newvert[i]);
    }
    if (preTransform != null)
        preTransform.transform(origin);
    // Find the range along the appropriate axis. 
    double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
    for (int i = 0; i < newvert.length; i++) {
        double value;
        if (axis == X_AXIS)
            value = newvert[i].x;
        else if (axis == Y_AXIS)
            value = newvert[i].y;
        else
            value = newvert[i].z;
        if (value < min)
            min = value;
        if (value > max)
            max = value;
    }
    if (min >= max)
        return obj;
    if (!forward) {
        double temp = min;
        min = max;
        max = temp;
    }
    double theta = angle * (Math.PI / 180.0);
    double scale = theta / (max - min);
    double radius = (max - min) / theta;
    Vec3 center;
    if (axis == X_AXIS) {
        if (direction == Y_AXIS)
            for (int i = 0; i < newvert.length; i++) {
                double a = scale * (newvert[i].x - min);
                double b = newvert[i].y - origin.y - radius;
                newvert[i].set(min - Math.sin(a) * b, origin.y + radius + Math.cos(a) * b, newvert[i].z);
            }
        else
            for (int i = 0; i < newvert.length; i++) {
                double a = scale * (newvert[i].x - min);
                double b = newvert[i].z - origin.z - radius;
                newvert[i].set(min - Math.sin(a) * b, newvert[i].y, origin.z + radius + Math.cos(a) * b);
            }
    } else if (axis == Y_AXIS) {
        if (direction == X_AXIS)
            for (int i = 0; i < newvert.length; i++) {
                double a = scale * (newvert[i].y - min);
                double b = newvert[i].x - origin.x - radius;
                newvert[i].set(origin.x + radius + Math.cos(a) * b, min - Math.sin(a) * b, newvert[i].z);
            }
        else
            for (int i = 0; i < newvert.length; i++) {
                double a = scale * (newvert[i].y - min);
                double b = newvert[i].z - origin.z - radius;
                newvert[i].set(newvert[i].x, min - Math.sin(a) * b, origin.z + radius + Math.cos(a) * b);
            }
    } else {
        if (direction == X_AXIS)
            for (int i = 0; i < newvert.length; i++) {
                double a = scale * (newvert[i].z - min);
                double b = newvert[i].x - origin.x - radius;
                newvert[i].set(origin.x + radius + Math.cos(a) * b, newvert[i].y, min - Math.sin(a) * b);
            }
        else
            for (int i = 0; i < newvert.length; i++) {
                double a = scale * (newvert[i].z - min);
                double b = newvert[i].y - origin.y - radius;
                newvert[i].set(newvert[i].x, origin.y + radius + Math.cos(a) * b, min - Math.sin(a) * b);
            }
    }
    if (postTransform != null)
        for (int i = 0; i < newvert.length; i++) postTransform.transform(newvert[i]);
    newmesh.setVertexPositions(newvert);
    return newmesh;
}
