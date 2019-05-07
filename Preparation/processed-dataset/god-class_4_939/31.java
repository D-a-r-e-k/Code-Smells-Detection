public boolean removeBan(String key) {
    BanObject bo = (BanObject) banList.get(key);
    if (bo == null)
        return false;
    if (bo.hostban != null) {
        banList.remove(bo.hostban);
    } else {
        if (bo.usr != null)
            banList.remove(bo.usr);
        if (bo.cookie != null)
            banList.remove(bo.cookie);
        if (bo.con != null)
            banList.remove(bo.con.getBanKey());
        if (bo.email != null)
            banList.remove(bo.email);
    }
    return true;
}
