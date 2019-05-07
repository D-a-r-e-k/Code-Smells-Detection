/**
     *  This method must return a list of attributes which are
     *  exposed by the SimpleMBean.  If there's a getXXX() method
     *  available, it'll be exposed as a getter, and if there's a
     *  setXXX() method available, it'll be exposed as a setter.
     *  For example:
     *  <pre>
     *     public void setFoo( String foo ) ...
     *     public String getFoo() ...
     *
     *     public String[] getAttributeNames()
     *     {
     *         String[] attrs = { "foo" };
     *
     *         return attrs;
     *     }
     *  </pre>
     *  Also, methods starting with "is" are also recognized as getters
     *  (e.g. <code>public boolean isFoo()</code>.)
     *
     *  @return An array of attribute names that can be get and optionally set.
     */
public abstract String[] getAttributeNames();
