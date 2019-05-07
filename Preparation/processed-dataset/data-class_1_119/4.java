private int[] getNextStartingWith2() {
    permutationCounter++;
    int i = N - 1;
    if (i <= 0) // may happen only on N<=1 
    {
        this.endWasReached = true;
        return null;
    }
    /** while (Value[i-1] >= Value[i])
          {
          i = i-1;
          }*/
    while (Value[i - 1] >= Value[i]) {
        i = i - 1;
        if (i == 0) {
            this.endWasReached = true;
            return null;
        }
    }
    int j = N;
    while (Value[j - 1] <= Value[i - 1]) {
        j = j - 1;
    }
    swap(i - 1, j - 1);
    // swap values at positions (i-1) and (j-1) 
    i++;
    j = N;
    while (i < j) {
        swap(i - 1, j - 1);
        i++;
        j--;
    }
    return this.Value;
}
