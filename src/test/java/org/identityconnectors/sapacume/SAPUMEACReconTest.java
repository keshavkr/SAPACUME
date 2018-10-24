/**
 * 
 */
package org.identityconnectors.sapacume.test;

import static org.identityconnectors.sapacume.SAPACUMEConstants.PATTERN_REQ_NO;
import static org.identityconnectors.sapacume.SAPACUMEConstants.PATTERN_REQ_TYPE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterBuilder;
import org.identityconnectors.sapacume.SAPACUMEConfiguration;
import org.junit.After;
import org.junit.Before;
//import org.junit.FixMethodOrder;
import org.junit.Test;
//import org.junit.runners.MethodSorters;

/**
 * @author Suresh Kadiyala
 *
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SAPUMEACReconTest extends TestCase {
	private static final Log LOGGER = Log.getLog(SAPUMEACReconTest.class);
	List<String> attrToGet = new ArrayList<String>();
	private static ConnectorFacade facade = null;
	SAPACUMEConfiguration config;

	@Before
	public void setUp() throws Exception {
		config = SAPUMEACTestUtils.newConfiguration();
		facade = ICFTestHelper.getConnectorFacadeHelperImp();
		attrToGet.add("RequestStatus");
		attrToGet.add("__UID__");
		attrToGet.add("UM IT Resource Name");
		attrToGet.add("RequestType");
		attrToGet.add("User ID");

	}
	
	@After
	public void tearDown() {
		// no-op as there are no consumed resources
	}
	

	@Test
	public void testAC_Roles_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			facade.search(new ObjectClass("ROLE"), null, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_Roles_Recon() excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_Roles_Recon() failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
	}
	@Test
	public void testAC_PriorityType_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			facade.search(new ObjectClass("PriorityType"), null, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_PriorityType_Recon() excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_PriorityType_Recon() failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
	}
	
	@Test
	public void testAC_ReconStatus_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			Uid uid = new Uid("4920 & User ID=ACTESTU2000 & __UID__=ACTESTU2000 &  ReqType='New Account' & logonname='ACTESTU2000'");
			builder.setOption("UM IT Resource Name", "SAP UM AC IT Resource");
			builder.setAttributesToGet(attrToGet);
	        facade.getObject(new ObjectClass("STATUS"), uid, builder.build());
	        assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_ReconStatus_Recon() excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_ReconStatus_Recon() failed "+e.getMessage());
			throw e;
		} finally {
			LOGGER.info("END");
		}
	}
	
	
	@Test
	public void testAC_FunctionArea_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			builder.setAttributesToGet(attrToGet);
			
			facade.search(new ObjectClass("FunctionArea"), null, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_FunctionArea_Recon() excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_FunctionArea_Recon() failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
	}
	
	@Test
	public void testAC_ItemProvActionType_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			builder.setAttributesToGet(attrToGet);
		
			facade.search(new ObjectClass("ItemProvActionType"), null, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_ItemProvActionType_Recon() excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_ItemProvActionType_Recon() failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
	}
	
	@Test
	public void testAC_BusProc_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			builder.setAttributesToGet(attrToGet);
			
			facade.search(new ObjectClass("BusProc"), null, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_BusProc_Recon() excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_BusProc_Recon() failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
	}
	
	@Test
	public void testAC_ReqInitSystem_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			builder.setAttributesToGet(attrToGet);
			facade.search(new ObjectClass("SYSTEM"), null, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_ReqInitSystem_Recon() excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_ReqInitSystem_Recon() failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
	}
	
	@Test
	public void testAC_Role_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			builder.setAttributesToGet(attrToGet);
			
			facade.search(new ObjectClass("activityGroups"), null, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_Role_Recon() excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_Role_Recon() failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
	}
	@Test
	public void testAC_UserId_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();	
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			builder.setAttributesToGet(attrToGet);
			Filter filter = FilterBuilder.equalTo(AttributeBuilder.build( "id", "ACTESTU2000" + PATTERN_REQ_NO +"4927"+ PATTERN_REQ_TYPE +"Ok"));
			facade.search(ObjectClass.ACCOUNT, filter, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_UserId_Recon excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_UserId_Recon failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
	}
	@Test
	public void testAC_UserName_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			builder.setAttributesToGet(attrToGet);
			Filter filter = FilterBuilder.equalTo(AttributeBuilder.build( "logonname", "ACTESTU2000"));
			facade.search(ObjectClass.ACCOUNT, filter, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_UserName_Recon excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_UserName_Recon failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
	}
	
	@Test
	public void testAC_Create_Recon() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
			OperationOptionsBuilder builder = new OperationOptionsBuilder();
			builder.setAttributesToGet(attrToGet);
			Filter filter = FilterBuilder.equalTo(AttributeBuilder.build( "id", "ACTESTU2000" + PATTERN_REQ_NO +"4927"+ PATTERN_REQ_TYPE +"New"));
			facade.search(ObjectClass.ACCOUNT, filter, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testAC_Create_Recon excuted successfully");
		} catch(Exception e){
			LOGGER.error("testAC_Create_Recon failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
	}
	@Test
	public void testGreaterThanFilter_User() throws Exception {
		LOGGER.info("BEGIN");
		DummyResultHandler handler = new DummyResultHandler();
		try{
					OperationOptionsBuilder builder = new OperationOptionsBuilder();
					builder.setAttributesToGet(attrToGet);
					Set<Attribute> attrSet = new HashSet<Attribute>();
					TestCase.assertNotNull(attrSet);
			Filter filter =FilterBuilder.greaterThan(AttributeBuilder.build("Last Updated","20150603000000"));

			facade.search(ObjectClass.ACCOUNT, filter, handler, builder.build());
			assertTrue(handler.getUserCount()>0);
			LOGGER.ok("testGreaterThanFilter_User() excuted successfully");
		} catch(Exception e){
			LOGGER.error("testGreaterThanFilter_User failed "+e.getMessage());
			throw e;
		} finally {
			handler = null;
			LOGGER.info("END");
		}
		LOGGER.info("END");
	}
	
	
}
