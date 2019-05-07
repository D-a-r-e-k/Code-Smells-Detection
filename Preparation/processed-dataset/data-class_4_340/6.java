/** Resets the field of each option in the Preferences window to its actual stored value. */
public void resetToCurrent() {
    _rootNode.resetToCurrent();
    // must reset the "current keystroke map" when resetting 
    VectorKeyStrokeOptionComponent.resetCurrentKeyStrokeMap();
}
