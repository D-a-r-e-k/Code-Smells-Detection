public void banHost(InetAddress ia, long millis, String msg) {
    BanObject bo = new BanObject(msg, "Server", millis);
    bo.hostban = ia.getHostAddress();
    banList.put(bo.hostban, bo);
}
