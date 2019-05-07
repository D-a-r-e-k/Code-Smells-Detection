private List<E> applyPermutation() {
    ArrayList<E> output = new ArrayList<E>(sourceArray);
    // Example : this.sourceArray = ["A","B","C","D"] 
    // perOrder:                  = [ 1 , 0 , 3 , 2 ] 
    // result  :                  = ["B","A","D","C"] 
    for (int i = 0; i < output.size(); i++) {
        output.set(i, this.sourceArray.get(this.currPermutationArray[i]));
    }
    return output;
}
