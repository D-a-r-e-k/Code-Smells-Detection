/******************************************************************************/
/**
 * Creates a permutation of the Numbers from 0 to a determined value.
 * @param length Number of Numbers and maximal distance to 0 for the Numbers
 * filling the permutation
 * @return Permutation of the Numbers between 0 and <code>length</code>
 */
public int[] createPermutation(int length) {
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
