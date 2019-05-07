private void reset() {
    // TODO: check that all state is reset  
    m_instrument = false;
    m_metadata = false;
    m_ignoreAlreadyInstrumented = false;
    m_cls = null;
    m_classPackageName = null;
    m_className = null;
    m_classSrcFileName = null;
    m_classBlockMetadata = null;
    m_classMethodDescriptors = null;
    m_syntheticStringIndex = -1;
    m_coverageFieldrefIndex = -1;
    m_registerMethodrefIndex = -1;
    m_preclinitMethodrefIndex = -1;
    m_classNameConstantIndex = -1;
    m_clinitID = -1;
    m_clinitStatus = 0;
    m_classInstrMethodCount = -1;
    m_classBlockCounts = null;
    m_classSignature = 0;
    m_methodID = -1;
    m_methodName = null;
    m_methodFirstLine = 0;
    m_methodBlockOffsets = null;
    m_methodJumpAdjOffsets = null;
    m_methodJumpAdjValues = null;
}
