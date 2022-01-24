CREATE TABLE PUB_SER_NO (

    SER_NO_TYP  VARCHAR(40),
    SER_NO_NAM  VARCHAR(100),
    BGN_SER_NO  INTEGER,
    CUR_SER_NO  INTEGER,
    END_SER_NO  INTEGER,
    STP_SPRD    INTEGER,
    FMT_OUT_LEN INTEGER,
    
    CONSTRAINT PK_PUBSERNO PRIMARY KEY (SER_NO_TYP)
);

CREATE TABLE EXAM (

    EXAM_CD         CHAR(17),
    TYP_CD          CHAR(1),
    RPET_IND        CHAR(1),
    CNTDWN_IND      CHAR(1),
    ANSW_IMM_IND    CHAR(1),
    TTL             VARCHAR(100),
    DSC             VARCHAR(2000),
    BGN_TIME        DATETIME,
    END_TIME        DATETIME,
    SEQ_NO          INTEGER,
    LAST_MANT_USR   VARCHAR(200),
    LAST_MANT_DAT   CHAR(8),
    LAST_MANT_TMSTP DATETIME,

    CONSTRAINT PK_EXAM PRIMARY KEY (EXAM_CD)
);
CREATE INDEX IDX_EXAM_1 ON EXAM (LAST_MANT_USR);

CREATE TABLE QUES (

    QUES_CD         CHAR(17),
    TYP_CD          CHAR(1),
    DSC             VARCHAR(2000),
    COMM            VARCHAR(2000),
    LAST_MANT_USR   VARCHAR(200),
    LAST_MANT_DAT   CHAR(8),
    LAST_MANT_TMSTP DATETIME,

    CONSTRAINT PK_QUES PRIMARY KEY (QUES_CD)
);
CREATE INDEX IDX_QUES_1 ON QUES (LAST_MANT_USR);

CREATE TABLE ANSW (

    QUES_CD         CHAR(17),
    ANSW_CD         CHAR(2),
    TYP_CD          CHAR(1),
    DSC             VARCHAR(2000),
    SCRE            INTEGER,
    LAST_MANT_USR   VARCHAR(200),
    LAST_MANT_DAT   CHAR(8),
    LAST_MANT_TMSTP DATETIME,

    CONSTRAINT PK_ANSW PRIMARY KEY (QUES_CD, ANSW_CD)
);

CREATE TABLE EXAM_QUES_RLN (

    EXAM_CD         CHAR(17),
    QUES_CD         CHAR(17),
    SEQ_NO          INTEGER,
    LAST_MANT_USR   VARCHAR(200),
    LAST_MANT_DAT   CHAR(8),
    LAST_MANT_TMSTP DATETIME,

    CONSTRAINT PK_EXAMQUESRLN PRIMARY KEY (EXAM_CD, QUES_CD)
);
CREATE INDEX IDX_EXAMQUESRLN_1 ON EXAM_QUES_RLN (QUES_CD);

CREATE TABLE EXAM_QUES_TYP_RLN (

    EXAM_CD         CHAR(17),
    QUES_TYP_CD     CHAR(1),
    SCRE            INTEGER,
    CNT             INTEGER,
    LAST_MANT_USR   VARCHAR(200),
    LAST_MANT_DAT   CHAR(8),
    LAST_MANT_TMSTP DATETIME,

    CONSTRAINT PK_EXAMQUESTYPRLN PRIMARY KEY (EXAM_CD, QUES_TYP_CD)
);

CREATE TABLE RESP_REC (

    OPEN_ID VARCHAR(200),
    EXAM_CD CHAR(17),
    SCRE    INTEGER,
    SPND    INTEGER,
    DAT     CHAR(8),

    CONSTRAINT PK_RESPREC PRIMARY KEY (EXAM_CD, OPEN_ID)
);
CREATE INDEX IDX_RESPREC_1 ON RESP_REC (OPEN_ID);

CREATE TABLE DTL_REC (

    OPEN_ID         VARCHAR(200),
    EXAM_CD         CHAR(17),
    QUES_CD         CHAR(17),
    ANSW_CD         VARCHAR(2000),
    SCRE            INTEGER,
    LAST_MANT_TMSTP DATETIME,

    CONSTRAINT PK_DTLREC PRIMARY KEY (EXAM_CD, OPEN_ID, QUES_CD)
);
CREATE INDEX IDX_DTLREC_1 ON DTL_REC (OPEN_ID);
CREATE INDEX IDX_DTLREC_2 ON DTL_REC (QUES_CD);

CREATE TABLE USR_INFO (

    OPEN_ID     VARCHAR(200),
    DPRT_NAM    VARCHAR(200),
    GRP_NAM     VARCHAR(200),
    USR_NAM     VARCHAR(200),
    REG_TMSTP   DATETIME,

    CONSTRAINT PK_USRINFO PRIMARY KEY (OPEN_ID)
);
CREATE INDEX IDX_USRINFO_1 ON USR_INFO (DPRT_NAM, GRP_NAM);

CREATE TABLE ROLE_INFO (

    OPEN_ID    VARCHAR(200),
    ROLE_ID    VARCHAR(200),
    
    CONSTRAINT PK_ROLEINFO PRIMARY KEY (OPEN_ID, ROLE_ID)
);

CREATE TABLE GRP_INFO (

    DPRT_NAM    VARCHAR(200),
    GRP_NAM     VARCHAR(200),
    SEQ_NO      INTEGER,

    CONSTRAINT PK_GRPINFO PRIMARY KEY (DPRT_NAM, GRP_NAM)
);
CREATE INDEX IDX_GRPINFO_1 ON GRP_INFO (GRP_NAM);

CREATE TABLE NCoV (

    OPEN_ID         VARCHAR(200),
    Q01             VARCHAR(200),
    Q02             VARCHAR(200),
    Q03             VARCHAR(200),
    Q04             VARCHAR(200),
    Q05             VARCHAR(200),
    Q06             VARCHAR(200),
    Q07             VARCHAR(200),
    Q08             VARCHAR(200),
    Q09             VARCHAR(200),
    Q10             VARCHAR(200),
    Q11             VARCHAR(200),
    Q12             VARCHAR(200),
    Q13             VARCHAR(200),
    Q14             VARCHAR(200),
    LAST_MANT_TMSTP DATETIME,
    USR_NAM         VARCHAR(200),
    DPRT_NAM        VARCHAR(200),
    GRP_NAM         VARCHAR(200),
    HAM_CD          VARCHAR(200),
    PHO_NUM         VARCHAR(200),
    DIST            VARCHAR(200),
    ADDR            VARCHAR(200),

    CONSTRAINT PK_NCoV PRIMARY KEY (OPEN_ID)
);
