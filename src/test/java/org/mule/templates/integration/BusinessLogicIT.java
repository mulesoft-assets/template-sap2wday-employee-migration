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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;

import com.mulesoft.module.batch.BatchTestHelper;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Anypoint Template that make calls to external systems.
 * 
 */
public class BusinessLogicIT extends AbstractTemplateTestCase {

	private static final long TIMEOUT_MILLIS = 300000;
	private static final long DELAY_MILLIS = 500;
	private static final Logger LOGGER = LogManager.getLogger(BusinessLogicIT.class);
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private BatchTestHelper helper;

	private static SubflowInterceptingChainLifecycleWrapper createSapEmployeeSubflow;
	private static SubflowInterceptingChainLifecycleWrapper queryWorkdayEmployeeSubflow;
	
	private Map<String, Object> createdEmployee = null;
	
	@Before
	public void setUp() throws Exception {
		final Properties props = new Properties();
    	try {
    		props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    	   logger.error("Error occured while reading mule.test.properties", e);
    	} 
    	initializeSubFlows();
    	createTestDataInSandBox();
    	helper = new BatchTestHelper(muleContext);
	}

	@Test
	public void testMainFlow() throws Exception {		
		Thread.sleep(3000);
		runFlow("triggerFlow");
		Thread.sleep(120000);		
		
		// Find migrated employee by ID
		Object response = queryWorkdayEmployeeSubflow.process(getTestEvent(createdEmployee, MessageExchangePattern.REQUEST_RESPONSE)).getMessage().getPayload();	
		Assert.assertNotNull(response);
	}

	/*
	 * Creates employee in Workday suitable for a migration.
	 */
	private void createTestDataInSandBox() throws Exception {
			createdEmployee = new HashMap<String, Object>();
			createdEmployee.put("FirstName", "Amy");
			createdEmployee.put("LastName", "Evans" + System.currentTimeMillis());
			
			String response = (String) createSapEmployeeSubflow.process(getTestEvent(createdEmployee, MessageExchangePattern.REQUEST_RESPONSE)).getMessage().getPayload();	
			LOGGER.info("CreateTestDataInSandBox response: " + response);
			createdEmployee.put("PersonalNumber", response);
	}
	
	private void initializeSubFlows() throws InitialisationException {
		createSapEmployeeSubflow = getSubFlow("createSapEmployeeSubflow");
		createSapEmployeeSubflow.initialise();
		queryWorkdayEmployeeSubflow = getSubFlow("queryWorkdayEmployeeSubflow");
		queryWorkdayEmployeeSubflow.initialise();
	}
}
