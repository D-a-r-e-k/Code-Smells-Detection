public MBeanConstructorInfo[] getConstructors() {
    Constructor[] constrs = clazz.getConstructors();
    MBeanConstructorInfo[] toRet = new MBeanConstructorInfo[constrs.length];
    for (int i = 0; i < toRet.length; i++) {
        Class[] cparams = constrs[i].getParameterTypes();
        MBeanParameterInfo[] params = new MBeanParameterInfo[cparams.length];
        for (int j = 0; j < params.length; j++) {
            params[j] = new MBeanParameterInfo(("param" + j), cparams[j].getName(), "param to constructor");
        }
        toRet[i] = new MBeanConstructorInfo(constrs[i].getName(), constrs[i].toString(), params);
    }
    return toRet;
}
