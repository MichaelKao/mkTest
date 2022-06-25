$(document).ready(function () {
	$('INPUT.loginInput').keyup(function () {
		let that = this;
		$('INPUT[name="login"]').val(
			$(that).val().replace(/^0/g, '').replace(/\D/gi, '')
			);
	});

	$('FORM').submit(function (event) {
		event.preventDefault();
		if (!$('INPUT.loginInput').val().match(/^09[0-9]{8}$/) && !$('INPUT.loginInput').val().match(/^9[0-9]{8}$/)) {
			$('.toast-body').html('手機格式不符；例：0912345678');
			$('.toast').toast('show');
			return;
		}
		if ($('INPUT[name="gender"]').val() === null) {
			$('.toast-body').html('請選擇您的性別');
			$('.toast').toast('show');
			return;
		}

		let form = this;
		$('INPUT[name="birthday"]').val($('#years').val() + '-' + $('#months').val() + '-' + $('#days').val());
		$.post(
			$(form).attr('action'),
			$(form).serialize(),
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$('.toast').on('hidden.bs.toast', function () {
						location.href = data.redirect;
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