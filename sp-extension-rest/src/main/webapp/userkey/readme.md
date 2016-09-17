SP extension portal

#Codes of mashup widget for sp extension portal
#Copy codes below to "Content" of Mashup Widget

<div style="background-color:#1897D3;background-image: url('');
     color: #FFFFFF;font-family: HPSimplified;
     font-size: 18px;background-repeat: no-repeat;
     padding-left: 20px;padding-top: 20px;padding-right: 20px;padding-bottom: 20px;line-height: .7; cursor:pointer" onclick="popupUserKeyList();">
<h3>SP Extension</h3>
<hr style="color: #FFF;"></hr>
<h5>Standalone portal, for user key management, etc.</h5>
</div>

<script type="text/javascript">
    function popupUserKeyList() {
    	var user = JSON.parse(sessionStorage.getItem("mppApp_user")); 
    	var org = JSON.parse(sessionStorage.getItem("mppApp_org"));
    	var userid = user.userId;
    	var orgname = org.name;
    	var orgid = org.id;
        var username = user.name;
        var email = user.emailAddress;
        
        //for admin authentication
        var adminON = "CSA-Provider";
        var adminUN = "admin";
        var adminPW = "cloud";
        //end
        
        if(email == undefined){
        	var href = '';
         	if(location.host.indexOf(":")>=0){
            	href =  location.protocol + '//' + location.hostname + ':8089/logout?sort=ascend&layout=list&approval=ALL&category=ALL&expired';
         	}else{
               	href = location.protocol + '//' + location.host + '/logout?sort=ascend&layout=list&approval=ALL&category=ALL&expired';
         	}

        	location.href = href;
        }else{
        	var href = '';
         	if(location.host.indexOf(":")>=0){
            	href =  location.protocol + '//' + location.hostname + ':8444/sp-content/userkey/index.html?un='
               	+ btoa(username) + '&e=' + btoa(email) + '&ui=' + btoa(userid) + '&on=' + btoa(orgname) + '&oi=' + btoa(orgid)
               	+ '&aon=' + btoa(adminON) + '&aun=' + btoa(adminUN) + '&apw=' + btoa(adminPW);
         	}else{
               	href = location.protocol + '//' + location.host + '/sp-content/userkey/index.html?un='
               	+ btoa(username) + '&e=' + btoa(email) + '&ui=' + btoa(userid) + '&on=' + btoa(orgname) + '&oi=' + btoa(orgid)
               	+ '&aon=' + btoa(adminON) + '&aun=' + btoa(adminUN) + '&apw=' + btoa(adminPW);
         	}

        	location.href = href;
        }
    }
</script>