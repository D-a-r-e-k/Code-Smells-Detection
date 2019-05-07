//  
// Protected methods  
//  
// anybody calling this had better have set Symtoltable!  
protected void reset() {
    init();
    // DTD preparsing defaults:  
    fValidation = true;
    fNotifyCharRefs = false;
}
