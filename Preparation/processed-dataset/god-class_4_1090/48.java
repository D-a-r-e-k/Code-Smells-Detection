//}}}  
//{{{ setTokenMarker() method  
public void setTokenMarker(TokenMarker tokenMarker) {
    TokenMarker oldTokenMarker = this.tokenMarker;
    this.tokenMarker = tokenMarker;
    // don't do this on initial token marker  
    if (oldTokenMarker != null && tokenMarker != oldTokenMarker) {
        lineMgr.setFirstInvalidLineContext(0);
    }
}
