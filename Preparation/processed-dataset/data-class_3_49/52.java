/**
     * Return hash code for Object x.
     * Since we are using power-of-two
     * tables, it is worth the effort to improve hashcode via
     * the same multiplicative scheme as used in IdentityHashMap.
     */
private static int hash(Object x) {
    int h = x.hashCode();
    // Multiply by 127 (quickly, via shifts), and mix in some high  
    // bits to help guard against bunching of codes that are  
    // consecutive or equally spaced.  
    return ((h << 7) - h + (h >>> 9) + (h >>> 17));
}
