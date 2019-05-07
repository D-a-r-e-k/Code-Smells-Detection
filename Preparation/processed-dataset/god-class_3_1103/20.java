/** Get a wireframe mesh representing the surface of this object at the
      specified accuracy. */
public WireframeMesh getWireframeMesh() {
    if (cachedWire != null)
        return cachedWire;
    return (cachedWire = convertToTriangleMesh(ArtOfIllusion.getPreferences().getInteractiveSurfaceError()).getWireframeMesh());
}
