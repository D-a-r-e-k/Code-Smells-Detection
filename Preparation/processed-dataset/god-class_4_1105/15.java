/** Swap two photons in the workspace array. */
private void swap(int first, int second) {
    Photon temp = workspace[first];
    workspace[first] = workspace[second];
    workspace[second] = temp;
}
