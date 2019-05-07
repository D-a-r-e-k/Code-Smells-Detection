/*   Copyright (C) 2003 Finalist IT Group
 *
 *   This file is part of JAG - the Java J2EE Application Generator
 *
 *   JAG is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *   JAG is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   You should have received a copy of the GNU General Public License
 *   along with JAG; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.finalist.jag.uml;

import com.finalist.jaggenerator.modules.*;
import com.finalist.jaggenerator.ConsoleLogger;
import com.finalist.jaggenerator.JagGenerator;
import com.finalist.uml14.simpleuml.*;
import org.w3c.dom.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * Generate an UML1.4 XMI model based on the Jag skelet configuration file.
 *
 * @author Rudie Ekkelenkamp - Finalist IT Group
 * @version $Revision: 1.23 $, $Date: 2006/03/11 13:44:04 $
 */
public class Jag2UMLGenerator {

   static Log log = LogFactory.getLog(Jag2UMLGenerator.class);

   public final static String stringType = "java.lang.String";
   public final static String intType = "java.lang.Integer";
   public final static String sqlDateType = "java.sql.Date";
   public final static String sqlTimestampType = "java.sql.Timestamp";
   public final static String sqlUtilType = "java.util.Date";
   public final static String byteType = "java.lang.Byte";
   public final static String floatType = "java.lang.Float";
   public final static String doubleType = "java.lang.Double";
   public final static String longType = "java.lang.Long";
   public final static String shortType = "java.lang.Short";
   public final static String bigDecimalType = "java.math.BigDecimal";
   public final static String defaultType = "DEFAULT";

   public final static String stringValue = "String";
   public final static String intValue = "Integer";
   public final static String sqlDateValue = "Date";
   public final static String sqlTimestampValue = "Timestamp";
   public final static String sqlUtilValue = "Date";
   public final static String byteValue = "Byte";
   public final static String floatValue = "Float";
   public final static String doubleValue = "Double";
   public final static String longValue = "Long";
   public final static String shortValue = "Short";
   public final static String bigDecimalValue = "BigDecimal";

   public final static String defaultValue = "String";

   private HashMap typeMappings = null;
   private SimpleUmlPackage javaLangPackage = null;
   private SimpleUmlPackage javaUtilPackage = null;
   private SimpleUmlPackage javaSqlPackage = null;
   private SimpleUmlPackage javaMathPackage = null;
   private static ConsoleLogger logger;

   /**
    * Makes a Jag2UMLGenerator with an external logger.
    *
    * @param logger somewhere to redirect output, other than System.out
    */
   public Jag2UMLGenerator(ConsoleLogger logger) {
      this.logger = logger;
   }

   /**
    * Makes a Jag2UMLGenerator.
    */
   public Jag2UMLGenerator() {
   }


   /**
    * Converts a JAG application file (XML) to XMI.
    *
    * @param skeletFileName the JAG application file to be converted to XMI.
    */
   public void generateXMI(String skeletFileName) {
      generateXMI(skeletFileName, null);
   }

   /**
    * Converts a JAG application file (XML) to XMI.
    *
    * @param skeletFileName the JAG application file to be converted to XMI.
    * @param output the file where the XMI will be written to.
    */
   public void generateXMI(String skeletFileName, File output) {
      Root root = null;
      File skeletFile = null;
      try {
         skeletFile = new File(skeletFileName);
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = null;
         Document doc = null;
         builder = dbf.newDocumentBuilder();
         doc = builder.parse(skeletFile);
         root = new Root(doc);
         
      } catch (Exception e) {
         e.printStackTrace();
      }

      if (output == null) {
         output = skeletFile.getParentFile();
      }

      generateXMI(root, output);
   }

   private void generateXMI(Root root, File output) {
      App app = root.app;
      Config conf = root.config;
       // app.setRootPackage();
      ArrayList sessionEJBs = root.getSessionEjbs();

      String rootPackage = app.rootPackageText.getText();
      String projectName = app.descriptionText.getText();

      SimpleModel simpleModel = new SimpleModel();
      simpleModel.setName(projectName);
      //SimpleDiagram diagram = new SimpleDiagram(projectName);
      //simpleModel.addSimpleDiagram(diagram);

      // First create the type packages.
      createBasicTypes(simpleModel);
      simpleModel.addSimpleUmlPackage(rootPackage);
      createDataSource(root.datasource, simpleModel);
      createConfigClass(root.config,root.app,root.paths,simpleModel);

      // Create the entity EJBs.
      HashMap entityEJBMap = createEntityEJBs(root.getEntityEjbs(), simpleModel);

      // Create the Session EJBs
      HashMap sessionEJBMap = createSessionEJBs(root.getSessionEjbs(), simpleModel);

      // Create the dependencies for the session EJBs to the Entity EJBS.
      // Session EJBs
      for (int i = 0; i < sessionEJBs.size(); i++) {
         Session s = (Session) sessionEJBs.get(i);
         String refName = s.getRefName();

         // Session UML class is: sessionEJBMap
         SimpleUmlClass sessionUMLClass = (SimpleUmlClass) sessionEJBMap.get(refName);

         ArrayList entityRefs = s.getEntityRefs();
         for (int j = 0; j < entityRefs.size(); j++) {
            String entityRefName = (String) entityRefs.get(j);
            SimpleUmlClass entityUMLClass = (SimpleUmlClass) entityEJBMap.get(entityRefName);
            if (entityUMLClass != null) {
               // Only add a reference if the entity could be found.
               SimpleDependency dep = new SimpleDependency();
               dep.setClient(sessionUMLClass);
               dep.setSupplier(entityUMLClass);
                // Stereotype not required.
               // simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_DEPENDENCTY_ENTITY_REF, dep);
               simpleModel.addSimpleDependency(dep);
            }
            // Now create a dependendency between sessionUMLClass and entityUMLClass.
         }
      }
      createContainerManagedRelations(root.getEntityEjbs(), entityEJBMap, simpleModel);

      try {
         if (output.isDirectory()) {
            output = new File(output, simpleModel.getName() + ".xmi");
         }
         OutputStream outputStream = new FileOutputStream(output);
         simpleModel.writeModel(outputStream);
      } catch (IOException ioe) {
         log("Error writing the file.");
      }
   }

   /**
    * Create all entity EJBs in the uml model and put them in a hashmap
    *
    * @param entityEJBs list with all entity EJBs.
    * @param simpleModel the uml model.
    * @return HashMap with all UML Entity classes.
    */
   private HashMap createEntityEJBs(ArrayList entityEJBs, SimpleModel simpleModel) {
      HashMap map = new HashMap();
      for (int i = 0; i < entityEJBs.size(); i++) {
         Entity e = (Entity) entityEJBs.get(i);
         String documentation = e.getDescription().toString();
         String name = e.getName().toString();
         String refName = e.getRefName();
         String tableName = e.getTableName();
         String displayName = e.getDisplayName().toString();
         String entityRootPackage = e.getRootPackage().toString();

         simpleModel.addSimpleUmlPackage(entityRootPackage);
         String isCompositeKey = e.getIsComposite();
         String isAssocation = e.getIsAssociationEntity();
         // "true" or "false"
         // Use the refName to put all EntityEJBs in a HashMap.
         // Add the standard definition for the URLS.
         SimpleUmlClass umlClass = new SimpleUmlClass(name, SimpleModelElement.PUBLIC);
         // The e should be a UML Class.
         // Use the stereoType:
         simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_CLASS_ENTITY, umlClass);
         simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, documentation, umlClass);
         simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_TABLE_NAME, tableName, umlClass);
         if (!"".equals(displayName)) {
            simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DISPLAY_NAME, displayName, umlClass);
         }
         if ("true".equals(isCompositeKey)) {
            simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_COMPOSITE_PRIMARY_KEY, e.getPrimaryKeyType().toString(), umlClass);
         }
         if ("true".equals(isAssocation)) {
            simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_IS_ASSOCIATION, isAssocation, umlClass);
         }

         ArrayList fields = (ArrayList) e.getFields();
         for (int j = 0; j < fields.size(); j++) {
            Field field = (Field) fields.get(j);
            String fieldType = field.getType();
            String fieldName = field.getName().toString();
            SimpleUmlClass type = (SimpleUmlClass) typeMappings.get(fieldType);
            if (type == null) {
               log("Unknown type: " + type + " for field " + fieldType);
               type = (SimpleUmlClass) typeMappings.get(this.stringType);
            }

            SimpleAttribute theAttribute = new SimpleAttribute(fieldName, SimpleModelElement.PUBLIC, type);
            umlClass.addSimpleAttribute(theAttribute);

            String foreignKey = field.getForeignKey().toString();
            // "true" or "false" if the current field as a foreign key.

            if (field.isPrimaryKey()) {
               simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_ATTRIBUTE_PRIMARY_KEY, theAttribute);
            } else if (field.isNullable() == false) {

               simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_ATTRIBUTE_REQUIRED, theAttribute);
            }

            if (field.isForeignKey()) {
               // Niet duidelijk of het plaatsen van 2 stereotypes mogelijk is....
               String stereoTypeForeignKey = JagUMLProfile.STEREOTYPE_ATTRIBUTE_FOREIGN_KEY;
            }

            String jdbcType = field.getJdbcType().toString();
            simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_JDBC_TYPE, jdbcType, theAttribute);

            String sqlType = field.getSqlType().toString();
            simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_SQL_TYPE, sqlType, theAttribute);

            String columnName = field.getColumnName();
            simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_COLUMN_NAME, columnName, theAttribute);

             boolean autoGeneratedPrimarykey = field.getHasAutoGenPrimaryKey();
             simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_AUTO_PRIMARY_KEY,
                     "" + autoGeneratedPrimarykey, theAttribute);
         }

         SimpleUmlPackage pk = simpleModel.addSimpleUmlPackage(entityRootPackage);
         pk.addSimpleClassifier(umlClass);
         map.put(refName, umlClass);
      }

      return map;
   }

   /** Create the session EJBs on the simpleModel. */
   private HashMap createSessionEJBs(ArrayList sessionEJBs, SimpleModel simpleModel) {
      HashMap map = new HashMap();
      for (int i = 0; i < sessionEJBs.size(); i++) {
         Session s = (Session) sessionEJBs.get(i);



         String sessionPackage = s.getRootPackage().toString();
         String documentation = s.getDescription().toString();
         String name = s.getName().toString();
         String refName = s.getRefName();

         simpleModel.addSimpleUmlPackage(sessionPackage);
         SimpleUmlPackage pk = simpleModel.addSimpleUmlPackage(sessionPackage);
         // Here the actual UML session should be stored.
         String sessionStereoType = JagUMLProfile.STEREOTYPE_CLASS_SERVICE;

         SimpleUmlClass sessionClass = new SimpleUmlClass(name, SimpleModelElement.PUBLIC);
         simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_CLASS_SERVICE, sessionClass);
         simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, documentation, sessionClass);
         pk.addSimpleClassifier(sessionClass);

         ArrayList methods = s.getBusinessMethods();
         if (methods != null) {
            for (int j=0; j< methods.size(); j++) {
               BusinessMethod bm = (BusinessMethod) methods.get(j);
               SimpleOperation op = new SimpleOperation();
               op.setName(bm.getMethodName());
               String desc = bm.getDescription();
               if (desc == null) {
                  desc = "";
               }
               simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, desc, op);

               // Now add the arguments.
               ArrayList arguments = bm.getArgumentList();
               if (arguments != null) {
                  for (int k=0; k < arguments.size(); k++) {
                     BusinessArgument arg = (BusinessArgument) arguments.get(k);
                     SimpleParameter sp = new SimpleParameter();
                     sp.setName(arg.getName());
                     SimpleUmlClass sc = new SimpleUmlClass(arg.getType(), SimpleModelElement.PUBLIC);
                     simpleModel.addSimpleClassifier(sc);                     
                     sp.setKind(SimpleParameter.IN);
                     sp.setType(sc);
                     op.addSimpleParameter(sp);

                  }
               }
               // And finally add the return type:
               SimpleParameter sp = new SimpleParameter();
               sp.setName("");
               SimpleUmlClass sc = new SimpleUmlClass(bm.getReturnType(), SimpleModelElement.PUBLIC);
               simpleModel.addSimpleClassifier(sc);
               sp.setKind(SimpleParameter.RETURN);
               sp.setType(sc);
               op.addSimpleParameter(sp);
               sessionClass.addSimpleOperation(op);
            }
         }

         map.put(refName, sessionClass);
      }
      return map;
   }

   /**
    * Determine the relations that are defined for an entity EJB.
    *
    * @param entityEJBs from the jag configuration file.
    * @param entityEJBMap Current classes in the model.
    * @param simpleModel The UML model.
    */
   private void createContainerManagedRelations(ArrayList entityEJBs, HashMap entityEJBMap, SimpleModel simpleModel) {
      for (int i = 0; i < entityEJBs.size(); i++) {
         Entity e = (Entity) entityEJBs.get(i);

         List relationFieldNames = e.getRelations();

         for (int j = 0; j < relationFieldNames.size(); j++) {
            Relation rel = (Relation) relationFieldNames.get(j);
            String source = e.getRefName();
            String destination = rel.getRelatedEntity().getRefName();
            boolean navigable = rel.isBidirectional();

            int targetCardinality = -1;
            if (rel.isTargetMultiple()) {
               // -1 means many.
               targetCardinality = -1;
            } else {
               targetCardinality = 1;
            }

            SimpleUmlClass sourceClass = (SimpleUmlClass) entityEJBMap.get(source);
            SimpleUmlClass destinationClass = (SimpleUmlClass) entityEJBMap.get(destination);
            if (sourceClass != null && destinationClass != null) {
               SimpleAssociationEnd sourceEnd = new SimpleAssociationEnd(sourceClass.getName(), sourceClass, 0, 1, navigable);
               SimpleAssociationEnd destinationEnd =
                     new SimpleAssociationEnd(destinationClass.getName(), destinationClass, 0, targetCardinality, true);
               SimpleAssociation assoc = new SimpleAssociation(rel.getFieldName().toString(), sourceEnd, destinationEnd);
               simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_ASSOCIATION_FOREIGN_FIELD,
                     rel.getForeignPkFieldName().toString(), assoc);
               simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_ASSOCIATION_MULTIPLICITY,
                                    rel.isTargetMultiple() ? JagUMLProfile.TAGGED_VALUE_ASSOCIATION_MULTIPLICITY_MANY_TO_ONE: JagUMLProfile.TAGGED_VALUE_ASSOCIATION_MULTIPLICITY_ONE_TO_ONE, assoc);
               simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_ASSOCIATION_BIDIRECTIONAL,
                                    rel.isBidirectional() ? "true":"false", assoc);
               simpleModel.addSimpleAssociation(assoc);
               // Create a CMR between these Entity EJBs.
            }

         }
      }

   }

    /** Create a class with all configuration settings for JAG. */
    private void createConfigClass(Config config, App app, Paths paths, SimpleModel simpleModel) {
        String rootPackage = app.rootPackageText.getText();
        String projectName = app.descriptionText.getText();
        String logging = app.getLogFramework();
        SimpleUmlClass dsClass = new SimpleUmlClass("Config" + projectName, SimpleModelElement.PUBLIC);
        simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_CLASS_JAG_CONFIG, dsClass);

        // Config part.
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_AUTHOR, config.getAuthorText(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_VERSION, config.getVersionText(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_COMPANY, config.getCompanyText(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TEMPLATE,
                config.getTemplate().getTemplateDir().toString(), dsClass);
        if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_APPLICATION_SERVER) != null) {
            simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_APPSERVER,
                (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_APPLICATION_SERVER), dsClass);
        }
        if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_RELATIONS) != null) {
            simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_RELATIONS,
                (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_RELATIONS), dsClass);
        }
       if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_MOCK) != null) {
           simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_MOCK,
               (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_MOCK), dsClass);
       }
        if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_JAVA5) != null) {
            simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_JAVA5,
                (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_USE_JAVA5), dsClass);
        }
        if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_WEB_TIER) != null) {
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_WEB_TIER,
                (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_WEB_TIER), dsClass);
        }

        if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_BUSINESS_TIER) != null) {
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_BUSINESS_TIER,
                (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_BUSINESS_TIER), dsClass);
        }
       if (config.getTemplateSettings().get(JagGenerator.TEMPLATE_SERVICE_TIER) != null) {
          simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_SERVICE_TIER,
                  (String) config.getTemplateSettings().get(JagGenerator.TEMPLATE_SERVICE_TIER), dsClass);
       }

        // app part.
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_APPLICATION_NAME, app.getName().toString(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_DESCRIPTION, projectName, dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_APPLICATION_VERSION, app.getVersion().toString(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_ROOT_PACKAGE, rootPackage, dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_LOGGING, logging, dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_DATE_FORMAT, app.getDateFormat().toString(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TIMESTAMP_FORMAT, app.getTimestampFormat().toString(), dsClass);

        // path part.
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_SERVICE_PATH, paths.getServiceOutput(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_EJB_PATH, paths.getEjbOutput(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_WEB_PATH, paths.getWebOutput(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_JSP_PATH, paths.getJspOutput(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TEST_PATH, paths.getTestOutput(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_CONFIG_PATH, paths.getConfigOutput(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_SWING_PATH, paths.getSwingOutput(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_MOCK_PATH, paths.getMockOutput(), dsClass);
        simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_HIBERNATE_PATH, paths.getHibernateOutput(), dsClass);

        simpleModel.addSimpleClassifier(dsClass);
    }


   /** Create a class with the datasource definition */
   private void createDataSource(Datasource ds, SimpleModel simpleModel) {
      SimpleUmlClass dsClass = new SimpleUmlClass("DataSource" + simpleModel.getName(), SimpleModelElement.PUBLIC);
      simpleModel.setStereoType(JagUMLProfile.STEREOTYPE_CLASS_DATA_SOURCE, dsClass);
      simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_JDBC_URL, ds.getJdbcUrl().toString(), dsClass);
      simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_MAPPING,
            ds.getDatabase().getTypeMapping(), dsClass);
      simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_USER_NAME, ds.getUserName().toString(), dsClass);
      simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_PASSWORD, ds.getPassword().toString(), dsClass);
      simpleModel.addTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_JNDI_NAME, ds.getJndiName().toString(), dsClass);
      simpleModel.addSimpleClassifier(dsClass);
   }

   /**
    *
    * Define the basic types and add them to the model if they don't already exist
    * We assume the types are in the java.lang subpackage...
    *
    * @param simpleModel the uml model to create
    * @return HashMap with a mapping of the different types.
    */
   public HashMap createBasicTypes(SimpleModel simpleModel) {
      if (typeMappings != null) {
         return typeMappings;
      }
      typeMappings = new HashMap();

      // First create the required uml packages.
      javaLangPackage = simpleModel.addSimpleUmlPackage("java.lang");
      javaUtilPackage = simpleModel.addSimpleUmlPackage("java.util");
      javaSqlPackage = simpleModel.addSimpleUmlPackage("java.sql");
      javaMathPackage = simpleModel.addSimpleUmlPackage("java.math");


      // Create the default types:
      HashMap basicTypes = new HashMap();

      basicTypes.put(stringType, stringValue);
      basicTypes.put(intType, intValue);
      basicTypes.put(byteType, byteValue);
      basicTypes.put(shortType, shortValue);
      basicTypes.put(floatType, floatValue);
      basicTypes.put(doubleType, doubleValue);
      basicTypes.put(longType, longValue);

      Set basicTypesSet = basicTypes.keySet();
      for (Iterator iterator = basicTypesSet.iterator(); iterator.hasNext();) {
         String key = (String) iterator.next();
         SimpleUmlClass simpleUmlClass = new SimpleUmlClass((String) basicTypes.get(key), SimpleModelElement.PUBLIC);
         javaLangPackage.addSimpleClassifier(simpleUmlClass);
         typeMappings.put(key, simpleUmlClass);
      }
      basicTypes.put(bigDecimalType, bigDecimalValue);
      basicTypes.put(sqlDateType, sqlDateValue);
      basicTypes.put(sqlTimestampType, sqlTimestampValue);
      basicTypes.put(sqlUtilType, sqlUtilValue);
      // Make string the default type.
      typeMappings.put(defaultType, typeMappings.get(stringType));

      // Add bigdecimals
      SimpleUmlClass simpleUmlClass = new SimpleUmlClass((String) basicTypes.get(bigDecimalType), SimpleModelElement.PUBLIC);
      javaMathPackage.addSimpleClassifier(simpleUmlClass);
      typeMappings.put(bigDecimalType, simpleUmlClass);

      simpleUmlClass = new SimpleUmlClass((String) basicTypes.get(sqlDateType), SimpleModelElement.PUBLIC);
      javaSqlPackage.addSimpleClassifier(simpleUmlClass);
      typeMappings.put(sqlDateType, simpleUmlClass);

      simpleUmlClass = new SimpleUmlClass((String) basicTypes.get(sqlTimestampType), SimpleModelElement.PUBLIC);
      javaSqlPackage.addSimpleClassifier(simpleUmlClass);
      typeMappings.put(sqlTimestampType, simpleUmlClass);

      simpleUmlClass = new SimpleUmlClass((String) basicTypes.get(sqlUtilType), SimpleModelElement.PUBLIC);
      javaUtilPackage.addSimpleClassifier(simpleUmlClass);
      typeMappings.put(sqlUtilType, simpleUmlClass);

      return typeMappings;
   }

   /**
    *
    * @param args
    */
   public static void main(String[] args) {
      if (args.length == 1) {
         (new Jag2UMLGenerator()).generateXMI(args[0]);
      } else {
         log("Pass a JAG xml file as argument!");
      }


   }

   private static void log(String message) {
      log.info(message);
      if (logger == null) {
         log.info(message);
      }
   }
}

/*
        $Log: Jag2UMLGenerator.java,v $
        Revision 1.23  2006/03/11 13:44:04  ekkelenkamp
        Don't write the Diagram information, it's not very compatible.

        Revision 1.22  2006/02/17 12:49:15  ekkelenkamp
        Added sql timestamp

        Revision 1.21  2005/12/24 13:35:48  ekkelenkamp
        added new tagged values.

        Revision 1.20  2005/09/23 07:23:58  ekkelenkamp
        export service tier selection

        Revision 1.19  2005/07/29 07:47:05  ekkelenkamp
        Don't use EntityRef

        Revision 1.18  2005/07/15 21:27:50  ekkelenkamp
        no spring path

        Revision 1.17  2005/06/09 19:09:54  ekkelenkamp
        java5 support.

        Revision 1.16  2005/03/04 21:07:22  ekkelenkamp
        Business method support for UML

        Revision 1.15  2005/03/04 15:17:52  ekkelenkamp
        business methods are in progress now.

        Revision 1.14  2005/02/04 09:35:05  ekkelenkamp
        Use constants where possible

        Revision 1.13  2005/02/04 08:20:42  ekkelenkamp
        UML synchronize up-to-date.

        Revision 1.12  2005/01/19 21:44:58  ekkelenkamp
        uml support for many-to-one relations and bidirectionality.

        Revision 1.11  2004/12/27 12:55:16  ekkelenkamp
        Support for business methods.

        Revision 1.10  2004/12/23 12:25:52  ekkelenkamp
        Support for business methods.

        Revision 1.9  2004/11/27 23:50:54  ekkelenkamp
        Improved UML/JAG synchronization.

        Revision 1.8  2004/11/27 19:30:07  ekkelenkamp
        Improved UML/JAG synchronization.

        Revision 1.7  2004/11/27 07:50:03  ekkelenkamp
        Improved UML/JAG synchronization.

        Revision 1.6  2004/11/26 22:36:13  ekkelenkamp
        export all project settings to a Config class.

        Revision 1.5  2004/03/28 11:55:34  ekkelenkamp
        tagged values on model

        Revision 1.4  2003/12/09 10:21:44  oconnor_m
        3.1 release - generic DB support and jag-config XML

        Revision 1.3  2003/11/25 15:10:30  oconnor_m
        support for importing/exporting from/to UML model via XMI

        Revision 1.2  2003/11/14 09:44:17  ekkelenkamp
        First functional version

        Revision 1.1  2003/11/02 14:01:31  ekkelenkamp
        Initial version of UML support in Jag

*/