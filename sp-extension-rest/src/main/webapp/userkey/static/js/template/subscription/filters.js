var cnode_filter_template = '<label><b>Context Node</b></label><br><br> '+
							'<select name="context_node_id" id="context_node_id" style="width:200px">'+
								'<%for(var i=0;i<cnodes.length;i++){%>'+
								'<option value="<%=cnodes[i].id%>"><%=cnodes[i].name%></option>'+
								'<%}%>'+
							'</select>';
							
var project_filter_template = '<label><b>Project Name</b></label><br><br> '+
							  '<select name="project" id="project" style="width:200px">'+
							  	'<%for(var i=0;i<projects.length;i++){%>'+
								'<option value="<%=projects[i].uuid%>"><%=projects[i].name%></option>'+
								'<%}%>'+
							  '</select>';
//refinement
var product_filter_template = '<label><b>Product Name</b></label><br><br> '+
'<select name="product_filter" id="product_filter" style="width:200px">'+
	'<%for(var i=0;i<products.length;i++){%>'+
	'<option value="<%=products[i].uuid%>"><%=products[i].product_name%></option>'+
	'<%}%>'+
'</select>';

var version_filter_template = '<label><b>Version</b></label><br><br> '+
'<select name="version_filter" id="version_filter" style="width:200px">'+
	'<%for(var i=0;i<versions.length;i++){%>'+
	'<option value="<%=versions[i].uuid%>"><%=versions[i].full_version%></option>'+
	'<%}%>'+
'</select>';
								
var cnode_tree_template = '<ul id="tree" class="ztree ui-menu ui-widget ui-widget-content ui-corner-bottom" style="width:198px;overflow: scroll;"></ul>';