Rem
Rem Copyright (c) 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    DESCRIPTION
Rem      Creates all tables for EMaaS Dashboard
Rem

Rem This table stores information of EMaaS Dashboard
Rem
Rem KEYS
Rem
Rem The primary key is DASHBOARD_ID           
Rem
Rem COLUMNS
Rem
Rem DASHBOARD_ID - Id of the dashboard
Rem NAME - name of the dashboard
Rem DESCRIPTION - description of the dashboard
Rem TYPE - type of the dashboard. 0: nomal dashboard, 1: one page dashboard, 2: dashboard set
Rem CREATION_DATE - creation date of the dashboard
Rem LAST_MODIFICATION_DATE - modification date of the dashboard
Rem LAST_MODIFIED_BY - latest user to modify the dashboard
Rem OWNER - owner username of the dashboard
Rem IS_SYSTEM - whether this is a system dashboard or not. A system dashbaoard can not be deleted. 0: not system dashboard, 1: is system dashboard
Rem APPLICATION_TYPE - indicate the application that an OOB dashboard belowns to. Not used fo non-OOB dashboards. 1: APM, 2: ITAnalytics, 3: LogAnalytics
Rem ENABLE_TIME_RANGE - whether enable the time range on dashboard
Rem SCREEN_SHOT - the screen shot of the dashboard
Rem DELETED - 0: not deleted, >0 deleted. If a dashboard is deleted, its deleted column should be set as its dashboard id
Rem TENANT_ID - the id of tenant
prompt EMS_DASHBOARD
CREATE TABLE EMS_DASHBOARD
  (
    DASHBOARD_ID           NUMBER(*,0) NOT NULL ,
    NAME                   VARCHAR2(64) NOT NULL,
    TYPE                   NUMBER(*,0) DEFAULT(0) NOT NULL,
    DESCRIPTION            VARCHAR2(256),
    CREATION_DATE          TIMESTAMP NOT NULL,
    LAST_MODIFICATION_DATE TIMESTAMP,
    LAST_MODIFIED_BY       VARCHAR2(128),
    OWNER                  VARCHAR2(128) NOT NULL,
    IS_SYSTEM              NUMBER(1,0) DEFAULT(1) NOT NULL,
    APPLICATION_TYPE       NUMBER(4,0),
    ENABLE_TIME_RANGE      NUMBER(1,0) DEFAULT(1) NOT NULL,
    SCREEN_SHOT            CLOB,
    DELETED                NUMBER(*,0) DEFAULT(0) NOT NULL,
    TENANT_ID              NUMBER(*,0) NOT NULL,
    CONSTRAINT EMS_DASHBOARD_PK PRIMARY KEY (DASHBOARD_ID, TENANT_ID) USING INDEX,
    CONSTRAINT EMS_DASHBOARD_U1 UNIQUE (NAME, OWNER,TENANT_ID,DELETED)    /* NOSONAR */
  ) ;
  
prompt EMS_DASHBOARD_SEQ
CREATE SEQUENCE EMS_DASHBOARD_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH 1001 CACHE 20 ORDER  NOCYCLE;

prompt EMS_DASHBOARD_TR
CREATE OR REPLACE TRIGGER  EMS_DASHBOARD_TR BEFORE INSERT ON EMS_DASHBOARD   /* NOSONAR */
FOR EACH ROW
BEGIN
  IF :new.DASHBOARD_ID IS NULL THEN
    SELECT EMS_DASHBOARD_SEQ.NEXTVAL INTO :new.DASHBOARD_ID FROM DUAL;
  END IF;
END;
/ 

Rem This table stores infomation of user favorite Dashboards
Rem
Rem KEYS
Rem    
Rem
Rem COLUMNS
Rem
Rem DASHBOARD_ID - Id of the dashboard
Rem USER_NAME - name of user
Rem CREATION_DATE - creation date of favorite
Rem TENANT_ID - the id of tenant
prompt EMS_DASHBOARD_FAVORITE
CREATE TABLE EMS_DASHBOARD_FAVORITE
  (
    USER_NAME          VARCHAR2(128) NOT NULL,
    DASHBOARD_ID       NUMBER(*,0) NOT NULL,
    CREATION_DATE      TIMESTAMP NOT NULL,
    TENANT_ID          NUMBER(*,0) NOT NULL,
    CONSTRAINT EMS_DASHBOARD_FAVORITE_PK PRIMARY KEY (USER_NAME, DASHBOARD_ID, TENANT_ID) USING INDEX,
    CONSTRAINT EMS_DASHBOARD_FAVORITE_FK1 FOREIGN KEY (DASHBOARD_ID, TENANT_ID) REFERENCES EMS_DASHBOARD (DASHBOARD_ID, TENANT_ID)    /* NOSONAR */
  ) ;
  

Rem This table stores information of Dashboard tile
Rem
Rem KEYS
Rem
Rem The primary key is TILE_ID          
Rem
Rem COLUMNS
Rem
Rem TILE_ID - Id of the dashboard tile
Rem DASHBOARD_ID - Id of the dashboard
Rem CREATION_DATE - creation date of the dashboard tile
Rem LAST_MODIFICATION_DATE - modification date of the dashboard tile
Rem LAST_MODIFIED_BY - latest user to modify the dashboard tile
Rem OWNER - owner username of the dashboard tile
Rem TITLE - title of the tile
Rem HEIGHT - HEIGHT of the tile
Rem WIDTH - WIDTH of the tile
Rem IS_MAXIMIZED - whether the tile is maximized
Rem POSITION - position of the tile
Rem TENANT_ID - the id of tenant
Rem WIDGET_UNIQUE_ID - the id by which you can uniquely identify the widget among the widgets 
Rem                    from the same provider service and its related data from anywhere
Rem WIDGET_NAME - widget name
Rem WIDGET_DESCRIPTION - widget description
Rem WIDGET_GROUP_NAME - which group this widget belongs to
Rem WIDGET_ICON - the path where icon can be got, which can be used to identify widget area, 
Rem               like LA, TA, et.c. Please use relative path, asset root URL will be got from service registration info.
Rem WIDGET_HISTOGRAM - the path where image can be got, which can be used to get a overview of this widget visually. 
Rem                    Please use relative path, asset root URL will be got from service registration info.
Rem WIDGET_OWNER - the owner of this widget
Rem WIDGET_CREATION_TIME - when this widget is created in UTC time and ISO8601 format
Rem WIDGET_SOURCE - where this widget comes from 0: Dashboard Framework, 1: Integrator
Rem WIDGET_KOC_NAME - Knockout Component name which is used to register this widget, 
Rem                naming convention: <Service_ID>_V<n>_WIDGET_xxx, ensure name is unique inside your service
Rem WIDGET_VIEWMODEL - where to load its view model javascript sources (relative path)
Rem WIDGET_TEMPLATE - where to load its template (relative path)
Rem PROVIDER_NAME - part of criteria to query provider asset root URL from SM.
Rem PROVIDER_VERSION - part of criteria to query provider asset root URL from SM.
Rem PROVIDER_ASSET_ROOT - part of criteria to query provider asset root URL from SM.
prompt EMS_DASHBOARD_TILE
CREATE TABLE EMS_DASHBOARD_TILE
  (
    TILE_ID                  NUMBER(*,0) NOT NULL,
    DASHBOARD_ID             NUMBER(*,0) NOT NULL,
    CREATION_DATE            TIMESTAMP NOT NULL,
    LAST_MODIFICATION_DATE   TIMESTAMP,
    LAST_MODIFIED_BY         VARCHAR2(128),
    OWNER                    VARCHAR2(128) NOT NULL,    
    TITLE                    VARCHAR2(64) NOT NULL,
    HEIGHT                   NUMBER(*,0),
    WIDTH                    NUMBER(*,0),
    IS_MAXIMIZED             NUMBER(1,0) DEFAULT(0) NOT NULL,
    POSITION                 NUMBER(*,0) NOT NULL,
    TENANT_ID                NUMBER(*,0) NOT NULL,
    WIDGET_UNIQUE_ID         VARCHAR2(64) NOT NULL,
    WIDGET_NAME              VARCHAR2(64) NOT NULL,
    WIDGET_DESCRIPTION       VARCHAR2(256),
    WIDGET_GROUP_NAME        VARCHAR2(64),
    WIDGET_ICON              VARCHAR2(1024),
    WIDGET_HISTOGRAM         VARCHAR2(1024),
    WIDGET_OWNER             VARCHAR2(128) NOT NULL,
    WIDGET_CREATION_TIME     VARCHAR2(32) NOT NULL,
    WIDGET_SOURCE            NUMBER(*,0) NOT NULL,
    WIDGET_KOC_NAME          VARCHAR2(256) NOT NULL,
    WIDGET_VIEWMODE          VARCHAR2(1024) NOT NULL,
    WIDGET_TEMPLATE          VARCHAR2(1024) NOT NULL,
    PROVIDER_NAME            VARCHAR2(64),
    PROVIDER_VERSION         VARCHAR2(64),
    PROVIDER_ASSET_ROOT      VARCHAR2(64),
    CONSTRAINT EMS_DASHBOARD_TILE_PK PRIMARY KEY (TILE_ID, TENANT_ID) USING INDEX,
    CONSTRAINT EMS_DASHBOARD_TILE_FK1 FOREIGN KEY (DASHBOARD_ID, TENANT_ID) REFERENCES EMS_DASHBOARD (DASHBOARD_ID, TENANT_ID)    /* NOSONAR */
  ) ;

prompt EMS_DASHBOARD_TILE_SEQ
CREATE SEQUENCE EMS_DASHBOARD_TILE_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH 10001 CACHE 20 ORDER  NOCYCLE;

prompt EMS_DASHBOARD_TILE_TR  
CREATE OR REPLACE TRIGGER  EMS_DASHBOARD_TILE_TR BEFORE INSERT ON EMS_DASHBOARD_TILE   /* NOSONAR */
FOR EACH ROW
BEGIN
  IF :new.TILE_ID IS NULL THEN
    SELECT EMS_DASHBOARD_TILE_SEQ.NEXTVAL INTO :new.TILE_ID FROM DUAL;
  END IF;
END;
/

Rem This table stores parameters of EMaaS Dashboard tile
Rem
Rem KEYS
Rem
Rem The primary key is TILE_ID, PARAM_NAME
Rem
Rem COLUMNS
Rem
Rem TILE_ID - Id of the dashboard tile
Rem PARAM_NAME - name of the dashboard tile parameter
Rem IS_SYSTEM - whether this is system parameter
Rem PARAM_TYPE - type of the dashboard tile parameter. 1: String 2: Number 3: Timestamp
Rem PARAM_VALUE_STR - string value of the dashboard tile parameter
Rem PARAM_VALUE_NUM - number value of the dashboard tile parameter
Rem PARAM_VALUE_TIMESTAMP - timestamp value of the dashboard tile parameter
prompt EMS_DASHBOARD_TILE_PARAMS
CREATE TABLE EMS_DASHBOARD_TILE_PARAMS
  (
    TILE_ID                NUMBER(*,0) NOT NULL,
    PARAM_NAME             VARCHAR2(64) NOT NULL,
    TENANT_ID              NUMBER(*,0) NOT NULL,
    IS_SYSTEM              NUMBER(1,0) DEFAULT(0) NOT NULL,
    PARAM_TYPE             NUMBER(*,0) NOT NULL,
    PARAM_VALUE_STR        VARCHAR2(1024),
    PARAM_VALUE_NUM        NUMBER(*,0),
    PARAM_VALUE_TIMESTAMP       TIMESTAMP,
    CONSTRAINT EMS_DASHBOARD_TILE_PARAMS_PK PRIMARY KEY (TILE_ID, TENANT_ID, PARAM_NAME) USING INDEX,
    CONSTRAINT EMS_DASHBOARD_TILE_PARAMS_FK1 FOREIGN KEY (TILE_ID, TENANT_ID) REFERENCES EMS_DASHBOARD_TILE (TILE_ID, TENANT_ID)    /* NOSONAR */
  ) ;

Rem This table stores the last accessed records information for EMaaS dashboard objects
Rem
Rem KEYS
Rem
Rem The primary keys are DASHBOARD_ID and ACCESSED_BY
Rem
Rem COLUMNS
Rem
Rem DASHBOARD_ID - id of dashboard
Rem ACCESSED_BY - user that accesed the objects
Rem ACCESS_DATE - access date
Rem TENANT_ID - the id of tenant
Rem 
prompt EMS_DASHBOARD_LAST_ACCESS
CREATE TABLE EMS_DASHBOARD_LAST_ACCESS 
  (
    DASHBOARD_ID   NUMBER(*,0) NOT NULL,
    ACCESSED_BY VARCHAR2(128) NOT NULL, 
    ACCESS_DATE TIMESTAMP NOT NULL,
    TENANT_ID     NUMBER(*,0) NOT NULL,
    CONSTRAINT EMS_DASHBOARD_LAST_ACCESS_PK PRIMARY KEY (DASHBOARD_ID, ACCESSED_BY,TENANT_ID) USING INDEX
   );

Rem This table stores infomation of user Preferences
Rem
Rem KEYS
Rem    
Rem
Rem COLUMNS
Rem
Rem USER_NAME - name of user
Rem PREF_KEY - key of preference
Rem PREF_VALUE - value of preference
Rem TENANT_ID - the id of tenant
prompt EMS_PREFERENCE
CREATE TABLE EMS_PREFERENCE
  (
    USER_NAME          VARCHAR2(128) NOT NULL,
    PREF_KEY           VARCHAR2(256) NOT NULL,
    PREF_VALUE         VARCHAR2(256) NOT NULL,
    TENANT_ID          NUMBER(*,0) NOT NULL,
    CONSTRAINT EMS_PREFERENCES_PK PRIMARY KEY (USER_NAME, PREF_KEY, TENANT_ID) USING INDEX
  ) ;


