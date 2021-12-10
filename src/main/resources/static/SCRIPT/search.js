$(document).ready(function () {

	var urlString = location.href;
	let loversWrap = $('DIV.loversWrap');
	let pageBtnWrap = $('DIV.pageBtnWrap');

	$('BUTTON.pageBtn').click(function () {
		let btn = this;
		page(btn);
	});

	function page(btn) {
		$.post(
			urlString,
			{
				'p': $(btn).data('page')
			},
			function (data) {
				loversWrap.empty();
				pageBtnWrap.empty();
				console.log(data)
				data.lovers.forEach(function (item) {
					let outterA = document.createElement('A');
					$(outterA).attr({
						'class': 'position-relative m-1',
						'href': '/profile/' + item.identifier + '/'
					});
					loversWrap.append(outterA);

					let IMG = document.createElement('IMG');
					$(IMG).attr({
						'class': 'border-radius-md width148whileMobile',
						'src': item.profileImage,
						'width': '152'
					});
					$(outterA).append(IMG);
					let iconDiv = document.createElement('DIV');
					$(iconDiv).attr({
						'class': 'position-absolute right-0 text-center',
						'style': 'width: 32px; top: 5px;'
					});
					$(outterA).append(iconDiv);
					if (item.vip) {
						let vipI = document.createElement('I');
						$(vipI).attr('class', 'fad fa-crown fontSize22 text-yellow text-shadow');
						$(iconDiv).append(vipI);
					}

					if (item.relief) {
						let reliefI = document.createElement('I');
						$(reliefI).attr('class', 'fas fa-shield-check fontSize22 text-success text-shadow');
						$(iconDiv).append(reliefI);
					}

					if (item.following) {
						let followingDiv = document.createElement('DIV');
						$(followingDiv).attr({
							'class': 'position-absolute left-0 text-center',
							'style': 'width: 32px; top: 5px;'
						});
						$(outterA).append(followingDiv);
						let followingI = document.createElement('I');
						$(followingI).attr('class', 'fas fa-heart-circle text-pink fontSize22');
						$(followingDiv).append(followingI);
					}

					let infoDiv = document.createElement('DIV');
					$(infoDiv).attr('class', 'position-absolute imageShadow bottom-0 left-0 right-0 mx-3 mb-1 py-0 text-bolder text-dark bg-white opacity-7 border-radius-md p-1 text-xs text-center');
					$(outterA).append(infoDiv);
					let nameAgeDiv = document.createElement('DIV');
					$(infoDiv).append(nameAgeDiv);
					let infoSpan = document.createElement('SPAN');
					$(infoSpan).html(item.nickname + ' ' + item.age);
					$(nameAgeDiv).append(infoSpan);
					if (item.relationship) {
						let relationshipDiv = document.createElement('DIV');
						$(infoDiv).append(relationshipDiv);
						let relationshipSpan = document.createElement('SPAN');
						$(relationshipSpan).html('尋找' + item.relationship);
						$(relationshipDiv).append(relationshipSpan);
					}
					let locationDiv = document.createElement('DIV');
					$(infoDiv).append(locationDiv);
					if (typeof (item.locations) != 'undefined') {
						item.locations.forEach(function (item, i, array) {
							console.log(array)
							console.log(i)
							if (i === array.length - 1) {
								let locationSpan = document.createElement('SPAN');
								$(locationSpan).html(item);
								$(locationDiv).append(locationSpan);
								return;
							}
							let locationSpan = document.createElement('SPAN');
							$(locationSpan).attr('class', 'me-1');
							$(locationSpan).html(item);
							$(locationDiv).append(locationSpan);
						});
					}
				});

				if (typeof (data.hasPrev) != 'undefined') {
					var prevBtn = document.createElement('BUTTON');
					$(prevBtn).attr({
						'class': 'btn btn-primary btn-round pageBtn mx-1 m-0 px-2 py-1',
						'data-page': data.hasPrev
					});
					pageBtnWrap.append(prevBtn);
					var prevI = document.createElement('I');
					$(prevI).attr('class', 'fad fa-angle-double-left ms-1');
					$(prevBtn).append(prevI);
					$(prevBtn).append('上一頁');
					$(prevBtn).click(function () {
						let btn = this;
						page(btn);
					});
				}
				if (typeof (data.hasNext) != 'undefined') {
					var nextBtn = document.createElement('BUTTON');
					$(nextBtn).attr({
						'class': 'btn btn-primary btn-round pageBtn mx-1 m-0 px-2 py-1',
						'data-page': data.hasNext
					});
					pageBtnWrap.append(nextBtn);
					$(nextBtn).append('下一頁');
					var nextI = document.createElement('I');
					$(nextI).attr('class', 'fad fa-angle-double-right ms-1');
					$(nextBtn).append(nextI);
					$(nextBtn).click(function () {
						let btn = this;
						page(btn);
					});
				}
			},
			'json'
			);
	}
});