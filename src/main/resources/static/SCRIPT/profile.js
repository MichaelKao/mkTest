$(document).ready(function () {

	$.post(
		'/peek.json',
		{
			whom: $('INPUT[name="whom"]').val()
		});

	var content = $("DIV.aboutMe").text();
	$('DIV.aboutMe').html(content);

	var content = $("DIV.idealConditions").text();
	$('DIV.idealConditions').html(content);

	$('BUTTON.fav').click(function () {
		$(this).toggleClass('liked')
	});

	var $giftModal = $('#giftModal');
	var $modal = $('#modal');
	var url;

	$('.gift').click(function () {
		$giftModal.modal('show');
		url = '/fare.json';
	});

	$('#giveMeLine').click(function () {
		$modal.modal('show');
		url = '/stalking.json';
	});
	
	$('#greeting').click(function () {
		$modal.modal('show');
		url = '/greet.json';
	});

	$('.confirmBtn').click(function (event) {
		event.preventDefault();
		let form = this;
		$.post(
			url,
			{
				howMany: $('INPUT[name="howMany"]').val(),
				whom: $('INPUT[name="whom"]').val(),
				what: $('TEXTAREA[name="what"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$giftModal.modal('hide');
					$modal.modal('hide');
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});
});