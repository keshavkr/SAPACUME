package org.identityconnectors.sapacume.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.AttributeInfo.Flags;
import org.identityconnectors.framework.common.objects.AttributeInfoUtil;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.OperationalAttributeInfos;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.sapume.SAPUMEConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for {@link org.identityconnectors.sapacume.SAPACUMESchema} class
 *
 * @author Suresh Kadiyala
 */
public class SAPUMEACSchemaTest{

	private  static Schema _schema;
	private  static final Log LOGGER = Log.getLog(SAPUMEACSchemaTest.class);
	
	private static Set<AttributeInfo> accattrInfos = new HashSet<AttributeInfo>();
	private static Set<AttributeInfo> grpattrInfos = new HashSet<AttributeInfo>();
	private static  Set<AttributeInfo> roleattrInfos = new HashSet<AttributeInfo>();
	private static ConnectorFacade facade = null;
	List<String> attrToGet = new ArrayList<String>();
	
	@BeforeClass
	public  static void testSchema_Attributes() throws Exception {
		LOGGER.info("BEGIN");
		facade = ICFTestHelper.getConnectorFacadeHelperImp();
		_schema = facade.schema();
		Set<ObjectClassInfo> objClassInfo = new HashSet<ObjectClassInfo>();		
		objClassInfo.addAll(_schema.getObjectClassInfo());
		Assert.assertNotNull(objClassInfo);
		Assert.assertTrue(objClassInfo.size() == 3);

		for(ObjectClassInfo objectClassInfo : _schema.getObjectClassInfo()){
			if(objectClassInfo.getType().equalsIgnoreCase("__ACCOUNT__")){
				accattrInfos.addAll(objectClassInfo.getAttributeInfo());

			}else if(objectClassInfo.getType().equalsIgnoreCase("Group")){
				grpattrInfos.addAll(objectClassInfo.getAttributeInfo());

			}else if(objectClassInfo.getType().equalsIgnoreCase("Role")){
				roleattrInfos.addAll(objectClassInfo.getAttributeInfo());
			}

		}
		
	}

	@Test
	public void testRequiredAttrs(){
		LOGGER.info("BEGIN");
		Set<AttributeInfo> requiredAttrInfo = new HashSet<AttributeInfo>();
		requiredAttrInfo.add(AttributeInfoUtil.find(SAPUMEConstants.LASTNAME, accattrInfos));
		requiredAttrInfo.add(AttributeInfoUtil.find(SAPUMEConstants.LOGONNAME, accattrInfos));
				
		for(AttributeInfo attr : requiredAttrInfo) {
			try{
				Assert.assertTrue(attr.isRequired());
			}catch(AssertionError ae){
				LOGGER.info("Error while asserting {0}", attr.getName());
				throw new AssertionError(ae + "while asserting "+ attr.getName());
			}
			displayLogs(attr.getName(),	Flags.REQUIRED);
		}
		requiredAttrInfo.clear();
		
		requiredAttrInfo.add(AttributeInfoUtil.find(SAPUMEConstants.UNIQUENAME,grpattrInfos));
		for(AttributeInfo attr : requiredAttrInfo) {
			try{
				Assert.assertTrue(attr.isRequired());
			}catch(AssertionError ae){
				LOGGER.info("Error while asserting {0}", attr.getName());
				throw new AssertionError(ae + "while asserting "+ attr.getName());
			}
			displayLogs(attr.getName(),	Flags.REQUIRED);
		}
		requiredAttrInfo.clear();
		
		requiredAttrInfo.add(AttributeInfoUtil.find(SAPUMEConstants.UNIQUENAME,roleattrInfos));
		for(AttributeInfo attr : requiredAttrInfo) {
			try{
				Assert.assertTrue(attr.isRequired());
			}catch(AssertionError ae){
				LOGGER.info("Error while asserting {0}", attr.getName());
				throw new AssertionError(ae + "while asserting "+ attr.getName());
			}
			displayLogs(attr.getName(),	Flags.REQUIRED);
		}

		LOGGER.info("END");
	}
	
	@Test
	public void testNotReturnedByDefaultAttrAttrs(){
		LOGGER.info("BEGIN");
		Set<AttributeInfo> nonDefaultReturnAttrInfoSet = new HashSet<AttributeInfo>();
		nonDefaultReturnAttrInfoSet.add(AttributeInfoUtil.find(OperationalAttributeInfos.PASSWORD.getName(), accattrInfos));
		for(AttributeInfo attr : nonDefaultReturnAttrInfoSet) {
			try{
				Assert.assertFalse(attr.isReturnedByDefault());
			}catch(AssertionError ae){
				LOGGER.info("Error while asserting {0}", attr.getName());
				throw new AssertionError(ae + "while asserting "+ attr.getName());
			}
			displayLogs(attr.getName(),	Flags.NOT_RETURNED_BY_DEFAULT);
		}
		LOGGER.info("END");
	}
	
	@Test
	public void testNotReadableAttrs(){
		LOGGER.info("BEGIN");
		Set<AttributeInfo> notReadableAttrsSet = new HashSet<AttributeInfo>();
		notReadableAttrsSet.add(AttributeInfoUtil.find(OperationalAttributeInfos.PASSWORD.getName(), accattrInfos));
		for(AttributeInfo attr : notReadableAttrsSet) {
			try{
				Assert.assertFalse(attr.isReadable());
			}catch(AssertionError ae){
				LOGGER.info("Error while asserting {0}", attr.getName());
				throw new AssertionError(ae + "while asserting "+ attr.getName());
			}
			displayLogs(attr.getName(),	Flags.NOT_READABLE);
		}
		LOGGER.info("END");
	}

	@Test
	public void testNonCreatableAttrs(){
		LOGGER.info("BEGIN");
		Set<AttributeInfo> noncreatableAttrs = new HashSet<AttributeInfo>();
		noncreatableAttrs.add(AttributeInfoUtil.find(SAPUMEConstants.DATASOURCE, accattrInfos));
		noncreatableAttrs.add(AttributeInfoUtil.find(SAPUMEConstants.ALLASSIGNED_ROLES, accattrInfos));
		noncreatableAttrs.add(AttributeInfoUtil.find(SAPUMEConstants.ALLASSIGNED_GROUPS, accattrInfos));

		
		for(AttributeInfo attr : noncreatableAttrs) {
			try{
				Assert.assertFalse(attr.isCreateable());
			}catch(AssertionError ae){
				LOGGER.info("Error while asserting {0}", attr.getName());
				throw new AssertionError(ae + "while asserting "+ attr.getName());
			}
			displayLogs(attr.getName(),	Flags.NOT_CREATABLE);
		}
	}

	@Test
	public void testNonUpdatebleAttrs(){
		LOGGER.info("BEGIN");
		Set<AttributeInfo> nonupdatebleAttrs = new HashSet<AttributeInfo>();
		nonupdatebleAttrs.add(AttributeInfoUtil.find(SAPUMEConstants.LOGONNAME, accattrInfos));
		nonupdatebleAttrs.add(AttributeInfoUtil.find(SAPUMEConstants.DATASOURCE, accattrInfos));
		nonupdatebleAttrs.add(AttributeInfoUtil.find(SAPUMEConstants.ALLASSIGNED_ROLES, accattrInfos));
		nonupdatebleAttrs.add(AttributeInfoUtil.find(SAPUMEConstants.ALLASSIGNED_GROUPS, accattrInfos));
		
		for(AttributeInfo attr : nonupdatebleAttrs) {
			try{
				Assert.assertFalse(attr.isUpdateable());
			}catch(AssertionError ae){
				LOGGER.info("Error while asserting {0}", attr.getName());
				throw new AssertionError(ae + "while asserting "+ attr.getName());
			}
			displayLogs(attr.getName(),	Flags.NOT_UPDATEABLE);
		}
	}
	
	@Test
	public void testMultiValuedAttrs(){
		LOGGER.info("BEGIN");
		Set<AttributeInfo> multiValuedAttrs = new HashSet<AttributeInfo>();
		multiValuedAttrs.add(AttributeInfoUtil.find(SAPUMEConstants.ASSIGNED_GROUPS, accattrInfos));
		multiValuedAttrs.add(AttributeInfoUtil.find(SAPUMEConstants.ASSIGNED_ROLES, accattrInfos));
		
		for(AttributeInfo attr : multiValuedAttrs) {
			try{
				Assert.assertTrue(attr.isMultiValued());
			}catch(AssertionError ae){
				LOGGER.info("Error while asserting {0}", attr.getName());
				throw new AssertionError(ae + "while asserting "+ attr.getName());
			}
			displayLogs(attr.getName(),	Flags.MULTIVALUED);
		}
	}

	private void displayLogs(String attrName,Flags flag){
		LOGGER.info("SUCCESSFULLY CHECKED {0} AS {1} ATTRIBUTE",attrName,	flag);
	}
}
