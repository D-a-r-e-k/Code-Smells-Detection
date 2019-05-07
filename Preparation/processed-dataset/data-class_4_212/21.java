/**
	 * @see org.webcurator.core.targets.TargetManager#getAnnotations(org.webcurator.domain.model.core.AbstractTarget)
	 */
public List<Annotation> getAnnotations(AbstractTarget aTarget) {
    List<Annotation> annotations = null;
    if (aTarget.getOid() != null) {
        String className;
        if (aTarget instanceof TargetGroup) {
            //Special case for lazy loaded groups  
            className = TargetGroup.class.getName();
        } else {
            className = aTarget.getClass().getName();
        }
        annotations = annotationDAO.loadAnnotations(className, aTarget.getOid());
    }
    if (annotations == null) {
        annotations = new ArrayList<Annotation>();
    }
    return annotations;
}
