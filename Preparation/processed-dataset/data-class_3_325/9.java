public static String printConfigurations(Collection<ProtocolConfiguration> configs) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (ProtocolConfiguration config : configs) {
        if (first)
            first = false;
        else
            sb.append(":");
        sb.append(config.getProtocolName());
        if (!config.getProperties().isEmpty()) {
            sb.append('(').append(config.propertiesToString()).append(')');
        }
    }
    return sb.toString();
}
