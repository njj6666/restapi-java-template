// var newLogTemplate = '<form method="post" id="newLogForm" name="newLogForm" enctype="multipart/form-data">'+
								// '<div class="childoptionlevel1left" style="text-align:center;padding-top:20px">'+
									// '<img alt="Add Project" src="images/icon_requestorcube.png" width="48" height="48">'+
									// '<p></p>'+
									// '<p class="proddis">Input name of new project</p>'+
								// '</div>'+
								// '<div class="childoptionlevel1right" style="padding-top:15px">'+
									// '<div class="row">'+
										// '<span class="lable"> '+
											// '<label>subscription<span class="redasterix">*</span></label> '+
										// '</span> '+
										// '<span class="optioncont"> '+
											// '<span><%=selectedSub%></span>'+
					            			// '<input type="text" id="subscription_id" name="subscription_id" style="visibility: hidden" value="<%=selectedSub%>"> '+
										// '</span> '+
									// '</div>'+
									// '<div class="row">'+
										// '<span class="lable"> '+
											// '<label>Component<span class="redasterix">*</span></label> '+
										// '</span> '+
										// '<span class="optioncont"> '+
											// '<input type="text" name="component" id="component" maxlength="50" class="uniform-input text"> '+
										// '</span> '+
									// '</div>'+
									// '<div class="row">'+
										// '<span class="lable"> '+
											// '<label>Log Level<span class="redasterix">*</span></label> '+
										// '</span> '+
										// '<span class="optioncont"> '+
											// '<input type="text" name="log_level" id="log_level" maxlength="50" class="uniform-input text"> '+
										// '</span> '+
									// '</div>'+
									// '<div class="row">'+
										// '<span class="lable"> '+
											// '<label>Event<span class="redasterix">*</span></label> '+
										// '</span> '+
										// '<span class="optioncont"> '+
											// '<input type="text" name="event" id="event" maxlength="50" class="uniform-input text"> '+
										// '</span> '+
									// '</div>'+
									// '<div class="row">'+
										// '<span class="lable"> '+
											// '<label>Message<span class="redasterix">*</span></label> '+
										// '</span> '+
										// '<span class="optioncont"> '+
											// '<input type="text" name="message" id="message" maxlength="50" class="uniform-input text"> '+
										// '</span> '+
									// '</div>'+
								// '</div>'+
							// '</form>';
							
var logListTemplate = '<table class="bordered" style="max-width:1000px">'+  
							'<thead>'+   
								'<tr>'+  
									'<th>Component</th>'+  
									'<th>Event</th>'+ 
									'<th>Message</th>'+
									'<th>Log Level</th>'+ 
									'<th>Log Path</th>'+
									'<th>Log Tag</th>'+
									'<th>Log Detail</th>'+
									'<th>Log Time</th>'+ 
								'</tr>'+  
							'</thead>'+  
							'<%_.each(logs, function(log){%>'+
							'<tr>'+  
								'<td><%=log.component%></td>'+ 
								'<td><%=log.event%></td>'+ 
								'<td><%=log.message%></td>'+ 
								'<td><%=log.log_level%></td>'+ 
								'<td><%=log.path%></td>'+ 
								'<td><%=log.tag%></td>'+ 
								'<td><%=log.detail%></td>'+ 
								'<td><%=log.log_time%></td>'+  
							'</tr>'+ 
							'<%})%>'+
						'</table>'; 
					