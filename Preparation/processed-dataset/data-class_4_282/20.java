/** Render a single object into the scene.  viewdir is the direction from 
     which the object is being viewed in world coordinates. */
private void renderObject(ObjectInfo obj, Vec3 orig, Vec3 viewdir, Mat4 toLocal, RasterContext context, Thread mainThread) {
    RenderingMesh mesh;
    Object3D theObject;
    double tol;
    int i;
    if (mainThread != renderThread)
        return;
    if (!obj.isVisible())
        return;
    theObject = obj.getObject();
    if (context.camera.visibility(obj.getBounds()) == Camera.NOT_VISIBLE)
        return;
    while (theObject instanceof ObjectWrapper) theObject = ((ObjectWrapper) theObject).getWrappedObject();
    if (theObject instanceof ObjectCollection) {
        Enumeration objects = ((ObjectCollection) theObject).getObjects(obj, false, theScene);
        Mat4 fromLocal = context.camera.getObjectToWorld();
        while (objects.hasMoreElements()) {
            ObjectInfo elem = (ObjectInfo) objects.nextElement();
            CoordinateSystem coords = elem.getCoords().duplicate();
            coords.transformCoordinates(fromLocal);
            context.camera.setObjectTransform(coords.fromLocal());
            renderObject(elem, orig, viewdir, coords.toLocal(), context, mainThread);
        }
        return;
    }
    if (adaptive) {
        double dist = obj.getBounds().distanceToPoint(toLocal.times(orig));
        double distToScreen = context.camera.getDistToScreen();
        if (dist < distToScreen)
            tol = surfaceError;
        else
            tol = surfaceError * dist / distToScreen;
    } else
        tol = surfaceError;
    mesh = obj.getRenderingMesh(tol);
    if (mesh == null)
        return;
    if (mainThread != renderThread)
        return;
    viewdir = toLocal.timesDirection(viewdir);
    if (context.lightPosition == null) {
        context.lightPosition = new Vec3[light.length];
        context.lightDirection = new Vec3[light.length];
    }
    for (i = light.length - 1; i >= 0; i--) {
        context.lightPosition[i] = toLocal.times(light[i].getCoords().getOrigin());
        if (!(light[i].getObject() instanceof PointLight))
            context.lightDirection[i] = toLocal.timesDirection(light[i].getCoords().getZDirection());
    }
    boolean bumpMap = theObject.getTexture().hasComponent(Texture.BUMP_COMPONENT);
    boolean cullBackfaces = (hideBackfaces && theObject.isClosed() && !theObject.getTexture().hasComponent(Texture.TRANSPARENT_COLOR_COMPONENT));
    ObjectMaterialInfo material = null;
    if (theObject.getMaterialMapping() != null)
        material = new ObjectMaterialInfo(theObject.getMaterialMapping(), toLocal);
    if (theObject.getTexture().hasComponent(Texture.DISPLACEMENT_COMPONENT))
        renderMeshDisplaced(mesh, viewdir, tol, cullBackfaces, bumpMap, material, context);
    else if (shadingMode == GOURAUD)
        renderMeshGouraud(mesh, viewdir, cullBackfaces, material, context);
    else if (shadingMode == HYBRID && !bumpMap)
        renderMeshHybrid(mesh, viewdir, cullBackfaces, material, context);
    else
        renderMeshPhong(mesh, viewdir, cullBackfaces, bumpMap, material, context);
}
