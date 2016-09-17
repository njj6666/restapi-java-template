var popupMenuNS={
		sortFilterMenu:"",		
};
function openSortFilterMenu(divId){//To open Sort or Filter menu in navigation panel
	var menu= document.getElementById(divId);
	if(menu.style.visibility=='visible')
		$('#'+divId).css("visibility","hidden");
	else{
		$('#'+divId).css("visibility","visible");
		verifyAndSetDivstyle(divId);
	}
	
}
function closeSortFilterMenu(divId){//To close the specific sort/filter menu in navigation panel
	$('#'+divId).css("visibility","hidden");
}
function closeAllFilterMenu(){ //To close all the filter menus
	var availableDiv=['sorting','filtering','multiActionDiv','multiActionDivApp','dateSorting','dateSortingApp'];
	for(var i=0;i<availableDiv.length;i++){		
			$('#'+availableDiv[i]).css("visibility","hidden");
	}
}
function closeOpenedMenu(){//When clicking any where else in the document other than the opened div
	$('#'+popupMenuNS.sortFilterMenu).css("visibility","hidden");
	popupMenuNS.sortFilterMenu='';
}
function verifyAndSetDivstyle(divId){
	var availableDiv=['sorting','filtering','multiActionDiv','multiActionDivApp','dateSorting','dateSortingApp'];
	for(var i=0;i<availableDiv.length;i++){
		if(availableDiv[i]!=divId)
			$('#'+availableDiv[i]).css("visibility","hidden");
	}
}
function cancelOpenedMenu(){
	popupMenuNS.sortFilterMenu='';
}
function trackOpenSortFilterMenu(divId){//Track the opened filter/sort menu
	popupMenuNS.sortFilterMenu=divId;
}

// Track and Close all Open pop-ups when clicked anywhere in the document

$(document).ready(function(){
	$(document).click(function(event){
		var elClass = $(event.target).prop('class');
		var elTagName = $(event.target)[0].localName;
		if(elClass!='sortFilterSection' && elTagName!='dfn'){
			closeAllFilterMenu();
		}
	});
});
