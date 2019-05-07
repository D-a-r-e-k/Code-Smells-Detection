/**
    * Debuging method configured by the loglevel mechanism
    * @param prefix to write before other stuff
    * @param t is the throwable to print the stacktrace from
    * @param type the type of message, konstants with MSG_ prefix are used here
    * @param lvl the level of atention to use here, konstants with LVL_ prefix are used here
    */
public static void debug(Object o, String prefix, Throwable t, short type, short lvl) {
    if (LOG_MASK[type].intValue() < lvl && !DEBUG)
        return;
    StringBuffer sb = new StringBuffer();
    sb.append(prefix);
    sb.append("\r\n");
    sb.append(t.toString());
    StackTraceElement ste[] = t.getStackTrace();
    for (int i = 0; i < ste.length; i++) {
        sb.append("\r\n    at ");
        sb.append(ste[i].getClassName());
        sb.append("(");
        sb.append(ste[i].getFileName());
        sb.append(":");
        sb.append(ste[i].getLineNumber());
        sb.append(")");
    }
    log(o, sb.toString(), type, lvl);
}
