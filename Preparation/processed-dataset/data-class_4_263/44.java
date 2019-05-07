//}}}  
//{{{ isElectricKey() methods  
/**
	 * Should inserting this character trigger a re-indent of
	 * the current line?
	 * @since jEdit 4.3pre2
	 * @deprecated Use #isElectricKey(char,int)
	 */
public boolean isElectricKey(char ch) {
    return mode.isElectricKey(ch);
}
