/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mule.api.MuleMessage;
import org.mule.api.routing.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.workday.hr.WorkerType;
/**
* The filter that's removing records from the payload with the same email address.
*
* @author aurel.medvegy
*/
public class WorkersDeduplicationFilter implements Filter {
	
	Logger logger = LoggerFactory.getLogger(WorkersDeduplicationFilter.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean accept(MuleMessage message) {
		List<WorkerType> payload = (List<WorkerType>) message.getPayload();
		List<String> emails = new ArrayList<String>();
		Iterator<WorkerType> iterator = payload.iterator();
		logger.info("total records:" + payload.size());
		while (iterator.hasNext()) {
			WorkerType next = iterator.next();
			if (next.getWorkerData().getPersonalData().getContactData().getEmailAddressData().isEmpty()){
				iterator.remove();
				continue;
			}
			String email = next.getWorkerData().getPersonalData().getContactData().getEmailAddressData().get(0).getEmailAddress();
			if (emails.contains(email)) {
				iterator.remove();
			} else {
				emails.add(email);
			}
		}
		
		logger.info("unique emails:" + emails.size());
		return true;
	}
}