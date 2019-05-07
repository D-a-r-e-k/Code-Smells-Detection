private boolean isAttributeMethodOverloaded(Method toCheckMeth) {
    String toCheckStr = toCheckMeth.getName();
    String methStr = null;
    if (toCheckStr.startsWith("is")) {
        toCheckStr = toCheckStr.substring(2);
    } else if (toCheckStr.startsWith("get")) {
        toCheckStr = toCheckStr.substring(3);
    }
    if (toCheckMeth.getName().startsWith("is") || toCheckMeth.getName().startsWith("get")) {
        for (int i = 0; i < readMeths.size(); i++) {
            Method meth = (Method) readMeths.get(i);
            methStr = meth.getName();
            if (toCheckStr.equals(methStr.substring(3))) {
                if (!(toCheckMeth.getReturnType().getName().equals(meth.getReturnType().getName())) || (toCheckMeth.getDeclaringClass().getName().equals(meth.getDeclaringClass().getName()))) {
                    //readMeths.remove(i); 
                    return true;
                }
                readMeths.remove(i);
            }
        }
        for (int i = 0; i < isIsMeths.size(); i++) {
            Method meth = (Method) isIsMeths.get(i);
            methStr = meth.getName();
            if (toCheckStr.equals(methStr.substring(2))) {
                if (!(toCheckMeth.getReturnType().getName().equals(meth.getReturnType().getName())) || (toCheckMeth.getDeclaringClass().getName().equals(meth.getDeclaringClass().getName()))) {
                    //isIsMeths.remove(i); 
                    return true;
                }
                isIsMeths.remove(i);
            }
        }
    } else if (toCheckMeth.getName().startsWith("set")) {
        toCheckStr = toCheckStr.substring(3);
        for (int i = 0; i < writeMeths.size(); i++) {
            Method meth = (Method) writeMeths.get(i);
            methStr = ((Method) writeMeths.get(i)).getName();
            if (toCheckStr.equals(methStr.substring(3))) {
                if (!(toCheckMeth.getParameterTypes()[0].getName().equals(meth.getParameterTypes()[0].getName())) || (toCheckMeth.getDeclaringClass().getName().equals(meth.getDeclaringClass().getName()))) {
                    //writeMeths.remove(i); 
                    return true;
                }
                writeMeths.remove(i);
            }
        }
    }
    return false;
}
