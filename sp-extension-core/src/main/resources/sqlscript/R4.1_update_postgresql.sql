--Note 
--When you update an existing SP instance to SP 4.1, make sure hibernate.hbm2ddl.auto=update in spext-ds.properties  
--and you need to run the follow sql to drop a column in table PROVIDER_REGION
--ALTER TABLE PROVIDER_REGION DROP COLUMN IAAS_PROVIDER;

--If you do not want to keep the data in spext database
--set hibernate.hbm2ddl.auto to create for the first time deployment of sp-content.war
--Run following SQL and then set hibernate.hbm2ddl.auto to update
alter table subscription_log alter column message type text ;
CREATE EXTENSION "uuid-ossp";
INSERT INTO PROVIDER_TYPE(UUID, TYPE, SUB_TYPE)
VALUES(uuid_generate_v4(), 'hpcs', 'hpcs');
INSERT INTO PROVIDER_TYPE(UUID, TYPE, SUB_TYPE)
VALUES(uuid_generate_v4(), 'ospool', 'default');
INSERT INTO PROVIDER_TYPE(UUID, TYPE, SUB_TYPE)
VALUES(uuid_generate_v4(), 'vmware', 'octo');

INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), 'xsmall', 'standard.xsmall', '1', '1', 'standard.xsmall(1vcpu, 1G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), 'small', 'standard.small', '2', '2', 'standard.small(2vcpu, 2G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), 'medium', 'standard.medium', '2', '4', 'standard.medium(2vcpu, 4G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), 'large', 'standard.large', '4', '8', 'standard.large(4vcpu, 8G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), 'xlarge', 'standard.xlarge', '4', '15', 'standard.xlarge(4vcpu, 15G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), 'highmemlarge', 'highmem.large', '4', '16', 'highmem.large(4vcpu, 16G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), '2xlarge', 'standard.2xlarge', '8', '30', 'standard.2xlarge(8vcpu, 30G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), 'highmemxlarge', 'highmem.xlarge', '4', '30', 'highmem.xlarge(4vcpu, 30G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), '4xlarge', 'standard.4xlarge', '12', '60', 'standard.4xlarge(12vcpu, 60G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), 'highmem2xlarge', 'highmem.2xlarge', '8', '60', 'highmem.2xlarge(8vcpu, 60G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), '8xlarge', 'standard.8xlarge', '16', '120', 'standard.8xlarge(16vcpu, 120G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='ospool' and SUB_TYPE='default'), 'small', 'small', '2', '4', 'small');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='ospool' and SUB_TYPE='default'), 'medium', 'medium', '4', '8', 'medium');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='ospool' and SUB_TYPE='default'), 'large', 'large', '8', '16', 'large');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='ospool' and SUB_TYPE='default'), 'xlarge', 'xlarge', '16', '32', 'xlarge');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='vmware' and SUB_TYPE='octo'), 'small', 'small', '2', '2', 'small(2vcpu, 2G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='vmware' and SUB_TYPE='octo'), 'medium', 'medium', '2', '4', 'medium(2vcpu, 4G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='vmware' and SUB_TYPE='octo'), 'large', 'large', '4', '8', 'large(4vcpu, 8G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='vmware' and SUB_TYPE='octo'), 'xlarge', 'xlarge', '4', '16', 'xlarge(4vcpu, 16G vmemory)');

INSERT INTO PROVIDER_REGION(UUID, PROVIDER_TYPE_ID, REGION_NAME, REGION_DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), 'region-a.geo-1', 'US WEST');
INSERT INTO PROVIDER_REGION(UUID, PROVIDER_TYPE_ID, REGION_NAME, REGION_DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs' and SUB_TYPE='hpcs'), 'region-b.geo-1', 'US EAST');

INSERT INTO PROVIDER_REGION_AZ(UUID, REGION_ID, AZ)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_REGION WHERE REGION_NAME='region-a.geo-1'), 'az1');
INSERT INTO PROVIDER_REGION_AZ(UUID, REGION_ID, AZ)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_REGION WHERE REGION_NAME='region-a.geo-1'), 'az2');
INSERT INTO PROVIDER_REGION_AZ(UUID, REGION_ID, AZ)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_REGION WHERE REGION_NAME='region-a.geo-1'), 'az3');
INSERT INTO PROVIDER_REGION_AZ(UUID, REGION_ID, AZ)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_REGION WHERE REGION_NAME='region-b.geo-1'), 'az1');
INSERT INTO PROVIDER_REGION_AZ(UUID, REGION_ID, AZ)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_REGION WHERE REGION_NAME='region-b.geo-1'), 'az2');
INSERT INTO PROVIDER_REGION_AZ(UUID, REGION_ID, AZ)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_REGION WHERE REGION_NAME='region-b.geo-1'), 'az3');

UPDATE PROVIDER_KEY
   SET REGION_ID = (SELECT UUID FROM PROVIDER_REGION WHERE REGION_NAME='region-a.geo-1')
WHERE REGION_NAME ='region-a.geo-1';
UPDATE PROVIDER_KEY
   SET REGION_ID = (SELECT UUID FROM PROVIDER_REGION WHERE REGION_NAME='region-b.geo-1')
WHERE REGION_NAME ='region-b.geo-1';