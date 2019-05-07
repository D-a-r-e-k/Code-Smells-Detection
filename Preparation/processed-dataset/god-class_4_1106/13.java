/** Construct the list of RTObjects and lights in the scene. */
protected void buildScene(final Scene theScene, final Camera theCamera) {
    final List<RTObject> obj = Collections.synchronizedList(new ArrayList<RTObject>());
    final List<RTLight> lt = Collections.synchronizedList(new ArrayList<RTLight>());
    final Thread mainThread = Thread.currentThread();
    final List<RTObjectFactory> factories = PluginRegistry.getPlugins(RTObjectFactory.class);
    ThreadManager threads = new ThreadManager(theScene.getNumObjects(), new ThreadManager.Task() {

        public void execute(int index) {
            if (renderThread != mainThread)
                return;
            ObjectInfo info = theScene.getObject(index);
            if (info.isVisible())
                addObject(obj, lt, info, theCamera, mainThread, factories);
        }

        public void cleanup() {
        }
    });
    threads.run();
    threads.finish();
    sceneObject = new RTObject[obj.size()];
    for (int i = 0; i < sceneObject.length; i++) {
        sceneObject[i] = obj.get(i);
        sceneObject[i].index = i;
        if (sceneObject[i].getMaterialMapping() != null) {
            if (materialBounds == null)
                materialBounds = new BoundingBox(sceneObject[i].getBounds());
            else
                materialBounds.extend(sceneObject[i].getBounds());
        }
    }
    light = new RTLight[lt.size()];
    for (int i = 0; i < light.length; i++) light[i] = lt.get(i);
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
}
