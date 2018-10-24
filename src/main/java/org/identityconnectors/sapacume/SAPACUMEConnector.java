/**
 * 
 */
package org.identityconnectors.sapacume;


import static org.identityconnectors.sapacume.SAPACUMEConstants.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.UpdateAttributeValuesOp;
import org.identityconnectors.sapume.SAPUMEConnection;
import org.identityconnectors.sapume.SAPUMEConnector;
import org.identityconnectors.sapume.SAPUMEUtil;
import org.identityconnectors.sapume.Update;
import org.openspml.message.Filter;
import org.openspml.message.FilterTerm;

/******************************* History of changes *********************************
 * Version		Date				Author					Description		
 ************************************************************************************
 * 1			Apr/2012				Ranjith		class created and methods added
 * 2			19/Jun/2012				Ranjith		doUpdate() method updated for Bug 14214721 
 * 3			27/Jun/2012				Ranjith		made changes to close code review comments 
 * 4			28/Jun/2012				Ranjith 	Updated getOtherLookups() method for Bug 14254657 
 * 5			18/Jul/2012				Ranjith		Updated for grc5.3 remove role issue 
 ************************************************************************************/

/**
 * @author ranjith.kumar
 * 
 */

@ConnectorClass(displayNameKey = "SAPACUME", configurationClass = SAPACUMEConfiguration.class, messageCatalogPaths = {"org/identityconnectors/sapacume/Messages","org/identityconnectors/sapume/Messages" })
public class SAPACUMEConnector implements Connector, SearchOp<Object>,
CreateOp, SchemaOp, UpdateAttributeValuesOp, TestOp {

	private static final Log log = Log.getLog(SAPACUMEConnector.class);

	private SAPACUMEConfiguration configuration;
	private SAPUMEConnection umeConnection = null;
	private SAPACUMESchema sapacumeSchema;
	//Start Bug 28284654 - SAP AC UME USER RECON BRINGS UNIQUEID INSTEAD OF UNIQUENAME FOR ROLES AND GROUPS
	private SAPUMEUtil sapumeUtil;
	//END Bug 28284654 - SAP AC UME USER RECON BRINGS UNIQUEID INSTEAD OF UNIQUENAME FOR ROLES AND GROUPS
	private SAPUMEConnector umeConnector;
	private Schema schema;
	private String requestTypeCode = null;
	private String requestTypeName = null;
	private String provAction = null;
	private String provItemAction = null;
	private String roleAction = null;
	Properties ac2umeMapProps = null;
	private String userIdName = null;
	/**
	 * It clears object references of the connector
	 */
	public void dispose() {
		configuration = null;
		schema = null;
		umeConnection = null;
		sapacumeSchema = null;
		umeConnector = null;
		ac2umeMapProps = null;
	}

	/**
	 * Getter method for Configuration object of the connector
	 * 
	 * @return
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Initializing the Configuration object of the connector
	 * 
	 * @param config
	 */
	public void init(Configuration config) {
		this.umeConnector = new SAPUMEConnector();
		this.configuration = (SAPACUMEConfiguration) config;
		umeConnector.init(configuration);// call to ume connector init method
		this.umeConnection = new SAPUMEConnection(configuration);
		//Start Bug 28284654 - SAP AC UME USER RECON BRINGS UNIQUEID INSTEAD OF UNIQUENAME FOR ROLES AND GROUPS
		sapumeUtil = new  SAPUMEUtil(this.umeConnection, configuration);
		//END Bug 28284654 - SAP AC UME USER RECON BRINGS UNIQUEID INSTEAD OF UNIQUENAME FOR ROLES AND GROUPS
		sapacumeSchema = new SAPACUMESchema(configuration, umeConnector);
		schema = sapacumeSchema.getSchema();
		ac2umeMapProps = sapacumeSchema.getUmeACMapProperties();
		userIdName = configuration.getIsGRC53()?USER_ID_53:ATTR_UI_USER_ID;
	}

	/**
	 * Providing the FilterTranslator for the connector. 
	 * It will be used to construct query parameter of executeQuery()
	 * 
	 * @param objectClass
	 * @param options
	 * @return
	 */
	public FilterTranslator<Object> createFilterTranslator(ObjectClass objectClass, OperationOptions options) {

		if (objectClass.is(STATUS)) {
			return new SAPACUMEFilterTranslator(true);
		} else if (objectClass.is(SYSTEM_RECON)
				|| objectClass.is(BUSPROC_RECON)
				|| objectClass.is(FUNCTIONAREA_RECON)
				|| objectClass.is(ITEMPROVACTIONTYPE)
				|| objectClass.is(PRIORITYTYPE_RECON)
				|| objectClass.is(REQTYPE)) {
			return new SAPACUMEFilterTranslator(false);
		} else {
			return umeConnector.createFilterTranslator(objectClass, options);
		}

	}

	/**
	 * Method executes the lookup operations with AC as part of SearchOp
	 * (1) If Object class is ACCOUNT it will invoke web service to retrieve request status 
	 * and update it in OIM
	 * (2) If Object class is ROLE, invokes web service to retrieve list of available roles in AC10
	 * (3) If Object class is SYSTEM, invokes web service to retrieve Application name from AC
	 * (4) Else it invokes web service to retrieve respective 
	 * lookup values based on ObjectClass value
	 * 
	 * @param objectClass
	 * @param query
	 * @param handler
	 * @param options
	 */
	public void executeQuery(ObjectClass objectClass, Object query,final ResultsHandler handler, OperationOptions options) {
		log.error("Perf: executeQuery Entered for query={0} objectClass = {1}",query,objectClass );
		log.info(configuration.getMessage("SAPACUME_INFO_QUERY_OBJCLASS"),query, objectClass);
		try {
			if (ROLE.equals(objectClass.getObjectClassValue())) {
				// call Role Lookup WS
				getRoles(objectClass, handler);
			} else if (SYSTEM_RECON.equals(objectClass.getObjectClassValue())) {
				// call Application Lookup WS
				getSystem(objectClass, handler);
			} else if (objectClass.is(BUSPROC_RECON)
					|| objectClass.is(FUNCTIONAREA_RECON)
					|| objectClass.is(ITEMPROVACTIONTYPE)
					|| objectClass.is(PRIORITYTYPE_RECON)
					|| objectClass.is(REQTYPE)) {
				// call Lookup WS
				getOtherLookups(objectClass, handler);
			} else if (objectClass.is(STATUS)) {
				// call Status Request WS - done
				getRequestStatus(query.toString(), handler, options);
			} else {
				final Set<String> actualAttrsToGetSet = new HashSet<String>(Arrays.asList(options.getAttributesToGet()));
				//Start : Bug 26167172 - AOB: SAP AC UME RECON IS NOT WORKING
				List<String> umeAttrsToGet = new ArrayList<String>();
				OperationOptionsBuilder builder = new OperationOptionsBuilder();
				Set<String> acSet = sapacumeSchema.getAc10attrSet();
				for(String attr: actualAttrsToGetSet){
					if(acSet.contains(attr)){
						String umAttr = addAttributes(attr);
						if(umAttr != null)
							umeAttrsToGet.add(umAttr);
					}else{
						umeAttrsToGet.add(attr);
					}
				}
				for (Map.Entry<String, Object> entry : options.getOptions().entrySet())
				{
					if (entry.getKey().equals("ATTRS_TO_GET")){
						builder.setAttributesToGet(umeAttrsToGet);
					}else{
						builder.setOption(entry.getKey(), entry.getValue()); 
					}
				}
				OperationOptions newOptionswithoutACattrs=builder.build();
				//End : Bug 26167172 - AOB: SAP AC UME RECON IS NOT WORKING
				String exeQuery = null;
				exeQuery = getValueFromFilter((Filter) query);
				if (exeQuery != null && exeQuery.contains(PATTERN_REQ_NO)) {
					final String qUid = exeQuery;
					final String[] uidReqIdReqType = splitUid(new Uid(exeQuery));
					ResultsHandler sapumeResultsHandler = new ResultsHandler() {

						public boolean handle(ConnectorObject umeCo) {

							String reqId = uidReqIdReqType[1];
							String status = null;
							try {
								status = getRequestStatusFromId(reqId);
							} catch (Exception e) {
								throw new ConnectorException(configuration.getMessage("SAPACUME_ERR_FAIL_TO_GET_REQ_STS") , e);
							}

							ConnectorObjectBuilder objectBuilder = addValidAttributes(actualAttrsToGetSet, umeCo);

							objectBuilder.addAttribute(AttributeBuilder.build(REQUESTID, reqId));
							objectBuilder.addAttribute(AttributeBuilder.build(ATTR_REQSTATUS, status));
							objectBuilder.addAttribute(AttributeBuilder.build(REQTYPE, uidReqIdReqType[2]));

							objectBuilder.setUid(qUid);
							objectBuilder.setName(umeCo.getName());
							objectBuilder.setObjectClass(umeCo.getObjectClass());
							return handler.handle(objectBuilder.build());
						}
					};
		             //  For Writeback called during Create, Update or Delete flows, Need to check if
                    // user is present before making a call to SAP UME's execute query
					Update update = new Update(configuration, umeConnection);
					String uniqueId = update.getUniqueIdentifier(uidReqIdReqType[0]);
					if( uniqueId != null){
						Filter oFilter = new Filter();
						FilterTerm oSub1FilterTerm = new FilterTerm();
						oSub1FilterTerm.setOperation(FilterTerm.OP_EQUAL);
						oSub1FilterTerm.setName(ID);
						oSub1FilterTerm.setValue(uniqueId);
						oFilter.addTerm(oSub1FilterTerm);
						///***********************options replaced with newOptionswithoutACattrs
						umeConnector.executeQuery(objectClass, oFilter,sapumeResultsHandler, newOptionswithoutACattrs);
						//*****************
					} else {
						ConnectorObjectBuilder acConnObj = new ConnectorObjectBuilder();
						acConnObj.setUid(uidReqIdReqType[0]);
						acConnObj.setName(uidReqIdReqType[0]);
						acConnObj.setObjectClass(ObjectClass.ACCOUNT);
						sapumeResultsHandler.handle(acConnObj.build());
					}
				} else {
					ResultsHandler umeResultsHandler = new ResultsHandler() {
						public boolean handle(ConnectorObject umeCo) {
							if (!ObjectClass.ACCOUNT.equals(umeCo.getObjectClass()))
								return handler.handle(umeCo);
							ConnectorObjectBuilder objectBuilder = addValidAttributes(actualAttrsToGetSet, umeCo);
							objectBuilder.setUid(umeCo.getUid());
							objectBuilder.setName(umeCo.getName());
							objectBuilder.setObjectClass(umeCo.getObjectClass());
							return handler.handle(objectBuilder.build());
						}
					};
					//************************options replaced with newOptionswithoutACattrs
					umeConnector.executeQuery(objectClass, query,umeResultsHandler, newOptionswithoutACattrs);
					//***********************
				}
			}
		} catch (Exception e) {
			log.error(e, configuration.getMessage("SAPACUME_ERR_EXECUTE_QUERY"));
			throw new ConnectorException(e);
		}
		log.error("Perf: executeQuery Exit for query={0} objectClass = {1}", query,objectClass );
	}

	/**
	 * @param exeQuery
	 * @param filter
	 * @return
	 */
	private String getValueFromFilter(Filter filter) {
		String exeQuery = null;
		if (filter != null) {
			for (Object object : filter.getTerms()) {
				FilterTerm filterTerm = (FilterTerm) object;
				if ((ID.equalsIgnoreCase(filterTerm.getName()) || (LOGONNAME.equalsIgnoreCase(filterTerm.getName())))) {
					exeQuery = filterTerm.getValue().toString();
				}
			}
		}
		return exeQuery;
	}

	/**
	 * Method of UpdateOp. Invoked in modify flow of connector
	 * 
	 * @param objectClass
	 * @param uid
	 * @param attrs
	 * @param options
	 * @returnlock
	 */
	public Uid update(ObjectClass objectClass, Uid uid, Set<Attribute> attrs,OperationOptions options) {
		log.info(configuration.getMessage("SAPACUME_INFO_UPDATE_UID_OBJCLASS"), uid,objectClass);
		log.error(" Perf: update() Entered for Uid : {0}", uid.getUidValue());
		log.info(configuration.getMessage("SAPACUME_INFO_ATTRIBUTE_OPTIONS"), attrs,options.getOptions());
		// need to check if attributes is AC or NONAC
		Set<String> ac10or53AttrSet = null;
		Uid rUid = null;
		String reqTypeOption = null;
		String actualUID = null;
		Set<Attribute> acUpdateSet = new HashSet<Attribute>();
		Set<Attribute> umeUpdateSet = new HashSet<Attribute>();
		if (configuration.getIsGRC53()) {
			ac10or53AttrSet = sapacumeSchema.getAc53attrSet();
		} else {
			ac10or53AttrSet = sapacumeSchema.getAc10attrSet();
		}
		for (Attribute attribute : attrs) {
			if (attribute.getName().equals(Uid.NAME)) {
				actualUID = splitUid(new Uid(AttributeUtil.getAsStringValue(attribute)))[0];
				attribute = AttributeBuilder.build(attribute.getName(),actualUID);
			}
			if ((attribute.getName().equals(OperationalAttributes.ENABLE_NAME))
					|| (attribute.getName().equals(OperationalAttributes.CURRENT_ATTRIBUTES))
					|| (ac10or53AttrSet.contains(attribute.getName()))) {
				acUpdateSet.add(attribute);
			} else {
				umeUpdateSet.add(attribute);
			}

		}
		if (options != null) {
			reqTypeOption = (String) options.getOptions().get(ATTR_REQ_TYPE);
		}
		String str[] = splitUid(uid);
		String requestId = str[1];
		String requestType = str[2];
		Uid userId = new Uid(str[0]);
		
		if (DELETE_REQ_TYPE.equalsIgnoreCase(reqTypeOption)) {
			return doUpdate(acUpdateSet, null, options, requestId);
		}
		if ((AttributeUtil.getCurrentAttributes(acUpdateSet)!=null && acUpdateSet.size() > 1)) {
			rUid = doUpdate(acUpdateSet, null, options, requestId);
		}

		if (!umeUpdateSet.isEmpty()) {
			Set<Attribute> acCurrentAttrs = AttributeUtil.getCurrentAttributes(attrs);	
			if(acCurrentAttrs!= null){			
				Attribute acUserIdAttr = AttributeUtil.find(userIdName, acCurrentAttrs);
				if(acUserIdAttr!=null){
					Set<Attribute> umeCurrentAttrs =  new HashSet<Attribute>();
					umeCurrentAttrs.add(AttributeBuilder.build(Name.NAME, AttributeUtil.getAsStringValue(acUserIdAttr)));
					umeUpdateSet.add(AttributeBuilder.buildCurrentAttributes(ObjectClass.ACCOUNT, umeCurrentAttrs));
				}
			}	

			Uid umeUid = umeConnector.update(objectClass, userId, umeUpdateSet,options);
			// We are changing rUid value for non ac fields update only. umeUid holds unique identifier, so we are not using that.
			if (rUid == null) {
				rUid = new Uid(userId.getUidValue() + PATTERN_REQ_NO +requestId+ PATTERN_REQ_TYPE +requestType);
			}
		}
		log.error("Perf: update() Exit for Uid :{0}" ,uid.getUidValue()); 
		return rUid;
	}

	/**
	 * Method of CreateOp. Invoked during AC user provisioning Also mapped to
	 * process Delete/Enable/Disable user flows of connector
	 * 
	 * @param objectClass
	 * @param attrs
	 * @param options
	 * 
	 */
	public Uid create(ObjectClass objectClass, Set<Attribute> attrs,OperationOptions options) {
		
		log.info(configuration.getMessage("SAPACUME_INFO_CREATE_OBJCLASS"), objectClass); 
		log.error("Perf: create() Entered for Uid :{0}",getUserIdValue(attrs));
		log.info(configuration.getMessage("SAPACUME_INFO_ATTRIBUTE_OPTIONS"), attrs,options.getOptions());
		// Note: attrs doesn't support to add new attributes so using new Map
		// object
		Map<String, Attribute> createAttrMap = new HashMap<String, Attribute>(AttributeUtil.toMap(attrs));
		// request status check not required for create flow operation
		/*
		 * if(!canProcessRequest(createAttrMap)){ throw new ConnectorException(
		 * "Previous request needs to process before this request"); }
		 */
		Uid uid = null;
		splitReqType(configuration.getCreateUserReqType());
		Attribute reqTypeAttr = AttributeBuilder.build(configuration.getRequestTypeAttrName(), requestTypeCode);
		createAttrMap.put(configuration.getRequestTypeAttrName(), reqTypeAttr);
		if (provAction != null) {
			Attribute provActionAttr = AttributeBuilder.build(configuration.getProvActionAttrName(), provAction);
			createAttrMap.put(configuration.getProvActionAttrName(),provActionAttr);
		}
		if (provItemAction != null) {
			Attribute provItemActionAttr = AttributeBuilder.build(configuration.getProvItemActionAttrName(), provItemAction);
			createAttrMap.put(configuration.getProvItemActionAttrName(),provItemActionAttr);
		}
		uid = initRequest(createAttrMap);
		log.error("Perf: create() Exit for Uid :{0}", uid.getUidValue());
		return uid;
	}

	/**
	 * Initializing connector schema
	 * 
	 * @return
	 */
	public Schema schema() {
		if (schema == null) {
			sapacumeSchema = new SAPACUMESchema(configuration, umeConnector);
			schema = sapacumeSchema.getSchema();
		}
		return schema;
	}

	/**
	 * Method initializing user provisioning request to Access Control web
	 * service
	 * 
	 * @param attrMap
	 * @return
	 */
	private Uid initRequest(Map<String, Attribute> attrMap) {
		Map<String, Object> outputMap = null;
		Map<String, Object> inputDataMap = populateInputData(attrMap);
		Uid uidReqId = null;
		try {
			Class<? extends Object> submitReqClientClazz = Class.forName(configuration.getUserAccessWS());
			Object serviceInstance = submitReqClientClazz.newInstance();
			Method subReqMethod = submitReqClientClazz.getMethod(SERVICE_METHODNAME, Map.class);
			outputMap = (Map<String, Object>)subReqMethod.invoke(serviceInstance, inputDataMap);

			String requestId = validateStatus(outputMap);
			// add a config parameter as mapToUID 
			String userIdValue = getUserIdValue(attrMap.values());
			if (userIdValue == null) {
				throw new ConnectorException(configuration.getMessage("SAPACUME_ERR_USER_ID_NOT_EMPTY"));
			}
			uidReqId = new Uid(userIdValue + PATTERN_REQ_NO + requestId + PATTERN_REQ_TYPE + requestTypeName);
			log.info(configuration.getMessage("SAPACUME_INFO_UID"), uidReqId);
		} catch (Exception e) {
			log.error(e, configuration.getMessage("SAPACUME_ERR_INIT_REQUEST_FAILED"));
			throw new ConnectorException(e);
		}
		return uidReqId;
	}

	/**
	 * Method validating the status from web service.
	 * Method do validation based on the status string starting character.
	 * If 0, web service call is success
	 * If 4, web service fails to execute the request
	 * If 1, web service Client fails to execute the web service call
	 * 
	 * @param outputMap
	 * @return
	 */
	private String validateStatus(Map<String, Object> outputMap) {
		ConnectorException ce = null;
		String requestNumber = null;
		String status = outputMap.get(STATUS).toString();
		log.info(configuration.getMessage("SAPACUME_INFO_WEBSERVICE_STATUS"), status);
		if (status.startsWith(DIGIT_ZERO)) {
			// Return Request number returned from submit request web service
			// client
			log.info( configuration.getMessage("SAPACUME_INFO_CALL_SERVICE_SUCCESS"));
			if (outputMap.get(REQUESTNO) != null) {
				requestNumber = (String) outputMap.get(REQUESTNO);
			}
		} else if (status.startsWith(DIGIT_ONE)) {
			// Log the exception and Return failure response
			ce = new ConnectorException(outputMap.get(STATUS).toString());
			log.error(ce, outputMap.get(STATUS).toString());
			throw ce;
		} else if (status.startsWith(DIGIT_FOUR)) {
			// If 'Log Audit Trial' = 'Yes' in Configuration,
			// then log the complete audit trial of the request in log file.
			// no requestno if request failed in target system.
			// if(VALUE_YES.equalsIgnoreCase(configuration.getLogAuditTrial())){
			// logAuditTrial(resultMap.get(REQUESTNO));
			// }
			ce = new ConnectorException(outputMap.get(STATUS).toString());
			log.error(ce, outputMap.get(STATUS).toString());
			throw ce;
		} else {
			// unexpected case
			ce = new ConnectorException(configuration.getMessage("SAPACUME_ERR_CALL_SERVICE"));
			log.error(ce.getMessage()); 
			throw ce;
		}
		return requestNumber;
	}

	/**
	 * Method populating the Form data in input data of web service
	 * 
	 * @param attrMap
	 * @return
	 */
	private Map<String, Object> populateInputData(Map<String, Attribute> attrMap) {
		Map<String, Object> inputDataMap = new HashMap<String, Object>(10,0.75f);
		inputDataMap.putAll(getConfigurationMap());
		inputDataMap.put(FORMDATA, attrMap);
		return inputDataMap;
	}

	/**
	 * Method populating request number in input data
	 * 
	 * @param requestNo
	 * @return
	 */
	private Map<String, Object> populateReqStatusInputData(String requestNo) {
		Map<String, Object> inputDataMap = new HashMap<String, Object>(10,0.75f);
		inputDataMap.putAll(getConfigurationMap());
		inputDataMap.put(REQUESTNO, requestNo);
		return inputDataMap;
	}

	/**
	 * Method provide a map with details in connector configuration object
	 * 
	 * @return
	 */
	private Map<String, Object> getConfigurationMap() {
		Map<String, Object> configMap = new HashMap<String, Object>(5, 0.75f);
		configMap.put(CONFIGURATION, configuration.toMap());
		log.info(configuration.getMessage("SAPACUME_INFO_CONFIG_MAP"), configMap);
		return configMap;
	}

	/**
	 * Method updates lookups
	 * 
	 * @param attrsToGet
	 * @param objClass
	 * @param allObjects
	 * @param handler
	 */
	private void createConnectorObject(List<String> attrsToGet,ObjectClass objClass, Map allObjects, ResultsHandler handler) {
		ConnectorObjectBuilder objectBuilder = new ConnectorObjectBuilder();
		String codekey = attrsToGet.get(0);
		String decodekey = attrsToGet.get(1);
		Set<String> keySet = allObjects.keySet();
		for (String key : keySet) {
			objectBuilder.addAttribute(AttributeBuilder.build(codekey, key));
			if (allObjects.get(key) == null) {
				objectBuilder.addAttribute(AttributeBuilder.build(decodekey,key));
			} else {
				objectBuilder.addAttribute(AttributeBuilder.build(decodekey,allObjects.get(key).toString()));
			}
			objectBuilder.setUid(key.toString());
			objectBuilder.setName(key.toString());

			objectBuilder.setObjectClass(objClass);
			handler.handle(objectBuilder.build());
		}
	}

	/**
	 * Method filtering request type from configuration lookup
	 * 
	 * @param reqType
	 */
	private void splitReqType(String reqType) {
		log.info(configuration.getMessage("SAPACUME_INFO_REQUEST_TYPE"), reqType);
		String[] type = reqType.split(PATTERN_REQ_TYPE);
		if (type.length == 1) {// for grc5.3
			String[] actionNtype = null;
			if (type[0].contains(PATTERN_SEMI_COLON)) {
				actionNtype = type[0].split(PATTERN_SEMI_COLON);
				requestTypeCode = actionNtype[0];
				requestTypeName = actionNtype[0];
				roleAction = actionNtype[1];
			} else {
				requestTypeCode = type[0];
				requestTypeName = type[0];
			}
		} else if (type.length > 1) {// for grc10
			requestTypeCode = type[0];
			requestTypeName = type[1];
			provAction = type[2];
			if (type.length > 3) {
				provItemAction = type[3];
			}
		}
	}

	/**
	 * Fetching userid, request id and request type from the UID
	 * 
	 * @param uid
	 * @return userid, request id and request type in string array
	 */
	public static String[] splitUid(Uid uid) {
		if (uid.getUidValue().contains(PATTERN_REQ_NO)) {
			String[] uidReqId = uid.getUidValue().split("\\~\\*\\~");
			String[] ReqIdReqType = uidReqId[1].split("\\~");
			String[] uidReqIdReqType = { uidReqId[0], ReqIdReqType[0],ReqIdReqType[1] };
			return uidReqIdReqType;
		} else {
			String[] uidReqIdReqType = { uid.getUidValue(), null, null };
			return uidReqIdReqType;
		}
	}

	/**
	 * Method invokes request status web service and updates writeback and recon
	 * fields of OIM
	 * 
	 * @param query
	 * @param handler
	 * @param options
	 * @throws Exception
	 */

	private void getRequestStatus(String query, ResultsHandler handler,OperationOptions options) throws Exception {
		ConnectorObjectBuilder objectBuilder = new ConnectorObjectBuilder();
		String reqNo = null;
		String userIDRecon = null;
		String reqstatus = null;
		String reqType = null;

		if (query.contains(PATTERN_AND)) {
			log.info(configuration.getMessage("SAPACUME_INFO_REQUEST_STATUS_FOR_RECON_JOB"));
			String[] qArray = null;
			String[] parameterArray = query.split(PATTERN_AND);
			for (String parameter : parameterArray) {
				if (parameter.contains(PATTERN_EQUALTO)) {
					qArray = parameter.split(PATTERN_EQUALTO);
					if (qArray.length == 2) {
						if (USERID.equalsIgnoreCase(qArray[0].trim())) {
							userIDRecon = qArray[qArray.length - 1].trim();
						} else if (REQUEST_STATUS_LOGON_NAME.equalsIgnoreCase(qArray[0].trim())) {
							userIDRecon = qArray[qArray.length - 1].trim();
						} else if (Uid.NAME.equalsIgnoreCase(qArray[0].trim())) {
							reqNo = qArray[qArray.length - 1].trim();
						} else if (REQTYPE.equalsIgnoreCase(qArray[0].trim())) {
							reqType = qArray[qArray.length - 1].trim();
						}
					}
				} else {
					reqNo = parameter.trim();
				}
			}

			if (reqNo != null) {
				reqstatus = getRequestStatusFromId(reqNo);
				if (reqstatus != null) {
					for (String attrToGet : options.getAttributesToGet()) {
						if (attrToGet.equalsIgnoreCase(ATTR_REQSTATUS)) {
							objectBuilder.addAttribute(ATTR_REQSTATUS,
									reqstatus);
						} else if (attrToGet.equalsIgnoreCase(USERID)) {
							objectBuilder.addAttribute(USERID, userIDRecon);
						} else if (attrToGet.equalsIgnoreCase(REQUEST_STATUS_LOGON_NAME)) {
							objectBuilder.addAttribute(REQUEST_STATUS_LOGON_NAME, userIDRecon);
						} else if (attrToGet.equalsIgnoreCase(REQUESTID)) {
							objectBuilder.addAttribute(REQUESTID, reqNo);
						} else if (attrToGet.equalsIgnoreCase(REQTYPE)) {
							objectBuilder.addAttribute(REQTYPE, reqType);
						}
					}
					objectBuilder.setUid(reqNo);
					objectBuilder.setName(userIDRecon);
					objectBuilder.setObjectClass(new ObjectClass(STATUS));
					ConnectorObject co = objectBuilder.build();

					if (co != null) {
						log.info(configuration.getMessage("SAPACUME_INFO_INVOKE_HANDLER"));
						handler.handle(co);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param objectClass
	 * @param handler
	 * @throws Exception
	 */
	private void getRoles(ObjectClass objectClass, ResultsHandler handler)throws Exception {
		List<String> valuesToGet = new ArrayList<String>();
		valuesToGet.add(roleCodeKeyDecode[0]);
		valuesToGet.add(roleCodeKeyDecode[1]);
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.putAll(getConfigurationMap());
		Map<String, Object> outputMap = invokeCallService(configuration.getRoleLookupWS(), inputMap);
		validateStatus(outputMap);
		createConnectorObject(valuesToGet, objectClass,(Map) outputMap.get(RESULT), handler);
	}

	/**
	 * 
	 * @param objectClass
	 * @param handler
	 * @throws Exception
	 */
	private void getSystem(ObjectClass objectClass, ResultsHandler handler)throws Exception {
		List<String> valuesToGet = new ArrayList<String>();
		valuesToGet.add(applCodeKeyDecode[0]);
		valuesToGet.add(applCodeKeyDecode[1]);
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.putAll(getConfigurationMap());
		Map<String, Object> outputMap = invokeCallService(configuration.getAppLookupWS(), inputMap);
		validateStatus(outputMap);
		createConnectorObject(valuesToGet, objectClass,(Map) outputMap.get(RESULT), handler);
	}

	/**
	 * Method invokes other lookup web service to retrieve values with respect
	 * to ObjectClass This is used only in AC10 lookups
	 * 
	 * @param objectClass
	 * @param handler
	 * @throws Exception
	 */
	private void getOtherLookups(ObjectClass objectClass, ResultsHandler handler)throws Exception {
		List<String> valuesToGet = new ArrayList<String>();
		valuesToGet.add(lookupCodeKeyDecode[0]);
		valuesToGet.add(lookupCodeKeyDecode[1]);
		Map<String, Object> outputMap = null;
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.putAll(getConfigurationMap());

		if (ITEMPROVACTIONTYPE.equalsIgnoreCase(objectClass.getObjectClassValue())) {
			outputMap = getOtherLookups(ITEMPROVTYPE, inputMap);
			Map<String, String> itemProvTypeMap = (Map) outputMap.get(RESULT);

			if (itemProvTypeMap != null) {
				outputMap = new HashMap<String, Object>(5, 0.75f);
				Map<String, Object> resultMap = new HashMap<String, Object>(10,0.75f);
				for (String itemProvType : itemProvTypeMap.keySet()) {
					inputMap.put(ITEMTYPE, itemProvType);
					resultMap.putAll((Map) getOtherLookups(objectClass.getObjectClassValue(), inputMap).get(RESULT));
				}
				outputMap.put(RESULT, resultMap);
			}
		} else {
			outputMap = getOtherLookups(objectClass.getObjectClassValue(),inputMap);
		}
		createConnectorObject(valuesToGet, objectClass,(Map) outputMap.get(RESULT), handler);
	}

	/**
	 * 
	 * @param objectClassName
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getOtherLookups(String objectClassName,Map<String, Object> inputMap) throws Exception {
		Map<String, Object> outputMap = null;
		inputMap.put(OBJECTCLASS, objectClassName);
		outputMap = invokeCallService(configuration.getOtherLookupWS(),inputMap);
		validateStatus(outputMap);
		return outputMap;
	}

	/**
	 * Generic method to invoke web service
	 * 
	 * @param className
	 * @param inputDataMap
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> invokeCallService(String className,Map<String, Object> inputDataMap) throws Exception {
		Map<String, Object> resultMap = null;
		Class<?> statusReqClientClazz = Class.forName(className);
		Object serviceInstance = statusReqClientClazz.newInstance();
		Method statusReqMethod = statusReqClientClazz.getMethod(SERVICE_METHODNAME, Map.class);
		resultMap = (Map<String, Object>) statusReqMethod.invoke(serviceInstance, inputDataMap);
		return resultMap;
	}

	/**
	 * Method returns validthru date as mill seconds. For Enable user, It
	 * returns maximum date "31/12/2500". For Disable user, It returns Current
	 * date.
	 * 
	 * @param isEnable
	 * @return
	 */
	private Long getValidThru(Boolean isEnable) {
		Long milliSec = null;
		Date dtValidThro = null;
		DateUtil dtUtil = new DateUtil();
		try {
			if (isEnable) {
				dtValidThro = dtUtil.returnDate("2500-12-31", "yyyy-MM-dd");
				String sDate = dtUtil.parseTime(dtValidThro, "yyyy-MM-dd");
				dtValidThro = dtUtil.returnDate(sDate, "yyyy-MM-dd");
				milliSec = dtValidThro.getTime();
			} else {
				dtValidThro = new Date();
				milliSec = dtValidThro.getTime();
			}
		} catch (ConnectorException eException) {
			log.error(eException, configuration.getMessage("SAPACUME_ERR_INIT_VALID_THRU"));
			throw ConnectorException.wrap(eException);
		}
		return milliSec;
	}

	/**
	 * Method returns false if the previous request is in pending status when
	 * configuration parameter IgnoreOpenStatus is NO
	 * 
	 * @param attrMap
	 * @return
	 */
	private boolean canProcessRequestId(String requestId) {
		boolean canProcessRequest = true;
		String requestStatus = null;
		// grc10="OK"
		// grc53="CLOSED"

		try {
			if (requestId != null) {
				requestStatus = getRequestStatusFromId(requestId);
				if (VALUE_NO.equalsIgnoreCase(configuration.getIgnoreOpenStatus())
						&& !(VALUE_OK.equalsIgnoreCase(requestStatus) || CLOSED.equalsIgnoreCase(requestStatus))) {
					canProcessRequest = false;
				}
			} else {
				// canProcessRequest = false;
				log.warn(configuration.getMessage("SAPACUME_WARN_REQSTATUS_ATTR_NOT_FOUND"));
			}
		} catch (Exception e) {
			throw new ConnectorException("Failed to get the Request Status", e);
		}
		return canProcessRequest;
	}


	/**
	 * Method includes logic for update flow Extracting required data from
	 * Attribute set and initiating the request to web service client
	 * 
	 * @param attrs
	 * @return
	 */
	private Uid doUpdate(Set<Attribute> attrs, String flowType,OperationOptions operationOptions, String requestId) {
		Uid rUid = null;
		Map<String, Attribute> updateAttrMap = null;
		Map<String, Attribute> modifiedAttrValMap = new HashMap<String, Attribute>(AttributeUtil.toMap(attrs));
		// if(!canProcessRequest(modifiedAttrValMap)){
		if (!canProcessRequestId(requestId)) {
			throw new ConnectorException(configuration.getMessage("SAPACUME_ERR_PRE_REQ_NOT_APPRVED_YET")); 
		}
		if (modifiedAttrValMap.keySet().contains(DECODE_USERLOCK)) {
			// Below condition modified to support Lock and Unlock user in SAP
			// UME --Jagadeesh
			if(AttributeUtil.getStringValue(modifiedAttrValMap.get(DECODE_USERLOCK)).equalsIgnoreCase(TRUE)){
				splitReqType(configuration.getLockUserReqType());
			} else {
				splitReqType(configuration.getUnlockUserReqType());
			}
		}
		// Below condition is added to support SAP UME with AC10 roles --
		// Jagadeesh
		// Start Bug 23743186- SAP UME: ROLE REQUEST IS NOT Generating Request ID IN GRC - Condition modified
		else if(modifiedAttrValMap.containsKey(UMEROLE) || modifiedAttrValMap.containsKey(UMEGROUP)){
			if (ADD_CHILD.equals(flowType)) {
				splitReqType(configuration.getAssignRoleReqType());
			} else if (REMOVE_CHILD.equals(flowType)) {
				splitReqType(configuration.getRemoveRoleReqType());
			}
		} else {
			splitReqType(configuration.getModifyUserReqType());
		}

		String reqTypeOption = (String) operationOptions.getOptions().get(ATTR_REQ_TYPE);

		Set<Attribute> currentAttrSet = AttributeUtil.getCurrentAttributes(attrs);
		if (currentAttrSet != null && !currentAttrSet.isEmpty()) {
			// Note: attrs doesn't support to add new attributes so using new
			// Set object
			updateAttrMap = new HashMap<String, Attribute>(AttributeUtil.toMap(currentAttrSet));
		} else {
			updateAttrMap = new HashMap<String, Attribute>();
		}
		modifiedAttrValMap.remove(OperationalAttributes.CURRENT_ATTRIBUTES);
		// Note: replace with new value
		for (String attrName : modifiedAttrValMap.keySet()) {
			//START Bug 28284654 - SAP AC UME USER RECON BRINGS UNIQUEID INSTEAD OF UNIQUENAME FOR ROLES AND GROUPS
			if(modifiedAttrValMap.containsKey(UMEROLE) || modifiedAttrValMap.containsKey(UMEGROUP)){
				String attrValue = sapumeUtil.getUniqueNamefromUniqueId(attrName,AttributeUtil.getAsStringValue(modifiedAttrValMap.get(attrName)));
				updateAttrMap.put(attrName,AttributeBuilder.build(attrName, attrValue));
			}else{
			//END Bug 28284654 - SAP AC UME USER RECON BRINGS UNIQUEID INSTEAD OF UNIQUENAME FOR ROLES AND GROUPS
				updateAttrMap.put(attrName, modifiedAttrValMap.get(attrName));
			}
		}
		// start SAP AC -Enable/Disable update
		if (modifiedAttrValMap.keySet().contains(OperationalAttributes.ENABLE_NAME)) {

			splitReqType(configuration.getModifyUserReqType());
			Long validToTimestamp = null;
			Attribute validToAttr = null;

			boolean isEnabled = AttributeUtil.getBooleanValue(modifiedAttrValMap.get(OperationalAttributes.ENABLE_NAME));
			validToTimestamp = getValidThru(isEnabled);
			validToAttr = AttributeBuilder.build(ATTR_UI_VALID_TO,validToTimestamp);
			updateAttrMap.put(ATTR_UI_VALID_TO, validToAttr);
			validToAttr = AttributeBuilder.build(VALIDTO, validToTimestamp);
			updateAttrMap.put(VALIDTO, validToAttr);
		}
		// end SAP AC -Enable/Disable update

		// Start handling delete functionality
		if (DELETE_REQ_TYPE.equalsIgnoreCase(reqTypeOption)) {
			splitReqType(configuration.getDeleteUserReqType());
		}
		// End handling delete functionality

		Attribute reqTypeAttr = AttributeBuilder.build(configuration.getRequestTypeAttrName(), requestTypeCode);
		updateAttrMap.put(configuration.getRequestTypeAttrName(), reqTypeAttr);

		if (provAction != null) {
			Attribute provActionAttr = AttributeBuilder.build(configuration.getProvActionAttrName(), provAction);
			updateAttrMap.put(configuration.getProvActionAttrName(),provActionAttr);
		}

		if (provItemAction != null) {
			Attribute provItemActionAttr = AttributeBuilder.build(configuration.getProvItemActionAttrName(), provItemAction);
			updateAttrMap.put(configuration.getProvItemActionAttrName(),provItemActionAttr);
		}

		if (roleAction != null) {// for grc5.3
			Attribute actionAttr = AttributeBuilder.build(ACTION, roleAction);
			updateAttrMap.put(ACTION, actionAttr);
		}

		rUid = initRequest(updateAttrMap);
		return rUid;
	}

	/**
	 * Method to handle add child table, It invokes update flow with child
	 * details
	 * 
	 * @param objectClass
	 * @param uid
	 * @param attrs
	 * @param options
	 * @return
	 */
	public Uid addAttributeValues(ObjectClass objectClass, Uid uid,Set<Attribute> attrs, OperationOptions options) {
		log.info(configuration.getMessage("SAPACUME_INFO_UID_OBJCLASS"), uid, objectClass);
		log.error("Perf: addAttributeValues() Entered for user {0}", uid.getUidValue());
		log.info(configuration.getMessage("SAPACUME_INFO_ATTRIBUTE_OPTIONS"), attrs,options.getOptions());
		Uid rUid = doUpdate(attrs, ADD_CHILD, options, splitUid(uid)[1]);
		log.error("Perf: addAttributeValues() Exit for user {0}", uid.getUidValue());
		return rUid;
	}

	/**
	 * Method to handle remove child table, It invokes update flow with child
	 * details
	 * 
	 * @param objectClass
	 * @param uid
	 * @param attrs
	 * @param options
	 * @return
	 * 
	 */
	public Uid removeAttributeValues(ObjectClass objectClass, Uid uid,Set<Attribute> attrs, OperationOptions options) {
		log.info(configuration.getMessage("SAPACUME_INFO_UID_OBJCLASS"), uid, objectClass);
		log.error("Perf: removeAttributeValues() Entered for user {0}", uid.getUidValue());
		log.info(configuration.getMessage("SAPACUME_INFO_ATTRIBUTE_OPTIONS"), attrs,options.getOptions());
		Uid rUid = doUpdate(attrs, REMOVE_CHILD, options, splitUid(uid)[1]);
		log.error("Perf: removeAttributeValues() Exit for user {0}", uid.getUidValue().toString());
		return rUid;
	}

	/**
	 * Method parsing userid from given collection
	 * 
	 * @param createAttrMap
	 * @return
	 */
	private String getUserIdValue(Collection<Attribute> attributes) {
		String userIdValue = null;
		for (Attribute attr : attributes) {
			if (attr.getName().equalsIgnoreCase(userIdName)) {
				userIdValue = AttributeUtil.getAsStringValue(attr);
				break;
			}
		}
		return userIdValue;
	}

	/**
	 * 
	 * @param requestId
	 * @return
	 * @throws Exception
	 */
	private String getRequestStatusFromId(String requestId) throws Exception {

		Map<String, Object> resultMap = null;
		Map<String, Object> inputDataMap = populateReqStatusInputData(requestId);
		resultMap = invokeCallService(configuration.getRequestStatusWS(),inputDataMap);
		return resultMap.get(ATTR_REQSTATUS).toString();
	}

	public void test() {
		try {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.putAll(getConfigurationMap());
			Map<String, Object> outputMap = invokeCallService(configuration.getRoleLookupWS(), inputMap);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new ConnectorException(configuration.getMessage("SAPACUME_ERR_CONNECTION_FAILED"), e);
		}
	}

	/**
	 * @param actualAttrsToGetSet
	 * @param umeCo
	 * @return
	 */
	
	//Start : Bug 26167172 - AOB: SAP AC UME RECON IS NOT WORKING
	private String addAttributes(String acAttrs) {
		String backendAcAttrname = ac2umeMapProps.getProperty(acAttrs);					
		if(backendAcAttrname!=null){
		return backendAcAttrname;
			}
		return null;
	}
	//End : Bug 26167172 - AOB: SAP AC UME RECON IS NOT WORKING
	
	private ConnectorObjectBuilder addValidAttributes(Set<String> actualAttrsToGetSet, ConnectorObject umeCo) {
		ConnectorObjectBuilder objectBuilder = new ConnectorObjectBuilder();
		for (Attribute attribute : umeCo.getAttributes()) {
			String umeAttrName = attribute.getName();
			String acAttrname = ac2umeMapProps.getProperty(umeAttrName);
			if (acAttrname != null && actualAttrsToGetSet.contains(acAttrname))
				objectBuilder.addAttribute(AttributeBuilder.build(acAttrname,attribute.getValue()));
			if (umeAttrName != null && actualAttrsToGetSet.contains(umeAttrName))
				objectBuilder.addAttribute(attribute);
		}
		return objectBuilder;
	}

}

