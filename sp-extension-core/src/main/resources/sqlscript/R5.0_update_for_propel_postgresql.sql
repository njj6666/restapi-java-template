INSERT INTO PROVIDER_TYPE(UUID, TYPE, SUB_TYPE)
VALUES(uuid_generate_v4(), 'hpcs_propel', 'default');

INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), 'xsmall', 'standard.xsmall', '1', '1', 'standard.xsmall(1vcpu, 1G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), 'small', 'standard.small', '2', '2', 'standard.small(2vcpu, 2G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), 'medium', 'standard.medium', '2', '4', 'standard.medium(2vcpu, 4G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), 'large', 'standard.large', '4', '8', 'standard.large(4vcpu, 8G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), 'xlarge', 'standard.xlarge', '4', '15', 'standard.xlarge(4vcpu, 15G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), 'highmemlarge', 'highmem.large', '4', '16', 'highmem.large(4vcpu, 16G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), '2xlarge', 'standard.2xlarge', '8', '30', 'standard.2xlarge(8vcpu, 30G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), 'highmemxlarge', 'highmem.xlarge', '4', '30', 'highmem.xlarge(4vcpu, 30G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), '4xlarge', 'standard.4xlarge', '12', '60', 'standard.4xlarge(12vcpu, 60G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), 'highmem2xlarge', 'highmem.2xlarge', '8', '60', 'highmem.2xlarge(8vcpu, 60G vmemory)');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), '8xlarge', 'standard.8xlarge', '16', '120', 'standard.8xlarge(16vcpu, 120G vmemory)');


INSERT INTO PROVIDER_REGION(UUID, PROVIDER_TYPE_ID, REGION_NAME, REGION_DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default'), 'region-a.geo-1', 'US WEST');

INSERT INTO PROVIDER_REGION_AZ(UUID, REGION_ID, AZ)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_REGION WHERE PROVIDER_TYPE_ID=(SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default') and REGION_NAME='region-a.geo-1'), 'az1');
INSERT INTO PROVIDER_REGION_AZ(UUID, REGION_ID, AZ)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_REGION WHERE PROVIDER_TYPE_ID=(SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default') and REGION_NAME='region-a.geo-1'), 'az2');
INSERT INTO PROVIDER_REGION_AZ(UUID, REGION_ID, AZ)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_REGION WHERE PROVIDER_TYPE_ID=(SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='hpcs_propel' and SUB_TYPE='default') and REGION_NAME='region-a.geo-1'), 'az3');


INSERT INTO PROVIDER_TYPE(UUID, TYPE, SUB_TYPE)
VALUES(uuid_generate_v4(), 'ospool_propel', 'default');

INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='ospool_propel' and SUB_TYPE='default'), 'small', 'small', '2', '4', 'small');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='ospool_propel' and SUB_TYPE='default'), 'medium', 'medium', '4', '8', 'medium');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='ospool_propel' and SUB_TYPE='default'), 'large', 'large', '8', '16', 'large');
INSERT INTO PROVIDER_SIZE(UUID, PROVIDER_TYPE_ID, SIZE, FLAVOR, VCPU, VMEMORY, DISPLAY_NAME)
VALUES(uuid_generate_v4(), (SELECT UUID FROM PROVIDER_TYPE WHERE TYPE='ospool_propel' and SUB_TYPE='default'), 'xlarge', 'xlarge', '16', '32', 'xlarge');