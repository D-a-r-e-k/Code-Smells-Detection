public String toString() {
    String s = "SimpleStorage:\n" + super.toString();
    s += " - user cache:  Capacity " + user_cache.getCapacity() + ", Usage " + user_cache.getUsage();
    s += ", " + user_cache.getHits() + " hits, " + user_cache.getMisses() + " misses\n";
    return s;
}
