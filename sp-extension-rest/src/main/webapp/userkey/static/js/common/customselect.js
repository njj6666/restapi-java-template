// Custom Select
$.fn.SelectCustomizer = function(){
    return this.each(function(){
        var obj = $(this);
		var name = obj.prop('id');
		var id_slc_options = name+'_options';
		var id_icn_select = name+'_iconselect';
		var id_holder = name+'_holder';
		var custom_select = name+'_customselect';
		var isSelected = false;
		
        obj.after("<div id=\""+id_slc_options+"\" class='customSelect'> </div>");
        obj.find('option').each(function(i){
        	var textTodisplay=$(this).html();
        	var mouseOverTxt="";
        	var textArr=new Array();
        	if(textTodisplay!=null && textTodisplay.indexOf("~")!=-1){
        		textArr=textTodisplay.split("~");
        		var text1=textArr[0];
        		var text2=textArr[1];
        		mouseOverTxt=text1+"-"+text2;        		
        		if(text1.length > 8) text1=text1.substr(0,5)+"...";
        		textTodisplay=text1+"-"+text2;
        		 $("#"+id_slc_options).append("<div id=\"" + $(this).prop("value") + "\" class=\"selectitems\"><span title='"+mouseOverTxt+"'>" + textTodisplay + "</span></div>");
        	}else{
        		if(textTodisplay.length > 29) { 
        			var wholeText=textTodisplay;
        			var textTodisplay=textTodisplay.substr(0,29)+"...";
        			$("#"+id_slc_options).append("<div id=\"" + $(this).prop("value") + "\" class=\"selectitems\"><span title='"+wholeText+"'>" + textTodisplay + "</span></div>");
        		}
        		else
        			$("#"+id_slc_options).append("<div id=\"" + $(this).prop("value") + "\" class=\"selectitems\"><span>" + textTodisplay + "</span></div>");
        		
        	}
        });
        obj.before("<input type=\"hidden\" value =\"\" name=\"" + this.name + "\" id=\""+custom_select+"\"/><div id=\""+id_icn_select+"\" class='selecticon'>" + this.title + "</div><div id=\""+id_holder+"\" class='selectholder'> </div>").remove();
		isSelected = false;
        $("#"+id_icn_select).click(function(){
        	if(isSelected){
				$("#"+id_holder).toggle(0);
				isSelected = false;
			}
			else{
				$("#"+id_holder).toggle(0);
				isSelected = true;
			}	
        });
        $("#"+id_holder).append($("#"+id_slc_options)[0]);
        $("#"+id_holder+ " .selectitems").mouseover(function(){
            $(this).addClass("hoverclass");
        });
        $("#"+id_holder+" .selectitems").mouseout(function(){
            $(this).removeClass("hoverclass");
        });
        $("#"+id_holder+" .selectitems").click(function(){
            $("#"+id_holder+" .selectedclass").removeClass("selectedclass");
            $(this).addClass("selectedclass");
            var thisselection = $(this).html();
            $("#"+custom_select).val(this.id);
            $("#"+id_icn_select).html(thisselection);
			isSelected = false;			
            $("#"+id_holder).toggle(0);
      });
		$('body').click(function(e) {
			var obj = (e.target ? e.target : e.srcElement);
			if (obj.id=='' && isSelected)
			{
				$("#"+id_holder).toggle(0);
				isSelected = false;
			}
					 
		});
		$('div').click(function(e) {
			var obj = (e.target ? e.target : e.srcElement);
			if (obj.className!='selecticon' && isSelected)
			{
				$("#"+id_holder).toggle(0);
				isSelected = false;
			}

		});												
    });
}