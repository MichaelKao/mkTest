$(document).ready(function () {

	var id

	$('BUTTON.failReasonBtn').click(function () {
		var reason = $(this).closest('LI').find('DIV.failReasonHidden').text();
		$('DIV.reason').text(reason);
	});

	$('BUTTON.success').click(function () {
		let button = this;
		id = $(button).closest('UL').find('INPUT[name="id"]').val();
		$.post(
			'/dashboard/success.json',
			{
				id: id
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

	$('BUTTON.fail').click(function () {
		id = $(this).closest('UL').find('INPUT[name="id"]').val();
	});

	$('FORM.failForm').submit(function (event) {
		event.preventDefault();
		let form = this;
		$.post(
			$(form).attr('action'),
			{
				failReason: $('TEXTAREA[name="failReason"]').val(),
				id: id
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