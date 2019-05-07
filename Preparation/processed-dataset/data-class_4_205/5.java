/**
     * Returns the name of the AbstractTarget.
     * @return the name of the AbstractTarget.
     * @hibernate.property column="AT_NAME" length="255" unique="true"
     */
public String getName() {
    return name;
}
