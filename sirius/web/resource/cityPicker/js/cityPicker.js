/**
 * Created by burni on 2017/7/4.
 */
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        define('ChineseDistricts', [], factory);
    } else {
        factory(jQuery);
    }
})(function($){
    var CityPicker = {
        provinceCode:"",
        provinceName:"",
        cityCode:"",
        cityName:"",
        districtCode:"",
        districtName:"",
        province:[],
        city:{},
        district:{},
        show: function(option){
            var $pickerMain = $(".picker-main");
            if($pickerMain.length == 0){
                CityPicker.option = option;
                event.stopPropagation();
                var picker = "<div class='picker-main' onclick='event.stopPropagation()' style='top:"+option.target.offsetHeight+"px;left:"+option.target.offsetLeft+"px'>" +
                    "<ul class='picker-header'>" +
                    "</ul>" +
                    "<div><ul class='picker-content'></ul></div>"+
                    "</div>";
                if($pickerMain.length == 0){
                    $(option.target).after(picker);
                }

                var header = "";
                if(option.level == 1){
                    header = "<li class='picker-header-active'>省份</li>";
                }else if(option.level == 2){
                    header = "<li class='province picker-header-active' onclick='CityPicker.showProvince()'>省份</li>" +
                        "<li class='city'>城市</li>";

                }else if(option.level == 3){
                    header = "<li class='picker-header-active' onclick='CityPicker.showProvince()'>省份</li>" +
                        "<li class='city' onclick='CityPicker.showCity()'>城市</li>" +
                        "<li class='district'>区县</li>";
                }
                $(".picker-header").append(header);

                //加载数据
                if(!CityPicker.province.length){
                    CityPicker.province = getData(option.url[0]);
                }
                loadProvince();
            }


        },
        loadCity: function(el){
            event.stopPropagation();
            CityPicker.provinceCode = el.id;
            CityPicker.provinceName = el.innerText;
            var $city = $(".city");
            if($city.length > 0 ){
                $(".picker-header-active").removeClass("picker-header-active");
                $city.addClass("picker-header-active");
                var pickerContent = $(".picker-content");
                pickerContent.html("");
                var city = CityPicker.city;
                if(!city[el.id]){
                    city[el.id] = getData(CityPicker.option.url[1]+"/"+CityPicker.provinceCode);
                }

                for(var i=0;i<city[el.id].length;i++){
                    var item = city[el.id][i];
                    var li = "<li id='"+item.code+"' onclick='CityPicker.loadDistrict(this)'>"+item.cityName+"</li>";
                    pickerContent.append(li);
                }
            }else{
                destroyPicker();
                CityPicker.option.target.value = CityPicker.provinceName+(CityPicker.cityName?"/"+CityPicker.cityName:"")+(CityPicker.districtName?"/"+CityPicker.districtName:"");
            }
        },
        loadDistrict: function(el) {
            event.stopPropagation();
            CityPicker.cityCode = el.id;
            CityPicker.cityName = el.innerText;
            var $district = $(".district");
            if($district.length > 0 ){
                $(".picker-header-active").removeClass("picker-header-active");
                $district.addClass("picker-header-active");
                var pickerContent = $(".picker-content");
                pickerContent.html("");
                var district = CityPicker.district;
                if(!district[el.id]){
                    district[el.id] = getData(CityPicker.option.url[2]+"/"+CityPicker.cityCode);
                }
                for(var i=0;i<district[el.id].length;i++){
                    var item = district[el.id][i];
                    var li = "<li id='"+item.code+"' onclick='CityPicker.chooseOver(this)'>"+item.name+"</li>";
                    pickerContent.append(li);
                }
            }else{
                destroyPicker();
                CityPicker.option.target.value = CityPicker.provinceName+(CityPicker.cityName?"/"+CityPicker.cityName:"")+(CityPicker.districtName?"/"+CityPicker.districtName:"");
            }
        },
        chooseOver: function(el) {
            CityPicker.districtCode = el.id;
            CityPicker.districtName = el.innerText;
            destroyPicker();
            CityPicker.option.target.value = CityPicker.provinceName+(CityPicker.cityName?"/"+CityPicker.cityName:"")+(CityPicker.districtName?"/"+CityPicker.districtName:"");
        },
        showProvince: function() {
            event.stopPropagation();
            var $active = $(".picker-header-active");
            if($active.text() != "省份"){
                $active.removeClass("picker-header-active");
                $(".province").addClass("picker-header-active");
                $(".picker-content").html("");
                loadProvince();
            }
        },
        showCity: function (){
            event.stopPropagation();
            var $city = $(".city");
            $(".picker-header-active").removeClass("picker-header-active");
            $city.addClass("picker-header-active");
            var pickerContent = $(".picker-content");
            pickerContent.html("");
            var city = CityPicker.city;
            if(!city[CityPicker.provinceCode]){
                city[CityPicker.provinceCode] = getData(CityPicker.option.url[1]+"/"+CityPicker.provinceCode);
            }

            for(var i=0;i<city[CityPicker.provinceCode].length;i++){
                var item = city[CityPicker.provinceCode][i];
                var li = "<li id='"+item.code+"' onclick='CityPicker.loadDistrict(this)'>"+item.name+"</li>";
                pickerContent.append(li);
            }
        }


    };

    function getData(url, data){
        var res=null;
        $.ajax({
            url:url,
            data:data,
            dataType:"json",
            type: "GET",
            async:false,
            success: function(re) {
                res = re;
            }
        });
        if(res.code == 0){
            return res.data;
        }else{
            return [];
        }
    }

    function loadProvince(){
        for(var i=0;i<CityPicker.province.length;i++){
            var item = CityPicker.province[i];
            var li = "<li id='"+item.code+"' onclick='CityPicker.loadCity(this)'>"+item.provinceName+"</li>";
            $('.picker-content').append(li);
        }
    }

    function showProvince(){
        event.stopPropagation();
        var $active = $(".picker-header-active");
        if($active.text() != "省份"){
            $active.removeClass("picker-header-active");
            $(".province").addClass("picker-header-active");
            $(".picker-content").html("");
            loadProvince();
        }
    }

    document.addEventListener("click",function(){
        destroyPicker();
    });

    function destroyPicker(){
        var picker = $(".picker-main");
        if(picker.length > 0){
            picker.remove();
        }
    }

    if(typeof window !== 'undefined'){
        window.CityPicker = CityPicker;
    }
});