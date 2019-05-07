// fixURI(String):String  
//  
// Package visible methods  
//  
/**
     * Returns the hashtable of declared entities.
     * <p>
     * <strong>REVISIT:</strong>
     * This should be done the "right" way by designing a better way to
     * enumerate the declared entities. For now, this method is needed
     * by the constructor that takes an XMLEntityManager parameter.
     */
Hashtable getDeclaredEntities() {
    return fEntities;
}
