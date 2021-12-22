$(document).ready(function () {
	$('#cellularPhoneNumber').on('change', function () {
		let that = this;
		$(that).val(
			$(that).val().replace(/\D/gi, '')
			);
		$('INPUT[name="username"]').val(
			$('#country').val().replace(/\D/g, '') +
			$(that).val().replace(/^0/g, '')
			);
	});

	$('#cellularPhoneNumber').keyup(function () {
		let that = this;
		$(that).val(
			$(that).val().replace(/\D/gi, '')
			);
		$('INPUT[name="username"]').val(
			$('#country').val().replace(/\D/g, '') +
			$(that).val().replace(/^0/g, '')
			);
	});

	var isAndroid = /android/i.test(navigator.userAgent);
	var isSafari = !!navigator.userAgent.match(/Version\/[\d\.]+.*Safari/);
	var isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
	var isNotStandalone = !window.matchMedia('(display-mode: standalone)').matches;
	if (isIOS && isNotStandalone && isSafari) {
		$('DIV.iosAddToDesktop').css('display', 'block');
	}
	if (isAndroid && isNotStandalone) {
		$('DIV.androidAddToDesktop').css('display', 'block');
	}

	$('BUTTON.addDeskColse').click(function () {
		$('DIV.addToDeskTop').each(function () {
			$(this).css('display', 'none')
		});
	});
	$("SPAN.togglePwd").click(function (e) {
		e.preventDefault();
		var span = this;
		var $input = $(span).siblings('INPUT[name="password"]');
		var $icon = $(span).find('I');
		if ($input.attr('type') == 'password') {
			$input.attr("type", 'text');
			$icon.attr('class', 'fad fa-eye-slash');
		} else {
			$input.attr('type', 'password');
			$icon.attr('class', 'fad fa-eye');
		}
	});
});