// Cloneable:  
/**
     * Chains to super.clone() and removes CloneNotSupportedException
     * from the method signature.
     */
public Object clone() {
    try {
        return super.clone();
    } catch (CloneNotSupportedException e) {
        throw new InternalError(e.toString());
    }
}
