/**
     * Analyzes 'cls' and/or instruments it for coverage:
     * <ul>
     *  <li> if 'instrument' is true, the class definition is instrumented for
     *       coverage if that is feasible
     *  <li> if 'metadata' is true, the class definition is analysed
     *       to create a {@link ClassDescriptor} for the original class definition 
     * </ul>
     * This method returns null if 'metadata' is 'false' *or* if 'cls' is an
     * interface [the latter precludes coverage of interface static
     * initializers and may be removed in the future].<P>
     * 
     * NOTE: if 'instrument' is 'true', the caller should always assume that 'cls'
     * has been mutated by this method even if it returned null. The caller should
     * then revert to the original class definition that was created as a
     * <code>cls.clone()</code> or by retaining the original definition bytes.
     * This part of contract is for efficienty and also simplifies the implementation. 
     */
public void process(final ClassDef cls, final boolean ignoreAlreadyInstrumented, final boolean instrument, final boolean metadata, final InstrResult out) {
    out.m_instrumented = false;
    out.m_descriptor = null;
    if (!(instrument || metadata))
        return;
    // nothing to do  
    if (cls.isInterface())
        return;
    else {
        reset();
        m_cls = cls;
        // TODO: handle classes that cannot be instrumented due to bytecode/JVM limitations  
        m_instrument = instrument;
        m_metadata = metadata;
        m_ignoreAlreadyInstrumented = ignoreAlreadyInstrumented;
        // TODO: create 'no instrumentation' execution path here  
        visit((ClassDef) null, null);
        // potentially changes m_instrument and m_metadata  
        if (m_metadata) {
            setClassName(cls.getName());
            out.m_descriptor = new ClassDescriptor(m_classPackageName, m_className, m_classSignature, m_classSrcFileName, m_classMethodDescriptors);
        }
        out.m_instrumented = m_instrument;
    }
}
