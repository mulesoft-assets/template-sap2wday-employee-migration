/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;

import com.workday.hr.EventTargetTransactionLogEntryDataType;
import com.workday.hr.TransactionLogEntryType;
import com.workday.hr.WorkerType;

public class TransactionLogProcessor implements MessageProcessor{

	@Override
	public MuleEvent process(MuleEvent event) throws MuleException {
		WorkerType worker = (WorkerType) event.getMessage().getPayload();
		EventTargetTransactionLogEntryDataType log = worker.getWorkerData().getTransactionLogEntryData();
		XMLGregorianCalendar lastModifiedDate = null;
		if (log != null){
			java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
			XMLGregorianCalendar now;
			try {
				now = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);									
				for (TransactionLogEntryType entry : log.getTransactionLogEntry()){								
					if (entry.getTransactionLogData().getTransactionEntryMoment() != null && entry.getTransactionLogData().getTransactionEntryMoment().compare(now) <= 0){
						if (entry.getTransactionLogData().getTransactionEffectiveMoment() != null && entry.getTransactionLogData().getTransactionEffectiveMoment().compare(now) <= 0) {
							if (lastModifiedDate != null && entry.getTransactionLogData().getTransactionEntryMoment().compare(lastModifiedDate) > 0){
								lastModifiedDate = entry.getTransactionLogData().getTransactionEntryMoment();
							}
							else
								lastModifiedDate = entry.getTransactionLogData().getTransactionEntryMoment();
						}
						else
							if (entry.getTransactionLogData().getTransactionEffectiveMoment() == null){
								if (lastModifiedDate == null || (lastModifiedDate != null && entry.getTransactionLogData().getTransactionEntryMoment().compare(lastModifiedDate) > 0))
									lastModifiedDate = entry.getTransactionLogData().getTransactionEntryMoment();
							}
					}								
				}
			} catch (DatatypeConfigurationException e1) {
				e1.printStackTrace();
			}
		}
				
		event.getMessage().setPayload(lastModifiedDate == null ? null : lastModifiedDate.normalize().toString());
		return event;
	}
	
}
