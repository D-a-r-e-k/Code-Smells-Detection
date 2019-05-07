private static final boolean isBlockLevel(String name) {
    return Arrays.binarySearch(BLOCK_ELEMENTS, name) >= 0;
}
