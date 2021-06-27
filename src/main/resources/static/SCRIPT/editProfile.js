$(document).ready(function () {

	var content = $("TEXTAREA#aboutMe").val();
	content = content.replace(/<br>/g, "\n");
	$('TEXTAREA#aboutMe').val(content);
	
	var content = $("TEXTAREA#idealConditions").val();
	content = content.replace(/<br>/g, "\n");
	$('TEXTAREA#idealConditions').val(content);

	$('#birth').datetimepicker({
		format: "YYYY-MM-DD",
		maxDate: new Date().setHours(0, 0, 0, 0),
		useCurrent: false,
		icons: {
			previous: 'fal fa-chevron-left',
			next: 'fal fa-chevron-right'
		},
		ignoreReadonly: true
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
});