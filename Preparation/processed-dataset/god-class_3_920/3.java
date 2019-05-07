private MBeanAttributeInfo[] getAttributes() throws Exception {
    int length = 0;
    int size = 0;
    int pos = 0;
    //Method[] meths = iclazz.getDeclaredMethods(); 
    //Method[] meths = iclazz.getDeclaredMethods(); 
    Method[] meths = iclazz.getMethods();
    readMeths = new Vector();
    writeMeths = new Vector();
    isIsMeths = new Vector();
    Vector readableVec = new Vector();
    Vector writableVec = new Vector();
    Vector isWriteStatus = new Vector();
    String[] types = null;
    String[] names = null;
    /** The Changes have been done for making standard MBean TCK compliance.
            However the restrictions are
            1. Overlaoaded methods are not handled.
            2.If set method with multiple parameter comes it is treated as Attribute
            ----> The restictions are to be fixed.
        */
    /** This loop is used to identify the attribute. Seperates "get", "set" and "is" **/
    for (int i = 0; i < meths.length; i++) {
        ///test 
        String methName = meths[i].getName();
        //////// 
        /** This is to avoid the method by named "get" "set" and "is" getting added in
                attribute list. */
        if (methName.equals("get") || methName.equals("set") || methName.equals("is")) {
            continue;
        }
        if ((methName.startsWith("get") && meths[i].getParameterTypes().length == 0) && !(meths[i].getReturnType().getName().equals("void")) || (methName.startsWith("is") && (meths[i].getReturnType().getName().equals("boolean") || meths[i].getReturnType().getName().equals("java.lang.Boolean")) && meths[i].getParameterTypes().length == 0)) {
            //System.out.println(" READ METH IS "+meths[i].getName()); 
            if (methName.startsWith("get")) {
                /*if(isAttributeMethodOverloaded(meths[i])){
                      throw new NotCompliantMBeanException();
                      }*/
                readMeths.add(meths[i]);
            } else {
                isIsMeths.add(meths[i]);
            }
        }
        if (methName.startsWith("set") && meths[i].getParameterTypes().length == 1 && meths[i].getReturnType().getName().equals("void")) {
            writeMeths.add(meths[i]);
        }
    }
    /** Comparison is done between get and set attribute to fine read-write access */
    for (int i = 0; i < readMeths.size(); i++) {
        boolean isMatched = false;
        Method read = (Method) readMeths.get(i);
        /** This remove and add is done to avoid checking the same method */
        readMeths.remove(i);
        if (isAttributeMethodOverloaded(read)) {
            throw new NotCompliantMBeanException();
        }
        readMeths.add(i, read);
        for (int j = 0; j < writeMeths.size(); j++) {
            Method write = (Method) writeMeths.get(j);
            writeMeths.remove(j);
            if (isAttributeMethodOverloaded(write)) {
                throw new NotCompliantMBeanException();
            }
            writeMeths.add(j, write);
            if (((Method) readMeths.get(i)).getName().substring(3).equals(((Method) writeMeths.get(j)).getName().substring(3))) {
                if (!(read.getReturnType().getName().equals(write.getParameterTypes()[0].getName()))) {
                    throw new NotCompliantMBeanException();
                }
                readableVec.add(new Boolean(true));
                writableVec.add(new Boolean(true));
                writeMeths.remove(j);
                isMatched = true;
                break;
            }
        }
        if (!isMatched) {
            readableVec.add(new Boolean(true));
            writableVec.add(new Boolean(false));
        }
    }
    /** Comparison is done between is and set attribute to find read-write access */
    for (int i = 0; i < isIsMeths.size(); i++) {
        boolean isMatched = false;
        Method is = (Method) isIsMeths.get(i);
        isIsMeths.remove(i);
        if (isAttributeMethodOverloaded(is)) {
            throw new NotCompliantMBeanException();
        }
        isIsMeths.add(i, is);
        for (int j = 0; j < writeMeths.size(); j++) {
            Method write = (Method) writeMeths.get(j);
            if (is.getName().substring(2).equals(write.getName().substring(3))) {
                if (!(is.getReturnType().getName().equals(write.getParameterTypes()[0].getName()))) {
                    throw new NotCompliantMBeanException();
                }
                /*readableVec.add(new Boolean(true));
                      writableVec.add(new Boolean(true));
                      writeMeths.remove(j);*/
                isWriteStatus.add(new Boolean(true));
                isMatched = true;
                writeMeths.remove(j);
                break;
            }
        }
        if (!isMatched) {
            //readableVec.add(new Boolean(true)); 
            //writableVec.add(new Boolean(false)); 
            isWriteStatus.add(new Boolean(false));
        }
    }
    /** Only the write access attribute */
    for (int i = 0; i < writeMeths.size(); i++) {
        Method meth = (Method) writeMeths.get(i);
        writeMeths.remove(i);
        if (isAttributeMethodOverloaded(meth)) {
            throw new NotCompliantMBeanException();
        }
        writeMeths.add(i, meth);
        readableVec.add(new Boolean(false));
        writableVec.add(new Boolean(true));
    }
    size = readMeths.size() + writeMeths.size();
    types = new String[size];
    names = new String[readMeths.size() + writeMeths.size() + isIsMeths.size()];
    pos = 0;
    /** Find the types of attribute and split the attributes name */
    for (int i = 0; i < readMeths.size(); i++) {
        names[i] = ((Method) readMeths.get(i)).getName().substring(3);
        //types[i] = convertToJmxArrayType(((Method)readMeths.get(i)).getReturnType().getName()); 
        types[i] = (((Method) readMeths.get(i)).getReturnType().getName());
    }
    for (int i = readMeths.size(); i < size; i++) {
        //types[i] = convertToJmxArrayType(((Method)writeMeths.get(pos)).getParameterTypes()[0].getName()); 
        types[i] = (((Method) writeMeths.get(pos)).getParameterTypes()[0].getName());
        names[i] = ((Method) writeMeths.get(pos)).getName().substring(3);
        pos++;
    }
    MBeanAttributeInfo[] toRet = new MBeanAttributeInfo[names.length];
    String des = "This attribute is present in a Standard MBean";
    for (int i = 0; i < size; i++) {
        if (clazz.getName().equals("javax.management.MBeanServerDelegate")) {
            if (names[i].equals("MBeanServerId"))
                des = "Specifies the ID of this JMX MBean Server";
            else if (names[i].equals("ImplementationName"))
                des = "Specifies the JMX implementation name (the name of this product).";
            else if (names[i].equals("ImplementationVendor"))
                des = "Specifies the JMX implementation vendor (the vendor of this product)";
            else if (names[i].equals("ImplementationVersion"))
                des = "Specifies the JMX implementation version (the version of this product). ";
            else if (names[i].equals("SpecificationName"))
                des = "Specifies the full name of the JMX specification implemented by this product.";
            else if (names[i].equals("SpecificationVendor"))
                des = "Specifies the vendor of the JMX specification implemented by this product.";
            else if (names[i].equals("SpecificationVersion"))
                des = "Specifies the version of the JMX specification implemented by this product.";
        } else if (clazz.getName().equals("javax.management.loading.MLet")) {
            if (names[i].equals("URLs"))
                des = "specifies the search path of URLs for loading classes and resources.";
        }
        toRet[i] = new MBeanAttributeInfo(names[i], types[i], des, ((Boolean) readableVec.get(i)).booleanValue(), ((Boolean) writableVec.get(i)).booleanValue(), false);
    }
    /** isIs artributes are handled here */
    pos = 0;
    //int writePos = size; 
    for (int i = size; i < toRet.length; i++) {
        Method meth = (Method) isIsMeths.get(pos);
        //toRet[i] = new MBeanAttributeInfo(meth.getName().substring(2),meth.getReturnType().getName(),des,true,((Boolean)writableVec.get(writePos)).booleanValue(),true); 
        toRet[i] = new MBeanAttributeInfo(meth.getName().substring(2), meth.getReturnType().getName(), des, true, ((Boolean) isWriteStatus.get(pos)).booleanValue(), true);
        pos++;
    }
    return toRet;
}
