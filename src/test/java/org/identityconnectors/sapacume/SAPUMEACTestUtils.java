package org.identityconnectors.sapacume.test;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.sapacume.SAPACUMEConfiguration;
import org.identityconnectors.sapacume.SAPACUMEConnector;
import org.identityconnectors.test.common.PropertyBag;
import org.identityconnectors.test.common.TestHelpers;

/**
 *  Utility methods for Unit tests
 *
 * @author Suresh Kadiyala
 */
public class SAPUMEACTestUtils {

	private static final Log LOGGER = Log.getLog(SAPUMEACTestUtils.class);
	
	/**
	 * Set values for all the config parameters
	 * @return SAPACUMEConfiguration
	 */

	public static SAPACUMEConfiguration newConfiguration() {
		SAPACUMEConfiguration config = new SAPACUMEConfiguration();
		boolean changePwdFlag= false;
		boolean logSPMLRequest= false;
		boolean pwdHandlingSupport= false;
		try {
			PropertyBag properties = TestHelpers.getProperties(SAPACUMEConnector.class);
			config.setUmeUserId(properties.getStringProperty("connector.umeUserId")); 
			config.setUmeUrl(properties.getStringProperty("connector.umeUrl"));
			GuardedString umePassword = properties.getProperty("connector.umePassword",GuardedString.class);
			config.setUmePassword(umePassword);
			config.setDummyPassword(properties.getProperty("connector.dummyPassword",GuardedString.class));
			boolean changePaawordFlag = 	properties.getProperty("connector.changePwdFlag",Boolean.class);
			if (changePaawordFlag==true){
				changePwdFlag= true;
				config.setChangePwdFlag(changePwdFlag);
			}else if(changePaawordFlag==false){
				config.setChangePwdFlag(changePwdFlag);
			}
			boolean passwordhanSupport = 	properties.getProperty("connector.pwdHandlingSupport",Boolean.class);
			if (passwordhanSupport==true){
				pwdHandlingSupport= true;
				config.setPwdHandlingSupport(pwdHandlingSupport);
			}else if(passwordhanSupport==false){
				config.setPwdHandlingSupport(pwdHandlingSupport);
			}
			boolean lofSpmlRequest = 	properties.getProperty("connector.logSPMLRequest",Boolean.class); 
			if (lofSpmlRequest==true){
				logSPMLRequest= true;
				config.setLogSPMLRequest(logSPMLRequest);
			}else if(lofSpmlRequest==false){
				config.setLogSPMLRequest(logSPMLRequest);
			}
			config.setEnableDate(properties
					.getStringProperty("connector.enableDate"));
			String groupDatasource[]=properties.getProperty("connector.groupDatasource",String[].class);
			config.setGroupDatasource(groupDatasource);
			String roleDatasource[]=properties.getProperty("connector.roleDatasource",String[].class);
			config.setRoleDatasource(roleDatasource);
			config.setLogonNameInitialSubstring(properties.getStringProperty("connector.logonNameInitialSubstring"));
			config.setUserAccessWS(properties.getStringProperty("connector.userAccessWS"));
			config.setCreateUserReqType(properties.getStringProperty("connector.createUserReqType"));
			config.setModifyUserReqType(properties.getStringProperty("connector.modifyUserReqType"));
			config.setWsdlFilePath(properties.getStringProperty("connector.wsdlFilePath"));
			config.setUserAccessAccessURL(properties.getStringProperty("connector.userAccessAccessURL"));
			config.setAuditLogsAccessURL(properties.getStringProperty("connector.auditLogsAccessURL"));
			config.setAuditLogsWS(properties.getStringProperty("connector.auditLogsWS"));
			config.setRequestTypeAttrName(properties.getStringProperty("connector.requestTypeAttrName"));
			config.setProvActionAttrName(properties.getStringProperty("connector.provActionAttrName"));
			config.setProvItemActionAttrName(properties.getStringProperty("connector.provItemActionAttrName"));
			config.setRequestStatusWS(properties.getStringProperty("connector.requestStatusWS"));
			config.setRequestStatusAccessURL(properties.getStringProperty("connector.requestStatusAccessURL"));
			config.setUnlockUserReqType(properties.getStringProperty("connector.unlockUserReqType"));
			config.setLockUserReqType(properties.getStringProperty("connector.lockUserReqType"));
			config.setOtherLookupAccessURL(properties.getStringProperty("connector.otherLookupAccessURL"));
			config.setOtherLookupWS(properties.getStringProperty("connector.otherLookupWS"));
			config.setGrcLanguage(properties.getStringProperty("connector.grcLanguage"));
			config.setGrcPassword(properties.getProperty("connector.grcPassword",GuardedString.class));
			config.setGrcUsername(properties.getStringProperty("connector.grcUsername"));
			config.setRoleLookupAccessURL(properties.getStringProperty("connector.roleLookupAccessURL"));
			config.setAppLookupAccessURL(properties.getStringProperty("connector.appLookupAccessURL"));
			config.setEntitlementRiskAnalysisAccessURL(properties.getStringProperty("connector.entitlementRiskAnalysisAccessURL"));
			config.setEntitlementRiskAnalysisWS(properties.getStringProperty("connector.entitlementRiskAnalysisWS"));
			config.setRoleLookupWS(properties.getStringProperty("connector.roleLookupWS"));
			config.setAppLookupWS(properties.getStringProperty("connector.appLookupWS"));
			config.setAssignRoleReqType(properties.getStringProperty("connector.assignRoleReqType"));
			config.setRemoveRoleReqType(properties.getStringProperty("connector.removeRoleReqType"));
			config.setDeleteUserReqType(properties.getStringProperty("connector.deleteUserReqType"));
			config.setAuditTrailWSDLPath(properties.getStringProperty("connector.auditTrailWSDLPath")); 
			config.setRequestStatusWSDLPath(properties.getStringProperty("connector.requestStatusWSDLPath")); 
			config.setSubmitRequestWSDLPath(properties.getStringProperty("connector.submitRequestWSDLPath")); 
			config.setSelectApplicationWSDLPath(properties.getStringProperty("connector.selectApplicationWSDLPath")); 
			config.setSearchRolesWSDLPath(properties.getStringProperty("connector.searchRolesWSDLPath")); 
			config.setIgnoreOpenStatus(properties.getStringProperty("connector.ignoreOpenStatus"));  
			config.setLogAuditTrial(properties.getStringProperty("connector.logAuditTrial"));  
		}catch (Exception e) {
			LOGGER.error("newConfiguration() failed", e.getMessage());
		}
		return config;
	}
}

