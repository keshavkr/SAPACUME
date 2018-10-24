package org.identityconnectors.sapacume.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;

import org.identityconnectors.framework.spi.ConfigurationProperty;
import org.identityconnectors.sapacume.SAPACUMEConfiguration;
import org.junit.Assert;
import org.junit.Test;

public class SAPUMEACConfigurationTests extends TestCase{
	
	@Test
	public void defaults() {		


		SAPACUMEConfiguration config = new SAPACUMEConfiguration();
		SAPACUMEConfiguration acConfiguration = new SAPACUMEConfiguration(config);
		//ume setters
		assertNull(acConfiguration.getUmeUserId());
		assertNull(acConfiguration.getUmeUrl());
		assertNull(acConfiguration.getUmePassword()); 
		assertNull(acConfiguration.getDummyPassword());  
		assertNull(acConfiguration.getChangePwdFlag());  
		assertNull(acConfiguration.getPwdHandlingSupport());  
		assertNull(acConfiguration.getLogSPMLRequest());  
		assertNull(acConfiguration.getEnableDate()); 
		assertNull(acConfiguration.getLogonNameInitialSubstring()); 
		assertNull(acConfiguration.getGroupDatasource()); 
		assertNull(acConfiguration.getRoleDatasource());
		//ac setters
		assertNull(acConfiguration.getGrcUsername());
		assertNull(acConfiguration.getGrcPassword()); 
		assertNull(acConfiguration.getGrcLanguage()); 
		assertNotNull(acConfiguration.getIsGRC53());
		assertNull(acConfiguration.getAuditTrailWSDLPath());
		assertNull(acConfiguration.getRequestStatusWSDLPath());
		assertNull(acConfiguration.getSubmitRequestWSDLPath());
		assertNull(acConfiguration.getSelectApplicationWSDLPath());
		assertNull(acConfiguration.getSearchRolesWSDLPath());
		assertNull(acConfiguration.getRoleLookupAccessURL());
		assertNull(acConfiguration.getAppLookupAccessURL());
		assertNull(acConfiguration.getUserAccessAccessURL ());
		assertNull(acConfiguration.getRequestStatusAccessURL());
		assertNull(acConfiguration.getAuditLogsAccessURL());
		assertNull(acConfiguration.getEntitlementRiskAnalysisAccessURL());
		assertNull(acConfiguration.getOtherLookupAccessURL());
		assertNull(acConfiguration.getModifyUserReqType());
		assertNull(acConfiguration.getIgnoreOpenStatus());
		assertNull(acConfiguration.getRequestStatusWS());
		assertNotNull(acConfiguration);
		assertNull(acConfiguration.getCreateUserReqType());
		assertNull(acConfiguration.getEntitlementRiskAnalysisWS());
		assertNull(acConfiguration.getLockUserReqType());
		assertNull(acConfiguration.getUnlockUserReqType()); 
		assertNull(acConfiguration.getRemoveRoleReqType());
		assertNull(acConfiguration.getProvItemActionAttrName());

		assertNull(acConfiguration.getDeleteUserReqType());
		assertNull(acConfiguration.getAssignRoleReqType());
		assertNull(acConfiguration.getAuditLogsWS());
		assertNull(acConfiguration.getProvActionAttrName());

		assertNull(acConfiguration.getLogAuditTrial());

		assertNull(acConfiguration.getRequestTypeAttrName());
		assertNull(acConfiguration.getRoleLookupWS());

		assertNull(acConfiguration.getAppLookupWS());
		assertNull(acConfiguration.getOtherLookupWS());
		assertNull(acConfiguration.getWsdlFilePath());
		assertNull(acConfiguration.getUserAccessWS());



	}
	
	@Test
	public void setters() {

		// Init config parameters
		SAPACUMEConfiguration acConfiguration = SAPUMEACTestUtils.newConfiguration();	
		//ume setters
		assertNotNull(acConfiguration.getUmeUserId());
		assertNotNull(acConfiguration.getUmeUrl());
		assertNotNull(acConfiguration.getUmePassword()); 
		assertNotNull(acConfiguration.getDummyPassword());  
		assertNotNull(acConfiguration.getChangePwdFlag());  
		assertNotNull(acConfiguration.getPwdHandlingSupport());  
		assertNotNull(acConfiguration.getLogSPMLRequest());  
		assertNotNull(acConfiguration.getEnableDate()); 
		assertNotNull(acConfiguration.getLogonNameInitialSubstring()); 
		assertNotNull(acConfiguration.getGroupDatasource()); 
		assertNotNull(acConfiguration.getRoleDatasource());
		//ac setters
		assertNotNull(acConfiguration.getGrcUsername());
		assertNotNull(acConfiguration.getGrcPassword()); 
		assertNotNull(acConfiguration.getGrcLanguage()); 
		assertNotNull(acConfiguration.getIsGRC53());
		assertNotNull(acConfiguration.getAuditTrailWSDLPath());
		assertNotNull(acConfiguration.getRequestStatusWSDLPath());
		assertNotNull(acConfiguration.getSubmitRequestWSDLPath());
		assertNotNull(acConfiguration.getSelectApplicationWSDLPath());
		assertNotNull(acConfiguration.getSearchRolesWSDLPath());
		assertNotNull(acConfiguration.getRoleLookupAccessURL());
		assertNotNull(acConfiguration.getAppLookupAccessURL());
		assertNotNull(acConfiguration.getUserAccessAccessURL ());
		assertNotNull(acConfiguration.getRequestStatusAccessURL());
		assertNotNull(acConfiguration.getAuditLogsAccessURL());
		assertNotNull(acConfiguration.getEntitlementRiskAnalysisAccessURL());
		assertNotNull(acConfiguration.getOtherLookupAccessURL());
		assertNotNull(acConfiguration.getModifyUserReqType());

		assertNotNull(acConfiguration.getIgnoreOpenStatus());
		assertNotNull(acConfiguration.getRequestStatusWS());
		assertNotNull(acConfiguration);
		assertNotNull(acConfiguration.getCreateUserReqType());
		assertNotNull(acConfiguration.getEntitlementRiskAnalysisWS());
		assertNotNull(acConfiguration.getLockUserReqType());
		assertNotNull(acConfiguration.getUnlockUserReqType()); 
		assertNotNull(acConfiguration.getRemoveRoleReqType());
		assertNotNull(acConfiguration.getProvItemActionAttrName());

		assertNotNull(acConfiguration.getDeleteUserReqType());
		assertNotNull(acConfiguration.getAssignRoleReqType());
		assertNotNull(acConfiguration.getAuditLogsWS());
		assertNotNull(acConfiguration.getProvActionAttrName());


		assertNotNull(acConfiguration.getRequestTypeAttrName());
		assertNotNull(acConfiguration.getRoleLookupWS());

		assertNotNull(acConfiguration.getAppLookupWS());
		assertNotNull(acConfiguration.getOtherLookupWS());
		assertNotNull(acConfiguration.getWsdlFilePath());
		assertNotNull(acConfiguration.getUserAccessWS());


	}
	
	
	@Test
	public void testConfigurationalPropsAnnotations() {	
	
		SAPACUMEConfiguration cfg = new SAPACUMEConfiguration();
		
		Method[] md = cfg.getClass().getMethods();
		int gettersCounter = 0;
		
		Collection<String> confidentialmethodSet = Arrays.asList("getGrcPassword","getDummyPassword","getUmePassword");
		Collection<String> requiredMethodSet =  Arrays.asList("getGrcUsername", "getGrcPassword", "getGrcLanguage","getUmeUserId","getChangePwdFlag","getPwdHandlingSupport","getEnableDate","getLogonNameInitialSubstring","getUmeUrl","getUmePassword","getDummyPassword");

		Collection<String> objclassesmethodSet = Arrays.asList("getUserAccessWS","getWsdlFilePath","getOtherLookupWS","getAppLookupWS","getRoleLookupWS","getRequestTypeAttrName","getLogAuditTrial","getProvActionAttrName","getAuditLogsWS","getAssignRoleReqType","getDeleteUserReqType","getProvItemActionAttrName","getRemoveRoleReqType","getUnlockUserReqType","getLockUserReqType","getEntitlementRiskAnalysisWS","getCreateUserReqType","getRequestStatusWS","getIgnoreOpenStatus"
				,"getModifyUserReqType","getOtherLookupAccessURL","getAuditLogsAccessURL","getRequestStatusAccessURL","getUserAccessAccessURL","getAppLookupAccessURL","getRoleLookupAccessURL","getSearchRolesWSDLPath","getSelectApplicationWSDLPath", "getSubmitRequestWSDLPath","getAuditTrailWSDLPath");
		
		for (Method method : md) {
			try {
				
				 Annotation[] annotations = method.getDeclaredAnnotations();
				
				
				for (Annotation annotation : annotations) {
					
					if (annotation instanceof ConfigurationProperty) {
						ConfigurationProperty myAnnotation = (ConfigurationProperty) annotation;
						gettersCounter++;
						// check for Object Classes
						if (objclassesmethodSet.contains(method.getName()))
							Assert.assertEquals(myAnnotation.objectClasses().length, 1);
						else
							// Assert.assertEquals(myAnnotation.objectClasses().length, 1); 
						// check for Confidential Tags
						if (confidentialmethodSet.contains(method.getName()))
							Assert.assertTrue(myAnnotation.confidential());
						else
							Assert.assertFalse(myAnnotation.confidential());

						// Check for Required Tags
						if (requiredMethodSet.contains(method.getName()))
							Assert.assertTrue(myAnnotation.required());
						else
							Assert.assertFalse(myAnnotation.required());
					}
				}
				
				
			} catch (AssertionError ae) {
				throw new AssertionError(ae + " : Error while asserting  "+ method.getName());
			}
			
		}
		//check for getter methods counts
		
		assertTrue(gettersCounter == 47 );
	}	
	@Test
	public void testValidate() {	
	
		SAPACUMEConfiguration acConfiguration = SAPUMEACTestUtils.newConfiguration();	
		acConfiguration.validate();
		
	}
	
}