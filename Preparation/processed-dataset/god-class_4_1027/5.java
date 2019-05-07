protected void dispatchEvent(HmeEvent event) {
    switch(event.getOpCode()) {
        case EVT_KEY:
            HmeEvent.Key e = (HmeEvent.Key) event;
            if (handleCallback(e))
                return;
    }
    super.dispatchEvent(event);
}
