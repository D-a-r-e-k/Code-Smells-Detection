public boolean isNullable() {
    return !isIdentity && nullability == SchemaObject.Nullability.NULLABLE;
}
