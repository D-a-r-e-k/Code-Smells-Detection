/** Find all the photon sources in the scene, and generate the photons in a PhotonMap. */
protected void generatePhotons(PhotonMap map) {
    List<PhotonSourceFactory> factories = PluginRegistry.getPlugins(PhotonSourceFactory.class);
    ArrayList<PhotonSource> sources = new ArrayList<PhotonSource>();
    for (RTLight lt : light) {
        // First give plugins a chance to handle it. 
        boolean processed = false;
        for (PhotonSourceFactory factory : factories) if (factory.processLight(lt, map, sources)) {
            processed = true;
            break;
        }
        if (processed)
            continue;
        // Process it in the default way. 
        if (lt.getLight() instanceof DirectionalLight)
            sources.add(new DirectionalPhotonSource((DirectionalLight) lt.getLight(), lt.getCoords(), map));
        else if (lt.getLight() instanceof PointLight)
            sources.add(new PointPhotonSource((PointLight) lt.getLight(), lt.getCoords(), map));
        else if (lt.getLight() instanceof SpotLight)
            sources.add(new SpotlightPhotonSource((SpotLight) lt.getLight(), lt.getCoords(), map));
    }
    ArrayList<PhotonSource> objectSources = new ArrayList<PhotonSource>();
    for (RTObject obj : sceneObject) {
        // First give plugins a chance to handle it. 
        boolean processed = false;
        for (PhotonSourceFactory factory : factories) if (factory.processObject(obj, map, sources)) {
            processed = true;
            break;
        }
        if (processed)
            continue;
        // Process it in the default way. 
        if (!obj.getTextureMapping().getTexture().hasComponent(Texture.EMISSIVE_COLOR_COMPONENT))
            continue;
        PhotonSource src;
        if (obj instanceof RTTriangle)
            src = new TrianglePhotonSource(((RTTriangle) obj).tri, map);
        else if (obj instanceof RTTriangleLowMemory)
            src = new TrianglePhotonSource(((RTTriangleLowMemory) obj).tri, map);
        else if (obj instanceof RTDisplacedTriangle)
            src = new DisplacedTrianglePhotonSource((RTDisplacedTriangle) obj, map);
        else if (obj instanceof RTEllipsoid)
            src = new EllipsoidPhotonSource((RTEllipsoid) obj, map);
        else if (obj instanceof RTSphere)
            src = new EllipsoidPhotonSource((RTSphere) obj, map);
        else if (obj instanceof RTCylinder)
            src = new CylinderPhotonSource((RTCylinder) obj, map);
        else if (obj instanceof RTCube)
            src = new CubePhotonSource((RTCube) obj, map);
        else
            continue;
        if (src.getTotalIntensity() > 0.0)
            objectSources.add(src);
    }
    if (objectSources.size() > 0)
        sources.add(new CompoundPhotonSource(objectSources.toArray(new PhotonSource[objectSources.size()])));
    sources.add(new EnvironmentPhotonSource(theScene, map));
    PhotonSource src[] = sources.toArray(new PhotonSource[sources.size()]);
    map.generatePhotons(src);
}
