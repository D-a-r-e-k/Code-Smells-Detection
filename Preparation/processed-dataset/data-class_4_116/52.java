@Override
public int hashCode() {
    int hash = 0;
    if (has("id")) {
        hash = getInt("id");
    }
    return hash;
}
