/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.workday.hr.EmployeeGetType;
import com.workday.hr.EmployeeReferenceType;
import com.workday.hr.ExternalIntegrationIDReferenceDataType;
import com.workday.hr.IDType;

public class WorkersRequest {
	
	public static EmployeeGetType createEmployeeRequest(String value) throws Exception{
		final GregorianCalendar gcalendar = new GregorianCalendar();
		gcalendar.setTime(new Date());
		final XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcalendar);

		final EmployeeGetType employeeGetType = new EmployeeGetType();
		final EmployeeReferenceType employeeReferenceType = new EmployeeReferenceType();
		final ExternalIntegrationIDReferenceDataType extIdReference = new ExternalIntegrationIDReferenceDataType();
		
		final IDType idType = new IDType();
		//idType.setSystemID("Jobvite");
		idType.setSystemID("Salesforce - Chatter");
		idType.setValue(value);
		
		extIdReference.setID(idType);
		employeeReferenceType.setIntegrationIDReference(extIdReference);

		employeeGetType.setEmployeeReference(employeeReferenceType);
		//employeeGetType.setAsOfDate(xmlDate);
		//employeeGetType.setAsOfMoment(xmlDate);
		
//		EmployeeType employeeType = new EmployeeType();
//		employeeType.getEmployeeData().get(0).getEmployeeID();
		return employeeGetType;
	}

	private static XMLGregorianCalendar xmlDate(Date date) throws DatatypeConfigurationException {
		GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	}
	
}
