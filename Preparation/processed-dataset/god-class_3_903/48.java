private static boolean Intersect(String set1, String set2) {
    if (set1 == null || set2 == null)
        return false;
    int[] nameSet1 = (int[]) allNextStates.get(set1);
    int[] nameSet2 = (int[]) allNextStates.get(set2);
    if (nameSet1 == null || nameSet2 == null)
        return false;
    if (nameSet1 == nameSet2)
        return true;
    for (int i = nameSet1.length; i-- > 0; ) for (int j = nameSet2.length; j-- > 0; ) if (nameSet1[i] == nameSet2[j])
        return true;
    return false;
}
