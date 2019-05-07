/** SplineMeshes cannot be keyframed directly, since any change to mesh topology would
      cause all keyframes to become invalid.  Return an actor for this mesh. */
public Object3D getPosableObject() {
    SplineMesh m = (SplineMesh) duplicate();
    return new Actor(m);
}
