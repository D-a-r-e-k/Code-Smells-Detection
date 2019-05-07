/**
     * Gets the current image rotation in radians.
     * @return the current image rotation in radians
     */
public float getImageRotation() {
    double d = 2.0 * Math.PI;
    float rot = (float) ((rotationRadians - initialRotation) % d);
    if (rot < 0) {
        rot += d;
    }
    return rot;
}
