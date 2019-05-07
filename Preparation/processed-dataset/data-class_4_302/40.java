private static String getPathWithoutEscapes(String origPath) {
    if (origPath != null && origPath.length() != 0 && origPath.indexOf('%') != -1) {
        // Locate the escape characters  
        StringTokenizer tokenizer = new StringTokenizer(origPath, "%");
        StringBuffer result = new StringBuffer(origPath.length());
        int size = tokenizer.countTokens();
        result.append(tokenizer.nextToken());
        for (int i = 1; i < size; ++i) {
            String token = tokenizer.nextToken();
            // Decode the 2 digit hexadecimal number following % in '%nn'  
            result.append((char) Integer.valueOf(token.substring(0, 2), 16).intValue());
            result.append(token.substring(2));
        }
        return result.toString();
    }
    return origPath;
}
