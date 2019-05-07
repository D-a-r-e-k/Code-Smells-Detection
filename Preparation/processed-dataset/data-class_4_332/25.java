/**
     * Gets the worker wishes this colony has.
     *
     * @return A copy of the wishes list with non-worker wishes removed.
     */
public List<WorkerWish> getWorkerWishes() {
    List<WorkerWish> result = new ArrayList<WorkerWish>();
    for (Wish wish : wishes) {
        if (wish instanceof WorkerWish) {
            result.add((WorkerWish) wish);
        }
    }
    return result;
}
