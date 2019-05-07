/*
 * Author jyang
 * Created on 2006-4-3 17:33:59
 */
package com.jasml.classes;

public class Attribute {
    // two byte attribute name index into constant pool, for attribute already known, 
    // this is not used, but translated into attribute tag    
    public int attribute_name_index;
    
    public byte attribute_tag;
    
    public int attribute_length;	
	
    public byte[] attrInfo;
    
    public String attribute_name;
	
    /*
     * This is for subclass
     */
	public Attribute(byte attrTag, int attrLength){
	    this.attribute_tag = attrTag;
	    this.attribute_length = attrLength;
	    attribute_name = Constants.ATTRIBUTE_NAMES[attribute_tag];
	}
	
	/*
	 * this is for the unknow attributes, will just store the attrInfo.
	 */
	public Attribute(int attribute_name_index, int attribute_length, byte[] attrInfo){
	    this.attribute_name_index = attribute_name_index;
	    this.attribute_length = attribute_length;
	    this.attrInfo = attrInfo;
	}

}
