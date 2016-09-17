function initData(){
	//for export
	this.orgMapping = [];
    var org = {
       			"id" : sessionStorage.getItem('orgid'),
       			"name" : sessionStorage.getItem('orgname')
       		}
    this.orgMapping.push(org);
    this.getCSAToken();
    //end
    this.getLDAPDN();
	this.getCMetadata();
	this.getOrgProjects();
	//refinement
	this.getProductDrops();
	this.initVersionDrops();
	
}

function getCMetadata(){
	var organization = sessionStorage.getItem('orgid');
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/context-metadatas?csa_org_id=' + organization;
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        // async : false,
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(data) {
       		self.metadatas = [];
       		if(_.isArray(data)){
       			self.metadatas = data;
       		}else if (_.isObject(data)){
       			self.metadatas.push(data);
       		}
       		self.getCNodes(organization);
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Query Error","Failed to get meta data.");
		}
    });	
}

function getCNodes(organization){
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/context-nodes?csa_org_id=' + organization;
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        // async : false,
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(data) {
       		self.cnodes = [];
       		self.businessDomain = [];
       		var forall = {
       			"id" : "",
       			"name" : "All"
       		}
       		self.cnodes.push(forall);
       		if(data.length > 0){
       			var rootnode = self.convertNodesToOneNode(data);
       			self.cnodes.push(rootnode);
       			//resource provider
       			for(var i=0;i<data.length;i++){
       				if(data[i].parent_node_id == null){
       					var rootnode = {
							id : data[i].uuid,
							name : data[i].display_name,
							open : true,
							children : []
						}
						self.businessDomain.push(rootnode);
						break;
       				}
       			}
       		}
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Query Error","Failed to get context nodes.");
		}
    });	
}

function getOrgProjects(){
	var organization = sessionStorage.getItem('orgid');
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/projects?csa_org_id=' + organization;
 	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        // async : false,
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(data) {
       		self.projects = [];
       		var forall = {
       			"uuid" : "",
       			"name" : "All"
       		}
       		self.projects.push(forall);
       		if(_.isArray(data)){
       			self.projects = self.projects.concat(data);
       		}else if (_.isObject(data)){
       			self.projects.push(data);
       		}
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Query Error","Failed to get projects.");
		}
    });	
}

function getCSAToken(){
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/csa/rest/login/' + sessionStorage.getItem("aon") + '/' + sessionStorage.getItem("aun");
	var token = "Basic " + btoa(sessionStorage.getItem("aun") + ":" + sessionStorage.getItem("apw"));
	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        // async : false,
        headers : {
        	"Authorization": token
        },
       	success : function(data) {
       		self.csaUser = data.id;
       		self.getCSAProviders();
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			console.log("fail to get admin authorization");
		}
    });	
}

function getCSAProviders(){
	var self = this;
	var restURL = location.protocol + '//' + location.host +'/csa/rest/artifact?userIdentifier=' +  self.csaUser + '&artifactType=RESOURCE_PROVIDER';
	var token = "Basic " + btoa(sessionStorage.getItem("aun") + ":" + sessionStorage.getItem("apw"));
	$.ajax({
        url: restURL,
        type: 'GET',
        dataType : 'json',
        // async : false,
        headers : {
        	"Authorization": token
        },
       	success : function(data) {
       		self.csaProviders = data.resourceProvider;
       		//for export
       		self.providerMapping = [];
       		_.each(self.csaProviders,function(csaProvider){
       			var provider = {
       				"id" : csaProvider.id,
       				"name" : csaProvider.displayName
       			}
       			self.providerMapping.push(provider);
       		})
       		//end
		},	
		error : function() {
			self.alertMessage("Query Error","Failed to get csa resource providers.");
		}
    });	
}

function getLDAPDN(){
	var organization = sessionStorage.getItem('orgid');
	var user = sessionStorage.getItem('userid');
	var self = this;
	var restURL = location.protocol + '//' + location.host + '/csa/sp/userldap-3.0.jsp?csa_org_id=' + organization + '&userId=' + user;
	$.ajax({
        url: restURL,
        type: 'GET',
        headers : {
        	"X-Auth-Token": sessionStorage.getItem('userid'),
			"X-Auth-User": sessionStorage.getItem('name'),
			"X-Auth-Org": sessionStorage.getItem('orgname')
        },
       	success : function(data) {
       		self.ldapDN = data;
		},	
		error : function(xmlHttpRequest, textStatus, errorThrown) {
			self.alertMessage("Query Error","Failed to get ldap dn.");
		}
    });	
}
	
//refinement	
	function getProductDrops(){
		var self = this;
		var restURL = location.protocol + '//' + location.host +'/sp-content/rest/v1/subscription-server-products/versions';
		self.products = [];
   		var forall = {
   			"uuid" : "",
   			"product_name" : "All"
   		}
   		self.products.push(forall);
   		var item;
   		var productNameList = new Array();
   		$.ajax({
	        url: restURL,
	        type: 'GET',
	        dataType : 'json',
	        headers : {
	        	"X-Auth-Token": sessionStorage.getItem('userid'),
				"X-Auth-User": sessionStorage.getItem('name'),
				"X-Auth-Org": sessionStorage.getItem('orgname')
	        },
	       	success : function(productList) {
	       		for(var i=0;i<productList.length;i++){
	       			if(!contains(productNameList,productList[i].product_name))
	       				productNameList.push(productList[i].product_name);
	       		}
	       		productNameList.sort();
	 			for(var i=0;i<productNameList.length;i++){
		   			item = {
						"uuid" : productNameList[i],
			   			"product_name" : productNameList[i]	
		   			}
	   			self.products.push(item);
	 			}
			},	
			error : function(xmlHttpRequest, textStatus, errorThrown) {
				self.alertMessage("Error","Failed to get product name");
			}
	    });	
	}
	
	function initVersionDrops(){
		var self = this;
		self.versions = [];
   		var forall = {
   			"uuid" : "",
   			"full_version" : "All"
   		}
   		self.versions.push(forall);
	}
	
	function contains(a, obj) {
	    var i = a.length;
	    while (i--) {
	       if (a[i] === obj) {
	           return true;
	       }
	    }
	    return false;
	}

