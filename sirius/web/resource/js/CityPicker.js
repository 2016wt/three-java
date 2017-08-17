function initPicker(e){
      var picker = $(".picker-container");
      if(!picker.length){
          var temp = "<div class='picker-container' style='position:absolute;top:"+ e.target.offsetHeight+"px;left:"+ e.target.offsetLeft+"px'>" +
				  	    "<div class='picker-top'>" +
					        "<ul>" +
					            "<li id='tab-province' class='picker-active' onclick='showPicker(event)'>省份</li>"+
					            "<li id='tab-city' class=''>城市</li>"+
					        "</ul>"+
					    "</div>"+
						"<div class='picker-s'>"+
							"<ul class='picker-ul'></ul>"+
						"<div>"+
					"</div>";
          $("body").append(temp);
      }
  };
  function showPicker(e){
	  e.stopPropagation();
	  $("#tab-province").attr({"class":"picker-active"});
      $("#tab-city").attr({"class":""});
      $(".picker-ul").html("");
      
      
      initPicker(e);
      if($(".picker-ul li").length==0){
    	  $.ajax({
    			url:"http://localhost:8080/address/provinceList",
    			dataType:"json",
    			async:false,
    			success:function(res){
    	    	   if(res.code=="0"){
		                 var array=res.data;
		                 var $ul=$(".picker-ul");
		                 for(var i=0;i<array.length;i++){
		                     var item=array[i];
		                     $ul.append("<li id='"+item.code+"' class='province' onclick='showCity(this)'>"+item.provinceName+"</li>");
		                 }
		             }else{
		                 alert("数据加载失败");
		             }
    	       } 
      });   
  }
  }
  function showCity(el){
	  event.stopPropagation();
      $("#tab-province").attr({"class":""});
      $("#tab-city").attr({"class":"picker-active"});
      var $el = $(el);
      $("#province-value").val(el.id);
      $("#district").val(el.innerText+"/");
      $.get("/address/cityList/"+$el.attr("id"),{},function(res){
          if(res.code == "0"){
              var array = res.data;
              var ul = $(".picker-ul");
              ul.html("");
              for(var i=0;i<array.length;i++){
                  var item = array[i];
                  var li = "<li id='"+item.cityCode+"' onclick='selectCity(this)'>"+item.cityName+"</li>";
                  ul.append(li);
              }
          }
      },"json");  
  }

  function selectCity(el){
      $("#picker-container").hide();
      $("#city-value").val(el.id);
      var input = $("#district");
      input.val(input.val()+el.innerText);
      $(".picker-container").remove();
  }	
  
  document.addEventListener("click",function(e){
	 if($(".picker-container").length){
		 $(".picker-container").remove();
	 }
  })