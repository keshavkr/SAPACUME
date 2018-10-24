package org.identityconnectors.sapacume.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfoUtil;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.sapacume.SAPACUMEConfiguration;
import org.identityconnectors.sapume.SAPUMEConfiguration;
import org.identityconnectors.sapume.SAPUMEConnector;
import org.identityconnectors.test.common.PropertyBag;
import org.identityconnectors.test.common.TestHelpers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link org.identityconnectors.sapume.Query} class
 *
 * @author Chellappan Sampath
 */
public class SAPUMEACReconciliationTest extends TestCase{
	private static final Log LOGGER = Log.getLog(SAPUMEACReconciliationTest.class);
	List<String> attrToGet = new ArrayList<String>();
	private static ConnectorFacade facade = null;
	
	@Before
	public void setUp() throws Exception {
		SAPACUMEConfiguration config = SAPUMEACTestUtils.newConfiguration();
        facade =ICFTestHelper.getConnectorFacadeHelperImp();
        
	}

	@After
	public void tearDown() {
		// no-op as there are no consumed resources
	}
	
	@Test 
	public void testFullReconciliation() throws Exception {
		LOGGER.info("BEGIN");
		try {
			ResultsHandler handler = new ResultsHandler() {
				public boolean handle(ConnectorObject obj) {
					// TODO Auto-generated method stub
					return true;
				}
			};
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			attrToGet.add("telephone");
			attrToGet.add("logonname");
			attrToGet.add("Uid");
			builder.setAttributesToGet(attrToGet);
			facade.search(ObjectClass.ACCOUNT, null, handler, builder.build());
		} catch(Exception e) {
			LOGGER.error("testFullReconciliation() failed "+e.getMessage());
			throw e;
		}
		LOGGER.info("END");
	}


}   
