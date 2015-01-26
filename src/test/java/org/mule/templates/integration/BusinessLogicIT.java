/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
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
		/*
		
		Thread.sleep(30000);
		
		runFlow("triggerFlow");
		
		Thread.sleep(1000*60*6);

		createdEmployee.put("PersonalNumber", createdEmployee.get("PersonalNumber"));
		createdEmployee.put("EmployeeNumber", createdEmployee.get("EmployeeNumber"));
		// Find migrated employee by ID
		Object response = queryWorkdayEmployeeSubflow.process(getTestEvent(createdEmployee, MessageExchangePattern.REQUEST_RESPONSE)).getMessage().getPayload();	
		Assert.assertNotNull(response);
		*/
	}

	/*
	 * Creates employee in Workday suitable for a migration.
	 */
	private void createTestDataInSandBox() throws MuleException, Exception {
		try {
			Map<String, Object> employee = new HashMap<String, Object>();
			long prefix = System.currentTimeMillis();
			employee.put("FirstName", "Amy");
			employee.put("LastName", "Evans" + prefix);
			Object response = createSapEmployeeSubflow.process(getTestEvent(employee, MessageExchangePattern.REQUEST_RESPONSE)).getMessage().getPayload();	
			System.err.println("createTestDataInSandBox " + response);
			employee.put("PersonalNumber", response);
			createdEmployee = employee;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
