/** Build the photon maps. */
protected void buildPhotonMap() {
    if (giMode != GI_PHOTON && giMode != GI_HYBRID && !caustics && scatterMode != SCATTER_PHOTONS && scatterMode != SCATTER_BOTH)
        return;
    PhotonMap shared = null;
    if (giMode == GI_PHOTON) {
        listener.statusChanged("Building Global Photon Map");
        globalMap = shared = new PhotonMap(globalPhotons, globalNeighborPhotons, false, false, true, false, this, rootNode, 1, null);
        generatePhotons(globalMap);
    } else if (giMode == GI_HYBRID) {
        listener.statusChanged("Building Global Photon Map");
        globalMap = shared = new PhotonMap(globalPhotons, globalNeighborPhotons, true, true, true, false, this, rootNode, 0, null);
        generatePhotons(globalMap);
    }
    if (caustics) {
        // Find a bounding box around all objects that can generate caustics. 
        BoundingBox bounds = null;
        for (RTObject obj : sceneObject) {
            Texture tex = obj.getTextureMapping().getTexture();
            MaterialMapping mm = obj.getMaterialMapping();
            if (tex.hasComponent(Texture.SPECULAR_COLOR_COMPONENT) || (tex.hasComponent(Texture.TRANSPARENT_COLOR_COMPONENT) && mm != null && mm.getMaterial().indexOfRefraction() != 1.0)) {
                if (bounds == null)
                    bounds = obj.getBounds();
                else
                    bounds = bounds.merge(obj.getBounds());
            }
        }
        if (bounds == null)
            bounds = new BoundingBox(0, 0, 0, 0, 0, 0);
        listener.statusChanged("Building Caustics Photon Map");
        causticsMap = shared = new PhotonMap(causticsPhotons, causticsNeighborPhotons, true, false, false, false, this, bounds, 2, shared);
        generatePhotons(causticsMap);
    }
    if (scatterMode == SCATTER_PHOTONS || scatterMode == SCATTER_BOTH) {
        // Find a bounding box around all objects with scattering materials. 
        BoundingBox bounds = null;
        for (RTObject obj : sceneObject) {
            Texture tex = obj.getTextureMapping().getTexture();
            MaterialMapping mm = obj.getMaterialMapping();
            if (tex.hasComponent(Texture.TRANSPARENT_COLOR_COMPONENT) && mm != null && mm.getMaterial().isScattering()) {
                if (bounds == null)
                    bounds = obj.getBounds();
                else
                    bounds = bounds.merge(obj.getBounds());
            }
        }
        if (bounds == null)
            bounds = new BoundingBox(0, 0, 0, 0, 0, 0);
        listener.statusChanged("Building Volume Photon Map");
        volumeMap = new PhotonMap(volumePhotons, volumeNeighborPhotons, false, scatterMode == SCATTER_PHOTONS, true, true, this, bounds, 0, shared);
        generatePhotons(volumeMap);
    }
}
