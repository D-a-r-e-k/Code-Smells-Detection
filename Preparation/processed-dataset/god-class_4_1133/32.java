// getScannerStateName(int):String  
/** Returns the dispatcher name. */
public String getDispatcherName(Dispatcher dispatcher) {
    if (DEBUG_DISPATCHER) {
        if (dispatcher != null) {
            String name = dispatcher.getClass().getName();
            int index = name.lastIndexOf('.');
            if (index != -1) {
                name = name.substring(index + 1);
                index = name.lastIndexOf('$');
                if (index != -1) {
                    name = name.substring(index + 1);
                }
            }
            return name;
        }
    }
    return "null";
}
