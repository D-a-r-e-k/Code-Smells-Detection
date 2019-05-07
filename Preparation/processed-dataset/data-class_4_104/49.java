/**
    * allows to check for logability of a message in advance of
    * performance critical messages
    */
public static boolean checkLogLvl(short type, short lvl) {
    return (LOG_MASK[type].intValue() >= lvl || DEBUG);
}
