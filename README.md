
# Anypoint Template: Sap to Workday Employee Migration

# License Agreement
This template is subject to the conditions of the 
<a href="https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf">MuleSoft License Agreement</a>.
Review the terms of the license before downloading and using this template. You can use this template for free 
with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio.

# Use Case
As a SAP admin I want to migrate employees to Workday.

This Anypoint Template should serve as a foundation for the process of migrating Employees from SAP instance to Workday, being able to specify available filtering criteria and desired behavior when an employee already exists in the destination system.

As implemented, this Anypoint Template leverages the [Batch Module](http://www.mulesoft.org/documentation/display/current/Batch+Processing).
The batch job is divided into *Process* and *On Complete* stages.

Before the *Process* stage the Anypoint Template will go to the SAP and query all the employees that match the filtering criteria. The criteria is based on available filtering fields for standard BAPI BAPI_EMPLOYEE_GETDATA. 

During the *Process* stage the template checks if there is an employee with such an id in the Workday and if it is present the template will update it using Change_Preferred_Name function, if it isn't - the template creates it using HIRE_EMPLOYEE function call.

Finally during the *On Complete* stage the Anypoint Template will both log output statistics data into the console and send a notification email with the results of the batch execution.

# Considerations

To make this Anypoint Template run, there are certain preconditions that must be considered. All of them deal with the preparations in both source and destination systems, that must be made in order for all to run smoothly. **Failing to do so could lead to unexpected behavior of the template.**

## Disclaimer

This Anypoint template uses a few private Maven dependencies from Mulesoft in order to work. If you intend to run this template with Maven support, you need to add three extra dependencies for SAP to the pom.xml file.


## SAP Considerations

Here's what you need to know to get this template to work with SAP.

### As a Data Source

The SAP backend system is used as a source of data. The SAP connector is used to send and receive the data from the SAP backend. 
The connector can either use RFC calls of BAPI functions and/or IDoc messages for data exchange, and needs to be properly customized per the "Properties to Configure" section.






## Workday Considerations


### As a Data Destination

There are no considerations with using Workday as a data destination.






# Run it!
Simple steps to get Sap to Workday Employee Migration running.


## Running On Premises
In this section we help you run your template on your computer.


### Where to Download Anypoint Studio and the Mule Runtime
If you are a newcomer to Mule, here is where to get the tools.

+ [Download Anypoint Studio](https://www.mulesoft.com/platform/studio)
+ [Download Mule runtime](https://www.mulesoft.com/lp/dl/mule-esb-enterprise)


### Importing a Template into Studio
In Studio, click the Exchange X icon in the upper left of the taskbar, log in with your
Anypoint Platform credentials, search for the template, and click **Open**.


### Running on Studio
After you import your template into Anypoint Studio, follow these steps to run it:

+ Locate the properties file `mule.dev.properties`, in src/main/resources.
+ Complete all the properties required as per the examples in the "Properties to Configure" section.
+ Right click the template project folder.
+ Hover your mouse over `Run as`
+ Click `Mule Application (configure)`
+ Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`
+ Click `Run`


### Running on Mule Standalone
Complete all properties in one of the property files, for example in mule.prod.properties and run your app with the corresponding environment variable. To follow the example, this is `mule.env=prod`. 
After this, to trigger the use case you just need to hit the local HTTP Listener with the port you configured in the properties file. If this is, for instance, `9090` then you should hit: `http://localhost:9090/migrateemployees` and this will output a summary report and send it in the email.

## Running on CloudHub
While creating your application on CloudHub (or you can do it later as a next step), go to Runtime Manager > Manage Application > Properties to set the environment variables listed in "Properties to Configure" as well as the **mule.env**.
Once your app is all set and started, supposing you choose `sapemployeemigration` as domain name to trigger the use case you just need to hit `http://sapemployeemigration.cloudhub.io/migrateemployees` and report will be sent to the email configured.

### Deploying your Anypoint Template on CloudHub
Studio provides an easy way to deploy your template directly to CloudHub, for the specific steps to do so check this


## Properties to Configure
To use this template, configure properties (credentials, configurations, etc.) in the properties file or in CloudHub from Runtime Manager > Manage Application > Properties. The sections that follow list example values.
### Application Configuration
+ http.port `9090`
+ page.size `100`

**Workday Connector configuration**
+ wday.username `user`
+ wday.tenant `tenant`
+ wday.password `secret`
+ wday.host `https://impl-cc.workday.com/...`
+ wday.system.id `system id`

+ wday.country `USA`
+ wday.state `USA-CA`
+ wday.organization `SUPERVISORY_ORGANIZATION-6-235`
+ wday.jobprofileId `39905`
+ wday.postalCode `90001`
+ wday.city `San Francisco`
+ wday.location `San_Francisco_Site`
+ wday.address `New York`
+ wday.currency `USD`
+ wday.testuser.id `test user id`
+ wday.system.id `System id`
+ wday.email `john.doe@aol.com`

**SAP Connector configuration**
+ sap.jco.user `user`
+ sap.jco.passwd `secret`
+ sap.jco.sysnr `14`
+ sap.jco.client `800`
+ sap.jco.lang `EN`
+ sap.jco.ashost `example.domain.com`

**SMTP Services configuration**
+ smtp.host `smtp.gmail.com`
+ smtp.port `587`
+ smtp.user `sender%40gmail.com`
+ smtp.password `secret`

**Mail details**
+ mail.from `mail.from@example.com`
+ mail.to `info@examplecom`
+ mail.subject `Employee Migration Report`

# API Calls
There are no special considerations regarding API calls.


# Customize It!
This brief guide intends to give a high level idea of how this template is built and how you can change it according to your needs.
As Mule applications are based on XML files, this page describes the XML files used with this template.

More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

* config.xml
* businessLogic.xml
* endpoints.xml
* errorHandling.xml


## config.xml
Configuration for connectors and configuration properties are set in this file. Even change the configuration here, all parameters that can be modified are in properties file, which is the recommended place to make your changes. However if you want to do core changes to the logic, you need to modify this file.

In the Studio visual editor, the properties are on the *Global Element* tab.


## businessLogic.xml
Functional aspect of the Anypoint Template is implemented on this XML, directed by a batch job that will be responsible for creations/updates. The several message processors constitute four high level actions that fully implement the logic of this Anypoint Template:

1. Job execution is invoked from triggerFlow (endpoints.xml).
2. During the *Process* stage, each Employee will be filtered depending on existing of matching Employee in the Workday instance. The matching is performed by querying a Workday instance for an entry with the given External Reference ID.
3. The next step will insert a new record into the Workday instance if there was none found in the previous step or update it if there was found matching employee.

Finally during the *On Complete* stage the Anypoint Template will log output statistics data into the console and send report to an email you choose.



## endpoints.xml
For the purpose of this particular Anypoint Template the *triggerFlow* executes the Batch Job in *businessLogic.xml* which handles all the logic of it.

###  Inbound Flow
**HTTP Inbound Endpoint** - Start Report Generation
+ `${http.port}` is set as a property to be defined either on a property file or in CloudHub environment variables.
+ The path configured by default is `migrateemployees` and you are free to change for the one you prefer.
+ The host name for all endpoints in your CloudHub configuration should be defined as `localhost`. CloudHub will then route requests from your application domain URL to the endpoint.
+ The endpoint is configured as a *request-response* since as a result of calling it the response will be the total of Employees migrated and filtered by the criteria specified.



## errorHandling.xml
This is the right place to handle how your integration reacts depending on the different exceptions. 
This file provides error handling that is referenced by the main flow in the business logic.




