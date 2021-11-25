$(document).ready(function () {
	var vipType = $('SELECT[name="vipType"]').val();
	var login = $('INPUT[name="login"]').val().replace(/^0/g, '').replace(/\D/gi, '');

	searchData();

	$('FORM[method="post"]').submit(function (e) {
		e.preventDefault();
		vipType = $('SELECT[name="vipType"]').val();
		login = $('INPUT[name="login"]').val().replace(/^0/g, '').replace(/\D/gi, '');
		searchData();
	});

	function searchData() {
		$.post(
			'/dashboard/searchVip.json',
			{
				'vipType': vipType,
				'login': login
			},
			function (data) {
				$('.maleMembers').empty();
				if (data.reason) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				} else {
					data.forEach(function (item, index) {
						var wrapDiv = document.createElement('DIV');
						$(wrapDiv).attr('class', 'row text-center align-items-center py-2');
						$('.maleMembers').append(wrapDiv);
						if (index % 2 == 0) {
							$(wrapDiv).attr('class', 'row text-center align-items-center bg-light border-radius-xl py-2');
						}
						var infoDiv = document.createElement('DIV');
						$(infoDiv).attr('class', 'col-2 d-flex justify-content-start');
						$(wrapDiv).append(infoDiv);
						var infoA = document.createElement('A');
						$(infoA).attr({
							'class': 'd-flex flex-column align-items-start',
							'href': '/profile/' + item.maleIdentifier + '/'
						});
						$(infoDiv).append(infoA);
						var nameSpan = document.createElement('SPAN');
						$(nameSpan).attr('class', 'text-primary');
						$(nameSpan).append(item.nickname);
						$(infoA).append(nameSpan);
						var loginDiv = document.createElement('DIV');
						$(loginDiv).attr('class', 'text-secondary');
						$(loginDiv).append(item.login);
						$(infoA).append(loginDiv);
						var typeDiv = document.createElement('DIV');
						$(typeDiv).attr('class', 'col-1 d-flex justify-content-center');
						$(typeDiv).append(item.type)
						$(wrapDiv).append(typeDiv);
						var paidDateDiv = document.createElement('DIV');
						$(paidDateDiv).attr('class', 'col-3 d-flex justify-content-center');
						$(paidDateDiv).append(item.paidDate)
						$(wrapDiv).append(paidDateDiv);
						var startDateDiv = document.createElement('DIV');
						$(startDateDiv).attr('class', 'col-3 d-flex justify-content-center');
						$(startDateDiv).append(item.startDate)
						$(wrapDiv).append(startDateDiv);
						var endDateDiv = document.createElement('DIV');
						$(endDateDiv).attr('class', 'col-3 d-flex justify-content-center');
						$(endDateDiv).append(item.endDate);
						$(wrapDiv).append(endDateDiv);
					});
				}
			},
			'json'
			)
	}
});