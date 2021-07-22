$(document).ready(function () {

	$('BUTTON.success').click(function () {
		let button = this;
		$.post(
			'/dashboard/withdrawalSuccess.json',
			{
				identifier: $(button).siblings('INPUT[name="identifier"]').val(),
				status: $(button).siblings('INPUT[name="status"]').val(),
				timestamp: $(button).siblings('INPUT[name="timestamp"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					window.location.reload();
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('BUTTON.fail').click(function () {
		let button = this;
		$.post(
			'/dashboard/withdrawalFail.json',
			{
				identifier: $(button).siblings('INPUT[name="identifier"]').val(),
				status: $(button).siblings('INPUT[name="status"]').val(),
				timestamp: $(button).siblings('INPUT[name="timestamp"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					window.location.reload();
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