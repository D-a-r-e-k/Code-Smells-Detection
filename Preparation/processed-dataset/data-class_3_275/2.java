/** Make this object identical to another one. */
public void copyObject(Object3D obj) {
    Tube t = (Tube) obj;
    super.copyObject(t);
    thickness = new double[t.thickness.length];
    System.arraycopy(t.thickness, 0, thickness, 0, thickness.length);
    endsStyle = t.endsStyle;
    copyTextureAndMaterial(obj);
}
