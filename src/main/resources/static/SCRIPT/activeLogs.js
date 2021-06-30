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
});