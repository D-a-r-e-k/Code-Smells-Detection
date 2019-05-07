public static void logMessage(MessageState messageState, Object o, String msg, short type, short lvl) {
    StringBuffer sb = new StringBuffer();
    try {
        if (LOG_MASK[type].intValue() < lvl && !DEBUG)
            return;
        sb.append("[");
        sb.append(Server.formatDefaultTimeStamp(System.currentTimeMillis()));
        sb.append("]");
        if (o != null) {
            sb.append(o.toString());
            sb.append(": ");
        }
        sb.append(msg);
        sb.append("\r\n");
        if ((type == MSG_MESSAGE && LOGFILE[MSG_MESSAGE].equals("console")) || (type == MSG_SEPAMESSAGE) && LOGFILE[MSG_MESSAGE].equals("console")) {
            System.out.print(sb.toString());
        } else {
            LogWriter.instance.addMessageLogMessage(messageState, type, sb.toString());
        }
        if (lvl == LVL_HALT)
            System.exit(1);
    } catch (Exception e) {
        System.err.println("Server.log caused Exception for Message:");
        System.err.print(sb.toString());
        e.printStackTrace();
    }
}
