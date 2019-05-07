/**
     * Fix up missing option groups.
     *
     * @param optionGroup The missing <code>OptionGroup</code>.
     * @param difficulty If true, add the option group to the difficulty levels.
     */
public void fixOptionGroup(OptionGroup optionGroup, boolean difficulty) {
    if (difficulty) {
        for (Option option : allOptionGroups.get("difficultyLevels").getOptions()) {
            if (option instanceof OptionGroup) {
                OptionGroup level = (OptionGroup) option;
                if (level.hasOptionGroup()) {
                    level.add(optionGroup);
                } else {
                    for (Option o : optionGroup.getOptions()) {
                        level.add(o);
                    }
                }
            }
        }
    } else {
        if (!allOptionGroups.containsKey(optionGroup.getId())) {
            allOptionGroups.put(optionGroup.getId(), optionGroup);
        }
    }
}
