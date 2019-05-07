// this simply returns the fCurrentEntity object;  
// this should only be used with caution by callers that  
// carefully manage the entity manager's behaviour, so that   
// this doesn't returning meaningless or misleading data.  
// @return  a reference to the current fCurrentEntity object  
public ScannedEntity getCurrentEntity() {
    return fCurrentEntity;
}
