public boolean canAdd(IntervalType other) {
    return other.startPartIndex >= startPartIndex && other.endPartIndex <= endPartIndex;
}
