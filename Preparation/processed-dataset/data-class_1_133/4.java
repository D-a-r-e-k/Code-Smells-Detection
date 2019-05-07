public void setLayoutOrientation(int orientation) {
    if (orientation < 0 && orientation > 1) {
        orientation = DEFAULT_ORIENTATION;
    } else {
        this.orientation = orientation;
    }
}
