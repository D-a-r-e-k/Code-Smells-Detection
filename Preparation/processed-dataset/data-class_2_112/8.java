/**
     * Compute a dot product between the current shadow ray direction and the
     * specified vector.
     * 
     * @param v direction vector
     * @return dot product of the vector with the shadow ray direction
     */
public float dot(Vector3 v) {
    return shadowRay.dot(v);
}
