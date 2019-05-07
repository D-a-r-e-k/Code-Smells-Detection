/** When setting the texture, we need to clear the cached meshes. */
public void setTexture(Texture tex, TextureMapping mapping) {
    super.setTexture(tex, mapping);
    cachedMesh = null;
    cachedWire = null;
}
