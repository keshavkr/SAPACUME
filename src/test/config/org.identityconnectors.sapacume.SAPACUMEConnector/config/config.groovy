import org.identityconnectors.contract.exceptions.ObjectNotFoundException
import org.identityconnectors.contract.data.groovy.Lazy
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder
import org.identityconnectors.framework.common.objects.ObjectClass

 EmbeddedObject getEmbeddedObject(String logonName) {
         EmbeddedObjectBuilder ObjBuilder = new EmbeddedObjectBuilder();
         ObjBuilder.setObjectClass(new ObjectClass("__CURRENT_ATTRIBUTES__"));
         ObjBuilder.addAttribute("email;UserInfo", "suresh@gmail.com");
         ObjBuilder.addAttribute("userId;UserInfo", logonName);
        
         return ObjBuilder.build();
		}
connector.umeUserId="administrator"
connector.umeUrl="http://172.20.55.218:50000/spml/spmlservice"
connector.umePassword=new GuardedString("Mphasis123".toCharArray()) 
connector.dummyPassword=new GuardedString("Mphasis1234".toCharArray())
connector.changePwdFlag=true
connector.pwdHandlingSupport=true
connector.logSPMLRequest=true
connector.enableDate="9999-12-31"
connector.groupDatasource=(String[])["PRIVATE_DATASOURCE","R3_ROLE_DS"]
connector.roleDatasource=(String[])["UME_ROLE_PERSISTENCE","PCD_ROLE_PERSISTENCE"]
connector.logonNameInitialSubstring="abcdefghijklmnopqrstuvwxyz1234567890"
connector.isGRC10="yes"

//AC connection parameters
connector.grcLanguage="EN"
connector.grcPassword=new GuardedString("Mphasis123".toCharArray()) 
connector.grcPort="8000"
connector.grcServer="172.20.55.147"
connector.grcUsername="basis"



connector.auditTrailWSDLPath="no"
connector.auditTrailWSDLPath="no"
connector.StatusWSDLPath="no"
connector.submitRequestWSDLPath="no"
connector.selectApplicationWSDLPath="no"
connector.searchRolesWSDLPath="no"

connector.roleLookupAccessURL="http://wksban04orstd.corp.mphasis.com:8000/sap/bc/srt/scs/sap/grac_search_roles_ws?sap-client=100"
connector.appLookupAccessURL="http://wksban04orstd.corp.mphasis.com:8000/sap/bc/srt/scs/sap/grac_select_appl_ws?sap-client=100"
connector.userAccessAccessURL ="http://wksban04orstd.corp.mphasis.com:8000/sap/bc/srt/scs/sap/grac_user_acces_ws?sap-client=100"
connector.requestStatusAccessURL="http://wksban04orstd.corp.mphasis.com:8000/sap/bc/srt/scs/sap/grac_request_status_ws?sap-client=100"
connector.auditLogsAccessURL="http://wksban04orstd.corp.mphasis.com:8000/sap/bc/srt/scs/sap/grac_audit_logs_ws?sap-client=100"
connector.entitlementRiskAnalysisAccessURL="http://wksban04orstd.corp.mphasis.com:8000/sap/bc/srt/scs/sap/grac_risk_analysis_wout_no_ws?sap-client=100"
connector.otherLookupAccessURL="http://wksban04orstd.corp.mphasis.com:8000/sap/bc/srt/scs/sap/grac_lookup_ws?sap-client=100"
connector.requestDetailsAccessURL="http://WKSBAN04ORSTD.corp.mphasis.com:8000/sap/bc/srt/scs/sap/grac_request_details_ws?sap-client=100"


connector.requestStatusValue="OK"
connector.ignoreOpenStatus="Yes"
//connector.riskLevel="High"

connector.createUserReqType="001~New Account~001"
connector.modifyUserReqType="002~Change Account~002"
connector.lockUserReqType="004~Lock Account~004"
connector.unlockUserReqType="005~unlock user~005"
connector.deleteUserReqType="003~Delete Account~003"
connector.assignRoleReqType="002~Change Account~002~006"
connector.removeRoleReqType="002~Change Account~002~009"


connector.requestStatusWS="oracle.iam.ws.sap.ac10.RequestStatus"
connector.auditLogsWS="oracle.iam.ws.sap.ac10.AuditLogs"
connector.entitlementRiskAnalysisWS="oracle.iam.grc.sod.scomp.impl.grcsap.util.webservice.sap.ac10.RiskAnalysisWithoutNo"
connector.roleLookupWS="oracle.iam.ws.sap.ac10.SearchRoles"
connector.appLookupWS="oracle.iam.ws.sap.ac10.SelectApplication"
connector.otherLookupWS="oracle.iam.ws.sap.ac10.SearchLookup"
connector.userAccessWS="oracle.iam.ws.sap.ac10.UserAccess"
connector.requestDetailsWS="oracle.iam.ws.sap.ac10.RequestDetails";

connector.wsdlFilePath=	"D:\\UmeACDevelopment\\wsdl"

connector.provItemActionAttrName="provItemAction;ReqLineItem"
connector.provActionAttrName="provAction;ReqLineItem"

connector.logAuditTrial="Yes"


connector.requestTypeAttrName="Reqtype;Header"

//connector.Role form names="UD_SPUMRC_P;UD_SAPRL"
//connector.Profile attribute name=	"USERPROFILE"

//connector.RoleAttributeLabel=	"Role Name"


// TODO fill in the following test configurations

// Connector WRONG configuration for ValidateApiOpTests
testsuite.Validate.invalidConfig = [
  [ umeUrl : "" ]//,
//  [ login : "" ],
//  [ password : "" ]
  ]

// Connector WRONG configuration for TestApiOpTests
testsuite.Test.invalidConfig = [
  [ umePassword: "NonExistingPassword_foo_bar_boo" ]
]
testsuite.Search.disable.caseinsensitive = true
testsuite {
    bundleJar = System.getProperty("bundleJar")
    bundleName = System.getProperty("bundleName")
    bundleVersion=System.getProperty("bundleVersion")
    connectorName="org.identityconnectors.sapacume.SAPACUMEConnector"

Schema {
        oclasses = ['__ACCOUNT__','Role','Group']
        attributes {
            __ACCOUNT__.oclasses = [
                '__NAME__','__ENABLE__','__CURRENT_ATTRIBUTES__','__PASSWORD__','logonname','isserviceuser',
			'firstname','lastname','salutation','title','jobtitle','mobile','displayname',
			'description','oldpassword','email','fax','locale','timezone','validfrom',
			'validto','certificate','lastmodifydate','islocked','ispassworddisabled','telephone',
			'department','id','securitypolicy','datasource','assignedroles','allassignedroles',
			'assignedgroups','allassignedgroups','company','streetaddress','city','zip','pobox',
			'country','state','orgunit','accessibilitylevel','passwordchangerequired','userId;UserInfo','title;UserInfo','fname;UserInfo','lname;UserInfo',
			'accno;UserInfo','validFrom;UserInfo','validTo;UserInfo','fax;UserInfo','email;UserInfo','telnumber;UserInfo',
			'logonLang;UserInfo','manager;UserInfo','managerEmail;UserInfo','managerFirstname;UserInfo','managerLastname;UserInfo','RequestStatus','requestorId;Header','email;Header','priority;Header','reqInitSystem;Header','reqDueDate;Header',
 'requestReason;Header','funcarea;Header','bproc;Header','itemName;ReqLineItem','RequestId','RequestType','personnelno;UserInfo','userLock;None','department;UserInfo','personnelarea;UserInfo','empposition;UserInfo'] // __ACCOUNT__.oclasses
	    Role.oclasses = [
		'lastmodifydate','displayname','datasource','member','description','__NAME__','id','uniquename','itemName;ReqLineItem'
            ] // Role.oclasses 
	    Group.oclasses = [
               'lastmodifydate','assignedroles','allassignedroles','displayname','datasource','member','description',
		'__NAME__','id','distinguishedname','uniquename','itemName;ReqLineItem'
            ] // Group.oclasses

        } // attributes
        
        //for ACUME
        
          attrTemplateACUme = [
            type: String.class,
            readable: true,
            createable: false,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ]// attrTemplate

        attrTemplate = [
            type: String.class,
            readable: true,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ]// attrTemplate

	readOnlyAttrTemplate = [
            type: String.class,
            readable: true,
            createable: false,
            updateable: false,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ]// attrTemplate

        attrTemplateLong = [
            type: long.class,
            readable: true,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ]// attrTemplateLong
       
        operations = [
          //  UpdateAttributeValuesOp: ['__ACCOUNT__','Role','Group'],  
            SearchApiOp: ['__ACCOUNT__','Role','Group'],
            ScriptOnConnectorApiOp: [],
            ValidateApiOp: [],
            AuthenticationApiOp: [],
            GetApiOp: ['__ACCOUNT__','Role','Group'],
            SchemaApiOp: ['__ACCOUNT__'],
            TestApiOp: ['__ACCOUNT__','Role','Group'],
          //  ScriptOnResourceApiOp: ['__ACCOUNT__'],
            CreateApiOp: ['__ACCOUNT__'],
            //DeleteApiOp: ['__ACCOUNT__'],
	     UpdateApiOp: ['__ACCOUNT__'],
	     //SyncApiOp: ['__ACCOUNT__']
        //    ResolveUsernameApiOp: ['__ACCOUNT__','Role','Group']	
        ]//operations
    } // Schema

    Schema."__NAME__".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: true,
            createable: true,
            updateable: true,
            required: true,
            multiValue: false,
            returnedByDefault: true
        ]// __NAME__
		

   Schema."__CURRENT_ATTRIBUTES__".attribute.__ACCOUNT__.oclasses = [
		type: EmbeddedObject.class,
		readable: true,
		createable: true,
		updateable: true,
		required: false,
		multiValue: false,
		returnedByDefault: true
		]// 
    Schema."__ENABLE__".attribute.__ACCOUNT__.oclasses = [
            type: boolean.class,
            readable: true,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ]// __ENABLE__

    Schema."__PASSWORD__".attribute.__ACCOUNT__.oclasses = [
            type: GuardedString.class,
            readable: false,
            createable: true,
            updateable: true,
            required: true,
            multiValue: false,
            returnedByDefault: false
        ]// __PASSWORD__

   Schema."oldpassword".attribute.__ACCOUNT__.oclasses = [
            type: GuardedString.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ]// __PASSWORD__

    Schema.firstname.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.lastname.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.isserviceuser.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.title.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.jobtitle.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.salutation.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.mobile.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.displayname.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.description.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.email.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.fax.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.locale.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.timezone.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.lastmodifydate.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.telephone.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.department.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.id.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.securitypolicy.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.datasource.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.readOnlyAttrTemplate")
    Schema.company.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.streetaddress.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.company.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.city.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.zip.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.pobox.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.country.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.state.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.orgunit.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.certificate.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.accessibilitylevel.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.passwordchangerequired.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplateACUme")
    Schema.allassignedroles.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.readOnlyAttrTemplate")
    Schema.allassignedgroups.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.readOnlyAttrTemplate")
    Schema.validfrom.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.validto.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	Schema."userId;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	Schema."title;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."fname;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."lname;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."accno;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	Schema."validFrom;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	Schema."validTo;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."fax;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."email;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."telnumber;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."logonLang;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."manager;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."managerEmail;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."managerFirstname;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."managerLastname;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."requestorId;Header".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."email;Header".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."priority;Header".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."reqInitSystem;Header".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."reqDueDate;Header".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."requestReason;Header".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."funcarea;Header".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."bproc;Header".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema.RequestId.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema.RequestType.attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."personnelno;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."userLock;None".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."empposition;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."personnelarea;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
	//Schema."department;UserInfo".attribute.__ACCOUNT__.oclasses = Lazy.get("testsuite.Schema.attrTemplate")

    Schema.logonname.attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: true,
            createable: false,
            updateable: false,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ] // logonname

   // Schema.lastname.attribute.__ACCOUNT__.oclasses = [
           //type: String.class,
           // readable: true,
           // createable: true,
           // updateable: true,
           // required: true,
           // multiValue: false,
           //returnedByDefault: true
      //  ] // lastname
    Schema.islocked.attribute.__ACCOUNT__.oclasses = [
            type: boolean.class,
            readable: true,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ] // islocked
    Schema.ispassworddisabled.attribute.__ACCOUNT__.oclasses = [
            type: boolean.class,
            readable: true,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ] // ispassworddisabled
    Schema.assignedroles.attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: true,
            createable: true,
            updateable: true,
            required: false,
            multiValue: true,
            returnedByDefault: true
        ] // assignedroles
    Schema.assignedgroups.attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: true,
            createable: true,
            updateable: true,
            required: false,
            multiValue: true,
            returnedByDefault: true
        ] // assignedgroups
        
     Schema."itemName;ReqLineItem".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: true,
            createable: true,
            updateable: true,
            required: false,
            multiValue: true,
            returnedByDefault: true
        ] // 
        
          Schema."RequestStatus".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: true,
            createable: false,
            updateable: false,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ] // 
           Schema."RequestId".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: true,
            createable: false,
            updateable: false,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ] // 
         Schema."RequestStatus".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: true,
            createable: false,
            updateable: false,
            required: false,
            multiValue: false,
            returnedByDefault: true
        ] //
        
             Schema."reqDueDate;Header".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."bproc;Header".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
        
         Schema."funcarea;Header".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."manager;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."managerEmail;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         
         Schema."managerFirstname;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."managerLastname;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."priority;Header".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."email;Header".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
        Schema."requestorId;Header".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."requestReason;Header".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
           Schema."reqInitSystem;Header".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
        Schema."personnelno;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."fname;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."userId;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."telnumber;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."department;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."lname;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."email;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
         Schema."fax;UserInfo".attribute.__ACCOUNT__.oclasses = [
            type: String.class,
            readable: false,
            createable: true,
            updateable: true,
            required: false,
            multiValue: false,
            returnedByDefault: false
        ] // 
        

    Schema.lastmodifydate.attribute.Group.oclasses = Lazy.get("testsuite.Schema.attrTemplate")    
    Schema.displayname.attribute.Group.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.datasource.attribute.Group.oclasses = Lazy.get("testsuite.Schema.readOnlyAttrTemplate")
    Schema.member.attribute.Group.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.description.attribute.Group.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.id.attribute.Group.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.distinguishedname.attribute.Group.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.allassignedroles.attribute.Group.oclasses = Lazy.get("testsuite.Schema.readOnlyAttrTemplate")
    Schema."itemName;ReqLineItem".attribute.Group.oclasses = Lazy.get("testsuite.Schema.attrTemplate") 
   
    Schema.uniquename.attribute.Group.oclasses = [
            type: String.class,
            readable: true,
            createable: true,
            updateable: true,
            required: true,
            multiValue: false,
            returnedByDefault: true
        ] // uniquename

    Schema."__NAME__".attribute.Group.oclasses = [
            type: String.class,
            readable: true,
            createable: true,
            updateable: true,
            required: true,
            multiValue: false,
            returnedByDefault: true
        ]// __NAME__

    Schema.assignedroles.attribute.Group.oclasses = [
            type: String.class,
            readable: true,
            createable: true,
            updateable: true,
            required: false,
            multiValue: true,
            returnedByDefault: true
        ] // assignedroles

    Schema.lastmodifydate.attribute.Role.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.displayname.attribute.Role.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.datasource.attribute.Role.oclasses = Lazy.get("testsuite.Schema.readOnlyAttrTemplate")
    Schema.member.attribute.Role.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.description.attribute.Role.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema.id.attribute.Role.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    Schema."itemName;ReqLineItem".attribute.Role.oclasses = Lazy.get("testsuite.Schema.attrTemplate")
    

    Schema.uniquename.attribute.Role.oclasses = [
            type: String.class,
            readable: true,
            createable: true,
            updateable: true,
            required: true,
            multiValue: false,
            returnedByDefault: true
        ] // uniquename

    Schema."__NAME__".attribute.Role.oclasses = [
            type: String.class,
            readable: true,
            createable: true,
            updateable: true,
            required: true,
            multiValue: false,
            returnedByDefault: true
        ]// __NAME__

}

__ACCOUNT__.__ENABLE__=new ObjectNotFoundException()
__ACCOUNT__.modified.__ENABLE__=new ObjectNotFoundException()

strictCheck=false
username = "SAPACTET1"
__ACCOUNT__."__NAME__"= "ACTESTU2000"

__ACCOUNT__."logonname"="TU1MAY28"

i0.Search.__ACCOUNT__."__NAME__"="ACTESTU8000"
i0.Search.__ACCOUNT__."userId;UserInfo"="ACTESTU8000" 
i0.Search.__ACCOUNT__."logonname"="ACTESTU8000"


i1.Search.__ACCOUNT__."__NAME__"="ACTESTU8001"
i1.Search.__ACCOUNT__."logonname"="ACTESTU8001"
i1.Search.__ACCOUNT__."userId;UserInfo"="ACTESTU8001" 

i2.Search.__ACCOUNT__."__NAME__"="800132251682"
i2.Search.__ACCOUNT__."logonname"="800132251682"
i2.Search.__ACCOUNT__."userId;UserInfo"="800132251682" 

i3.Search.__ACCOUNT__."__NAME__"="800132251683"
i3.Search.__ACCOUNT__."logonname"="800132251683"
i3.Search.__ACCOUNT__."userId;UserInfo"="800132251683" 

i4.Search.__ACCOUNT__."__NAME__"="800132251684"
i4.Search.__ACCOUNT__."logonname"="800132251684"
i4.Search.__ACCOUNT__."userId;UserInfo"="800132251684" 

i5.Search.__ACCOUNT__."__NAME__"="800132251685"
i5.Search.__ACCOUNT__."logonname"="800132251685"
i5.Search.__ACCOUNT__."userId;UserInfo"="800132251685" 

i6.Search.__ACCOUNT__."__NAME__"="800132251686"
i6.Search.__ACCOUNT__."logonname"="800132251686"
i6.Search.__ACCOUNT__."userId;UserInfo"="800132251686" 

i7.Search.__ACCOUNT__."__NAME__"="800132251687"
i7.Search.__ACCOUNT__."logonname"="800132251687"
i7.Search.__ACCOUNT__."userId;UserInfo"="500132251687" 

i8.Search.__ACCOUNT__."__NAME__"="800132251688"
i8.Search.__ACCOUNT__."logonname"="800132251688"
i8.Search.__ACCOUNT__."userId;UserInfo"="800132251688" 

i9.Search.__ACCOUNT__."__NAME__"="800132251689"
i9.Search.__ACCOUNT__."logonname"="800132251689"
i9.Search.__ACCOUNT__."userId;UserInfo"="800132251689" 




__ACCOUNT__."firstname"="suresh"
__ACCOUNT__."lastname"="kadiyala"
__ACCOUNT__."password"= new GuardedString("Mphasis12".toCharArray())
//__ACCOUNT__."__PASSWORD__"=new GuardedString("Mphasis12".toCharArray())
// Role."uniquename"="JK1NOV20"
__ACCOUNT__."salutation"="mr"
__ACCOUNT__."title"="mr"
__ACCOUNT__.jobtitle="SSE"
__ACCOUNT__.mobile="8152040605"
__ACCOUNT__.displayname="kadiyala suresh"
//done changes
__ACCOUNT__.description="creating"
__ACCOUNT__.oldpassword=new ObjectNotFoundException()
__ACCOUNT__.email="kadiyala@email.com"
__ACCOUNT__.fax="1234567890"
__ACCOUNT__.locale="en"
__ACCOUNT__."timezone"="IST"
__ACCOUNT__."validfrom"=new ObjectNotFoundException()
__ACCOUNT__."validto"=new ObjectNotFoundException()
__ACCOUNT__."certificate"=new ObjectNotFoundException()
__ACCOUNT__."lastmodifydate"=new ObjectNotFoundException()
__ACCOUNT__."islocked"=new ObjectNotFoundException()
__ACCOUNT__."ispassworddisabled"=new ObjectNotFoundException()
__ACCOUNT__."telephone"="8152040605"
__ACCOUNT__."department"="APPS"
__ACCOUNT__."id"=new ObjectNotFoundException()
__ACCOUNT__."securitypolicy"=new ObjectNotFoundException()
//__ACCOUNT__."datasource"=
__ACCOUNT__."assignedroles"=new ObjectNotFoundException()
__ACCOUNT__."allassignedroles"=new ObjectNotFoundException()
__ACCOUNT__."assignedgroups"=new ObjectNotFoundException()
__ACCOUNT__."allassignedgroups"=new ObjectNotFoundException()
__ACCOUNT__."company"="Mphasis"
__ACCOUNT__."streetaddress"="south"
__ACCOUNT__."city"="Bangalore"
__ACCOUNT__."zip"="523271"
__ACCOUNT__."pobox"="Bangalore"
__ACCOUNT__."country"="India"
__ACCOUNT__."state"="Karnataka"
__ACCOUNT__."orgunit"="OU"
__ACCOUNT__."accessibilitylevel"=new ObjectNotFoundException()
__ACCOUNT__."passwordchangerequired"=new ObjectNotFoundException()
//__ACCOUNT__."__NAME__"="JK1DEC15"
__ACCOUNT__."isserviceuser"=new ObjectNotFoundException()





 testsuite.Update.updateToNullValue.skippedAttributes = [
                '__NAME__','__ENABLE__','__CURRENT_ATTRIBUTES__','__PASSWORD__','logonname','isserviceuser',
			'lastname','salutation','title','jobtitle','mobile','displayname','firstname',
			'description','oldpassword','email','fax','locale','timezone','validfrom',
			'validto','certificate','lastmodifydate','islocked','ispassworddisabled','telephone',
			'department','id','securitypolicy','datasource','assignedroles','allassignedroles',
			'assignedgroups','allassignedgroups','company','streetaddress','city','zip','pobox',
			'country','state','orgunit','accessibilitylevel','passwordchangerequired','itemName;ReqLineItem','validFrom;UserInfo',
			'empposition;UserInfo','personnelarea;UserInfo','personnelno;UserInfo','logonLang;UserInfo','lname;UserInfo','fname;UserInfo','reqDueDate;Header','accno;UserInfo','email;Header',
			'email;UserInfo','telnumber;UserInfo','title;UserInfo','fax;UserInfo','userId;UserInfo','department;UserInfo','validTo;UserInfo','userLock;None','managerFirstname;UserInfo','managerLastname;UserInfo','managerEmail;UserInfo','manager;UserInfo','funcarea;Header','requestorId;Header','requestReason;Header','bproc;Header','priority;Header','reqInitSystem;Header'
           ]
		   
__ACCOUNT__.modified."firstname"="testing contracts acume"
__ACCOUNT__.modified."validfrom"=new ObjectNotFoundException()
__ACCOUNT__.modified."validto"=new ObjectNotFoundException()
__ACCOUNT__.modified."assignedroles"="ROLE.UME_ROLE_PERSISTENCE.un:SAP_CTS_DEPLOY"
__ACCOUNT__.modified."assignedgroups"="GRUP.PRIVATE_DATASOURCE.un:Guests"
__ACCOUNT__.modified."securitypolicy"="default"
__ACCOUNT__.modified."id"=new ObjectNotFoundException()
__ACCOUNT__.modified."password"=new ObjectNotFoundException()
__ACCOUNT__.modified."oldpassword"=new ObjectNotFoundException()
__ACCOUNT__.modified."certificate"=new ObjectNotFoundException()
__ACCOUNT__.modified."passwordchangerequired"="false"
__ACCOUNT__.modified."timezone"="IST"
__ACCOUNT__.modified."salutation"="Mr"
__ACCOUNT__.modified."zip"="560079"
__ACCOUNT__.modified."email"="sandeep@gmail.com"
__ACCOUNT__.modified."department"="apps"
__ACCOUNT__.modified."fax"="1234567890"
__ACCOUNT__.modified."accessibilitylevel"="1"
__ACCOUNT__.modified."isserviceuser"=new ObjectNotFoundException()
__ACCOUNT__.modified."company"="Mphasis"
__ACCOUNT__.modified."lastmodifydate"=new ObjectNotFoundException()
__ACCOUNT__.modified."description"="updated"
__ACCOUNT__.modified."country"="India"

__ACCOUNT__.modified."pobox"="12345678"
__ACCOUNT__.modified."orgunit"=new ObjectNotFoundException()
__ACCOUNT__.modified."telephone"="8152040605"
__ACCOUNT__.modified."islocked"="false"
__ACCOUNT__.modified."streetaddress"="GTP"
__ACCOUNT__.modified."displayname"="JK1DEC15"

__ACCOUNT__.modified."locale"="en"
__ACCOUNT__.modified."mobile"="8152040605"
__ACCOUNT__.modified."jobtitle"="SSE"
__ACCOUNT__.modified."ispassworddisabled"=new ObjectNotFoundException()
__ACCOUNT__.modified."city"="BangaloreUpdate"
__ACCOUNT__.modified."lastname"="testsssssss"
__ACCOUNT__.modified."title"="MR"
__ACCOUNT__.modified."__NAME__"=new ObjectNotFoundException()
__ACCOUNT__.modified."__CURRENT_ATTRIBUTES__"= getEmbeddedObject("ACTESTU506")

//__ACCOUNT__.modified."logonname"=new ObjectNotFoundException()

i1.modified.__ACCOUNT__."__NAME__"="000132251780"
i1.modified.__ACCOUNT__."logonname"="000132251780"

i2.Update.__ACCOUNT__."__NAME__"="000132251781"
i2.Update.__ACCOUNT__."logonname"="000132251781"

__ACCOUNT__.added."assignedroles"=new ObjectNotFoundException()
__ACCOUNT__.added."assignedgroups"=new ObjectNotFoundException()
Search.compareExistingObjectsByUidOnly = true



i1.Multi.__ACCOUNT__."__NAME__"="000132251780"
i1.Multi.__ACCOUNT__."logonname"="000132251780"

i2.Multi.__ACCOUNT__."__NAME__"="000132251781"
i2.Multi.__ACCOUNT__."logonname"="000132251781"

i3.Multi.__ACCOUNT__."__NAME__"="000132251782"
i3.Multi.__ACCOUNT__."logonname"="000132251782"

i4.Multi.__ACCOUNT__."__NAME__"="000132251783"
i4.Multi.__ACCOUNT__."logonname"="000132251783"

i5.Multi.__ACCOUNT__."__NAME__"="000132251784"
i5.Multi.__ACCOUNT__."logonname"="000132251784"

i6.Multi.__ACCOUNT__."__NAME__"="000132251785"
i6.Multi.__ACCOUNT__."logonname"="000132251785"

i7.Multi.__ACCOUNT__."__NAME__"="000132251786"
i7.Multi.__ACCOUNT__."logonname"="000132251786"

i8.Multi.__ACCOUNT__."__NAME__"="000132251787"
i8.Multi.__ACCOUNT__."logonname"="000132251787"

i9.Multi.__ACCOUNT__."__NAME__"="000132251788"
i9.Multi.__ACCOUNT__."logonname"="000132251788"

i10.Multi.__ACCOUNT__."__NAME__"="000132251789"
i10.Multi.__ACCOUNT__."logonname"="000132251789"

//i0.Multi.__ACCOUNT__."validto"="1451458003"


//i0.Multi.__ACCOUNT__.validto


__ACCOUNT__."requestReason;Header"="SURESH"
__ACCOUNT__."fname;UserInfo" ="suresh";
__ACCOUNT__."lname;UserInfo" = "kadiyala";
__ACCOUNT__."userId;UserInfo" = "ACTESTU2000"
__ACCOUNT__."email;UserInfo"  = "kadiyala@email.com";
__ACCOUNT__."bproc;Header" ="basis";
__ACCOUNT__."email;Header"="kadiyala@email.com";
__ACCOUNT__."reqInitSystem;Header"= "NT4@172.20.55.162";
__ACCOUNT__."priority;Header"="006";
__ACCOUNT__."telnumber;UserInfo"="8152040605";
__ACCOUNT__."requestorId;Header"="Header"
__ACCOUNT__."reqDueDate;Header"=new ObjectNotFoundException()
__ACCOUNT__."funcarea;Header"=new ObjectNotFoundException()
__ACCOUNT__."title;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__."accno;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__."logonLang;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__."manager;UserInfo"="BASIS";
__ACCOUNT__."managerEmail;UserInfo"="basis123@mphasis.com";
__ACCOUNT__."managerFirstname;UserInfo"="basisf";
__ACCOUNT__."managerLastname;UserInfo"="basisl";
__ACCOUNT__."department;UserInfo"="APPS"
__ACCOUNT__."personnelarea;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__."personnelno;UserInfo"="9986659812";
__ACCOUNT__."userLock;None"=new ObjectNotFoundException()
__ACCOUNT__."empposition;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__."RequestType"="ok";
__ACCOUNT__."validFrom;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__."validTo;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__."itemName;ReqLineItem"=new ObjectNotFoundException()
__ACCOUNT__."RequestId"=new ObjectNotFoundException() 
//account."itemName;ReqLineItem"
__ACCOUNT__."fax;UserInfo"="1234567890"
update."userId;UserInfo"="suresh1";
update."fname;UserInfo" ="suresh1231";
update."lname;UserInfo" = "kadiyala1231";
update."email;UserInfo"  = "kadiyala1@email.com";
update."requestReason;Header"= "kadiyala1";
update."bproc;Header" ="basis";
update."email;Header"="kadiyala@email.com";
update."reqInitSystem;Header"= "EH6CLNT800";
update."priority;Header"="006";
update."telnumber;UserInfo"="815204060511";
update."requestorId;Header"="Header"
update."__NAME__" ="USER.PRIVATE_DATASOURCE.un:ACTESTU2000~*~4927~Ok"
update."firstname" ="surejunit"  
//update."userLock;None"="1";
userid="suresh kadiyala"


i0.Create.__ACCOUNT__."__NAME__"="ACTESTU2000"
i1.Create.__ACCOUNT__."__NAME__"="ACTESTU2001"
i1.Create.__ACCOUNT__."userId;UserInfo" = "ACTESTU2001"
i0.Update.__ACCOUNT__."userId;UserInfo"="ACTESTU1200"
__ACCOUNT__.modified."__CURRENT_ATTRIBUTES__"= getEmbeddedObject("ACTESTU1200")
i0.Update.__ACCOUNT__."__NAME__"="ACTESTU1200"

i1.Update.__ACCOUNT__."userId;UserInfo"="ACTESTU1101"
i2.Update.__ACCOUNT__."userId;UserInfo"="ACTESTU1102"

i0.Update.__ACCOUNT__."fax;UserInfo"="111111111111"

i1.Update.__ACCOUNT__."__NAME__"="ACTESTU1101"
i2.Update.__ACCOUNT__."__NAME__"="ACTESTU1102"

i0.Update.__ACCOUNT__."email;UserInfo"="sandeep@gmail.com"
i0.Update.__ACCOUNT__."telnumber;UserInfo"="8152040605"
i0.Update.__ACCOUNT__."validFrom;UserInfo"=new ObjectNotFoundException()
i0.Update.__ACCOUNT__."validTo;UserInfo"=new ObjectNotFoundException()
i0.Update.__ACCOUNT__."manager;UserInfo"="basis"
i0.Update.__ACCOUNT__."requestReason;Header"="contarct tetsing"
i0.Update.__ACCOUNT__."itemName;ReqLineItem"=new ObjectNotFoundException()
__ACCOUNT__.modified."email;UserInfo"="sandeep@gmail.com"
__ACCOUNT__.modified."userId;UserInfo"="ACTESTU1200"
__ACCOUNT__.modified."telnumber;UserInfo"="8152040605"
__ACCOUNT__.modified."validFrom;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__.modified."validTo;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__.modified."manager;UserInfo"="basis"
__ACCOUNT__.modified."requestReason;Header"="contarct tetsing"
__ACCOUNT__.modified."itemName;ReqLineItem"=new ObjectNotFoundException()
__ACCOUNT__.modified."personnelno;UserInfo"="99999999999"
__ACCOUNT__.modified."userLock;None"=new ObjectNotFoundException()
__ACCOUNT__.modified."fax;UserInfo"="1234567890"
__ACCOUNT__.modified."personnelarea;UserInfo"="mr"
__ACCOUNT__.modified."fname;UserInfo"="testing contracts acume" 
__ACCOUNT__.modified."lname;UserInfo"="testsssssss" 
__ACCOUNT__.modified."empposition;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__.modified."department;UserInfo"="APPS"
__ACCOUNT__.modified."logonLang;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__.modified."email;Header"="kadiyala@email.com"
__ACCOUNT__.modified."managerEmail;UserInfo"="baaaasis123@mphasis.com";
__ACCOUNT__.modified."managerFirstname;UserInfo"="basisf"
__ACCOUNT__.modified."managerLastname;UserInfo"="basisl"
__ACCOUNT__.modified."reqDueDate;Header"=new ObjectNotFoundException()
__ACCOUNT__.modified."reqInitSystem;Header"= "NT4@172.20.55.162"
__ACCOUNT__.modified."priority;Header"="006";
__ACCOUNT__.modified."allassignedgroups"=new ObjectNotFoundException()
__ACCOUNT__.modified."allassignedroles"=new ObjectNotFoundException()
__ACCOUNT__.modified."password"=new ObjectNotFoundException()
__ACCOUNT__.modified."passwordchangerequired"=new ObjectNotFoundException()
__ACCOUNT__.modified."accno;UserInfo"=new ObjectNotFoundException()
__ACCOUNT__.modified."title;UserInfo"="MR"
__ACCOUNT__.modified."logonLang;UserInfo"="en"



