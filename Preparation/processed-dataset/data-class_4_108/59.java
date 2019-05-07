private static void logToConsole(Object o, LogLevel error) {
    if (jagGenerator == null) {
        System.out.println(o);
    } else {
        jagGenerator.logger.log(o.toString(), error);
    }
}
