var profileMainPageTemplate = '<div id="profileDiv">'+
								'<nav>'+
									'<div class="leftnav">'+			
										'<div class="left_menubox">'+
											'<ul class="left_menu">'+	
												'<li class="activepage">'+
													'<a href="javascript:void(0)" onclick="getAllUserKeys()">User Keys</a>'+
												'</li>'+	
											'</ul>'+
										'</div>'+	
									'</div>'+
								'</nav>'+
								'<form id="userKeysForm">'+		
									'<section id="centersection_big" style="">'+
										'<div class="section-big-lfnav">'+  
										'</div>'+
									'</section>'+
									'<div id="loading" style="position: absolute; top: 50%; left: 50%; display: none;">'+
										'<img id="ajaxImg" src="static/images/ajax-loader.gif" border="0" alt="Loading">'+
									'</div>'+
								'</form>'+
							'</div>';

var userKeyListTemplate = '<article>'+
								'<div class="cntrtopnav sortbig">'+
									'<div class="sortnav"> '+
										// '<div class="filternav">'+
											'<input type="search" maxlength="50" class="uniform-input text" placeholder="Filter by key name..." oninput="filterUserKey(event)"> '+
				            			// '</div>'+
									'</div>'+
									'<div class="forbuttoncat" style="margin: 10px 20px 0 0">'+
										'<a id="newUserKeyBtn" class="greybutton" style="cursor: pointer" onclick="openNewUserKeyDialog()">'+
											'<span>New User Key</span>'+								
										'</a>'+
									'</div>'+         
								'</div>'+
								'<div id="mycustomscroll_so" ng-controller="userKeyCtrl" onclick="queryUserKeys()">'+	
									'<div id="userKeysListDiv">'+
									'</div>'+			
								'</div>'+
							'</article>';

var userKeyListItemTemplate = '<div class="mainlist" data-uuid="<%=uuid%>" data-keyname="<%=name%>" data-email="<%=email%>">'+
									'<div class="listimage">'+
										'<a title="<%=name%>">'+
											'<img src="static/images/key.png" border="0" width="75" height="75" alt="Image">'+
										'</a>'+
									'</div>'+
									'<div class="listcontcataloge" style="width:60%">'+
										'<div class="listconttitle">'+
											'<div class="listconttitlecataloge" style="width:15%">'+
												'<p>'+
													'<span><%=name%></span>'+ 
													'<br clear="all">'+
												'</p>'+ 
											'</div>'+
											'<div class="listcontitem">'+
												'<ul>'+
													'<li>'+
														'<div class="statustab">'+
															'<a title="LDAP Email">'+
																'<img src="static/images/profile_visited.png" border="0" alt="Email">'+
															'</a>'+
															'<p>'+
																'<span class="title"><%=email%></span><br>'+ 
																'<span class="des">LDAP Email</span>'+
															'</p>'+
														'</div>'+
													'</li>'+
													'<li>'+
														'<div class="statustab">'+
															'<a title="Date Created">'+
																'<img src="static/images/date_icon.png" border="0" alt="Date">'+
															'</a>'+
															'<p>'+
																'<span class="title"><%=new Date(create_date).toUTCString()%></span><br> '+
																'<span class="des">Date Created</span>'+
															'</p>'+
														'</div>'+
													'</li>'+							
												'</ul>'+
											'</div>'+
										'</div>'+
									'</div> '+
									'<div class="forbuttoncat">'+
										'<a id="deleteUserKey" class="redbutton deleteRequestBtn" style="cursor: pointer" onclick="confirmDeleteUserKey(event)">'+
											'<span>Delete</span>'+								
										'</a>'+
									'</div>'+
									'<div class="forbuttoncat">'+
										'<a id="editUserKey" class="greybutton viewRequestBtn" style="cursor: pointer" onclick="viewDetails(event)">'+
											'<span>View Details</span>'+								
										'</a>'+
									'</div>'+
								'</div>';
								
var noDataTemplate = '<span class="nodata">No user keys were found that match the criteria.</span>';
								
var newUserKeyTemplate = '<form method="post" id="newUserKeyForm" name="newUserKeyForm" enctype="multipart/form-data">'+
								'<div class="childoptionlevel1left">'+
									'<img alt="New User Key" src="static/images/key.png">'+
									'<p>New User Key </p>'+
									'<p class="proddis">Provide details about your user key</p>'+
								'</div>'+
								'<div class="childoptionlevel1right" style="padding-top:15px">'+
									'<div class="row">'+
										'<span class="lable"> '+
											'<label>Key Name<span class="redasterix">*</span></label> '+
										'</span> '+
										'<span class="optioncont"> '+
											'<input type="text" name="name" id="name" maxlength="50" onblur="validateKeyName(\'name\');" class="uniform-input text"> '+
										'</span> '+
										'<div id="keynamevalidate" class="validationsymbol">'+
										'</div>'+
										'<div id="keyNameValName" class="validationsymbol">'+	
										'</div>'+ 
									'</div>'+
									'<div class="row">'+
										'<span class="lable">'+ 
											'<label>Ldap Email<span class="redasterix">*</span></label> '+
										'</span>'+ 
										'<span class="optioncont">'+ 
											'<label><%=emailAddress%></label>'+
											'<input type="hidden" name="email" id="email" value="<%=emailAddress%>"> '+
										'</span> '+
										'<div id="ldapemailvalidate" class="validationsymbol">'+
										'</div>'+
										'<div id="ldapEmailValName" class="validationsymbol">'+
										'</div> '+
									'</div>'+
									'<div class="row">'+
										'<span class="lable">'+ 
											'<label>Public Key<span class="redasterix">*</span></label> '+
										'</span>'+ 
										'<span class="optioncont">'+ 
											'<input type="file" name="public-key" id="public-key" class="uniform-input text" accept="application/json"> '+
										'</span> '+
									'</div>'+
								'</div>'+
							'</form>';	
							
var userKeyDetailTemplate = '<form method="put" id="userKeyForm" name="userKeyForm" enctype="multipart/form-data">'+
								'<div class="childoptionlevel1left">'+
									'<img alt="User Key Details" src="static/images/key.png">'+
									'<p>User Key Details</p>'+
									'<p class="proddis">Could change details about your user key</p>'+
								'</div>'+
								'<div class="childoptionlevel1right" style="padding-top:15px">'+
									// '<input type="hidden" name="uuid" id="uuid" maxlength="255" class="uniform-input text" value="<%=uuid%>"> '+
									'<div class="row">'+
										'<span class="lable"> '+
											'<label>Key Name</label> '+
										'</span> '+
										'<span class="optioncont"> '+
											'<input type="text" name="name" id="name" maxlength="50" onblur="validateKeyName(\'name\');" class="uniform-input text" value="<%=name%>"> '+
										'</span> '+
										'<div id="keynamevalidate" class="validationsymbol">'+
										'</div>'+
										'<div id="keyNameValName" class="validationsymbol">'+	
										'</div>'+ 
									'</div>'+
									'<div class="row">'+
										'<span class="lable">'+ 
											'<label>Ldap Email</label> '+
										'</span>'+ 
										'<span class="optioncont">'+ 
											'<label><%=email%></label>'+
											'<input type="hidden" name="email" id="email" value="<%=email%>"> '+
										'</span> '+
										'<div id="ldapemailvalidate" class="validationsymbol">'+
										'</div>'+
										'<div id="ldapEmailValName" class="validationsymbol">'+
										'</div> '+
									'</div>'+
									'<div class="row">'+
										'<span class="lable">'+ 
											'<label>Public Key</label> '+
										'</span>'+ 
										'<span class="optioncont">'+ 
											'<input type="file" name="public-key" id="public-key" class="uniform-input text" accept="application/json"> '+
										'</span> '+
									'</div>'+
									'<div class="row">'+
										'<span class="lable">'+ 
											'<label>Public Key Content</label> '+
										'</span>'+ 
										'<span class="optioncont">'+ 
											'<textarea id="key_content" style="width:380px;height:100px" readonly><%=keycontent%> </textarea>'+
										'</span> '+
									'</div>'+
								'</div>'+
							'</form>';