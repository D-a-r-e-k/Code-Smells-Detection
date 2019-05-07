/** Add a single object to the scene. */
protected void addObject(List<RTObject> obj, List<RTLight> lt, ObjectInfo info, Camera camera, Thread mainThread, List<RTObjectFactory> factories) {
    boolean displaced = false;
    double tol;
    if (renderThread != mainThread)
        return;
    // First give plugins a chance to handle the object. 
    for (RTObjectFactory factory : factories) if (factory.processObject(info, theScene, camera, obj, lt))
        return;
    // Handle it in the default way. 
    Object3D theObject = info.getObject();
    Mat4 toLocal = info.getCoords().toLocal();
    Mat4 fromLocal = info.getCoords().fromLocal();
    if (theObject instanceof PointLight) {
        lt.add(new RTSphericalLight((PointLight) theObject, info.getCoords(), penumbra));
        return;
    }
    if (theObject instanceof SpotLight) {
        lt.add(new RTSphericalLight((SpotLight) theObject, info.getCoords(), penumbra));
        return;
    }
    if (theObject instanceof DirectionalLight) {
        lt.add(new RTDirectionalLight((DirectionalLight) theObject, info.getCoords(), penumbra));
        return;
    }
    while (theObject instanceof ObjectWrapper) theObject = ((ObjectWrapper) theObject).getWrappedObject();
    if (theObject instanceof ObjectCollection) {
        Enumeration enm = ((ObjectCollection) theObject).getObjects(info, false, theScene);
        while (enm.hasMoreElements()) {
            ObjectInfo elem = (ObjectInfo) enm.nextElement();
            if (!elem.isVisible())
                continue;
            ObjectInfo copy = elem.duplicate();
            copy.getCoords().transformCoordinates(fromLocal);
            addObject(obj, lt, copy, camera, mainThread, factories);
        }
        return;
    }
    Vec3 cameraOrig = camera.getCameraCoordinates().getOrigin();
    double distToScreen = theCamera.getDistToScreen();
    if (adaptive) {
        double dist = info.getBounds().distanceToPoint(toLocal.times(cameraOrig));
        if (dist < distToScreen)
            tol = surfaceError;
        else
            tol = surfaceError * dist / distToScreen;
    } else
        tol = surfaceError;
    Texture tex = theObject.getTexture();
    if (tex != null && tex.hasComponent(Texture.DISPLACEMENT_COMPONENT)) {
        displaced = true;
        if (theObject.canConvertToTriangleMesh() != Object3D.CANT_CONVERT) {
            TriangleMesh tm = theObject.convertToTriangleMesh(tol);
            tm.setTexture(tex, theObject.getTextureMapping().duplicate());
            if (theObject.getMaterialMapping() != null)
                tm.setMaterial(theObject.getMaterial(), theObject.getMaterialMapping().duplicate());
            theObject = tm;
        }
    }
    if (!info.isDistorted()) {
        if (theObject instanceof Sphere) {
            Vec3 rad = ((Sphere) theObject).getRadii();
            if (rad.x == rad.y && rad.x == rad.z) {
                obj.add(new RTSphere((Sphere) theObject, fromLocal, toLocal, info.getObject().getAverageParameterValues()));
                return;
            } else {
                obj.add(new RTEllipsoid((Sphere) theObject, fromLocal, toLocal, info.getObject().getAverageParameterValues()));
                return;
            }
        } else if (theObject instanceof Cylinder) {
            obj.add(new RTCylinder((Cylinder) theObject, fromLocal, toLocal, info.getObject().getAverageParameterValues()));
            return;
        } else if (theObject instanceof Cube) {
            obj.add(new RTCube((Cube) theObject, fromLocal, toLocal, info.getObject().getAverageParameterValues()));
            return;
        } else if (theObject instanceof ImplicitObject && ((ImplicitObject) theObject).getPreferDirectRendering()) {
            obj.add(new RTImplicitObject((ImplicitObject) theObject, fromLocal, toLocal, info.getObject().getAverageParameterValues(), tol));
            return;
        }
    }
    RenderingMesh mesh = info.getRenderingMesh(tol);
    if (mesh == null)
        return;
    mesh.transformMesh(fromLocal);
    Vec3 vert[] = mesh.vert;
    RenderingTriangle t[] = mesh.triangle;
    if (displaced) {
        Vec3 cameraZDir = camera.getCameraCoordinates().getZDirection();
        double vertTol[] = new double[vert.length];
        if (adaptive)
            for (int i = 0; i < vert.length; i++) {
                Vec3 offset = vert[i].minus(cameraOrig);
                double vertDist = offset.length();
                if (offset.dot(cameraZDir) < 0.0)
                    vertDist = -vertDist;
                vertTol[i] = (vertDist < distToScreen ? surfaceError : surfaceError * vertDist / distToScreen);
            }
        for (int i = 0; i < t.length; i++) {
            RenderingTriangle tri = mesh.triangle[i];
            if (mesh.faceNorm[i].length() < TOL)
                continue;
            if (vert[tri.v1].distance(vert[tri.v2]) < TOL)
                continue;
            if (vert[tri.v1].distance(vert[tri.v3]) < TOL)
                continue;
            if (vert[tri.v2].distance(vert[tri.v3]) < TOL)
                continue;
            double localTol;
            if (adaptive) {
                localTol = vertTol[tri.v1];
                if (vertTol[tri.v2] < localTol)
                    localTol = vertTol[tri.v2];
                if (vertTol[tri.v3] < localTol)
                    localTol = vertTol[tri.v3];
            } else
                localTol = tol;
            RTDisplacedTriangle dispTri = new RTDisplacedTriangle(mesh, i, fromLocal, toLocal, localTol, time);
            RTObject dt = dispTri;
            if (!dispTri.isReallyDisplaced()) {
                if (reducedMemory)
                    dt = new RTTriangleLowMemory(mesh, i, fromLocal, toLocal);
                else
                    dt = new RTTriangle(mesh, i, fromLocal, toLocal);
            }
            obj.add(dt);
            if (adaptive && dt instanceof RTDisplacedTriangle) {
                double dist = dt.getBounds().distanceToPoint(cameraOrig);
                if (dist < distToScreen)
                    ((RTDisplacedTriangle) dt).setTolerance(surfaceError);
                else
                    ((RTDisplacedTriangle) dt).setTolerance(surfaceError * dist / distToScreen);
            }
            if (renderThread != mainThread)
                return;
        }
    } else
        for (int i = 0; i < t.length; i++) {
            RenderingTriangle tri = mesh.triangle[i];
            if (mesh.faceNorm[i].length() < TOL)
                continue;
            if (vert[tri.v1].distance(vert[tri.v2]) < TOL)
                continue;
            if (vert[tri.v1].distance(vert[tri.v3]) < TOL)
                continue;
            if (vert[tri.v2].distance(vert[tri.v3]) < TOL)
                continue;
            if (reducedMemory)
                obj.add(new RTTriangleLowMemory(mesh, i, fromLocal, toLocal));
            else
                obj.add(new RTTriangle(mesh, i, fromLocal, toLocal));
        }
}
