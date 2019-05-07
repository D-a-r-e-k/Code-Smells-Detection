// Implementations for ClassVisitor. 
public void visitProgramClass(ProgramClass programClass) {
    if (CaughtClassMarker.isCaught(programClass)) {
        classVisitor.visitProgramClass(programClass);
    }
}
