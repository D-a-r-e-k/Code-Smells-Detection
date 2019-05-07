/*
 * Author jyang Created on 2006-4-3 15:49:46
 */
package com.jasml.classes;

public class JavaClass {
    public int magic = 0xCAFEBABE;

    public int minor_version = 0;

    public int major_version = 46;

    public int constant_pool_count;

    public ConstantPool constantPool;

    public short access_flags;

    public int this_class;

    public int super_class;

    public int interfaces_count;

    public int[] interfaces;

    public int fields_count;

    public Field[] fields;

    public int methods_count;

    public Method[] methods;

    public int attributes_count;

    public Attribute[] attributes;

}