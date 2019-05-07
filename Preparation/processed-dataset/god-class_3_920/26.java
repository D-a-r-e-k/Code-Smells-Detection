public void postRegister(java.lang.Boolean registrationDone) {
    if (!(object instanceof MBeanRegistration))
        return;
    ((MBeanRegistration) object).postRegister(registrationDone);
}
