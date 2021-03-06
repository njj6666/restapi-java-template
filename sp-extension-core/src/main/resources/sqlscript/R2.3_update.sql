-- This script is to upgrade sp-extension db before R2.3 to R2.3 release.
CREATE TABLE [dbo].[PROVIDER_REGION](
	[UUID] [varchar](255) NOT NULL,
	[IAAS_PROVIDER] [varchar](255) NULL,
	[REGION_NAME] [varchar](255) NULL,
 CONSTRAINT [PK_PROVIDER_REGION] PRIMARY KEY CLUSTERED 
(
	[UUID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE UNIQUE NONCLUSTERED INDEX [UK_PROVIDER_REGION] ON [dbo].[PROVIDER_REGION]
(
	[IAAS_PROVIDER] ASC,
	[REGION_NAME] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)

GO

ALTER TABLE dbo.PROVIDER_KEY ADD
	REGION_NAME varchar(255) NULL
GO
DROP INDEX [UK_PROVIDER_KEY_AZ] ON [dbo].[PROVIDER_KEY]
GO
ALTER TABLE dbo.PROVIDER_KEY
	DROP COLUMN AVAILABILITY_ZONE
GO

CREATE UNIQUE NONCLUSTERED INDEX [UK_PROVIDER_KEY_REGION] ON [dbo].[PROVIDER_KEY]
(
	[CSA_PROVIDER_ID] ASC,
	[REGION_NAME] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

INSERT INTO dbo.PROVIDER_REGION(UUID, IAAS_PROVIDER, REGION_NAME)
VALUES('HPCS-WEST-01', 'HPCS', 'region-a.geo-1')
GO
INSERT INTO dbo.PROVIDER_REGION(UUID, IAAS_PROVIDER, REGION_NAME)
VALUES('HPCS-EAST-01', 'HPCS', 'region-b.geo-1')
GO