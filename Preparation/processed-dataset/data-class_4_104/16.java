public void addToken(String token, String cookie) {
    if (!USE_TOKENSTORE || token == null || cookie == null)
        return;
    tokenStore.put(cookie, token);
}
