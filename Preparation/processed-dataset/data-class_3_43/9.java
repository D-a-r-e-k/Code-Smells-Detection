// isSynthesize():boolean 
// 
// Object methods 
// 
/** Returns a string representation of this object. */
public String toString() {
    StringBuffer str = new StringBuffer();
    str.append(fBeginLineNumber);
    str.append(':');
    str.append(fBeginColumnNumber);
    str.append(':');
    str.append(fBeginCharacterOffset);
    str.append(':');
    str.append(fEndLineNumber);
    str.append(':');
    str.append(fEndColumnNumber);
    str.append(':');
    str.append(fEndCharacterOffset);
    return str.toString();
}
