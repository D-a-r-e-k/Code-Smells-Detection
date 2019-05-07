/**
     * Removes the given <code>AIGoods</code> from this colony's list. The
     * <code>AIGoods</code>-object is not disposed as part of this operation.
     * Use that method instead to remove the object completely (this method
     * would then be called indirectly).
     *
     * @param ag The <code>AIGoods</code> to be removed.
     * @see AIGoods#dispose()
     */
public void removeAIGoods(AIGoods ag) {
    while (aiGoods.remove(ag)) {
    }
}
