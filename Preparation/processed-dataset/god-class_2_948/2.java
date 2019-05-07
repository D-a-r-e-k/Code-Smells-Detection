/**
     * Set the current shadow ray. The ray's direction is used as the sample's
     * orientation.
     * 
     * @param shadowRay shadow ray from the point being shaded towards the light
     */
public void setShadowRay(Ray shadowRay) {
    this.shadowRay = shadowRay;
}
