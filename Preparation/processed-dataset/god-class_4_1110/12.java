/** Main method in which the image is rendered. */
public void run() {
    final Thread thisThread = Thread.currentThread();
    if (renderThread != thisThread)
        return;
    updateTime = System.currentTimeMillis();
    // Record information about the scene. 
    findLights();
    ambColor = theScene.getAmbientColor();
    envColor = theScene.getEnvironmentColor();
    envMapping = theScene.getEnvironmentMapping();
    envMode = theScene.getEnvironmentMode();
    fogColor = theScene.getFogColor();
    fog = theScene.getFogState();
    fogDist = theScene.getFogDistance();
    ParameterValue envParam[] = theScene.getEnvironmentParameterValues();
    envParamValue = new double[envParam.length];
    for (int i = 0; i < envParamValue.length; i++) envParamValue[i] = envParam[i].getAverageValue();
    // Determine information about the viewpoint. 
    final Vec3 viewdir = theCamera.getViewToWorld().timesDirection(Vec3.vz());
    Point p = new Point(width / 2, height / 2);
    final Vec3 orig = theCamera.getCameraCoordinates().getOrigin();
    Vec3 center = theCamera.convertScreenToWorld(p, focalDist);
    p.x++;
    Vec3 hvec = theCamera.convertScreenToWorld(p, focalDist).minus(center);
    p.x--;
    p.y++;
    Vec3 vvec = theCamera.convertScreenToWorld(p, focalDist).minus(center);
    p.y--;
    smoothScale = smoothing * hvec.length() / focalDist;
    // Render the objects. 
    final ObjectInfo sortedObjects[] = sortObjects();
    ThreadManager threads = new ThreadManager(sortedObjects.length, new ThreadManager.Task() {

        public void execute(int index) {
            RasterContext context = (RasterContext) threadRasterContext.get();
            ObjectInfo obj = sortedObjects[index];
            context.camera.setObjectTransform(obj.getCoords().fromLocal());
            renderObject(obj, orig, viewdir, obj.getCoords().toLocal(), context, thisThread);
            if (thisThread != renderThread)
                return;
            if (System.currentTimeMillis() - updateTime > 5000)
                updateImage();
        }

        public void cleanup() {
            ((RasterContext) threadRasterContext.get()).cleanup();
        }
    });
    threads.run();
    threads.finish();
    finish(createFinalImage(center, orig, hvec, vvec));
}
