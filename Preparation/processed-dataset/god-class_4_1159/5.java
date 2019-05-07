/**
     * Refilters the current model messages, removing the ones that
     * are no longer valid.
     *
     * @param options The <code>OptionGroup</code> for message display
     *     to enforce.
     */
public void refilterModelMessages(OptionGroup options) {
    for (ModelMessage m : new ArrayList<ModelMessage>(modelMessages)) {
        try {
            String id = m.getMessageType().getOptionName();
            if (!options.getBoolean(id))
                modelMessages.remove(m);
        } catch (Exception e) {
        }
        ;
    }
}
