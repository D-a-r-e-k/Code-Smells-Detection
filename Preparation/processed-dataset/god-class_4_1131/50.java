// getDeclaredEntities():Hashtable  
/** Prints the contents of the buffer. */
static final void print(ScannedEntity currentEntity) {
    if (DEBUG_BUFFER) {
        if (currentEntity != null) {
            System.out.print('[');
            System.out.print(currentEntity.count);
            System.out.print(' ');
            System.out.print(currentEntity.position);
            if (currentEntity.count > 0) {
                System.out.print(" \"");
                for (int i = 0; i < currentEntity.count; i++) {
                    if (i == currentEntity.position) {
                        System.out.print('^');
                    }
                    char c = currentEntity.ch[i];
                    switch(c) {
                        case '\n':
                            {
                                System.out.print("\\n");
                                break;
                            }
                        case '\r':
                            {
                                System.out.print("\\r");
                                break;
                            }
                        case '\t':
                            {
                                System.out.print("\\t");
                                break;
                            }
                        case '\\':
                            {
                                System.out.print("\\\\");
                                break;
                            }
                        default:
                            {
                                System.out.print(c);
                            }
                    }
                }
                if (currentEntity.position == currentEntity.count) {
                    System.out.print('^');
                }
                System.out.print('"');
            }
            System.out.print(']');
            System.out.print(" @ ");
            System.out.print(currentEntity.lineNumber);
            System.out.print(',');
            System.out.print(currentEntity.columnNumber);
        } else {
            System.out.print("*NO CURRENT ENTITY*");
        }
    }
}
