$(document).ready(function () {
	$('INPUT[name="login"]').keyup(function () {
		let that = this;
		$(this).val(
			$(this).val().replace(/^0/g, '').replace(/\D/gi, '')
			);
	});

	$('FORM').submit(function (event) {
		event.preventDefault();
		if ($('INPUT[name="gender"]').val() === null) {
			$('.toast-body').html('請選擇您的性別');
			$('.toast').toast('show');
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