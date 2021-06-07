$(document).ready(function () {
	$('INPUT[name="login"]').keyup(function () {
		let that = this;
		$(this).val(
			$(this).val().replace(/^0/g, '').replace(/\D/gi, '')
		);
	});
});