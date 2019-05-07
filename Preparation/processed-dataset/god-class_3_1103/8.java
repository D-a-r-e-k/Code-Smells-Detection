/** Determine whether this tube is a closed surface. */
public boolean isClosed() {
    return (endsStyle != OPEN_ENDS || (thickness[0] == 0.0 && thickness[thickness.length - 1] == 0.0));
}
