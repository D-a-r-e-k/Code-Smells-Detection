public boolean isTokenValid(String token, String cookie) {
    if (!USE_TOKENSTORE)
        return true;
    if (token == null || cookie == null)
        return false;
    String t = (String) tokenStore.get(cookie);
    if (t == null || !t.equals(token))
        return false;
    return true;
}
