/**
     * Add a modifier.
     *
     * @param modifier a <code>Modifier</code> value
     */
public void addModifier(Modifier modifier) {
    String id = modifier.getId();
    if (!allModifiers.containsKey(id)) {
        allModifiers.put(id, new ArrayList<Modifier>());
    }
    allModifiers.get(id).add(modifier);
}
