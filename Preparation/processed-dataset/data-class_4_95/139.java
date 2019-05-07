private boolean checkAccessConstraint(Socket socket) {
    try {
        if (getAccessConstraintConfig() != null) {
            getAccessConstraintConfig().checkAccept(socket);
        }
        return true;
    } catch (SecurityException se) {
        logger.warning("SecurityException occurred accepting connection : " + se.getMessage());
        return false;
    }
}
