/** Create an exact duplicate of this object. */
public Object3D duplicate() {
    Curve c = (Curve) super.duplicate();
    double t[] = new double[thickness.length];
    System.arraycopy(thickness, 0, t, 0, t.length);
    Tube tube = new Tube(c, t, endsStyle);
    tube.copyTextureAndMaterial(this);
    return tube;
}
