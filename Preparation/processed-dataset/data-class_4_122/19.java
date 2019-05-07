void setValueCardinality(final int[] cardinality) {
    if ((cardinality == null) || (cardinality.length != 2))
        throw new IllegalArgumentException("null or invalid input: cardinality");
    m_valueCardinality = cardinality;
}
