/**
     * Some image formats, like TIFF may present the images rotated that have
     * to be compensated.
     * @param initialRotation New value of property initialRotation.
     */
public void setInitialRotation(float initialRotation) {
    float old_rot = rotationRadians - this.initialRotation;
    this.initialRotation = initialRotation;
    setRotation(old_rot);
}
