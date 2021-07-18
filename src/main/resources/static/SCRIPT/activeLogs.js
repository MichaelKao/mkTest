$(document).ready(function () {
	$('BUTTON.accept').click(function (event) {
		event.preventDefault();
		var whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();

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
		var whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();

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
	var whom;
	var commentBtn;

	$('.requestLine').click(function () {
		$modal.modal('show');
		whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();
	});
	$('.rate').click(function () {
		$rateModal.modal('show');
		whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();
		commentBtn = $(this);
	});

	$('BUTTON.requestLineBtn').click(function (event) {
		event.preventDefault();
		$.post(
			'/stalking.json',
			{
				whom: whom,
				what: $('TEXTAREA[name="what"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
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

	$('BUTTON.commentBtn').click(function (event) {
		event.preventDefault();
		var rate = $('INPUT[name="rating"]:checked').val();
		if (rate === undefined) {
			rate = null;
		}
		$.post(
			'/rate.json',
			{
				whom: whom,
				rate: rate,
				comment: $('TEXTAREA[name="comment"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$rateModal.modal('hide');
					commentBtn.css('display', 'none');
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('BUTTON.openLine').click(function () {
		whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();
		$.post(
			'/maleOpenLine.json',
			{
				whom: whom
			},
			function (data) {
				if (data.response) {
					location.href = data.redirect;
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