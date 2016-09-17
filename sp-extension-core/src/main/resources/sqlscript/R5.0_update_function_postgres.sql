alter table SUBSCRIPTION_SERVER_PRODUCT add product_version varchar(20);

CREATE TABLE PRODUCT_VERSION(
	UUID varchar(255) PRIMARY KEY NOT NULL,
	PRODUCT_NAME varchar(255) NULL,
	FULL_VERSION varchar(255) NULL,
	DEFAULT_FLAG char(1) NULL,
	PRIMARY_VERSION varchar(255) NULL,
	URL varchar(255) NULL);
	
	
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'oracle','12.1.0.2','Y','12.1','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'oracle','11.2.0.4','N','11.2','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'java','1.7.0_80','N','1.8','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'java','1.8.0_45','Y','1.8','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'tomcat','7.0.61','N','7.0','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'tomcat','8.0.22','Y','8.0','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'nginx','1.6.2','Y','1.6','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'apex','4.2.6','Y','4.2','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'php','5.3','Y','5.3','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'postgresql9','9.3.5','Y','9.3','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'weblogic','12.1.3','Y','12.1','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'maven','3.3.3','Y','3.3','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'apache','2.4','Y','2.4','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'git','2.4.1','Y','2.4','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'gerrit','2.10.4','Y','2.10','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'ruby','2.0','Y','2.0','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'gitlab','7.11.4','Y','7.11','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'nexus','2.11.3-01','Y','2.11','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'sonar','5.1','Y','5.1','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'jenkins','1.615','Y','1.615','');

insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'postgresqlplus','9.3.1.3','Y','9.3','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'mysql','5.1.73','Y','5.1','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'sqlserver','11.0.3128.0','Y','11.0.3128.0','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'jboss-wildfly','8.2.0.Final','Y','8.2','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'jboss-soa-p','5.3.1.GA','Y','5.3','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'jboss-fsw','6.0.0.GA','Y','6.0','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'jboss-eap','6.2.4','Y','6.2','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'jboss-eap','6.3.3.1','N','6.3','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'jboss-eap','6.4.0','N','6.4','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'jboss-bpms','6.0.3','Y','6.0','');
insert into product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'jboss-bpms','6.1.0','N','6.1','');

insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'csdemo','1.0','Y','1.0',''); 
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'csglobal','1.0','Y','1.0',''); 
insert into dbo.product_version(uuid,product_name,full_version,default_flag,primary_version,url) values(uuid_generate_v4(),'jenkins-slave','1.0','Y','1.0','');


