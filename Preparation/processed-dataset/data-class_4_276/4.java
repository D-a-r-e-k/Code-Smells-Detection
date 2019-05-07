/** Get the bounding box for the mesh.  This is always the bounding box for the unsmoothed
     control mesh.  If the smoothing method is set to approximating, the final surface may not
     actually touch the sides of this box.  If the smoothing method is set to interpolating,
     the final surface may actually extend outside this box. */
public BoundingBox getBounds() {
    if (bounds == null)
        findBounds();
    return bounds;
}
