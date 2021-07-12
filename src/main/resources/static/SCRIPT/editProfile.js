$(document).ready(function () {

	var $modal = $('#modal');

	$('BUTTON.deleteAccount').click(function () {
		$modal.modal('show');
	});

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

	$('FORM').submit(function (event) {
		event.preventDefault();
		let form = this;

		$.post(
			$(form).attr('action'),
			$(form).serialize(),
			function (data) {
				if (data.response) {
					location.href = data.redirect;
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('INPUT.service').change(function () {
		let input = this;
		$.post(
			'service.json',
			{
				service: $(this).val()
			},
			function (data) {
				if (data.response) {
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('INPUT.location').change(function () {
		let input = this;
		$.post(
			'location.json',
			{
				location: $(this).val()
			},
			function (data) {
				if (data.response) {
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('INPUT[type="file"]').change(function (event) {
		event.preventDefault();
		let file = this;

		var formData = new FormData();
		formData.append('file', $(file).prop('files')[0]);

		$.ajax({
			url: "/uploadIdentity",
			type: 'POST',
			data: formData,
			enctype: 'multipart/form-data',
			processData: false,
			contentType: false,
			cache: false,
			success: function (data) {
				$('.toast-body').html(data.reason);
				$('.toast').toast('show');
			},

			error: function (data) {
				$('.toast-body').html(data.reason);
				$('.toast').toast('show');
			}
		});
		return false;
	});
});