/**
     * A method to be called when a character data node has been replaced
     */
void replacedCharacterData(NodeImpl node, String oldvalue, String value) {
    //now that we have finished replacing data, we need to perform the same actions  
    //that are required after a character data node has been modified  
    //send the value of false for replace parameter so that mutation  
    //events if appropriate will be initiated  
    modifiedCharacterData(node, oldvalue, value, false);
}
