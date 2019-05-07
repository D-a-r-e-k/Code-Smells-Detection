private static long getUsedMemory() {
    gc();
    long totalMemory = Runtime.getRuntime().totalMemory();
    gc();
    long freeMemory = Runtime.getRuntime().freeMemory();
    long usedMemory = totalMemory - freeMemory;
    return usedMemory;
}
