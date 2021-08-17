$(document).ready(function () {

	$('BUTTON.stopRecurring').click(function () {
		$(this).attr('disabled', true);
		$.post(
			'/stopRecurring.json',
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
	});
});