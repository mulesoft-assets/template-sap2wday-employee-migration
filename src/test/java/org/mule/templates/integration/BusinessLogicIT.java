/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleException;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;

import com.mulesoft.module.batch.BatchTestHelper;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Anypoint Tempalte that make calls to external systems.
 * 
 */
public class BusinessLogicIT extends AbstractTemplateTestCase {

	protected static final int TIMEOUT_SEC = 120;
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private BatchTestHelper helper;

	private static SubflowInterceptingChainLifecycleWrapper createSapEmployeeSubflow;
	private static SubflowInterceptingChainLifecycleWrapper queryWorkdayEmployeeSubflow;
	
	private Map<String, Object> createdEmployee = null;

	@BeforeClass
	public static void init(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Calendar cal = Calendar.getInstance();
		System.setProperty("migration.startDate", "\"" + sdf.format(cal.getTime()) + "\"");	
	}
	
	@Before
	public void setUp() throws Exception {
		helper = new BatchTestHelper(muleContext);

		createSapEmployeeSubflow = getSubFlow("createSapEmployeeSubflow");
		createSapEmployeeSubflow.initialise();
		queryWorkdayEmployeeSubflow = getSubFlow("queryWorkdayEmployeeSubflow");
		queryWorkdayEmployeeSubflow.initialise();

		final Properties props = new Properties();
    	try {
    		props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    	   logger.error("Error occured while reading mule.test.properties", e);
    	} 
    	createTestDataInSandBox();
	}

	@Test
	public void testMainFlow() throws Exception {
		Thread.sleep(20000);
		runFlow("triggerFlow");

		// Wait for the batch job executed by the poll flow to finish
		helper.awaitJobTermination(TIMEOUT_SEC * 1000, 500);
		//helper.assertJobWasSuccessful();

		System.err.println("querying workday with employee " + createdEmployee);
		Object response = queryWorkdayEmployeeSubflow.process(getTestEvent(createdEmployee, MessageExchangePattern.REQUEST_RESPONSE)).getMessage().getPayload();	
		System.err.println("queryWorkdayEmployeeSubflow " + response.getClass());
		System.err.println("queryWorkdayEmployeeSubflow " + response);
	}

	private void createTestDataInSandBox() throws MuleException, Exception {
		try {
			Map<String, Object> employee = new HashMap<String, Object>();
			long prefix = System.currentTimeMillis();
			employee.put("FirstName", "Amy");
			employee.put("LastName", "Adams" + prefix);
			Object response = createSapEmployeeSubflow.process(getTestEvent(employee, MessageExchangePattern.REQUEST_RESPONSE)).getMessage().getPayload();	
			System.err.println("createTestDataInSandBox " + response.getClass());
			System.err.println("createTestDataInSandBox " + response);
			employee.put("PersonalNumber", response);
			createdEmployee = employee;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
