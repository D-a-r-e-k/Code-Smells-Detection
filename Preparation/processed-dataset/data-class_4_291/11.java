public byte getNullability() {
    return isIdentity ? SchemaObject.Nullability.NO_NULLS : nullability;
}
