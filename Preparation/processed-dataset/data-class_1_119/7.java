/**
     * Facade. use it with getNext. efficency: O(N)
     *
     * @return a new Array with the permutatation order. for example:
     * perm0=[1,2,3,4] perm1=[1,2,4,3] perm2=[1,3,2,4]
     */
public int[] getNext() {
    if (!hasNext()) {
        throw new RuntimeException("IntegerPermutationIter exceeds the total number of permutaions." + " Suggestion: do a check with hasNext() , or count till getTotalNumberOfPermutations" + " before using getNext()");
    }
    // if it is the first one , return original 
    int[] internalArray;
    if (this.permutationCounter == 0) {
        this.permutationCounter++;
        internalArray = this.Value;
    } else {
        // if hasNext() has precaclulated it , take this value. 
        if (this.wasNextValueCalculatedAlready) {
            internalArray = this.Value;
            this.wasNextValueCalculatedAlready = false;
        } else {
            internalArray = getNextStartingWith2();
            if (this.endWasReached) {
                return null;
            }
        }
    }
    this.currentValueBackup = arrayClone(internalArray);
    return arrayClone(internalArray);
}
