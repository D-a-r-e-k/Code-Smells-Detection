/** @return the OpenDefinitonsDocument that is associated with this DefinitionsDocument. */
public OpenDefinitionsDocument getOpenDefDoc() {
    if (_odd == null)
        throw new IllegalStateException("The OpenDefinitionsDocument for this DefinitionsDocument has never been set");
    else
        return _odd;
}
