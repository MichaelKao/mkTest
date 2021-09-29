$(document).ready(function () {

	$('BUTTON.success').dblclick(function (e) {
		e.preventDefault();
	});
	$('BUTTON.success').click(function () {
		let btn = this;
		$.post(
			'/dashboard/stopRecurring.json',
			{
				applyID: $(btn).siblings('INPUT[name="applyID"]').val()
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