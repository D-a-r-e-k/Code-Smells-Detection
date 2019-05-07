private static void log(String message) {
    log.info(message);
    if (logger == null) {
        log.info(message);
    }
}
