public boolean handleEvent(HmeEvent event) {
    switch(event.getOpCode()) {
        case EVT_INIT_INFO:
            {
                HmeEvent.InitInfo info = (HmeEvent.InitInfo) event;
                mParams = info.getParams();
                mMemento = info.getMemento();
                return handleInitInfo(info);
            }
    }
    return super.handleEvent(event);
}
