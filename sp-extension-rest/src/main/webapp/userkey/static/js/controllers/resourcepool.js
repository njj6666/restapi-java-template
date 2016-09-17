function showResourcePoolContent(){
	var self = this;
	
	$("#context_view").empty();
	var contentTemplate = _.template(contextViewTemplate);
	$("#context_view").append(contentTemplate);
		
	var icons = {
      header: "ui-icon-circle-arrow-e",
      activeHeader: "ui-icon-circle-arrow-s"
    };
    $("#context_accordion").accordion({
    	icons: icons,
      	heightStyle: "fill"
    });
    
	self.showBusinessDomain();
}

function showBusinessDomain(){
	var self = this;
	//generate tree
	self.generateBusinessDomainTree();
	//get sub nodes and providers
	var selectedNode = self.businessDomainTree.getSelectedNodes()[0];
	self.getAccessibleSubNodes(selectedNode);
	self.getAvailableProviders(selectedNode);
}

function generateBusinessDomainTree(){
	var self = this;
	var setting = {
		view: {
			dblClickExpand: false,
			selectedMulti: false,
			showIcon : false,
			showLine : false
		},
		callback: {
			onClick: function(event, treeId, treeNode) {
				self.getAccessibleSubNodes(treeNode);
				self.getAvailableProviders(treeNode);
			}
		}
	};
	
	self.businessDomainTree = $.fn.zTree.init($("#context_tree"), setting, self.businessDomain);
	self.businessDomainTree.selectNode(self.businessDomainTree.getNodes()[0]);
}

function getAvailableProviders(node){
	//load
	$("#resource_pool_view").empty();
	var contentTemplate = _.template(loadingTemplate);
	$("#resource_pool_view").append(contentTemplate);
	//end
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/context-nodes/providers?context_node_id=' + node.id + '&ldap_dns=' + self.ldapDN;
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(providers) {
       		self.listProviders(providers);
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Query Error","Failed to get available resource pool of " + node.name + ".");
		}
    });	
}

function listProviders(providers){
	var self = this;
	var resourcepools = [];
    _.each(providers,function(provider){
       	var pool = _.find(self.providerMapping, function(mapping){ return mapping.id == provider});
       	if(pool != null){
       		resourcepools.push(pool);
       	}
    });
    
    if(resourcepools.length != providers.length){
    	setTimeout(function(){
    		self.listProviders(providers);
    	},1000); 	
    }else{
       	$("#resource_pool_view").empty();
	   	var contentTemplate = _.template(providerViewTemplate,{"providers":resourcepools});
		$("#resource_pool_view").append(contentTemplate);
				
		var icons = {
			header: "ui-icon-circle-arrow-e",
			activeHeader: "ui-icon-circle-arrow-s"
		};
		$("#provider_accordion").accordion({
			icons: icons,
			heightStyle: "fill",
			active : resourcepools.length-1
		});
		
		self.listAvailableServers(resourcepools);
    }
}

function getAccessibleSubNodes(parent_node){
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/context-nodes?parent_node_id=' + parent_node.id + '&ldap_dns=' + self.ldapDN;
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(nodes) {
       		_.each(nodes,function(node){
       			var tmpsubnode = {
						id : node.uuid,
						name : node.display_name,
						open : true,
						children : []
					}
				if(self.businessDomainTree.getNodesByParam("id", tmpsubnode.id, null).length == 0){
					self.businessDomainTree.addNodes(parent_node,tmpsubnode);
				}
       		});
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Query Error","Failed to get sub nodes of " + node.name + ".");
		}
    });	
}

function listAvailableServers(providers){
	var self = this;
	_.each(providers,function(provider){
		var domEle = $("#servers_of_" + provider.id);
		self.listAvailableServersByProviderID(domEle,provider);
		
		var desEle = $("#des_of_" + provider.id);
		self.getProviderType(provider,domEle,desEle);
	})
}

function listAvailableServersByProviderID(parentDom,provider){
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/provider-servers?csa-provider-id=' + provider.id;
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
       		//pagination of servers
			// var count,page;
			// var currentpage = 1;
			// var max = Math.floor(screen.height * 0.8 / 55);
			
			parentDom.empty();
			if(servers.length > 0){
				// count = servers.length;
				// page = Math.ceil(count/max);
				
				_.each(servers,function(server){
					server.csa_provider_id = server["csa-provider-id"];
					server.csa_provider_name = provider.name;
					server.create_date = new Date(server["create-date"]).toUTCString();
					server.is_allocated = server["is-allocated"];
					server.os_type = server["os-type"];
					server.ip_address = server["ip-address"];
					server.private_ip_address = server["private-ip-address"];
					server.public_ip_address = server["public-ip-address"];
				});
				
				var contentTemplate = _.template(providerServersListTemplate,{"servers": servers});
				parentDom.append(contentTemplate);
			}else{
				parentDom.append(noProviderServersTemplate);
			}
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Query Error","Failed to get available servers of provider '" + provider.name + "'.");
		}
    });	
}

function getProviderType(provider,serverDom,parentDom){
	var self = this;
	var token = "Basic " + btoa(sessionStorage.getItem("aun") + ":" + sessionStorage.getItem("apw"));
	var restURL = location.protocol + '//' + location.host +'/csa/rest/artifact/'+ provider.id +'/resolveProperties?userIdentifier=' +  self.csaUser + '&propertyName=providerType';
	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        headers : {
        	"Authorization": token
        },
       	success : function(data) {
       		var type = data.property[0].values[0].value;
       		// var des = parentDom.html();
       		var des = "( Provider Type : " + type + " )";
       		parentDom.html(des);
       		if(type == "hpcs"){
       			serverDom.find(".nodata").html("The resource provider '" + provider.name + "' is a 'hpcs' provider, the 'hpcs' servers are invisible to consumer.")
       		}
		}
    });	
}