//  
// DocumentEvent methods  
//  
/**
     * Introduced in DOM Level 2. Optional. <p>
     * Create and return Event objects.
     *
     * @param type The eventType parameter specifies the type of Event
     * interface to be created.  If the Event interface specified is supported
     * by the implementation this method will return a new Event of the
     * interface type requested. If the Event is to be dispatched via the
     * dispatchEvent method the appropriate event init method must be called
     * after creation in order to initialize the Event's values.  As an
     * example, a user wishing to synthesize some kind of Event would call
     * createEvent with the parameter "Events". The initEvent method could then
     * be called on the newly created Event to set the specific type of Event
     * to be dispatched and set its context information.
     * @return Newly created Event
     * @exception DOMException NOT_SUPPORTED_ERR: Raised if the implementation
     * does not support the type of Event interface requested
     * @since WD-DOM-Level-2-19990923
     */
public Event createEvent(String type) throws DOMException {
    if (type.equalsIgnoreCase("Events") || "Event".equals(type)) {
        return new EventImpl();
    } else if (type.equalsIgnoreCase("MutationEvents") || "MutationEvent".equals(type)) {
        return new MutationEventImpl();
    } else if (type.equalsIgnoreCase("UIEvents") || "UIEvent".equals(type)) {
        return new UIEventImpl();
    } else if (type.equalsIgnoreCase("MouseEvents") || "MouseEvent".equals(type)) {
        return new MouseEventImpl();
    } else {
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, msg);
    }
}
