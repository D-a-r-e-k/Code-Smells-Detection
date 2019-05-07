//}}}  
//{{{ getFlag() method  
private boolean getFlag(int flag) {
    int mask = 1 << flag;
    return (flags & mask) == mask;
}
