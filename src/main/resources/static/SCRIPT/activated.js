$(document).ready(function () {
	$('FORM').submit(function (event) {
		event.preventDefault();
		let form = this;
		$.post(
			$(form).attr('action'),
			$(form).serialize(),
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					if (data.redirect) {
						$('.toast').on('hidden.bs.toast', function () {
							fbq('track', 'CompleteRegistration');
							location.href = data.redirect;
						});
					}
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