public void visitLibraryClass(LibraryClass libraryClass) {
    if (CaughtClassMarker.isCaught(libraryClass)) {
        classVisitor.visitLibraryClass(libraryClass);
    }
}
