SP Content REST
====

## Build

### (optional) if want to use PostgreSQL

#### create database on PostgreSQL

```
sudo -H -u postgres psql <<EOF
create role spextdbuser login password 'spext' superuser inherit;
create database spextdb with owner=spextdbuser connection limit=-1;
grant all on database spextdb to spextdbuser;
EOF
```
#### update bean.xml to use PostgreSQL drive and access info

open file `sp-extension\sp-extension-rest\src\main\resources\spext-ds.properties`
```
# jdbc for postgresql
jdbc.driverClassName=org.postgresql.Driver
jdbc.url=jdbc:postgresql://localhost:5432/spextdb
jdbc.username=spextdbuser
jdbc.password=spext
```

#### ask hibernate to create the tables

open file `sp-extension\sp-extension-rest\src\main\resources\spext-ds.properties`
```
hibernate.hbm2ddl.auto=update
```

### now build war

```
cd sp-extension
mvn -e clean package
```
## Deploy on Linux

### deploy SP content war to JBoss of CSA

```
cp sp-extension/sp-extension-rest/target/sp-content.war $CSA_HOME/jboss-as-7.1.1.Final/standalone/deployments
# jboss now hot-scanner will deploy the war
```

or, auto deploy with maven:
```
mvn -e -Dcsa.host=<the_csa_host_name_or_ip_address> -Dcsa.user.keyfile=<path_of_private_key> clean package antrun:run
```
*IMPORTANT*: under neath, it uses ant ssh/scp with maven-antrun-plugin, so there must be a ssh server working on remote csa server, and you should configure the ssh key pair for authentication.
default user is 'csauser', you can change it by specify the java property to maven: -Dcsa.user=<csa_user_name>
default private key file path is '${user.home}/.ssh/spadmin_dsa, you can change it by specify the java property to maven: -Dcsa.user.keyfile=<path_of_private_key>
*NOTE*: this also apply to deploy `sp-extension-ui-csa` and `sp-extension-ui-csp`:
```
cd sp-extension-ui-csp
mvn -e -Dcsa.host=<the_csa_host_name_or_ip_address> -Dcsa.user.keyfile=<path_of_private_key> clean package antrun:run
cd sp-extension-ui-csa
mvn -e -Dcsa.host=<the_csa_host_name_or_ip_address> -Dcsa.user.keyfile=<path_of_private_key> clean package antrun:run
```

if you want to deploy all of these three with one command:
```
cd sp-extension
mvn -e -Dcsa.host=<the_csa_host_name_or_ip_address> -Dcsa.user.keyfile=<path_of_private_key> clean package antrun:run
```

## REST interfaces

### Provider Key

* list provider keys
  + GET rest/v1/provider-keys
  + Query parameter
      - csa-provider-id     the CSA provider ID
      - region-name         the region name of iaas provider
  + example
      - list all
        curl -X GET "http://localhost:8080/sp-content/rest/v1/provider-keys.json"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/provider-keys"
      - get key of specific AZ of provider
        curl -X GET "http://localhost:8080/sp-content/rest/v1/provider-keys.json?csa-provider-id=ff808081428eb41d0142fa6cfbb0591f&availability-zone=az-3.region-a.geo-1"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/provider-keys?csa-provider-id=ff808081428eb41d0142fa6cfbb0591f&availability-zone=az-3.region-a.geo-1"
* add provider key
  + POST rest/v1/provider-keys
  + Form parameter
  	  - name                the name of the provider key
      - csa-provider-id     the CSA provider ID
      - region-name         the region name of iaas provider
      - auth-type           "key" or "password"
      - username            the user name for SSH login
      - password            password for "password" auth-type
      - key-file            private key file for "key" auth-type
  + example
      - request
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json" --form auth-type=key --form name=hpcs-west-01 --form username=root --form key-file=@spcluster_dsa --form csa-provider-id=ff808081428eb41d0142fa6cfbb0591f --form region-name=WEST.region-a.geo-1 "http://localhost:8080/sp-content/rest/v1/provider-keys"
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json" --form auth-type=password --form name=test --form username=root --form password=root --form csa-provider-id=ff808081428eb41d0142fa6cfbb0591f "http://localhost:8080/sp-content/rest/v1/provider-keys"
      - response
        {"uuid":"b8864163-c7ed-484c-849f-e32fb5e50455","username":"root","password":null,"
        csa-provider-id":"ff808081428eb41d0142fa6cfbb0591f","availability-zone":"az-3.re
        gion-a.geo-1","key-file":"LS0tLS1CRUdJTiBEU0EgUFJJVkFURSBLRVktLS0tLQpNSUlCdXdJQk
        FBS0JnUURBRXVLVTY2N0FiUVhBOHgvTGVOSHEyRnZYTzY3RHBLMmpvb09ldW9xUHRpbXEzZGo4CndLSW
        dTb2JEYzNTUFdIdGFHOWNRb21tT0VYQk55aEk1NFp3R3oveGlqOCtUZE4rQkJpbTBGNnNmZmM3Y3VHVF
        IKOWlWN0gzOXNTS1JyTkkvUmIxV01rT1I1RnZZM1Fha3k5SFRvOTFyTFVmL1ZzeWNpUXRCNnpMNmxtUU
        lWQU5TZwpvaFRoaGJFc1BkUytVRm44TWJwTXFBSzNBb0dBYW50MnJWamx1WHhlNlZiUkNmOTM0cEZqeU
        syd0xabnpxdkFXCjZWRFBlN1pkU2R4bFNQTWpNMXlRenB2cVp6bmVyRDNRY1V2b3VYRXRGbDhSaGhpTj
        RnQjFSR1orUGlmQ096Vm4KTDhtZHFzNG5NckRweFVENGpiMjJxSXZGMmVJaExRZWN4ZHJWaHNWM2thSn
        VYemdNVjlkbmcwNUtPQ2ZjMDJmRQpXcWN2eG53Q2dZRUF1eFYwV3lvZmU3Z0RFMXlKRG9NbENJMzVxd3
        crVWlSdlNUQkQ4cXh5NHBFcUhtVDVyZ0VoCjBXalpXalFVNWxVd3BZSThIb1NYTkwxaEV6a3BzZlpzWl
        NxSDdRc0pQcUFFMzlFN1doVmRQK2UxOFJpM1JFUGwKc2FCSnlSeFVIcmlzRm1kSkZhaHRUYmNUb2t6Q1
        lJR1QrQkprUVZlTTBDdWZXR1A2Z2VCNE5Ya0NGQUttSHFlOQp6N0RFZ3gxVnpQOHJGcGk3YlU3awotLS
        0tLUVORCBEU0EgUFJJVkFURSBLRVktLS0tLQo=","create-date":1387350617293}
        *NOTE*: the key file is base64 encoded
* update provider key
  + PUT rest/v1/provider-keys/{uuid}
  + Request parameter
      - uuid                the UUID of the provider key
  + Form parameter
      - name                the name of the provider key
      - csa-provider-id     the CSA provider ID
      - region-name         the region name of iaas provider
      - auth-type           "key" or "password"
      - username            the user name for SSH login
      - password            password for "password" auth-type
      - key-file            private key file for "key" auth-type
  + example
      - request
        curl -X PUT -H "Content-Type: multipart/form-data;" -H "Accept: application/json" --form auth-type=password --form name=ECS --form username=root --form password=sopr00t "http://localhost:8080/sp-content/rest/v1/provider-keys/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
        {"uuid":"b8864163-c7ed-484c-849f-e32fb5e50455","username":"root","password":"sop
        r00t","csa-provider-id":"ff808081428eb41d0142fa6cfbb0591f","availability-zone":"
        az-3.region-a.geo-1","key-file":"LS0tLS1CRUdJTiBEU0EgUFJJVkFURSBLRVktLS0tLQpNSUlCdXdJQkFBS0JnUU
        RBRXVLVTY2N0FiUVhBOHgvTGVOSHEyRnZYTzY3RHBLMmpvb09ldW9xUHRpbXEzZGo4CndLSWdTb2JEYz
        NTUFdIdGFHOWNRb21tT0VYQk55aEk1NFp3R3oveGlqOCtUZE4rQkJpbTBGNnNmZmM3Y3VHVFIKOWlWN0
        gzOXNTS1JyTkkvUmIxV01rT1I1RnZZM1Fha3k5SFRvOTFyTFVmL1ZzeWNpUXRCNnpMNmxtUUlWQU5TZw
        pvaFRoaGJFc1BkUytVRm44TWJwTXFBSzNBb0dBYW50MnJWamx1WHhlNlZiUkNmOTM0cEZqeUsyd0xabn
        pxdkFXCjZWRFBlN1pkU2R4bFNQTWpNMXlRenB2cVp6bmVyRDNRY1V2b3VYRXRGbDhSaGhpTjRnQjFSR1
        orUGlmQ096Vm4KTDhtZHFzNG5NckRweFVENGpiMjJxSXZGMmVJaExRZWN4ZHJWaHNWM2thSnVYemdNVj
        lkbmcwNUtPQ2ZjMDJmRQpXcWN2eG53Q2dZRUF1eFYwV3lvZmU3Z0RFMXlKRG9NbENJMzVxd3crVWlSdl
        NUQkQ4cXh5NHBFcUhtVDVyZ0VoCjBXalpXalFVNWxVd3BZSThIb1NYTkwxaEV6a3BzZlpzWlNxSDdRc0
        pQcUFFMzlFN1doVmRQK2UxOFJpM1JFUGwKc2FCSnlSeFVIcmlzRm1kSkZhaHRUYmNUb2t6Q1lJR1QrQk
        prUVZlTTBDdWZXR1A2Z2VCNE5Ya0NGQUttSHFlOQp6N0RFZ3gxVnpQOHJGcGk3YlU3awotLS0tLUVORC
        BEU0EgUFJJVkFURSBLRVktLS0tLQo="}
        *NOTE*: the key file is base64 encoded
* get provider key by uuid
  + GET rest/v1/provider-keys/{uuid}
  + Request parameter
      - uuid                the UUID of the provider key
  + example
      - request
        curl -X GET "http://localhost:8080/sp-content/rest/v1/provider-keys/b8864163-c7ed-484c-849f-e32fb5e50455.json"
      - response
        {"uuid":"b8864163-c7ed-484c-849f-e32fb5e50455","username":null,"password":null,"
        csa-provider-id":"ff808081428eb41d0142fa6cfbb0591f","availability-zone":"az-3.re
        gion-a.geo-1","key-file":"LS0tLS1CRUdJTiBEU0EgUFJJVkFURSBLRVktLS0tLQpNSUlCdXdJQk
        FBS0JnUURBRXVLVTY2N0FiUVhBOHgvTGVOSHEyRnZYTzY3RHBLMmpvb09ldW9xUHRpbXEzZGo4CndLSW
        dTb2JEYzNTUFdIdGFHOWNRb21tT0VYQk55aEk1NFp3R3oveGlqOCtUZE4rQkJpbTBGNnNmZmM3Y3VHVF
        IKOWlWN0gzOXNTS1JyTkkvUmIxV01rT1I1RnZZM1Fha3k5SFRvOTFyTFVmL1ZzeWNpUXRCNnpMNmxtUU
        lWQU5TZwpvaFRoaGJFc1BkUytVRm44TWJwTXFBSzNBb0dBYW50MnJWamx1WHhlNlZiUkNmOTM0cEZqeU
        syd0xabnpxdkFXCjZWRFBlN1pkU2R4bFNQTWpNMXlRenB2cVp6bmVyRDNRY1V2b3VYRXRGbDhSaGhpTj
        RnQjFSR1orUGlmQ096Vm4KTDhtZHFzNG5NckRweFVENGpiMjJxSXZGMmVJaExRZWN4ZHJWaHNWM2thSn
        VYemdNVjlkbmcwNUtPQ2ZjMDJmRQpXcWN2eG53Q2dZRUF1eFYwV3lvZmU3Z0RFMXlKRG9NbENJMzVxd3
        crVWlSdlNUQkQ4cXh5NHBFcUhtVDVyZ0VoCjBXalpXalFVNWxVd3BZSThIb1NYTkwxaEV6a3BzZlpzWl
        NxSDdRc0pQcUFFMzlFN1doVmRQK2UxOFJpM1JFUGwKc2FCSnlSeFVIcmlzRm1kSkZhaHRUYmNUb2t6Q1
        lJR1QrQkprUVZlTTBDdWZXR1A2Z2VCNE5Ya0NGQUttSHFlOQp6N0RFZ3gxVnpQOHJGcGk3YlU3awotLS
        0tLUVORCBEU0EgUFJJVkFURSBLRVktLS0tLQo=","create-date":1387350617293}
        *NOTE*: the key file is base64 encoded
    or
      - request
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/provider-keys/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?><provider-key><uuid>b8864
        163-c7ed-484c-849f-e32fb5e50455</uuid><csa-provider-id>ff808081428eb41d0142fa6cf
        bb0591f</csa-provider-id><availability-zone>az-3.region-a.geo-1</availability-zo
        ne><key-file>LS0tLS1CRUdJTiBEU0EgUFJJVkFURSBLRVktLS0tLQpNSUlCdXdJQkFBS0JnUURBRXV
        LVTY2N0FiUVhBOHgvTGVOSHEyRnZYTzY3RHBLMmpvb09ldW9xUHRpbXEzZGo4CndLSWdTb2JEYzNTUFd
        IdGFHOWNRb21tT0VYQk55aEk1NFp3R3oveGlqOCtUZE4rQkJpbTBGNnNmZmM3Y3VHVFIKOWlWN0gzOXN
        TS1JyTkkvUmIxV01rT1I1RnZZM1Fha3k5SFRvOTFyTFVmL1ZzeWNpUXRCNnpMNmxtUUlWQU5TZwpvaFR
        oaGJFc1BkUytVRm44TWJwTXFBSzNBb0dBYW50MnJWamx1WHhlNlZiUkNmOTM0cEZqeUsyd0xabnpxdkF
        XCjZWRFBlN1pkU2R4bFNQTWpNMXlRenB2cVp6bmVyRDNRY1V2b3VYRXRGbDhSaGhpTjRnQjFSR1orUGl
        mQ096Vm4KTDhtZHFzNG5NckRweFVENGpiMjJxSXZGMmVJaExRZWN4ZHJWaHNWM2thSnVYemdNVjlkbmc
        wNUtPQ2ZjMDJmRQpXcWN2eG53Q2dZRUF1eFYwV3lvZmU3Z0RFMXlKRG9NbENJMzVxd3crVWlSdlNUQkQ
        4cXh5NHBFcUhtVDVyZ0VoCjBXalpXalFVNWxVd3BZSThIb1NYTkwxaEV6a3BzZlpzWlNxSDdRc0pQcUF
        FMzlFN1doVmRQK2UxOFJpM1JFUGwKc2FCSnlSeFVIcmlzRm1kSkZhaHRUYmNUb2t6Q1lJR1QrQkprUVZ
        lTTBDdWZXR1A2Z2VCNE5Ya0NGQUttSHFlOQp6N0RFZ3gxVnpQOHJGcGk3YlU3awotLS0tLUVORCBEU0E
        gUFJJVkFURSBLRVktLS0tLQo=</key-file><create-date>2013-12-18T15:10:17.293+08:00</
        create-date></provider-key>
        *NOTE*: the key file is base64 encoded
* delete provider key
  + DELETE rest/v1/provider-keys/{uuid}
  + Request parameter
      - uuid                the UUID of the provider key
  + example
    curl -X DELETE "http://localhost:8080/sp-content/rest/v1/provider-keys/85aeae3e-1869-4d81-bd94-a398ff26f49f"

### Provider Region
* list provider regions
  + GET rest/v1/provider regions
  + example
      - list all provider-regions
      curl -X GET "http://localhost:8080/sp-content/rest/v1/provider-regions.json"

      - list provider regions by iaas provider
      curl -X GET "http://localhost:8080/sp-content/rest/v1/provider-regions.json?iaas-provider=hpcs"
  
### User Key

* list user keys
  + GET rest/v1/user-keys
  + example
      - list all keys
        curl -X GET "http://localhost:8080/sp-content/rest/v1/user-keys.json"
* add user key
  + POST rest/v1/user-keys
  + example
      - request
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json" --form public-key=@spcluster_dsa.pub --form email=xuqing.tan@hp.com --form name=default-key "http://localhost:8080/sp-content/rest/v1/user-keys"
      - response
        {"uuid":"85aeae3e-1869-4d81-bd94-a398ff26f49f","name":"default-key","email":"xuq
        ing.tan@hp.com","public-key":"c3NoLWRzcyBBQUFBQjNOemFDMWtjM01BQUFDQkFNQVM0cFRycn
        NCdEJjRHpIOHQ0MGVyWVc5Yzdyc09rcmFPaWc1NjZpbysyS2FyZDJQekFvaUJLaHNOemRJOVllMW9iMX
        hDaWFZNFJjRTNLRWpuaG5BYlAvR0tQejVOMDM0RUdLYlFYcXg5OXp0eTRaTkgySlhzZmYyeElwR3Mwaj
        lGdlZZeVE1SGtXOWpkQnFUTDBkT2ozV3N0Ui85V3pKeUpDMEhyTXZxV1pBQUFBRlFEVW9LSVU0WVd4TE
        QzVXZsQlovREc2VEtnQ3R3QUFBSUJxZTNhdFdPVzVmRjdwVnRFSi8zZmlrV1BJcmJBdG1mT3E4QmJwVU
        05N3RsMUozR1ZJOHlNelhKRE9tK3BuT2Q2c1BkQnhTK2k1Y1MwV1h4R0dHSTNpQUhWRVpuNCtKOEk3Tl
        djdnlaMnF6aWN5c09uRlFQaU52YmFvaThYWjRpRXRCNXpGMnRXR3hYZVJvbTVmT0F4WDEyZURUa280Sj
        l6VFo4UmFweS9HZkFBQUFJRUF1eFYwV3lvZmU3Z0RFMXlKRG9NbENJMzVxd3crVWlSdlNUQkQ4cXh5NH
        BFcUhtVDVyZ0VoMFdqWldqUVU1bFV3cFlJOEhvU1hOTDFoRXprcHNmWnNaU3FIN1FzSlBxQUUzOUU3V2
        hWZFArZTE4UmkzUkVQbHNhQkp5UnhVSHJpc0ZtZEpGYWh0VGJjVG9rekNZSUdUK0JKa1FWZU0wQ3VmV0
        dQNmdlQjROWGs9IGpib3NzYWRtQHVzcGxzdnVseDI3Ny5lbGFicy5lZHMuY29tCg=="}
        *NOTE*: the key file is base64 encoded
* get user key by uuid
  + GET rest/v1/user-keys/{uuid}
  + Request parameter
      - uuid                the UUID of the user key
  + example
      - request
        curl -X GET "http://localhost:8080/sp-content/rest/v1/user-keys/85aeae3e-1869-4d81-bd94-a398ff26f49f.json"
      - response
        {"uuid":"85aeae3e-1869-4d81-bd94-a398ff26f49f","name":"default-key","email":"xuq
        ing.tan@hp.com","public-key":"c3NoLWRzcyBBQUFBQjNOemFDMWtjM01BQUFDQkFNQVM0cFRycn
        NCdEJjRHpIOHQ0MGVyWVc5Yzdyc09rcmFPaWc1NjZpbysyS2FyZDJQekFvaUJLaHNOemRJOVllMW9iMX
        hDaWFZNFJjRTNLRWpuaG5BYlAvR0tQejVOMDM0RUdLYlFYcXg5OXp0eTRaTkgySlhzZmYyeElwR3Mwaj
        lGdlZZeVE1SGtXOWpkQnFUTDBkT2ozV3N0Ui85V3pKeUpDMEhyTXZxV1pBQUFBRlFEVW9LSVU0WVd4TE
        QzVXZsQlovREc2VEtnQ3R3QUFBSUJxZTNhdFdPVzVmRjdwVnRFSi8zZmlrV1BJcmJBdG1mT3E4QmJwVU
        05N3RsMUozR1ZJOHlNelhKRE9tK3BuT2Q2c1BkQnhTK2k1Y1MwV1h4R0dHSTNpQUhWRVpuNCtKOEk3Tl
        djdnlaMnF6aWN5c09uRlFQaU52YmFvaThYWjRpRXRCNXpGMnRXR3hYZVJvbTVmT0F4WDEyZURUa280Sj
        l6VFo4UmFweS9HZkFBQUFJRUF1eFYwV3lvZmU3Z0RFMXlKRG9NbENJMzVxd3crVWlSdlNUQkQ4cXh5NH
        BFcUhtVDVyZ0VoMFdqWldqUVU1bFV3cFlJOEhvU1hOTDFoRXprcHNmWnNaU3FIN1FzSlBxQUUzOUU3V2
        hWZFArZTE4UmkzUkVQbHNhQkp5UnhVSHJpc0ZtZEpGYWh0VGJjVG9rekNZSUdUK0JKa1FWZU0wQ3VmV0
        dQNmdlQjROWGs9IGpib3NzYWRtQHVzcGxzdnVseDI3Ny5lbGFicy5lZHMuY29tCg=="}
        *NOTE*: the key file is base64 encoded
* update user key
  + PUT rest/v1/user-keys/{uuid}
  + Request parameter
      - uuid                the UUID of the user key
  + example
    curl -X PUT -H "Content-Type: multipart/form-data;" -H "Accept: application/json" --form public-key=@spcluster_dsa.pub --form email=xuqing.tan@hp.com --form name=default-key "http://localhost:8080/sp-content/rest/v1/user-keys/85aeae3e-1869-4d81-bd94-a398ff26f49f"
* delete user key
  + DELETE rest/v1/user-keys/{uuid}
  + example
    curl -X DELETE "http://localhost:8080/sp-content/rest/v1/user-keys/85aeae3e-1869-4d81-bd94-a398ff26f49f"

### OO Tailored REST APIs

* get user public keys
  + GET rest/v1/keys/users/{email}
  + Request parameter
      - email               the user's email
  + Response
      - string contains all the public keys of the user
  + example
      - request
        curl -X GET "http://localhost:8080/sp-content/rest/v1/keys/users/xuqing.tan@hp.com"
      - response
        ssh-dss AAAAB3NzaC1kc3MAAACBAMAS4pTrrsBtBcDzH8t40erYW9c7rsOkraOig566io+2Kard2PzA
        oiBKhsNzdI9Ye1ob1xCiaY4RcE3KEjnhnAbP/GKPz5N034EGKbQXqx99zty4ZNH2JXsff2xIpGs0j9Fv
        VYyQ5HkW9jdBqTL0dOj3WstR/9WzJyJC0HrMvqWZAAAAFQDUoKIU4YWxLD3UvlBZ/DG6TKgCtwAAAIBq
        e3atWOW5fF7pVtEJ/3fikWPIrbAtmfOq8BbpUM97tl1J3GVI8yMzXJDOm+pnOd6sPdBxS+i5cS0WXxGG
        GI3iAHVEZn4+J8I7NWcvyZ2qzicysOnFQPiNvbaoi8XZ4iEtB5zF2tWGxXeRom5fOAxX12eDTko4J9zT
        Z8Rapy/GfAAAAIEAuxV0Wyofe7gDE1yJDoMlCI35qww+UiRvSTBD8qxy4pEqHmT5rgEh0WjZWjQU5lUw
        pYI8HoSXNL1hEzkpsfZsZSqH7QsJPqAE39E7WhVdP+e18Ri3REPlsaBJyRxUHrisFmdJFahtTbcTokzC
        YIGT+BJkQVeM0CufWGP6geB4NXk= jbossadm@usplsvulx277.elabs.eds.com
* get user public key by key name
  + GET rest/v1/keys/users/{email}/{key-name}
  + Request parameter
      - email               the user's email
      - key-name            the public key name
  + Response
      - string contains the public key content
  + example
      - request
        curl -X GET "http://localhost:8080/sp-content/rest/v1/keys/users/xuqing.tan@hp.com/default-key"
      - response
        ssh-dss AAAAB3NzaC1kc3MAAACBAMAS4pTrrsBtBcDzH8t40erYW9c7rsOkraOig566io+2Kard2PzA
        oiBKhsNzdI9Ye1ob1xCiaY4RcE3KEjnhnAbP/GKPz5N034EGKbQXqx99zty4ZNH2JXsff2xIpGs0j9Fv
        VYyQ5HkW9jdBqTL0dOj3WstR/9WzJyJC0HrMvqWZAAAAFQDUoKIU4YWxLD3UvlBZ/DG6TKgCtwAAAIBq
        e3atWOW5fF7pVtEJ/3fikWPIrbAtmfOq8BbpUM97tl1J3GVI8yMzXJDOm+pnOd6sPdBxS+i5cS0WXxGG
        GI3iAHVEZn4+J8I7NWcvyZ2qzicysOnFQPiNvbaoi8XZ4iEtB5zF2tWGxXeRom5fOAxX12eDTko4J9zT
        Z8Rapy/GfAAAAIEAuxV0Wyofe7gDE1yJDoMlCI35qww+UiRvSTBD8qxy4pEqHmT5rgEh0WjZWjQU5lUw
        pYI8HoSXNL1hEzkpsfZsZSqH7QsJPqAE39E7WhVdP+e18Ri3REPlsaBJyRxUHrisFmdJFahtTbcTokzC
        YIGT+BJkQVeM0CufWGP6geB4NXk= jbossadm@usplsvulx277.elabs.eds.com
* get provider private key
  + GET rest/v1/keys/providers/{csa-provider-id}
  + Request parameter
      - csa-provider-id     the CSA provider ID
  + Query parameter
      - availability-zone   the HPCS AZ
  + example
      - request
        curl -L -X GET "http://localhost:8080/sp-content/rest/v1/keys/providers/ff808081428eb41d0142fa6cfbb0591f&availability-zone=az-3.region-a.geo-1"
      - response
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><provider-key
        ><uuid>b8864163-c7ed-484c-849f-e32fb5e50455</uuid><csa-provider-id>ff808081428eb
        41d0142fa6cfbb0591f</csa-provider-id><availability-zone>az-3.region-a.geo-1</ava
        ilability-zone><key-file>LS0tLS1CRUdJTiBEU0EgUFJJVkFURSBLRVktLS0tLQpNSUlCdXdJQkF
        BS0JnUURBRXVLVTY2N0FiUVhBOHgvTGVOSHEyRnZYTzY3RHBLMmpvb09ldW9xUHRpbXEzZGo4CndLSWd
        Tb2JEYzNTUFdIdGFHOWNRb21tT0VYQk55aEk1NFp3R3oveGlqOCtUZE4rQkJpbTBGNnNmZmM3Y3VHVFI
        KOWlWN0gzOXNTS1JyTkkvUmIxV01rT1I1RnZZM1Fha3k5SFRvOTFyTFVmL1ZzeWNpUXRCNnpMNmxtUUl
        WQU5TZwpvaFRoaGJFc1BkUytVRm44TWJwTXFBSzNBb0dBYW50MnJWamx1WHhlNlZiUkNmOTM0cEZqeUs
        yd0xabnpxdkFXCjZWRFBlN1pkU2R4bFNQTWpNMXlRenB2cVp6bmVyRDNRY1V2b3VYRXRGbDhSaGhpTjR
        nQjFSR1orUGlmQ096Vm4KTDhtZHFzNG5NckRweFVENGpiMjJxSXZGMmVJaExRZWN4ZHJWaHNWM2thSnV
        YemdNVjlkbmcwNUtPQ2ZjMDJmRQpXcWN2eG53Q2dZRUF1eFYwV3lvZmU3Z0RFMXlKRG9NbENJMzVxd3c
        rVWlSdlNUQkQ4cXh5NHBFcUhtVDVyZ0VoCjBXalpXalFVNWxVd3BZSThIb1NYTkwxaEV6a3BzZlpzWlN
        xSDdRc0pQcUFFMzlFN1doVmRQK2UxOFJpM1JFUGwKc2FCSnlSeFVIcmlzRm1kSkZhaHRUYmNUb2t6Q1l
        JR1QrQkprUVZlTTBDdWZXR1A2Z2VCNE5Ya0NGQUttSHFlOQp6N0RFZ3gxVnpQOHJGcGk3YlU3awotLS0
        tLUVORCBEU0EgUFJJVkFURSBLRVktLS0tLQo=</key-file><create-date>2013-12-18T15:10:17
        .293+08:00</create-date></provider-key></collection>
        *NOTE*: the key file is base64 encoded

### Provider Server

* list provider Servers
  + GET rest/v1/provider-servers
  + Query parameter
      - csa-provider-id     the CSA provider ID
      - is-allocated        whether the server is allocated
  + example
      - list all
        curl -X GET "http://localhost:8080/sp-content/rest/v1/provider-servers.json"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/provider-servers"
      - get server of specific  provider 
        curl -X GET "http://localhost:8080/sp-content/rest/v1/provider-servers.json?csa-provider-id=ff808081428eb41d0142fa6cfbb0591f"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/provider-servers?csa-provider-id=ff808081428eb41d0142fa6cfbb0591f"
      - get server of specific  provider with allocated as true
        curl -X GET "http://localhost:8080/sp-content/rest/v1/provider-servers.json?csa-provider-id=ff808081428eb41d0142fa6cfbb0591f&is-allocated=true"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/provider-servers?csa-provider-id=ff808081428eb41d0142fa6cfbb0591f&is-allocated=true"
* add provider server
  + POST rest/v1/provider-servers
  + Form parameter
      - name               	      the name of the provider server
      - csa-provider-id     	      the CSA provider ID
      - size       		      size of the server
      - provider           	      provider of the server
      - is-allocated           	      whether the server is allocated or not
      - os-type           	      os type of the server
      - ip-address            	      ipaddress
      - public-ip-address             public ip address
      - private-ip-address            private ip address
  + example
      - request
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json"  --form name=server1 --form provider=ecs ip-address=192.168.1.2 --form csa-provider-id=ff808081428eb41d0142fa6cfbb0591f --form is-allocated=false "http://localhost:8080/sp-content/rest/v1/provider-servers"
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json"  --form name=test --form provider=ecs ip-address=192.168.1.2 --form csa-provider-id=ff808081428eb41d0142fa6cfbb0591f --form is-allocated --form csa-provider-id=ff808081428eb41d0142fa6cfbb0591f "http://localhost:8080/sp-content/rest/v1/provider-servers"
      - response
     <?xml version="1.0" encoding="UTF-8" standalone="yes"?><provider-server><uuid>aacc6c4a-452d-4818-8613-a119ba6e0870</uuid><csa-provider-id>ff808081428eb41d0142fa6cfbb0591f</csa-provider-id><size>small</size><provider>ecs</provider><is-allocated>true</is-allocated><os-type>linux</os-type><ip-address>192.168.1.2</ip-address><public-ip-address>16.157.55.122</public-ip-address><private-ip-address>192.168.1.2</private-ip-address><create-date>2014-07-03T17:21:16.837+08:00</create-date></provider-server>
* update provider server
  + PUT rest/v1/provider-servers/{uuid}
  + Request parameter
      - uuid                the UUID of the provider server
  + Form parameter
      - name               	     the name of the provider server
      - csa-provider-id     	     the CSA provider ID
      - size       		     size of the server
      - provider           	     provider of the server
      - is-allocated           	     whether the server is allocated or not
      - os-type           	     os type of the server
      - ip-address            	     ipaddress
      - public-ip-address            public ip address
      - private-ip-address           private ip address
  + example
      - request
        curl -X PUT -H "Content-Type: multipart/form-data;" -H "Accept: application/xml" --form name=server1 --form provider=ecs ip-address=192.168.1.2 --form csa-provider-id=ff808081428eb41d0142fa6cfbb0591f --form is-allocated=false "http://localhost:8080/sp-content/rest/v1/provider-servers/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
      <?xml version="1.0" encoding="UTF-8" standalone="yes"?><provider-server><uuid>aacc6c4a-452d-4818-8613-a119ba6e0870</uuid><csa-provider-id>ff808081428eb41d0142fa6cfbb0591f</csa-provider-id><size>small</size><provider>ecs</provider><is-allocated>true</is-allocated><os-type>linux</os-type><ip-address>192.168.1.2</ip-address><public-ip-address>16.157.55.122</public-ip-address><private-ip-address>192.168.1.2</private-ip-address><create-date>2014-07-03T17:21:16.837+08:00</create-date></provider-server>
* get provider server by uuid
  + GET rest/v1/provider-servers/{uuid}
  + Request parameter
      - uuid                the UUID of the provider server
  + example
      - request
        curl -X GET "http://localhost:8080/sp-content/rest/v1/provider-servers/b8864163-c7ed-484c-849f-e32fb5e50455.json"
      - response
       {"provider-server":{"uuid":"aacc6c4a-452d-4818-8613-a119ba6e0870","csa-provider-id":"ff808081428eb41d0142fa6cfbb0591f","size":"small","provider":"ecs","is-allocated":false,"os-type":"linux","ip-address":"192.168.1.2","public-ip-address":"16.157.55.122","private-ip-address":"192.168.1.2","create-date":"2014-07-03T17:21:16.837+08:00"}}
    or
      - request
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/provider-servers/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
       <?xml version="1.0" encoding="UTF-8" standalone="yes"?><provider-server><uuid>aacc6c4a-452d-4818-8613-a119ba6e0870</uuid><csa-provider-id>ff808081428eb41d0142fa6cfbb0591f</csa-provider-id><size>small</size><provider>ecs</provider><is-allocated>true</is-allocated><os-type>linux</os-type><ip-address>192.168.1.2</ip-address><public-ip-address>16.157.55.122</public-ip-address><private-ip-address>192.168.1.2</private-ip-address><create-date>2014-07-03T17:21:16.837+08:00</create-date></provider-server>
* delete provider server
  + DELETE rest/v1/provider-servers/{uuid}
  + Request parameter
      - uuid                the UUID of the provider server
  + example
    curl -X DELETE "http://localhost:8080/sp-content/rest/v1/provider-servers/85aeae3e-1869-4d81-bd94-a398ff26f49f"

### Context Meta Data

* list Context Meta Data
  + GET rest/v1/context-metadatas
  + Query parameter
      - csa_org_id     the CSA provider ID
  + example
      - list all
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-metadatas.json"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-metadatas"
      - get key of specific  provider
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-metadatas.json?csa_org_id=ff808081428eb41d0142fa6cfbb0591f"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-metadatas?csa_org_id=ff808081428eb41d0142fa6cfbb0591f"
* add Context Meta Data
  + POST rest/v1/context-metadatas
  + Form parameter
      - name               	      the name of the context Meta Data
      - csa_org_id     	      the CSA Org ID
      - level       		      the level of the context meta data in orginization
  + example
      - request
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json"  --form name=orgnization --form level=0 --form csa_org_id=ff808081428eb41d0142fa6cfbb0591f "http://localhost:8080/sp-content/rest/v1/context-metadatas"
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json"  --form name=department --form level=1 --form csa_org_id=ff808081428eb41d0142fa6cfbb0591f "http://localhost:8080/sp-content/rest/v1/context-metadatas"
      - response
     <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_metadata><uuid>46a673e7-146d-4ccf-832f-1b241fdd5768</uuid><level>0</level><name>org1</name><csa_org_id>ff808081428eb41d0142fa6cfbb0591f</csa_org_id><create_date>2014-07-07T22:34:27.848+08:00</create_date></context_metadata></collection>
* update Context Meta Data
  + PUT rest/v1/context-metadatas/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Meta Data
  + Form parameter
 	  - name               	      the name of the context Meta Data
      - csa_org_id     	      the CSA Org ID
      - level       		      the level of the context meta data in orginization
  + example
      - request
        curl -X PUT -H "Content-Type: multipart/form-data;" -H "Accept: application/xml"  --form name=orgnizations --form level=0 --form csa_org_id=ff808081428eb41d0142fa6cfbb0591f "http://localhost:8080/sp-content/rest/v1/context-metadatas/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
      <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_metadata><uuid>46a673e7-146d-4ccf-832f-1b241fdd5768</uuid><level>0</level><name>org1</name><csa_org_id>ff808081428eb41d0142fa6cfbb0591f</csa_org_id><create_date>2014-07-07T22:34:27.848+08:00</create_date></context_metadata></collection>
* get Context Meta Data by uuid
  + GET rest/v1/context-metadatas/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Meta Data
  + example
      - request
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-metadatas/b8864163-c7ed-484c-849f-e32fb5e50455.json"
      - response
      {"context_metadata":{"uuid":"731b5fcc-332c-477a-9cc7-69b463bb8589","level":0,"name":"org1","csa_org_id":"ff808081428eb41d0142fa6cfbb0591f","create_date":"2014-07-07T22:39:10.707+08:00"}}
       or
      - request
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-metadatas/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
       <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_metadata><uuid>46a673e7-146d-4ccf-832f-1b241fdd5768</uuid><level>0</level><name>org1</name><csa_org_id>ff808081428eb41d0142fa6cfbb0591f</csa_org_id><create_date>2014-07-07T22:34:27.848+08:00</create_date></context_metadata></collection>
* delete Context Meta Data
  + DELETE rest/v1/context-metadatas/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Meta Data
  + example
    curl -X DELETE "http://localhost:8080/sp-content/rest/v1/context-metadatas/85aeae3e-1869-4d81-bd94-a398ff26f49f"


### Context Nodes

* list Context Nodes
  + GET rest/v1/context-nodes
  + Query parameter
      - csa_org_id     the CSA provider ID
      - context_metadata_id     context metadata id
      - parent_node_id     parent node id
      - ldap_dns     ldap dns
  + example
      - list all
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-nodes.json"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-nodes"
      - get nodes of specific  orginization id
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-nodes.json?csa_org_id=ff808081428eb41d0142fa6cfbb0591f"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-nodes?csa_org_id=ff808081428eb41d0142fa6cfbb0591f"
        - get nodes of specific  parent id
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-nodes.json?parent_node_id=ff808081428eb41d0142fa6cfbb0591f"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-nodes?parent_node_id=ff808081428eb41d0142fa6cfbb0591f"
          - get nodes of specific  meta data id
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-nodes.json?context_metadata_id=ff808081428eb41d0142fa6cfbb0591f"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-nodes?context_metadata_id=ff808081428eb41d0142fa6cfbb0591f"
          - get nodes of specific  orgnization and ldap dns
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-nodes.json?csa_org_id=ff808081428eb41d0142fa6cfbb0591f&ldap_dns='cn=test,ou=people,o=hp.com'"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-nodes?csa_org_id=ff808081428eb41d0142fa6cfbb0591f&ldap_dns=cn%3Dtest,ou%3Dpeople,o%3Dhp.com"
          - get nodes of specific  parent and ldap dns
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-nodes.json?parent_node_id=ff808081428eb41d0142fa6cfbb0591f&ldap_dns='cn=test,ou=people,o=hp.com'"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-nodes?parent_node_id=ff808081428eb41d0142fa6cfbb0591f&ldap_dns=cn%3Dtest,ou%3Dpeople,o%3Dhp.com"
* add Context Node
  + POST rest/v1/context-nodes
  + Form parameter
      - name               	      the name of the context Meta Data
      - display_name     	      the display name
      - context_metadata_id       		      the context metadata of the node
      - parent_node_id         the parent node of the node
  + example
      - request
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json"  --form name=org1 --form context_metadata_id=ff808081428eggggggggcfbb0591f --form parent_node_id=ff808081428eb41d0142fa6cfbb0591f "http://localhost:8080/sp-content/rest/v1/context-nodes"
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json"  --form name=dep1 --form context_metadata_id=ff8080814ggggggg142fa6cfbb0591f --form parent_node_id=ff808081428eb41d0142fa6cfbb0591f "http://localhost:8080/sp-content/rest/v1/context-nodes"
      - response
     <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_node><uuid>325d7552-886c-4e33-ace7-460645e259a3</uuid><name>org1</name><display_name>org1</display_name><context_metadata_id>ff808081428eb41d0142fa6cfbb0591f</context_metadata_id><parent_node_id>null</parent_node_id><create_date>2014-07-07T22:59:25.937+08:00</create_date></context_node></collection>
* update Context Node
  + PUT rest/v1/context-nodes/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Node
  + Form parameter
      - name               	      the name of the context Node
      - display_name     	      the display name
      - context_metadata_id       		      the context metadata of the node
      - parent_node_id         the parent node of the node
  + example
      - request
        curl -X PUT -H "Content-Type: multipart/form-data;" -H "Accept: application/xml"  --form name=dep1 --form context_metadata_id=ff8080814ggggggg142fa6cfbb0591f --form parent_node_id=ff808081428eb41d0142fa6cfbb0591f  "http://localhost:8080/sp-content/rest/v1/context-nodes/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
     <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_node><uuid>325d7552-886c-4e33-ace7-460645e259a3</uuid><name>org1</name><display_name>org1</display_name><context_metadata_id>ff808081428eb41d0142fa6cfbb0591f</context_metadata_id><parent_node_id>null</parent_node_id><create_date>2014-07-07T22:59:25.937+08:00</create_date></context_node></collection>
* get Context Node by uuid
  + GET rest/v1/context-nodes/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Node
  + example
      - request
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-nodes/b8864163-c7ed-484c-849f-e32fb5e50455.json"
      - response
      {"context_node":{"uuid":"fc2a9f98-0ff3-46e9-a2da-98a624d9c357","name":"org1","display_name":"org1","context_metadata_id":"ff808081428eb41d0142fa6cfbb0591f","parent_node_id":"null","create_date":"2014-07-07T22:58:19.222+08:00"}} or
      - request
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-nodes/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
      <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_node><uuid>325d7552-886c-4e33-ace7-460645e259a3</uuid><name>org1</name><display_name>org1</display_name><context_metadata_id>ff808081428eb41d0142fa6cfbb0591f</context_metadata_id><parent_node_id>null</parent_node_id><create_date>2014-07-07T22:59:25.937+08:00</create_date></context_node></collection>
* delete Context Meta Data
  + DELETE rest/v1/context-nodes/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Node
  + example
    curl -X DELETE "http://localhost:8080/sp-content/rest/v1/context-nodes/85aeae3e-1869-4d81-bd94-a398ff26f49f"


### Context Nodes Provider Mappings

* list context node provider mappings
  + GET rest/v1/context-providers
  + Query parameter
      - context_node_id     the context node id
  + example
      - list all
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-providers.json"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-providers"
      - get providers of context node 
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-providers.json?context_node_id=ff808081428eb41d0142fa6cfbb0591f"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-providers?context_node_id=ff808081428eb41d0142fa6cfbb0591f"
* add context node provider mappings
  + POST rest/v1/context-providers
  + Form parameter
      - context_node_id               	      the context node id for mapping
      - csa_provider_id     	      the provider id for mapping
  + example
      - request
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json"  --form context_node_id=ff808081428eb41d0142fa6cfbb0591n --form csa_provider_id=ff808081428eb41d0142fa6cfbb0591p "http://localhost:8080/sp-content/rest/v1/context-providers"
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json"  --form context_node_id=ff808081428eb41d0142fa6cfbb0591n --form csa_provider_id=ff808081428eb41d0142fa6cfbb0591p "http://localhost:8080/sp-content/rest/v1/context-providers"
      - response
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_provider><uuid>de27f7fa-ce15-4d3f-86e1-42919c12214d</uuid><context_node_id>ff808081428eb41d0142fa6cfbb0591n</context_node_id><csa_provider_id>ff808081428eb41d0142fa6cfbb0591p</csa_provider_id><create_date>2014-07-07T23:08:28.401+08:00</create_date></context_provider></collection>
* update Context Node
  + PUT rest/v1/context-providers/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Node provider mapping
  + Form parameter
      - context_node_id               	      the context node id for mapping
      - csa_provider_id     	      the provider id for mapping
  + example
      - request
        curl -X PUT -H "Content-Type: multipart/form-data;" -H "Accept: application/xml"  --form context_node_id=ff808081428eb41d0142fa6cfbb0591n --form csa_provider_id=ff808081428eb41d0142fa6cfbb0591p "http://localhost:8080/sp-content/rest/v1/context-providers/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
     <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_provider><uuid>de27f7fa-ce15-4d3f-86e1-42919c12214d</uuid><context_node_id>ff808081428eb41d0142fa6cfbb0591n</context_node_id><csa_provider_id>ff808081428eb41d0142fa6cfbb0591p</csa_provider_id><create_date>2014-07-07T23:08:28.401+08:00</create_date></context_provider></collection>
* get Context Node by uuid
  + GET rest/v1/context-providers/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Node provider mapping
  + example
      - request
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-providers/b8864163-c7ed-484c-849f-e32fb5e50455.json"
      - response
      {"context_provider":{"uuid":"de27f7fa-ce15-4d3f-86e1-42919c12214d","context_node_id":"ff808081428eb41d0142fa6cfbb0591n","csa_provider_id":"ff808081428eb41d0142fa6cfbb0591p","create_date":"2014-07-07T23:08:28.401+08:00"}}
       or
      - request
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-providers/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
      <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_provider><uuid>de27f7fa-ce15-4d3f-86e1-42919c12214d</uuid><context_node_id>ff808081428eb41d0142fa6cfbb0591n</context_node_id><csa_provider_id>ff808081428eb41d0142fa6cfbb0591p</csa_provider_id><create_date>2014-07-07T23:08:28.401+08:00</create_date></context_provider></collection>
* delete Context Meta Data
  + DELETE rest/v1/context-providers/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Node
  + example
    curl -X DELETE "http://localhost:8080/sp-content/rest/v1/context-providers/85aeae3e-1869-4d81-bd94-a398ff26f49f"


### Context Nodes Ldap Mappings

* list context node Ldap mappings
  + GET rest/v1/context-ldaps
  + Query parameter
      - context_node_id     the context node id
      - ldap_dn   the ldap dn  for mapping
  + example
      - list all
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-ldaps.json"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-ldaps"
      - get by context node 
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-ldaps.json?context_node_id=ff808081428eb41d0142fa6cfbb0591f"
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-ldaps?context_node_id=ff808081428eb41d0142fa6cfbb0591f"
      - get  context node by ldap dn
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-ldaps.json?ldap_dn=Y249dGVzdDEsb3U9Z3JvdXBzLG89aHAuY29tO2NuPXRlc3QyLG91PWdyb3VwcyxvPWhwLmNvbQ=="
        or:
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-ldaps?ldap_dn=Y249dGVzdDEsb3U9Z3JvdXBzLG89aHAuY29tO2NuPXRlc3QyLG91PWdyb3VwcyxvPWhwLmNvbQ=="
* add context node ldaps mappings
  + POST rest/v1/context-ldaps
  + Form parameter
      - context_node_id               	      the context ldaps id for mapping
      - ldap_dn     	      the ldap dn  for mapping
  + example
      - request
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json"  --form context_node_id=ff808081428eb41d0142fa6cfbb0591n --form ldap_dn=Y249dGVzdDEsb3U9Z3JvdXBzLG89aHAuY29tO2NuPXRlc3QyLG91PWdyb3VwcyxvPWhwLmNvbQ== "http://localhost:8080/sp-content/rest/v1/context-ldaps"
        curl -X POST -H "Content-Type: multipart/form-data;" -H "Accept: application/json"  --form context_node_id=ff808081428eb41d0142fa6cfbb0591n --form ldap_dn=Y249dGVzdDEsb3U9Z3JvdXBzLG89aHAuY29tO2NuPXRlc3QyLG91PWdyb3VwcyxvPWhwLmNvbQ== "http://localhost:8080/sp-content/rest/v1/context-ldaps"
      - response
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_provider><uuid>de27f7fa-ce15-4d3f-86e1-42919c12214d</uuid><context_node_id>cn=test1,ou=groups,o=hp.com;cn=test2,ou=groups,o=hp.com</ldap_dn><csa_provider_id>ff808081428eb41d0142fa6cfbb0591p</ldap_dn><create_date>2014-07-07T23:08:28.401+08:00</create_date></context_provider></collection>
* update Context Node ldaps mappings
  + PUT rest/v1/context-ldaps/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Node provider mapping
  + Form parameter
      - context_node_id               	      the context node id for mapping
      - ldap_dn     	      the ldap dn  for mapping
  + example
      - request
        curl -X PUT -H "Content-Type: multipart/form-data;" -H "Accept: application/xml"  --form context_node_id=ff808081428eb41d0142fa6cfbb0591n --form csa_provider_id=ff808081428eb41d0142fa6cfbb0591p "http://localhost:8080/sp-content/rest/v1/context-ldaps/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
     <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_provider><uuid>de27f7fa-ce15-4d3f-86e1-42919c12214d</uuid><context_node_id>cn=test1,ou=groups,o=hp.com;cn=test2,ou=groups,o=hp.com</ldap_dn><csa_provider_id>ff808081428eb41d0142fa6cfbb0591p</ldap_dn><create_date>2014-07-07T23:08:28.401+08:00</create_date></context_provider></collection>
* get Context Node ldaps mappings by uuid
  + GET rest/v1/context-ldaps/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Node ldap mapping
  + example
      - request
        curl -X GET "http://localhost:8080/sp-content/rest/v1/context-ldaps/b8864163-c7ed-484c-849f-e32fb5e50455.json"
      - response
      {"context_provider":{"uuid":"de27f7fa-ce15-4d3f-86e1-42919c12214d","context_node_id":"ff808081428eb41d0142fa6cfbb0591n","ldap_dn":"cn=test1,ou=groups,o=hp.com;cn=test2,ou=groups,o=hp.com","create_date":"2014-07-07T23:08:28.401+08:00"}}
       or
      - request
        curl -X GET -H "Accept: application/xml" "http://localhost:8080/sp-content/rest/v1/context-ldaps/b8864163-c7ed-484c-849f-e32fb5e50455"
      - response
      <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><context_provider><uuid>de27f7fa-ce15-4d3f-86e1-42919c12214d</uuid><context_node_id>cn=test1,ou=groups,o=hp.com;cn=test2,ou=groups,o=hp.com</ldap_dn><csa_provider_id>ff808081428eb41d0142fa6cfbb0591p</ldap_dn><create_date>2014-07-07T23:08:28.401+08:00</create_date></context_provider></collection>
* delete Context Node ldaps mappings
  + DELETE rest/v1/context-ldaps/{uuid}
  + Request parameter
      - uuid                the UUID of the Context Node
  + example
    curl -X DELETE "http://localhost:8080/sp-content/rest/v1/context-ldaps/85aeae3e-1869-4d81-bd94-a398ff26f49f"
















