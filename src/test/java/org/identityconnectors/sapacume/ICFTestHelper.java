/* $Header: idc/bundles/java/sapacume/src/test/java/org/identityconnectors/sapacume/ICFTestHelper.java /main/1 2015/10/05 23:34:38 smelgiri Exp $ */

/* Copyright (c) 2013, 2015, Oracle and/or its affiliates. 
All rights reserved.*/

/*
   DESCRIPTION
    <short description of component this file declares/defines>

   PRIVATE CLASSES
    <list of private classes defined - with one-line descriptions>

   NOTES
    <other useful comments, qualifications, etc.>

   MODIFIED    (MM/DD/YY)
   jagadeeshkumar.r     10/15/2014 - Creation
 */

/**
 *  @author  jagadeeshkumar.r 
 */

package org.identityconnectors.sapacume.test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConfigurationProperties;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.framework.api.ConnectorInfo;
import org.identityconnectors.framework.api.ConnectorInfoManager;
import org.identityconnectors.framework.api.ConnectorInfoManagerFactory;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.sapacume.SAPACUMEConfiguration;
import org.identityconnectors.sapacume.SAPACUMEConnector;
import org.identityconnectors.test.common.TestHelpers;

public final class ICFTestHelper {

	private final static String _bundleLoc ="D:\\ADE_local\\idc\\bundles\\java\\sapac\\dist\\org.identityconnectors.sapacume-1.0.11190.jar";
    private boolean connectionFailed = false;
    private ConnectorFacade _facade = null;
    private static SAPACUMEConfiguration sapacumeConfiguration = null;
    
	private static final Log _log = Log.getLog(ICFTestHelper.class);
    
    public static ICFTestHelper getInstance() {
        return new ICFTestHelper();
    }

    private ICFTestHelper() {
    }
    
    public ConnectorFacade getFacade() {

        if (connectionFailed)
            throw new IllegalStateException("Connection to target failed");

        if (_facade == null) {
            initializeFacade();
        }
        return _facade;
    }
    
    private void initializeFacade() {
        _log.info("Loading the bundle " + _bundleLoc);
        File bundleJarFile = new File(_bundleLoc);
        if (!bundleJarFile.exists()) {
            throw new IllegalArgumentException("Bundle jar doesn't exist at "
                    + _bundleLoc);
        }

        try {
            URL connectorBundle = bundleJarFile.toURI().toURL();
            _log.info("Bundle location: " + _bundleLoc);

            ConnectorInfoManagerFactory infoManagerFactory = ConnectorInfoManagerFactory
                    .getInstance();
            ConnectorInfoManager infoManager = infoManagerFactory
                    .getLocalManager(connectorBundle);

            List<ConnectorInfo> connectorInfos = infoManager
                    .getConnectorInfos();
            if (connectorInfos.size() != 1) {
                _log.error(
                        "Connector cannot be loaded. Only one bundle should be present."
                                + " {0} bundles found", connectorInfos.size());
                throw new IllegalStateException("Improper bundle found");
            }

            // Get configuration props
            APIConfiguration apiConfig = connectorInfos.get(0)
                    .createDefaultAPIConfiguration();
            
            // Set configuration props
            ConfigurationProperties configProps = apiConfig
                    .getConfigurationProperties();
            
            setsapumeConfigProps(configProps);
            
            _facade = ConnectorFacadeFactory.getInstance().newInstance(
                    apiConfig);
            _facade.schema();
            _log.info("Connector validated");
//            _facade.test();

        } catch (Exception e) {
            _log.error("Unable to initialize the bundle at {0}", _bundleLoc);
            connectionFailed = true;
            throw new ConnectorException(e);
        }
    }
    
	public ConnectorFacade newFacade() {
		SAPACUMEConfiguration cfg = SAPUMEACTestUtils.newConfiguration();
	return newFacade(cfg);	
	}

	public ConnectorFacade newFacade(SAPACUMEConfiguration cfg) {
		ConnectorFacadeFactory factory = ConnectorFacadeFactory.getInstance();
		APIConfiguration impl = TestHelpers.createTestConfiguration(SAPACUMEConnector.class, cfg);
	return factory.newInstance(impl);
	}	
    
	private void setsapumeConfigProps(ConfigurationProperties sapumeConfigProps) {
		
		SAPACUMEConfiguration config = SAPUMEACTestUtils.newConfiguration();
		sapumeConfigProps.setPropertyValue("umeUrl", config.getUmeUrl());
		sapumeConfigProps.setPropertyValue("umeUserId", config.getUmeUserId());
		sapumeConfigProps.setPropertyValue("umePassword", config.getUmePassword());
		sapumeConfigProps.setPropertyValue("dummyPassword", config.getDummyPassword());
		sapumeConfigProps.setPropertyValue("changePwdFlag", config.getChangePwdFlag());
		sapumeConfigProps.setPropertyValue("pwdHandlingSupport", config.getPwdHandlingSupport());
		sapumeConfigProps.setPropertyValue("logSPMLRequest", config.getLogSPMLRequest());
		sapumeConfigProps.setPropertyValue("enableDate", config.getEnableDate());
		sapumeConfigProps.setPropertyValue("logonNameInitialSubstring",config.getLogonNameInitialSubstring());
		sapumeConfigProps.setPropertyValue("roleDatasource", config.getRoleDatasource());
		sapumeConfigProps.setPropertyValue("groupDatasource", config.getGroupDatasource());
		sapumeConfigProps.setPropertyValue("userAccessWS", config.getUserAccessWS());
		sapumeConfigProps.setPropertyValue("createUserReqType", config.getCreateUserReqType());
		sapumeConfigProps.setPropertyValue("modifyUserReqType", config.getModifyUserReqType());
		sapumeConfigProps.setPropertyValue("wsdlFilePath", config.getWsdlFilePath());
		sapumeConfigProps.setPropertyValue("userAccessAccessURL", config.getUserAccessAccessURL());
		sapumeConfigProps.setPropertyValue("auditLogsAccessURL", config.getAuditLogsAccessURL());
		sapumeConfigProps.setPropertyValue("auditLogsWS", config.getAuditLogsWS());
		sapumeConfigProps.setPropertyValue("requestTypeAttrName", config.getRequestTypeAttrName());
		sapumeConfigProps.setPropertyValue("provActionAttrName", config.getProvActionAttrName());
		sapumeConfigProps.setPropertyValue("provItemActionAttrName", config.getProvItemActionAttrName());
		sapumeConfigProps.setPropertyValue("requestStatusWS", config.getRequestStatusWS());
		sapumeConfigProps.setPropertyValue("requestStatusAccessURL", config.getRequestStatusAccessURL());
		sapumeConfigProps.setPropertyValue("unlockUserReqType", config.getUnlockUserReqType());
		sapumeConfigProps.setPropertyValue("grcLanguage", config.getGrcLanguage());
		sapumeConfigProps.setPropertyValue("grcPassword", config.getGrcPassword());
		sapumeConfigProps.setPropertyValue("grcUsername", config.getGrcUsername());

	}
	
	static ConnectorFacade connectorFacadeBundle = null;
	public static  ConnectorFacade getConnectorFacadeHelperImp() {
		if( null != connectorFacadeBundle)
			return connectorFacadeBundle;
		SAPACUMEConfiguration config = new SAPACUMEConfiguration();
		setSapConfig(config, getConfig());
		ConnectorFacadeFactory factory = ConnectorFacadeFactory.getInstance();
		APIConfiguration impl = TestHelpers.createTestConfiguration(SAPACUMEConnector.class, config);
		connectorFacadeBundle =factory.newInstance(impl); 
		return connectorFacadeBundle;
	}
    
    private static void setSapConfig(SAPACUMEConfiguration acConfig,
    		SAPACUMEConfiguration groovyConfig) {
    	final String METHOD_NAME = "setSapConfig";
    	Object noparams[] = {};
    	Method[] groovyConfigMethods = groovyConfig.getClass().getMethods();
    	Method[] acConfigMethods = acConfig.getClass().getMethods();

    	for (Method method: groovyConfigMethods) {
    		if(method.getName().startsWith("get")) {                 
    			String methodName=new String(method.getName().substring(3));
    			for(Method acMethod: acConfigMethods){
    				if(acMethod.getName().startsWith("set")) {                 
    					String acMethodName=new String(acMethod.getName().substring(3));
    					if(methodName.equals(acMethodName)){
    						try {
    							acMethod.invoke(acConfig, method.invoke(groovyConfig, noparams));
    						} catch (IllegalArgumentException e) {
    							_log.error(METHOD_NAME+e.getMessage());
    						} catch (IllegalAccessException e) {
    							_log.error(METHOD_NAME+e.getMessage());
    						} catch (InvocationTargetException e) {
    							_log.error(METHOD_NAME+e.getMessage());
    						}
    					}
    				}
    			}
    		}
    	}
    }
    private static SAPACUMEConfiguration getConfig() {
        
        if(sapacumeConfiguration==null){
        	sapacumeConfiguration=SAPUMEACTestUtils.newConfiguration();
        }
     return sapacumeConfiguration;
    }
}
