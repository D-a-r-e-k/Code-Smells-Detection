/**
     * Set the diffuse and specular radiance emitted by the current light
     * source. These should usually be the same, but are distinguished to allow
     * for non-physical light setups or light source types which compute diffuse
     * and specular responses seperately.
     * 
     * @param d diffuse radiance
     * @param s specular radiance
     */
public void setRadiance(Color d, Color s) {
    ldiff = d.copy();
    lspec = s.copy();
}
