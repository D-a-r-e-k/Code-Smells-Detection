/**
     * Updates the wishes for the <code>Colony</code>.
     */
private void updateWishes() {
    updateWorkerWishes();
    updateGoodsWishes();
    Collections.sort(wishes);
}
