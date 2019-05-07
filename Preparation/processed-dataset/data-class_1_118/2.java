/**
     * On first call, returns the source as an array; on any other call
     * thereafter, a new permutation
     *
     * @return null if we overflowed! the array otherwise
     */
public List<E> getNextArray() {
    List<E> permutationResult;
    // will hold the array result 
    if (this.permOrder.hasNextPermutaions()) {
        this.currPermutationArray = this.permOrder.nextPermutation();
        permutationResult = applyPermutation();
    } else {
        permutationResult = null;
    }
    return permutationResult;
}
