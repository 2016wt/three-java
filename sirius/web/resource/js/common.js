function bootstrapTableParams(id, params) {
	var data = $(id).serializeArray(); // 自动将form表单封装成json
	var i = data.length;
	var int = 0;
	for ( var x in params) {
		data[i + int] = {
			name : x,
			value : params[x]
		};
		int++;

	}

	return data;
}

function startLoding() {
	$.blockUI({
		message : '<img height="100px" src="resource/image/timg.gif">'
	});
}
function stopLoding() {
	$.unblockUI();
}
function dateFormatting(arg) {
	return arg.substring(0, 19);
}