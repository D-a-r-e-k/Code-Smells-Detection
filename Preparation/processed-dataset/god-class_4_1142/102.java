private boolean removeParticle(XSModelGroupImpl group, XSParticleDecl particle) {
    XSParticleDecl member;
    for (int i = 0; i < group.fParticleCount; i++) {
        member = group.fParticles[i];
        if (member == particle) {
            for (int j = i; j < group.fParticleCount - 1; j++) group.fParticles[j] = group.fParticles[j + 1];
            group.fParticleCount--;
            return true;
        }
        if (member.fType == XSParticleDecl.PARTICLE_MODELGROUP) {
            if (removeParticle((XSModelGroupImpl) member.fValue, particle))
                return true;
        }
    }
    return false;
}
