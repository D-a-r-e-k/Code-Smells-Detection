/**
   * Get the next node identity value in the list, and call the iterator
   * if it hasn't been added yet.
   *
   * @param identity The node identity (index).
   * @return identity+1, or DTM.NULL.
   */
protected int getNextNodeIdentity(int identity) {
    identity += 1;
    if (identity >= m_nodes.size()) {
        if (!nextNode())
            identity = DTM.NULL;
    }
    return identity;
}
