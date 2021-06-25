$(document).ready(function () {
	
	var content = $("DIV.introduction").text();
	$('DIV.introduction').html(content);
	
	var content = $("DIV.idealType").text();
	$('DIV.idealType').html(content);
	
	$('BUTTON.fav').click(function () {
		$(this).toggleClass('liked')
	});
});