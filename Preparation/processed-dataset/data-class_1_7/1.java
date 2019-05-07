public boolean equals(Object value) {
    return (value instanceof Money) && cents == ((Money) value).cents;
}
