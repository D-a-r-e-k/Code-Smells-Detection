/**
   * Construct a new mesh by beveling and extruding the original one.
   *
   * @param height      the extrude height
   * @param width       the bevel width
   */
public TriangleMesh bevelMesh(double height, double width) {
    switch(mode) {
        case BEVEL_FACES:
            return bevelIndividualFaces(height, width);
        case BEVEL_FACE_GROUPS:
            return bevelFacesAsGroup(height, width);
        case BEVEL_EDGES:
            return bevelEdges(height, width);
        case BEVEL_VERTICES:
            return bevelVertices(height, width);
    }
    return null;
}
