$(document).ready(function () {
	$('#cellularPhoneNumber').keyup(function () {
		let that = this;
		$(this).val(
			$(this).val().replace(/\D/gi, '')
		);
		$('INPUT[name="username"]').val(
			$('#country').val().replace(/\D/g, '') +
			$(this).val().replace(/^0/g, '')
		);
	});
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