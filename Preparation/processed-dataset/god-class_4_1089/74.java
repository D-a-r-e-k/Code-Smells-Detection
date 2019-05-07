//}}}  
//}}}  
//{{{ Private members  
//{{{ Flags  
//{{{ setFlag() method  
private void setFlag(int flag, boolean value) {
    if (value)
        flags |= 1 << flag;
    else
        flags &= ~(1 << flag);
}
