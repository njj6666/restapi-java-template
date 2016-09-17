function showFilterPage(){
	var self = this;
	var nodeDom = $("#cnode_view");
	var projectDom = $("#oproject_view");
	var productDom = $("#products_view");
	var versionDom = $("#versions_view");
	
	//context nodes
	var compiledTemplate1 = _.template(cnode_filter_template,{cnodes:self.cnodes});
 	nodeDom.empty();	
 	nodeDom.append(compiledTemplate1);
 	$( "#context_node_id" ).selectmenu({
		create : function(){
			var parentDom = $("#context_node_id-menu").parent();
			parentDom.empty();
			var compiledTemplate = _.template(cnode_tree_template);
			parentDom.append(compiledTemplate);
			var setting = {
				view: {
					selectedMulti: false,
					showIcon : false,
					showLine : false
				},
				callback: {
					onClick: function(event, treeId, treeNode) {
						$( "#context_node_id option" ).val(treeNode.id);
						$( "#context_node_id option" ).html(treeNode.name);
						$( "#context_node_id-button .ui-selectmenu-text").empty();
						$( "#context_node_id-button .ui-selectmenu-text").append(treeNode.name);
						$( "#context_node_id" ).selectmenu( "close" );
					}
				}
			};
			
			var zTreeObj = $.fn.zTree.init($("#tree"), setting, self.cnodes);
		}
	});
	 
	//project      	 
    var compiledTemplate2 = _.template(project_filter_template,{projects:self.projects});
 	projectDom.empty();	
 	projectDom.append(compiledTemplate2);
 	$( "#project" ).selectmenu();
 	
	//products drop down      	 
    var compiledTemplate3 = _.template(product_filter_template,{products:self.products});
 	productDom.empty();	
 	productDom.append(compiledTemplate3);
 	$( "#product_filter" ).selectmenu({
 		change: function(event, ui){
 			getVersionByProduct($( "#product_filter" ).val());
 		}
 	});
 	
 	//versions drop down      	 
    var compiledTemplate4 = _.template(version_filter_template,{versions:self.versions});
 	versionDom.empty();	
 	versionDom.append(compiledTemplate4);
 	$( "#version_filter" ).selectmenu();
       	
}

//pagination
var count,page;
var currentpage = 1;
var max = Math.floor(screen.height * 0.6 / 55);
var marker = (currentpage - 1) * max;

function doFilter(){
	var owner_id = sessionStorage.getItem("emailAddress");
	var organization_id = sessionStorage.getItem("orgid");
	var context_node_id = $("#context_node_id").val();
	var project = $("#project").val();
	var status = $("#status").val();
	var start_from = $("#start_from").val();
	var start_to = $("#start_to").val();
	var terminate_from = $("#terminate_from").val();
	var terminate_to = $("#terminate_to").val();
	var product_filter = $("#product_filter").val();
	var version_filter = $("#version_filter").val();
	
	if(start_from.length>0){
		start_from = start_from + "%2000:00:00";
	}
	if(start_to.length>0){
		start_to = start_to + "%2023:59:59";
	}
	if(terminate_from.length>0){
		terminate_from = terminate_from + "%2000:00:00";
	}
	if(terminate_to.length>0){
		terminate_to = terminate_to + "%2023:59:59";
	}
	var filter = {
		'owner_id' : owner_id,
		'organization_id' : organization_id,
		'context_node_id' : context_node_id,
		'project' : project,
		'status' : status,
		'start_from' : start_from,
		'start_to' : start_to,
		'terminate_from' : terminate_from,
		'terminate_to' : terminate_to,
		'product_filter' : product_filter,
		'version_filter' : version_filter
	}
	
	currentpage = 1;
	marker = (currentpage - 1) * max;
	
	this.getSubscriptions(filter);
}

function getSubscriptions(filter){
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/subscriptions?max=' + max + '&marker=' + marker + '&query_type=mix';
	restURL += '&owner_id=' + filter.owner_id + '&organization_id=' + filter.organization_id + '&context_node_id=' + filter.context_node_id + '&project=' + filter.project;
	restURL += '&status=' + filter.status + '&start_from=' + filter.start_from + '&start_to=' + filter.start_to;
	restURL += '&terminate_from=' + filter.terminate_from + '&terminate_to=' + filter.terminate_to;
	restURL += '&product_filter=' + filter.product_filter + '&version_filter=' + filter.version_filter;
	var self = this;
	//for export
	self.currentFilter = filter;
	//end
	
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
       		self.subscriptions = [];
       		self.subscriptions = data.subscription;
       		count = data.size;
       		page = Math.ceil(count/max);
       		
			$("#subscriptionsListDiv").empty();
			if(self.subscriptions.length > 0){
				_.each(self.subscriptions,function(subscription){
					subscription.statusIcon = self.getStatusIcon(subscription.status);
					subscription.csa_org_name = sessionStorage.getItem('orgname');
					
					if(subscription.start_time != undefined){
						subscription.start_time = new Date(subscription.start_time).toUTCString();
					}else{
						subscription.start_time = "N/A";
					}
					
					if(subscription.terminate_time != undefined){
						subscription.terminate_time = new Date(subscription.terminate_time).toUTCString();
					}else{
						subscription.terminate_time = "N/A";
					}
					
					subscription.project_name = self.projectID2Name(subscription.project);
				});
				var contentTemplate = _.template(subscriptionListItemTemplate,{'subscriptions':self.subscriptions,'currentpage':currentpage,'page':page});
				$("#subscriptionsListDiv").append(contentTemplate);
				
				$('.pagination').jqPagination({
				    paged: function(page) {
				    	currentpage = page;
				    	marker = (currentpage - 1) * max;
				    	self.getSubscriptions(filter);
				    }
				});
			}else{
				$("#subscriptionsListDiv").append(noSubscriptionsTemplate);
			}
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Query Error","Failed to get subscriptions.");
		}
    });	
}

function projectID2Name(project_id){
	var self = this;
	var result = "N/A";
	for(var j=0;j<self.projects.length;j++){
		if(projects[j].uuid == project_id){
			result = projects[j].name;
			break;
		}
	}
	return result;
}

function getStatusIcon(status){
	if(status.toLowerCase() == "active"){
		return "static/images/service_status-online.png";
	}else if(status.toLowerCase() == "terminated"){
		return "static/images/service_status-canceled.png";
	}else if(status.toLowerCase() == "cancelled"){
		return "static/images/service_status-failed.png";
	}else{
		return "static/images/service_status-in_progress.png";
	}
}

function getServers(event){
	var self = this;
	var uuid = $(event.currentTarget).parent().parent().data("uuid");
	var name = $(event.currentTarget).parent().parent().data("name");
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/subscription-servers?subscription_id=' + uuid;
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(servers) {	
       		var newList = new Array();
       		var productList=new Array();
       		_.each(servers,function(server){
       			productList = self.getProducts(server.uuid);
       			for(var i=0;i<productList.length;i++){
       				server.products=productList[i].product_name;
       				server.productid=productList[i].uuid;
       				if(productList[i].product_version == undefined || productList[i].product_version == ""){
       					server.version="unknown"
       				}else{
       					server.version=productList[i].product_version;
       				}
       				
					var temp = new Object();
					for(var p in server){
						var name = p;
						temp[name] = server[p];
					}
					newList.push(temp);
       			}
       		});
       		servers=newList;
       		if(servers.length>0){
       			var compiledTemplate = _.template(serverListTemplate,{'servers':servers});
				var dialogOption = $.extend({}, {
					title : "Servers Of Subscription '" + name + "'",
					draggable : true,
					width : 'auto',
					height: 'auto',
					modal : true,
					buttons : null,
					beforeclose : function(event, ui) {
						// reset the content.
						$(this).empty();
					}
			
				});
				$("#sp-dialog").empty();
				$("#sp-dialog").append(compiledTemplate).dialog(dialogOption);
       		}else{
       			self.alertMessage("No Servers","There's not any servers found.");
       		}	
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to get servers.");
		}
    });	
}

function getProducts(server_id){
	var result = "";
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/subscription-server-products?server_id=' + server_id;
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        async : false,
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(products) {	
       		result = products;
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to get products.");
		}
    });	
    return result;
}

function getProperties(event){
	var self = this;
	var uuid = $(event.currentTarget).parent().parent().data("uuid");
	var name = $(event.currentTarget).parent().parent().data("name");
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/subscription-properties?subscription_id=' + uuid;
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(properties) {	
       		if(properties.length>0){
       			var compiledTemplate = _.template(propertyListTemplate,{'properties':properties});
				var dialogOption = $.extend({}, {
					title : "Properties Of '" + name + "'",
					draggable : true,
					width : 'auto',
					height: 'auto',
					modal : true,
					buttons : null,
					beforeclose : function(event, ui) {
						// reset the content.
						$(this).empty();
					}
			
				});
				$("#sp-dialog").empty();
				$("#sp-dialog").append(compiledTemplate).dialog(dialogOption);
       		}else{
       			self.alertMessage("No Properties","There's not any properties found.");
       		}	
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to get properties.");
		}
    });	
}

function getLogs(event){
	var self = this;
	var uuid = $(event.currentTarget).parent().parent().data("uuid");
	var name = $(event.currentTarget).parent().parent().data("name");
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/subscription-logs?subscription_id=' + uuid;
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(logs) {	
       		_.each(logs,function(log){
       			if(log.log_time != undefined){
					log.log_time = new Date(log.log_time).toUTCString();
				}else{
					log.log_time = "N/A";
				}
       		});
       		if(logs.length>0){
       			var compiledTemplate = _.template(logListTemplate,{'logs':logs});
				var dialogOption = $.extend({}, {
					title : "Logs Of Subscription '" + name + "'",
					draggable : true,
					width : 'auto',
					height: 'auto',
					modal : true,
					buttons : null,
					beforeclose : function(event, ui) {
						// reset the content.
						$(this).empty();
					}
			
				});
				$("#sp-dialog").empty();
				$("#sp-dialog").append(compiledTemplate).dialog(dialogOption);
       		}else{
       			self.alertMessage("No Logs","There's not any logs found.");
       		}	
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to get logs.");
		}
    });	
}

function exportLogs(event){
	var self = this;
	var uuid = $(event.currentTarget).parent().parent().data("uuid");
	var name = $(event.currentTarget).parent().parent().data("name");
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/subscription-logs/export';
	var fileName = "logs_of_subscription_"+name+".xml";
	restURL += "/" + encodeURIComponent(fileName);
	restURL += '?subscription_id=' + uuid;
 	$.ajax({
        url: restURL,
        type: 'GET',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(logs) {
       		if(logs.indexOf("log_level") > 0){
       			var blob = new Blob([logs], {type: "application/xml"});
				self.SaveToDisk(blob,"logs_of_subscription_"+name+".xls");
			}else{
       			self.alertMessage("No Logs","There's not any logs found.");
       		}
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to export subscription logs.");
		}
    });	
}

function exportToXLS(){
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/subscriptions/export';
	var fileName = "subscriptions_report.xml";
	restURL += "/" + encodeURIComponent(fileName);
	restURL += '?max=0&marker=0';
	restURL += '&owner_id=' + self.currentFilter.owner_id + '&organization_id=' + self.currentFilter.organization_id 
			+ '&context_node_id=' + self.currentFilter.context_node_id + '&project=' + self.currentFilter.project;
	restURL += '&status=' + self.currentFilter.status + '&start_from=' + self.currentFilter.start_from + '&start_to=' + self.currentFilter.start_to
			+ '&terminate_from=' + self.currentFilter.terminate_from + '&terminate_to=' + self.currentFilter.terminate_to;
	restURL += '&product_filter=' + self.currentFilter.product_filter + '&version_filter=' + self.currentFilter.version_filter;

 	$.ajax({
        url: restURL,
        type: 'GET',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname'),
        	"orgMapping" : JSON.stringify(self.orgMapping),
        	"providerMapping" : JSON.stringify(self.providerMapping)
        },
       	success : function(data) {
       		var blob = new Blob([data], {type: "application/xml"});
			self.SaveToDisk(blob,"subscription_report.xls");
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Error","Failed to export subscriptions report.");
		}
	});
}

function SaveToDisk(blobURL, fileName) {
    var reader = new FileReader();
    reader.readAsDataURL(blobURL);
    reader.onload = function (event) {
        var save = document.createElement('a');
        save.href = event.target.result;
        save.target = '_blank';
        save.download = fileName || 'unknown file';

        var event = document.createEvent('Event');
        event.initEvent('click', true, true);
        save.dispatchEvent(event);
        (window.URL || window.webkitURL).revokeObjectURL(save.href);
    };
}

function updateVersion(event){
	var self = this;
	var uuid = $(event.currentTarget).parent().parent().data("uuid");
	var version = $(event.currentTarget).parent().parent().data("version");
	var currentVersion = document.getElementById(uuid).innerHTML;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/subscription-server-products/'+uuid+'/'+currentVersion;
	
	if(version == currentVersion){
		self.alertMessage("Info","You did not update the version.");
	}else {
		$.ajax({
	        url: restURL,
	        type: 'PUT',
	        dataType : 'json',
	        headers : {
	        	"X-Auth-Token": sessionStorage.getItem('userid'),
				"X-Auth-User": sessionStorage.getItem('name'),
				"X-Auth-Org": sessionStorage.getItem('orgname')
	        },
	       	success : function(servers) {	
	       		self.alertMessage("Info","Version is updated.");
			},	
			error : function(xmlHttpRequest, textStatus, errorThrown) {
				self.alertMessage("Error","Failed to update product.");
			}
	    });	
	}
 	
}

function getVersionByProduct(product){
	var self = this;
	var productName = product;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/subscription-server-products/versions/'+product;
	
		$.ajax({
	        url: restURL,
	        type: 'GET',
	        dataType : 'json',
	        headers : {
	        	"X-Auth-Token": sessionStorage.getItem('userid'),
				"X-Auth-User": sessionStorage.getItem('name'),
				"X-Auth-Org": sessionStorage.getItem('orgname')
	        },
	       	success : function(versions) {	
	       		var version_filter = $("#version_filter");
	 			version_filter.empty();
	 			var option = $("<option>").text("All").val("")
	 		    version_filter.append(option);
	 			for(var i=0;i<versions.length;i++){
	 				option = $("<option>").text(versions[i].full_version).val(versions[i].full_version);
		 		    version_filter.append(option);
	 			}
	 			version_filter.selectmenu("refresh");
			},	
			error : function(xmlHttpRequest, textStatus, errorThrown) {
				self.alertMessage("Error","Failed to get version of "+product+".");
			}
	    });	
 	
}