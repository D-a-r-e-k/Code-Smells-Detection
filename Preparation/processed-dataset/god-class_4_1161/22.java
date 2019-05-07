/**
     * Tries to complete any wishes for a unit that has just arrived.
     *
     * @param unit A <code>Unit</code> that is arriving in this colony.
     * @return True if a wish was successfully completed.
     */
public boolean completeWish(Unit unit) {
    boolean ret = false;
    int i = 0;
    while (i < wishes.size()) {
        if (wishes.get(i) instanceof WorkerWish) {
            WorkerWish ww = (WorkerWish) wishes.get(i);
            if (ww.satisfiedBy(unit) && completeWish(ww, "satisfied (" + unit.getId() + ")")) {
                ret = true;
                continue;
            }
        }
        i++;
    }
    return ret;
}
