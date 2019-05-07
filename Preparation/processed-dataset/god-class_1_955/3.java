private int[] arrayClone(int[] sourceArray) {
    int[] destArray = new int[sourceArray.length];
    System.arraycopy(sourceArray, 0, destArray, 0, sourceArray.length);
    return destArray;
}
