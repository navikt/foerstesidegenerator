CREATE TABLE FOERSTESIDE
(
  foersteside_id    NUMBER(19,0)                    NOT NULL,
	loepenummer       VARCHAR2(24 CHAR)               NOT NULL,
  opprettet_dato    TIMESTAMP                       NOT NULL,
  uthentet          NUMBER(1,0)         DEFAULT 0,
  uthentet_dato     TIMESTAMP                       NOT NULL,
	CONSTRAINT PK_FOERSTESIDE PRIMARY KEY (foersteside_id)
);

CREATE TABLE FOERSTESIDE_METADATA
(
  foersteside_metadata_id   NUMBER(19,0)            NOT NULL,
	foersteside_id            NUMBER(19,0)            NOT NULL,
  key                       VARCHAR2(50 CHAR)       NOT NULL,
  value                     VARCHAR2(2000 CHAR),
	CONSTRAINT PK_FOERSTESIDE_METADATA PRIMARY KEY (foersteside_metadata_id)
);

ALTER TABLE FOERSTESIDE_METADATA
  ADD CONSTRAINT FK_FOERSTESIDE
FOREIGN KEY (foersteside_id)
REFERENCES FOERSTESIDE;

CREATE SEQUENCE FOERSTESIDE_SEQ               START WITH 100 INCREMENT BY 10 NOMAXVALUE NOCYCLE NOCACHE;
CREATE SEQUENCE FOERSTESIDE_METADATA_SEQ      START WITH 100 INCREMENT BY 10 NOMAXVALUE NOCYCLE NOCACHE;