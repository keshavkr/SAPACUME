package org.identityconnectors.sapacume.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.AttributeInfoUtil;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.sapacume.SAPACUMEConfiguration;
import org.identityconnectors.sapacume.SAPACUMEConnector;
import org.identityconnectors.sapume.SAPUMEConfiguration;
import org.identityconnectors.sapume.SAPUMEConnection;
import org.identityconnectors.test.common.PropertyBag;
import org.identityconnectors.test.common.TestHelpers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for {@link org.identityconnectors.sapacume.SAPACUMEConnector} class
 *
 * @author Suresh Kadiyala
 */
public class SAPUMEACUpdateTest extends TestCase {
	private static final Log LOGGER = Log.getLog(SAPUMEACUpdateTest.class);
	List<String> attrToGet = new ArrayList<String>();
	private Schema _schema;
	private static Map<String, AttributeInfo> _accountAttributeMap;
	private static Set<String> _accountAttributeNames;
	private static Set<String> _groupAttributeNames;
	private static Map<String, AttributeInfo> _groupAttributeMap;
	private static Set<String> _roleAttributeNames;
	private static Map<String, AttributeInfo> _roleAttributeMap;
	private static ConnectorFacade facade=null;
	SAPACUMEConfiguration config = null;
	
	@Before
	public void setUp() throws Exception {
		config  = SAPUMEACTestUtils.newConfiguration();
		facade =ICFTestHelper.getConnectorFacadeHelperImp();
		//calling schema
		_schema = facade.schema();
		for(ObjectClassInfo objectClassInfo : _schema.getObjectClassInfo()){
			if(objectClassInfo.getType().equalsIgnoreCase("__ACCOUNT__")){
				_accountAttributeMap = AttributeInfoUtil.toMap(objectClassInfo.getAttributeInfo());
				_accountAttributeNames = _accountAttributeMap.keySet();
			}else if(objectClassInfo.getType().equalsIgnoreCase("Group")){
				_groupAttributeMap = AttributeInfoUtil.toMap(objectClassInfo.getAttributeInfo());
				_groupAttributeNames = _groupAttributeMap.keySet();
			}else	if(objectClassInfo.getType().equalsIgnoreCase("Role")){
				_roleAttributeMap = AttributeInfoUtil.toMap(objectClassInfo.getAttributeInfo());
				_roleAttributeNames = _roleAttributeMap.keySet();
			}

		}
		attrToGet.addAll(_accountAttributeNames);
		attrToGet.addAll(_groupAttributeNames);
		attrToGet.addAll(_roleAttributeNames);

	}

	@After
	public void tearDown() {
		facade = null;
		config = null;
		attrToGet = null;
		_roleAttributeNames = null;
		_groupAttributeNames = null;
		_accountAttributeNames = null;
	}
	
	@Test
	public void testUpdate_Ac10Attribute() throws Exception{
		LOGGER.info("BEGIN");
		Uid uid = null;
		try {
			Set<Attribute> replaceAttr = toAttributeSet(true, false);
			replaceAttr.add(AttributeBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT, replaceAttr));
			assertNotNull(replaceAttr);
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			Iterator<Attribute> itr = replaceAttr.iterator();
			while (itr.hasNext()) {
				Attribute attr = itr.next();
				if (attr.getName().toString().equalsIgnoreCase(Name.NAME) || attr.getName().toString().equalsIgnoreCase(Uid.NAME))
					uid = new Uid(attr.getValue().get(0).toString());
			}

			replaceAttr.remove(AttributeBuilder.build("__NAME__", "USER.PRIVATE_DATASOURCE.un:ACTESTU2000~*~4927~Ok"));
			uid = facade.update(ObjectClass.ACCOUNT, uid, replaceAttr, builder.build());
			assertTrue(uid.getValue().toString().contains("Change Account"));
			
		} catch (Exception e) {
			LOGGER.error("testUpdate_Ac10Attribute failed "+e.getMessage());
			throw e;
		}
		LOGGER.info("END");
	}
	@Test
	public void testUpdate_Ac10UnLock() throws Exception{
		LOGGER.info("BEGIN");
		Uid uid = null;
		try {
			Set<Attribute> replaceAttr = toAttributeSet(true, false);
			replaceAttr.add(AttributeBuilder.build("userLock;None", "0"));
			replaceAttr.add(AttributeBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT, replaceAttr));
			assertNotNull(replaceAttr);
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			Iterator<Attribute> itr = replaceAttr.iterator();
			while (itr.hasNext()) {
				Attribute attr = itr.next();
				if (attr.getName().toString().equalsIgnoreCase(Name.NAME) || attr.getName().toString().equalsIgnoreCase(Uid.NAME))
					uid = new Uid(attr.getValue().get(0).toString());
			}
			replaceAttr.remove(AttributeBuilder.build("__NAME__", "USER.PRIVATE_DATASOURCE.un:ACTESTU2000~*~4927~Ok"));
			uid = facade.update(ObjectClass.ACCOUNT, uid, replaceAttr, builder.build());
		     assertTrue(uid.getUidValue().toString().contains("unlock user"));
			
		} catch (Exception e) {
			LOGGER.error("testUpdate_Ac10UnLock failed "+e.getMessage());
			throw e;
		}
		LOGGER.info("END");
	}
	
	@Test
	public void testUpdate_Ac10Lock() throws Exception{
		LOGGER.info("BEGIN");
		Uid uid = null;
		try {
			Set<Attribute> replaceAttr = toAttributeSet(true, false);
			replaceAttr.add(AttributeBuilder.build("userLock;None", "1"));
			replaceAttr.add(AttributeBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT, replaceAttr));
			assertNotNull(replaceAttr);
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			Iterator<Attribute> itr = replaceAttr.iterator();
			while (itr.hasNext()) {
				Attribute attr = itr.next();
				if (attr.getName().toString().equalsIgnoreCase(Name.NAME) || attr.getName().toString().equalsIgnoreCase(Uid.NAME))
					uid = new Uid(attr.getValue().get(0).toString());
			}
			replaceAttr.remove(AttributeBuilder.build("__NAME__", "USER.PRIVATE_DATASOURCE.un:ACTESTU2000~*~4927~Ok"));
			uid = facade.update(ObjectClass.ACCOUNT, uid, replaceAttr, builder.build());
		    assertTrue(uid.getValue().contains("Lock Account"));
		
		} catch (Exception e) {
			LOGGER.error("testUpdate_Ac10Lock failed "+e.getMessage());
			throw e;
		}
		LOGGER.info("END");
	}
	
	
	
	
	@Test
	public void testUpdate_acEnable() throws Exception{
		LOGGER.info("BEGIN");
		Uid uid = null;
		try {
			Set<Attribute> replaceAttr = toAttributeSet(true, false);
			replaceAttr.add(AttributeBuilder.build(OperationalAttributes.ENABLE_NAME, true));
			replaceAttr.add(AttributeBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT, replaceAttr));
			assertNotNull(replaceAttr);
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			Iterator<Attribute> itr = replaceAttr.iterator();
			while (itr.hasNext()) {
				Attribute attr = itr.next();
				if (attr.getName().toString().equalsIgnoreCase(Name.NAME) || attr.getName().toString().equalsIgnoreCase(Uid.NAME))
					uid = new Uid(attr.getValue().get(0).toString());
			}
			replaceAttr.remove(AttributeBuilder.build("__NAME__", "USER.PRIVATE_DATASOURCE.un:ACTESTU2000~*~4927~Ok"));
			uid = facade.update(ObjectClass.ACCOUNT, uid, replaceAttr, builder.build());
			assertTrue(uid.getValue().toString().contains("Change Account"));
	
			
		} catch (Exception e) {
			LOGGER.error("testUpdate_AcEnableCheck failed "+e.getMessage());
			throw e;
		}
		LOGGER.info("END");
	}
	@Test
	public void testUpdate_acDisable() throws Exception{
		LOGGER.info("BEGIN");
		Uid uid = null;
		try {
			Set<Attribute> replaceAttr = toAttributeSet(true, false);
			replaceAttr.add(AttributeBuilder.build(OperationalAttributes.ENABLE_NAME,false));
			replaceAttr.add(AttributeBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT, replaceAttr));
			assertNotNull(replaceAttr);
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			Iterator<Attribute> itr = replaceAttr.iterator();
			while (itr.hasNext()) {
				Attribute attr = itr.next();
				if (attr.getName().toString().equalsIgnoreCase(Name.NAME) || attr.getName().toString().equalsIgnoreCase(Uid.NAME))
					uid = new Uid(attr.getValue().get(0).toString());
			}

			replaceAttr.remove(AttributeBuilder.build("__NAME__", "USER.PRIVATE_DATASOURCE.un:ACTESTU2000~*~4927~Ok"));
			uid = facade.update(ObjectClass.ACCOUNT, uid, replaceAttr, builder.build());
			assertTrue(uid.getValue().toString().contains("Change Account"));
		} catch (Exception e) {
			LOGGER.error("testUpdate_AcDisableCheck failed "+e.getMessage());
			throw e;
		}
		LOGGER.info("END");
	}
	
	@Test(expected = ConnectorException.class)
	public void testAdd_inValidChildData_OneRole() throws Exception {
		LOGGER.info("BEGIN");
		
		try{
			Uid uid = null;
			Set<Attribute> replaceAttr = toAttributeSet(true, false);
            assertNotNull(replaceAttr);
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			for (Attribute attr : replaceAttr) {
				if (attr.getName().toString().equalsIgnoreCase(Name.NAME) || 
						attr.getName().toString().equalsIgnoreCase(Uid.NAME))
					uid = new Uid(attr.getValue().get(0).toString());
			}
			assertNotNull(uid);
		
			replaceAttr.remove(AttributeBuilder.build("__NAME__", "USER.PRIVATE_DATASOURCE.un:ACTESTU2000~*~4927~Ok"));
			replaceAttr.add(AttributeBuilder.build("roleId","sdsdsdsdd"));
			replaceAttr.add(AttributeBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT, replaceAttr));
			uid = facade.addAttributeValues(ObjectClass.ACCOUNT, uid, replaceAttr, builder.build());
			
		} 
		catch (Exception e) {
			LOGGER.error("testAdd_inValidChildData_OneRole failed "+e.getMessage());
		}
		LOGGER.info("END");
	}
	
	@Test
	public void testRemove_ChildData_OneRole() throws Exception {
		LOGGER.info("BEGIN");
		try{
			Uid uid = null;
			Set<Attribute> replaceAttr = toAttributeSet(true, false);
            assertNotNull(replaceAttr);
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			for (Attribute attr : replaceAttr) {
				if (attr.getName().toString().equalsIgnoreCase(Name.NAME) || 
						attr.getName().toString().equalsIgnoreCase(Uid.NAME))
					uid = new Uid(attr.getValue().get(0).toString());
			}
			assertNotNull(uid);
			replaceAttr.remove(AttributeBuilder.build("__NAME__", "USER.PRIVATE_DATASOURCE.un:ACTESTU2000~*~4927~Ok"));
			replaceAttr.add(AttributeBuilder.build("roleId","ROLE.UME_ROLE_PERSISTENCE.un:NWA_SUPERADMIN"));
			replaceAttr.add(AttributeBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT, replaceAttr));
			uid = facade.removeAttributeValues(ObjectClass.ACCOUNT, uid, replaceAttr, builder.build());
			assertTrue(uid.getValue().toString().contains("Change Account"));
			
		} catch (Exception e) {
			LOGGER.error("testRemove_ChildData_OneRolefailed "+e.getMessage());
			throw e;
		}
		LOGGER.info("END");
	}
	@Test
	public void testAdd_ChildData_OneRole() throws Exception {
		LOGGER.info("BEGIN");
		
		try{
			Uid uid = null;
			Set<Attribute> replaceAttr = toAttributeSet(true, false);
            assertNotNull(replaceAttr);
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			for (Attribute attr : replaceAttr) {
				if (attr.getName().toString().equalsIgnoreCase(Name.NAME) || 
						attr.getName().toString().equalsIgnoreCase(Uid.NAME))
					uid = new Uid(attr.getValue().get(0).toString());
			}
			assertNotNull(uid);
			replaceAttr.remove(AttributeBuilder.build("__NAME__", "USER.PRIVATE_DATASOURCE.un:ACTESTU2000~*~4927~Ok"));
			replaceAttr.add(AttributeBuilder.build("roleId","ROLE.UME_ROLE_PERSISTENCE.un:NWA_SUPERADMIN"));
			replaceAttr.add(AttributeBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT, replaceAttr));
			uid = facade.addAttributeValues(ObjectClass.ACCOUNT, uid, replaceAttr, builder.build());
			assertTrue(uid.getValue().toString().contains("Change Account"));
			
		} 
		catch (Exception e) {
			LOGGER.error("testAdd_ChildData_OneRole failed "+e.getMessage());
			throw e;
		}
		LOGGER.info("END");
	}
	public Set<Attribute> toAttributeSet(boolean addName,
			boolean passwdColDefined) {
		LOGGER.info("BEGIN");
		Set<Attribute> ret = new HashSet<Attribute>();
		Map<String, Object> map = toMap(passwdColDefined);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Collection) {
				ret.add(AttributeBuilder.build(entry.getKey(),
						(Collection) entry.getValue()));
			}
			if (entry.getValue() instanceof GuardedString) {
				GuardedString newPassword = (GuardedString) entry.getValue();
				String sPassword = new SAPUMEConnection(
						new SAPUMEConfiguration()).decode(newPassword);

				ret.add(AttributeBuilder.build("password", sPassword));
			} else {
				ret.add(AttributeBuilder.build(entry.getKey(), (String) entry
						.getValue()));
			}
		}
		LOGGER.info("END");
		return ret;
	}

	Map<String, Object> toMap(boolean passwdColDefined) {
		LOGGER.info("BEGIN");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			PropertyBag prop = TestHelpers.getProperties(SAPACUMEConnector.class);
			for (String attrName : attrToGet) {
				String sAttr = "update." + attrName;
				try {
					if (prop.getStringProperty(sAttr) != null) {
						if (sAttr.contains("__PASSWORD__")) {
							GuardedString gspwd = new GuardedString(prop
									.getStringProperty(sAttr).toCharArray());
							map.put(attrName, gspwd);
						} else {
							map.put(attrName, prop.getStringProperty(sAttr));
						}
						
					}
				} catch (Exception e) {	}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER.info("END");
		return map;
	}
}
