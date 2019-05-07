/**
    * Logging method configured by the loglevel mechanism
    * @param msg the message to log
    * @param type the type of message (MSG_ prefixed konstants are used here)
    * @param lvl the level of attention to use here (LVL_ prefixed konstants are used here)
    */
public static void log(Object o, String msg, short type, short lvl) {
    StringBuffer sb = new StringBuffer();
    try {
        if (LOG_MASK[type].intValue() < lvl && !DEBUG)
            return;
        sb.append("[");
        sb.append(Server.formatDefaultTimeStamp(System.currentTimeMillis()));
        switch(lvl) {
            case LVL_MAJOR:
                sb.append("] MAJOR-| ");
                break;
            case LVL_HALT:
                sb.append("] HALT -| ");
                break;
            default:
                sb.append("]      -| ");
        }
        if (o != null) {
            sb.append(o.toString());
            sb.append(": ");
        }
        sb.append(msg);
        sb.append("\r\n");
        if ((type == MSG_CONFIG && LOGFILE[MSG_CONFIG].equals("console")) || (type == MSG_AUTH && LOGFILE[MSG_AUTH].equals("console")) || (type == MSG_STATE && LOGFILE[MSG_STATE].equals("console")) || (type == MSG_TRAFFIC && LOGFILE[MSG_TRAFFIC].equals("console")) || (type == MSG_ERROR && LOGFILE[MSG_ERROR].equals("console"))) {
            System.out.print(sb.toString());
        } else {
            LogWriter.instance.addLogMessage(type, sb.toString());
        }
        if (lvl == LVL_HALT)
            System.exit(1);
    } catch (Exception e) {
        System.err.println("Server.log caused Exception for Message:");
        System.err.print(sb.toString());
        e.printStackTrace();
    }
}
