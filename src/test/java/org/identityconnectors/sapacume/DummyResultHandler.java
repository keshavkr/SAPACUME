/**
 * 
 */
package org.identityconnectors.sapacume.test;

import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ResultsHandler;

/**
 * @author Suresh Kadiyala
 *
 */
public class DummyResultHandler implements ResultsHandler {

    private int userCount = 0;
     
    public boolean handle(ConnectorObject userObj) {
        setUserCount(getUserCount() + 1);
        return true;
    }

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

}