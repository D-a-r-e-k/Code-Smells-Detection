public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Permutation int[]=");
    sb.append(Arrays.toString(getCurrentPermutationArray()));
    List<E> permutationResult = applyPermutation();
    sb.append("\nPermutationSet Source Object[]=");
    sb.append(this.sourceArray.toString());
    sb.append("\nPermutationSet Result Object[]=");
    sb.append(permutationResult.toString());
    return sb.toString();
}
