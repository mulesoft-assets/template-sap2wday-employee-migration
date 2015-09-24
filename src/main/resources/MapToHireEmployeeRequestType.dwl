%dw 1.0
%output application/java
--- 
{
	version: 'v20',
	businessProcessParameters : {
		autoComplete : true
	},
	hireEmployeeData : {
		applicantData: {
			externalIntegrationIDData: {
				ID: [{
					systemID	: p('wday.system.id'),
				    value		: payload.personalNumber
				    }]
			},
			personalData: {
				contactData: {
					addressData: [{
						addressLineData: [{
							type	: 'ADDRESS_LINE_1',
							value	: 'New York'

						}],
						countryReference: {
							ID: [{
								type	: 'ISO_3166-1_Alpha-3_Code',
								value	: 'USA'
							}]
						},
						countryRegionReference: {
							ID: [{
								type	: 'Country_Region_ID',
								value	: 'USA-CA'
							}]
						},
						effectiveDate	: payload.hireDate as :date default now as :date,
						municipality 	: 'San Francisco',
						postalCode 		: '90001',
						usageData: [{
							typeData:[{
								primary : true,
								typeReference: {
									ID: [{
										type	: 'Communication_Usage_Type_ID',
										value	: 'WORK'
									}]
								}
							}]
						}]
					}],
					emailAddressData 	: [{
						emailAddress	: 'john.doe@aol.com',
						usageData: [{
							public: true,
							typeData: [{
								primary: true,
								typeReference: {
									ID: [{
										type	: 'Communication_Usage_Type_ID',
										value	: 'HOME'
									}]
								}
							}]
						}]
					}],
					phoneData: [{
						internationalPhoneCode: '1',
						phoneDeviceTypeReference: {
							ID: [{
								type	: 'Phone_Device_Type_ID',
								value	: '1063.5'
							}]
						},
						phoneNumber		: payload.phone default null,
						usageData: [{
							public: true,
							typeData: [{
								primary: true,
								typeReference: {
									ID: [{
										type	: 'Communication_Usage_Type_ID',
										value	: 'HOME'
									}]
								}
							}]
						}]
					}]
				},
				nameData: {
					legalNameData: {
						nameDetailData: {
							countryReference: {
								ID: [{
									type	: 'ISO_3166-1_Alpha-3_Code',
									value	: 'USA'
								}]
							},
							firstName	: payload.firstName,
							lastName	: payload.lastName
						}
					}
				}
			}
		},	
			hireDate: now as :date,
			hireEmployeeEventData: {
				employeeExternalIDData: {
					externalID: [{
						externalID: payload.personalNumber,
						systemID: p('wday.system.id')
					}]
				},
				employeeTypeReference: {
			        ID: [{
			            type: 'Employee_Type_ID',
			            value: 'Regular'
			        }]
			    },
			    firstDayOfWork: now as :date,
			    hireReasonReference: {
			    	ID: [{
			    		type	: 'General_Event_Subcategory_ID',
			    		value	: 'Hire_Employee_New_Hire_Fill_Vacancy'
			    	}]
			    },
			    positionDetails: {
			    	positionTitle: payload.title default null,
			    	defaultHours: 40,
			    	scheduledHours: 40,
			    	jobProfileReference: {
			    		ID: [{
			    			type: 'Job_Profile_ID',
			    			value: '39905'
			    		}]
			    	},
			    	locationReference: {
			    		ID: [{
			    			type: 'Location_ID',
			    			value: 'San_Francisco_Site'
			    		}]
			    	},
			    	payRateTypeReference : {
			    		ID : [{
			    			type: 'Pay_Rate_Type_ID',
			    			value: 'Salary'
			    		}]
			    	},
			    	positionTimeTypeReference : {
			    		ID : [{
			    			type: 'Position_Time_Type_ID',
			    			value: 'Full_time'
			    		}]
			    	}
			    }
			},
			organizationReference: {
				ID: [{
					type: 'Organization_Reference_ID',
					value: '50006855'
				}]
			},
			proposeCompensationForHireSubProcess: {
				businessSubProcessParameters : {
					autoComplete : true
				},
				proposeCompensationForHireData : {
					compensationGuidelinesData : {
						compensationGradeReference : {
							ID: [{
								type: 'Compensation_Grade_ID',
								value: 'Non_Management'
							}]
						},
						compensationPackageReference : {
							ID : [{
								type : 'Compensation_Package_ID',
								value: 'Non_Management_Compensation_Package'
							}]
						}
					},
				payPlanData: {
					payPlanSubData: [{
						amount: "100.0",
						currencyReference : {
							ID : [{
								type: 'Currency_ID',
								value: 'USD'
							}]
						},
						frequencyReference : {
							ID: [{
								type : 'Frequency_ID',
								value : 'Annual'
							}]
						},
						payPlanReference : {
							ID : [{
								type : 'Compensation_Plan_ID',
								value: 'SALARY_Hourly_Plan'
							}]
						}
					}],
					'replace' : false
				},
				primaryCompensationBasis : payload.basePay default null
				}
			}
		}
	
	
} as :object {class: "com.workday.staffing.HireEmployeeRequestType"}