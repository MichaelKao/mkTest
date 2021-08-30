$(document).ready(function () {
	$('BUTTON.confirmBtn').click(function () {
		$.post(
			'/deleteAccount',
			(data) => {
			if (data.response) {
				location.href = data.redirect;
			}
		},
			'json'
			);
	});

	$('BUTTON.resetPwdBtn').click(function () {
		event.preventDefault();
		let btn = this;
		$.post(
			'/password.json',
			{
				password: $('INPUT[name="password"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$('.toast').on('hidden.bs.toast', function () {
						location.href = '/';
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