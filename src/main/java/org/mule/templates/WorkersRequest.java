/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates;

import java.util.Date;
import java.util.GregorianCalendar;

import com.workday.hr.EmployeeGetType;
import com.workday.hr.EmployeeReferenceType;
import com.workday.hr.ExternalIntegrationIDReferenceDataType;
import com.workday.hr.IDType;

public class WorkersRequest {
	
	public static EmployeeGetType createEmployeeRequest(String value, String systemId) throws Exception{
		final GregorianCalendar gcalendar = new GregorianCalendar();
		gcalendar.setTime(new Date());

		final EmployeeGetType employeeGetType = new EmployeeGetType();
		final EmployeeReferenceType employeeReferenceType = new EmployeeReferenceType();
		final ExternalIntegrationIDReferenceDataType extIdReference = new ExternalIntegrationIDReferenceDataType();
		
		final IDType idType = new IDType();
		idType.setSystemID(systemId);
		idType.setValue(value);
		
		extIdReference.setID(idType);
		employeeReferenceType.setIntegrationIDReference(extIdReference);

		employeeGetType.setEmployeeReference(employeeReferenceType);
		return employeeGetType;
	}
}
