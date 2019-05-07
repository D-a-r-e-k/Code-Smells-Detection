/**
     * Simple extension conversion of .wm to .vm
     */
private String convertName(String name) {
    return (name.indexOf(WM_EXT) < 0) ? name : name.substring(0, name.indexOf(WM_EXT)) + VM_EXT;
}
