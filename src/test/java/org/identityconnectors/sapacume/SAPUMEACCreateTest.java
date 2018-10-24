package org.identityconnectors.sapacume.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.AttributeInfoUtil;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
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
public class SAPUMEACCreateTest extends TestCase {
	private static final Log LOGGER = Log.getLog(SAPUMEACCreateTest.class);
	List<String> attrToGet = new ArrayList<String>();
	private Schema _schema;
	private static Map<String, AttributeInfo> _accountAttributeMap;
	private static Set<String> _accountAttributeNames;
	private static Set<String> _groupAttributeNames;
	private static Map<String, AttributeInfo> _groupAttributeMap;
	private static Set<String> _roleAttributeNames;
	private static Map<String, AttributeInfo> _roleAttributeMap;
	private static ConnectorFacade facade=null;
	SAPACUMEConfiguration config  = null;
	@Before
	public void setUp() throws Exception {
		config = SAPUMEACTestUtils.newConfiguration();
        facade = ICFTestHelper.getConnectorFacadeHelperImp();
        //calling schema
        _schema = facade.schema();
		for(ObjectClassInfo objectClassInfo : _schema.getObjectClassInfo()){
			if(objectClassInfo.getType().equalsIgnoreCase("__ACCOUNT__")){

				_accountAttributeMap = AttributeInfoUtil.toMap(objectClassInfo.getAttributeInfo());
				_accountAttributeNames = _accountAttributeMap.keySet();
			}else if(objectClassInfo.getType().equalsIgnoreCase("Group")){

				_groupAttributeMap = AttributeInfoUtil.toMap(objectClassInfo.getAttributeInfo());
				_groupAttributeNames = _groupAttributeMap.keySet();
			}else if(objectClassInfo.getType().equalsIgnoreCase("Role")){

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
	public void testCreate_Account() throws Exception {
		LOGGER.info("BEGIN");
		Uid uid = null;
		try {
			Set<Attribute> attrSet = toAttributeSet(true, false);
			assertNotNull(attrSet);
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			attrSet.add(AttributeBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT, attrSet));
			uid = facade.create(ObjectClass.ACCOUNT,  attrSet, builder.build());
			assertNotNull(uid);
			
		} catch (Exception e) {
			LOGGER.error("testCreate_Account failed "+e.getMessage());
			throw e;
		}
		LOGGER.info("END");
	}
	
	@Test 
    public void testTestMethod(){           
            
		facade.test(); 
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
				String sAttr = "__ACCOUNT__." + attrName;
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
			LOGGER.error("toMap failed "+e.getMessage());
		}
		LOGGER.info("END");
		return map;
	}
}
