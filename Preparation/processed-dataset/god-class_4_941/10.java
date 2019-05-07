private void log(String message) {
    log.info(message);
    if (logger != null) {
        logger.log(message);
    }
}
