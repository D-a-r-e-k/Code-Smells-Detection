/**
     * Get a string of the form "P1(config_str1):P2:P3(config_str3)" and return
     * ProtocolConfigurations for it. That means, parse "P1(config_str1)", "P2" and
     * "P3(config_str3)"
     * @param config_str Configuration string
     * @return Vector of strings
     */
private static Vector<String> parseProtocols(String config_str) throws IOException {
    Vector<String> retval = new Vector<String>();
    PushbackReader reader = new PushbackReader(new StringReader(config_str));
    int ch;
    StringBuilder sb;
    boolean running = true;
    while (running) {
        String protocol_name = readWord(reader);
        sb = new StringBuilder();
        sb.append(protocol_name);
        ch = read(reader);
        if (ch == -1) {
            retval.add(sb.toString());
            break;
        }
        if (ch == ':') {
            // no attrs defined 
            retval.add(sb.toString());
            continue;
        }
        if (ch == '(') {
            // more attrs defined 
            reader.unread(ch);
            String attrs = readUntil(reader, ')');
            sb.append(attrs);
            retval.add(sb.toString());
        } else {
            retval.add(sb.toString());
        }
        while (true) {
            ch = read(reader);
            if (ch == ':') {
                break;
            }
            if (ch == -1) {
                running = false;
                break;
            }
        }
    }
    reader.close();
    return retval;
}
