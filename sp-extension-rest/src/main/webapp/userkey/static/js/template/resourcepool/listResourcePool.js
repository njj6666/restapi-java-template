var resourcePoolPageTemplate = '<div style="height:100%">'+
									'<div id="context_view" style="float:left;height:80%; width:20%;margin-top:10px;margin-left:10px">'+
									'</div>'+
									'<div id="resource_pool_view" style="float:right;height:80%; width:75%;margin-top:10px;margin-right:10px">'+
									'</div>'+
								'</div>';

var providerViewTemplate = '<div id="provider_accordion" style="width:100%">'+
								'<%_.each(providers, function(provider){%>'+
								'<h3><span style="margin-left:20px"><b style="font-size:13px">Resource Provider : <%=provider.name%></b></span>'+
								'<span style="margin-left:10px"><b id="des_of_<%=provider.id%>" style="font-size:12px"></b></span></h3>'+
								'<div id="servers_of_<%=provider.id%>" style="text-align:center">'+
								'</div>'+
								'<%})%>'+
							'</div>';
							
var contextViewTemplate = '<div id="context_accordion" style="width:100%;">'+
								'<h3><span style="margin-left:20px"><b style="font-size:13px">Business Domain</b></span></h3>'+
								'<div id="context_tree_view">'+
									'<ul id="context_tree" class="ztree ui-menu ui-corner-bottom"></ul>'+
								'</div>'+
							'</div>';
							
var providerServersListTemplate = '<table class="bordered">'+  
									'<thead>'+   
										'<tr>'+  
											'<th>Name</th>'+  
											'<th>Provider</th>'+  
											'<th>Size</th>'+ 
											'<th>OS Type</th>'+ 
											'<th>Status</th>'+
											'<th>IP</th>'+
											'<th>Public IP</th>'+
											'<th>Private IP</th>'+
											'<th>Create Date</th>'+
										'</tr>'+  
									'</thead>'+  
									'<%_.each(servers, function(server){%>'+
									'<tr>'+  
										'<td>'+
											'<img src="static/images/Server.png" border="0" width="16" height="16">'+
											'<span style="font-size:13px; margin-left:10px"><%=server.name%></span>'+
										'</td>'+  
										'<td>'+
											'<img src="static/images/cloud-computing-icon.png" border="0" width="16" height="16">'+
											'<span style="font-size:13px; margin-left:10px"><%=server.provider%></span>'+
										'</td>'+    
										'<td>'+
											'<img src="static/images/service_usage.png" border="0" width="16" height="16">'+
											'<span style="font-size:13px; margin-left:10px"><%=server.size%></span>'+
										'</td>'+
										'<td>'+
											'<img src="<%=server.os_type==\'windows\'?\'static/images/windows.png\':\'static/images/linux.png\'%>" border="0" width="16" height="16">'+
											'<span style="font-size:13px; margin-left:10px"><%=server.os_type%></span>'+
										'</td>'+
										'<td>'+
											'<img src="<%=server.is_allocated==\'false\'?\'static/images/service_status-online.png\':\'static/images/service_status-canceled.png\'%>" border="0" width="16" height="16">'+
											'<span style="font-size:13px; margin-left:10px"><%=server.is_allocated==\'false\'?\'Available\':\'Allocated\'%></span>'+
										'</td>'+ 
										'<td>'+
											'<img src="static/images/NetworkServer.png" border="0" width="16" height="16">'+
											'<span style="font-size:13px; margin-left:10px"><%=server.ip_address%></span>'+
										'</td>'+ 
										'<td>'+
											'<img src="static/images/NetworkServer.png" border="0" width="16" height="16">'+
											'<span style="font-size:13px; margin-left:10px"><%=server.public_ip_address%></span>'+
										'</td>'+ 
										'<td>'+
											'<img src="static/images/NetworkServer.png" border="0" width="16" height="16">'+
											'<span style="font-size:13px; margin-left:10px"><%=server.private_ip_address%></span>'+
										'</td>'+ 
										'<td>'+
											'<img src="static/images/date_icon.png" border="0" width="16" height="16">'+
											'<span style="font-size:13px; margin-left:10px"><%=server.create_date%></span>'+
										'</td>'+ 
									'</tr>'+ 
									'<%})%>'+
								'</table>';

var noProviderServersTemplate = '<span class="nodata">No provider servers were found.</span>';

var loadingTemplate = '<div id="providerloading" style="position: absolute; top: 50%; left: 50%;">'+
							'<img src="static/images/ajax-loader.gif" border="0" alt="Loading">'+
					'</div>';