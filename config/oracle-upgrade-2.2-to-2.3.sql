CREATE INDEX iParent ON m_task (parent) INITRANS 30;

ALTER TABLE m_sync_situation_description ADD fullFlag NUMBER(1, 0);
ALTER TABLE m_shadow ADD fullSynchronizationTimestamp TIMESTAMP;
ALTER TABLE m_task ADD expectedTotal NUMBER(19, 0);
ALTER TABLE m_assignment ADD disableReason VARCHAR2(255 CHAR);
ALTER TABLE m_focus ADD disableReason VARCHAR2(255 CHAR);
ALTER TABLE m_shadow ADD disableReason VARCHAR2(255 CHAR);

CREATE TABLE m_report (
    name_norm VARCHAR2(255 CHAR),
    name_orig VARCHAR2(255 CHAR),
    class_namespace VARCHAR2(255 CHAR),
    class_localPart VARCHAR2(100 CHAR),
    query CLOB,
    reportExport NUMBER(10,0),
    reportFields CLOB,
    reportOrientation NUMBER(10,0),
    reportParameters CLOB,
    reportTemplateJRXML CLOB,
    reportTemplateStyleJRTX CLOB,
    id NUMBER(19,0) NOT NULL,
    oid VARCHAR2(36 CHAR) NOT NULL,
    PRIMARY KEY (id, oid),
    UNIQUE (name_norm)
) INITRANS 30;

CREATE INDEX iReportName ON m_report (name_orig) INITRANS 30;

ALTER TABLE m_report 
    ADD CONSTRAINT fk_report 
    FOREIGN KEY (id, oid) 
    REFERENCES m_object;

