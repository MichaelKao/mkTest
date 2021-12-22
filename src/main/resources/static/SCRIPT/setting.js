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
				oldPassword: $('INPUT[name="oldPassword"]').val(),
				newPassword: $('INPUT[name="newPassword"]').val(),
				confirmNewPassword: $('INPUT[name="confirmNewPassword"]').val()
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

	$('BUTTON.unblock').click(function () {
		event.preventDefault();
		let btn = this;
		$.post(
			'/unblock.json',
			{
				blockedIdentifier: $(btn).attr('id')
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html('解除封鎖成功');
					$('.toast').toast('show');
					$(btn).closest('DIV.badge').remove();
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$("SPAN.togglePwd").click(function (e) {
		e.preventDefault();
		var span = this;
		var $input = $(span).siblings('INPUT');
		var $icon = $(span).find('I');
		if ($input.attr('type') == 'password') {
			$input.attr('type', 'text');
			$icon.attr('class', 'fad fa-eye-slash');
		} else {
			$input.attr('type', 'password');
			$icon.attr('class', 'fad fa-eye');
		}
	});
});