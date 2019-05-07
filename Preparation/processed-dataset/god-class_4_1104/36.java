/** When setting the texture, we need to clear the caches. */
public void setTexture(Texture tex, TextureMapping mapping) {
    super.setTexture(tex, mapping);
    cachedMesh = null;
    cachedWire = null;
}
