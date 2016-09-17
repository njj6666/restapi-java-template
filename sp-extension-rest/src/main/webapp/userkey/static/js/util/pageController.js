jQuery.i18n.properties({
	name:['messages','error'],
	path:'static/js/l10n/',	  		
	mode:'map',
	language:'en'	  		
}); 

$(document).ready(function(){
	var parms = location.search;
	var nameParm = parms.split("&")[0];
	var emailParm = parms.split("&")[1];
	var userIDParm = parms.split("&")[2];
	var orgNameParm = parms.split("&")[3];
	var orgIDParm = parms.split("&")[4];
	var aonParm = parms.split("&")[5];
	var aunParm = parms.split("&")[6];
	var apwParm = parms.split("&")[7];
	var name = atob(nameParm.substring(nameParm.indexOf("=")+1));
	var email = atob(emailParm.substring(emailParm.indexOf("=")+1));
	var userid = atob(userIDParm.substring(userIDParm.indexOf("=")+1));
	var orgname = atob(orgNameParm.substring(orgNameParm.indexOf("=")+1));
	var orgid = atob(orgIDParm.substring(orgIDParm.indexOf("=")+1));
	var aon = atob(aonParm.substring(aonParm.indexOf("=")+1));
	var aun = atob(aunParm.substring(aunParm.indexOf("=")+1));
	var apw = atob(apwParm.substring(apwParm.indexOf("=")+1));
	sessionStorage.setItem("name",name);
	sessionStorage.setItem("emailAddress",email);
	sessionStorage.setItem("userid",userid);
	sessionStorage.setItem("orgname",orgname);
	sessionStorage.setItem("orgid",orgid);
	sessionStorage.setItem("aon",aon);
	sessionStorage.setItem("aun",aun);
	sessionStorage.setItem("apw",apw);
	$("#logosection .profile").html('<a href="javascript:void(0)">'+sessionStorage.getItem("name")+'</a>');
	
	initData();
	showProfilePage();
});
	
//profile		
function showProfilePage(){
	var parentDom = $("#profileTab");
	parentDom.empty();
	var compiledTemplate = _.template(profileMainPageTemplate);
	parentDom.append(compiledTemplate);
	
	$("#profileLink").addClass("highlight");
	$("#subscriptionLink").removeClass("highlight");
	$("#resourcePoolLink").removeClass("highlight");
	$("#profileTab").show();
	$("#subscriptionTab").hide();
	$("#resourcePoolTab").hide();
	
	this.getAllUserKeys();
}

//subscription
function showSubscriptionPage(){
	var self = this;
	var parentDom = $("#subscriptionTab");
	
	var compiledTemplate = _.template(subscriptionPageTemplate);	
	parentDom.empty();
	parentDom.append(compiledTemplate);
	
	var icons = {
      header: "ui-icon-circle-arrow-e",
      activeHeader: "ui-icon-circle-arrow-s"
    };
    $("#filter_accordion").accordion({
    	icons: icons,
      	heightStyle: "content"
    });
	$( "#status" ).selectmenu();
	
	$( "#start_from" ).datepicker({
      changeMonth: true,
      dateFormat: "yy-mm-dd",
      onClose: function( selectedDate ) {
        $( "#start_to" ).datepicker( "option", "minDate", selectedDate );
      }
    });
    $( "#start_to" ).datepicker({
      changeMonth: true,
      dateFormat: "yy-mm-dd",
      onClose: function( selectedDate ) {
        $( "#start_from" ).datepicker( "option", "maxDate", selectedDate );
      }
    });	
	
	$( "#terminate_from" ).datepicker({
      changeMonth: true,
      dateFormat: "yy-mm-dd",
      onClose: function( selectedDate ) {
        $( "#terminate_to" ).datepicker( "option", "minDate", selectedDate );
      }
    });
    $( "#terminate_to" ).datepicker({
      changeMonth: true,
      dateFormat: "yy-mm-dd",
      onClose: function( selectedDate ) {
        $( "#terminate_from" ).datepicker( "option", "maxDate", selectedDate );
      }
    });	
    
	// self.initOtherData();
	self.showFilterPage();
	self.showEastContentPage();
    self.getDataForEastContentPage();
    
    $("#profileLink").removeClass("highlight");
	$("#subscriptionLink").addClass("highlight");
	$("#resourcePoolLink").removeClass("highlight");
	$("#profileTab").hide();
	$("#subscriptionTab").show();
	$("#resourcePoolTab").hide();
}

//resource pool		
function showResourcePoolPage(){
	var self = this;
	var parentDom = $("#resourcePoolTab");
	parentDom.empty();
	var compiledTemplate = _.template(resourcePoolPageTemplate);
	parentDom.append(compiledTemplate);
	
	if(self.businessDomain.length == 0){
		self.alertMessage("No Data","There's not business domain.");
	}else{
		setTimeout(function(){
    		self.showResourcePoolContent();
    	},500); 
	}
			
	$("#profileLink").removeClass("highlight");
	$("#subscriptionLink").removeClass("highlight");
	$("#resourcePoolLink").addClass("highlight");
	$("#profileTab").hide();
	$("#subscriptionTab").hide();
	$("#resourcePoolTab").show();
}

function showEastContentPage(){
	var parentDom = $("#cs-layout-management-main-content");
	parentDom.empty();
	var compiledTemplate = _.template(subscriptionListTemplate);
	parentDom.append(compiledTemplate);	
}

function getDataForEastContentPage(){
	var self = this;
	var filter = {
		'owner_id' : sessionStorage.getItem("emailAddress"),
		'organization_id' : sessionStorage.getItem("orgid"),
		'context_node_id' : "",
		'project' : "",
		'status' : "",
		'start_from' : "",
		'start_to' : "",
		'terminate_from' : "",
		'terminate_to' : "",
		'product_filter' : "",
		'version_filter' : ""
	}
	self.getSubscriptions(filter);	
}