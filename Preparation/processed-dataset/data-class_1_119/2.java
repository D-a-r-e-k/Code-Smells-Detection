/**
     * Swaps by array indexes
     *
     * @param i
     * @param j
     */
private void swap(int i, int j) {
    int temp = this.Value[i];
    this.Value[i] = this.Value[j];
    this.Value[j] = temp;
}
