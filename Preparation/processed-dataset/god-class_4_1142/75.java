private void expandRelatedComplexTypeComponents(XSComplexTypeDecl type, Vector componentList, String namespace, Hashtable dependencies) {
    addRelatedType(type.getBaseType(), componentList, namespace, dependencies);
    expandRelatedAttributeUsesComponents(type.getAttributeUses(), componentList, namespace, dependencies);
    final XSParticle particle = type.getParticle();
    if (particle != null) {
        expandRelatedParticleComponents(particle, componentList, namespace, dependencies);
    }
}
