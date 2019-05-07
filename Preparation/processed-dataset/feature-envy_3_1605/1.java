//----------------/ 
//- Constructors -/ 
//----------------/ 
public PartialTerminationDescriptor() {
    super();
    _nsURI = "http://www.fpml.org/2003/FpML-4-0";
    _xmlName = "partialTermination";
    _elementDefinition = true;
    //-- set grouping compositor 
    setCompositorAsSequence();
    org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
    org.exolab.castor.mapping.FieldHandler handler = null;
    org.exolab.castor.xml.FieldValidator fieldValidator = null;
    //-- initialize attribute descriptors 
    //-- initialize element descriptors 
    //-- _partyOne 
    desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.PartyOne.class, "_partyOne", "partyOne", org.exolab.castor.xml.NodeType.Element);
    handler = new org.exolab.castor.xml.XMLFieldHandler() {

        public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
            PartialTermination target = (PartialTermination) object;
            return target.getPartyOne();
        }

        public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
            try {
                PartialTermination target = (PartialTermination) object;
                target.setPartyOne((org.castor.xmlctf.bestpractise.genpackage.PartyOne) value);
            } catch (java.lang.Exception ex) {
                throw new IllegalStateException(ex.toString());
            }
        }

        public java.lang.Object newInstance(java.lang.Object parent) {
            return new org.castor.xmlctf.bestpractise.genpackage.PartyOne();
        }
    };
    desc.setSchemaType("xml.srcgen.template.generated.PartyOne");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
    desc.setRequired(true);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    //-- validation code for: _partyOne 
    fieldValidator = new org.exolab.castor.xml.FieldValidator();
    fieldValidator.setMinOccurs(1);
    {
    }
    desc.setValidator(fieldValidator);
    //-- _partyTwo 
    desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.PartyTwo.class, "_partyTwo", "partyTwo", org.exolab.castor.xml.NodeType.Element);
    handler = new org.exolab.castor.xml.XMLFieldHandler() {

        public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
            PartialTermination target = (PartialTermination) object;
            return target.getPartyTwo();
        }

        public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
            try {
                PartialTermination target = (PartialTermination) object;
                target.setPartyTwo((org.castor.xmlctf.bestpractise.genpackage.PartyTwo) value);
            } catch (java.lang.Exception ex) {
                throw new IllegalStateException(ex.toString());
            }
        }

        public java.lang.Object newInstance(java.lang.Object parent) {
            return new org.castor.xmlctf.bestpractise.genpackage.PartyTwo();
        }
    };
    desc.setSchemaType("xml.srcgen.template.generated.PartyTwo");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
    desc.setRequired(true);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    //-- validation code for: _partyTwo 
    fieldValidator = new org.exolab.castor.xml.FieldValidator();
    fieldValidator.setMinOccurs(1);
    {
    }
    desc.setValidator(fieldValidator);
    //-- _partyThreeHref 
    desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.PartyThreeHref.class, "_partyThreeHref", "partyThreeHref", org.exolab.castor.xml.NodeType.Element);
    handler = new org.exolab.castor.xml.XMLFieldHandler() {

        public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
            PartialTermination target = (PartialTermination) object;
            return target.getPartyThreeHref();
        }

        public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
            try {
                PartialTermination target = (PartialTermination) object;
                target.setPartyThreeHref((org.castor.xmlctf.bestpractise.genpackage.PartyThreeHref) value);
            } catch (java.lang.Exception ex) {
                throw new IllegalStateException(ex.toString());
            }
        }

        public java.lang.Object newInstance(java.lang.Object parent) {
            return new org.castor.xmlctf.bestpractise.genpackage.PartyThreeHref();
        }
    };
    desc.setSchemaType("xml.srcgen.template.generated.PartyThreeHref");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    //-- validation code for: _partyThreeHref 
    fieldValidator = new org.exolab.castor.xml.FieldValidator();
    {
    }
    desc.setValidator(fieldValidator);
    //-- _partialAmount 
    desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.math.BigDecimal.class, "_partialAmount", "partialAmount", org.exolab.castor.xml.NodeType.Element);
    handler = new org.exolab.castor.xml.XMLFieldHandler() {

        public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
            PartialTermination target = (PartialTermination) object;
            return target.getPartialAmount();
        }

        public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
            try {
                PartialTermination target = (PartialTermination) object;
                target.setPartialAmount((java.math.BigDecimal) value);
            } catch (java.lang.Exception ex) {
                throw new IllegalStateException(ex.toString());
            }
        }

        public java.lang.Object newInstance(java.lang.Object parent) {
            return new java.math.BigDecimal(0);
        }
    };
    desc.setImmutable(true);
    desc.setSchemaType("decimal");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
    desc.setRequired(true);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    //-- validation code for: _partialAmount 
    fieldValidator = new org.exolab.castor.xml.FieldValidator();
    fieldValidator.setMinOccurs(1);
    {
        //-- local scope 
        org.exolab.castor.xml.validators.DecimalValidator typeValidator;
        typeValidator = new org.exolab.castor.xml.validators.DecimalValidator();
        fieldValidator.setValidator(typeValidator);
    }
    desc.setValidator(fieldValidator);
    //-- _assignmentNotificationList 
    desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification.class, "_assignmentNotificationList", "assignmentNotification", org.exolab.castor.xml.NodeType.Element);
    handler = new org.exolab.castor.xml.XMLFieldHandler() {

        public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
            PartialTermination target = (PartialTermination) object;
            return target.getAssignmentNotification();
        }

        public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
            try {
                PartialTermination target = (PartialTermination) object;
                target.addAssignmentNotification((org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification) value);
            } catch (java.lang.Exception ex) {
                throw new IllegalStateException(ex.toString());
            }
        }

        public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
            try {
                PartialTermination target = (PartialTermination) object;
                target.removeAllAssignmentNotification();
            } catch (java.lang.Exception ex) {
                throw new IllegalStateException(ex.toString());
            }
        }

        public java.lang.Object newInstance(java.lang.Object parent) {
            return new org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification();
        }
    };
    desc.setSchemaType("list");
    desc.setComponentType("xml.srcgen.template.generated.AssignmentNotification");
    desc.setHandler(handler);
    desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
    desc.setMultivalued(true);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    //-- validation code for: _assignmentNotificationList 
    fieldValidator = new org.exolab.castor.xml.FieldValidator();
    fieldValidator.setMinOccurs(0);
    {
    }
    desc.setValidator(fieldValidator);
    //-- _partialTerminationChoice 
    desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.castor.xmlctf.bestpractise.genpackage.PartialTerminationChoice.class, "_partialTerminationChoice", "-error-if-this-is-used-", org.exolab.castor.xml.NodeType.Element);
    handler = new org.exolab.castor.xml.XMLFieldHandler() {

        public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
            PartialTermination target = (PartialTermination) object;
            return target.getPartialTerminationChoice();
        }

        public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException, IllegalArgumentException {
            try {
                PartialTermination target = (PartialTermination) object;
                target.setPartialTerminationChoice((org.castor.xmlctf.bestpractise.genpackage.PartialTerminationChoice) value);
            } catch (java.lang.Exception ex) {
                throw new IllegalStateException(ex.toString());
            }
        }

        public java.lang.Object newInstance(java.lang.Object parent) {
            return new org.castor.xmlctf.bestpractise.genpackage.PartialTerminationChoice();
        }
    };
    desc.setSchemaType("xml.srcgen.template.generated.PartialTerminationChoice");
    desc.setHandler(handler);
    desc.setContainer(true);
    desc.setClassDescriptor(new org.castor.xmlctf.bestpractise.genpackage.descriptors.PartialTerminationChoiceDescriptor());
    desc.setNameSpaceURI("http://www.fpml.org/2003/FpML-4-0");
    desc.setRequired(true);
    desc.setMultivalued(false);
    addFieldDescriptor(desc);
    addSequenceElement(desc);
    //-- validation code for: _partialTerminationChoice 
    fieldValidator = new org.exolab.castor.xml.FieldValidator();
    fieldValidator.setMinOccurs(1);
    {
    }
    desc.setValidator(fieldValidator);
}
