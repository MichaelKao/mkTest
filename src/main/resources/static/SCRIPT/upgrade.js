$(document).ready(function () {
	
	fbq('track', 'vippageview');
	
	$('BUTTON.stopRecurring').click(function () {
		$(this).attr('disabled', true);
		$.post(
			'/stopRecurring.json',
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
	});

	$('BUTTON.trialBtn').click(function () {
		$(this).addClass('d-none');
		$('DIV.trialInput').removeClass('d-none');
		$('DIV.trialInput').addClass('d-flex');
	});

	$('BUTTON.confirmTrialCode').click(function () {
		var btn = this;
		$(btn).attr('disabled', true);
		$.post(
			'/trial.json',
			{
				code: $('INPUT[name="code"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					window.location.reload();
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$(btn).attr('disabled', false);
				}
			},
			'json'
			);
	});
});