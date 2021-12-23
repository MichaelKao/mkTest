$(document).ready(function () {

	var hostName = location.hostname;
	// 複製連結
	$('DIV#copyMemberLink').click(function () {
		var content = hostName + '/dashboard/meMBERS.xls?since=2021-01-01&until=2021-11-01';

		navigator.clipboard.writeText(content)
			.then(() => {
				$('.toast-body').html('已複製');
				$('.toast').toast('show');
			})
			.catch(err => {
				console.log('Something went wrong', err);
			})
	});
});