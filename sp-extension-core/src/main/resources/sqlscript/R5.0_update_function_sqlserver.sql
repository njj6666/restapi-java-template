USE spext;

alter table dbo.SUBSCRIPTION_SERVER_PRODUCT add product_version varchar(20);

CREATE TABLE [dbo].[PRODUCT_VERSION](
	[UUID] [varchar](255) PRIMARY KEY NOT NULL,
	[PRODUCT_NAME] [varchar](255) NULL,
	[FULL_VERSION] [varchar](255) NULL,
	[DEFAULT_FLAG] [char](1) NULL,
	[PRIMARY_VERSION] [varchar](255) NULL,
	[URL] [varchar](255) NULL);
	
	
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'oracle','12.1.0.2','Y','12.1','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'oracle','11.2.0.4','N','11.2','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'java','1.7.0_80','N','1.8','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'java','1.8.0_45','Y','1.8','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'tomcat','7.0.61','N','7.0','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'tomcat','8.0.22','Y','8.0','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'nginx','1.6.2','Y','1.6','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'apex','4.2.6','Y','4.2','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'php','5.3','Y','5.3','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'postgresql9','9.3.5','Y','9.3','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'weblogic','12.1.3','Y','12.1','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'maven','3.3.3','Y','3.3','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'apache','2.4','Y','2.4','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'git','2.4.1','Y','2.4','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'gerrit','2.10.4','Y','2.10','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'ruby','2.0','Y','2.0','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'gitlab','7.11.4','Y','7.11','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'nexus','2.11.3-01','Y','2.11','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'sonar','5.1','Y','5.1','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'jenkins','1.615','Y','1.615','');

insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'postgresqlplus','9.3.1.3','Y','9.3','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'mysql','5.1.73','Y','5.1','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'sqlserver','11.0.3128.0','Y','11.0.3128.0','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'jboss-wildfly','8.2.0.Final','Y','8.2','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'jboss-soa-p','5.3.1.GA','Y','5.3','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'jboss-fsw','6.0.0.GA','Y','6.0','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'jboss-eap','6.2.4','Y','6.2','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'jboss-eap','6.3.3.1','N','6.3','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'jboss-eap','6.4.0','N','6.4','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'jboss-bpms','6.0.3','Y','6.0','');
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'jboss-bpms','6.1.0','N','6.1','');

insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'csdemo','1.0','Y','1.0',''); 
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'csglobal','1.0','Y','1.0',''); 
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(NEWID(),'jenkins-slave','1.0','Y','1.0','');

