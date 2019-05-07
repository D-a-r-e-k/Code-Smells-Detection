public void setChildParentDistance(int distance) {
    if (distance <= 0)
        throw new IllegalArgumentException("Distance has to be positive integer " + distance);
    childParentDistance = distance;
}
