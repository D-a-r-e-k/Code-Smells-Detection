/******************************************************************************/
/**
 * Erzeugt eine Permutation der Zahlen von 0 bis length
 * 
 * @param length Count and highest value of the generated sequence.
 * @return sequence of numbers, contains every number a single time. The 
 * sequence consists of numbers between 0 and length.
 */
private int[] createPermutation(int length) {
    int[] permutation = new int[length];
    for (int i = 0; i < permutation.length; i++) {
        int newValue = (int) (Math.random() * length);
        for (int j = 0; j < i; j++) if (newValue == permutation[j]) {
            newValue = (int) (Math.random() * length);
            j = -1;
        }
        permutation[i] = newValue;
    }
    return permutation;
}
