var deleteConfirmTemplate = '<div class="modalpopup">'+
								'<div class="top"><a class="closeimg simplemodal-close" href="javascript:void(0)" onclick="emptyConfirmModal()"><img src="static/images/close.png" border="0" alt="Close"></a></div>'+
								'<div class="cntr">'+
							    		' <div class="modalimg-req">'+
							    			'<img src="static/images/delete_icon.png" border="0" alt="Question">'+
							    		'</div> '+ 
							    		'<div class="modaltxt-req">'+
							    			'<span id="confrmTxt">Are you sure you want to delete the user key?</span><br>  '+ 
							  			'</div>'+
							    		'<div class="modbtns-cancel">'+
							   				'<a class="greybutton " href="javascript:void(0)" data-uuid="<%=uuid%>" onclick="deleteUserKey(event);">'+
							   				 	'<span>Yes</span>  '+
							   				'</a>'+
							   				'<a class="redbutton simplemodal-close" href="javascript:void(0)" onclick="emptyConfirmModal()">'+
							   					'<span>No</span>'+
							   				'</a>'+
							    		'</div>'+
							 	'</div>'+
								'<div class="btm"></div>'+
							'</div>';
							
var dialogMessageTemplate = '<div class="modalpopup">'+
								'<div class="top"><a class="closeimg simplemodal-close" href="javascript:void(0)" onclick="emptyConfirmModal()"><img src="static/images/close.png" border="0" alt="Close"></a></div>'+
								'<div class="cntr">'+
							    		' <div class="modalimg-req" title="<%=title%>">'+
							    			'<img src="static/images/expsoon_icon.png" border="0" alt="Question">'+
							    		'</div> '+ 
							  			'<div class="modaltxt-req">'+
							    			'<span id="confrmTxt"><%=message%></span><br>  '+ 
							  			'</div>'+
							    		'<div class="modbtns-cancel" style="margin-left:280px">'+
							   				'<a class="greybutton simplemodal-close" href="javascript:void(0)" onclick="emptyConfirmModal()">'+
							   					'<span>Ok</span>'+
							   				'</a>'+
							    		'</div>'+
							 	'</div>'+
								'<div class="btm"></div>'+
							'</div>';
