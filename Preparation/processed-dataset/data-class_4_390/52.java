public int getNumberActiverTroopers() {
    int count = 0;
    // Initialize the troopers.  
    for (int loop = 1; loop < locations(); loop++) {
        if (isTrooperActive(loop)) {
            count++;
        }
    }
    return count;
}
