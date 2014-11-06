/**
 * Mule Anypoint Template
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import static junit.framework.Assert.assertEquals;

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
import org.mule.templates.utils.Employee;

import com.mulesoft.module.batch.BatchTestHelper;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Anypoint Tempalte that make calls to external systems.
 * 
 */
public class BusinessLogicIT extends AbstractTemplateTestCase {

	private static final String TEMPLATE_PREFFIX = "wday2sfdc-worker-migration";
	protected static final int TIMEOUT_SEC = 60;
	private static SubflowInterceptingChainLifecycleWrapper retrieveUserFlow;
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private BatchTestHelper helper;
	private String EMAIL = "bwillis@gmailtest.com";
	private Employee testEmployee;
	private String WORKDAY_ID;

	@BeforeClass
	public static void init(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Calendar cal = Calendar.getInstance();
		System.setProperty("migration.startDate", "\"" + sdf.format(cal.getTime()) + "\"");	
	}
	
	@Before
	public void setUp() throws Exception {
		helper = new BatchTestHelper(muleContext);

		retrieveUserFlow = getSubFlow("retrieveUserSFDC");
		retrieveUserFlow.initialise();

		final Properties props = new Properties();
    	try {
    		props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    	   logger.error("Error occured while reading mule.test.properties", e);
    	} 
    	WORKDAY_ID = props.getProperty("wday.testuser.id");
    	
    	createTestDataInSandBox();
	}

	@Test
	public void testMainFlow() throws Exception {
		Thread.sleep(20000);
		runFlow("mainFlow");

		// Wait for the batch job executed by the poll flow to finish
		helper.awaitJobTermination(TIMEOUT_SEC * 1000, 500);
		helper.assertJobWasSuccessful();

		Map<String, Object> user = new HashMap<String, Object>();
		user.put("Email", EMAIL);
		Map<String, Object> payload = invokeRetrieveFlow(retrieveUserFlow,
				user);
		
		assertEquals("The user first name should have been sync", testEmployee.getGivenName(), payload.get("FirstName"));
		assertEquals("The user last name should have been sync", testEmployee.getFamilyName(), payload.get("LastName"));
	}

	private void createTestDataInSandBox() throws MuleException, Exception {
		SubflowInterceptingChainLifecycleWrapper flow = getSubFlow("updateWorkdayEmployee");
		flow.initialise();
		logger.info("updating a workday employee...");
		try {
			flow.process(getTestEvent(prepareEdit(), MessageExchangePattern.REQUEST_RESPONSE));						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Employee prepareEdit(){
		String name = TEMPLATE_PREFFIX + System.currentTimeMillis();
		logger.info("employee name: " + name);
		testEmployee = new Employee(name, TEMPLATE_PREFFIX + System.currentTimeMillis(), "bwillis@gmailtest.com", "650-232-2323", "999 Main St", "San Francisco", "CA", "94105", "US", "o7aHYfwG", 
				"2014-04-17-07:00", "2014-04-21-07:00", "QA Engineer", "San_Francisco_site", "Regular", "Full Time", "Salary", "USD", "140000", "Annual", "39905", "21440", WORKDAY_ID);
		return testEmployee;
	}		
	
}
