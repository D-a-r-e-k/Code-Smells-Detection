/**
     * Trace the shadow ray, attenuating the sample's color by the opacity of
     * intersected objects.
     * 
     * @param state shading state representing the point to be shaded
     */
public final void traceShadow(ShadingState state) {
    Color opacity = state.traceShadow(shadowRay);
    Color.blend(ldiff, Color.BLACK, opacity, ldiff);
    Color.blend(lspec, Color.BLACK, opacity, lspec);
}
