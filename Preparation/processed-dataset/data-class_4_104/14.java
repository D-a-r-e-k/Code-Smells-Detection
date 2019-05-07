public void removeToken(String cookie) {
    if (cookie == null)
        return;
    tokenStore.remove(cookie);
}
