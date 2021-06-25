$(document).ready(function () {
	
	var content = $("DIV.aboutMe").text();
	$('DIV.aboutMe').html(content);
	
	var content = $("DIV.idealConditions").text();
	$('DIV.idealConditions').html(content);
	
	$('BUTTON.fav').click(function () {
		$(this).toggleClass('liked')
	});
});