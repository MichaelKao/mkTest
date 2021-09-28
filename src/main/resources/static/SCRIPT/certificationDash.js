$(document).ready(function () {

	var id;

	$('BUTTON.seePic').click(function () {
		let button = this;
		id = $(button).closest('TR').find('INPUT[name="id"]').val();
		$.post(
			'/dashboard/seeCetificationPic.json',
			{
				id: id
			},
			function (data) {
				if (data.response) {
					$('DIV.modal-body>IMG').attr('src', data.result);
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
		let button = this;
		id = $(button).closest('TR').find('INPUT[name="id"]').val();
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
		id = $(button).closest('TR').find('INPUT[name="id"]').val();
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