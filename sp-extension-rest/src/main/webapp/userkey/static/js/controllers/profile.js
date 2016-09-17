//query user key list
function getAllUserKeys(){
	var parentDom = $("#centersection_big .section-big-lfnav");
	parentDom.empty();
	var compiledTemplate = _.template(userKeyListTemplate);
	parentDom.append(compiledTemplate);	
	
	this.queryUserKeys();
}

function queryUserKeys(){
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/user-keys?email='+sessionStorage.getItem('emailAddress')
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(data) {
       		self.userkeys = [];
       		if(_.isArray(data)){
       			self.userkeys = data;
       		}else if (_.isObject(data)){
       			self.userkeys.push(data);
       		}
			$("#userKeysListDiv").empty();
			if(self.userkeys.length > 0){
				_.each(self.userkeys,function(userkey){
					userkey.create_date = userkey["create-date"];
					var contentTemplate = _.template(userKeyListItemTemplate,userkey);
					$("#userKeysListDiv").append(contentTemplate);
				});
			}else{
				$("#userKeysListDiv").append(noDataTemplate);
			}
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to get user keys.");
		}
    });	
}

//open new key dialog
function openNewUserKeyDialog(){
	var self = this;
	var buttons = {};
	buttons["Create"] = function(){
		self.postToCreateKey();
	};
	buttons["Cancel"] = function() {
		$(this).dialog('close');
	}
	var compiledTemplate = _.template(newUserKeyTemplate,{'emailAddress':sessionStorage.getItem('emailAddress')});
	var dialogOption = $.extend({}, {
		title : "Create User Key",
		draggable : true,
		buttons : buttons,
		width : 800,
		modal : true,
		beforeclose : function(event, ui) {
			// reset the content.
			$(this).empty();
		}

	});
	$("#sp-dialog").empty();
	$("#sp-dialog").append(compiledTemplate).dialog(dialogOption);	
}

// Validation - user key Name
function validateKeyName(srcId) {
	var keyName = document.getElementById(srcId);
	var spaceFlag=0; 
	if(keyName!=null){
	    var strText = keyName.value; 
	    if (strText!="")       
	    {       
	    	var strArr = new Array();
	    	strArr = strText.split("");
	        if(strArr[0]==" ") 
	        	spaceFlag=1;
	    }
	}
	if ( keyName != null && keyName.value != null &&  spaceFlag==1 ){
		//not begin with null
		document.getElementById('keynamevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		$("#keyNameValName").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.userKeyNameSpaceValid')+"</span>");}
	else if (isSpclChar(keyName.value) == true){
		//no special character allowed
		document.getElementById('keynamevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		$("#keyNameValName").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionNameSpecialCharater')+"</span>");
		return false;
	   }	
	else if (keyName.value.length == 0 )
		{
			document.getElementById('keynamevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
			$("#keyNameValName").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.userKeyNameValid')+"</span>");
			return false;
		}
		else
		{
			//subName success
			document.getElementById('keynamevalidate').innerHTML = '<img src="static/images/right_img.gif" border="0" alt="Right">';
			$("#keyNameValName").html("");
			return true;
	    }
}

// Validation - ldap email
function emailAddressCheck(value) {
	var expression = /\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
	var reg = new RegExp(expression);
	return reg.test(value);
}
		
function validateLdapEmail(srcId) {
	var email = document.getElementById(srcId);
	var spaceFlag=0; 
	if(email!=null){
	    var strText = email.value; 
	    if (strText!="")       
	    {       
	    	var strArr = new Array();
	    	strArr = strText.split("");
	        if(strArr[0]==" ") 
	        	spaceFlag=1;
	    }
	}
	if ( email != null && email.value != null &&  spaceFlag==1 ){
		//not begin with null
		document.getElementById('ldapemailvalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		$("#ldapEmailValName").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.ldapEmailSpaceValid')+"</span>");
	}else if (email.value.length == 0 ){
		document.getElementById('ldapemailvalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		$("#ldapEmailValName").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.ldapEmailValid')+"</span>");
		return false;
	}else if (emailAddressCheck(email.value) == false){
		//no special character allowed
		document.getElementById('ldapemailvalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		$("#ldapEmailValName").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.ldapEmailSyntaxValid')+"</span>");
		return false;
	}else{
			//subName success
			document.getElementById('ldapemailvalidate').innerHTML = '<img src="static/images/right_img.gif" border="0" alt="Right">';
			$("#ldapEmailValName").html("");
			return true;
	}
}

function isSpclChar(srcVal) {
	var iChars = "&%<";
	for (var i = 0; i < srcVal.length; i++) {
		if (iChars.indexOf(srcVal.charAt(i)) != -1) return true;
	}
}

//post request to create key
function postToCreateKey(){
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/user-keys';
	var formData = new FormData($("form[name=newUserKeyForm]")[0]);
    $.ajax({
        url: restURL,
        type: 'POST',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function() {
			$("#sp-dialog").dialog('close');
			self.queryUserKeys();
		},
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to post new user key.");
			$("#sp-dialog").dialog('close');
		},
        // Form data
        data: formData,
        //Options to tell jQuery not to process data or worry about content-type.
        cache: false,
        contentType: false,
        processData: false
    });
}

function confirmDeleteUserKey(event){
	var uuid = $(event.currentTarget).parent().parent().data("uuid");
	var compiledTemplate = _.template(deleteConfirmTemplate,{uuid:uuid});
	$("#message-dialog").empty();
	$("#message-dialog").append(compiledTemplate).modal();	
}

function deleteUserKey(event){
	var uuid = $(event.currentTarget).data("uuid");
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/user-keys';
    $.ajax({
        url: restURL + "/" + uuid,
        type: 'DELETE',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function() {
			self.queryUserKeys();
		},
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to delete user key.");
		}
    });
    self.emptyConfirmModal();
}

function viewDetails(event){
	var self = this;
	
	var uuid = $(event.currentTarget).parent().parent().data("uuid");
	var name = $(event.currentTarget).parent().parent().data("keyname");
	var email = $(event.currentTarget).parent().parent().data("email");
	
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/user-keys/' + uuid;
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(data) {	
       		var userkey = {
       			'uuid' : data.uuid,
       			'name' : data.name,
       			'email' : data.email,
       			'keycontent' : atob(data["public-key"])
       		}	
			var buttons = {};
			buttons["Save"] = function(){
				self.changeUserKey(uuid);
			};
			buttons["Cancel"] = function() {
				$(this).dialog('close');
			}
			var compiledTemplate = _.template(userKeyDetailTemplate,userkey);
			var dialogOption = $.extend({}, {
				title : "User Key Details",
				draggable : true,
				buttons : buttons,
				width : 800,
				modal : true,
				beforeclose : function(event, ui) {
					// reset the content.
					$(this).empty();
				}
		
			});
			$("#sp-dialog").empty();
			$("#sp-dialog").append(compiledTemplate).dialog(dialogOption);
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to get user key detail.");
		}
    });	
}

function changeUserKey(uuid){
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/user-keys/'+uuid;
	var formData = new FormData($("form[name=userKeyForm]")[0]);
    $.ajax({
        url: restURL,
        type: 'PUT',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function() {
			$("#sp-dialog").dialog('close');
			self.queryUserKeys();
		},
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to change user key.");
			$("#sp-dialog").dialog('close');
		},
        // Form data
        data: formData,
        //Options to tell jQuery not to process data or worry about content-type.
        cache: false,
        contentType: false,
        processData: false
    });
}

function filterUserKey(event){
	var filterStr = $(event.currentTarget).val().trim();
	if(filterStr.length > 0){
		if(this.userkeys.length > 0){
			$("#userKeysListDiv").empty();
			var findResult = false;
			_.each(this.userkeys,function(userkey){
				if(userkey.name.toLowerCase().indexOf(filterStr.toLowerCase())>=0){
					var contentTemplate = _.template(userKeyListItemTemplate,userkey);
					$("#userKeysListDiv").append(contentTemplate);
					findResult = true;
				}
			});
			if(!findResult){
				$("#userKeysListDiv").append(noDataTemplate);
			}
		}
	}else{
		$("#userKeysListDiv").empty();
		_.each(this.userkeys,function(userkey){
			var contentTemplate = _.template(userKeyListItemTemplate,userkey);
			$("#userKeysListDiv").append(contentTemplate);
		});
	}
}
