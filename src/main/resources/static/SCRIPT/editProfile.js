$(document).ready(function () {

	$('#datepickerUntil').datetimepicker({
		format: "YYYY-MM-DD",
		maxDate: new Date().setHours(0, 0, 0, 0),
		useCurrent: false,
		icons: {
			previous: 'fal fa-chevron-left',
			next: 'fal fa-chevron-right'
		},
		ignoreReadonly: true
	});
});