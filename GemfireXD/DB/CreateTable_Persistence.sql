Create table ods."CLIENTS"(
    client_id bigint NOT NULL,
    ver bigint NOT NULL,
    full_client_sys_cd LONG VARCHAR,
    client_sys_cd LONG VARCHAR,
    client_lgcy_cd varchar(3) NOT NULL,
    client_acronym LONG VARCHAR,
    impl_strt_dt DATE,
    go_lve_dt DATE,
    env_cd LONG VARCHAR,
    main_app_cd LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (client_id,ver)
)
PARTITION BY COLUMN (client_id)
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;

Create table ods."ADJUSTMENT"(
    bill_ent_id bigint NOT NULL,
    adj_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    adj_typ_ref_id bigint NOT NULL,
    is_cr_adj SMALLINT,
    bill_ent_schd_id bigint NOT NULL,
    adj_amt NUMERIC(18,4) NOT NULL,
    descr LONG VARCHAR,
    post_dt DATE,
    orig_rcpt_id bigint,
    nsf_rsn_ref_id bigint,
    whld_typ_ref_id bigint,
    src_typ_ref_id bigint,
    bill_src_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,adj_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BANK"(
    bnk_org_id bigint NOT NULL,
    bnk_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    bnk_full_nm varchar(50),
    rtng_num varchar(35) NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bnk_org_id,bnk_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BENEFIT_GROUP_NAME"(
    grp_id bigint NOT NULL,
    bene_grp_id bigint NOT NULL,
    bene_grp_nm_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    bene_grp_nm varchar(80),
    descr LONG VARCHAR,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (grp_id,bene_grp_id,bene_grp_nm_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BENEFIT_GROUPS"(
    grp_id bigint NOT NULL,
    bene_pkg_id bigint NOT NULL,
    bene_grp_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    bene_grp_cd varchar(50),
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (grp_id,bene_pkg_id,bene_grp_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BENEFIT_PACKAGE"(
    bene_pkg_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    bene_pkg_cd varchar(20) NOT NULL,
    bene_pkg_typ_ref_id bigint NOT NULL,
    org_id bigint,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bene_pkg_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BILLING_ENTITY"(
    bill_ent_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    bill_ent_ref_id bigint NOT NULL,
    grp_id bigint,
    prsn_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BILLING_RECONCILIATION"(
    bill_ent_id bigint NOT NULL,
    bill_recon_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    cr_typ_ref_id bigint NOT NULL,
    cr_src_id bigint NOT NULL,
    dr_typ_ref_id bigint NOT NULL,
    dr_src_id bigint NOT NULL,
    amt NUMERIC(19,4),
    cncl_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,bill_recon_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BILLING_SCHEDULE"(
    bill_schd_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    acct_prd_strt DATE,
    acct_prd_end DATE,
    prd_mo_cnt INTEGER,
    bill_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_schd_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BILLING_SOURCE"(
    bill_ent_id bigint NOT NULL,
    src_typ_ref_id bigint NOT NULL,
    bill_src_id bigint NOT NULL,
    client_id bigint NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,src_typ_ref_id,bill_src_id,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CHARGE_ITEM"(
    bill_ent_id bigint NOT NULL,
    prem_part_id bigint NOT NULL,
    bill_ent_schd_id bigint NOT NULL,
    chrg_itm_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    chrg_ityp_ref_id bigint NOT NULL,
    chrg_itm_amt NUMERIC(19,4),
    post_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,prem_part_id,bill_ent_schd_id,chrg_itm_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CHECK"(
    clm_pay_id bigint NOT NULL,
    chk_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    chk_seq_num INTEGER,
    chk_num LONG VARCHAR,
    payee_id bigint,
    cashed_dt DATE,
    reissued_dt DATE,
    prnt_dt LONG VARCHAR,
    chk_typ LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (clm_pay_id,chk_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;









 Create table ods."CLAIM"(
    prsn_id bigint NOT NULL,
    clm_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    subs_id bigint,
    servicing_prvd_id bigint NOT NULL,
    bene_grp_id bigint,
    clm_typ_ref_id bigint NOT NULL,
    clm_sub_typ_ref_id bigint NOT NULL,
    clm_rcv_dt DATE,
    clm_inp_dt DATE,
    last_act_ts TIMESTAMP,
    clm_pd_dt DATE,
    nxt_rvw_dt DATE,
    serv_frm_dt DATE,
    serv_to_dt DATE,
    payee_prvd_id bigint,
    clm_rls_ind LONG VARCHAR,
    cur_illness_dt DATE,
    simillar_illness_dt DATE,
    pcp_id bigint,
    refng_prvd_id bigint,
    pre_auth_num LONG VARCHAR,
    pay_calc_dt DATE,
    clm_ai_eob_ind LONG VARCHAR,
    explain_cd_id bigint,
    img_addr LONG VARCHAR,
    unable_to_wrk_frm DATE,
    unable_to_wrk_to DATE,
    hosp_frm DATE,
    hosp_to DATE,
    lab_svind LONG VARCHAR,
    mdcr_re_subm_num LONG VARCHAR,
    out_of_area_ind LONG VARCHAR,
    xray_ind LONG VARCHAR,
    clm_records_ind LONG VARCHAR,
    clm_proc_dt DATE,
    crtd_frm_clm_id bigint,
    adj_to_clm_id bigint,
    adj_frm_clm_id bigint,
    ntwk_prvd_ent_prfx LONG VARCHAR,
    ntwk_prvd_prfx LONG VARCHAR,
    ntwk_prvd_cap_prfx LONG VARCHAR,
    non_partic_prvd_prfx LONG VARCHAR,
    serv_def_prfx LONG VARCHAR,
    prvd_accum_prfx LONG VARCHAR,
    prcs_ctrl_agnt_prfx LONG VARCHAR,
    mod_prc_rules_prfx LONG VARCHAR,
    prvd_ntwk_id LONG VARCHAR,
    prvd_agree_id LONG VARCHAR,
    mdcr_assign_ind LONG VARCHAR,
    pay_prvd_ind LONG VARCHAR,
    oth_bene_ind LONG VARCHAR,
    acdt_ind LONG VARCHAR,
    acdt_st LONG VARCHAR,
    acdt_dt DATE,
    acdt_amt NUMERIC,
    ptnt_acct_num LONG VARCHAR,
    ptnt_pd_amt NUMERIC,
    clm_tot_chrg NUMERIC,
    clm_tot_pay NUMERIC,
    chk_cycle_ovrd_ind LONG VARCHAR,
    clm_inp_method LONG VARCHAR,
    clm_aud_ind LONG VARCHAR,
    ext_ref_ind LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,clm_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CLIENT_REFERENCE_DATA"(
    client_ref_dta_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    cmn_ref_dta_id bigint,
    ref_ent LONG VARCHAR,
    ref_dmn LONG VARCHAR,
    ref_cd LONG VARCHAR,
    ref_descr LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (client_ref_dta_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."COB_CLAIM_DIAGNOSIS"(
    prsn_id bigint NOT NULL,
    prsn_cob_id bigint NOT NULL,
    rec_ord INTEGER NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    diag_ind char(1),
    clm_diag_cd varchar(10),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,prsn_cob_id,rec_ord,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."COB_ORGANIZATION_PERSON"(
    prsn_id bigint NOT NULL,
    prsn_cob_id bigint NOT NULL,
    org_prsn_typ_ref_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    org_nm varchar(80),
    first_nm varchar(50),
    mid_nm varchar(50),
    last_nm varchar(50),
    empr_tax_id varchar(25),
    ssn varchar(12),
    str_ln1 varchar(100),
    str_ln2 varchar(100),
    cty varchar(75),
    st varchar(50),
    pstl_cd varchar(20),
    phn_num varchar(50),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,prsn_cob_id,org_prsn_typ_ref_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CODE_VALUE"(
    cd_val_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    cd_val varchar(50) NOT NULL,
    cd_typ_ref_id bigint NOT NULL,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    del SMALLINT NOT NULL,
    PRIMARY KEY (cd_val_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."COMMUNICATION"(
    inq_id bigint NOT NULL,
    cmcn_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    track_num varchar(15),
    cntc_id bigint,
    first_nm varchar(50),
    last_nm varchar(50),
    phn_num varchar(50),
    strt_tm TIMESTAMP,
    end_tm TIMESTAMP,
    prsn_id bigint,
    insrt_usr varchar(50),
    upd_usr varchar(50),
    insrt_serv_rec_ts TIMESTAMP,
    upd_serv_rec_ts TIMESTAMP,
    prsn_evnt_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (inq_id,cmcn_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."COMPLAINT"(
    inq_id bigint NOT NULL,
    complaint_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    complaint_sub_typ_ref_id bigint NOT NULL,
    complaint_typ_ref_id bigint NOT NULL,
    prsn_id bigint,
    aor_prsn_id bigint,
    track_num varchar(15),
    case_id varchar(20),
    expd SMALLINT,
    expd_descr LONG VARCHAR,
    filling_typ_ref_id bigint,
    complaint_descr LONG VARCHAR,
    rts_offr SMALLINT,
    rts_provided SMALLINT,
    info_provided_to_caller LONG VARCHAR,
    rcpt_method_ref_id bigint,
    case_crtd TIMESTAMP,
    due_dt DATE,
    ext_dt DATE,
    ext_rsn LONG VARCHAR,
    close_dt DATE,
    drug_dosage varchar(100),
    drug_nm varchar(100),
    drug_qty varchar(100),
    prescribing_physn_nm varchar(100),
    prescribing_physn_phn varchar(50),
    prescribing_physn_fax varchar(50),
    rslvd_on_call SMALLINT,
    img_recv_dt DATE,
    coor_usr varchar(50),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (inq_id,complaint_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CONTACT"(
    cntc_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    cntc_typ_ref_id bigint NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (cntc_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."DIAGNOSIS_CODE"(
    diag_cd_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    diag_cd LONG VARCHAR,
    diag_descr LONG VARCHAR,
    rltd_diag_cd LONG VARCHAR,
    std_ref_cd_1 LONG VARCHAR,
    std_ref_cd_2 LONG VARCHAR,
    diag_typ LONG VARCHAR,
    expr_dt DATE,
    eff_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (diag_cd_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."ELECTRONIC_ADDRESS"(
    cntc_id bigint NOT NULL,
    elec_addr_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    elec_addr varchar(100),
    addr_fmt varchar(50),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (cntc_id,elec_addr_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."ENTITY_BANK_ACCOUNT"(
    bill_ent_id bigint NOT NULL,
    bnk_acct_id bigint NOT NULL,
    ent_bnk_acct_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,bnk_acct_id,ent_bnk_acct_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."EXHIBIT"(
    exhibit_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    exhibit_cd_ref_id bigint NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (exhibit_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."EXPLANATION_CODE"(
    explain_cd_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    explain_typ LONG VARCHAR,
    explain_stat LONG VARCHAR,
    sd LONG VARCHAR,
    ld_1 LONG VARCHAR,
    ld_2 LONG VARCHAR,
    explain_lblty_ind LONG VARCHAR,
    edi_clm_stat_cat LONG VARCHAR,
    edi_clm_stat LONG VARCHAR,
    edi_clm_adj_rsn LONG VARCHAR,
    rem_rmrk LONG VARCHAR,
    prvd_adj_rsn LONG VARCHAR,
    hc_plcy_id LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (explain_cd_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."GENERAL_LEDGER"(
    bill_ent_id bigint NOT NULL,
    gl_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    src_typ_ref_id bigint,
    bill_src_id bigint,
    gl_map_id bigint NOT NULL,
    dr_amt NUMERIC(18,4) NOT NULL,
    cr_amt NUMERIC(18,4) NOT NULL,
    post_dt DATE,
    crtd_dt DATE,
    je_num varchar(50),
    bene_pkg_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,gl_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."GROUP_RELATION"(
    grp_id bigint NOT NULL,
    grp_reln_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    grp_reln_typ_ref_id bigint NOT NULL,
    rltd_grp_id bigint NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (grp_id,grp_reln_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."GROUPS"(
    grp_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    grp_cd varchar(20),
    grp_typ_ref_id bigint NOT NULL,
    org_id bigint,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (grp_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."INQUIRY"(
    inq_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    caller_typ_ref_id bigint NOT NULL,
    is_in_bound SMALLINT,
    track_num varchar(15),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (inq_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."INVOICE"(
    bill_ent_id bigint NOT NULL,
    inv_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    pay_optn_ref_id bigint,
    init_flg SMALLINT,
    inv_num varchar(50),
    descr LONG VARCHAR,
    inv_amt NUMERIC(18,4) NOT NULL,
    min_due NUMERIC(18,4) NOT NULL,
    due_dt DATE,
    inv_dt DATE,
    cntc_id bigint,
    extnd_due_prd INTEGER,
    top_ent_schd_id bigint,
    cur_drct_bal NUMERIC(18,4) NOT NULL,
    cur_ssa_bal NUMERIC(18,4) NOT NULL,
    prior_drct_bal NUMERIC(18,4) NOT NULL,
    prior_ssa_bal NUMERIC(18,4) NOT NULL,
    add_ach_amt NUMERIC(18,4) NOT NULL,
    elig_expr_dt DATE,
    decd_cnt INTEGER,
    term_cnt INTEGER,
    cncl_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,inv_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."LEP_APPEAL"(
    prsn_id bigint NOT NULL,
    lep_apl_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    ext_apl_id varchar(50) NOT NULL,
    apl_dt DATE,
    rcv_dt DATE,
    apl_dcn varchar(50),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,lep_apl_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."LETTER"(
    prsn_id bigint NOT NULL,
    ltr_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,ltr_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."LINE_ADDITIONAL_DIAGNOSIS"(
    prsn_id bigint NOT NULL,
    clm_id bigint NOT NULL,
    seq_num INTEGER NOT NULL,
    clm_add_diag_id bigint NOT NULL,
    ln_add_diag_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    diag_seq_num INTEGER,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,clm_id,seq_num,clm_add_diag_id,ln_add_diag_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."LINE_DISALLOW_EXPLANATION"(
    prsn_id bigint NOT NULL,
    clm_id bigint NOT NULL,
    seq_num INTEGER NOT NULL,
    ln_disallow_explain_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    disallow_amt NUMERIC(18,4),
    explain_cd_id bigint,
    disallow_explain_cd LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,clm_id,seq_num,ln_disallow_explain_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."LINE_PROCEDURE_MODIFIER"(
    prsn_id bigint NOT NULL,
    clm_id bigint NOT NULL,
    seq_num INTEGER NOT NULL,
    ln_pr_mod_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    mod_seq_num INTEGER,
    pr_mod_cd LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,clm_id,seq_num,ln_pr_mod_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."MARX_CALENDAR"(
    marx_cal_id bigint NOT NULL,
    ver bigint NOT NULL,
    pay_dt DATE,
    plan_dta_due_dt DATE,
    mo_rpt_rls_dt DATE,
    cert_of_enrl_dt DATE,
    strt_dt DATE,
    end_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (marx_cal_id,ver)
)
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."NOTE"(
    inq_id bigint NOT NULL,
    src_id bigint NOT NULL,
    src_typ_ref_id bigint NOT NULL,
    nte_ord INTEGER NOT NULL,
    nte_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    nte_typ_ref_id bigint,
    wrk_basket varchar(50),
    nte LONG VARCHAR,
    nte_ts TIMESTAMP,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (inq_id,src_id,src_typ_ref_id,nte_ord,nte_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."ORGANIZATION_CODE"(
    org_id bigint NOT NULL,
    cd_val_id bigint NOT NULL,
    org_cd_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (org_id,cd_val_id,org_cd_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."ORGANIZATION_CONTACT_TYPE"(
    org_id bigint NOT NULL,
    cntc_typ_ref_id bigint NOT NULL,
    org_cntc_typ_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (org_id,cntc_typ_ref_id,org_cntc_typ_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."ORGANIZATIONS"(
    org_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    org_nm varchar(80),
    org_typ_ref_id bigint NOT NULL,
    descr LONG VARCHAR,
    empr_tax_id varchar(25),
    web_site varchar(100),
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (org_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PAYMENT"(
    bill_ent_id bigint NOT NULL,
    pay_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    pay_amt NUMERIC(18,4) NOT NULL,
    chk_num varchar(15),
    pay_snt_dt DATE,
    pay_src_ref_id bigint NOT NULL,
    refun_rsn_ref_id bigint NOT NULL,
    ent_bnk_acct_id bigint,
    cntc_id bigint,
    cncl_dt DATE,
    src_typ_ref_id bigint,
    bill_src_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,pay_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_COB"(
    prsn_id bigint NOT NULL,
    prsn_cob_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    act_typ_ref_id bigint,
    birth_dt DATE,
    gndr_cd char(1),
    ssn varchar(12),
    informant_reln_ref_id bigint,
    rcpt_method_ref_id bigint,
    rec_typ varchar(5) NOT NULL,
    rx_id varchar(20),
    rx_grp varchar(15),
    rx_bin varchar(15),
    rx_pcn varchar(15),
    rx_plan_phn varchar(100),
    cob_src varchar(5),
    seq_num varchar(3),
    msp_rsn_cd char(1),
    covrg_cd char(1),
    ind_plcy_num varchar(17),
    grp_plcy_num varchar(20),
    eff_dt DATE,
    expr_dt DATE,
    relshp_cd varchar(2),
    payr_id varchar(10),
    prsn_cd varchar(3),
    payr_ord varchar(3),
    emp_info_cd char(1),
    lead_contrc varchar(9),
    cls_act_typ varchar(2),
    wcsa_amt NUMERIC(12,2),
    wcsa_ind varchar(2),
    wcmsa_stlmnt_dt DATE,
    tot_rx_stlmnt_amt NUMERIC(12,2),
    is_rx_amt_incl char(1),
    term_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,prsn_cob_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_CODE"(
    prsn_id bigint NOT NULL,
    cd_val_id bigint NOT NULL,
    prsn_cd_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,cd_val_id,prsn_cd_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_CONTACT"(
    prsn_id bigint NOT NULL,
    cntc_id bigint NOT NULL,
    prsn_cntc_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    cntc_typ_ref_id bigint NOT NULL,
    cnfdt_flg SMALLINT,
    def_flg SMALLINT,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,cntc_id,prsn_cntc_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_EVENT"(
    prsn_evnt_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    src_typ_id bigint NOT NULL,
    prsn_id bigint,
    attach_src_id bigint,
    evnt_typ_ref_id bigint NOT NULL,
    client_evnt_typ_ref_id bigint,
    evnt_descr LONG VARCHAR,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id LONG VARCHAR NOT NULL,
    src_sys_rec_id LONG VARCHAR NOT NULL,
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_evnt_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_LEP_EVENT"(
    prsn_id bigint NOT NULL,
    prsn_lep_evnt_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    lep_amt NUMERIC(14,2),
    tot_uncovered_mos INTEGER,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,prsn_lep_evnt_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_LEP_PROFILE"(
    prsn_id bigint NOT NULL,
    prsn_lep_prfl_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,prsn_lep_prfl_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_ORGANIZATION_RELATION"(
    prsn_id bigint NOT NULL,
    org_id bigint NOT NULL,
    prsn_org_reln_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    reln_typ_ref_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    src_sys_rec_id varchar(150),
    src_sys_ref_id varchar(10) NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,org_id,prsn_org_reln_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_PAYMENT_OPTIONS"(
    prsn_id bigint NOT NULL,
    prsn_pay_optn_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    pay_optn_ref_id bigint NOT NULL,
    whld_typ_ref_id bigint,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,prsn_pay_optn_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_RELATION"(
    prsn_id bigint NOT NULL,
    rltd_prsn_id bigint NOT NULL,
    prsn_reln_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    reln_typ_ref_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,rltd_prsn_id,prsn_reln_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_ROLE"(
    prsn_id bigint NOT NULL,
    prsn_typ_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    prsn_typ_ref_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,prsn_typ_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_SUBSIDY_PROFILE"(
    prsn_id bigint NOT NULL,
    prsn_sbsdy_prfl_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    sbsdy_typ_ref_id bigint NOT NULL,
    sbsdy_level_ref_id bigint,
    copay_cat_ref_id bigint,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,prsn_sbsdy_prfl_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_WORK_ITEM"(
    prsn_id bigint NOT NULL,
    prsn_wrk_itm_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    wrk_ityp_ref_id bigint NOT NULL,
    add_dt DATE,
    proc_stat_ref_id bigint,
    proc_stat_dt DATE,
    prsn_evnt_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,prsn_wrk_itm_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSONS"(
    prsn_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    ttl LONG VARCHAR,
    first_nm LONG VARCHAR,
    last_nm LONG VARCHAR,
    mid_nm LONG VARCHAR,
    sfx LONG VARCHAR,
    birth_dt DATE,
    gndr_ref_id bigint,
    ethn_ref_id bigint,
    mar_stat_ref_id bigint,
    lang_ref_id bigint,
    prsn_stat_ref_id bigint,
    eff_dt DATE,
    expr_dt DATE,
    attach_src_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id LONG VARCHAR NOT NULL,
    src_sys_rec_id LONG VARCHAR NOT NULL,
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PHONE"(
    cntc_id bigint NOT NULL,
    phn_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    area_cd varchar(3),
    loc_exch varchar(10),
    phn_num varchar(50),
    phn_ext varchar(10),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (cntc_id,phn_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PLAN_MEMBERSHIPS"(
    prsn_id bigint NOT NULL,
    bene_grp_id bigint NOT NULL,
    plan_mbrshp_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    is_blbl SMALLINT,
    is_elig SMALLINT,
    elig_rsn_ref_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id LONG VARCHAR NOT NULL,
    src_sys_rec_id LONG VARCHAR,
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,bene_grp_id,plan_mbrshp_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."POS_CODE"(
    pos_cd_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    pos_cd LONG VARCHAR,
    pos_descr LONG VARCHAR,
    io_ind LONG VARCHAR,
    serv_cat_ref_id bigint,
    serv_grp_ref_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (pos_cd_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."POSTAL_ADDRESS"(
    cntc_id bigint NOT NULL,
    pstl_addr_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    str_ln1 varchar(100),
    str_ln2 varchar(100),
    str_ln3 varchar(100),
    cty varchar(75),
    cnty varchar(50),
    st varchar(50),
    pstl_cd varchar(20),
    cntry varchar(100),
    vldtd SMALLINT,
    vldtn_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (cntc_id,pstl_addr_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PREMIUM"(
    grp_id bigint NOT NULL,
    prem_cat_id bigint NOT NULL,
    prem_rec_ord INTEGER NOT NULL,
    prem_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    eff_dt DATE NOT NULL,
    expr_dt DATE,
    prem_amt_rt NUMERIC(12,4),
    prem_ratio_fctr NUMERIC(12,4),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (grp_id,prem_cat_id,prem_rec_ord,prem_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PROCEDURE_CODE"(
    pr_cd_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    pr_cd LONG VARCHAR,
    pr_descr LONG VARCHAR,
    std_ref_cd LONG VARCHAR,
    std_ref_cd_2 LONG VARCHAR,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (pr_cd_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."RECEIPT"(
    bill_ent_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    rcpt_id bigint NOT NULL,
    rcpt_typ_ref_id bigint NOT NULL,
    rcpt_amt NUMERIC(18,4) NOT NULL,
    acct_num varchar(50) NOT NULL,
    chk_num varchar(15),
    rcpt_dt DATE,
    prem_strt_dt DATE,
    prem_end_dt DATE,
    prd_mo_cnt INTEGER,
    post_dt DATE,
    confirm_num varchar(50),
    rcpt_stat_ref_id bigint NOT NULL,
    whld_typ_ref_id bigint NOT NULL,
    descr LONG VARCHAR,
    src_typ_ref_id bigint,
    bill_src_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,ver,client_id,rcpt_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."REFERENCE_DATA"(
    ref_dta_id bigint NOT NULL,
    ver bigint NOT NULL,
    ref_dmn varchar(50) NOT NULL,
    ref_cd varchar(30),
    descr LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    del SMALLINT NOT NULL,
    PRIMARY KEY (ref_dta_id,ver,ref_dmn)
)
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."RETURNED_MAIL"(
    rtrn_mail_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    prsn_id bigint,
    recv_dt DATE,
    doc_num varchar(50),
    rtrn_mail_typ_cd varchar(10),
    rtrn_rsn varchar(100),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (rtrn_mail_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."TOPIC"(
    inq_id bigint NOT NULL,
    tpc_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    case_id varchar(20),
    tpc_stat_ref_id bigint NOT NULL,
    tpc_sub_typ_ref_id bigint NOT NULL,
    tpc_typ_ref_id bigint NOT NULL,
    prsn_id bigint,
    descr LONG VARCHAR,
    dtl_descr LONG VARCHAR,
    end_tm TIMESTAMP,
    strt_tm TIMESTAMP,
    wrk_basket varchar(50),
    vld_frm_dt TIMESTAMP NOT NULL,
    insrt_usr varchar(50),
    upd_usr varchar(50),
    insrt_serv_rec_ts TIMESTAMP,
    upd_serv_rec_ts TIMESTAMP,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (inq_id,tpc_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."UNAPPLIED_CASH"(
    unapp_csh_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    err_typ_ref_id bigint,
    stat_ref_id bigint,
    src_typ_ref_id bigint,
    bill_src_id bigint,
    cncl_dt DATE,
    err_descr LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (unapp_csh_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BANK_ACCOUNT"(
    bnk_org_id bigint NOT NULL,
    bnk_id bigint NOT NULL,
    bnk_acct_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    acct_nm varchar(100),
    acct_num varchar(50) NOT NULL,
    acct_typ_ref_id bigint NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bnk_org_id,bnk_id,bnk_acct_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BENEFIT_PACKAGE_RELATION"(
    bene_pkg_id bigint NOT NULL,
    pkg_reln_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    pkg_reln_typ_ref_id bigint NOT NULL,
    rltd_bene_pkg_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bene_pkg_id,pkg_reln_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BILLING_ENTITY_CONTACT"(
    bill_ent_id bigint NOT NULL,
    cntc_id bigint NOT NULL,
    bill_ent_cntc_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    cntc_typ_ref_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,cntc_id,bill_ent_cntc_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BILLING_ENTITY_DETAIL"(
    bill_ent_id bigint NOT NULL,
    client_id bigint NOT NULL,
    ver bigint NOT NULL,
    fin_resp_ref_id bigint NOT NULL,
    grp_id bigint NOT NULL,
    is_grp_stmt SMALLINT,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,client_id,ver)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."BILLING_ENTITY_SCHEDULE"(
    bill_ent_id bigint NOT NULL,
    bill_schd_id bigint NOT NULL,
    bill_ent_schd_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    whld_typ_ref_id bigint NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,bill_schd_id,bill_ent_schd_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CHECK_STATUS"(
    clm_pay_id bigint NOT NULL,
    chk_id bigint NOT NULL,
    chk_stat_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    stat_seq_num INTEGER,
    chk_stat LONG VARCHAR,
    chk_stat_rsn LONG VARCHAR,
    stat_dt DATE,
    usr_id LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (clm_pay_id,chk_id,chk_stat_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CLAIM_ADDITIONAL_DIAGNOSIS"(
    prsn_id bigint NOT NULL,
    clm_id bigint NOT NULL,
    clm_add_diag_id bigint NOT NULL,
    client_id bigint NOT NULL,
    ver bigint NOT NULL,
    diag_cd_id bigint NOT NULL,
    trnslt_diag_cd_id bigint,
    sbmt_diag_cd_id bigint,
    prsnt_on_adm_ind LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,clm_id,clm_add_diag_id,client_id,ver)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;






















 Create table ods."CLAIM_DETAIL"(
    prsn_id bigint NOT NULL,
    clm_id bigint NOT NULL,
    seq_num INTEGER NOT NULL,
    clm_dtl_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    ln_of_bus LONG VARCHAR,
    ln_of_bus_ind LONG VARCHAR,
    cap_fnd_id LONG VARCHAR,
    cap_pool_id LONG VARCHAR,
    pos_cd_id bigint,
    pr_cd_id bigint,
    diag_cd_id bigint,
    rltd_diag_cd_id bigint,
    cur_clm_stat_cd LONG VARCHAR,
    serv_frm_dt DATE,
    serv_to_dt DATE,
    rm_typ_id LONG VARCHAR,
    clm_chrg_amt NUMERIC,
    anes_mod LONG VARCHAR,
    clm_rt_amt NUMERIC,
    alwd_amt NUMERIC,
    considered_amt NUMERIC,
    num_of_unt INTEGER,
    alwd_num_of_unt INTEGER,
    deductable_amt NUMERIC,
    copay_amt NUMERIC,
    coinsurance_amt NUMERIC,
    risk_whld_amt NUMERIC,
    clm_pd_amt NUMERIC,
    disallow_amt NUMERIC,
    explain_cd_id bigint,
    disallow_explain_cd bigint,
    agree_prc NUMERIC,
    disc_amt NUMERIC,
    suppl_disc_amt NUMERIC,
    its_disc_amt NUMERIC,
    prvd_ntwk_stat LONG VARCHAR,
    ref_ind LONG VARCHAR,
    pre_auth_ind LONG VARCHAR,
    capitated_svind LONG VARCHAR,
    subs_pay NUMERIC,
    prvd_pay NUMERIC,
    pre_pd_amt NUMERIC,
    serv_rpt_cat LONG VARCHAR,
    exper_cat LONG VARCHAR,
    acct_cat LONG VARCHAR,
    ref_id LONG VARCHAR,
    ref_seq_num INTEGER,
    pre_auth_id LONG VARCHAR,
    pre_auth_seq_num INTEGER,
    pre_auth_src LONG VARCHAR,
    io_ind LONG VARCHAR,
    ambul_pay_cd LONG VARCHAR,
    ambul_pay_stat LONG VARCHAR,
    clm_837_seq_num INTEGER,
    med_util_edit_ind LONG VARCHAR,
    sbmt_diag_cd bigint,
    sbmt_rltd_diag_cd bigint,
    trnslt_diag_cd bigint,
    trnslt_rltd_diag_cd bigint,
    prvd_specialty bigint,
    prvd_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,clm_id,seq_num,clm_dtl_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CLAIM_PAYMENT"(
    clm_pay_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    clm_pay_dt DATE,
    clm_pay_ref LONG VARCHAR,
    clm_pay_typ LONG VARCHAR,
    clm_pay_sub_typ LONG VARCHAR,
    ln_of_bus bigint,
    payee_typ LONG VARCHAR,
    subs_id bigint,
    prvd_id bigint,
    cmb_clm_flg LONG VARCHAR,
    prd_end_dt DATE,
    orig_amt NUMERIC(18,4),
    deductable_amt NUMERIC(18,4),
    clm_pay_net_amt NUMERIC(18,4),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (clm_pay_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CLAIM_PAYMENT_DETAIL"(
    prsn_id bigint NOT NULL,
    clm_id bigint NOT NULL,
    clm_pay_id bigint NOT NULL,
    clm_pay_dtl_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    ln_of_bus bigint,
    orig_amt NUMERIC(18,4),
    eob_flg LONG VARCHAR,
    vio_flg LONG VARCHAR,
    ntwk_flg LONG VARCHAR,
    prompt_pay_disc NUMERIC(18,4),
    disc_pct NUMERIC(18,4),
    prior_pd_amt NUMERIC(18,4),
    clm_pay_net_amt NUMERIC(18,4),
    prompt_pay_cd LONG VARCHAR,
    ovrd_flg LONG VARCHAR,
    int_amt NUMERIC(18,4),
    calc_int NUMERIC(18,4),
    reimb_amt NUMERIC(18,4),
    prior_reimb NUMERIC(18,4),
    confidential_flg LONG VARCHAR,
    allowable_amt NUMERIC(18,4),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,clm_id,clm_pay_id,clm_pay_dtl_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CLAIM_PAYMENT_REDUCTION"(
    clm_pay_rdctn_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    pay_rdctn_ref LONG VARCHAR,
    pay_rdctn_sub_typ LONG VARCHAR,
    plan_yr DATE,
    tax_yr DATE,
    pay_rdctn_typ LONG VARCHAR,
    crt_dt DATE,
    payee_typ LONG VARCHAR,
    prvd_id bigint,
    subs_id bigint,
    auto_pay_rdctn_flg LONG VARCHAR,
    rdctn_orig_amt NUMERIC(18,4),
    recovered_amt NUMERIC(18,4),
    recv_amt NUMERIC(18,4),
    wo_amt NUMERIC(18,4),
    rdctn_net_amt NUMERIC(18,4),
    rdctn_stat LONG VARCHAR,
    disallow_explain_cd LONG VARCHAR,
    usr_id LONG VARCHAR,
    prem_typ_flg LONG VARCHAR,
    ext_pay_flg LONG VARCHAR,
    rdctn_descr LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (clm_pay_rdctn_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CLAIM_REDUCTION_DETAIL"(
    prsn_id bigint NOT NULL,
    clm_id bigint NOT NULL,
    clm_pay_rdctn_id bigint NOT NULL,
    clm_rdctn_dtl_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    payee_typ LONG VARCHAR,
    prvd_id bigint,
    subs_id bigint,
    crt_dt DATE,
    orig_amt NUMERIC(18,4),
    auto_pay_rdctn_flg LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,clm_id,clm_pay_rdctn_id,clm_rdctn_dtl_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CLAIM_REDUCTION_HISTORY"(
    clm_pay_rdctn_id bigint NOT NULL,
    clm_rdctn_hist_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    seq_num INTEGER,
    rdctn_evnt_typ LONG VARCHAR,
    clm_pay_id bigint,
    rec_dt DATE,
    prd_end_dt DATE,
    rdctn_amt NUMERIC(18,4),
    usr_id LONG VARCHAR,
    rdctn_rsn LONG VARCHAR,
    chk_num LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (clm_pay_rdctn_id,clm_rdctn_hist_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CLAIM_STATUS"(
    prsn_id bigint NOT NULL,
    clm_id bigint NOT NULL,
    seq_num INTEGER NOT NULL,
    clm_stat_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    clm_stat_cd LONG VARCHAR,
    cur_its_stat_cd LONG VARCHAR,
    clm_stat_ts TIMESTAMP,
    stat_rsn_ref_id bigint,
    usr_id LONG VARCHAR,
    routed_usr_id LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,clm_id,seq_num,clm_stat_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."COMPLAINT_STATUS"(
    inq_id bigint NOT NULL,
    complaint_id bigint NOT NULL,
    complaint_stat_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    rej_rsn_ref_id bigint,
    complaint_stat_ref_id bigint NOT NULL,
    complaint_stat_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (inq_id,complaint_id,complaint_stat_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."CONTACT_TYPE_CONTACT"(
    cntc_id bigint NOT NULL,
    org_cntc_typ_id bigint NOT NULL,
    cntc_typ_cntc_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (cntc_id,org_cntc_typ_id,cntc_typ_cntc_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."EXHIBIT_GROUP"(
    exhibit_grp_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    bene_grp_id bigint,
    triggering_exhibit_id bigint NOT NULL,
    lang_ref_id bigint,
    suppressed_flg SMALLINT,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (exhibit_grp_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."EXHIBIT_GROUP_EXHIBIT"(
    exhibit_id bigint NOT NULL,
    exhibit_grp_id bigint NOT NULL,
    exhibit_grp_exhibit_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    cmpnt_num varchar(10),
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (exhibit_id,exhibit_grp_id,exhibit_grp_exhibit_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."GENERAL_LEDGER_MAP"(
    gl_map_id bigint NOT NULL,
    ver bigint NOT NULL,
    cr_acct_ref_id bigint NOT NULL,
    dr_acct_ref_id bigint NOT NULL,
    gl_map_typ_ref_id bigint NOT NULL,
    gl_map_nm varchar(50),
    descr LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (gl_map_id,ver)
)
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."INVOICE_DETAIL"(
    bill_ent_id bigint NOT NULL,
    inv_id bigint NOT NULL,
    inv_dtl_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    ent_schd_strt_dt DATE,
    ent_schd_end_dt DATE,
    bnfc_id bigint,
    whld_typ_ref_id bigint,
    dtl_typ_ref_id bigint,
    bnfc_reln_ref_id bigint,
    inv_dtl_amt NUMERIC(18,4) NOT NULL,
    cur_flg SMALLINT,
    cr_flg SMALLINT,
    bene_grp_id bigint,
    src_typ_ref_id bigint,
    bill_src_id bigint,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,inv_id,inv_dtl_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."INVOICE_STATUS"(
    bill_ent_id bigint NOT NULL,
    inv_id bigint NOT NULL,
    inv_stat_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    inv_stat_dt DATE,
    inv_stat_ref_id bigint NOT NULL,
    descr LONG VARCHAR,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (bill_ent_id,inv_id,inv_stat_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."LEP_APPEAL_DECISION"(
    prsn_id bigint NOT NULL,
    lep_apl_id bigint NOT NULL,
    lep_apl_dec bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    apl_dec_dt DATE,
    lep_apl_dec_ref_id bigint NOT NULL,
    rcv_dt DATE,
    mail_notice_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,lep_apl_id,lep_apl_dec,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."LETTER_DETAIL"(
    prsn_id bigint NOT NULL,
    ltr_id bigint NOT NULL,
    ltr_dtl_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,ltr_id,ltr_dtl_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."LETTER_JOB"(
    prsn_id bigint NOT NULL,
    ltr_dtl_id bigint NOT NULL,
    ltr_job_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    src_ref_id bigint,
    ltr_src_num varchar(50),
    qa_dt DATE,
    inbound_src_ref_id bigint,
    ltr_inbound_src_num varchar(50),
    pg_cnt INTEGER,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,ltr_dtl_id,ltr_job_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."LETTER_REGISTER"(
    prsn_id bigint NOT NULL,
    ltr_id bigint NOT NULL,
    ltr_rgstr_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    exhibit_id bigint NOT NULL,
    rqst_dt DATE,
    tgr_dt DATE,
    prt_dt DATE,
    mail_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,ltr_id,ltr_rgstr_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."LETTER_WORK_ITEM"(
    prsn_id bigint NOT NULL,
    ltr_rgstr_id bigint NOT NULL,
    prsn_wrk_itm_id bigint NOT NULL,
    ltr_wrk_itm_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    wrk_itm_exibit_eff_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,ltr_rgstr_id,prsn_wrk_itm_id,ltr_wrk_itm_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_EVENT_ATTRIBUTE"(
    prsn_evnt_id bigint NOT NULL,
    prsn_evnt_attr_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    attr_typ_ref_id bigint NOT NULL,
    seq_evnt_num INTEGER,
    nmrc_evnt_attr NUMERIC(23,4),
    txt_evnt_attr LONG VARCHAR,
    ts_evnt_attr TIMESTAMP,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id LONG VARCHAR NOT NULL,
    src_sys_rec_id LONG VARCHAR,
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_evnt_id,prsn_evnt_attr_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_EVENT_STATUS"(
    prsn_evnt_id bigint NOT NULL,
    prsn_evnt_stat_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    stat_ref_id bigint NOT NULL,
    evnt_stat_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_evnt_id,prsn_evnt_stat_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_EVENT_STATUS_REASON"(
    prsn_evnt_stat_id bigint NOT NULL,
    prsn_evnt_stat_rsn_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    rsn_ref_id bigint NOT NULL,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id LONG VARCHAR NOT NULL,
    src_sys_rec_id LONG VARCHAR,
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_evnt_stat_id,prsn_evnt_stat_rsn_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PERSON_LEP_PROFILE_RECORD"(
    prsn_id bigint NOT NULL,
    prsn_lep_prfl_id bigint NOT NULL,
    prsn_lep_prfl_rec_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    lep_rec_typ_ref_id bigint NOT NULL,
    creditable_covrg_flg SMALLINT,
    incr_uncovered_mos INTEGER,
    prior_plan_uncovered_mos INTEGER,
    attest_rspns_typ_ref_id bigint,
    elig_strt_dt DATE,
    end_of_iep DATE,
    gap_strt_dt DATE,
    gap_end_dt DATE,
    beq_rspns_dt DATE,
    rspns_to_attest_dt DATE,
    lep_cms_confirm_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (prsn_id,prsn_lep_prfl_id,prsn_lep_prfl_rec_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PREMIUM_CATEGORY"(
    grp_id bigint NOT NULL,
    prem_cat_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    prem_tbl_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (grp_id,prem_cat_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PREMIUM_PART"(
    grp_id bigint NOT NULL,
    prem_part_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    prem_id bigint,
    sbsdy_level_ref_id bigint,
    resp_prty_ref_id bigint NOT NULL,
    splt_method_ref_id bigint NOT NULL,
    prem_part_amt NUMERIC(19,4),
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (grp_id,prem_part_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."PREMIUM_TABLE"(
    grp_id bigint NOT NULL,
    bene_grp_id bigint NOT NULL,
    prem_tbl_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    prem_typ_ref_id bigint NOT NULL,
    eff_dt DATE,
    expr_dt DATE,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (grp_id,bene_grp_id,prem_tbl_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;





 Create table ods."TOPIC_COMMUNICATION"(
    cmcn_inq_id bigint NOT NULL,
    tpc_inquire_id bigint NOT NULL,
    cmcn_id bigint NOT NULL,
    tpc_id bigint NOT NULL,
    ver bigint NOT NULL,
    client_id bigint NOT NULL,
    insrt_usr varchar(50),
    upd_usr varchar(50),
    insrt_serv_rec_ts TIMESTAMP,
    upd_serv_rec_ts TIMESTAMP,
    vld_frm_dt TIMESTAMP NOT NULL,
    vld_to_dt TIMESTAMP,
    src_sys_ref_id varchar(10) NOT NULL,
    src_sys_rec_id varchar(150),
    del SMALLINT NOT NULL,
    PRIMARY KEY (cmcn_inq_id,tpc_inquire_id,cmcn_id,tpc_id,ver,client_id)
)
PARTITION BY COLUMN (client_id)
COLOCATE WITH (ods."CLIENTS")
REDUNDANCY 1
AsyncEventListener(AsychronorousListener)
PERSISTENT 'STORE1' ASYNCHRONOUS;
