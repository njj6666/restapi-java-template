var subscriptionListTemplate = '<article>'+
									'<div id="subscriptionsListDiv">'+
									'</div>'+
								'</article>';
								
var subscriptionListItemTemplate = '<table class="bordered">'+  
										'<thead>'+   
											'<tr>'+  
												'<th>Name</th>'+  
												'<th>Status</th>'+  
												'<th>Owner</th>'+ 
												'<th>Organization</th>'+ 
												'<th>Project</th>'+
												'<th>Start Time</th>'+
												'<th>Terminate Time</th>'+
												'<th>More...'+
												'<img title="Export Subscriptions" src="static/images/export.png" width="16" height="16" style="cursor:pointer;margin-top:-2px;margin-bottom:-6px;float:right" onclick="exportToXLS()">'+
												'</th>'+
											'</tr>'+  
										'</thead>'+  
										'<%_.each(subscriptions, function(subscription){%>'+
										'<tr data-uuid="<%=subscription.uuid%>" data-name="<%=subscription.name%>">'+  
											'<td style="max-width: 200px;">'+
												'<img src="static/images/subscriptions-all-up.png" border="0" width="16" height="16">'+
												'<span style="font-size:13px; margin-left:10px"><%=subscription.name%></span>'+
											'</td>'+  
											'<td>'+
												'<img src="<%=subscription.statusIcon%>" border="0" width="16" height="16">'+
												'<span style="font-size:13px; margin-left:10px"><%=subscription.status%></span>'+
											'</td>'+    
											'<td>'+
												'<img src="static/images/profile_active.png" border="0" width="16" height="16">'+
												'<span style="font-size:13px; margin-left:10px"><%=subscription.owner_id%></span>'+
											'</td>'+
											'<td>'+
												'<img src="static/images/org_image.png" border="0" width="16" height="16">'+
												'<span style="font-size:13px; margin-left:10px"><%=subscription.csa_org_name%></span>'+
											'</td>'+
											'<td style="max-width: 200px;">'+
												'<img src="static/images/icon_requestorcube.png" border="0" width="16" height="16">'+
												'<span style="font-size:13px; margin-left:10px"><%=subscription.project_name%></span>'+
											'</td>'+ 
											'<td>'+
												'<img src="static/images/date_icon.png" border="0" width="16" height="16">'+
												'<span style="font-size:13px; margin-left:10px"><%=subscription.start_time%></span>'+
											'</td>'+ 
											'<td>'+
												'<img src="static/images/date_icon.png" border="0" width="16" height="16">'+
												'<span style="font-size:13px; margin-left:10px"><%=subscription.terminate_time%></span>'+
											'</td>'+
											'<td>'+
												'<img title="Properties" src="static/images/Gears.png" width="16" height="16" style="cursor:pointer" onclick="getProperties(event)">&nbsp;&nbsp;&nbsp;&nbsp;'+
												'<img title="Servers" src="static/images/Server.png" width="16" height="16" style="cursor:pointer" onclick="getServers(event)">&nbsp;&nbsp;&nbsp;&nbsp;'+
												'<img title="Preview Logs" src="static/images/ico-drop-file-32.png" width="16" height="16" style="cursor:pointer" onclick="getLogs(event)">&nbsp;&nbsp;&nbsp;&nbsp;'+
												'<img title="Export Logs" src="static/images/export.png" width="16" height="16" style="cursor:pointer" onclick="exportLogs(event)">'+
											'</td>'+ 
										'</tr>'+ 
										'<%})%>'+
									'</table>'+
									'<div class="pagination" style="float:right">'+
									   ' <a href="#" class="first" data-action="first">&laquo;</a>'+
									    '<a href="#" class="previous" data-action="previous">&lsaquo;</a>'+
									    '<input type="text" readonly="readonly" data-current-page="<%=currentpage%>" data-max-page="<%=page%>" />'+
									    '<a href="#" class="next" data-action="next">&rsaquo;</a>'+
									    '<a href="#" class="last" data-action="last">&raquo;</a>'+
									'</div>'; 

var noSubscriptionsTemplate = '<span class="nodata">No subscriptions were found.</span>';

var subscriptionPageTemplate = '<div style="height:100%" id="contentPage">'+
									'<div id="filter_accordion" style="float:right;width:18%">'+
										 '<h3><span style="margin-left:20px"><b style="font-size:13px">Subscription Filter</b></span></h3>'+
										 '<div style="padding-top:15px;padding-left:10px">'+
											'<div id="cnode_view"></div><br><br>'+
											'<div id="oproject_view"></div><br><br>'+
											'<div>'+
												'<label><b>Status</b></label><br><br>'+
												'<select id="status" style="width:200px;">'+
												 	 '<option value="">All</option>'+
												     '<option value="PENDING">PENDING</option>'+
													 '<option value="ACTIVE">ACTIVE</option>'+
													 '<option value="CANCELLED">CANCELLED</option>'+
													 '<option value="TERMINATED">TERMINATED</option>'+
												'</select>'+
											'</div><br><br>'+
											'<div id="products_view"></div><br><br>'+
											'<div id="versions_view"></div><br><br>'+
											'<div>'+
												'<div style="float:left">'+
													'<label><b>Start Time From</b></label><br><br>'+
													'<input type="text" id="start_from" name="start_from" style="width:80px;">'+
												'</div>'+
												'<div style="float:right">'+
													'<label><b>To</b></label><br><br>'+
													'<input type="text" id="start_to" name="start_to" style="width:80px;">'+
												'</div>'+
											'</div><br><br>'+
											'<div><br><br><br>'+
												'<div style="float:left">'+
													'<label><b>Terminate Time From</b></label><br><br>'+
													'<input type="text" id="terminate_from" name="terminate_from" style="width:80px;">'+
												'</div>'+
												'<div style="float:right">'+
													'<label><b>To</b></label><br><br>'+
													'<input type="text" id="terminate_to" name="terminate_to" style="width:80px;">'+
												'</div>'+
											'</div><br><br>'+
											'<div class="forbuttoncat" id="sub_filter_btn" style="height:30px">'+
												'<a class="greenbutton" style="cursor: pointer" onclick="doFilter()">'+
													'<span>Search</span>'+								
												'</a>'+
											'</div>'+
										'</div>'+
									'</div>'+
									'<div style="height:100%; width:80%;">'+
										'<div id="cs-layout-management-main-content" style="height:100%;">'+
										'</div>'+
									'</div>'+
								'</div>';
