$(document).ready(function () {
	$('BUTTON.accept').click(function (event) {
		event.preventDefault();
		var whom = $(this).closest('DIV.buttons').find('INPUT').val();

		$.post(
			"/stalked.json",
			{
				whom: whom
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$('.toast').on('hidden.bs.toast', function () {
						window.location.reload();
					});
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					if (data.redirect) {
						$('.toast').on('hidden.bs.toast', function () {
							location.href = data.redirect;
						});
					}
				}
			},
			'json'
			);
		return false;
	});
	$('BUTTON.refuse').click(function (event) {
		event.preventDefault();
		var whom = $(this).closest('DIV.buttons').find('INPUT').val();

		$.post(
			"/notStalked.json",
			{
				whom: whom
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$('.toast').on('hidden.bs.toast', function () {
						window.location.reload();
					});
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	var $modal = $('#modal');
	var $rateModal = $('#rateModal');
	var url;
	var whom;

	$('.requestLine').click(function () {
		$modal.modal('show');
		url = '/stalking.json';
		whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();
	});
	$('.rate').click(function () {
		$rateModal.modal('show');
		url = '/rate.json';
		whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();
	});

	$('BUTTON.confirmBtn').click(function (event) {
		event.preventDefault();
		var rate = $('INPUT[name="rating"]:checked').val();
		if (rate === undefined) {
			rate = null;
		}
		console.log(whom)
		console.log(rate)
		$.post(
			url,
			{
				whom: whom,
				what: $('TEXTAREA[name="what"]').val(),
				rate: rate,
				comment: $('TEXTAREA[name="comment"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$modal.modal('hide');
					$rateModal.modal('hide');
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