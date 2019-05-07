public Object visit(final IMethodCollection methods, final Object ctx) {
    final ClassDef cls = m_cls;
    final boolean trace2 = m_log.atTRACE2();
    final int originalMethodCount = methods.size();
    final boolean constructMetadata = m_metadata;
    // create block count map: TODO: is the extra slot really needed?  
    // - create [potentially unused] slot for added <clinit>  
    m_classBlockCounts = new int[originalMethodCount + 1];
    if (constructMetadata) {
        // prepare to collect metadata:  
        m_classBlockMetadata = new int[originalMethodCount + 1][][];
        // same comments as above  
        m_classMethodDescriptors = new MethodDescriptor[originalMethodCount];
    }
    // visit each original method:  
    for (int m = 0; m < originalMethodCount; ++m) {
        final Method_info method = methods.get(m);
        m_methodName = method.getName(cls);
        if (trace2)
            m_log.trace2("visit", (method.isSynthetic() ? "synthetic " : "") + "method #" + m + ": [" + m_methodName + "]");
        final boolean isClinit = IClassDefConstants.CLINIT_NAME.equals(m_methodName);
        // TODO: research whether synthetic methods add nontrivially to line coverage or not  
        boolean excluded = false;
        if (!isClinit) {
            if (m_excludeSyntheticMethods && method.isSynthetic()) {
                excluded = true;
                if (trace2)
                    m_log.trace2("visit", "skipped synthetic method");
            } else if (m_excludeBridgeMethods && method.isBridge()) {
                excluded = true;
                if (trace2)
                    m_log.trace2("visit", "skipped bridge method");
            }
        }
        if (excluded) {
            if (constructMetadata) {
                m_classMethodDescriptors[m] = new MethodDescriptor(m_methodName, method.getDescriptor(cls), IMetadataConstants.METHOD_EXCLUDED, m_methodBlockSizes, null, 0);
            }
        } else {
            if ((method.getAccessFlags() & (IAccessFlags.ACC_ABSTRACT | IAccessFlags.ACC_NATIVE)) != 0) {
                if (constructMetadata) {
                    m_classMethodDescriptors[m] = new MethodDescriptor(m_methodName, method.getDescriptor(cls), IMetadataConstants.METHOD_ABSTRACT_OR_NATIVE, m_methodBlockSizes, null, 0);
                }
                if (trace2)
                    m_log.trace2("visit", "skipped " + (method.isAbstract() ? "abstract" : "native") + " method");
            } else // this is a regular, non-<clinit> method that has bytecode:  
            {
                // reset first line:  
                m_methodFirstLine = 0;
                // set current method ID:  
                m_methodID = m;
                if (isClinit) {
                    // if <clinit> found: note the ID but delay processing until the very end  
                    m_clinitID = m;
                    if (trace2)
                        m_log.trace2("visit", "<clinit> method delayed");
                } else {
                    // visit attributes [skip visit (IAttributeCollection) method]:      
                    final IAttributeCollection attributes = method.getAttributes();
                    final int attributeCount = attributes.size();
                    for (int a = 0; a < attributeCount; ++a) {
                        final Attribute_info attribute = attributes.get(a);
                        attribute.accept(this, ctx);
                    }
                    if (constructMetadata) {
                        if ($assert.ENABLED)
                            $assert.ASSERT(m_classBlockCounts[m_methodID] > 0, "invalid block count for method " + m_methodID + ": " + m_classBlockCounts[m_methodID]);
                        if ($assert.ENABLED)
                            $assert.ASSERT(m_methodBlockSizes != null && m_methodBlockSizes.length == m_classBlockCounts[m_methodID], "invalid block sizes map for method " + m_methodID);
                        final int[][] methodBlockMetadata = m_classBlockMetadata[m_methodID];
                        final int status = (methodBlockMetadata == null ? IMetadataConstants.METHOD_NO_LINE_NUMBER_TABLE : 0);
                        m_classMethodDescriptors[m] = new MethodDescriptor(m_methodName, method.getDescriptor(cls), status, m_methodBlockSizes, methodBlockMetadata, m_methodFirstLine);
                    }
                }
            }
        }
    }
    // add <clinit> (and instrument if needed) [a <clinit> is always needed  
    // even if there are no other instrumented method to act as a load hook]:  
    final boolean instrumentClinit = false;
    // TODO: make use of this [to limit instrumentation to clinitHeader only], take into account whether we added and whether it is synthetic  
    final Method_info clinit;
    if (m_clinitID >= 0) {
        // <clinit> existed in the original class: needs to be covered  
        // m_clinitStatus = 0;  
        clinit = methods.get(m_clinitID);
        m_classInstrMethodCount = originalMethodCount;
    } else {
        // there is no <clinit> defined by the original class: add one [and mark it synthetic]  
        m_clinitStatus = IMetadataConstants.METHOD_ADDED;
        // mark as added by us  
        final int attribute_name_index = cls.addCONSTANT_Utf8(Attribute_info.ATTRIBUTE_CODE, true);
        final int name_index = cls.addCONSTANT_Utf8(IClassDefConstants.CLINIT_NAME, true);
        final int descriptor_index = cls.addCONSTANT_Utf8("()V", true);
        final IAttributeCollection attributes;
        if (MARK_ADDED_ELEMENTS_SYNTHETIC)
            attributes = ElementFactory.newAttributeCollection(2);
        else
            attributes = ElementFactory.newAttributeCollection(1);
        final CodeAttribute_info code = new CodeAttribute_info(attribute_name_index, 0, 0, new byte[] { (byte) _return }, AttributeElementFactory.newExceptionHandlerTable(0), ElementFactory.newAttributeCollection(0));
        attributes.add(code);
        if (MARK_ADDED_ELEMENTS_SYNTHETIC) {
            attributes.add(new SyntheticAttribute_info(m_syntheticStringIndex));
        }
        clinit = new Method_info(IAccessFlags.ACC_STATIC | IAccessFlags.ACC_PRIVATE, name_index, descriptor_index, attributes);
        m_clinitID = cls.addMethod(clinit);
        if (trace2)
            m_log.trace2("visit", "added synthetic <clinit> method");
        // TODO: this should exclude <clinit> if it were added by us  
        m_classInstrMethodCount = originalMethodCount + 1;
    }
    if ($assert.ENABLED)
        $assert.ASSERT(m_classInstrMethodCount >= 0, "m_classInstrMethodCount not set");
    // visit <clinit>:  
    {
        m_methodFirstLine = 0;
        m_methodID = m_clinitID;
        if (trace2)
            m_log.trace2("visit", (clinit.isSynthetic() ? "synthetic " : "") + "method #" + m_methodID + ": [<clinit>]");
        final IAttributeCollection attributes = clinit.getAttributes();
        final int attributeCount = attributes.size();
        for (int a = 0; a < attributeCount; ++a) {
            final Attribute_info attribute = attributes.get(a);
            attribute.accept(this, ctx);
        }
    }
    // add pre-<clinit> method:  
    {
        final int attribute_name_index = cls.addCONSTANT_Utf8(Attribute_info.ATTRIBUTE_CODE, true);
        final int name_index = cls.addCONSTANT_Utf8(PRECLINIT_METHOD_NAME, false);
        final int descriptor_index = cls.addCONSTANT_Utf8("()[[Z", false);
        final IAttributeCollection attributes;
        if (MARK_ADDED_ELEMENTS_SYNTHETIC)
            attributes = ElementFactory.newAttributeCollection(2);
        else
            attributes = ElementFactory.newAttributeCollection(1);
        final ByteArrayOStream buf = new ByteArrayOStream(PRECLINIT_INIT_CAPACITY);
        {
            final int[] blockCounts = m_classBlockCounts;
            final int instrMethodCount = m_classInstrMethodCount;
            // actual number of methods to instrument may be less than the size of the block map   
            if ($assert.ENABLED)
                $assert.ASSERT(blockCounts != null && blockCounts.length >= instrMethodCount, "invalid block count map");
            // new and set COVERAGE_FIELD:  
            // push first dimension:  
            CodeGen.push_int_value(buf, cls, instrMethodCount);
            // [stack +1]  
            // new boolean [][]:  
            final int type_index = cls.addClassref("[[Z");
            buf.write4(_multianewarray, type_index >>> 8, // indexbyte1  
            type_index, // indexbyte2  
            1);
            // only one dimension created here  
            // [stack +1]  
            // clone array ref:  
            buf.write4(_dup, // [stack +2]  
            // store in the static field  
            _putstatic, m_coverageFieldrefIndex >>> 8, // indexbyte1  
            m_coverageFieldrefIndex);
            // indexbyte2  
            // [stack +1]  
            for (int m = 0; m < instrMethodCount; ++m) {
                final int blockCount = blockCounts[m];
                if (blockCount > 0) {
                    // clone array ref:  
                    buf.write(_dup);
                    // [stack +2]  
                    // push outer dim index:  
                    CodeGen.push_int_value(buf, cls, m);
                    // [stack +3]  
                    // push dim:  
                    CodeGen.push_int_value(buf, cls, blockCount);
                    // [stack +4]  
                    // newarray boolean []:  
                    buf.write3(_newarray, 4, // "T_BOOLEAN"  
                    // add subarray to the outer array:  
                    _aastore);
                }
            }
            // [stack +1]  
            {
                // clone array ref  
                buf.write(_dup);
                // [stack +2]  
                CodeGen.push_constant_index(buf, m_classNameConstantIndex);
                // [stack +3]  
                buf.write3(_ldc2_w, m_stampIndex >>> 8, // indexbyte1  
                m_stampIndex);
                // indexbyte2  
                // [stack +5]  
                buf.write3(_invokestatic, m_registerMethodrefIndex >>> 8, // indexbyte1  
                m_registerMethodrefIndex);
            }
            // pop and return extra array ref:  
            buf.write(_areturn);
        }
        final CodeAttribute_info code = new CodeAttribute_info(attribute_name_index, 5, 0, // adjust constants if the bytecode emitted above changes  
        EMPTY_BYTE_ARRAY, AttributeElementFactory.newExceptionHandlerTable(0), ElementFactory.newAttributeCollection(0));
        code.setCode(buf.getByteArray(), buf.size());
        attributes.add(code);
        if (MARK_ADDED_ELEMENTS_SYNTHETIC) {
            attributes.add(new SyntheticAttribute_info(m_syntheticStringIndex));
        }
        final Method_info preclinit = new Method_info(IAccessFlags.ACC_STATIC | IAccessFlags.ACC_PRIVATE, name_index, descriptor_index, attributes);
        cls.addMethod(preclinit);
        if (trace2)
            m_log.trace2("visit", "added synthetic pre-<clinit> method");
    }
    if (constructMetadata) {
        if ($assert.ENABLED)
            $assert.ASSERT(m_classBlockCounts[m_methodID] > 0, "invalid block count for method " + m_methodID + " (" + IClassDefConstants.CLINIT_NAME + "): " + m_classBlockCounts[m_methodID]);
        if ($assert.ENABLED)
            $assert.ASSERT(m_methodBlockSizes != null && m_methodBlockSizes.length == m_classBlockCounts[m_methodID], "invalid block sizes map for method " + m_methodID);
        final int[][] methodBlockMetadata = m_classBlockMetadata[m_methodID];
        m_clinitStatus |= (methodBlockMetadata == null ? IMetadataConstants.METHOD_NO_LINE_NUMBER_TABLE : 0);
        // TODO: this still does not process not added/synthetic case    
        if ((m_clinitStatus & IMetadataConstants.METHOD_ADDED) == 0)
            m_classMethodDescriptors[m_methodID] = new MethodDescriptor(IClassDefConstants.CLINIT_NAME, clinit.getDescriptor(cls), m_clinitStatus, m_methodBlockSizes, methodBlockMetadata, m_methodFirstLine);
    }
    return ctx;
}
