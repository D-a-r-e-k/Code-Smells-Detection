/** Sets the OpenDefinitionsDocument that holds this DefinitionsDocument (the odd can only be set once).
    * @param odd the OpenDefinitionsDocument to set as this DD's holder
    */
public void setOpenDefDoc(OpenDefinitionsDocument odd) {
    if (_odd == null)
        _odd = odd;
}
