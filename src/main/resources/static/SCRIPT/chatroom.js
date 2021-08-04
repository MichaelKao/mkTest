$(document).ready(function () {
	$('BUTTON.locationBack').click(function () {
		if (!document.referrer || document.referrer === location.href || document.referrer.indexOf(location.origin) < 0) {
			location.href = '/';
		} else {
			window.history.back();
		}
	});
});