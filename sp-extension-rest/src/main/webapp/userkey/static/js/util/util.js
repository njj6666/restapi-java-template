//message
function alertMessage(title,message){
	var compiledTemplate = _.template(dialogMessageTemplate,{'title':title,'message':message});
	$("#message-dialog").empty();
	$("#message-dialog").append(compiledTemplate).modal();
}

//confirm dialog
function emptyConfirmModal(){
	$.modal.close();
	$("#message-dialog").empty();
}

function isSpclChar(srcVal) {
	var iChars = "&%<";
	for (var i = 0; i < srcVal.length; i++) {
		if (iChars.indexOf(srcVal.charAt(i)) != -1) return true;
	}
}


function validate_basic(srcId,flagDom,messageDom,isrequired) {
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
		document.getElementById(flagDom).innerHTML = '<img src="images/wrong_img.gif" border="0" alt="Wrong">';
		$("#"+messageDom).html("<span class='nodata-error' style='width:200px'>Cannot begin with a space.</span>");}
	else if (isSpclChar(keyName.value) == true){
		//no special character allowed
		document.getElementById(flagDom).innerHTML = '<img src="images/wrong_img.gif" border="0" alt="Wrong">';
		$("#"+messageDom).html("<span class='nodata-error' style='width:200px'>These special characters are not allowed: %, <, and &.</span>");
		return false;
	   }	
	else if (keyName.value.length == 0 && isrequired != "no")
		{
			document.getElementById(flagDom).innerHTML = '<img src="images/wrong_img.gif" border="0" alt="Wrong">';
			$("#"+messageDom).html("<span class='nodata-error' style='width:200px'>Cannot be blank.</span>");
			return false;
		}
		else if(isrequired != "no")
		{
			//success
			document.getElementById(flagDom).innerHTML = '<img src="images/right_img.gif" border="0" alt="Right">';
			$("#"+messageDom).html("");
			return true;
	    }else{
	    	$("#"+flagDom).empty();
			$("#"+messageDom).html("");
			return true;
	    }
}

function validate_project_name(srcId,flagDom,messageDom,isrequired)   
{  
	var pn = document.getElementById(srcId).value;
	if (pn.length >0 && !/^[a-z][a-z 0-9]*$/.test(pn)){
		//not begin with null
		document.getElementById(flagDom).innerHTML = '<img src="images/wrong_img.gif" border="0" alt="Wrong">';
		$("#"+messageDom).html("<span class='nodata-error' style='width:200px'>Only lowercase characters and numbers are allowed.Must start with character.</span>");
		return false;
	}		
	else if (pn.length == 0 && isrequired != "no")
	{
		document.getElementById(flagDom).innerHTML = '<img src="images/wrong_img.gif" border="0" alt="Wrong">';
		$("#"+messageDom).html("<span class='nodata-error' style='width:200px'>Cannot be blank.</span>");
		return false;
	}
	else if(isrequired != "no")
	{
		//success
		document.getElementById(flagDom).innerHTML = '<img src="images/right_img.gif" border="0" alt="Right">';
		$("#"+messageDom).html("");
		return true;
	}else{
	    	$("#"+flagDom).empty();
			$("#"+messageDom).html("");
			return true;
	    }
}

//sort metadata by level
function sortMetadataByLevel(a,b){
	return a.level - b.level
}

function getNextLevelMetadata(parentNode){
	var metadata = null;
	if(this.metadatas.length > 0){
		this.metadatas.sort(this.sortMetadataByLevel);
		if(parentNode == null){
			metadata = this.metadatas[0];
		}else{
			for(var i=0;i<this.metadatas.length;i++){
				if(this.metadatas[i].uuid == parentNode.context_metadata_id){
					if(i < this.metadatas.length-1){
						metadata = this.metadatas[i+1];
					}
					break;
				}
			}
		}
	}
	return metadata;
}

function getWholeMetadata(node){
	var metadata = null;
	for(var i=0;i<this.metadatas.length;i++){
		if(this.metadatas[i].uuid == node.context_metadata_id){
			metadata = this.metadatas[i];
			break;
		}
	}
	return metadata;
}


function sortNodeByLevel(a,b){
	return a.level - b.level
}

function convertNodesToOneNode(nodes){
	var self = this;
	var onenode;
	for(var i=0;i<nodes.length;i++){
		var node = nodes[i];
		node.level = self.getWholeMetadata(node).level;
	}
	nodes.sort(self.sortNodeByLevel);
	onenode = {
		id : nodes[0].uuid,
		name : nodes[0].display_name,
		open : true
	}
	if(nodes[0].child_node_ids != null){
		onenode.children = [];
		self.pushSubnodes(onenode,nodes[0].child_node_ids,nodes);
	}
	return onenode;
}

function pushSubnodes(target,subids,nodes){
	var self = this;
	for(var i=0;i<nodes.length;i++){
		var node = nodes[i];
		if(_.contains(subids,node.uuid)){
			var treenode = {
				id : node.uuid,
				name : node.display_name,
				open : true
			}
			if(node.child_node_ids != null){
				treenode.children = [];
				self.pushSubnodes(treenode,node.child_node_ids,nodes);
			}
			target.children.push(treenode);
		}
	}
}
