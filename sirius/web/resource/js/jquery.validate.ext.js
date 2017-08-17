$.fn.removeError = function(errorClass){
	$(this).tooltip("destroy");
}

$.fn.addError = function(message, fun){
	var next = $(this).next();
	if(next.hasClass("tooltip")){
		next.find(".tooltip-inner").html(message);
	}else{
		$(this).tooltip({
			title:message,
			trigger:"manual"
		});
		$(this).tooltip("show");
	}
}

$.validator.addMethod("reg", function(value,element,params){
	if(value != undefined && value.length > 0){
		if(params != undefined){
			return params.test(value);
		}else{
			return true;
		}
	}else{
		return true;
	}
});

$.validator.addMethod("cpca", function(value,element,params){
	if(value){
		var vdProvince = $(element).data("vd-province");
		var vdCity = $(element).data("vd-city");
		var vdArea = $(element).data("vd-area");
		if(vdProvince != undefined){
			var val = $("#" + vdProvince).val();
			if(val == undefined || $.trim(val) == ""){
				return false;
			}
		}
		if(vdCity != undefined){
			var val = $("#" + vdCity).val();
			if(val == undefined || $.trim(val) == ""){
				return false;
			}
		}
		if(vdArea != undefined){
			var val = $("#" + vdArea).val();
			if(val == undefined || $.trim(val) == ""){
				return false;
			}
		}
		return true;
	}
});