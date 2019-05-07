private static String grabSystemProp(Property annotation) {
    String[] system_property_names = annotation.systemProperty();
    String retval = null;
    for (String system_property_name : system_property_names) {
        if (system_property_name != null && system_property_name.length() > 0) {
            if (system_property_name.equals(Global.BIND_ADDR) || system_property_name.equals(Global.BIND_ADDR_OLD))
                if (Util.isBindAddressPropertyIgnored())
                    continue;
            try {
                retval = System.getProperty(system_property_name);
                if (retval != null)
                    return retval;
            } catch (SecurityException ex) {
                log.error("failed getting system property for " + system_property_name, ex);
            }
        }
    }
    return retval;
}
