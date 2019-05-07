/** When setting texture parameters, we need to clear the caches. */
public void setParameterValues(ParameterValue val[]) {
    super.setParameterValues(val);
    cachedMesh = null;
}
