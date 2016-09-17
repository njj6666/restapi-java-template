/*
 * � Copyright 2012 Hewlett-Packard Development Company, L.P.
 * Confidential computer software. Valid license from HP required for possession, use or copying.
 * Consistent with FAR 12.211 and 12.212, Commercial Computer Software, Computer Software Documentation, and Technical Data for
 * Commercial Items are licensed to the U.S. Government under vendor's standard commercial license.
 * 
 */

/* Validation for Service Configure and Modify Subscription pages -> Begin */
$(document).ready(function(){	
	$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
	var lang = cspGlobalAttrNS.navigatorLocale;
   	switch (lang) {
   		case "de":
			/* For German Locale*/
   			$.datepicker.setDefaults( $.datepicker.regional[ "de" ] );
		   	break;
   		case "fr":
			/* For French Locale*/				
   			$.datepicker.setDefaults( $.datepicker.regional[ "fr" ] );
			break; 
   		case "es":
   	    	/* For Spanish Locale*/
   			$.datepicker.setDefaults( $.datepicker.regional[ "es" ] );
   			break;
   	    case "zh":
   	    	/* For Chinese Locale*/
   	    	$.datepicker.setDefaults( $.datepicker.regional[ "zh-CN" ] );
   			break;
   	    case "ja":
   	    	/* For Japanese Locale*/
   	    	$.datepicker.setDefaults( $.datepicker.regional[ "ja" ] );
   			break;
   	    case "ko":
   	    	/* For Korean Locale*/
   	    	$.datepicker.setDefaults( $.datepicker.regional[ "ko" ] );
   			break;
   	    case "da":
   	    	/* For Danish Locale*/
   	    	$.datepicker.setDefaults( $.datepicker.regional[ "da" ] );
   			break;
   	    case "ru":
   	    	/* For Russian Locale*/
   	    	$.datepicker.setDefaults( $.datepicker.regional[ "ru" ] );
   			break;
   	    case "it":
   	    	/* For Italian Locale*/
   	    	$.datepicker.setDefaults( $.datepicker.regional[ "it" ] );
   			break;
   	    case "nl":
   	    	/* For Dutch Locale*/
   	    	$.datepicker.setDefaults( $.datepicker.regional[ "nl" ] );
   			break;
   	    case "pt_BR":
   	    	/* For Portuguese-Brazil Locale*/
   	    	$.datepicker.setDefaults( $.datepicker.regional[ "pt-BR" ] );
   			break;
   		default:
			 /* For English Locale*/
   			$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
   	}
   	
	document.getElementById('endDate').disabled=true;
	$('#endDate').datepicker().datepicker('disable');
});
//Added for locale based validation
$('#startDate').keyup(function() {
	validateGivenStartDate();
});

$('#endDate').keyup(function() {
	var valid=validateLocaleEndDate();
	
});

// Ended for locale based validation

var from ="service";

//Validation - Fields Not Empty
function checkFilled() {
		var flag = 0;
		var subsName = "";
		var endDate = "";
		var startDate = "";
		subsName = document.getElementById("subsName").value;
		subsName = subsName.replace(/^\s+/,""); // strip leading spaces
		if (subsName.length > 0) {flag ++;}
					
		if(document.getElementById("noEndDate1").checked == true){
			startDate = document.getElementById("startDate").value;
			startDate = startDate.replace(/^\s+/,""); // strip leading spaces
			if(startDate.length>0){flag ++;}
			endDate = document.getElementById("endDate").value;
			endDate = endDate.replace(/^\s+/,""); // strip leading spaces
			if (endDate.length > 0){flag ++;}			
			if (flag == 3) {
					$("#reqbtn").removeClass("greybuttonrequest");
					$("#reqbtn").addClass("bluebutton");
			        document.getElementById("reqbtn").setAttribute("href","javascript:void(0)");   
					return true;
				}
				else {
					$("#reqbtn").removeClass("bluebutton");
					$("#reqbtn").addClass("greybuttonrequest");
					document.getElementById("reqbtn").removeAttribute("href"); 
					return false;
				} // in case a field is filled then erased
		}
		else{
			startDate = document.getElementById("startDate").value;
			startDate = startDate.replace(/^\s+/,""); // strip leading spaces
			if (startDate.length > 0) {flag ++;}
			if (flag == 2) {
				$("#reqbtn").removeClass("greybuttonrequest");
				$("#reqbtn").addClass("bluebutton");
				document.getElementById("reqbtn").setAttribute("href","javascript:void(0)");
		        return true;
			}
			else {
				$("#reqbtn").removeClass("bluebutton");
				$("#reqbtn").addClass("greybuttonrequest");
				document.getElementById("reqbtn").removeAttribute("href");
				return false;
			} // in case a field is filled then erased
			
		}	

	}

function checkModify(){
	var changed = false;
	var changedSubsName=$("#subsNameForm").val();
    var changedSubsDesc=$("#subsDescForm").val();
	var changedStartDate=$("#startDate").val();
	var changedEndDate=$("#endDate").val();
	
	if ((subscriptionDetailGlobalNS.fList[0] != changedSubsName) || (subscriptionDetailGlobalNS.fList[1] != changedSubsDesc) || (subscriptionDetailGlobalNS.fList[2] != changedStartDate) || (subscriptionDetailGlobalNS.fList[3] != changedEndDate) || subscriptionDetailGlobalNS.isOptionChanged || isDateChanged) {
	      changed = true;
	      $("#subsbtn").removeClass("greybuttonrequest");
	  	  $("#subsbtn").addClass("bluebutton");
	  	  document.getElementById("subsbtn").setAttribute("href","javascript:void(0)");
	  	  return true;
	    }else{
	    	changed = false;
	    	$("#subsbtn").removeClass("bluebutton");
	    	$("#subsbtn").addClass("greybuttonrequest");
	    	document.getElementById("subsbtn").removeAttribute("href");
	    	return false;
	    }
}

function checkModifyRequest(){
		var changed = false;
		var changedSubsName=$("#subsNameForm").val();
	    var changedSubsDesc=$("#subsDescForm").val();
		var changedStartDate=$("#startDate").val();
		var changedEndDate=$("#endDate").val();
		if(subscriptionDetailGlobalNS.fList!=null && subscriptionDetailGlobalNS.fList !=undefined && subscriptionDetailGlobalNS.fList!=""){
			if ((subscriptionDetailGlobalNS.fList[0] != changedSubsName) || (subscriptionDetailGlobalNS.fList[1] != changedSubsDesc) || (subscriptionDetailGlobalNS.fList[2] != changedStartDate) || (subscriptionDetailGlobalNS.fList[3] != changedEndDate) || subscriptionDetailGlobalNS.isOptionChanged) {
			      changed = true;		      
			  	  return true;
			    }else{
			    	changed = false;		    	
			    	return false;
			    }
		}		
}

function checkFilledRequest(){
	var flag = 0;
	var subsName = "";
	var endDate = "";
	var startDate = "";
	subsName = document.getElementById("subsName").value;
	subsName = subsName.replace(/^\s+/,""); // strip leading spaces
	if (subsName.length > 0) {flag ++;}
				
	if(document.getElementById("noEndDate1").checked == true){
		startDate = document.getElementById("startDate").value;
		startDate = startDate.replace(/^\s+/,""); // strip leading spaces
		if(startDate.length>0){flag ++;}
		endDate = document.getElementById("endDate").value;
		endDate = endDate.replace(/^\s+/,""); // strip leading spaces
		if (endDate.length > 0){flag ++;}			
		if (flag > 0) {
				return true;
			}
			else {
				return false;
			} // in case a field is filled then erased
	}
	else{
		startDate = document.getElementById("startDate").value;
		startDate = startDate.replace(/^\s+/,""); // strip leading spaces
		if (startDate.length > 0) {flag ++;}
		if (flag > 1) {
			return true;
		}
		else {
			return false;
		} // in case a field is filled then erased
		
	}
}


// Validation - Subscription Name
function validateSubName(srcId) {
	if(srcId=='subsName')
	{
		checkFilled();
		from="service";
	}
	else{
		checkModify();
		from="subscription";
	}		
		
	var subName = document.getElementById(srcId);
	var spaceFlag=0; 
	if(subName!=null){
	    var strText = subName.value; 
	    if (strText!="")       
	    {       
	    	var strArr = new Array();
	    	strArr = strText.split("");
	        if(strArr[0]==" ") 
	        	spaceFlag=1;
	    }
	}
	if ( subName != null && subName.value != null &&  spaceFlag==1 ){
		//not begin with null
		document.getElementById('subsvalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		$("#subsValName").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionNameSpaceValid')+"</span>");}
	else if (isSpclChar(subName.value) == true){
		//no special character allowed
		document.getElementById('subsvalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		$("#subsValName").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionNameSpecialCharater')+"</span>");
		return false;
	   }	
	else if (subName.value.length == 0 )
		{
			document.getElementById('subsvalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
			$("#subsValName").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionNameValid')+"</span>");
			/*$("#subsValName").html("");
			$("#subsvalidate").html("");*/
			return false;
		}
		else
		{
			//subName success
			document.getElementById('subsvalidate').innerHTML = '<img src="static/images/right_img.gif" border="0" alt="Right">';
			$("#subsValName").html("");
			return true;
	    }
		
}

//Validation for subscription Description
function validateSubsDesc(textAreaId)
{
	var subsDesc = document.getElementById(textAreaId);
	var flag=true;
	if((subsDesc != null && subsDesc.value != null) &&  subsDesc.value.length != 0 &&  isSpclChar(subsDesc.value) )
	{
		document.getElementById('subsDescValidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		$("#subsDescValName").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionNameSpecialCharater')+"</span>");
		flag=false;
	}
	else{
		document.getElementById('subsDescValidate').innerHTML = '';
		$("#subsDescValName").html("");
		$("#subsbtn").removeClass("greybuttonrequest");
		$("#subsbtn").addClass("bluebutton");
		flag=true;
	}
	sizeBox();
	return flag;
}

function clearErrors()
{
	document.getElementById('subsvalidate').innerHTML = '';
	$("#subsValName").html("");
	document.getElementById('subsDescValidate').innerHTML = '';
	$("#subsDescValName").html("");
	document.getElementById('enddatevalidate').innerHTML="";
	$("#endDateValTxt").html("");
}
// Validation - Start Date
function validateStartDate(srcId) {
	if(srcId=='subsName'){
		checkFilled();
		from="service";
	}
	else{
		checkModify();
		from="subscription"; 
	}
	
	var dat=new Date();
	var today=(dat.getMonth()+1) + '/' +dat.getDate() + '/' +dat.getFullYear();
	var mydate= $('#internalStartDate').val();	
	if(mydate.length==0){
		document.getElementById('strtdatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		$("#startDateValTxt").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionStartPastDateValid')+"</span>");
		return false;
	}
	else if(isValidDate(mydate)){
		document.getElementById('strtdatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		var localeError=getLocaleBasedStartDateError();
		$("#startDateValTxt").html("<span class='nodata-error'>"+localeError+"</span>");
		return false;
	}
	else if((new Date(mydate) < new Date(today)) && !(isValidDate(mydate))){
		document.getElementById('strtdatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
		$("#startDateValTxt").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionStartPastDateValid')+"</span>");
		return false;
	}	
	else{
		document.getElementById('strtdatevalidate').innerHTML = '<img src="static/images/right_img.gif" border="0" alt="Right">';
		$("#startDateValTxt").html("");
		return true;
	}
		
}

// Validation - End Date
function validateEndDate() {
	if(document.getElementById("subsbtn")!=null){
		$("#subsbtn").removeClass("greybuttonrequest");
		$("#subsbtn").addClass("bluebutton");
		document.getElementById("subsbtn").setAttribute("href","javascript:void(0)");
	}
			
	if(($("#noEndDate").is(':checked'))==true){
		document.getElementById('enddatevalidate').innerHTML="";
		$("#endDateValTxt").html("");
	}else if ($("#internalEndDate").val().length == 0 || isValidDate($('#internalEndDate').val())) {
			document.getElementById('enddatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
			var length=$("#endDate").val().length;
			if(length == 0)
				$("#endDateValTxt").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionEndDateEmpty')+"</span>");
			else if(isValidDate($('#internalEndDate').val())){
				var localeEndError=getLocaleBasedEndDateError();
				$("#endDateValTxt").html("<span class='nodata-error'>"+localeEndError+"</span>");
			}
			return false;
		}
	else {
		if (isEndDateOneYearBeyond()) {
			document.getElementById('enddatevalidate').innerHTML = '<img src="static/images/right_img.gif" border="0" alt="Right">';
			$("#endDateValTxt").html("");
			return true;
		} 
		else if($("#noEndDate").is(':checked')){ 
			//document.getElementById("enddatecalen").disabled=true;
			$('#endDate').datepicker().datepicker('disable');
      		document.getElementById("endDate").disabled=true;
      		document.getElementById('enddatevalidate').innerHTML="";
      		$("#endDateValTxt").html("");
		} 
		else {
			document.getElementById('enddatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
			$("#endDateValTxt").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionEndDateOneYearValid',orgCustomPropNS.endDatePeriod)+"</span>");
			return false;
		}
	}
}

// Validation - Is No End Date Checked 
function isNoEndDateChecked(srcId) {
	var endDateChkBox = document.getElementById("noEndDate");
	if (endDateChkBox != null) {
		if (endDateChkBox.checked) {
			return true;
		} 
		else {
			return validateEndDate(srcId);
		}
	}
}

// Is Valid Date
function isValidDate(srcDate) {
	var date=srcDate.split('/');
	var mm = parseInt(date[0],10);
	var dd = parseInt(date[1],10);
	var yyyy = parseInt(date[2],10);
	var noOfDaysInMonth = getNoOfDaysInMonth(parseInt(mm), parseInt(yyyy));
	if(mm >= 1 && mm <= 12) {
		if(dd >= 1 && dd <= noOfDaysInMonth) {
			if(yyyy >=1900 && yyyy <= 9999)
				return isNaN(new Date(srcDate));
			else 
				return true;
		} 
		else 
			return true;
	}
	else 
		return true;
}

//Special Character Validation
function isSpclChar(srcVal) {
	var iChars = "&%<";
	for (var i = 0; i < srcVal.length; i++) {
		if (iChars.indexOf(srcVal.charAt(i)) != -1) return true;
	}
}

// Check - End Date is beyond one year and less than Start Date
function isEndDateOneYearBeyond() {
	var serviceStartDt = $('#internalStartDate').val();
	var serviceEndDt = $('#internalEndDate').val();
	var oneYearEndDate = "";
	var splitStartDate=serviceStartDt.split("10");
	var splitEndDate=serviceEndDt.split("10");
	if (serviceStartDt.length != 0 && !isValidDate(serviceStartDt) && serviceEndDt.length != 0 && !isValidDate(serviceEndDt)) {
		if (new Date(serviceStartDt) > new Date(serviceEndDt)) return false;				// End Date should be greater than Start Date
		oneYearEndDate = getDefaultOneYearEndDate(); 												// End Date should not beyond 1 Year
		if (new Date(serviceEndDt) > new Date (oneYearEndDate)) return false;		
		return true;
	}
	else 		
		return false;	
}

// To get End Date [One Year against Start Date]
function getOneYearEndDate() {
	var lang = cspGlobalAttrNS.navigatorLocale; 	
	var finalEndDate = "";
	var lastDay ="";
	var	serviceStartDate = document.getElementById("internalStartDate");	
	var endDateProp=0;
	var dateArray=new Array();
	var monthTemp="";
	var dateTemp="";
	if (serviceStartDate != null && serviceStartDate != "") {
		var s = serviceStartDate.value.split("/");
		var month = s[0];
		var day = s[1];
		var year = s[2];
		//Added for change of end date period from property file
		if(orgCustomPropNS.endDatePeriod!=null && orgCustomPropNS.endDatePeriod !=undefined)
			endDateProp=parseInt(orgCustomPropNS.endDatePeriod);
		if(endDateProp==0 || orgCustomPropNS.endDatePeriod==null || orgCustomPropNS.endDatePeriod==undefined)
			endDateProp=1;
		month=parseInt(month,10)+endDateProp;
		while(month>12){
			month=month-12;
			year=++s[2];
		}		
		//Added ended for change of end date period from property file
		var noOfDaysInMonth = getNoOfDaysInMonth(parseInt(month), parseInt(year));
	}
	switch(lang){
	case "de":
		if (serviceStartDate != null && serviceStartDate != "") {
		if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
				finalEndDate = (noOfDaysInMonth-1) + "." + month + "." + year;
			else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = lastDay + "." + 12  + "." +s[2];
			}
			else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = lastDay + "." + (month-1) + "." + year;
			}else
				finalEndDate = (day-1) + "." + month + "." + year;	
			
		}	
		dateArray=finalEndDate.split(".");
		monthTemp=dateArray[1];
		dateTemp=dateArray[0];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=dateTemp+"."+monthTemp+"."+dateArray[2];
		return finalEndDate;
		break;
	case "fr":	
		if (serviceStartDate != null && serviceStartDate != "") {
			if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
				finalEndDate = (noOfDaysInMonth-1) + "/" + month + "/" + year;
			else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = lastDay + "/" + 12  + "/" +s[2];
			}
			else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = lastDay + "/" + (month-1) + "/" + year;
			}else
				finalEndDate = (day-1) + "/" + month + "/" + year;		
		}	
		dateArray=finalEndDate.split("/");
		monthTemp=dateArray[1];
		dateTemp=dateArray[0];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=dateTemp+"/"+monthTemp+"/"+dateArray[2];
		return finalEndDate;
		break;
	case "es":
		if (serviceStartDate != null && serviceStartDate != "") {
			if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
				finalEndDate = (noOfDaysInMonth-1) + "/" + month + "/" + year;
			else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = lastDay + "/" + 12  + "/" +s[2];
			}
			else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = lastDay + "/" + (month-1) + "/" + year;
			}else
				finalEndDate = (day-1) + "/" + month + "/" + year;		
		}	
		dateArray=finalEndDate.split("/");
		monthTemp=dateArray[1];
		dateTemp=dateArray[0];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=dateTemp+"/"+monthTemp+"/"+dateArray[2];
		return finalEndDate;
		break;
    case "zh":
		if (serviceStartDate != null && serviceStartDate != "") {
			if (parseInt(noOfDaysInMonth) <= parseInt(day)) 				
				finalEndDate = year + "-" + month + "-" +(noOfDaysInMonth-1);
			else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = s[2] + "-" + 12  + "-" +lastDay;
			}
			else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = year + "-" + (month-1) + "-" + lastDay;
			}else
				finalEndDate = year + "-" + month + "-" + (day-1); 		
		}	
		dateArray=finalEndDate.split("-");
		monthTemp=dateArray[1];
		dateTemp=dateArray[2];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=dateArray[0]+"-"+monthTemp+"-"+dateTemp;
		return finalEndDate;
    	break; 
    case "ja":    		
    	  if (serviceStartDate != null && serviceStartDate != "") { 
    		if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
    			finalEndDate = year + "/" + month + "/" +(noOfDaysInMonth-1);
    		else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = s[2] + "/" + 12  + "/" +lastDay;
			}
    		else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = year + "/" + (month-1) + "/" + lastDay;
			}
    		else 
    			finalEndDate = year + "/" + month + "/" +(day-1);		
    	}    	
    	dateArray=finalEndDate.split("/");
  		monthTemp=dateArray[1];
  		dateTemp=dateArray[2];
  		if(monthTemp<10)monthTemp="0"+monthTemp;
  		if(dateTemp<10)dateTemp="0"+dateTemp;
  		finalEndDate=dateArray[0]+"/"+monthTemp+"/"+dateTemp;
    	return finalEndDate;
    	break;
    case "ko":
    	if (serviceStartDate != null && serviceStartDate != "") {
      		if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
      			finalEndDate = year + "-" + month + "-" + (noOfDaysInMonth-1);
      		else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = s[2] + "-" + 12  + "-" +lastDay;
			}
      		else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = year + "-" + (month-1) + "-" + lastDay;
			}
      		else 
      			finalEndDate = year + "-" + month + "-" + (day-1);		
      	}	
    	dateArray=finalEndDate.split("-");
		monthTemp=dateArray[1];
		dateTemp=dateArray[2];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=dateArray[0]+"-"+monthTemp+"-"+dateTemp;
      	return finalEndDate;
      	break;
    case "da":
    	if (serviceStartDate != null && serviceStartDate != "") {
      		if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
      			finalEndDate = (noOfDaysInMonth-1) + "-" + month + "-" + year;
      		else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = lastDay + "-" + 12  + "-" +s[2];
			}
      		else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = lastDay + "-" + (month-1) + "-" + year;
      		}
      		else 
      			finalEndDate = (day-1) + "-" + month + "-" + year;		
      	}	
    	dateArray=finalEndDate.split("-");
		monthTemp=dateArray[1];
		dateTemp=dateArray[0];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=dateTemp+"-"+monthTemp+"-"+dateArray[2];
      	return finalEndDate;
    	break;
    case "ru":
		if (serviceStartDate != null && serviceStartDate != "") {
		if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
				finalEndDate = (noOfDaysInMonth-1) + "." + month + "." + year;
			else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = lastDay + "." + 12  + "." +s[2];
			}
			else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = lastDay + "." + (month-1) + "." + year;
			}else
				finalEndDate = (day-1) + "." + month + "." + year;	
			
		}	
		dateArray=finalEndDate.split(".");
		monthTemp=dateArray[1];
		dateTemp=dateArray[0];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=dateTemp+"."+monthTemp+"."+dateArray[2];
		return finalEndDate;
		break;
    case "it":
		if (serviceStartDate != null && serviceStartDate != "") {
			if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
				finalEndDate = (noOfDaysInMonth-1) + "/" + month + "/" + year;
			else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = lastDay + "/" + 12  + "/" +s[2];
			}
			else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = lastDay + "/" + (month-1) + "/" + year;
			}else
				finalEndDate = (day-1) + "/" + month + "/" + year;		
		}	
		dateArray=finalEndDate.split("/");
		monthTemp=dateArray[1];
		dateTemp=dateArray[0];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=dateTemp+"/"+monthTemp+"/"+dateArray[2];
		return finalEndDate;
		break;
    case "nl":
    	if (serviceStartDate != null && serviceStartDate != "") {
      		if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
      			finalEndDate = (noOfDaysInMonth-1) + "-" + month + "-" + year;
      		else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = lastDay + "-" + 12  + "-" +s[2];
			}
      		else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = lastDay + "-" + (month-1) + "-" + year;
      		}
      		else 
      			finalEndDate = (day-1) + "-" + month + "-" + year;		
      	}	
    	dateArray=finalEndDate.split("-");
		monthTemp=dateArray[1];
		dateTemp=dateArray[0];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=dateTemp+"-"+monthTemp+"-"+dateArray[2];
      	return finalEndDate;
    	break;
    case "pt_BR":
		if (serviceStartDate != null && serviceStartDate != "") {
			if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
				finalEndDate = (noOfDaysInMonth-1) + "/" + month + "/" + year;
			else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = lastDay + "/" + 12  + "/" +s[2];
			}
			else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = lastDay + "/" + (month-1) + "/" + year;
			}else
				finalEndDate = (day-1) + "/" + month + "/" + year;		
		}	
		dateArray=finalEndDate.split("/");
		monthTemp=dateArray[1];
		dateTemp=dateArray[0];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=dateTemp+"/"+monthTemp+"/"+dateArray[2];
		return finalEndDate;
		break;
	default:	
		if (serviceStartDate != null && serviceStartDate != "") {
			if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
				finalEndDate = month + "/" + (noOfDaysInMonth-1) + "/" + year;
			else if(parseInt(day) == 1 && parseInt(month) == 1){
				lastDay = getNoOfDaysInMonth(12, parseInt(year));
				--s[2];
				finalEndDate = 12 + "/" + lastDay + "/" +s[2];
			}
			else if(parseInt(day) == 1){
				lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
				finalEndDate = (month-1) + "/" + lastDay + "/" + year;
			}else
				finalEndDate = month + "/" + (day-1) + "/" + year;		
		}	
		dateArray=finalEndDate.split("/");
		monthTemp=dateArray[0];
		dateTemp=dateArray[1];
		if(monthTemp<10)monthTemp="0"+monthTemp;
		if(dateTemp<10)dateTemp="0"+dateTemp;
		finalEndDate=monthTemp+"/"+dateTemp+"/"+dateArray[2];
		return finalEndDate;
	}
}

// To get No of days in a Month
function getNoOfDaysInMonth(month,year) {
	var m = [31,28,31,30,31,30,31,31,30,31,30,31];
	if (month != 2) {
		return m[month - 1];
	} 
	else if (year%4 != 0) {
		return m[1];
	}
	else if (year%100 == 0 && year%400 != 0) {
		return m[1];
	} 
	else {
		return (parseInt(m[1]) + 1);
	}
}

function getDefaultOneYearEndDate(){
var finalEndDate = "";
var lastDay ="";
var	serviceStartDate = document.getElementById("internalStartDate");	
var endDateProp=0;
if(orgCustomPropNS.endDatePeriod!=null && orgCustomPropNS.endDatePeriod !=undefined)
	endDateProp=parseInt(orgCustomPropNS.endDatePeriod);
if (serviceStartDate != null && serviceStartDate != "") {
	var s = serviceStartDate.value.split("/");
	var month = s[0];
	var day = s[1];
	var year = s[2];
	//Added for change of end date period from property file
	month=parseInt(month,10)+endDateProp;
	while(month>12){
		month=month-12;
		year=++s[2];
	}		
	//Added ended for change of end date period from property file	
	var noOfDaysInMonth = getNoOfDaysInMonth(parseInt(month), parseInt(year));
	if (parseInt(noOfDaysInMonth) <= parseInt(day)) 
		finalEndDate = month + "/" + (noOfDaysInMonth-1) + "/" + year;
	else if(parseInt(day) == 1 && parseInt(month) == 1){
		lastDay = getNoOfDaysInMonth(12, parseInt(year));
		--s[2];
		finalEndDate = 12 + "/" + lastDay  + "/" +s[2];
	}
	else if(parseInt(day) == 1){
		lastDay = getNoOfDaysInMonth(parseInt(month-1), parseInt(year));
		finalEndDate = (month-1) + "/" + lastDay + "/" + year;
	}else
		finalEndDate = month + "/" + (day-1) + "/" + year;		
}	
return finalEndDate;
}

function validateLocaleStartDate(){
	var dat=new Date();
	var lang = cspGlobalAttrNS.navigatorLocale;
	var today=(dat.getMonth()+1) + '/' +dat.getDate() + '/' +dat.getFullYear();
	var	localeStartDate = document.getElementById("startDate");	
	var s=new Array();
	var dummyStrtDate=new Date();
	switch(lang){
	case "de":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split(".");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || year.indexOf("/")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;		
	case "fr":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split("/");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;
	case "es":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split("/");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;
	case "zh":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split("-");
			if(s.length!=3) return false;
			var month = s[1];
			var year = s[0];
			var day = s[2];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || day.indexOf(".")!=-1 || day.indexOf("/")!=-1 || day.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;
	case "ja":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split("/");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[2];
			var year = s[0];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || day.indexOf(".")!=-1 || day.indexOf("-")!=-1 || day.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;
	case "ko":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split("-");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[2];
			var year = s[0];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || day.indexOf(".")!=-1 || day.indexOf("/")!=-1 || day.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;
	case "da":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split("-");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("/")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;
	case "ru":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split(".");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || year.indexOf("/")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;	
	case "it":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split("/");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;
	case "nl":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split("-");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("/")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;
	case "pt_BR":
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split("/");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
		break;
	default:	
		if (localeStartDate != null && localeStartDate != "") {
			s = localeStartDate.value.split("/");
			if(s.length!=3) return false;
			var month = s[0];
			var day = s[1];
			var year = s[2];		
			dummyStrtDate=month+"/"+day+"/"+year;
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyStrtDate;
		}	
	}
}

function validateLocaleEndDate(){
	var dummyEndDate=getLocaleEndDate();	
	if(($("#noEndDate").is(':checked'))==true){
		document.getElementById('enddatevalidate').innerHTML="";
		$("#endDateValTxt").html("");
		return true;
	}else {
		if(dummyEndDate==false){
			document.getElementById("internalEndDate").value=dummyEndDate;
			document.getElementById('enddatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
			var localeEndError=getLocaleBasedEndDateError();
			$("#endDateValTxt").html("<span class='nodata-error'>"+localeEndError+"</span>");
			return false;
		}
		else{
			if($("#endDate").val().length==0){
				document.getElementById("internalEndDate").value=dummyEndDate;
				document.getElementById('enddatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
				$("#endDateValTxt").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionEndDateEmpty')+"</span>");
				return false;
			}
			else if(isValidDate(dummyEndDate)){
				document.getElementById("internalEndDate").value=dummyEndDate;
				document.getElementById('enddatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
				var localeEndError=getLocaleBasedEndDateError();
				$("#endDateValTxt").html("<span class='nodata-error'>"+localeEndError+"</span>");
				return false;
			}
			else if(isLocaleEndDateOneYearBeyond(dummyEndDate)){
				document.getElementById("internalEndDate").value=dummyEndDate;
				document.getElementById('enddatevalidate').innerHTML = '<img src="static/images/right_img.gif" border="0" alt="Right">';
				$("#endDateValTxt").html("");
				return true;
			}
			else if($("#noEndDate").is(':checked')){ 
				//document.getElementById("enddatecalen").disabled=true;
				$('#endDate').datepicker().datepicker('disable');
	      		document.getElementById("endDate").disabled=true;
	      		document.getElementById('enddatevalidate').innerHTML="";
	      		$("#endDateValTxt").html("");
	      		return true;
			} 
			else {
				document.getElementById("internalEndDate").value=dummyEndDate;
				document.getElementById('enddatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
				$("#endDateValTxt").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionEndDateOneYearValid',orgCustomPropNS.endDatePeriod)+"</span>");
				return false;
			}
		}
	}	
}

function isLocaleEndDateOneYearBeyond(localeEndDate) {
	var serviceStartDt = $('#internalStartDate').val();
	var serviceEndDt = localeEndDate;
	var oneYearEndDate = "";
	var splitStartDate=serviceStartDt.split("10");
	var splitEndDate=serviceEndDt.split("10");
	if (serviceStartDt.length != 0 && !isValidDate(serviceStartDt) && serviceEndDt.length != 0 && !isValidDate(serviceEndDt)) {
		if (new Date(serviceStartDt) > new Date(serviceEndDt)) return false;				// End Date should be greater than Start Date
		oneYearEndDate = getDefaultOneYearEndDate(); 												// End Date should not beyond 1 Year
		if (new Date(serviceEndDt) > new Date (oneYearEndDate)) return false;		
		return true;
	}
	else 		
		return false;	
}

function getLocaleEndDate(){
	var	localeEndDate = $("#endDate").val();
	var lang=cspGlobalAttrNS.navigatorLocale;
	var dummyEndDate=new Date();
	var s=new Array();
	switch(lang){
	case "de":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split(".");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || year.indexOf("/")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else				
				return dummyEndDate;
		}	
		break;		
	case "fr":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split("/");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;				
		}	
		break;
	case "es":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split("/");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;			
		}	
		break;
	case "zh":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split("-");
			if(s.length!=3) return false;
			var month = s[1];
			var year = s[0];
			var day= s[2];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || day.indexOf(".")!=-1 || day.indexOf("/")!=-1 || day.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;			
		}	
		break;
	case "ja":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split("/");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[2];
			var year = s[0];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || day.indexOf(".")!=-1 || day.indexOf("-")!=-1 || day.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;				
		}	
		break;
	case "ko":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split("-");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[2];
			var year = s[0];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || day.indexOf(".")!=-1 || day.indexOf("/")!=-1 || day.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;				
		}	
		break;
	case "da":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split("-");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("/")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;			
		}	
		break;
	case "ru":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split(".");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || year.indexOf("/")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;
		}	
		break;	
	case "it":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split("/");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;			
		}	
		break;	
	case "nl":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split("-");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("/")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;			
		}	
		break;
	case "pt_BR":
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split("/");
			if(s.length!=3) return false;
			var month = s[1];
			var day = s[0];
			var year = s[2];		
			dummyEndDate=month+"/"+day+"/"+year;		
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;			
		}	
		break;
	default:	
		if (localeEndDate != null && localeEndDate != "") {
			s = localeEndDate.split("/");
			if(s.length!=3) return false;
			var month = s[0];
			var day = s[1];
			var year = s[2];		
			dummyEndDate=month+"/"+day+"/"+year;
			if(s.length>3 || year.indexOf(".")!=-1 || year.indexOf("-")!=-1 || year.indexOf(",")!=-1)
				return false;
			else
				return dummyEndDate;
		}	
	}
}
/* Validation for Service Configure and Modify Subscription pages <- End */

function getLocaleBasedStartDateError(){
	var textError;
		textError=jQuery.i18n.prop('csa.consumer.error.subscriptionStartDateFormatValid');
	return textError;
}

function getLocaleBasedEndDateError(){
	var textError;
		textError=jQuery.i18n.prop('csa.consumer.error.subscriptionEndDateFormatValid');
	return textError;
}

function validateGivenStartDate(){
	
	var dat=new Date();
	var today=(dat.getMonth()+1) + '/' +dat.getDate() + '/' +dat.getFullYear();
	var data=validateLocaleStartDate();
		if(data==false){
			document.getElementById('strtdatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
			var localeError=getLocaleBasedStartDateError();
			$("#startDateValTxt").html("<span class='nodata-error'>"+localeError+"</span>");
			document.getElementById("internalStartDate").value=data;
		}
		else{
			if (!(isValidDate(data)) && (new Date(data) >= new Date(today)) &&  $('#startDate').val().length!=0) {	
				document.getElementById('strtdatevalidate').innerHTML = '<img src="static/images/right_img.gif" border="0" alt="Right">';
				$("#startDateValTxt").html("");
				document.getElementById("internalStartDate").value=data;
				setEndDate();
			}	
			else{
				if($('#startDate').val().length==0){
					document.getElementById('strtdatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
					$("#startDateValTxt").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionStartPastDateValid')+"</span>");
				}
				else if(isValidDate(data)){
					document.getElementById('strtdatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
					var localeError=getLocaleBasedStartDateError();
					$("#startDateValTxt").html("<span class='nodata-error'>"+localeError+"</span>");
					document.getElementById("internalStartDate").value=data;
				}
				else if((new Date(data) < new Date(today)) && !(isValidDate(data))){
					document.getElementById('strtdatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
					$("#startDateValTxt").html("<span class='nodata-error'>"+jQuery.i18n.prop('csa.consumer.error.subscriptionStartPastDateValid')+"</span>");
					document.getElementById("internalStartDate").value=data;
				}	
				else{
					document.getElementById('strtdatevalidate').innerHTML = '<img src="static/images/wrong_img.gif" border="0" alt="Wrong">';
					var localeError=getLocaleBasedStartDateError();
					$("#startDateValTxt").html("<span class='nodata-error'>"+localeError+"</span>");
					document.getElementById("internalStartDate").value=data;
				}
			}
		}	
	
}

function addDate(dateid)
{	
	document.getElementById("internalStartDate").value=$.format.date(new Date(), "MM/dd/yyyy");
	document.getElementById(dateid).value=getFormattedPublishedDate(new Date());
}

function setDate(dateString,field) {
	  var dateField=document.getElementById(field);
      dateField.value = dateString;
      if(field=='strtdatevalidate')
		  showLocaleDate(dateField.value,'startDate');
      else
    	  showLocaleDate(dateField.value,'endDate');
	  validateEndDate();
    return;
  }

function showLocaleDate(dateField,textBoxId){
	
	if(textBoxId=='startDate'){
		document.getElementById("internalStartDate").value=dateField;
		setEndDate();		
	}
	else{
		document.getElementById("internalEndDate").value=dateField;
	}
	document.getElementById(textBoxId).value=getFormattedPublishedDate(dateField);
		
	}
