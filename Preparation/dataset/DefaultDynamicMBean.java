/**
* The XMOJO Project 5
* Copyright � 2003 XMOJO.org. All rights reserved.

* NO WARRANTY

* BECAUSE THE LIBRARY IS LICENSED FREE OF CHARGE, THERE IS NO WARRANTY FOR
* THE LIBRARY, TO THE EXTENT PERMITTED BY APPLICABLE LAW. EXCEPT WHEN
* OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES
* PROVIDE THE LIBRARY "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED
* OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS
* TO THE QUALITY AND PERFORMANCE OF THE LIBRARY IS WITH YOU. SHOULD THE
* LIBRARY PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING,
* REPAIR OR CORRECTION.

* IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING WILL
* ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR REDISTRIBUTE
* THE LIBRARY AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY
* GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE
* USE OR INABILITY TO USE THE LIBRARY (INCLUDING BUT NOT LIMITED TO LOSS OF
* DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD
* PARTIES OR A FAILURE OF THE LIBRARY TO OPERATE WITH ANY OTHER SOFTWARE),
* EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF
* SUCH DAMAGES.
**/

package com.adventnet.jmx;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Vector;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.DynamicMBean;
import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MBeanRegistration;
import javax.management.MBeanInfo;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.NotificationFilter;
import javax.management.NotificationBroadcaster;
import javax.management.AttributeNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.NotCompliantMBeanException;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeMBeanException;
import javax.management.RuntimeOperationsException;
import javax.management.InvalidAttributeValueException;

/**
 * This class acts as the wrapper dynamic mbean for standard mbeans
 * that intends to register with the MBeanServer. This class gets
 * instantiated from the server's registerMBean method, whenever the
 * incoming object is a standard mbean.
 */
public class DefaultDynamicMBean implements DynamicMBean,
											NotificationBroadcaster,
                                     		NotificationListener,
                                     		NotificationFilter,
                                     		MBeanRegistration
{
    public MBeanInfo mbeanInfo = null;
    public Object object = null;
    public Class clazz = null;
    public Class iclazz = null;
    public Method[] superInterfaceMeths = null;
    public Vector readMeths = null;
    public Vector writeMeths = null;
    public Vector isIsMeths = null;

    public DefaultDynamicMBean(Object object) throws Exception
    {
        this.object = object;
        clazz = object.getClass();

        Class[] interfaces = clazz.getInterfaces();

        boolean flag = false;

        if(interfaces == null)
        {
            clazz = clazz.getSuperclass();
            if(clazz == null)
                throw new Exception("NonJmxMBeanRegistrationException");
            if((interfaces = clazz.getInterfaces()) == null)
                throw new Exception("NonJmxMBeanRegistrationException");
        }
        else
        {
            for(int i = 0;i<interfaces.length;i++)
            {
                if(interfaces[i].getName().equals(clazz.getName() + "MBean") )
                {
                    iclazz = interfaces[i];
                    flag = true;
                    /** This will be used for Attribute's get, set and is methods overloading */
                    Class[] interClass = interfaces[i].getInterfaces();
                    if(interClass != null)
					{
                        if(interClass.length >0)
						{
                            superInterfaceMeths = interClass[0].getMethods();
                        }
                    }
                    break;
                }
            }

            if(!flag)
            {
                clazz = clazz.getSuperclass();
                if(clazz == null)
                    throw new Exception("NonJmxMBeanRegistrationException");
                if((interfaces = clazz.getInterfaces()) == null)
                    throw new Exception("NonJmxMBeanRegistrationException");
			}
    	}

        if(!flag)
        {
			for(int i = 0;i<interfaces.length;i++)
            {
                if(interfaces[i].getName().equals(clazz.getName() + "MBean") )
                    {
                        iclazz = interfaces[i];
                        flag = true;
                        break;
                    }
            }

			while(!flag)
			{
				flag = getFlagValue(clazz,flag);
        		if(clazz==null)
					break;
			}
		}

        if(!flag)
            throw new Exception("NonJmxMBeanRegistrationException");

        clazz = object.getClass();
        makeMBeanInfo();
    }

    public Object getStandardMBeanObject()
    {
        return object;
    }

    protected void makeMBeanInfo() throws Exception
    {
        String className = clazz.getName();
        String description = className + " MBean";

        mbeanInfo = new MBeanInfo(className,
                                  description,
                                  getAttributes(),
                                  getConstructors(),
                                  getOperations(),
                                  getNotifications());
    }

    private MBeanAttributeInfo[] getAttributes() throws Exception
    {
        int length = 0;
        int size = 0;
        int pos = 0;
        //Method[] meths = iclazz.getDeclaredMethods();
        //Method[] meths = iclazz.getDeclaredMethods();
        Method[] meths = iclazz.getMethods();
        readMeths =  new Vector();
        writeMeths = new Vector();
        isIsMeths =  new Vector();
        Vector readableVec= new Vector();
        Vector writableVec= new Vector();
        Vector isWriteStatus = new Vector();
        String[]  types = null;
        String[] names = null;

        /** The Changes have been done for making standard MBean TCK compliance.
            However the restrictions are
            1. Overlaoaded methods are not handled.
            2.If set method with multiple parameter comes it is treated as Attribute
            ----> The restictions are to be fixed.
        */

        /** This loop is used to identify the attribute. Seperates "get", "set" and "is" **/


        for(int i=0; i<meths.length; i++){
            ///test
            String methName = meths[i].getName();
                        ////////
            /** This is to avoid the method by named "get" "set" and "is" getting added in
                attribute list. */
            if(methName.equals("get") || methName.equals("set") || methName.equals("is")){
                continue;
            }
            if((methName.startsWith("get") && meths[i].getParameterTypes().length == 0 ) && !(meths[i].getReturnType().getName().equals("void"))|| (methName.startsWith("is") &&  (meths[i].getReturnType().getName().equals("boolean") || meths[i].getReturnType().getName().equals("java.lang.Boolean")) &&
                                                                                                                                                    meths[i].getParameterTypes().length == 0 )){
                                //System.out.println(" READ METH IS "+meths[i].getName());
                if(methName.startsWith("get")){
                    /*if(isAttributeMethodOverloaded(meths[i])){
                      throw new NotCompliantMBeanException();
                      }*/
                    readMeths.add(meths[i]);
                }
                else{
                    isIsMeths.add(meths[i]);
                    /*if(isAttributeMethodOverloaded(meths[i])){
                      throw new NotCompliantMBeanException();
                      }*/
                }
            }
            if(methName.startsWith("set") && meths[i].getParameterTypes().length ==1 && meths[i].getReturnType().getName().equals("void")){
                writeMeths.add(meths[i]);
                                /*if(isAttributeMethodOverloaded(meths[i])){
                                  throw new NotCompliantMBeanException();
                                  }*/
            }

        }

        /** Comparison is done between get and set attribute to fine read-write access */
        for(int i=0; i<readMeths.size(); i++){
            boolean isMatched = false;
            Method read = (Method)readMeths.get(i);
            /** This remove and add is done to avoid checking the same method */
                        readMeths.remove(i);
            if(isAttributeMethodOverloaded(read)){
                throw new NotCompliantMBeanException();
            }
            readMeths.add(i,read);
            for(int j=0;j<writeMeths.size(); j++){
                Method write = (Method)writeMeths.get(j);
                writeMeths.remove(j);
                if(isAttributeMethodOverloaded(write)){
                    throw new NotCompliantMBeanException();
                }
                writeMeths.add(j,write);
                if(((Method)readMeths.get(i)).getName().substring(3).equals(((Method)writeMeths.get(j)).getName().substring(3))){
                    if(!(read.getReturnType().getName().equals(write.getParameterTypes()[0].getName()))){
                        throw new NotCompliantMBeanException();
                    }
                    readableVec.add(new Boolean(true));
                    writableVec.add(new Boolean(true));
                    writeMeths.remove(j);
                    isMatched = true;
                    break;
                }
            }
            if(!isMatched){
                readableVec.add(new Boolean(true));
                writableVec.add(new Boolean(false));
            }
        }
        /** Comparison is done between is and set attribute to find read-write access */
        for(int i=0;i<isIsMeths.size();i++){
            boolean isMatched = false;
            Method is = (Method)isIsMeths.get(i);
            isIsMeths.remove(i);
            if(isAttributeMethodOverloaded(is)){
                throw new NotCompliantMBeanException();
            }
            isIsMeths.add(i,is);
            for(int j=0;j<writeMeths.size(); j++){
                Method write = (Method)writeMeths.get(j);
                if(is.getName().substring(2).equals(write.getName().substring(3))){
                    if(!(is.getReturnType().getName().equals(write.getParameterTypes()[0].getName()))){
                        throw new NotCompliantMBeanException();
                    }
                    /*readableVec.add(new Boolean(true));
                      writableVec.add(new Boolean(true));
                      writeMeths.remove(j);*/
                    isWriteStatus.add(new Boolean(true));
                    isMatched = true;
                    writeMeths.remove(j);
                    break;
                }
            }
            if(!isMatched){
                //readableVec.add(new Boolean(true));
                //writableVec.add(new Boolean(false));
                isWriteStatus.add(new Boolean(false));
            }
        }

        /** Only the write access attribute */
        for(int i=0; i<writeMeths.size();i++){
            Method meth = (Method)writeMeths.get(i);
            writeMeths.remove(i);
            if(isAttributeMethodOverloaded(meth)){
                throw new NotCompliantMBeanException();
            }
            writeMeths.add(i,meth);
            readableVec.add(new Boolean(false));
            writableVec.add(new Boolean(true));
                                //System.out.println("222222222222222222222 readable is "+readableVec);
            //System.out.println("333333333333333333333 writable is "+writableVec);
        }
        size = readMeths.size()+writeMeths.size();
        types = new String[size];
        names = new String[readMeths.size()+writeMeths.size()+isIsMeths.size()];
        pos = 0;

        /** Find the types of attribute and split the attributes name */
        for(int i=0; i< readMeths.size(); i++){
            names[i] = ((Method)readMeths.get(i)).getName().substring(3);
            //types[i] = convertToJmxArrayType(((Method)readMeths.get(i)).getReturnType().getName());
            types[i] = (((Method)readMeths.get(i)).getReturnType().getName());
        }
        for(int i=readMeths.size(); i<size; i++){
            //types[i] = convertToJmxArrayType(((Method)writeMeths.get(pos)).getParameterTypes()[0].getName());
            types[i] = (((Method)writeMeths.get(pos)).getParameterTypes()[0].getName());
            names[i] = ((Method)writeMeths.get(pos)).getName().substring(3);
            pos++;
        }

        MBeanAttributeInfo[] toRet = new MBeanAttributeInfo[names.length];
        String des = "This attribute is present in a Standard MBean";
        for(int i = 0; i<size;i++)
            {

                if(clazz.getName().equals("javax.management.MBeanServerDelegate"))
                    {
                        if(names[i].equals("MBeanServerId"))
                            des = "Specifies the ID of this JMX MBean Server";
                        else if(names[i].equals("ImplementationName"))
                            des = "Specifies the JMX implementation name (the name of this product).";
                        else if(names[i].equals("ImplementationVendor"))
                            des = "Specifies the JMX implementation vendor (the vendor of this product)";
                        else if(names[i].equals("ImplementationVersion"))
                            des = "Specifies the JMX implementation version (the version of this product). ";
                        else if(names[i].equals("SpecificationName"))
                            des = "Specifies the full name of the JMX specification implemented by this product.";
                        else if(names[i].equals("SpecificationVendor"))
                            des = "Specifies the vendor of the JMX specification implemented by this product.";
                        else if(names[i].equals("SpecificationVersion"))
                            des = "Specifies the version of the JMX specification implemented by this product.";
                    }
                else if(clazz.getName().equals("javax.management.loading.MLet"))
                    {
                        if(names[i].equals("URLs"))
                            des = "specifies the search path of URLs for loading classes and resources.";
                    }

                toRet[i] = new MBeanAttributeInfo(names[i],types[i],des,((Boolean)readableVec.get(i)).booleanValue(),((Boolean)writableVec.get(i)).booleanValue(),false);
            }
        /** isIs artributes are handled here */
        pos =0;
                                //int writePos = size;
        for(int i=size; i<toRet.length; i++){
            Method meth = (Method)isIsMeths.get(pos);
            //toRet[i] = new MBeanAttributeInfo(meth.getName().substring(2),meth.getReturnType().getName(),des,true,((Boolean)writableVec.get(writePos)).booleanValue(),true);
            toRet[i] = new MBeanAttributeInfo(meth.getName().substring(2),meth.getReturnType().getName(),des,true,((Boolean)isWriteStatus.get(pos)).booleanValue(),true);
            pos++;
            //writePos++;
        }

        return toRet;
    }

    private boolean isAttributeMethodOverloaded(Method toCheckMeth){
        String toCheckStr = toCheckMeth.getName();
        String methStr = null;
        if(toCheckStr.startsWith("is")){
            toCheckStr = toCheckStr.substring(2);
        }
        else if(toCheckStr.startsWith("get")){
            toCheckStr = toCheckStr.substring(3);
        }
        if(toCheckMeth.getName().startsWith("is") || toCheckMeth.getName().startsWith("get")){
            for(int i=0; i<readMeths.size(); i++){
                Method meth = (Method)readMeths.get(i);
                methStr = meth.getName();
                if(toCheckStr.equals(methStr.substring(3))){
                    if(!(toCheckMeth.getReturnType().getName().equals(meth.getReturnType().getName()))
                                                || (toCheckMeth.getDeclaringClass().getName().equals(meth.getDeclaringClass().getName()))){
                        //readMeths.remove(i);
                                                return true;
                     }
                    readMeths.remove(i);
                                        //return true;
                }
            }
            for(int i=0; i<isIsMeths.size(); i++){
                Method meth = (Method)isIsMeths.get(i);
                methStr = meth.getName();
                if(toCheckStr.equals(methStr.substring(2))){
                    if(!(toCheckMeth.getReturnType().getName().equals(meth.getReturnType().getName()))
                                        || (toCheckMeth.getDeclaringClass().getName().equals(meth.getDeclaringClass().getName())))
{
                      //isIsMeths.remove(i);
                                          return true;
                    }
                    isIsMeths.remove(i);
                                        //return true;
                }
            }
        }
        else if(toCheckMeth.getName().startsWith("set")){
            toCheckStr = toCheckStr.substring(3);
            for(int i=0; i<writeMeths.size();i++){
                Method meth = (Method)writeMeths.get(i);
                methStr = ((Method)writeMeths.get(i)).getName();
                if(toCheckStr.equals(methStr.substring(3))){
                    if(!(toCheckMeth.getParameterTypes()[0].getName().equals(meth.getParameterTypes()[0].getName()))||(toCheckMeth.getDeclaringClass().getName().equals(meth.getDeclaringClass().getName()))){
                      //writeMeths.remove(i);
                                          return true;
                    }
                    writeMeths.remove(i);
                                        //return true;
                }
            }
        }
        return false;
    }

    public MBeanConstructorInfo[] getConstructors()
    {
        Constructor[] constrs = clazz.getConstructors();
        MBeanConstructorInfo[] toRet = new MBeanConstructorInfo[constrs.length];

        for(int i=0;i<toRet.length;i++)
        {
            Class[] cparams = constrs[i].getParameterTypes();
            MBeanParameterInfo[] params = new MBeanParameterInfo[cparams.length];

            for(int j=0;j<params.length;j++)
            {
                params[j] = new MBeanParameterInfo( ("param" + j ),
                                                    cparams[j].getName(),
                                                    "param to constructor");
            }

            toRet[i] = new MBeanConstructorInfo(constrs[i].getName(),
                                                constrs[i].toString(),
                                                params);
        }

        return toRet;
    }

    public MBeanOperationInfo[] getOperations()
    {
        int length = 0;
        int pos = 0;

        Method[] meths = iclazz.getMethods();

        //MBeanOperationInfo[] toRet = new MBeanOperationInfo[length];
        Vector operInfo = new Vector();
        MBeanOperationInfo mbOperInfo = null;

        for(int i=0;i<meths.length; i++)
        {
            String methName = meths[i].getName();
            String returnType = meths[i].getReturnType().getName();
            Class[] mparams = meths[i].getParameterTypes();
            if(methName.startsWith("get") && !methName.equals("get")&&
               (mparams.length ==0) &&!(returnType.equals("void"))){
                continue;
            }
            if(methName.startsWith("is") && !methName.equals("is")&&
               (mparams.length ==0) &&(returnType.equals("boolean")||                                                                                                                       returnType.equals("java.lang.Boolean"))){
                continue;
            }
            if(methName.startsWith("set") && !methName.equals("set")&&
               (mparams.length == 1) && returnType.equals("void")){
                continue;
            }

            MBeanParameterInfo[] params = new MBeanParameterInfo[mparams.length];
                            //commentSystem.out.println("\n\n** DEFAULT DYNAMIC MBEAN = "+meths[i].getName());
            for(int j=0;j<params.length;j++)
                {
                    //commentSystem.out.println("** PARAMETER = "+mparams[j].getName());
                    //params[j] = new MBeanParameterInfo( ("param" + j ), convertToJmxArrayType(mparams[j].getName()),"param to method");
                    params[j] = new MBeanParameterInfo(("param"+j),mparams[j].getName(),"param to method");
                }
            String des = "Operation exposed for management";
            if(clazz.getName().equals("javax.management.loading.MLet"))
                {
                    if(meths[i].getName().equals("addURL"))
                        des = "Appends the specified URL to the list of URLs to search for classes and resources.";
                    else if(meths[i].getName().equals("getMBeansFromURL"))
                        des = "Loads a text file containing MLET tags that define the MBeans to be added to the agent.";
                }

            mbOperInfo = new MBeanOperationInfo(meths[i].getName(),des,params,meths[i].getReturnType().getName(),MBeanOperationInfo.ACTION);
            operInfo.add(mbOperInfo);
        }

        int size = operInfo.size();
        MBeanOperationInfo[] toRet = new MBeanOperationInfo[size];
        for(int i=0; i<size; i++){
            toRet[i] = (MBeanOperationInfo)operInfo.get(i);
        }
        return toRet;
    }

    public MBeanNotificationInfo[] getNotifications()
    {
        //currently not supported
        return null;
    }

    public MBeanInfo getMBeanInfo()
    {
        return mbeanInfo;
    }

    public Object getAttribute(String attribute) //throws Exception
        throws javax.management.AttributeNotFoundException,
               javax.management.MBeanException,
               javax.management.ReflectionException
    {
        try{

            Class e  = null;
            e = object.getClass();
            Method attrMethod = null;
            Object[] arguments = new Object[0];

            try{
                //String[] attributes = getAttributes();
                MBeanAttributeInfo[] attrInfo = getMBeanInfo().getAttributes();
                boolean isAttributeFound = false;
                for(int i=0; i<attrInfo.length; i++){
                        if(attribute.equals(attrInfo[i].getName())){
                                isAttributeFound = true;
                        }
                }
                if(!isAttributeFound){
                        throw new AttributeNotFoundException();
                }

                attrMethod = e.getMethod("get"+attribute,null);
                /*if(attrMethod == null){
                        throw new AttributeNotFoundException();
                }*/
                return attrMethod.invoke(object,arguments);
            }catch(Exception ee){
                                /** For TCK */
                if(ee instanceof NoSuchMethodException){
                    attrMethod = e.getMethod("is"+attribute,null);
                    return attrMethod.invoke(object,arguments);
                }
                throw ee;
            }
        }catch(Exception e){
                        if(e instanceof AttributeNotFoundException){
                                throw (AttributeNotFoundException)e;
                        }
            if(e instanceof NoSuchMethodException)
                throw new AttributeNotFoundException();

            if(e instanceof InvocationTargetException)
                {
                                /** For TCK */
                    InvocationTargetException ite = (InvocationTargetException)e;
                    Throwable th = ite.getTargetException();
                    if(th instanceof Error){
                        Error er = (Error)th;
                        RuntimeErrorException ree = new RuntimeErrorException(er);
                        throw ree;
                    }
                    Exception e1 = (Exception)((InvocationTargetException)e).getTargetException();
                    throw new ReflectionException(e1);
                }
            throw new ReflectionException(e);
        }
    }

    public AttributeList getAttributes(String[] attributes)
    //throws Exception
    {
        AttributeList toRet = null;
        if(attributes == null)
            return toRet;

        toRet = new AttributeList();
        for(int i = 0; i<attributes.length;i++)
        {
            Attribute attr = null;

            try{
                attr = new Attribute(attributes[i], getAttribute(attributes[i]));
            }catch(Exception e){
                attr = new Attribute(attributes[i], e);
            }
            toRet.add(attr);
        }

        return toRet;
    }

    public Object invoke(String actionName, Object[] params, String[] signature)
        throws javax.management.MBeanException,
               javax.management.ReflectionException
    {
        Class e  = null;
        try
		{
            e = object.getClass();

            MBeanOperationInfo[] opers = mbeanInfo.getOperations();

            MBeanOperationInfo oper = null;
            for(int i=0;i<opers.length;i++)
            {
                if(opers[i].getName().equals(actionName))
                {
                    MBeanParameterInfo[] pars = opers[i].getSignature();
                    if(pars == null)
                        break;

                    if(params != null)
                    {
                         if( !(pars.length == params.length) )
						 {
                            if(i != (opers.length-1)){
								continue;
							}
							else{
								throw new RuntimeOperationsException(new IllegalArgumentException());
							}
						}
					}

					if(params != null && signature != null)
					{
						if(params.length != signature.length)
						{
							throw new ReflectionException(new IllegalArgumentException());
						}
					}

                    int j=0;
                    for(;j<pars.length;j++)
                        {
                            if(params[j] != null){
                                String jmxParam = convertToJmxArrayType(signature[j]);

                                if(!pars[j].getType().equals(jmxParam))
                                    {
                                        String temp = convertToWrapperType(pars[j].getType());

                                        if((temp == null) || (!(temp.equals(jmxParam))))
                                            continue;
                                    }
                            }



                        }
                    if(j < pars.length)
                        continue;

                    oper = opers[i];
                    break;
                }
            }

            if(oper == null){
                /* Provision for handling Attribute */
                /*if(actionName.startsWith("get") || actionName.startsWith("set")){
                  actionName = actionName.substring(3);
                  }
                  else if(actionName.startsWith("is")){
                  actionName = actionName.substring(2);
                  }
                  Object attribute = getAttribute(actionName);
                  if(attribute == null){
                  throw new MBeanException(new Exception());
                  }*/
                /** This is to execute Attributes .Setter will have only one parameters*/
                Class[]  paramClass = null;
                if(params != null){
                    if(params.length == 0 || params.length == 1){
                        paramClass = new Class[params.length];
                        for(int i=0; i<params.length;i++){
                            paramClass[i] = params[i].getClass();
                        }
                    }
                    else{
                        //throw new MBeanException(new Exception());
                        throw new RuntimeOperationsException(new IllegalArgumentException());
                    }
                }
                Method attrMethod = null;
                try{
                    attrMethod = e.getMethod(actionName,paramClass);
                }catch(NoSuchMethodException ne){
                    if(paramClass.length == 1){
                        Method[] meths = e.getMethods();
                        for(int i=0;i< meths.length; i++){
                            if(meths[i].getName().equals(actionName)){
                                Class[] parameters = meths[i].getParameterTypes();
                                if(parameters[0].isAssignableFrom(paramClass[0])){
                                    attrMethod = meths[i];
                                    return attrMethod.invoke(object,params);
                                }
                            }
                        }
                        if(attrMethod == null){
                            paramClass[0] = getPrimitiveClass(paramClass[0].getName());
                            attrMethod = e.getMethod(actionName,paramClass);
                        }
                    }
                    else{
                        throw new MBeanException(new Exception());
                    }
                }
                if(attrMethod == null){
                    throw new MBeanException(new Exception());
                }
                return attrMethod.invoke(object,params);
                //throw new MBeanException(new Exception());
            }
            MBeanParameterInfo[] pars = oper.getSignature();
            Class parameterTypes[]=null;
            if(params != null){
                parameterTypes = new Class[params.length];
                for(int i=0; i<params.length; i++)
                {
                    Class clazz = getProperClass(pars[i].getType());
                    if(clazz == null)
                        clazz = Thread.currentThread().getContextClassLoader().loadClass(signature[i]);

                    parameterTypes[i] = clazz;
                }
            }

            Method attrMethod = e.getMethod(actionName,parameterTypes);
            return attrMethod.invoke(object,params);
        }
		catch(ReflectionException re)
		{
        	throw re;
        }
		catch(Exception ex)
		{
            if(ex instanceof InvocationTargetException)
                {
                    /** For TCK */
                    Exception ee = null;
                    InvocationTargetException ite = (InvocationTargetException)ex;
                    Throwable th = ite.getTargetException();
                    if(th instanceof Error){
                        Error er = (Error)th;
                        RuntimeErrorException ree = new RuntimeErrorException(er);
                        throw ree;
                    }
                    else{
                        //ee = (Exception)th;
                        if(th instanceof RuntimeException){
                                                        throw new RuntimeMBeanException((RuntimeException)th);
                        }
                        if(th instanceof ReflectionException){
                                                        throw new MBeanException((ReflectionException)th);
                                                }
                                        }
                            throw new MBeanException((Exception)th);
                }
            if(ex instanceof RuntimeOperationsException){
                                throw (RuntimeOperationsException)ex;
            }
            if(ex instanceof RuntimeException){
                throw new RuntimeMBeanException((RuntimeException)ex);
            }

            if(ex instanceof ClassNotFoundException)
                {

                    try{
                        Class[] classParams = new Class[params.length];
                        for(int i=0; i<params.length; i++){
                            if(isPrimitiveDataType(signature[i]))
                                classParams[i] = getProperClass(signature[i]);
                            else
                                classParams[i] = params[i].getClass();
                        }
                        //try{
                        Method meth = e.getMethod(actionName,classParams);
                        return meth.invoke(object,params);
                        /*}catch(NoSuchMethodException cnfe){
                          Class[] cParams = new Class[params.length];
                          for(int i=0; i<params.length; i++){
                          try{
                          cParams[i] = DefaultLoaderRepositoryExt.loadClass(signature[i]);
                          }catch(Exception exc){
                          //when classnotfound in default loader repository, there is a chance it to be
                          //system class like java.*
                          if(isPrimitiveDataType(signature[i]))
                          cParams[i] = getProperClass(signature[i]);
                          else
                          cParams[i] = Thread.currentThread().getContextClassLoader().loadClass(signature[i]);
                          }
                          }
                          Method meth = e.getMethod(actionName,cParams);
                          return meth.invoke(object,params);
                          }*/
                    }catch(NoSuchMethodException ne){
						try
						{
							Class[] cParams = new Class[params.length];
							  for(int i=0; i<params.length; i++){
							  try{
							  cParams[i] = DefaultLoaderRepositoryExt.loadClass(signature[i]);
							  }catch(Exception exc){
							  //when classnotfound in default loader repository, there is a chance it to be
							  //system class like java.*
							  if(isPrimitiveDataType(signature[i]))
							  cParams[i] = getProperClass(signature[i]);
							  else
							  cParams[i] = Thread.currentThread().getContextClassLoader().loadClass(signature[i]);
							  }
							  }
							  Method meth = e.getMethod(actionName,cParams);
							  return meth.invoke(object,params);
						}catch(NoSuchMethodException nee)
						{
                        throw new ReflectionException(ne);
						}catch(Exception eee)
						{
							throw new MBeanException(eee);
						}
                    }catch(Exception ene){
						if(ene instanceof MBeanException)
						{
							throw (MBeanException)ene;
						}

                        throw new MBeanException(ene);
                    }
                }
            throw new MBeanException(ex);
            //throw new ReflectionException(e);
        }
    }

    private boolean isPrimitiveDataType(String type)
    {
        if(type.endsWith("int"))
            return true;
        else if(type.endsWith("long"))
            return true;
        else if(type.endsWith("byte"))
            return true;
        else if(type.endsWith("float"))
            return true;
        else if(type.endsWith("char"))
            return true;
        else if(type.endsWith("short"))
            return true;
        else if(type.endsWith("double"))
            return true;
        else if(type.endsWith("boolean"))
            return true;

        return false;

    }

    private boolean isPrimitiveArrayDataType(String type)
    {
        if(type.endsWith("int[]"))
            return true;
        else if(type.endsWith("long[]"))
            return true;
        else if(type.endsWith("byte[]"))
            return true;
        else if(type.endsWith("float[]"))
            return true;
        else if(type.endsWith("char[]"))
            return true;
        else if(type.endsWith("short[]"))
            return true;
        else if(type.endsWith("double[]"))
            return true;
        else if(type.endsWith("boolean[]"))
            return true;

        return false;

    }

    private String convertToWrapperType(String type)
    {
        if(type.endsWith("int"))
            return "java.lang.Integer";
        else if(type.endsWith("int[]"))
            return "[Ljava.lang.Integer;";
        else if(type.endsWith("long"))
            return "java.lang.Long";
        else if(type.endsWith("long[]"))
            return "[Ljava.lang.Long;";
        else if(type.endsWith("byte"))
            return "java.lang.Byte";
        else if(type.endsWith("byte[]"))
            return "[Ljava.lang.Byte;";
        else if(type.endsWith("float"))
            return "java.lang.Float";
        else if(type.endsWith("float[]"))
            return "[Ljava.lang.Float;";
        else if(type.endsWith("char"))
            return "java.lang.Character";
        else if(type.endsWith("char[]"))
            return "[Ljava.lang.Character;";
        else if(type.endsWith("short"))
            return "java.lang.Short";
        else if(type.endsWith("short[]"))
            return "[Ljava.lang.Short;";
        else if(type.endsWith("double"))
            return "java.lang.Double";
        else if(type.endsWith("double[]"))
            return "[Ljava.lang.Double;";
        else if(type.endsWith("boolean"))
            return "java.lang.Boolean";
        else if(type.endsWith("boolean[]"))
            return "[Ljava.lang.Boolean;";

        return null;

    }

    private Class getProperClass(String type)
    {
        if(type.endsWith("int[]"))
            return (new int[0]).getClass();
        else if(type.endsWith("long[]"))
            return (new long[0]).getClass();
        else if(type.endsWith("byte[]"))
            return (new byte[0]).getClass();
        else if(type.endsWith("float[]"))
            return (new float[0]).getClass();
        else if(type.endsWith("char[]"))
            return (new char[0]).getClass();
        else if(type.endsWith("short[]"))
            return (new short[0]).getClass();
        else if(type.endsWith("double[]"))
            return (new double[0]).getClass();
        else if(type.endsWith("boolean[]"))
            return (new boolean[0]).getClass();
        else if(type.endsWith("String[]")){
            return (new String[0]).getClass();
        }

        if(type.endsWith("int"))
            return int.class;
        else if(type.endsWith("long"))
            return long.class;
        else if(type.endsWith("byte"))
            return byte.class;
        else if(type.endsWith("float"))
            return float.class;
        else if(type.endsWith("char"))
            return char.class;
        else if(type.endsWith("short"))
            return short.class;
        else if(type.endsWith("double"))
            return double.class;
        else if(type.endsWith("boolean"))
            return boolean.class;

        return null;
    }

    private Class getPrimitiveClass(String className){
        if(className.equals("java.lang.Integer")){
            return Integer.TYPE;
        }
        else if(className.equals("java.lang.Long")){
            return Long.TYPE;
        }
        else if(className.equals("java.lang.Short")){
            return Short.TYPE;
        }
        else if(className.equals("java.lang.Float")){
            return Float.TYPE;
        }
        else if(className.equals("java.lang.Double")){
            return Double.TYPE;
        }
        else if(className.equals("java.lang.Character")){
            return Character.TYPE;
        }
        else if(className.equals("java.lang.Boolean")){
            return Boolean.TYPE;
        }

        return null;
    }

    private String convertToJmxArrayType(String type)
    {
        if(type.equals("[I") )
            return "int[]";
        else if(type.equals("[J"))
            return "long[]";
        else if(type.equals("[B"))
            return "byte[]";
        else if(type.equals("[F"))
            return "float[]";
        else if(type.equals("[C"))
            return "char[]";
        else if(type.equals("[S"))
            return "short[]";
        else if(type.equals("[D"))
            return "double[]";
        else if(type.equals("[Z"))
            return "boolean[]";

        if(type.startsWith("[L") &&  type.endsWith(";"))
            return (type.substring(2, type.length() -1) + "[]");

        return type;
    }

    public void setAttribute(Attribute attribute)
    //throws Exception
        throws javax.management.AttributeNotFoundException,
               javax.management.InvalidAttributeValueException,
               javax.management.MBeanException,
               javax.management.ReflectionException
	{

        if(attribute == null)
            throw new RuntimeOperationsException(new IllegalArgumentException("Null values not possible"));

        try
		{
            Class e  = null;
            try
			{
                e = Thread.currentThread().getContextClassLoader().loadClass(mbeanInfo.getClassName());
            }
			catch(ClassNotFoundException cce)
			{
                MBeanServerImpl server = null;
                ArrayList list = MBeanServerFactory.findMBeanServer(null);
                if(list != null)
                    server = (MBeanServerImpl)list.toArray()[0];
                else throw cce;

                Object loader = server.getLoaderTable().get(mbeanInfo.getClassName());
                if(loader == null)
                    throw cce;

                e = Class.forName(mbeanInfo.getClassName(), true, (ClassLoader)loader);
            }

            MBeanAttributeInfo[] attrs = mbeanInfo.getAttributes();
            MBeanAttributeInfo attr = null;
            for(int i=0;i<attrs.length;i++)
            {

                if(attrs[i].getName().equals(attribute.getName()))
                    {
                        attr = attrs[i];
                        break;
                    }
            }

            if(attr == null)
                throw new AttributeNotFoundException(attribute.getName());

            Class parameterTypes[]=new Class[1];

            Class clazz = getProperClass(attr.getType());
            if(clazz == null){
                clazz = attribute.getValue().getClass();
            }
            parameterTypes[0] = clazz;

            Method attrMethod = null;
            ArrayList methodList = new ArrayList();
            ArrayList paramList = new ArrayList();
            try
			{
                Method[] meths = e.getMethods();
                Class[] params = null;
                parameterTypes[0] = null;
                for(int i=0; i<meths.length; i++)
				{
                    Method meth = meths[i];
                    if(meth.getName().equals("set"+attribute.getName()))
					{
                        params = meth.getParameterTypes();
                        //Attribute will have only one param
                        if(params != null)
						{
                            if(params[0].isAssignableFrom(clazz)){
                                parameterTypes[0] = params[0];
                                methodList.add(meth);
                                paramList.add(parameterTypes[0]);
                            }
                        }
                    }

                    if(params != null && params.length >0 && params[0] != null)
					{
                        if(parameterTypes[0] == null)
						{
							throw new InvalidAttributeValueException();
                        }
                    }
                }
                int size = methodList.size();
                if(size == 0){
                        throw new NoSuchMethodException();
                }
                else if(size == 1){
                        attrMethod = (Method)methodList.get(0);
                }
                else{
                    MBeanInfo info = getMBeanInfo();
                    //String className = info.getClassName()+"MBean";
                    //Class mbeanClass = Class.forName(className);
                    Method[] mbeanMeths =iclazz.getMethods();
                    int mbeanMethsSize = mbeanMeths.length;
                    boolean isFound = false;
                    for(int i=0;i<size;i++){
                        Method meth = (Method)methodList.get(i);
                        for(int j=0; j<mbeanMethsSize; j++){
                            if(meth.getName().equals(mbeanMeths[j].getName())){
                                if(Arrays.equals(meth.getExceptionTypes(),mbeanMeths[j].getExceptionTypes())){                                                                          attrMethod = meth;
                                  isFound = true;
                                  break;
                                }
                            }
                        }
                        if(isFound){
                                break;
                        }
                    }
                }
                if(attrMethod == null){
                        throw new NoSuchMethodException();
                }
            }
            catch(Exception ee){
                                if(ee instanceof InvalidAttributeValueException){
                                        throw (InvalidAttributeValueException)ee;
                                }
                                if(ee instanceof NoSuchMethodException){
                     throw new AttributeNotFoundException();
                }
                //ee.printStackTrace();
                throw new MBeanException(ee);
           }

            try
			{
                attrMethod.invoke(object, new Object[]{attribute.getValue()} );
            }catch(Exception e2){
                                if(e2 instanceof AttributeNotFoundException){
                    throw (AttributeNotFoundException)e2;
                }
                if(e2 instanceof InvocationTargetException)
                {
                    /** For TCK */
                    InvocationTargetException ite = (InvocationTargetException)e2;
                    Throwable th = ite.getTargetException();
                    if(th instanceof Error){
                        Error er = (Error)th;
                        RuntimeErrorException ree = new RuntimeErrorException(er);
                        throw ree;
                    }
                    else{
                            if(th instanceof ReflectionException){
                                    throw (ReflectionException)th;
                            }

                            if(th instanceof RuntimeException){
                                    throw new RuntimeMBeanException((RuntimeException)th);
                            }
                            else{
                                    throw new MBeanException((Exception)th);
                            }
                    }
                }
            }
        }
        catch(ReflectionException e){
            throw e;
        }
        catch(Exception e){
                        if(e instanceof InvalidAttributeValueException){
                                        throw (InvalidAttributeValueException)e;
                        }
            if(e instanceof AttributeNotFoundException){
                throw (AttributeNotFoundException)e;
            }

			if(e instanceof InvocationTargetException)
            {
                            /** For TCK */
                InvocationTargetException ite = (InvocationTargetException)e;
                Throwable th = ite.getTargetException();
                if(th instanceof Error){
                    Error er = (Error)th;
                    RuntimeErrorException ree = new RuntimeErrorException(er);
                    throw ree;
                }
                                    else{
                                                    if(th instanceof ReflectionException){
                                                            throw (ReflectionException)th;
                                                    }

                                                    if(th instanceof RuntimeException){
                                                            throw new RuntimeMBeanException((RuntimeException)th);
                                                    }
                                                    else{
                                                            throw new MBeanException((Exception)th);
                                                    }
                                    }
            }

            if(e instanceof RuntimeMBeanException){
                    throw (RuntimeMBeanException)e;
            }
            if(e instanceof MBeanException){
                    throw (MBeanException)e;
            }
        }
    }

    public AttributeList setAttributes(AttributeList attributes)
    {
        if(attributes == null)
            throw new RuntimeOperationsException(new IllegalArgumentException("Null values not possible"));

        Object[] array = attributes.toArray();

        if(array == null)
            return attributes;

        for(int i = 0; i<array.length;i++)
        {
            Attribute attr = null;
            try
			{
                attr = (Attribute)array[i];
            }
			catch(ClassCastException ce)
			{
                continue;
            }

            try
			{
                setAttribute(attr);
            }
			catch(Exception e)
			{
                int index = attributes.indexOf(attr);
                attributes.remove(index);
                attr = new Attribute(attr.getName(), e);
                attributes.add(index, attr);
            }
        }

        return attributes;
    }

    //Implementing NotificationBroadcaster
    public void addNotificationListener(NotificationListener listener,
                                        NotificationFilter filter,
                                        Object handback)
        						throws IllegalArgumentException
    {
        if( !(object instanceof NotificationBroadcaster))
        {
            throw new RuntimeOperationsException(new IllegalArgumentException("The MBean does not implement NotificationBroadCaster"));
        }

        ((NotificationBroadcaster)object).addNotificationListener(listener,
															filter, handback);
    }

    public void removeNotificationListener(NotificationListener listener)
        								throws ListenerNotFoundException
    {
        if( !(object instanceof NotificationBroadcaster))
        {
            throw new RuntimeOperationsException(new IllegalArgumentException("The MBean does not implement NotificationBroadCaster"));
        }

       ((NotificationBroadcaster)object).removeNotificationListener(listener);
    }

    public MBeanNotificationInfo[] getNotificationInfo()
    {
        if( !(object instanceof NotificationBroadcaster))
            return null;

        return ((NotificationBroadcaster)object).getNotificationInfo();
    }

    //Implementing for NotificationFilter
    public boolean isNotificationEnabled(Notification notification)
    {
        if( !(object instanceof NotificationFilter))
            return false;

        return ((NotificationFilter)object).isNotificationEnabled(notification);
    }

    //IMplementing for NotificationListener
    public void handleNotification(Notification notification, Object handback)
    {
        if( !(object instanceof NotificationListener))
            return;

        ((NotificationListener)object).handleNotification(notification, handback);
    }

    //IMplementing for MBeanRegistration
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception
    {
        if( !(object instanceof MBeanRegistration))
            return null;

        return ((MBeanRegistration)object).preRegister(server, name);
    }

    public void postRegister(java.lang.Boolean registrationDone)
    {
        if( !(object instanceof MBeanRegistration))
            return;

        ((MBeanRegistration)object).postRegister(registrationDone);
    }

    public void preDeregister() throws Exception
    {
        if( !(object instanceof MBeanRegistration))
            return;
        try
		{
            ((MBeanRegistration)object).preDeregister();
        }
        catch(Exception e)
		{
            throw e;
        }
    }

    public void postDeregister()
    {
        if( !(object instanceof MBeanRegistration))
            return;

        ((MBeanRegistration)object).postDeregister();
    }

	private boolean getFlagValue(Class mainClass,boolean value)
	{
		clazz = mainClass.getSuperclass();

		Class [] interfaces = clazz.getInterfaces();
		for(int i = 0;i<interfaces.length;i++)
		{
			if(interfaces[i].getName().equals(clazz.getName() + "MBean") )
			{
				iclazz = interfaces[i];
				value = true;
				break;
			}

		}

		return value;
	}
}