$(document).ready(function () {

	let p;
	let type;
	let wrap;

	if (isMobile()) {
		if ($('INPUT[name="gender"]').val() === 'male') {
			$('A.reliefA').addClass('active');
			document.querySelector("DIV.mobileMode").innerHTML = document.querySelector("DIV.relief").innerHTML;
		} else {
			$('A.vipA').addClass('active');
			document.querySelector("DIV.mobileMode").innerHTML = document.querySelector("DIV.vip").innerHTML;
		}
	}

	$('A.mobileModeA').click(function () {
		let a = this;
		type = $(a).data('type');
		$('BUTTON#mobileMode').data('type', type);
		$('BUTTON#mobileMode').data('page', 0);
		wrap = $('DIV.mobileMode');
		$.post(
			'/seeMoreLover.json',
			{
				p: 0,
				type: type
			},
			(data) => {
			if (data.response) {
				$('A.mobileModeA').each(function (i, obj) {
					$(this).removeClass('active');
				});
				$(a).addClass('active');
				wrap.empty();
				data.result.forEach(function (item) {
					let outterA = document.createElement('A');
					$(outterA).attr({
						'class': 'position-relative m-1',
						'href': '/profile/' + item.identifier + '/'
					});
					wrap.append(outterA)

					let IMG = document.createElement('IMG');
					$(IMG).attr({
						'class': 'border-radius-md',
						'src': item.profileImage,
						'width': '152'
					});
					$(outterA).append(IMG);

					let iconDiv = document.createElement('DIV');
					$(iconDiv).attr({
						'class': 'position-absolute top-0 right-0 text-center',
						'style': 'width: 25px;'
					});
					$(outterA).append(iconDiv);

					if (item.vip) {
						let vipImg = document.createElement('IMG');
						$(vipImg).attr({
							'class': 'border-radius-md',
							'src': '/vip.svg',
							'width': '25'
						});
						$(iconDiv).append(vipImg);
					}

					if (item.relief) {
						let reliefImg = document.createElement('IMG');
						$(reliefImg).attr({
							'class': 'border-radius-md',
							'src': '/accept.svg',
							'width': '25'
						});
						$(iconDiv).append(reliefImg);
					}

					let infoDiv = document.createElement('DIV');
					$(infoDiv).attr('class', 'position-absolute bottom-0 right-0 d-flex text-light text-bold');
					$(outterA).append(infoDiv);

					let infoSpan = document.createElement('SPAN');
					$(infoSpan).attr('class', 'bg-dark opacity-6 border-radius-md px-1');
					$(infoSpan).html(item.nickname + ' ' + item.age);
					$(infoDiv).append(infoSpan);
				});
			}
		},
			'json'
			);
		return false;
	});

	$('BUTTON.seeMoreBtn').click(function () {
		let button = this;
		p = $(button).data('page');
		type = $(button).data('type');
		if ($(button).attr('id') === 'mobileMode') {
			wrap = $('DIV.mobileMode');
		} else {
			wrap = $('DIV.' + type);
		}
		$.post(
			'/seeMoreLover.json',
			{
				p: parseInt(p) + 1,
				type: type
			},
			(data) => {
			if (data.response) {
				wrap.empty();
				data.result.forEach(function (item) {
					let outterA = document.createElement('A');
					$(outterA).attr({
						'class': 'position-relative m-1',
						'href': '/profile/' + item.identifier + '/'
					});
					wrap.append(outterA)

					let IMG = document.createElement('IMG');
					$(IMG).attr({
						'class': 'border-radius-md',
						'src': item.profileImage,
						'width': '152'
					});
					$(outterA).append(IMG);

					let iconDiv = document.createElement('DIV');
					$(iconDiv).attr({
						'class': 'position-absolute top-0 right-0 text-center',
						'style': 'width: 25px;'
					});
					$(outterA).append(iconDiv);

					if (item.vip) {
						let vipImg = document.createElement('IMG');
						$(vipImg).attr({
							'class': 'border-radius-md',
							'src': '/vip.svg',
							'width': '25'
						});
						$(iconDiv).append(vipImg);
					}

					if (item.relief) {
						let reliefImg = document.createElement('IMG');
						$(reliefImg).attr({
							'class': 'border-radius-md',
							'src': '/accept.svg',
							'width': '25'
						});
						$(iconDiv).append(reliefImg);
					}

					let infoDiv = document.createElement('DIV');
					$(infoDiv).attr('class', 'position-absolute bottom-0 right-0 d-flex text-light text-bold');
					$(outterA).append(infoDiv);

					let infoSpan = document.createElement('SPAN');
					$(infoSpan).attr('class', 'bg-dark opacity-6 border-radius-md px-1');
					$(infoSpan).html(item.nickname + ' ' + item.age);
					$(infoDiv).append(infoSpan);
				});
			}
			if (data.lastPage) {
				$(button).data('page', -1);
			} else {
				$(button).data('page', parseInt(p) + 1);
			}
		},
			'json'
			);
		return false;
	});

	function isMobile() {

		var mobileAgents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
		var mobileFlag = false;

		// 根據 userAgent 判斷是否是手機
		for (var v = 0; v < mobileAgents.length; v++) {
			if (navigator.userAgent.indexOf(mobileAgents[v]) > 0) {
				mobileFlag = true;
				break;
			}
		}
		var screenWidth = $(window).width();
		var screenHeight = $(window).height();

		// 根據 screen 判斷
		if (screenWidth < 769 && screenHeight < 800) {
			mobileFlag = true;
		}

		return mobileFlag;
	}
});