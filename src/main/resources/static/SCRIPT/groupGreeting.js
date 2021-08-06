$(document).ready(function () {
	$('FORM').submit(function (event) {
		event.preventDefault();
		console.log('招呼語: ' + $('TEXTAREA').val());
		let form = this;
		$.post(
			$(form).attr('action'),
			{
				greetingMessage: $('TEXTAREA').val()
			},
			function (data) {
				console.log(data);
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					if (data.redirect) {
						$('.toast').on('hidden.bs.toast', function () {
							location.href = data.redirect;
						});
					}
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