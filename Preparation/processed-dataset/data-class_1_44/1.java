public static long sizeOf(Creator c) {
    long size = 0;
    Object[] objects = new Object[100];
    try {
        Object primer = c.create();
        long startingMemoryUse = getUsedMemory();
        for (int i = 0; i < objects.length; i++) {
            objects[i] = c.create();
        }
        long endingMemoryUse = getUsedMemory();
        float approxSize = (endingMemoryUse - startingMemoryUse) / 100f;
        size = Math.round(approxSize);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return size;
}
