public StringBuffer dump(int indent, Set alreadyDumped) {
    StringBuffer value = super.dump(indent, alreadyDumped).append(' ').append(name);
    return value;
}
