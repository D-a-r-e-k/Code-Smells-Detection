private static int[] GetStateSetIndicesForUse(String arrayString) {
    int[] ret;
    int[] set = (int[]) allNextStates.get(arrayString);
    if ((ret = (int[]) tableToDump.get(arrayString)) == null) {
        ret = new int[2];
        ret[0] = lastIndex;
        ret[1] = lastIndex + set.length - 1;
        lastIndex += set.length;
        tableToDump.put(arrayString, ret);
        orderedStateSet.add(set);
    }
    return ret;
}
