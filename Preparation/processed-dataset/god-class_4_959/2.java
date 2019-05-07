// IClassDefVisitor:  
public Object visit(final ClassDef ignore, final Object ctx) {
    final ClassDef cls = m_cls;
    final String clsVMName = cls.getName();
    final String clsName = Types.vmNameToJavaName(clsVMName);
    final boolean trace1 = m_log.atTRACE1();
    if (trace1)
        m_log.trace1("visit", "class: [" + clsVMName + "]");
    // skip synthetic classes if enabled:  
    if (SKIP_SYNTHETIC_CLASSES && cls.isSynthetic()) {
        m_instrument = false;
        m_metadata = false;
        if (trace1)
            m_log.trace1("visit", "skipping synthetic class");
        return ctx;
    }
    // TODO: ideally, this check should be done in outer scope somewhere  
    if (!m_warningIssued && clsName.startsWith(IAppConstants.APP_PACKAGE)) {
        m_warningIssued = true;
        m_log.warning(IAppConstants.APP_NAME + " classes appear to be included on the instrumentation");
        m_log.warning("path: this is not a correct way to use " + IAppConstants.APP_NAME);
    }
    // field uniqueness check done to detect double instrumentation:  
    {
        final int[] existing = cls.getFields(COVERAGE_FIELD_NAME);
        if (existing.length > 0) {
            m_instrument = false;
            m_metadata = false;
            if (m_ignoreAlreadyInstrumented) {
                if (trace1)
                    m_log.trace1("visit", "skipping instrumented class");
                return ctx;
            } else {
                // TODO: use a app coded exception  
                throw new IllegalStateException("class [" + clsName + "] appears to be instrumented already");
            }
        }
    }
    final IConstantCollection constants = cls.getConstants();
    SyntheticAttribute_info syntheticMarker = null;
    // cache the location of "Synthetic" string:  
    {
        if (MARK_ADDED_ELEMENTS_SYNTHETIC)
            m_syntheticStringIndex = cls.addCONSTANT_Utf8(Attribute_info.ATTRIBUTE_SYNTHETIC, true);
    }
    // add a Fieldref for the runtime coverage collector field:  
    {
        // note: this is a bit premature if the class has no methods that need  
        // instrumentation  
        // TODO: the mutated version is easily discardable; however, this case  
        // needs attention at metadata/report generation level  
        final int coverageFieldOffset;
        final String fieldDescriptor = "[[Z";
        // note that post-4019 builds can modify this field outside of <clinit> (although  
        // it can only happen as part of initializing a set of classes); however, it is legal  
        // to declare this field final:  
        final int fieldModifiers = IAccessFlags.ACC_PRIVATE | IAccessFlags.ACC_STATIC | IAccessFlags.ACC_FINAL;
        // add declared field:  
        if (MARK_ADDED_ELEMENTS_SYNTHETIC) {
            final IAttributeCollection fieldAttributes = ElementFactory.newAttributeCollection(1);
            syntheticMarker = new SyntheticAttribute_info(m_syntheticStringIndex);
            fieldAttributes.add(syntheticMarker);
            coverageFieldOffset = cls.addField(COVERAGE_FIELD_NAME, fieldDescriptor, fieldModifiers, fieldAttributes);
        } else {
            coverageFieldOffset = cls.addField(COVERAGE_FIELD_NAME, fieldDescriptor, fieldModifiers);
        }
        //add fieldref:  
        m_coverageFieldrefIndex = cls.addFieldref(coverageFieldOffset);
    }
    // add a Methodref for Runtime.r():  
    {
        // TODO: compute this without loading Runtime Class?  
        final String classJVMName = "com/vladium/emma/rt/RT";
        final int class_index = cls.addClassref(classJVMName);
        // NOTE: keep this descriptor in sync with the actual signature  
        final String methodDescriptor = "([[ZLjava/lang/String;J)V";
        final int nametype_index = cls.addNameType("r", methodDescriptor);
        m_registerMethodrefIndex = constants.add(new CONSTANT_Methodref_info(class_index, nametype_index));
    }
    // SF FR 971186: split the init logic into a separate method so it could  
    // be called from regular method headers if necessary:   
    // add a Methodref for pre-<clinit> method:  
    {
        // NOTE: keep this descriptor in sync with the actual signature  
        final String methodDescriptor = "()[[Z";
        final int nametype_index = cls.addNameType(PRECLINIT_METHOD_NAME, methodDescriptor);
        m_preclinitMethodrefIndex = constants.add(new CONSTANT_Methodref_info(cls.getThisClassIndex(), nametype_index));
    }
    // add a CONSTANT_String that corresponds to the class name [in JVM format]:  
    {
        m_classNameConstantIndex = constants.add(new CONSTANT_String_info(cls.getThisClass().m_name_index));
    }
    // visit method collection:           
    visit(cls.getMethods(), ctx);
    // if necessary, do SUID compensation [need to be done after method  
    // visits when it is known whether a <clinit> was added]:  
    if (m_doSUIDCompensation) {
        // compensation not necessary if the original clsdef already defined <clinit>:  
        boolean compensate = ((m_clinitStatus & IMetadataConstants.METHOD_ADDED) != 0);
        int existingSUIDFieldCount = 0;
        if (compensate) {
            // compensation not necessary if the original clsdef already controlled it via 'serialVersionUID':  
            {
                final int[] existing = cls.getFields(SUID_FIELD_NAME);
                existingSUIDFieldCount = existing.length;
                if (existingSUIDFieldCount > 0) {
                    final IFieldCollection fields = cls.getFields();
                    for (int f = 0; f < existingSUIDFieldCount; ++f) {
                        final Field_info field = fields.get(existing[f]);
                        if ((field.getAccessFlags() & (IAccessFlags.ACC_STATIC | IAccessFlags.ACC_FINAL)) == (IAccessFlags.ACC_STATIC | IAccessFlags.ACC_FINAL)) {
                            // TODO: should also check for presence of a non-zero initializer  
                            compensate = false;
                            break;
                        }
                    }
                }
            }
            // compensation not necessary if we can determine that this class  
            // does not implement java.io.Serializable/Externalizable:  
            if (compensate && (cls.getThisClassIndex() == 0)) // no superclasses [this tool can't traverse inheritance chains]  
            {
                boolean serializable = false;
                final IInterfaceCollection interfaces = cls.getInterfaces();
                for (int i = 0, iLimit = interfaces.size(); i < iLimit; ++i) {
                    final CONSTANT_Class_info ifc = (CONSTANT_Class_info) constants.get(interfaces.get(i));
                    final String ifcName = ifc.getName(cls);
                    if (JAVA_IO_SERIALIZABLE_NAME.equals(ifcName) || JAVA_IO_EXTERNALIZABLE_NAME.equals(ifcName)) {
                        serializable = true;
                        break;
                    }
                }
                if (!serializable)
                    compensate = false;
            }
        }
        if (compensate) {
            if (existingSUIDFieldCount > 0) {
                // if we get here, the class declares a 'serialVersionUID' field  
                // that is not both static and final and/or is not initialized  
                // statically: warn that SUID compensation may not work  
                m_log.warning("class [" + clsName + "] declares a 'serialVersionUID'");
                m_log.warning("field that is not static and final: this is likely an implementation mistake");
                m_log.warning("and can interfere with " + IAppConstants.APP_NAME + "'s SUID compensation");
            }
            final String fieldDescriptor = "J";
            final int fieldModifiers = IAccessFlags.ACC_PRIVATE | IAccessFlags.ACC_STATIC | IAccessFlags.ACC_FINAL;
            final IAttributeCollection fieldAttributes = ElementFactory.newAttributeCollection(MARK_ADDED_ELEMENTS_SYNTHETIC ? 2 : 1);
            final int nameIndex = cls.addCONSTANT_Utf8(Attribute_info.ATTRIBUTE_CONSTANT_VALUE, true);
            final int valueIndex = constants.add(new CONSTANT_Long_info(cls.computeSUID(true)));
            // ignore the added <clinit>  
            final ConstantValueAttribute_info initializer = new ConstantValueAttribute_info(nameIndex, valueIndex);
            fieldAttributes.add(initializer);
            if (MARK_ADDED_ELEMENTS_SYNTHETIC) {
                if (syntheticMarker == null)
                    syntheticMarker = new SyntheticAttribute_info(m_syntheticStringIndex);
                fieldAttributes.add(syntheticMarker);
            }
            cls.addField(SUID_FIELD_NAME, fieldDescriptor, fieldModifiers, fieldAttributes);
        }
    }
    // if (m_doSUIDCompensation)  
    // visit class attributes [to get src file name, etc]:  
    visit(cls.getAttributes(), ctx);
    return ctx;
}
