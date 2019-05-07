/**
     * Introduced in DOM Level 2. <p>
     * Distribution engine for DOM Level 2 Events. 
     * <p>
     * Event propagation runs as follows:
     * <ol>
     * <li>Event is dispatched to a particular target node, which invokes
     *   this code. Note that the event's stopPropagation flag is
     *   cleared when dispatch begins; thereafter, if it has 
     *   been set before processing of a node commences, we instead
     *   immediately advance to the DEFAULT phase.
     * <li>The node's ancestors are established as destinations for events.
     *   For capture and bubble purposes, node ancestry is determined at 
     *   the time dispatch starts. If an event handler alters the document 
     *   tree, that does not change which nodes will be informed of the event. 
     * <li>CAPTURING_PHASE: Ancestors are scanned, root to target, for 
     *   Capturing listeners. If found, they are invoked (see below). 
     * <li>AT_TARGET: 
     *   Event is dispatched to NON-CAPTURING listeners on the
     *   target node. Note that capturing listeners on this node are _not_
     *   invoked.
     * <li>BUBBLING_PHASE: Ancestors are scanned, target to root, for
     *   non-capturing listeners. 
     * <li>Default processing: Some DOMs have default behaviors bound to
     *   specific nodes. If this DOM does, and if the event's preventDefault
     *   flag has not been set, we now return to the target node and process
     *   its default handler for this event, if any.
     * </ol>
     * <p>
     * Note that registration of handlers during processing of an event does
     * not take effect during this phase of this event; they will not be called
     * until the next time this node is visited by dispatchEvent. On the other
     * hand, removals take effect immediately.
     * <p>
     * If an event handler itself causes events to be dispatched, they are
     * processed synchronously, before processing resumes
     * on the event which triggered them. Please be aware that this may 
     * result in events arriving at listeners "out of order" relative
     * to the actual sequence of requests.
     * <p>
     * Note that our implementation resets the event's stop/prevent flags
     * when dispatch begins.
     * I believe the DOM's intent is that event objects be redispatchable,
     * though it isn't stated in those terms.
     * @param node node to dispatch to
     * @param event the event object to be dispatched to 
     *              registered EventListeners
     * @return true if the event's <code>preventDefault()</code>
     *              method was invoked by an EventListener; otherwise false.
    */
protected boolean dispatchEvent(NodeImpl node, Event event) {
    if (event == null)
        return false;
    // Can't use anyone else's implementation, since there's no public  
    // API for setting the event's processing-state fields.  
    EventImpl evt = (EventImpl) event;
    // VALIDATE -- must have been initialized at least once, must have  
    // a non-null non-blank name.  
    if (!evt.initialized || evt.type == null || evt.type.length() == 0) {
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "UNSPECIFIED_EVENT_TYPE_ERR", null);
        throw new EventException(EventException.UNSPECIFIED_EVENT_TYPE_ERR, msg);
    }
    // If nobody is listening for this event, discard immediately  
    LCount lc = LCount.lookup(evt.getType());
    if (lc.total == 0)
        return evt.preventDefault;
    // INITIALIZE THE EVENT'S DISPATCH STATUS  
    // (Note that Event objects are reusable in our implementation;  
    // that doesn't seem to be explicitly guaranteed in the DOM, but  
    // I believe it is the intent.)  
    evt.target = node;
    evt.stopPropagation = false;
    evt.preventDefault = false;
    // Capture pre-event parentage chain, not including target;  
    // use pre-event-dispatch ancestors even if event handlers mutate  
    // document and change the target's context.  
    // Note that this is parents ONLY; events do not  
    // cross the Attr/Element "blood/brain barrier".   
    // DOMAttrModified. which looks like an exception,  
    // is issued to the Element rather than the Attr  
    // and causes a _second_ DOMSubtreeModified in the Element's  
    // tree.  
    ArrayList pv = new ArrayList(10);
    Node p = node;
    Node n = p.getParentNode();
    while (n != null) {
        pv.add(n);
        p = n;
        n = n.getParentNode();
    }
    // CAPTURING_PHASE:  
    if (lc.captures > 0) {
        evt.eventPhase = Event.CAPTURING_PHASE;
        // Ancestors are scanned, root to target, for   
        // Capturing listeners.  
        for (int j = pv.size() - 1; j >= 0; --j) {
            if (evt.stopPropagation)
                break;
            // Someone set the flag. Phase ends.  
            // Handle all capturing listeners on this node  
            NodeImpl nn = (NodeImpl) pv.get(j);
            evt.currentTarget = nn;
            Vector nodeListeners = getEventListeners(nn);
            if (nodeListeners != null) {
                Vector nl = (Vector) nodeListeners.clone();
                // call listeners in the order in which they got registered  
                int nlsize = nl.size();
                for (int i = 0; i < nlsize; i++) {
                    LEntry le = (LEntry) nl.elementAt(i);
                    if (le.useCapture && le.type.equals(evt.type) && nodeListeners.contains(le)) {
                        try {
                            le.listener.handleEvent(evt);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }
    // Both AT_TARGET and BUBBLE use non-capturing listeners.  
    if (lc.bubbles > 0) {
        // AT_TARGET PHASE: Event is dispatched to NON-CAPTURING listeners  
        // on the target node. Note that capturing listeners on the target  
        // node are _not_ invoked, even during the capture phase.  
        evt.eventPhase = Event.AT_TARGET;
        evt.currentTarget = node;
        Vector nodeListeners = getEventListeners(node);
        if (!evt.stopPropagation && nodeListeners != null) {
            Vector nl = (Vector) nodeListeners.clone();
            // call listeners in the order in which they got registered  
            int nlsize = nl.size();
            for (int i = 0; i < nlsize; i++) {
                LEntry le = (LEntry) nl.elementAt(i);
                if (!le.useCapture && le.type.equals(evt.type) && nodeListeners.contains(le)) {
                    try {
                        le.listener.handleEvent(evt);
                    } catch (Exception e) {
                    }
                }
            }
        }
        // BUBBLING_PHASE: Ancestors are scanned, target to root, for  
        // non-capturing listeners. If the event's preventBubbling flag  
        // has been set before processing of a node commences, we  
        // instead immediately advance to the default phase.  
        // Note that not all events bubble.  
        if (evt.bubbles) {
            evt.eventPhase = Event.BUBBLING_PHASE;
            int pvsize = pv.size();
            for (int j = 0; j < pvsize; j++) {
                if (evt.stopPropagation)
                    break;
                // Someone set the flag. Phase ends.  
                // Handle all bubbling listeners on this node  
                NodeImpl nn = (NodeImpl) pv.get(j);
                evt.currentTarget = nn;
                nodeListeners = getEventListeners(nn);
                if (nodeListeners != null) {
                    Vector nl = (Vector) nodeListeners.clone();
                    // call listeners in the order in which they got  
                    // registered  
                    int nlsize = nl.size();
                    for (int i = 0; i < nlsize; i++) {
                        LEntry le = (LEntry) nl.elementAt(i);
                        if (!le.useCapture && le.type.equals(evt.type) && nodeListeners.contains(le)) {
                            try {
                                le.listener.handleEvent(evt);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        }
    }
    // DEFAULT PHASE: Some DOMs have default behaviors bound to specific  
    // nodes. If this DOM does, and if the event's preventDefault flag has  
    // not been set, we now return to the target node and process its  
    // default handler for this event, if any.  
    // No specific phase value defined, since this is DOM-internal  
    if (lc.defaults > 0 && (!evt.cancelable || !evt.preventDefault)) {
    }
    return evt.preventDefault;
}
