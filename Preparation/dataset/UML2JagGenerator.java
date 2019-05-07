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
import com.finalist.jaggenerator.*;
import com.finalist.uml14.simpleuml.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import org.w3c.dom.Document;

/**
 * Generate a JAG skelet configuration file based on an UML1.4 XMI model.
 *
 * @author Rudie Ekkelenkamp, Michael O'Connor - Finalist IT Group
 * @version $Revision: 1.26 $, $Date: 2005/12/24 13:35:48 $
 * @todo Plug in a generic java class field type --> SQL column type mapping, instead of DEFAULT_SQL_TYPE..
 */
public class UML2JagGenerator {
   private ConsoleLogger logger;
   private static SimpleModel model;
   static Log log = LogFactory.getLog(UML2JagGenerator.class);

   private static final String DEFAULT_SQL_TYPE = "VARCHAR(255)"; // a safe generic column type for most databases
   private static final String CHARACTER_PRIMITIVE = "char";
   private static final String CHARACTER_CLASS = "java.lang.Character";
   private static final String JAVA_LANG_PACKAGE_PREFIX = "java.lang.";
   private static final char DOT = '.';
   private static final String EMPTY_STRING = "";
   private static final String INTEGER_PRIMITIVE = "int";
   private static final String INTEGER_CLASS = "java.lang.Integer";
   private static final String DEFAULT_JDBC_TYPE = "VARCHAR";
   private static final String NUMBER_SQL_TYPE = "NUMBER";
   private static final String TIMESTAMP_SQL_TYPE = "DATE";
   private static final String BIGINT_JDBC_TYPE = "BIGINT";

   /**
    * Makes a UML2JagGenerator with an external logger.
    *
    * @param logger somewhere to redirect output, other than System.out
    */
   public UML2JagGenerator(ConsoleLogger logger) {
      this.logger = logger;
      model = new SimpleModel();
   }

   /**
    * Makes a UML2JagGenerator.
    */
   public UML2JagGenerator() {
      model = new SimpleModel();
   }


   /**
    * Convert the UML model into a JAG readable XML application file.
    *
    * @param xmiFileName
    * @param outputDir directory where the XML file will be stored.
    * @return the xml File created, if any.
    */
   public synchronized File generateXML(String xmiFileName, String outputDir) {
      URL url = null;
      try {
         url = new URL("FILE", EMPTY_STRING, xmiFileName);
      } catch (MalformedURLException e) {
         e.printStackTrace();
      }

      model.readModel(url);

      try {
         checkInterruptStatus();
         return generateXML(model, outputDir);

      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      return null;
   }

   private File generateXML(SimpleModel model, String outputDir) throws InterruptedException {
      File file = null;
      Root root = generateConfig(model);
      checkInterruptStatus();

      try {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = dbf.newDocumentBuilder();
         Document doc = builder.newDocument();
         org.w3c.dom.Element skelet = doc.createElement("skelet");
         doc.appendChild(skelet);
         root.getXML(skelet);
         String XMLDoc = com.finalist.jaggenerator.JagGenerator.outXML(doc);
         log.debug("The generated xml project file is: ");
         log.debug(XMLDoc);
         file = new File(outputDir, model.getName() + ".xml");
         checkInterruptStatus();

         OutputStream outputStream = new FileOutputStream(file);
         outputStream.write(XMLDoc.getBytes());
         outputStream.flush();
         outputStream.close();

      } catch (Exception e) {
         e.printStackTrace();
         log("Error writing the file.");
      }

      return file;
   }

   /**
    * Generate a config file readable by JAG from the UML model
    *
    * @param model UML Model
    * @return Root conifguration
    */
   private Root generateConfig(SimpleModel model) throws InterruptedException {
      Root root = new Root();
      createDataSource(model, root.datasource);
      createConfig(model, root.config, root.app, root.paths);
      checkInterruptStatus();

      // Create the Session EJBs
      HashMap sessionEJBMap = createSessionEJBs(model);
      if (sessionEJBMap != null && sessionEJBMap.size() > 0) {
         root.setSessionEjbs(new ArrayList(sessionEJBMap.values()));
      }
      checkInterruptStatus();

      // Create the entity EJBs.
      HashMap entityEJBMap = createEntityEJBs(model);
      if (entityEJBMap != null && entityEJBMap.size() > 0) {
         root.setEntityEjbs(new ArrayList(entityEJBMap.values()));
      }
      checkInterruptStatus();

      // Create the container-managed relations
      createContainerManagedRelations(entityEJBMap, model);

      return root;
   }

   private void checkInterruptStatus() throws InterruptedException {
      if (Thread.interrupted()) {
         throw new InterruptedException();
      }
   }

   /**
    * Create all entity EJBs in the uml model and put them in a hashmap
    *
    * @param model the uml model.
    * @return HashMap with all Entity classes.
    */
   private HashMap createEntityEJBs(SimpleModel model) {
      HashMap map = new HashMap();
      // Get a list of all packages in the model.
      Collection pkList = model.getAllSimpleUmlPackages(model);
      for (Iterator pkIterator = pkList.iterator(); pkIterator.hasNext();) {
         SimpleUmlPackage simpleUmlPackage = (SimpleUmlPackage) pkIterator.next();
         Collection list;
         list = simpleUmlPackage.getSimpleClassifiers();
         for (Iterator pkit = list.iterator(); pkit.hasNext();) {
            SimpleModelElement el = (SimpleModelElement) pkit.next();
            if ((el instanceof SimpleUmlClass) &&
                  model.getStereoType(el) != null &&
                  model.getStereoType(el).equalsIgnoreCase(JagUMLProfile.STEREOTYPE_CLASS_ENTITY)) {
               // We got a winner, it's a class with the right stereotype.

               SimpleUmlClass suc = (SimpleUmlClass) el;

               String rootPackage = simpleUmlPackage.getFullPackageName();
               String tableName = getTaggedValue(model,
                     JagUMLProfile.TAGGED_VALUE_CLASS_TABLE_NAME, suc,
                     Utils.unformat(Utils.firstToLowerCase(suc.getName())));

               Entity entity = new Entity(EMPTY_STRING, tableName, EMPTY_STRING);
               entity.setRootPackage(rootPackage);
               entity.setName(suc.getName());
               entity.setDescription(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, suc));
               if (model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DISPLAY_NAME, suc) != null) {
                  entity.setDisplayName(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DISPLAY_NAME, suc));
               }
               if (model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_IS_ASSOCIATION, suc) != null) {
                  entity.isAssociationEntity.setSelectedItem(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_IS_ASSOCIATION, suc));
               }
               
               entity.setRefName(entity.getName().toString());

               // Now iterate over the fields of the model element and Create fields that will be added to the entity.
               int pkCount = 0;
               Collection attributes = suc.getSimpleAttributes();
               Field primaryKeyField = null;
               for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
                  SimpleAttribute att = (SimpleAttribute) iterator.next();
                  boolean isPK = equal(model.getStereoType(att), JagUMLProfile.STEREOTYPE_ATTRIBUTE_PRIMARY_KEY);
                  boolean required = false;
                  if (isPK) {
                     required = true;
                  } else {
                     // Only set required to true if a stereotype has been defined.
                     required = equal(model.getStereoType(att), JagUMLProfile.STEREOTYPE_ATTRIBUTE_REQUIRED);
                  }

                  Column col = new Column();
                  String colName = model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_COLUMN_NAME, att);

                  if (colName == null) {
                     //make a column name based on the attribute name
                     colName = Utils.unformat(att.getName());
                  }
                  col.setName(colName);
                  String sqlType = getTaggedValue(model,
                        JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_SQL_TYPE, att, null);
                  String jdbcType = getTaggedValue(model,
                        JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_JDBC_TYPE, att, null);
                  col.setPrimaryKey(isPK);
                  col.setNullable(!required);

                  SimpleClassifier theClassifier = att.getType();
                  String fieldType = theClassifier.getOwner().getFullPackageName();
                  if (fieldType != null && !Character.isLowerCase(theClassifier.getName().charAt(0))) {
                     fieldType = fieldType + DOT + theClassifier.getName();
                  } else {
                     //JAG doesn't support primitive entity bean persistant field types..
                     String primitiveType = theClassifier.getName();
                     if (CHARACTER_PRIMITIVE.equals(primitiveType)) {
                        fieldType = CHARACTER_CLASS;
                     }
                     else if (INTEGER_PRIMITIVE.equals(primitiveType)) {
                        fieldType = INTEGER_CLASS;
                     }
                     else {
                        fieldType = JAVA_LANG_PACKAGE_PREFIX +
                              Character.toUpperCase(primitiveType.charAt(0)) +
                              primitiveType.substring(1);
                     }
                  }
                  if (sqlType == null) {
                     String[] mappedTypes = getDatabaseColumnTypesForClass(fieldType);
                     sqlType = mappedTypes[0];
                     jdbcType = mappedTypes[1];
                  }
                  col.setSqlType(sqlType);
                  String autoGeneratedPrimaryKey = model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_ATTRIBUTE_AUTO_PRIMARY_KEY, att);
                  boolean generate = false;
                  if ("true".equalsIgnoreCase(autoGeneratedPrimaryKey)) {
                     generate = true;
                  }
                  Field field = new Field(entity, col);
                  field.setName(att.getName());
                  field.setType(fieldType); // Overrule the automatic setting by the Field constructor.
                  if (isPK) {
                     field.setPrimaryKey(isPK);
                  }
                  field.setSqlType(sqlType);
                  field.setJdbcType(jdbcType);
                  field.setHasAutoGenPrimaryKey(generate);


                  if (isPK) {
                     pkCount++;
                     primaryKeyField = field;
                  }
                  entity.add(field);
               }

               if (pkCount > 1) {
                  // It's a composite primary key.
                  entity.setIsComposite("true");
                  // Set the primary key class....
                  String compositePK = model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_COMPOSITE_PRIMARY_KEY, suc);
                  entity.setPKeyType(compositePK);
                  // set the composite key here//
               } else {
                  if (primaryKeyField != null) {
                     entity.setPrimaryKey(primaryKeyField);
                  }
               }

               if (pkCount == 0) {
                  log("UML Error! Entity '" + entity.getName() + "' has no primary key! At least one attribute " +
                        "in an entity bean must have the stereotype \"PrimaryKey\".");
                  JOptionPane.showMessageDialog(null,
                              "Entity '" + entity.getName() + "' has no primary key! At least one attribute " +
                              "in an entity bean must have the stereotype \"PrimaryKey\".",
                              "UML Error!",
                              JOptionPane.ERROR_MESSAGE);
               } else {
                  // Put the entity in the hashmap entity
                  map.put(entity.getRefName(), entity);
               }
            }
         }
      }
      return map;
   }

   /** yet another mapping...
    *
    * These mappings are as far as possible database-generic, using Oracle specifics where genericity (is that a word?)
    * is not possible (Oracle-specific types are automatically re-typed in JAG if the user selects a non-Oracle database
    * in the 'Datasource' settings).
    * NOTE: 'Genericity' is more important than accuracy here..
    *
    * @return a two-length String array [sql type, jdbc type]
    */
   private String[] getDatabaseColumnTypesForClass(String javaClass) {
      if (Byte.class.getName().equals(javaClass) ||
          Short.class.getName().equals(javaClass) ||
          Integer.class.getName().equals(javaClass) ||
          Long.class.getName().equals(javaClass) ||
          Double.class.getName().equals(javaClass)) {
         return new String[] {NUMBER_SQL_TYPE, BIGINT_JDBC_TYPE};
      }
      if (java.sql.Timestamp.class.getName().equals(javaClass) ||
          java.sql.Date.class.getName().equals(javaClass) ||
          java.util.Date.class.getName().equals(javaClass)) {
         return new String[] {TIMESTAMP_SQL_TYPE, TIMESTAMP_SQL_TYPE};
      }

      return new String[] {DEFAULT_SQL_TYPE, DEFAULT_JDBC_TYPE};
   }

   private String getTaggedValue(SimpleModel model, String taggedValueAttributeSqlType,
                                 SimpleModelElement att, String defaultValue) {
      String value = model.getTaggedValue(taggedValueAttributeSqlType, att);
      return value == null ? defaultValue : value;
   }

   /** Create the session EJBs on the simpleModel. */
   private HashMap createSessionEJBs(SimpleModel model) {
      HashMap map = new HashMap();

      // Get a list of all packages in the model.
      Collection pkList = model.getAllSimpleUmlPackages(model);
      for (Iterator pkIterator = pkList.iterator(); pkIterator.hasNext();) {
         SimpleUmlPackage simpleUmlPackage = (SimpleUmlPackage) pkIterator.next();

         Collection list = simpleUmlPackage.getSimpleClassifiers();
         for (Iterator it = list.iterator(); it.hasNext();) {
            SimpleModelElement el = (SimpleModelElement) it.next();
            if ((el instanceof SimpleUmlClass) &&
                  model.getStereoType(el) != null &&
                  model.getStereoType(el).equals(JagUMLProfile.STEREOTYPE_CLASS_SERVICE)) {
               // We got a winner, it's a class with the right stereotype.
               ArrayList businessMethods = new ArrayList();

               SimpleUmlClass suc = (SimpleUmlClass) el;
               Collection operations = suc.getSimpleOperations();
               for (Iterator oit = operations.iterator(); oit.hasNext();) {
                   BusinessMethod bm = new BusinessMethod();
                   SimpleOperation operation = (SimpleOperation) oit.next();
                   log.info("The operation name is: " + operation.getName());

                   bm.setMethodName(operation.getName());
                   String desc = model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, operation);
                   if (desc == null) {
                     bm.setDescription("");
                   } else {
                      bm.setDescription(desc);
                   }

                   ArrayList argList = new ArrayList();
                   log.info("The number of parameters: " + operation.getSimpleParameters().size());
                   for (Iterator pit = operation.getSimpleParameters().iterator(); pit.hasNext();) {
                       SimpleParameter param = (SimpleParameter) pit.next();
                       BusinessArgument arg = new BusinessArgument();
                       String type;
                      // inout of return.
                       log.debug("Param kind: " + param.getKind());
                       if (param.getType() != null) {
                          if (param.getType().getName() != null && param.getType().getName().equalsIgnoreCase("void")) {
                             type = "void";
                          } else {
                              String packageName = param.getType().getOwner().getFullPackageName();
                              if (packageName == null) {
                                 packageName = "";
                              } else {
                                 packageName = param.getType().getOwner().getFullPackageName() + ".";
                              }
                              String typeName = param.getType().getName();
                              if (typeName != null) {
                                 if (typeName.startsWith("java::lang::")) {
                                    // This is prepended by poseidon 3.
                                    typeName = typeName.substring(12);
                                 }
                              }
                              type = "" + packageName + typeName;
                          }
                       } else {
                          type = "void";
                       }
                       if (param.getKind().equalsIgnoreCase(SimpleParameter.RETURN)) {
                           log.info("Found a return type");
                           bm.setReturnType(type);
                       } else {
                           log.info("The param name is: " + param.getName());
                           arg.setName(param.getName());
                           log.info("The param type is: " + type);
                           arg.setType(type);
                           argList.add(arg);
                       }
                   }
                  bm.setArgumentList(argList);
                  businessMethods.add(bm);
               }

               String rootPackage = simpleUmlPackage.getFullPackageName();
               Session session = new Session(rootPackage);
               session.setName(suc.getName());
               session.setRootPackage(rootPackage);
               session.setRefName(suc.getName());
               session.setDescription(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_DOCUMENTATION, suc));
               session.setBusinessMethods(businessMethods);
               // Now add the entity refs to the session.
               Collection deps = model.getSimpleDependencies();
               for (Iterator iterator = deps.iterator(); iterator.hasNext();) {
                  SimpleDependency sd = (SimpleDependency) iterator.next();
                   // session.addRef(entityRef);
                  if (sd.getClient().getName().equals(session.getName().toString())) {
                     // This dependency is from the current Session. Add a ref.
                     String entityRef = sd.getSupplier().getName();
                     session.addRef(entityRef);
                  }
               }
               // Put the entity in the hashmap entity
               map.put(session.getRefName(), session);
            }
         }
      }
      return map;
   }

   /**
    * Determine the relations that are defined for an entity EJB.
    *
    * @param entityEJBMap Current classes in the model.
    * @param simpleModel The UML model.
    */
   private void createContainerManagedRelations(HashMap entityEJBMap, SimpleModel simpleModel) {
      Iterator assocs = simpleModel.getSimpleAssociations().iterator();
      while (assocs.hasNext()) {
         SimpleAssociation assoc = (SimpleAssociation) assocs.next();

         String sourceEntityName = assoc.getSource().getSimpleClassifier().getName();
         String destinationEntityName = assoc.getDestination().getSimpleClassifier().getName();
         Entity sourceEntity = (Entity) entityEJBMap.get(sourceEntityName);
         Entity destinationEntity = (Entity) entityEJBMap.get(destinationEntityName);
         boolean multiplicity;
         boolean bidirectional;
         if (JagUMLProfile.TAGGED_VALUE_ASSOCIATION_MULTIPLICITY_ONE_TO_ONE.equals(simpleModel.getTaggedValue(JagUMLProfile.TAGGED_VALUE_ASSOCIATION_MULTIPLICITY, assoc))) {
            multiplicity = false;
         } else {
            // This is the default.
            multiplicity = true;
         }
         if ("true".equals(simpleModel.getTaggedValue(JagUMLProfile.TAGGED_VALUE_ASSOCIATION_BIDIRECTIONAL, assoc))) {
            bidirectional = true;
         } else {
            // This is the default.
            bidirectional = false;
         }

         if (sourceEntity == null || destinationEntity == null) {
            log("The relation named '" + assoc.getName() +
                  "' has 1 or more 'association ends' " +
                  "whose names do not correspond to entity bean class names");
            continue;
         }

         ForeignKey info = new ForeignKey();
         String fkFieldName = assoc.getName();
         String fkColumnName = null;
         Iterator i = sourceEntity.getFields().iterator();
         while (i.hasNext()) {
            Field field = (Field) i.next();
            if (field.getName().equals(fkFieldName)) {
               fkColumnName = field.getColumnName();
            }
         }
         info.setPkTableName(destinationEntity.getLocalTableName().toString());
         info.setPkColumnName(destinationEntity.getPrimaryKey().getColumnName());
         info.setFkColumnName(fkColumnName);
         info.setFkName(fkFieldName);

         Relation relation = new Relation(sourceEntity, info, false);
         relation.setBidirectional(bidirectional);
         relation.setTargetMultiple(multiplicity);
         sourceEntity.addRelation(relation);
         log("Added relation: " + relation);
      }
   }

   private void log(String message) {
      log.info(message);
      if (logger != null) {
         logger.log(message);
      }
   }

   /** Create a class with the datasource definition */
   private void createDataSource(SimpleModel model, Datasource ds) {
      boolean datasourceFound = false;
      // Get a list of all packages in the model.
      Collection pkList = model.getAllSimpleUmlPackages(model);
      for (Iterator pkIterator = pkList.iterator(); pkIterator.hasNext();) {
         SimpleUmlPackage simpleUmlPackage = (SimpleUmlPackage) pkIterator.next();
         Collection list = simpleUmlPackage.getSimpleClassifiers();
         for (Iterator it = list.iterator(); it.hasNext();) {
            SimpleModelElement el = (SimpleModelElement) it.next();
            if ((el instanceof SimpleUmlClass) &&
                  model.getStereoType(el) != null &&
                  model.getStereoType(el).equals(JagUMLProfile.STEREOTYPE_CLASS_DATA_SOURCE)) {
               // We got a winner, it's a class with the right stereotype.
               datasourceFound = true;
               ds.setJndi(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_JNDI_NAME, el));
               ds.setMapping(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_MAPPING, el));
               ds.setJdbcUrl(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_JDBC_URL, el));
               ds.setUserName(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_USER_NAME, el));
               ds.setPassword(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CLASS_DATA_SOURCE_PASSWORD, el));
            }
         }
      }

      if (!datasourceFound) {
         ds.setJndi("jdbc/" + model.getName());

      }
   }

    /** Create a class with the datasource definition */
    private void createConfig(SimpleModel model, Config config, App app, Paths paths) {
       // Get a list of all packages in the model.
       Collection pkList = model.getAllSimpleUmlPackages(model);
       for (Iterator pkIterator = pkList.iterator(); pkIterator.hasNext();) {
          SimpleUmlPackage simpleUmlPackage = (SimpleUmlPackage) pkIterator.next();
          Collection list = simpleUmlPackage.getSimpleClassifiers();
          for (Iterator it = list.iterator(); it.hasNext();) {
             SimpleModelElement el = (SimpleModelElement) it.next();
             if ((el instanceof SimpleUmlClass) &&
                   model.getStereoType(el) != null &&
                   model.getStereoType(el).equals(JagUMLProfile.STEREOTYPE_CLASS_JAG_CONFIG)) {
                // We got a winner, it's a class with the right stereotype.

                 config.setAuthor(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_AUTHOR, el));
                 config.setVersion(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_VERSION, el));
                 config.setCompany(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_COMPANY, el));
                 String templateDir = model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TEMPLATE, el);
                 if (templateDir != null) {
                    File dir = new File(templateDir);
                    config.getTemplate().setTemplateDir(dir);
                 }
                 HashMap map = new HashMap();
                 map.put(JagGenerator.TEMPLATE_APPLICATION_SERVER, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_APPSERVER, el));
                 map.put(JagGenerator.TEMPLATE_USE_RELATIONS, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_RELATIONS, el));
                 map.put(JagGenerator.TEMPLATE_USE_JAVA5, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_JAVA5, el));
                 map.put(JagGenerator.TEMPLATE_USE_MOCK, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_USE_MOCK, el));
                 map.put(JagGenerator.TEMPLATE_WEB_TIER,  model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_WEB_TIER, el));
                 map.put(JagGenerator.TEMPLATE_BUSINESS_TIER, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_BUSINESS_TIER, el));
                 map.put(JagGenerator.TEMPLATE_SERVICE_TIER, model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_CONFIG_SERVICE_TIER, el));
                 config.setTemplateSettings(map);

                 app.setName(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_APPLICATION_NAME, el));
                 app.setDescription(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_DESCRIPTION, el));
                 app.setVersion(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_APPLICATION_VERSION, el));
                 app.setRootPackage(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_ROOT_PACKAGE, el));
                 app.setLogFramework(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_LOGGING, el));
                 app.setDateFormat(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_DATE_FORMAT, el));
                 app.setTimestampFormat(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TIMESTAMP_FORMAT, el));

                 paths.setServiceOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_SERVICE_PATH, el));
                 paths.setEjbOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_EJB_PATH, el));
                 paths.setWebOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_WEB_PATH, el));
                 paths.setJspOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_JSP_PATH, el));
                 paths.setTestOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_TEST_PATH, el));
                 paths.setConfigOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_CONFIG_PATH, el));
                 paths.setSwingOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_SWING_PATH, el));
                 paths.setMockOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_MOCK_PATH, el));
                 paths.setHibernateOutput(model.getTaggedValue(JagUMLProfile.TAGGED_VALUE_MODEL_HIBERNATE_PATH, el));
             }
          }
       }
    }


   private boolean equal(String a, String b) {
      if (a != null && b != null && a.equals(b)) {
         return true;
      } else
         return false;
   }

   /**
    *
    * @param args
    */
   public static void main(String[] args) {
      if (args.length == 1) {
         new UML2JagGenerator().generateXML(args[0], ".");
      } else {
         System.out.println("Pass an xmi file as argument!");
      }
   }
}

/*
        $Log: UML2JagGenerator.java,v $
        Revision 1.26  2005/12/24 13:35:48  ekkelenkamp
        added new tagged values.

        Revision 1.25  2005/09/29 17:03:32  ekkelenkamp
        Only refs from the current dependency

        Revision 1.24  2005/09/29 14:48:06  ekkelenkamp
        Bug fix for required field.

        Revision 1.23  2005/09/23 07:23:58  ekkelenkamp
        export service tier selection

        Revision 1.22  2005/07/29 07:47:05  ekkelenkamp
        Don't use EntityRef

        Revision 1.21  2005/07/15 21:27:50  ekkelenkamp
        no spring path

        Revision 1.20  2005/06/09 19:09:54  ekkelenkamp
        java5 support.

        Revision 1.19  2005/03/04 21:07:22  ekkelenkamp
        Business method support for UML

        Revision 1.18  2005/03/04 15:17:33  ekkelenkamp
        business methods are in progress now.

        Revision 1.17  2005/02/04 09:35:06  ekkelenkamp
        Use constants where possible

        Revision 1.16  2005/02/04 08:20:43  ekkelenkamp
        UML synchronize up-to-date.

        Revision 1.15  2005/01/19 21:44:58  ekkelenkamp
        uml support for many-to-one relations and bidirectionality.

        Revision 1.14  2004/12/27 12:55:16  ekkelenkamp
        Support for business methods.

        Revision 1.13  2004/12/23 12:25:52  ekkelenkamp
        Support for business methods.

        Revision 1.12  2004/12/22 11:23:01  ekkelenkamp
        initial support for business methods.

        Revision 1.11  2004/12/05 23:27:44  ekkelenkamp
        Fixes for relation fields update.

        Revision 1.10  2004/11/27 23:50:54  ekkelenkamp
        Improved UML/JAG synchronization.

        Revision 1.9  2004/11/27 19:30:07  ekkelenkamp
        Improved UML/JAG synchronization.

        Revision 1.8  2004/11/27 07:50:04  ekkelenkamp
        Improved UML/JAG synchronization.

        Revision 1.7  2004/03/28 12:34:03  ekkelenkamp
        Set root package

        Revision 1.6  2004/03/28 11:55:34  ekkelenkamp
        tagged values on model

        Revision 1.5  2003/11/27 17:54:13  oconnor_m
        no message

        Revision 1.4  2003/11/25 15:10:30  oconnor_m
        support for importing/exporting from/to UML model via XMI

        Revision 1.3  2003/11/14 09:44:39  ekkelenkamp
        Functional version, only container managed relations are missing

        Revision 1.2  2003/11/03 12:32:42  ekkelenkamp
        Initial support for Session EJBs.

        Revision 1.1  2003/11/02 14:01:30  ekkelenkamp
        Initial version of UML support in Jag

*/