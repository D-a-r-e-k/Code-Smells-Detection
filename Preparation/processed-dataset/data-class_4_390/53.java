public int getRandomTrooper() {
    Vector<Integer> activeTroops = new Vector<Integer>();
    for (int loop = 1; loop < locations(); loop++) {
        if (isTrooperActive(loop)) {
            activeTroops.add(loop);
        }
    }
    int locInt = Compute.randomInt(activeTroops.size());
    return activeTroops.elementAt(locInt);
}
