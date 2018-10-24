/**
 * 
 */
package org.identityconnectors.sapacume;

/**
 * @author ranjith.kumar
 *
 */
public interface SAPACUMEConstants {
	public static final String SYSTEM_RECON = "SYSTEM";
	public static final String BUSPROC_RECON = "BusProc";
	public static final String FUNCTIONAREA_RECON = "FunctionArea";
	public static final String ROLE = "ROLE";
	public static final String ITEMPROVACTIONTYPE = "ItemProvActionType";
	public static final String CREATE_REQ_TYPE = "NEW";
	public static final String DELETE_REQ_TYPE = "DELETE";
	public static final String USERID = "User ID";
	public static final String FORMDATA = "FormData";
	public static final String LOGONNAME = "logonname";
	public static final String ITEMPROVTYPE = "ItemProvType";
	public static final String REQUESTID = "RequestId";
	public static final String ITEMTYPE = "ItemType";
	public static final String CLOSED = "CLOSED";
	public static final String VALUE_OK = "OK";
	public static final String VALUE_NO ="NO";
        public static final String TRUE="true";
        public static final String ROLEID="roleId";
        public static final String REQTYPE = "RequestType";
        public static final String VALIDTO = "validto";
        public static final String ACTION = "action";
        public static final String PRIORITYTYPE_RECON = "PriorityType";
        public static final String ID = "id";
        public static final String REQUESTNO = "RequestNo";
        public static final String VALUE_YES ="YES";
 	public static final String ATTR_REQSTATUS = "RequestStatus";
	// UME Request line items
        // Start Bug 23743186- SAP UME: ROLE REQUEST IS NOT Generating Request ID IN GRC
 	public static final String UMEROLE = "umerole;itemName;ReqLineItem";
 	public static final String UMEGROUP = "umegroup;itemName;ReqLineItem";
        // End Bug 23743186- SAP UME: ROLE REQUEST IS NOT Generating Request ID IN GRC 
	public static final String DIGIT_ZERO = "0";
	public static final String DIGIT_ONE = "1";
	public static final String DIGIT_FOUR = "4";
	public static final String STATUS = "Status";
	public static final String RESULT = "Result";
	public static final String USERIDKEY = "userid";
	public static final String PATTERN_REQ_NO = "~*~";
	public static final String PATTERN_REQ_TYPE = "~";
	public static final String PATTERN_EQUALTO = "=";
	public static final String PATTERN_SEMI_COLON = ";";
	public static final String PATTERN_AND = "&";
	public static final String CONFIGURATION = "Configuration";
	public static final String OBJECTCLASS = "ObjectClass";
	
	public static final String[] roleCodeKeyDecode = new String[]{"RCODE","RDECODE"};
	public static final String[] applCodeKeyDecode = new String[]{"REQSYSCODE","REQSYSDECODE"};
	public static final String[] lookupCodeKeyDecode = new String[]{"LCODE","LDECODE"};
	
	public static final String DECODE_USERLOCK = "userLock;None";
	
	public static final String REMOVE_CHILD = "removeChild";
	public static final String ADD_CHILD = "addChild";
	public static final String ATTR_UI_USER_ID = "userId;UserInfo";
	public static final String ATTR_UI_TITLE = "title;UserInfo";
	public static final String ATTR_UI_FNAME = "fname;UserInfo";
	public static final String ATTR_UI_LNAME = "lname;UserInfo";
	public static final String ATTR_UI_ACCNO = "accno;UserInfo";
	public static final String ATTR_UI_VALID_FROM = "validFrom;UserInfo";
	public static final String ATTR_UI_VALID_TO = "validTo;UserInfo";
	public static final String ATTR_UI_FAX = "fax;UserInfo";
	public static final String ATTR_UI_EMAIL = "email;UserInfo";
	public static final String ATTR_UI_TELEPHONE = "telnumber;UserInfo";
	public static final String ATTR_UI_LONGON_LANGUAGE = "logonLang;UserInfo";
	public static final String ATTR_UI_AC_MANAGER = "manager;UserInfo";
	public static final String ATTR_UI_AC_MGR_EMAIL = "managerEmail;UserInfo";
	public static final String ATTR_UI_AC_MGR_FNAME = "managerFirstname;UserInfo";
	public static final String ATTR_UI_AC_MGR_LNAME = "managerLastname;UserInfo";
	
	public static final String ATTR_AC_REQ_ID = "__UID__";
	public static final String ATTR_AC_REQ_STATUS = "Requeststatus";
	public static final String ATTR_REQ_TYPE = "ReqType";
	
	public static final String ATTR_HDR_REQTR_ID = "requestorId;Header";
	public static final String ATTR_HDR_REQTR_EMAIL = "email;Header";
	public static final String ATTR_HDR_PRIORITY = "priority;Header";
	public static final String ATTR_HDR_SYSTEM = "reqInitSystem;Header";
	public static final String ATTR_HDR_REQ_DUEDATE = "reqDueDate;Header";
	public static final String ATTR_HDR_REQ_REASON = "requestReason;Header";
	public static final String ATTR_HDR_FUNC_AREA= "funcarea;Header";
	public static final String ATTR_HDR_BPROCESS= "bproc;Header";
	public static final String ATTR_ROLE_OR_GROUP_NAME = "itemName;ReqLineItem";
	public static final String SERVICE_METHODNAME = "callService";
	
	//Start --  added for AC supported attributes 
	public static final String ATTR_UI_DEPARTMENT= "department;UserInfo"; 
	public static final String ATTR_UI_FORM_OF_ADDRESS= "personnelarea;UserInfo";
	public static final String ATTR_UI_MOBILE= "personnelno;UserInfo";

	public static final String ATTR_UI_POSITION ="empposition;UserInfo";
	
	//End --ended for AC supported attributes
	
	//Start UME Schema Attributes
	public static final String PASSWORD ="__PASSWORD__";
	public static final String ENABLE ="__ENABLE__";
	//public static final String LOCK_OUT ="__ENABLE_";
	public static final String CURRENT_ATTRIBUTES = "__CURRENT_ATTRIBUTES__";
	public static final String OLD_PASSWORD ="oldpassword";
	public static final String IS_PASSWORD_DISABLED ="ispassworddisabled";
	//End UME Schema Attributes
	
	// Start-- AC53 Attributes
	
	public static final String COMPANY_53 ="company";
	public static final String FUNCTIONAL_AREA_53 ="functionalArea";
	public static final String MANAGER_ID_53 ="mgrId";
	public static final String MANAGER_EMAIL_ADDRESS_53 ="mgrEmailAddress";
	public static final String MANAGER_FIRST_NAME_53 ="mgrFirstName";
	public static final String MANAGER_LAST_NAME_53 ="mgrLastName";
	public static final String MANAGER_TELEPHONE_53 ="managerTelephone";
	public static final String PRIORITY_53 ="priority";
	
	public static final String REQUESTOR_EMAIL_ADDRESS_53 ="requestorEmailAddress";
	public static final String REQUESTOR_FIRST_NAME_53 ="requestorFirstName";
	public static final String REQUESTOR_ID__53_53 ="requestorId";
	public static final String REQUESTOR_LAST_NAME_53 ="requestorLastName";
	public static final String REQUEST_REASON_53 ="requestReason";

	public static final String APPLICATION_53 ="application";
	public static final String DEPARTMENT_53 ="department";
	public static final String EMAIL_ADDRESS_53 ="emailAddress";

	public static final String FIRST_NAME_53 ="firstName";
	public static final String LOCATION_53 ="location";
	public static final String LAST_NAME_53 ="lastname";
	public static final String USER_ID_53 ="userId";
	public static final String VALIDFROM_53 ="validFrom";
	public static final String TELEPHONE_53 ="telephone";
	
	// END-- AC53 Attributes
	
	
	
	public static final String REQUEST_STATUS_LOGON_NAME ="Logon Name"; 
	
	

}
