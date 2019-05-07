private String cleanupSuspectData(String s) {
    StringBuilder sb = new StringBuilder(s.length());
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (Verifier.isXMLCharacter(c))
            sb.append(c);
        else
            sb.append("0x" + Integer.toString(c, 16).toUpperCase());
    }
    return sb.toString();
}
