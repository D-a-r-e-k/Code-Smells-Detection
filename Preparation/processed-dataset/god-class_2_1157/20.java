/**
     * Adds an <code>OptionGroup</code> to the specification
     *
     * @param optionGroup <code>OptionGroup</code> to add
     * @param recursive a <code>boolean</code> value
     */
public void addOptionGroup(OptionGroup optionGroup, boolean recursive) {
    // Add the options of the group 
    Iterator<Option> iter = optionGroup.iterator();
    while (iter.hasNext()) {
        Option option = iter.next();
        if (option instanceof OptionGroup) {
            allOptionGroups.put(option.getId(), (OptionGroup) option);
            if (recursive) {
                addOptionGroup((OptionGroup) option, true);
            }
        } else {
            addAbstractOption((AbstractOption) option);
        }
    }
}
