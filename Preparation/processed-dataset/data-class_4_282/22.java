/**
   * Create a Fragment object.
   *
   * @param addColor   the additive color in ERGB format
   * @param multColor  the multiplicative color in ERGB format
   * @param depth      the depth of the fragment
   * @param material   a description of the material for the object being rendered
   * @param isBackface true if this triangle faces away from the camera
   */
private Fragment createFragment(int addColor, int multColor, float depth, ObjectMaterialInfo material, boolean isBackface) {
    if (multColor == 0) {
        // It is fully opaque. 
        return new OpaqueFragment(addColor, depth);
    } else if (addColor == 0 && multColor == WHITE_ERGB && material == null)
        return null;
    else {
        if (material == null)
            return new TransparentFragment(addColor, multColor, depth, BACKGROUND_FRAGMENT);
        return new MaterialFragment(addColor, multColor, depth, BACKGROUND_FRAGMENT, material, !isBackface);
    }
}
