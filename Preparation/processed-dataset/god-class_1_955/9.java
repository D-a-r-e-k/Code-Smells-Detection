/**
     * Utility method to convert the array into a string examples: [] [0]
     * [0,1][1,0]
     *
     * @param array
     */
public String toString(int[] array) {
    if (array.length <= 0) {
        return "[]";
    }
    StringBuffer stBuffer = new StringBuffer("[");
    for (int i = 0; i < (array.length - 1); i++) {
        stBuffer.append(array[i]).append(",");
    }
    stBuffer.append(array[array.length - 1]).append("]");
    return stBuffer.toString();
}
