/** Build the octree. */
protected void buildTree() {
    BoundingBox objBounds[] = new BoundingBox[sceneObject.length];
    double minx, maxx, miny, maxy, minz, maxz;
    int i;
    // Find the bounding boxes for each object, and for the entire scene. 
    minx = miny = minz = Double.MAX_VALUE;
    maxx = maxy = maxz = -Double.MAX_VALUE;
    for (i = 0; i < sceneObject.length; i++) {
        objBounds[i] = sceneObject[i].getBounds();
        if (objBounds[i].minx < minx)
            minx = objBounds[i].minx;
        if (objBounds[i].maxx > maxx)
            maxx = objBounds[i].maxx;
        if (objBounds[i].miny < miny)
            miny = objBounds[i].miny;
        if (objBounds[i].maxy > maxy)
            maxy = objBounds[i].maxy;
        if (objBounds[i].minz < minz)
            minz = objBounds[i].minz;
        if (objBounds[i].maxz > maxz)
            maxz = objBounds[i].maxz;
    }
    minx -= TOL;
    miny -= TOL;
    minz -= TOL;
    maxx += TOL;
    maxy += TOL;
    maxz += TOL;
    // Create the octree. 
    rootNode = new OctreeNode(minx, maxx, miny, maxy, minz, maxz, sceneObject, objBounds, null);
    // Find the nodes which contain the camera and the lights. 
    cameraNode = rootNode.findNode(theCamera.getCameraCoordinates().getOrigin());
    lightNode = new OctreeNode[light.length];
    for (i = 0; i < light.length; i++) {
        if (light[i].getLight() instanceof DirectionalLight)
            lightNode[i] = null;
        else
            lightNode[i] = rootNode.findNode(light[i].getCoords().getOrigin());
    }
}
