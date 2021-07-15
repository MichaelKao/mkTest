$(document).ready(function () {

	var id;

	$('BUTTON.seePic').click(function () {
		let button = this;
		id = $(button).closest('TBODY').find('INPUT[name="id"]').val();
		$('DIV.modal-body>IMG').attr('src', 'https://d35hi420xc5ji7.cloudfront.net/identity/' + id);
	});

	$('BUTTON.fail').click(function () {
		let button = this;
		id = $(button).closest('TBODY').find('INPUT[name="id"]').val();
		$.post(
			'/dashboard/identityFailed.json',
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

	$('BUTTON.success').click(function () {
		let button = this;
		id = $(button).closest('TBODY').find('INPUT[name="id"]').val();
		$.post(
			'/dashboard/identityPassed.json',
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
});