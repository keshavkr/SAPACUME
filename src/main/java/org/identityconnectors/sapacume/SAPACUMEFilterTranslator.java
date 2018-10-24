
package org.identityconnectors.sapacume;

import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;

/**
 * This is an implementation of AbstractFilterTranslator that gives a concrete representation
 * of which filters can be applied at the connector level (natively). If the 
 * SAP doesn't support a certain expression type, that factory
 * method should return null. This level of filtering is present only to allow any
 * native contructs that may be available to help reduce the result set for the framework,
 * which will (strictly) reapply all filters specified after the connector does the initial
 * filtering.<p><p>Note: The generic query type is most commonly a String, but does not have to be.
 * 
 */



public class SAPACUMEFilterTranslator extends AbstractFilterTranslator<Object> {
	private boolean isValid = true;

	public SAPACUMEFilterTranslator(boolean isValid ){
		this.isValid = isValid;
}
	
    /**
     * {@inheritDoc}
     */
    @Override
    protected String createEqualsExpression(EqualsFilter filter, boolean not) {
    	if(!isValid){
    		return null;
    	}
        if (!not && filter.getAttribute().is(Name.NAME))
            return AttributeUtil.getAsStringValue(filter.getAttribute());
        else if (!not && filter.getAttribute().is(Uid.NAME))
            return AttributeUtil.getAsStringValue(filter.getAttribute());
        else
        	return filter.getName() + "=" + 
            AttributeUtil.getAsStringValue(filter.getAttribute());
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String createAndExpression(Object leftExpression, Object rightExpression) {
    	if(!isValid){
    		return null;
    	}
        // sap does not support AND but we can use one of them
        if (leftExpression!=null && rightExpression!=null)          
            return leftExpression + " & " + rightExpression;
        else
            return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String createOrExpression(Object leftExpression, Object rightExpression) {
    	if(!isValid){
    		return null;
    	}
        // sap does not support AND but we can use one of them
        if (leftExpression!=null && rightExpression!=null)          
            return leftExpression + " | " + rightExpression;
        else
            return null;
    }
      
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected String createGreaterThanExpression(GreaterThanFilter filter, boolean not) {        
    	if(!isValid){
    		return null;
    	}
        return filter.getName()+ ">" +AttributeUtil.getAsStringValue(filter.getAttribute());
   
}
}
