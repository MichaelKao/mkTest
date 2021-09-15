$(document).ready(function () {
	$('FORM.wireTransfer').submit(function (event) {
		event.preventDefault();
		let form = this;
		$.post(
			$(form).attr('action'),
			$(form).serialize(),
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

	$('BUTTON.returnFare').dblclick(function (e) {
		e.preventDefault();
	});
	$('BUTTON.returnFare').click(function (e) {
		e.preventDefault();
		var btn = this;
		$(btn).attr('disabled', true);
		$.post(
			'returnFare.json',
			{
				history: $(btn).siblings('INPUT[name="returnFareId"]').val()
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