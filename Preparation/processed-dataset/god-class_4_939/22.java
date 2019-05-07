private boolean mayTempAdminhost(String ia) {
    if (ia.startsWith("192.168.") || ia.startsWith("10.") || ia.startsWith("172.16") || ia.equals("127.0.0.1"))
        return false;
    return true;
}
