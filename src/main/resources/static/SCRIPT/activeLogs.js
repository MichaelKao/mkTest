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

	$('.requestLine').click(function () {
		$modal.modal('show');
		url = '/stalking.json';
	});
	$('.rate').click(function () {
		$rateModal.modal('show');
		url = '/rate.json';
	});


	$('BUTTON.confirmBtn').click(function (event) {
		var whom = $('INPUT[name="whom"]').val();
		var what = $('TEXTAREA[name="what"]').val();
		var rate = $('INPUT[name="rating"]:checked').val();
		var comment = $('TEXTAREA[name="comment"]').val()
		console.log(url)
		console.log('whom', $('INPUT[name="whom"]').val())
		console.log('what', )
		console.log('rate', $('INPUT[name="rating"]:checked').val())
		console.log('comment', $('TEXTAREA[name="comment"]').val())
		event.preventDefault();
		let confirmBtn = this;
		$.post(
			url,
			{
				whom: $('INPUT[name="whom"]').val(),
				what: $('TEXTAREA[name="what"]').val(),
				rate: $('INPUT[name="rating"]:checked').val(),
				comment: $('TEXTAREA[name="comment"]').val(),
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