/**
     *  Calls a transmutator chain.
     *
     *  @param list Chain to call
     *  @param text Text that should be passed to the mutate() method
     *              of each of the mutators in the chain.
     *  @return The result of the mutation.
     */
protected String callMutatorChain(Collection list, String text) {
    if (list == null || list.size() == 0) {
        return text;
    }
    for (Iterator i = list.iterator(); i.hasNext(); ) {
        StringTransmutator m = (StringTransmutator) i.next();
        text = m.mutate(m_context, text);
    }
    return text;
}
