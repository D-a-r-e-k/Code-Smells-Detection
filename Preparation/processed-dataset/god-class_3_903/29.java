private static boolean EqualLoByteVectors(List vec1, List vec2) {
    if (vec1 == null || vec2 == null)
        return false;
    if (vec1 == vec2)
        return true;
    if (vec1.size() != vec2.size())
        return false;
    for (int i = 0; i < vec1.size(); i++) {
        if (((Integer) vec1.get(i)).intValue() != ((Integer) vec2.get(i)).intValue())
            return false;
    }
    return true;
}
