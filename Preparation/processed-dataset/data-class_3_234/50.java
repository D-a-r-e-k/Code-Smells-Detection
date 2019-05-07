public long deepestUri() {
    return longestActiveQueue == null ? -1 : longestActiveQueue.getCount();
}
