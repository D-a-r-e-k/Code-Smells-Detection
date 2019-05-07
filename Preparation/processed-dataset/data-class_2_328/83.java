// -- DifficultyLevels -- 
public List<OptionGroup> getDifficultyLevels() {
    List<OptionGroup> result = new ArrayList<OptionGroup>();
    for (Option option : allOptionGroups.get("difficultyLevels").getOptions()) {
        if (option instanceof OptionGroup) {
            result.add((OptionGroup) option);
        }
    }
    return result;
}
