public boolean isStabiliserHit(int loc) {
    return (stabiliserHits & (1 << loc)) == (1 << loc);
}
