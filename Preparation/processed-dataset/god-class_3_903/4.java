private static char[] ExpandCharArr(char[] oldArr, int incr) {
    char[] ret = new char[oldArr.length + incr];
    System.arraycopy(oldArr, 0, ret, 0, oldArr.length);
    return ret;
}
