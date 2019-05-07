/**
     *  This method must return a list of operations which
     *  are to be exposed by the SimpleMBean.  Note that using overloaded
     *  method names is not supported - only one will be exposed as a JMX method
     *  at random.
     *
     *  @return An array of method names that should be exposed as
     *          JMX operations.
     */
public abstract String[] getMethodNames();
