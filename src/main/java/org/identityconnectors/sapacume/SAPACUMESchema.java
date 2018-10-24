/**
 * 
 */
package org.identityconnectors.sapacume;


import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.identityconnectors.common.IOUtil;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.AttributeInfo.Flags;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributeInfos;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.UpdateAttributeValuesOp;
import org.identityconnectors.sapume.SAPUMEConnector;


/**
 * @author ranjith.kumar
 *
 */
public final class SAPACUMESchema implements SAPACUMEConstants{

	//private static final Log LOG = Log.getLog(SAPACUMESchema.class);
	private Schema schema = null;
	//private Map<String, AttributeInfo> umeAccountAttrMap;
	private SAPUMEConnector sapumeConnector = null;
	private SAPACUMEConfiguration sapacConfiguration = null;
	private Set<String> ac10attrSet = new HashSet<String>();
	private Set<String> ac53attrSet = new HashSet<String>();
	private boolean isUMESchemaRequired;
	private boolean isGRC53 = false;
	private String fileName = "SapacToSapumeFieldMappings.properties";
	
	private String ac53Attributes[] = { COMPANY_53, VALIDTO, FIRST_NAME_53,
			LAST_NAME_53, VALIDFROM_53, TELEPHONE_53, DEPARTMENT_53,
			FUNCTIONAL_AREA_53, MANAGER_ID_53, MANAGER_EMAIL_ADDRESS_53,
			MANAGER_FIRST_NAME_53, MANAGER_LAST_NAME_53, MANAGER_TELEPHONE_53,
			PRIORITY_53, REQUESTID, REQUESTOR_EMAIL_ADDRESS_53,
			REQUESTOR_FIRST_NAME_53, REQUESTOR_ID__53_53,
			REQUESTOR_LAST_NAME_53, REQUEST_REASON_53, ATTR_REQSTATUS,
			REQTYPE, APPLICATION_53, EMAIL_ADDRESS_53, LOCATION_53,
			USER_ID_53, ROLEID, ROLEID, ATTR_AC_REQ_ID, DECODE_USERLOCK };
	
	private String ac10Attributes[] = { ATTR_HDR_REQTR_ID,
			ATTR_HDR_REQTR_EMAIL, ATTR_HDR_PRIORITY, ATTR_HDR_SYSTEM,
			ATTR_HDR_REQ_DUEDATE, ATTR_HDR_REQ_REASON, ATTR_HDR_FUNC_AREA,
			ATTR_HDR_BPROCESS, ATTR_UI_USER_ID, ATTR_UI_TITLE, ATTR_UI_FNAME,
			ATTR_UI_LNAME, ATTR_UI_ACCNO, ATTR_UI_VALID_FROM, ATTR_UI_VALID_TO,
			ATTR_UI_FAX, ATTR_UI_EMAIL, ATTR_UI_TELEPHONE,
			ATTR_UI_LONGON_LANGUAGE, ATTR_UI_AC_MANAGER, ATTR_UI_AC_MGR_EMAIL,
			ATTR_UI_AC_MGR_FNAME, ATTR_UI_AC_MGR_LNAME, ATTR_UI_DEPARTMENT,
			ATTR_UI_FORM_OF_ADDRESS, ATTR_UI_MOBILE, DECODE_USERLOCK,
			ATTR_UI_POSITION, REQUESTID, REQTYPE,
			ATTR_HDR_SYSTEM, ATTR_ROLE_OR_GROUP_NAME, UMEROLE, UMEGROUP, ATTR_REQSTATUS };


	public SAPACUMESchema(SAPACUMEConfiguration sapacConfiguration, SAPUMEConnector sapumeConnector){
		this.sapacConfiguration = sapacConfiguration;
		this.sapumeConnector = sapumeConnector;
		this.isUMESchemaRequired = true;
	}

/*	public Map<String, AttributeInfo> getAccountAttrMap() {
		if(umeAccountAttrMap.size()>0)
			initSchema();
		return umeAccountAttrMap;
	}*/

	public Schema getSchema() {
		initSchema();
		return schema;
	}
	
	public Schema getOnlyAcSchema(){
		isUMESchemaRequired=false;
		initSchema();
		isUMESchemaRequired=true;
		return schema;
	}

	private void initSchema() {
		ObjectClassInfo oci = null;
		ObjectClassInfo roleInfo = null;
		ObjectClassInfo groupInfo = null;
		SchemaBuilder schemaBuilder = new SchemaBuilder(SAPACUMEConnector.class);
		Set<AttributeInfo> attrSet = new HashSet<AttributeInfo>();
		Set<AttributeInfo> roleAttrSet = new HashSet<AttributeInfo>();
		Set<AttributeInfo> groupAttrSet = new HashSet<AttributeInfo>();
		isGRC53 = sapacConfiguration.getIsGRC53();

		if(isGRC53){
			//ac53 attributes
			for(String attribute :ac53Attributes){
				//TODO - Need to set flags for 53 attrs
				attrSet.add(AttributeInfoBuilder.build(attribute, String.class));
				ac53attrSet.add(attribute);
			}
			//role 53
			roleAttrSet.add(AttributeInfoBuilder.build(ROLEID, String.class));
			//group 53
			groupAttrSet.add(AttributeInfoBuilder.build(ROLEID, String.class));				
		}else{
			for(String attribute :ac10Attributes){
				if(ATTR_ROLE_OR_GROUP_NAME.equalsIgnoreCase(attribute)){
					attrSet.add(AttributeInfoBuilder.build(attribute,String.class, EnumSet.of(Flags.MULTIVALUED)));
				} else if (getNotReturnedByDefaultSet().contains(attribute)){
					attrSet.add(AttributeInfoBuilder.build(attribute,String.class, EnumSet.of(Flags.NOT_RETURNED_BY_DEFAULT,Flags.NOT_READABLE)));
				} else if (REQUESTID.equalsIgnoreCase(attribute)
						|| REQTYPE.equalsIgnoreCase(attribute)
						|| ATTR_REQSTATUS.equalsIgnoreCase(attribute)){
					attrSet.add(AttributeInfoBuilder.build(attribute,String.class, EnumSet.of(Flags.NOT_UPDATEABLE,Flags.NOT_CREATABLE)));
				}
				else {
					attrSet.add(AttributeInfoBuilder.build(attribute, String.class));
				}
				ac10attrSet.add(attribute);
			} 
			//current attributes
			//attrSet.add(AttributeInfoBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT_NAME));
			//role 10
			roleAttrSet.add(AttributeInfoBuilder.build(ATTR_ROLE_OR_GROUP_NAME, String.class ));
			//group 10
			groupAttrSet.add(AttributeInfoBuilder.build(ATTR_ROLE_OR_GROUP_NAME, String.class));		
		}

		ObjectClassInfoBuilder ociAccountBuilder = new ObjectClassInfoBuilder();       	
		ObjectClassInfoBuilder ociRoleBuilder = new ObjectClassInfoBuilder();
		ObjectClassInfoBuilder ociGroupBuilder = new ObjectClassInfoBuilder();

		//START ADDING UME ATTRIBUTES
		if(isUMESchemaRequired){
			Schema schema = sapumeConnector.schema();
			Set<ObjectClassInfo> set = schema.getObjectClassInfo();
			for(ObjectClassInfo objectClassInfo : set){
				if(objectClassInfo.getType().equalsIgnoreCase("__ACCOUNT__")){
					Set<AttributeInfo> accountAttr = new HashSet<AttributeInfo>();
					Set<AttributeInfo> attrInfoSet = objectClassInfo.getAttributeInfo();
					Set<AttributeInfo> attrAccountSet = new HashSet<AttributeInfo>();
					Set<String> ac2umeMappedSet = getUmeMappedFields();
					for(AttributeInfo attrInfo : attrInfoSet){							
						//EnumSet<Flags> flagSet = (EnumSet<Flags>) attributeInfo.getFlags();
						if(!attrInfo.getType().getName().equals("org.identityconnectors.framework.common.objects.EmbeddedObject")){
							Set<AttributeInfo.Flags> flagSet =  new HashSet<AttributeInfo.Flags>();
							for(AttributeInfo.Flags aFlags : attrInfo.getFlags()){
								flagSet.add(aFlags);
							}
							flagSet.add(Flags.NOT_CREATABLE);	
							if(ac2umeMappedSet.contains(attrInfo.getName())){
								flagSet.add(Flags.NOT_UPDATEABLE);
								attrAccountSet.add(AttributeInfoBuilder.build(attrInfo.getName(), attrInfo.getType(), flagSet));
							}else{
								attrAccountSet.add(AttributeInfoBuilder.build(attrInfo.getName(), attrInfo.getType(), flagSet));
							}
						}else {
							attrAccountSet.add(attrInfo);
						}									
					}
					accountAttr.addAll(attrAccountSet);					
					ociAccountBuilder.addAllAttributeInfo(accountAttr);											
				}else if(objectClassInfo.getType().equalsIgnoreCase("Role")){
					ociRoleBuilder.addAllAttributeInfo(objectClassInfo.getAttributeInfo());
				}else if(objectClassInfo.getType().equalsIgnoreCase("Group")){						
					ociGroupBuilder.addAllAttributeInfo(objectClassInfo.getAttributeInfo());				
				}
			}
		}else{
			//OPERATIONAL ATTRIBUTES
			attrSet.add(AttributeInfoBuilder.build(Name.NAME, String.class)); 			
			attrSet.add(OperationalAttributeInfos.ENABLE);	        	        
			attrSet.add(OperationalAttributeInfos.LOCK_OUT);
		}
		//end ADDING UME ATTRIBUTES

		//role object
		ociRoleBuilder.addAllAttributeInfo(roleAttrSet);
		ociRoleBuilder.setType("Role");
		roleInfo = ociRoleBuilder.build();
		schemaBuilder.defineObjectClass(roleInfo);

		//group object
		ociGroupBuilder.addAllAttributeInfo(groupAttrSet);
		ociGroupBuilder.setType("Group");
		groupInfo = ociGroupBuilder.build();
		schemaBuilder.defineObjectClass(groupInfo);

		//account object
		ociAccountBuilder.addAllAttributeInfo(attrSet);
		ociAccountBuilder.setType(ObjectClass.ACCOUNT_NAME);
		oci = ociAccountBuilder.build();
		schemaBuilder.defineObjectClass(oci);

		schemaBuilder.clearSupportedObjectClassesByOperation();
		schemaBuilder.addSupportedObjectClass(CreateOp.class, oci);
		schemaBuilder.addSupportedObjectClass(SchemaOp.class, oci);
		schemaBuilder.addSupportedObjectClass(TestOp.class, oci);
		schemaBuilder.addSupportedObjectClass(UpdateAttributeValuesOp.class,
				oci);
		schemaBuilder.addSupportedObjectClass(SearchOp.class, oci);
		schemaBuilder.addSupportedObjectClass(SearchOp.class, roleInfo);
		schemaBuilder.addSupportedObjectClass(SearchOp.class, groupInfo);	
		schemaBuilder.addSupportedObjectClass(TestOp.class, roleInfo);
		schemaBuilder.addSupportedObjectClass(TestOp.class, groupInfo);
		schema = schemaBuilder.build();
	}

	private Set<String> getNotReturnedByDefaultSet(){
		HashSet<String> acNotReturnedSet = new HashSet<String>();
		if(isGRC53){
			//TODO - Add attributes for AC 53
		}else{
		acNotReturnedSet.add(ATTR_HDR_BPROCESS);
		acNotReturnedSet.add(ATTR_HDR_FUNC_AREA);
		acNotReturnedSet.add(ATTR_UI_AC_MANAGER);
		acNotReturnedSet.add(ATTR_UI_AC_MGR_EMAIL);
		acNotReturnedSet.add(ATTR_UI_AC_MGR_FNAME);
		acNotReturnedSet.add(ATTR_UI_AC_MGR_FNAME);
		acNotReturnedSet.add(ATTR_UI_AC_MGR_LNAME);
		acNotReturnedSet.add(ATTR_HDR_PRIORITY);
		acNotReturnedSet.add(ATTR_HDR_REQ_DUEDATE);
		acNotReturnedSet.add(ATTR_HDR_REQTR_EMAIL);
		acNotReturnedSet.add(ATTR_HDR_REQTR_ID);
		acNotReturnedSet.add(ATTR_HDR_REQ_REASON);
		acNotReturnedSet.add(ATTR_HDR_SYSTEM);
		acNotReturnedSet.add(ATTR_UI_MOBILE);
		acNotReturnedSet.add(ATTR_UI_FNAME);
		acNotReturnedSet.add(ATTR_UI_USER_ID);
		acNotReturnedSet.add(ATTR_UI_FAX);
		acNotReturnedSet.add(ATTR_UI_TELEPHONE);//TODO - Remove all invalid entries from this set
		acNotReturnedSet.add(ATTR_UI_DEPARTMENT);
		acNotReturnedSet.add(ATTR_UI_LNAME);
		acNotReturnedSet.add(ATTR_UI_EMAIL);
		}
		return acNotReturnedSet;
	}
	
	
	public Properties getUmeACMapProperties(){
		if(isGRC53){
			//TODO - added temporarily
			fileName = "Sapac53ToSapum.properties";
		}
		InputStream inputStream = IOUtil.getResourceAsStream(SAPACUMEConnector.class, fileName);
		Properties properties = new Properties();		
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new ConnectorException("Could not load "+fileName+" file ", e);
		}				
		return properties;
	}
	
	
	private Set<String> getUmeMappedFields(){
		Properties properties = getUmeACMapProperties();
		Set<String> umMappedFields = new HashSet<String>();
		Set<Object> keySet = properties.keySet();
		for (Object key : keySet) {
			umMappedFields.add(key.toString());
		}		
		return umMappedFields;
	}
	

	public Set<String> getAc10attrSet() {
		if(ac10attrSet.size()<1)
			initSchema();
		return ac10attrSet;
	}

	public Set<String> getAc53attrSet() {
		if(ac53attrSet.size()<1)
			initSchema();
		return ac53attrSet;
	}
}
