private void expandRelatedModelGroupComponents(XSModelGroup modelGroup, Vector componentList, String namespace, Hashtable dependencies) {
    XSObjectList particles = modelGroup.getParticles();
    final int length = (particles == null) ? 0 : particles.getLength();
    for (int i = 0; i < length; i++) {
        expandRelatedParticleComponents((XSParticle) particles.item(i), componentList, namespace, dependencies);
    }
}
