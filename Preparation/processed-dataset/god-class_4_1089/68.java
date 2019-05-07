//}}}  
//{{{ commitTemporary() method  
void commitTemporary() {
    setFlag(TEMPORARY, false);
    finishLoading();
}
