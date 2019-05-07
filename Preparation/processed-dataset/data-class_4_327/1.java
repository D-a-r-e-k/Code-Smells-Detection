// Utilities 
// TODO: this might be useful elsewhere, too 
private int limitToRange(int value, int lower, int upper) {
    return Math.max(lower, Math.min(value, upper));
}
