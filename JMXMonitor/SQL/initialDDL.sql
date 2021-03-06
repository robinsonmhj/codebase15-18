create table sandbox.resourceStatistics(
		serverName varchar(100) not null,
		getTime timestamp,
		usedMemory int,
		getsRate numeric(14,1),
		putsRate numeric(14,1),
		totalRegionEntryCount bigint,
		memberUpTime bigint,
		totalDiskUsage bigint,
		cpuUsage numeric(5,1),
		diskReadsRate numeric(14,1),
		diskWritesRate numeric(14,1),
		diskStoreDiskReadsRate numeric(14,1),
		diskStoreDiskWritesRate numeric(14,1),
		diskStoreTotalBytesOnDisk bigint,
		ASLDiskReadsRate numeric(14,1),
		ASLDiskWritesRate numeric(14,1),
		ASLTotalBytesOnDisk bigint,
		primary key(getTime,serverName)
)
distributed by (getTime)
PARTITION by list(serverName)
(
		PARTITION devrhgemfirev1 VALUES('devrhgemfirev1.tmghealth.com'),
		PARTITION devrhgemfirev2 VALUES('devrhgemfirev2.tmghealth.com'),
		PARTITION devrhgemfirev3 VALUES('devrhgemfirev3.tmghealth.com'),
		PARTITION devrhgemfirev4 VALUES('devrhgemfirev4.tmghealth.com'),
		PARTITION devrhgemfirev5 VALUES('devrhgemfirev5.tmghealth.com'),
		PARTITION devrhgemfirev6 VALUES('devrhgemfirev6.tmghealth.com'),		
		DEFAULT PARTITION others 
);
GRANT ALL ON TABLE sandbox.resourceStatistics TO dev_edw_etl;
GRANT SELECT ON TABLE sandbox.resourceStatistics TO dev_ods_read;


drop table if exists sandbox.tableStatistics;
create table sandbox.tableStatistics(
		serverName varchar(100) not null,
		tableName varchar(100),
		updates int,
		deletes int,
		inserts int,
		rowCount bigint,
		getTime timestamp,
		primary key(getTime,tableName,serverName)
)
distributed by (getTime)
PARTITION by list(tableName)
(
		PARTITION adjustment VALUES('ADJUSTMENT'),
		PARTITION agreement VALUES('AGREEMENT'),
		PARTITION bank VALUES('BANK'),
		PARTITION bank_account VALUES('BANK_ACCOUNT'),
		PARTITION benefit_group_name VALUES('BENEFIT_GROUP_NAME'),
		PARTITION benefit_groups VALUES('BENEFIT_GROUPS'),
		PARTITION benefit_package VALUES('BENEFIT_PACKAGE'),
		PARTITION benefit_package_attribute VALUES('BENEFIT_PACKAGE_ATTRIBUTE'),
		PARTITION benefit_package_relation VALUES('BENEFIT_PACKAGE_RELATION'),
		PARTITION billing_entity VALUES('BILLING_ENTITY'),
		PARTITION billing_entity_contact VALUES('BILLING_ENTITY_CONTACT'),
		PARTITION billing_entity_detail VALUES('BILLING_ENTITY_DETAIL'),
		PARTITION billing_entity_schedule VALUES('BILLING_ENTITY_SCHEDULE'),
		PARTITION billing_reconciliation VALUES('BILLING_RECONCILIATION'),
		PARTITION billing_schedule VALUES('BILLING_SCHEDULE'),
		PARTITION billing_source VALUES('BILLING_SOURCE'),
		PARTITION charge_item VALUES('CHARGE_ITEM'),
		PARTITION "check" VALUES('CHECK'),
		PARTITION check_status VALUES('CHECK_STATUS'),
		PARTITION claim VALUES('CLAIM'),
		PARTITION claim_additional_diagnosis VALUES('CLAIM_ADDITIONAL_DIAGNOSIS'),
		PARTITION claim_attribute VALUES('CLAIM_ATTRIBUTE'),
		PARTITION claim_cob VALUES('CLAIM_COB'),
		PARTITION claim_coshare_tracking VALUES('CLAIM_COSHARE_TRACKING'),
		PARTITION claim_detail VALUES('CLAIM_DETAIL'),
		PARTITION claim_hospital VALUES('CLAIM_HOSPITAL'),
		PARTITION claim_line_attribute VALUES('CLAIM_LINE_ATTRIBUTE'),
		PARTITION claim_payment VALUES('CLAIM_PAYMENT'),
		PARTITION claim_payment_detail VALUES('CLAIM_PAYMENT_DETAIL'),
		PARTITION claim_payment_reduction VALUES('CLAIM_PAYMENT_REDUCTION'),
		PARTITION claim_reduction_detail VALUES('CLAIM_REDUCTION_DETAIL'),
		PARTITION claim_reduction_history VALUES('CLAIM_REDUCTION_HISTORY'),
		PARTITION claim_status VALUES('CLAIM_STATUS'),
		PARTITION client_reference_data VALUES('CLIENT_REFERENCE_DATA'),
		PARTITION clients VALUES('CLIENTS'),
		PARTITION cob_claim_diagnosis VALUES('COB_CLAIM_DIAGNOSIS'),
		PARTITION cob_organization_person VALUES('COB_ORGANIZATION_PERSON'),
		PARTITION code_value VALUES('CODE_VALUE'),
		PARTITION communication VALUES('COMMUNICATION'),
		PARTITION complaint VALUES('COMPLAINT'),
		PARTITION complaint_status VALUES('COMPLAINT_STATUS'),
		PARTITION contact VALUES('CONTACT'),
		PARTITION contact_type_contact VALUES('CONTACT_TYPE_CONTACT'),
		PARTITION correspondence VALUES('CORRESPONDENCE'),
		PARTITION cst_companion_extract_271 VALUES('CST_COMPANION_EXTRACT_271'),
		PARTITION diagnosis_code VALUES('DIAGNOSIS_CODE'),
		PARTITION electronic_address VALUES('ELECTRONIC_ADDRESS'),
		PARTITION entity_bank_account VALUES('ENTITY_BANK_ACCOUNT'),
		PARTITION etl_metadata VALUES('ETL_METADATA'),
		PARTITION exhibit VALUES('EXHIBIT'),
		PARTITION exhibit_group VALUES('EXHIBIT_GROUP'),
		PARTITION exhibit_group_exhibit VALUES('EXHIBIT_GROUP_EXHIBIT'),
		PARTITION explanation_code VALUES('EXPLANATION_CODE'),
		PARTITION fdi_correspondence VALUES('FDI_CORRESPONDENCE'),
		PARTITION fdi_tx_idcard VALUES('FDI_TX_IDCARD'),
		PARTITION fdi_tx_letter VALUES('FDI_TX_LETTER'),
		PARTITION file_transfer_run VALUES('FILE_TRANSFER_RUN'),
		PARTITION file_transfer_stat VALUES('FILE_TRANSFER_STAT'),
		PARTITION general_ledger VALUES('GENERAL_LEDGER'),
		PARTITION general_ledger_map VALUES('GENERAL_LEDGER_MAP'),
		PARTITION group_relation VALUES('GROUP_RELATION'),
		PARTITION groups VALUES('GROUPS'),
		PARTITION inquiry VALUES('INQUIRY'),
		PARTITION invoice VALUES('INVOICE'),
		PARTITION invoice_detail VALUES('INVOICE_DETAIL'),
		PARTITION invoice_status VALUES('INVOICE_STATUS'),
		PARTITION lep_appeal VALUES('LEP_APPEAL'),
		PARTITION lep_appeal_decision VALUES('LEP_APPEAL_DECISION'),
		PARTITION letter VALUES('LETTER'),
		PARTITION letter_detail VALUES('LETTER_DETAIL'),
		PARTITION letter_job VALUES('LETTER_JOB'),
		PARTITION letter_register VALUES('LETTER_REGISTER'),
		PARTITION letter_work_item VALUES('LETTER_WORK_ITEM'),
		PARTITION line_additional_diagnosis VALUES('LINE_ADDITIONAL_DIAGNOSIS'),
		PARTITION line_disallow_explanation VALUES('LINE_DISALLOW_EXPLANATION'),
		PARTITION line_procedure_modifier VALUES('LINE_PROCEDURE_MODIFIER'),
		PARTITION marx_calendar VALUES('MARX_CALENDAR'),
		PARTITION moop_accumulator VALUES('MOOP_ACCUMULATOR'),
		PARTITION moop_balance VALUES('MOOP_BALANCE'),
		PARTITION moop_balance_exceptions VALUES('MOOP_BALANCE_EXCEPTIONS'),
		PARTITION moop_file_log VALUES('MOOP_FILE_LOG'),
		PARTITION note VALUES('NOTE'),
		PARTITION organization_code VALUES('ORGANIZATION_CODE'),
		PARTITION organization_contact_type VALUES('ORGANIZATION_CONTACT_TYPE'),
		PARTITION organization_event VALUES('ORGANIZATION_EVENT'),
		PARTITION organizations VALUES('ORGANIZATIONS'),
		PARTITION payment VALUES('PAYMENT'),
		PARTITION person_accumulator VALUES('PERSON_ACCUMULATOR'),
		PARTITION person_cob VALUES('PERSON_COB'),
		PARTITION person_code VALUES('PERSON_CODE'),
		PARTITION person_contact VALUES('PERSON_CONTACT'),
		PARTITION person_event VALUES('PERSON_EVENT'),
		PARTITION person_event_attribute VALUES('PERSON_EVENT_ATTRIBUTE'),
		PARTITION person_event_status VALUES('PERSON_EVENT_STATUS'),
		PARTITION person_event_status_reason VALUES('PERSON_EVENT_STATUS_REASON'),
		PARTITION person_lep_event VALUES('PERSON_LEP_EVENT'),
		PARTITION person_lep_profile VALUES('PERSON_LEP_PROFILE'),
		PARTITION person_lep_profile_record VALUES('PERSON_LEP_PROFILE_RECORD'),
		PARTITION person_organization_relation VALUES('PERSON_ORGANIZATION_RELATION'),
		PARTITION person_payment_options VALUES('PERSON_PAYMENT_OPTIONS'),
		PARTITION person_relation VALUES('PERSON_RELATION'),
		PARTITION person_role VALUES('PERSON_ROLE'),
		PARTITION person_subsidy_profile VALUES('PERSON_SUBSIDY_PROFILE'),
		PARTITION person_work_item VALUES('PERSON_WORK_ITEM'),
		PARTITION persons VALUES('PERSONS'),
		PARTITION phone VALUES('PHONE'),
		PARTITION plan_memberships VALUES('PLAN_MEMBERSHIPS'),
		PARTITION pos_code VALUES('POS_CODE'),
		PARTITION postal_address VALUES('POSTAL_ADDRESS'),
		PARTITION premium VALUES('PREMIUM'),
		PARTITION premium_category VALUES('PREMIUM_CATEGORY'),
		PARTITION premium_part VALUES('PREMIUM_PART'),
		PARTITION premium_table VALUES('PREMIUM_TABLE'),
		PARTITION procedure_code VALUES('PROCEDURE_CODE'),
		PARTITION procedure_price VALUES('PROCEDURE_PRICE'),
		PARTITION receipt VALUES('RECEIPT'),
		PARTITION reference_data VALUES('REFERENCE_DATA'),
		PARTITION reference_data_1 VALUES('reference_data_1'),
		PARTITION returned_mail VALUES('RETURNED_MAIL'),
		PARTITION revenue_code VALUES('REVENUE_CODE'),
		PARTITION service_code VALUES('SERVICE_CODE'),
		PARTITION test_moop_balance VALUES('TEST_MOOP_BALANCE'),
		PARTITION topic VALUES('TOPIC'),
		PARTITION topic_communication VALUES('TOPIC_COMMUNICATION'),
		PARTITION um_activity VALUES('UM_ACTIVITY'),
		PARTITION um_diagnosis_line VALUES('UM_DIAGNOSIS_LINE'),
		PARTITION um_diagnosis_set VALUES('UM_DIAGNOSIS_SET'),
		PARTITION um_inpatient VALUES('UM_INPATIENT'),
		PARTITION um_inpatient_review VALUES('UM_INPATIENT_REVIEW'),
		PARTITION um_inpatient_status VALUES('UM_INPATIENT_STATUS'),
		PARTITION um_inpatient_stay_length VALUES('UM_INPATIENT_STAY_LENGTH'),
		PARTITION um_review VALUES('UM_REVIEW'),
		PARTITION um_service VALUES('UM_SERVICE'),
		PARTITION um_service_override VALUES('UM_SERVICE_OVERRIDE'),
		PARTITION unapplied_cash VALUES('UNAPPLIED_CASH'),
		PARTITION work_generated_keys VALUES('WORK_GENERATED_KEYS'),		
		DEFAULT PARTITION others 
);
GRANT ALL ON TABLE sandbox.tableStatistics TO dev_edw_etl;
GRANT SELECT ON TABLE sandbox.tableStatistics TO dev_ods_read;



drop table if exists dl.tableStatistics;
create table dl.tableStatistics(
		serverName varchar(100) not null,
		tableName varchar(100),
		updates int,
		deletes int,
		inserts int,
		rowCount bigint,
		getTime timestamp,
		primary key(getTime,tableName,serverName)
)
distributed by (getTime)
PARTITION by list(tableName)
(
		PARTITION adjustment VALUES('ADJUSTMENT'),
		PARTITION agreement VALUES('AGREEMENT'),
		PARTITION bank VALUES('BANK'),
		PARTITION bank_account VALUES('BANK_ACCOUNT'),
		PARTITION benefit_group_name VALUES('BENEFIT_GROUP_NAME'),
		PARTITION benefit_groups VALUES('BENEFIT_GROUPS'),
		PARTITION benefit_package VALUES('BENEFIT_PACKAGE'),
		PARTITION benefit_package_attribute VALUES('BENEFIT_PACKAGE_ATTRIBUTE'),
		PARTITION benefit_package_relation VALUES('BENEFIT_PACKAGE_RELATION'),
		PARTITION billing_entity VALUES('BILLING_ENTITY'),
		PARTITION billing_entity_contact VALUES('BILLING_ENTITY_CONTACT'),
		PARTITION billing_entity_detail VALUES('BILLING_ENTITY_DETAIL'),
		PARTITION billing_entity_schedule VALUES('BILLING_ENTITY_SCHEDULE'),
		PARTITION billing_reconciliation VALUES('BILLING_RECONCILIATION'),
		PARTITION billing_schedule VALUES('BILLING_SCHEDULE'),
		PARTITION billing_source VALUES('BILLING_SOURCE'),
		PARTITION charge_item VALUES('CHARGE_ITEM'),
		PARTITION "check" VALUES('CHECK'),
		PARTITION check_status VALUES('CHECK_STATUS'),
		PARTITION claim VALUES('CLAIM'),
		PARTITION claim_additional_diagnosis VALUES('CLAIM_ADDITIONAL_DIAGNOSIS'),
		PARTITION claim_attribute VALUES('CLAIM_ATTRIBUTE'),
		PARTITION claim_cob VALUES('CLAIM_COB'),
		PARTITION claim_coshare_tracking VALUES('CLAIM_COSHARE_TRACKING'),
		PARTITION claim_detail VALUES('CLAIM_DETAIL'),
		PARTITION claim_hospital VALUES('CLAIM_HOSPITAL'),
		PARTITION claim_line_attribute VALUES('CLAIM_LINE_ATTRIBUTE'),
		PARTITION claim_payment VALUES('CLAIM_PAYMENT'),
		PARTITION claim_payment_detail VALUES('CLAIM_PAYMENT_DETAIL'),
		PARTITION claim_payment_reduction VALUES('CLAIM_PAYMENT_REDUCTION'),
		PARTITION claim_reduction_detail VALUES('CLAIM_REDUCTION_DETAIL'),
		PARTITION claim_reduction_history VALUES('CLAIM_REDUCTION_HISTORY'),
		PARTITION claim_status VALUES('CLAIM_STATUS'),
		PARTITION client_reference_data VALUES('CLIENT_REFERENCE_DATA'),
		PARTITION clients VALUES('CLIENTS'),
		PARTITION cob_claim_diagnosis VALUES('COB_CLAIM_DIAGNOSIS'),
		PARTITION cob_organization_person VALUES('COB_ORGANIZATION_PERSON'),
		PARTITION code_value VALUES('CODE_VALUE'),
		PARTITION communication VALUES('COMMUNICATION'),
		PARTITION complaint VALUES('COMPLAINT'),
		PARTITION complaint_status VALUES('COMPLAINT_STATUS'),
		PARTITION contact VALUES('CONTACT'),
		PARTITION contact_type_contact VALUES('CONTACT_TYPE_CONTACT'),
		PARTITION correspondence VALUES('CORRESPONDENCE'),
		PARTITION cst_companion_extract_271 VALUES('CST_COMPANION_EXTRACT_271'),
		PARTITION diagnosis_code VALUES('DIAGNOSIS_CODE'),
		PARTITION electronic_address VALUES('ELECTRONIC_ADDRESS'),
		PARTITION entity_bank_account VALUES('ENTITY_BANK_ACCOUNT'),
		PARTITION etl_metadata VALUES('ETL_METADATA'),
		PARTITION exhibit VALUES('EXHIBIT'),
		PARTITION exhibit_group VALUES('EXHIBIT_GROUP'),
		PARTITION exhibit_group_exhibit VALUES('EXHIBIT_GROUP_EXHIBIT'),
		PARTITION explanation_code VALUES('EXPLANATION_CODE'),
		PARTITION fdi_correspondence VALUES('FDI_CORRESPONDENCE'),
		PARTITION fdi_tx_idcard VALUES('FDI_TX_IDCARD'),
		PARTITION fdi_tx_letter VALUES('FDI_TX_LETTER'),
		PARTITION file_transfer_run VALUES('FILE_TRANSFER_RUN'),
		PARTITION file_transfer_stat VALUES('FILE_TRANSFER_STAT'),
		PARTITION general_ledger VALUES('GENERAL_LEDGER'),
		PARTITION general_ledger_map VALUES('GENERAL_LEDGER_MAP'),
		PARTITION group_relation VALUES('GROUP_RELATION'),
		PARTITION groups VALUES('GROUPS'),
		PARTITION inquiry VALUES('INQUIRY'),
		PARTITION invoice VALUES('INVOICE'),
		PARTITION invoice_detail VALUES('INVOICE_DETAIL'),
		PARTITION invoice_status VALUES('INVOICE_STATUS'),
		PARTITION lep_appeal VALUES('LEP_APPEAL'),
		PARTITION lep_appeal_decision VALUES('LEP_APPEAL_DECISION'),
		PARTITION letter VALUES('LETTER'),
		PARTITION letter_detail VALUES('LETTER_DETAIL'),
		PARTITION letter_job VALUES('LETTER_JOB'),
		PARTITION letter_register VALUES('LETTER_REGISTER'),
		PARTITION letter_work_item VALUES('LETTER_WORK_ITEM'),
		PARTITION line_additional_diagnosis VALUES('LINE_ADDITIONAL_DIAGNOSIS'),
		PARTITION line_disallow_explanation VALUES('LINE_DISALLOW_EXPLANATION'),
		PARTITION line_procedure_modifier VALUES('LINE_PROCEDURE_MODIFIER'),
		PARTITION marx_calendar VALUES('MARX_CALENDAR'),
		PARTITION moop_accumulator VALUES('MOOP_ACCUMULATOR'),
		PARTITION moop_balance VALUES('MOOP_BALANCE'),
		PARTITION moop_balance_exceptions VALUES('MOOP_BALANCE_EXCEPTIONS'),
		PARTITION moop_file_log VALUES('MOOP_FILE_LOG'),
		PARTITION note VALUES('NOTE'),
		PARTITION organization_code VALUES('ORGANIZATION_CODE'),
		PARTITION organization_contact_type VALUES('ORGANIZATION_CONTACT_TYPE'),
		PARTITION organization_event VALUES('ORGANIZATION_EVENT'),
		PARTITION organizations VALUES('ORGANIZATIONS'),
		PARTITION payment VALUES('PAYMENT'),
		PARTITION person_accumulator VALUES('PERSON_ACCUMULATOR'),
		PARTITION person_cob VALUES('PERSON_COB'),
		PARTITION person_code VALUES('PERSON_CODE'),
		PARTITION person_contact VALUES('PERSON_CONTACT'),
		PARTITION person_event VALUES('PERSON_EVENT'),
		PARTITION person_event_attribute VALUES('PERSON_EVENT_ATTRIBUTE'),
		PARTITION person_event_status VALUES('PERSON_EVENT_STATUS'),
		PARTITION person_event_status_reason VALUES('PERSON_EVENT_STATUS_REASON'),
		PARTITION person_lep_event VALUES('PERSON_LEP_EVENT'),
		PARTITION person_lep_profile VALUES('PERSON_LEP_PROFILE'),
		PARTITION person_lep_profile_record VALUES('PERSON_LEP_PROFILE_RECORD'),
		PARTITION person_organization_relation VALUES('PERSON_ORGANIZATION_RELATION'),
		PARTITION person_payment_options VALUES('PERSON_PAYMENT_OPTIONS'),
		PARTITION person_relation VALUES('PERSON_RELATION'),
		PARTITION person_role VALUES('PERSON_ROLE'),
		PARTITION person_subsidy_profile VALUES('PERSON_SUBSIDY_PROFILE'),
		PARTITION person_work_item VALUES('PERSON_WORK_ITEM'),
		PARTITION persons VALUES('PERSONS'),
		PARTITION phone VALUES('PHONE'),
		PARTITION plan_memberships VALUES('PLAN_MEMBERSHIPS'),
		PARTITION pos_code VALUES('POS_CODE'),
		PARTITION postal_address VALUES('POSTAL_ADDRESS'),
		PARTITION premium VALUES('PREMIUM'),
		PARTITION premium_category VALUES('PREMIUM_CATEGORY'),
		PARTITION premium_part VALUES('PREMIUM_PART'),
		PARTITION premium_table VALUES('PREMIUM_TABLE'),
		PARTITION procedure_code VALUES('PROCEDURE_CODE'),
		PARTITION procedure_price VALUES('PROCEDURE_PRICE'),
		PARTITION receipt VALUES('RECEIPT'),
		PARTITION reference_data VALUES('REFERENCE_DATA'),
		PARTITION reference_data_1 VALUES('reference_data_1'),
		PARTITION returned_mail VALUES('RETURNED_MAIL'),
		PARTITION revenue_code VALUES('REVENUE_CODE'),
		PARTITION service_code VALUES('SERVICE_CODE'),
		PARTITION test_moop_balance VALUES('TEST_MOOP_BALANCE'),
		PARTITION topic VALUES('TOPIC'),
		PARTITION topic_communication VALUES('TOPIC_COMMUNICATION'),
		PARTITION um_activity VALUES('UM_ACTIVITY'),
		PARTITION um_diagnosis_line VALUES('UM_DIAGNOSIS_LINE'),
		PARTITION um_diagnosis_set VALUES('UM_DIAGNOSIS_SET'),
		PARTITION um_inpatient VALUES('UM_INPATIENT'),
		PARTITION um_inpatient_review VALUES('UM_INPATIENT_REVIEW'),
		PARTITION um_inpatient_status VALUES('UM_INPATIENT_STATUS'),
		PARTITION um_inpatient_stay_length VALUES('UM_INPATIENT_STAY_LENGTH'),
		PARTITION um_review VALUES('UM_REVIEW'),
		PARTITION um_service VALUES('UM_SERVICE'),
		PARTITION um_service_override VALUES('UM_SERVICE_OVERRIDE'),
		PARTITION unapplied_cash VALUES('UNAPPLIED_CASH'),
		PARTITION work_generated_keys VALUES('WORK_GENERATED_KEYS'),		
		DEFAULT PARTITION others 
);
ALTER TABLE dl.tableStatistics OWNER TO tmg_etl_edw;
GRANT ALL ON TABLE dl.tableStatistics TO tmg_etl_edw;
GRANT SELECT ON TABLE dl.tableStatistics TO tmg_dl_read;

drop table if exists dl.resourceStatistics;
create table dl.resourceStatistics(
		serverName varchar(100) not null,
		getTime timestamp,
		usedMemory int,
		getsRate numeric(14,1),
		putsRate numeric(14,1),
		totalRegionEntryCount bigint,
		memberUpTime bigint,
		totalDiskUsage bigint,
		cpuUsage numeric(5,1),
		diskReadsRate numeric(14,1),
		diskWritesRate numeric(14,1),
		diskStoreDiskReadsRate numeric(14,1),
		diskStoreDiskWritesRate numeric(14,1),
		diskStoreTotalBytesOnDisk bigint,
		ASLDiskReadsRate numeric(14,1),
		ASLDiskWritesRate numeric(14,1),
		ASLTotalBytesOnDisk bigint,
		primary key(getTime,serverName)
)
distributed by (getTime)
PARTITION by list(serverName)
(	
		PARTITION prodrhgfxdv1 VALUES('prodrhgfxdv1.tmghealth.com'),
		PARTITION prodrhgfxdv2 VALUES('prodrhgfxdv2.tmghealth.com'),
		PARTITION prodrhgfxdv3 VALUES('prodrhgfxdv3.tmghealth.com'),
		PARTITION prodrhgfxdv4 VALUES('prodrhgfxdv4.tmghealth.com'),
		PARTITION prodrhgfxdv5 VALUES('prodrhgfxdv5.tmghealth.com'),
		PARTITION prodrhgfxdv6 VALUES('prodrhgfxdv6.tmghealth.com'),
		PARTITION prodrhgfxdv7 VALUES('prodrhgfxdv7.tmghealth.com'),
		PARTITION prodrhgfxdv8 VALUES('prodrhgfxdv8.tmghealth.com'),
		PARTITION prodrhgfxdv9 VALUES('prodrhgfxdv9.tmghealth.com'),
		PARTITION prodrhgfxdv10 VALUES('prodrhgfxdv10.tmghealth.com'),
		PARTITION prodrhgfxdv11 VALUES('prodrhgfxdv11.tmghealth.com'),
		PARTITION prodrhgfxdv12 VALUES('prodrhgfxdv12.tmghealth.com'),
		PARTITION prodrhgfxdv13 VALUES('prodrhgfxdv13.tmghealth.com'),
		PARTITION prodrhgfxdv14 VALUES('prodrhgfxdv14.tmghealth.com'),
		PARTITION prodrhgfxdv15 VALUES('prodrhgfxdv15.tmghealth.com'),
		PARTITION prodrhgfxdv16 VALUES('prodrhgfxdv16.tmghealth.com'),
		PARTITION prodrhgfxdv17 VALUES('prodrhgfxdv17.tmghealth.com'),
		PARTITION prodrhgfxdv18 VALUES('prodrhgfxdv18.tmghealth.com'),
		PARTITION prodrhgfxdv19 VALUES('prodrhgfxdv19.tmghealth.com'),
		DEFAULT PARTITION others 
);
ALTER TABLE dl.resourceStatistics OWNER TO tmg_etl_edw;
GRANT ALL ON TABLE dl.resourceStatistics TO tmg_etl_edw;
GRANT SELECT ON TABLE dl.resourceStatistics TO tmg_dl_read;
