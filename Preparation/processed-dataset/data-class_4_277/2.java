/** Get the RaytracerContext in which the map is being built. */
public RaytracerContext getContext() {
    return (RaytracerContext) getRaytracer().threadContext.get();
}
