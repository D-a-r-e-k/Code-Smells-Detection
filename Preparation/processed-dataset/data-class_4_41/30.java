// getNamesValue(String):short 
/**
     * Fixes Microsoft Windows&reg; specific characters.
     * <p>
     * Details about this common problem can be found at 
     * <a href='http://www.cs.tut.fi/~jkorpela/www/windows-chars.html'>http://www.cs.tut.fi/~jkorpela/www/windows-chars.html</a>
     */
protected int fixWindowsCharacter(int origChar) {
    /* PATCH: Asgeir Asgeirsson */
    switch(origChar) {
        case 130:
            return 8218;
        case 131:
            return 402;
        case 132:
            return 8222;
        case 133:
            return 8230;
        case 134:
            return 8224;
        case 135:
            return 8225;
        case 136:
            return 710;
        case 137:
            return 8240;
        case 138:
            return 352;
        case 139:
            return 8249;
        case 140:
            return 338;
        case 145:
            return 8216;
        case 146:
            return 8217;
        case 147:
            return 8220;
        case 148:
            return 8221;
        case 149:
            return 8226;
        case 150:
            return 8211;
        case 151:
            return 8212;
        case 152:
            return 732;
        case 153:
            return 8482;
        case 154:
            return 353;
        case 155:
            return 8250;
        case 156:
            return 339;
        case 159:
            return 376;
    }
    return origChar;
}
