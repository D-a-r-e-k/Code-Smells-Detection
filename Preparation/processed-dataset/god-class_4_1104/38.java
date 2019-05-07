/** When setting texture parameters, we need to clear the caches. */
public void setParameterValue(TextureParameter param, ParameterValue val) {
    super.setParameterValue(param, val);
    cachedMesh = null;
}
