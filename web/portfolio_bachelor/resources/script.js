$(document).ready(function(){	

	$(".project-link").click(function () {   
		$(".highlight").animate({width: "0px"}, 300);
		$(this).find(".highlight").animate({width: "15px"}, 300);
		
		div_id = $(this).attr("id");
		
		$(".content-projekt").hide(500);

		setTimeout(function(){
			$("#content-"+div_id).show(500);
		}, 520);
	});
	
});
