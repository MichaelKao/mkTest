$(document).ready(function () {

	$.post(
		'/peek.json',
		{
			whom: $('INPUT[name="whom"]').val()
		});

	$('BUTTON.fav').click(function () {
		$(this).toggleClass('liked');
		$.post(
			'/favorite.json',
			{
				identifier: $('INPUT[name="whom"]').val()
			},
			(data) => {
			if (data.response) {
				$('.toast-body').html(data.reason);
				$('.toast').toast('show');
			}
		},
			'json'
			);

	});

	var $giftModal = $('#giftModal');
	var $modal = $('#modal');
	var url;

	$('.gift').click(function () {
		$giftModal.modal('show');
		url = '/fare.json';
	});

	$('A#giveMeLine').on("click", function () {
		$modal.modal('show');
		url = '/stalking.json';
	});

	$('A#greeting').on("click", function () {
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
				console.log(data)
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