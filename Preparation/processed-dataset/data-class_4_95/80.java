/** Prints usage */
private static String printUsage() {
    StringBuffer sb = new StringBuffer();
    sb.append("QuickServer - Java library/framework for creating robust multi-client TCP servers.\n");
    sb.append("Copyright (C) QuickServer.org\n\n");
    sb.append("Usage: QuickServer [-options]\n");
    sb.append("Where options include:\n");
    sb.append("  -about\t" + "Opens About Dialog box\n");
    sb.append("  -load <xml_config_file> [load-options]\t" + "Loads the server from xml file.\n");
    sb.append("  Where load-options include:\n");
    sb.append("     -fullXML2File <file_name>\t" + "Dumps the Full XML configuration of the QuickServer loaded.\n");
    return sb.toString();
}
