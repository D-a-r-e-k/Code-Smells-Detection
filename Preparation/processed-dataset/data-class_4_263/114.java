//}}}  
//{{{ parseBufferLocalProperties() method  
private void parseBufferLocalProperties(CharSequence prop) {
    StringBuilder buf = new StringBuilder();
    String name = null;
    boolean escape = false;
    for (int i = 0; i < prop.length(); i++) {
        char c = prop.charAt(i);
        switch(c) {
            case ':':
                if (escape) {
                    escape = false;
                    buf.append(':');
                    break;
                }
                if (name != null) {
                    // use the low-level property setting code  
                    // so that if we have a buffer-local  
                    // property with the same value as a default,  
                    // later changes in the default don't affect  
                    // the buffer-local property  
                    properties.put(name, new PropValue(buf.toString(), false));
                    name = null;
                }
                buf.setLength(0);
                break;
            case '=':
                if (escape) {
                    escape = false;
                    buf.append('=');
                    break;
                }
                name = buf.toString();
                buf.setLength(0);
                break;
            case '\\':
                if (escape)
                    buf.append('\\');
                escape = !escape;
                break;
            case 'n':
                if (escape) {
                    buf.append('\n');
                    escape = false;
                    break;
                }
            case 'r':
                if (escape) {
                    buf.append('\r');
                    escape = false;
                    break;
                }
            case 't':
                if (escape) {
                    buf.append('\t');
                    escape = false;
                    break;
                }
            default:
                buf.append(c);
                break;
        }
    }
}
