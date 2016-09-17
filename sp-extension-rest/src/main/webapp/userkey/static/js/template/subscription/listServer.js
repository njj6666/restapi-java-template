var serverListTemplate = '<table class="bordered">'+  
							'<thead>'+   
								'<tr>'+  
									'<th>IP</th>'+  
									'<th>Public IP</th>'+  
									'<th>Private IP</th>'+ 
									'<th>Size</th>'+
									'<th>IaaS Provider</th>'+
									'<th>DNS</th>'+
									'<th>Product</th>'+ 
									'<th>Version</th>'+ 
									'<th>Action</th>'+ 
								'</tr>'+  
							'</thead>'+  
							'<%_.each(servers, function(server){%>'+
							'<tr data-uuid="<%=server.productid%>" data-version="<%=server.version%>">'+  
								'<td><%=server.ip_address%></td>'+  
								'<td><%=server.public_ip_address%></td>'+    
								'<td><%=server.private_ip_address%></td>'+
								'<td><%=server.size%></td>'+ 
								'<td><%=server.iaas_provider%></td>'+ 
								'<td><%=server.dns%></td>'+ 
								'<td><%=server.products%></td>'+ 
								'<td><p id="<%=server.productid%>" contenteditable="true" ><%=server.version%></p></td>'+ 
								'<td><input type="button"  onclick="updateVersion(event)" value="Update Version"/></td>'+ 
							'</tr>'+ 
							'<%})%>'+
						'</table>'; 
						
var propertyListTemplate = '<table class="bordered">'+  
							'<thead>'+   
								'<tr>'+  
									'<th>Property Name</th>'+  
									'<th>Property Value</th>'+ 
								'</tr>'+  
							'</thead>'+  
							'<%_.each(properties, function(property){%>'+
							'<tr>'+  
								'<td><%=property.property_name%></td>'+  
								'<td><%=property.property_value%></td>'+ 
							'</tr>'+ 
							'<%})%>'+
						'</table>'; 