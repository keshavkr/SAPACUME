/**
 *	<br>Modification History:</br>
 *  S.No.                 Date                              Bug fix no.
 *  1. JAGADEESH        14 August 2013                     Bug 17288932 - oim to sap connection over ssl and lb with web dispatcher 
 */
package org.identityconnectors.sapacume;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.spi.ConfigurationProperty;
import org.identityconnectors.sapume.SAPUMEConfiguration;


/**
 * @author ranjith.kumar
 *
 */
public class SAPACUMEConfiguration extends SAPUMEConfiguration {
	private static final Log log = Log.getLog(SAPACUMEConfiguration.class);
	//******SAP AC IT Resource parameters******//

	private String grcUsername;
	private GuardedString grcPassword;
	private String grcLanguage;
		
	//******SAP AC Configuration lookup parameters******//
	
	public boolean isGRC53 = false;
	
	//Web service client name configurations
	private String userAccessWS;
	private String requestStatusWS;
	private String auditLogsWS;
	private String entitlementRiskAnalysisWS;
	private String roleLookupWS;
	private String appLookupWS;
	private String otherLookupWS;
	
	private String userAccessAccessURL;
	private String requestStatusAccessURL;
	private String auditLogsAccessURL;
	private String entitlementRiskAnalysisAccessURL;
	private String roleLookupAccessURL;
	private String appLookupAccessURL;
	private String otherLookupAccessURL;
	
	//Access Request Management configurations
	private String createUserReqType;
	private String modifyUserReqType;
	private String lockUserReqType;
	private String unlockUserReqType;
	private String deleteUserReqType;
	private String assignRoleReqType;
	private String removeRoleReqType;
	private String ignoreOpenStatus;
	private String logAuditTrial;
	
	private String requestTypeAttrName;
	private String provActionAttrName;
	private String provItemActionAttrName;
	
	private String wsdlFilePath;

	//Start :: BUG 17288932
	private String auditTrailWSDLPath;
	private String requestStatusWSDLPath;
	private String submitRequestWSDLPath;
	private String selectApplicationWSDLPath;
	private String searchRolesWSDLPath;
	//private String requestDetailsWS;
	//private String  requestDetailsAccessURL;
	//End :: BUG 17288932
	
	/**
	 * 
	 */
	public SAPACUMEConfiguration(){
		super();
	}
	
	public SAPACUMEConfiguration(SAPACUMEConfiguration sapConfig){

		grcUsername = sapConfig.getGrcUsername();
		grcPassword = sapConfig.getGrcPassword();
		grcLanguage = sapConfig.getGrcLanguage();
		
		isGRC53 = sapConfig.getIsGRC53();		
		
		userAccessWS = sapConfig.getUserAccessWS();
		requestStatusWS = sapConfig.getRequestStatusWS();
		auditLogsWS = sapConfig.getAuditLogsWS();
		entitlementRiskAnalysisWS = sapConfig.getEntitlementRiskAnalysisWS();
		roleLookupWS = sapConfig.getRoleLookupWS();
		appLookupWS = sapConfig.getAppLookupWS();
		otherLookupWS = sapConfig.getOtherLookupWS();
		//requestDetailsWS = sapConfig.getRequestDetailsWS();
		createUserReqType = sapConfig.getCreateUserReqType();
		modifyUserReqType = sapConfig.getModifyUserReqType(); 
		lockUserReqType = sapConfig.getLockUserReqType();
		unlockUserReqType = sapConfig.getUnlockUserReqType();
		deleteUserReqType = sapConfig.getDeleteUserReqType();
		assignRoleReqType = sapConfig.getAssignRoleReqType();
		ignoreOpenStatus = sapConfig.getIgnoreOpenStatus();
		logAuditTrial = sapConfig.getLogAuditTrial();

		userAccessAccessURL = sapConfig.getUserAccessAccessURL();
		requestStatusAccessURL = sapConfig.getRequestStatusAccessURL();
		auditLogsAccessURL = sapConfig.getAuditLogsAccessURL();
		entitlementRiskAnalysisAccessURL = sapConfig.getEntitlementRiskAnalysisAccessURL();
		roleLookupAccessURL = sapConfig.getRoleLookupAccessURL();
		appLookupAccessURL = sapConfig.getAppLookupAccessURL();
		otherLookupAccessURL = sapConfig.getOtherLookupAccessURL();
		//requestDetailsAccessURL = sapConfig.getRequestDetailsAccessURL();
		requestTypeAttrName = sapConfig.getRequestTypeAttrName();
		provActionAttrName = sapConfig.getProvActionAttrName();
		provItemActionAttrName = sapConfig.getProvItemActionAttrName();
		removeRoleReqType = sapConfig.getRemoveRoleReqType();
		
		wsdlFilePath = sapConfig.getWsdlFilePath();
		//Start :: BUG 17288932
		auditTrailWSDLPath= sapConfig.getAuditTrailWSDLPath();
		requestStatusWSDLPath= sapConfig.getRequestStatusWSDLPath();
		submitRequestWSDLPath = sapConfig.getSubmitRequestWSDLPath();
		selectApplicationWSDLPath= sapConfig.getSelectApplicationWSDLPath();
		searchRolesWSDLPath = sapConfig.getSearchRolesWSDLPath();
		//End :: BUG 17288932
	}
	
	
	@Override
    public void validate() {
        validateRequired();
    }

    /**
     * Validates that all required properties are in place.
     * 
     * @throws ConfigurationException in case value of required property is missing.
     */
    private void validateRequired() {
        try {
            PropertyDescriptor[] propertyDescs = Introspector.getBeanInfo(this.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescs) {
                Method getter = propertyDescriptor.getReadMethod();
                assert getter != null;
                ConfigurationProperty annotation = getter.getAnnotation(ConfigurationProperty.class);
                if (annotation != null && annotation.required()) {
                    Object value = getter.invoke(this);
                    if (value == null) {
                        // missing required property
                        throw new ConfigurationException(getMessage(annotation.displayMessageKey())+getMessage("SAPACUME_ERR_VALUE_IS_NOT_SET"));
                    }
                    else if (value instanceof String && StringUtil.isBlank((String)value)) {
                        // blank String
                        throw new ConfigurationException(getMessage(annotation.displayMessageKey())+getMessage("SAPACUME_ERR_VALUE_IS_NOT_SET"));
                    }
                }
            }
        } catch (IntrospectionException ex) {
            // should not happen, just log it
            log.warn(ex, getMessage("SAPACUME_WARN_VALIDATING_REQ_PROP"));
        } catch (IllegalAccessException ex) {
            // should not happen, just log it
            log.warn(ex, getMessage("SAPACUME_WARN_VALIDATING_REQ_PROP"));
        } catch (InvocationTargetException ex) {
            // should not happen, just log it
            log.warn(ex, getMessage("SAPACUME_WARN_VALIDATING_REQ_PROP"));
        }
    }


    /**
     * Returns localized message.
     *
     * @param key Message key.
     * @return Localized message.
     */
    public String getMessage(String key) {
        return getConnectorMessages().format(key, key);
    }

    /**
     * Returns localized message.
     *
     * @param key Message key.
     * @param objects Values to be set to message placeholders.
     * @return Localized message.
     */
    public String getMessage(String key, Object... objects) {
        return getConnectorMessages().format(key, key, objects);
    }
	
	
	@ConfigurationProperty(order = 12, displayMessageKey="AC_IS_GRC_53_DISPLAY", helpMessageKey="AC_IS_GRC_53_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public boolean getIsGRC53() {
		return isGRC53;
	}

	public void setIsGRC53(boolean isGRC53) {
		this.isGRC53 = isGRC53;
	}

	@ConfigurationProperty(order = 13, displayMessageKey="AC_GRC_LANGUAGE_DISPLAY", helpMessageKey="AC_GRC_LANGUAGE_HELP", required=true)
	public String getGrcLanguage() {
		return this.grcLanguage;
	}

	public void setGrcLanguage(String grcLanguage) {
		this.grcLanguage = grcLanguage;
	}

	/**
	 * @return the username
	 */
	@ConfigurationProperty(order = 14, displayMessageKey="AC_GRC_USERNAME_DISPLAY", helpMessageKey="AC_GRC_USERNAME_HELP", required=true)
	public String getGrcUsername() {
		return this.grcUsername;
	}

	public void setGrcUsername(String grcUsername) {
		this.grcUsername = grcUsername;
	}

	/**
	 * @return the password
	 */
	@ConfigurationProperty(order = 15, displayMessageKey="AC_GRC_PASSWORD_DISPLAY", helpMessageKey="AC_GRC_PASSWORD_HELP", required=true, confidential=true)
	public GuardedString getGrcPassword() {
		return this.grcPassword;
	}

	/**
	 * @param password the password to set
	 */
	public void setGrcPassword(GuardedString grcPassword) {
		this.grcPassword = grcPassword;
	}

	/**
	 * @return the userAccessWS
	 */
	@ConfigurationProperty(order = 16, displayMessageKey="AC_USER_ACCESS_WS_DISPLAY", helpMessageKey="AC_USER_ACCESS_WS_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getUserAccessWS() {
		return userAccessWS;
	}

	/**
	 * @param userAccessWS the userAccessWS to set
	 */
	public void setUserAccessWS(String userAccessWS) {
		this.userAccessWS = userAccessWS;
	}

	/**
	 * @return the statusRequestWS
	 */
	@ConfigurationProperty(order = 17, displayMessageKey="AC_REQUEST_STATUS_WS_DISPLAY", helpMessageKey="AC_REQUEST_STATUS_WS_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getRequestStatusWS() {
		return requestStatusWS;
	}

	/**
	 * @param statusRequestWs the statusRequestWS to set
	 */
	public void setRequestStatusWS(String requestStatusWS) {
		this.requestStatusWS = requestStatusWS;
	}

	/**
	 * @return the auditLogsWS
	 */
	@ConfigurationProperty(order = 18, displayMessageKey="AC_AUDIT_LOGS_WS_DISPLAY", helpMessageKey="AC_AUDIT_LOGS_WS_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getAuditLogsWS() {
		return auditLogsWS;
	}

	/**
	 * @param auditLogsWs the auditLogsWS to set
	 */
	public void setAuditLogsWS(String auditLogsWs) {
		this.auditLogsWS = auditLogsWs;
	}

	/**
	 * @return the entitlementRiskAnalysisWS
	 */
	@ConfigurationProperty(order = 19, displayMessageKey="AC_ENTITLEMENT_RISK_ANALYSIS_WS_DISPLAY", helpMessageKey="AC_ENTITLEMENT_RISK_ANALYSIS_WS_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getEntitlementRiskAnalysisWS() {
		return entitlementRiskAnalysisWS;
	}

	/**
	 * @param entitlementRiskAnalysisWs the entitlementRiskAnalysisWS to set
	 */
	public void setEntitlementRiskAnalysisWS(String entitlementRiskAnalysisWs) {
		entitlementRiskAnalysisWS = entitlementRiskAnalysisWs;
	}

	/**
	 * @return the roleLookupWS
	 */
	@ConfigurationProperty(order = 20, displayMessageKey="AC_ROLE_LOOKUP_WS_DISPLAY", helpMessageKey="AC_ROLE_LOOKUP_WS_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getRoleLookupWS() {
		return roleLookupWS;
	}

	/**
	 * @param roleLookupWs the roleLookupWS to set
	 */
	public void setRoleLookupWS(String roleLookupWs) {
		roleLookupWS = roleLookupWs;
	}

	/**
	 * @return the appLookupWS
	 */
	@ConfigurationProperty(order = 21, displayMessageKey="AC_APP_LOOKUP_WS_DISPLAY", helpMessageKey="AC_APP_LOOKUP_WS_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getAppLookupWS() {
		return appLookupWS;
	}

	/**
	 * @param appLookupWs the appLookupWS to set
	 */
	public void setAppLookupWS(String appLookupWs) {
		appLookupWS = appLookupWs;
	}

	/**
	 * @return the otherLookupWS
	 */
	@ConfigurationProperty(order = 22, displayMessageKey="AC_OTHER_LOOKUP_WS_DISPLAY", helpMessageKey="AC_OTHER_LOOKUP_WS_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getOtherLookupWS() {
		return otherLookupWS;
	}

	/**
	 * @param otherLookupWs the otherLookupWS to set
	 */
	public void setOtherLookupWS(String otherLookupWs) {
		otherLookupWS = otherLookupWs;
	}

	/**
	 * @return the createUserReqType
	 */
	@ConfigurationProperty(order = 23, displayMessageKey="AC_CREATE_USER_REQTYPE_DISPLAY", helpMessageKey="AC_CREATE_USER_REQTYPE_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getCreateUserReqType() {
		return createUserReqType;
	}

	/**
	 * @param createUserReqType the createUserReqType to set
	 */
	public void setCreateUserReqType(String createUserReqType) {
		this.createUserReqType = createUserReqType;
	}

	/**
	 * @return the modifyUserReqType
	 */
	@ConfigurationProperty(order = 24, displayMessageKey="AC_MODIFY_USER_REQTYPE_DISPLAY", helpMessageKey="AC_MODIFY_USER_REQTYPE_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getModifyUserReqType() {
		return modifyUserReqType;
	}

	/**
	 * @param modifyUserReqType the modifyUserReqType to set
	 */
	public void setModifyUserReqType(String modifyUserReqType) {
		this.modifyUserReqType = modifyUserReqType;
	}

	/**
	 * @return the lockUserReqType
	 */
	@ConfigurationProperty(order = 25, displayMessageKey="AC_LOCK_USER_REQTYPE_DISPLAY", helpMessageKey="AC_LOCK_USER_REQTYPE_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getLockUserReqType() {
		return lockUserReqType;
	}

	/**
	 * @param lockUserReqType the lockUserReqType to set
	 */
	public void setLockUserReqType(String lockUserReqType) {
		this.lockUserReqType = lockUserReqType;
	}

	/**
	 * @return the unlockUserReqType
	 */
	@ConfigurationProperty(order = 26, displayMessageKey="AC_UNLOCK_USER_REQTYPE_DISPLAY", helpMessageKey="AC_UNLOCK_USER_REQTYPE_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getUnlockUserReqType() {
		return unlockUserReqType;
	}

	/**
	 * @param unlockUserReqType the unlockUserReqType to set
	 */
	public void setUnlockUserReqType(String unlockUserReqType) {
		this.unlockUserReqType = unlockUserReqType;
	}

	/**
	 * @return the deleteUserReqType
	 */
	@ConfigurationProperty(order = 27, displayMessageKey="AC_DELETE_USER_REQTYPE_DISPLAY", helpMessageKey="AC_DELETE_USER_REQTYPE_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getDeleteUserReqType() {
		return deleteUserReqType;
	}

	/**
	 * @param deleteUserReqType the deleteUserReqType to set
	 */
	public void setDeleteUserReqType(String deleteUserReqType) {
		this.deleteUserReqType = deleteUserReqType;
	}

	/**
	 * @return the assignRoleReqType
	 */
	@ConfigurationProperty(order = 28, displayMessageKey="AC_ASSIGN_ROLE_REQTYPE_DISPLAY", helpMessageKey="AC_ASSIGN_ROLE_REQTYPE_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getAssignRoleReqType() {
		return assignRoleReqType;
	}

	/**
	 * @param assignRoleReqType the assignRoleReqType to set
	 */
	public void setAssignRoleReqType(String assignRoleReqType) {
		this.assignRoleReqType = assignRoleReqType;
	}

	/**
	 * @return the ignoreOpenStatus
	 */
	@ConfigurationProperty(order = 29, displayMessageKey="AC_IGNORE_OPEN_STATUS_DISPLAY", helpMessageKey="AC_IGNORE_OPEN_STATUS_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getIgnoreOpenStatus() {
		return ignoreOpenStatus;
	}

	/**
	 * @param ignoreOpenStatus the ignoreOpenStatus to set
	 */
	public void setIgnoreOpenStatus(String ignoreOpenStatus) {
		this.ignoreOpenStatus = ignoreOpenStatus;
	}

	/**
	 * @return the logAuditTrial
	 */
	@ConfigurationProperty(order = 30, displayMessageKey="AC_LOG_AUDIT_TRIAL_DISPLAY", helpMessageKey="AC_LOG_AUDIT_TRIAL_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getLogAuditTrial() {
		return logAuditTrial;
	}

	/**
	 * @param logAuditTrial the logAuditTrial to set
	 */
	public void setLogAuditTrial(String logAuditTrial) {
		this.logAuditTrial = logAuditTrial;
	}

	/**
	 * @return the userAccessAccessURL
	 */
	@ConfigurationProperty(order = 31, displayMessageKey="AC_USER_ACCESS_ACCESS_URL_DISPLAY", helpMessageKey="AC_USER_ACCESS_ACCESS_URL_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getUserAccessAccessURL() {
		return userAccessAccessURL;
	}

	/**
	 * @param userAccessAccessURL the userAccessAccessURL to set
	 */
	public void setUserAccessAccessURL(String userAccessAccessURL) {
		this.userAccessAccessURL = userAccessAccessURL;
	}

	/**
	 * @return the statusRequestAccessURL
	 */
	@ConfigurationProperty(order = 32, displayMessageKey="AC_REQUEST_STATUS_ACCESS_URL_DISPLAY", helpMessageKey="AC_REQUEST_STATUS_ACCESS_URL_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getRequestStatusAccessURL() {
		return requestStatusAccessURL;
	}

	/**
	 * @param statusRequestAccessurl the statusRequestAccessURL to set
	 */
	public void setRequestStatusAccessURL(String requestStatusAccessURL) {
		this.requestStatusAccessURL = requestStatusAccessURL;
	}

	/**
	 * @return the auditLogsAccessURL
	 */
	@ConfigurationProperty(order = 33, displayMessageKey="AC_AUDIT_LOGS_ACCESS_URL_DISPLAY", helpMessageKey="AC_AUDIT_LOGS_ACCESS_URL_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getAuditLogsAccessURL() {
		return auditLogsAccessURL;
	}

	/**
	 * @param auditLogsAccessURL the auditLogsAccessURL to set
	 */
	public void setAuditLogsAccessURL(String auditLogsAccessurl) {
		auditLogsAccessURL = auditLogsAccessurl;
	}

	/**
	 * @return the entitlementRiskAnalysisAccessURL
	 */
	@ConfigurationProperty(order = 34, displayMessageKey="AC_ENTITLEMENT_RISK_ANALYSIS_ACCESS_URL_DISPLAY", helpMessageKey="AC_ENTITLEMENT_RISK_ANALYSIS_ACCESS_URL_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getEntitlementRiskAnalysisAccessURL() {
		return entitlementRiskAnalysisAccessURL;
	}

	/**
	 * @param entitlementRiskAnalysisAccessurl the entitlementRiskAnalysisAccessURL to set
	 */
	public void setEntitlementRiskAnalysisAccessURL(
			String entitlementRiskAnalysisAccessurl) {
		entitlementRiskAnalysisAccessURL = entitlementRiskAnalysisAccessurl;
	}

	/**
	 * @return the roleLookupAccessURL
	 */
	@ConfigurationProperty(order = 35, displayMessageKey="AC_ROLE_LOOKUP_ACCESS_URL_DISPLAY", helpMessageKey="AC_ROLE_LOOKUP_ACCESS_URL_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getRoleLookupAccessURL() {
		return roleLookupAccessURL;
	}

	/**
	 * @param roleLookupAccessurl the roleLookupAccessURL to set
	 */
	public void setRoleLookupAccessURL(String roleLookupAccessurl) {
		roleLookupAccessURL = roleLookupAccessurl;
	}

	/**
	 * @return the appLookupAccessURL
	 */
	@ConfigurationProperty(order = 36, displayMessageKey="AC_APP_LOOKUP_ACCESS_URL_DISPLAY", helpMessageKey="AC_APP_LOOKUP_ACCESS_URL_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getAppLookupAccessURL() {
		return appLookupAccessURL;
	}

	/**
	 * @param appLookupAccessurl the appLookupAccessURL to set
	 */
	public void setAppLookupAccessURL(String appLookupAccessurl) {
		appLookupAccessURL = appLookupAccessurl;
	}

	/**
	 * @return the otherLookupAccessURL
	 */
	@ConfigurationProperty(order = 37, displayMessageKey="AC_OTHER_LOOKUP_ACCESS_URL_DISPLAY", helpMessageKey="AC_OTHER_LOOKUP_ACCESS_URL_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getOtherLookupAccessURL() {
		return otherLookupAccessURL;
	}

	/**
	 * @param otherLookupAccessurl the otherLookupAccessURL to set
	 */
	public void setOtherLookupAccessURL(String otherLookupAccessurl) {
		otherLookupAccessURL = otherLookupAccessurl;
	}

	/**
	 * @return the requestTypeAttrName
	 */
	@ConfigurationProperty(order = 38, displayMessageKey="AC_REQUEST_TYPE_ATTRNAME_DISPLAY", helpMessageKey="AC_REQUEST_TYPE_ATTRNAME_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getRequestTypeAttrName() {
		return requestTypeAttrName;
	}

	/**
	 * @param requestTypeAttrName the requestTypeAttrName to set
	 */
	public void setRequestTypeAttrName(String requestTypeAttrName) {
		this.requestTypeAttrName = requestTypeAttrName;
	}

	/**
	 * @return the provActionAttrName
	 */
	@ConfigurationProperty(order = 39, displayMessageKey="AC_PROV_ACTION_ATTRNAME_DISPLAY", helpMessageKey="AC_PROV_ACTION_ATTRNAME_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getProvActionAttrName() {
		return provActionAttrName;
	}

	/**
	 * @param provActionAttrName the provActionAttrName to set
	 */
	public void setProvActionAttrName(String provActionAttrName) {
		this.provActionAttrName = provActionAttrName;
	}

	/**
	 * @return the provItemActionAttrName
	 */
	@ConfigurationProperty(order = 40, displayMessageKey="AC_PROV_ITEM_ACTION_ATTRNAME_DISPLAY", helpMessageKey="AC_PROV_ITEM_ACTION_ATTRNAME_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getProvItemActionAttrName() {
		return provItemActionAttrName;
	}

	/**
	 * @param provItemActionAttrName the provItemActionAttrName to set
	 */
	public void setProvItemActionAttrName(String provItemActionAttrName) {
		this.provItemActionAttrName = provItemActionAttrName;
	}

	/**
	 * @return the removeRoleReqType
	 */
	@ConfigurationProperty(order = 41, displayMessageKey="AC_REMOVE_ROLE_REQTYPE_DISPLAY", helpMessageKey="AC_REMOVE_ROLE_REQTYPE_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getRemoveRoleReqType() {
		return removeRoleReqType;
	}

	/**
	 * @param removeRoleReqType the removeRoleReqType to set
	 */
	public void setRemoveRoleReqType(String removeRoleReqType) {
		this.removeRoleReqType = removeRoleReqType;
	}

	/**
	 * @return the wsdlFilePath
	 */
	@ConfigurationProperty(order = 42, displayMessageKey="AC_WSDL_FILE_PATH_DISPLAY", helpMessageKey="AC_WSDL_FILE_PATH_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getWsdlFilePath() {
		return wsdlFilePath;
	}

	/**
	 * @param wsdlFilePath the wsdlFilePath to set
	 */
	public void setWsdlFilePath(String wsdlFilePath) {
		this.wsdlFilePath = wsdlFilePath;
	}

	//Start :: BUG 17288932

	/**
	 * @return the auditTrailWSDLPath
	 */
	@ConfigurationProperty(order = 43, displayMessageKey="AC_AUDIT_TRIAL_WSDL_PATH_DISPLAY", helpMessageKey="AC_AUDIT_TRIAL_WSDL_PATH_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getAuditTrailWSDLPath() {
		return auditTrailWSDLPath;
	}

	/**
	 * @param auditTrailWSDLPath the auditTrailWSDLPath to set
	 */
	public void setAuditTrailWSDLPath(String auditTrailWSDLPath) {
		this.auditTrailWSDLPath = auditTrailWSDLPath;
	}

	/**
	 * @return the requestStatusWSDLPath
	 */
	@ConfigurationProperty(order = 44, displayMessageKey="AC_REQUEST_STATUS_WSDL_PATH_DISPLAY", helpMessageKey="AC_REQUEST_STATUS_WSDL_PATH_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getRequestStatusWSDLPath() {
		return requestStatusWSDLPath;
	}

	/**
	 * @param requestStatusWSDLPath the requestStatusWSDLPath to set
	 */
	public void setRequestStatusWSDLPath(String requestStatusWSDLPath) {
		this.requestStatusWSDLPath = requestStatusWSDLPath;
	}

	/**
	 * @return the submitRequestWSDLPath
	 */
	@ConfigurationProperty(order = 45, displayMessageKey="AC_SUBMIT_REQUEST_WSDL_PATH_DISPLAY", helpMessageKey="AC_SUBMIT_REQUEST_WSDL_PATH_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getSubmitRequestWSDLPath() {
		return submitRequestWSDLPath;
	}

	/**
	 * @param submitRequestWSDLPath the submitRequestWSDLPath to set
	 */
	public void setSubmitRequestWSDLPath(String submitRequestWSDLPath) {
		this.submitRequestWSDLPath = submitRequestWSDLPath;
	}

	/**
	 * @return the selectApplicationWSDLPath
	 */
	@ConfigurationProperty(order = 46, displayMessageKey="AC_SELECT_APPLICATION_WSDL_PATH_DISPLAY", helpMessageKey="AC_SELECT_APPLICATION_WSDL_PATH_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getSelectApplicationWSDLPath() {
		return selectApplicationWSDLPath;
	}

	/**
	 * @param selectApplicationWSDLPath the selectApplicationWSDLPath to set
	 */
	public void setSelectApplicationWSDLPath(String selectApplicationWSDLPath) {
		this.selectApplicationWSDLPath = selectApplicationWSDLPath;
	}

	/**
	 * @return the searchRolesWSDLPath
	 */
	@ConfigurationProperty(order = 47, displayMessageKey="AC_SEARCH_ROLES_WSDL_PATH_DISPLAY", helpMessageKey="AC_SEARCH_ROLES_WSDL_PATH_HELP", objectClasses="ObjectClass.ACCOUNT_NAME")
	public String getSearchRolesWSDLPath() {
		return searchRolesWSDLPath;
	}

	/**
	 * @param searchRolesWSDLPath the searchRolesWSDLPath to set
	 */
	public void setSearchRolesWSDLPath(String searchRolesWSDLPath) {
		this.searchRolesWSDLPath = searchRolesWSDLPath;
	}
	//End :: BUG 17288932

	/*public String getRequestDetailsWS() {
		return requestDetailsWS;
	}

	public void setRequestDetailsWS(String requestDetailsWS) {
		this.requestDetailsWS = requestDetailsWS;
	}

	public String getRequestDetailsAccessURL() {
		return requestDetailsAccessURL;
	}

	public void setRequestDetailsAccessURL(String requestDetailsAccessURL) {
		this.requestDetailsAccessURL = requestDetailsAccessURL;
	}*/

	/**
	 * Method building a map with data of the class
	 * It using all declared variables of the class to construct the data map
	 * It returns an empty map,If any exception. 
	 * @return
	 */
	public Map<String, Object> toMap(){
		Map<String, Object> dataMap = new HashMap<String, Object>(10,0.75f);
		Field[] fields = this.getClass().getDeclaredFields();
		Object valueObj = null;
		try {
			for(Field field : fields){
				valueObj = field.get(this);
				if(valueObj != null){
					dataMap.put(field.getName(), valueObj);
				} else {
					dataMap.put(field.getName(), null);
				}
			}
		} catch (IllegalArgumentException e) {
			//Suppressing this Exception 
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			//Suppressing this Exception			
			e.printStackTrace();
		}
		return dataMap;
	}
}
