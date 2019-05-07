/** Tubes cannot be keyframed directly, since any change to mesh topology would
      cause all keyframes to become invalid.  Return an actor for this mesh. */
public Object3D getPosableObject() {
    Tube m = (Tube) duplicate();
    return new Actor(m);
}
