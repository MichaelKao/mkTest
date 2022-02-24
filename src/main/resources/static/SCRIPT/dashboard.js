$(document).ready(function () {

	var hostName = location.hostname;

	const today = new Date();
	const currentYear = today.getFullYear();
	const currentMonth = today.getMonth() + 1;
	const currentDate = today.getDate();
	let sinceYear = currentYear;
	let sinceMonth = currentMonth - 1;

	if (currentMonth === 1) {
		sinceYear = currentYear - 1;
		sinceMonth = 12;
	}

	// 複製連結
	$('DIV#copyMemberLink').click(function () {
		var content =
			`${hostName}/dashboard/meMBERS.xls?since=${sinceYear}-${sinceMonth < 10 ? '0' + sinceMonth : sinceMonth}-01
				&until=${currentYear}-${currentMonth < 10 ? '0' + currentMonth : currentMonth}-${currentDate < 10 ? '0' + currentDate : currentDate}`;

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