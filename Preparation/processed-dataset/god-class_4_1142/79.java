private void expandRelatedParticleComponents(XSParticle component, Vector componentList, String namespace, Hashtable dependencies) {
    XSTerm term = component.getTerm();
    switch(term.getType()) {
        case XSConstants.ELEMENT_DECLARATION:
            addRelatedElement((XSElementDeclaration) term, componentList, namespace, dependencies);
            break;
        case XSConstants.MODEL_GROUP:
            expandRelatedModelGroupComponents((XSModelGroup) term, componentList, namespace, dependencies);
            break;
        default:
            break;
    }
}
