/**
     * Each message must have a unique ID, in case of using async replication,
     * and a smart queue, this id is used to replace messages not yet sent.
     *
     * @return String
     */
public String getUniqueId() {
    if (this.uniqueId != null)
        return this.uniqueId;
    StringBuilder result = new StringBuilder(getSsoId());
    result.append("#-#");
    result.append(System.currentTimeMillis());
    return result.toString();
}
