/**
     * Get a <code>FreeColGameObject</code> with the specified id and
     * class, owned by this player.
     *
     * @param id The id.
     * @param returnClass The expected class of the object.
     * @return The game object, or null if not found.
     * @throws IllegalStateException on failure to validate the object
     *     in any way.
     */
public <T extends FreeColGameObject> T getFreeColGameObject(String id, Class<T> returnClass) throws IllegalStateException {
    T t = getGame().getFreeColGameObject(id, returnClass);
    if (t == null) {
        throw new IllegalStateException("Not a " + returnClass.getName() + ": " + id);
    } else if (t instanceof Ownable) {
        if (this != ((Ownable) t).getOwner()) {
            throw new IllegalStateException(returnClass.getName() + " not owned by " + getId() + ": " + id);
        }
    } else {
        throw new IllegalStateException("Not ownable: " + id);
    }
    return t;
}
