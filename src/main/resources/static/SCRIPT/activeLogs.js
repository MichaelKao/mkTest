$(document).ready(function () {
	$(window).scrollTop(0);

	var scrollCount = 0;
	var scrollCountLimit = 10;

	var timeout;
	window.addEventListener('scroll', handler);
	function handler() {
		clearTimeout(timeout);
		timeout = setTimeout(function () {
			if ($(window).scrollTop() + $(window).height() > $(document).height() - 300) {
				scrollCount += 1;
				var $nextPage = $('INPUT[name="nextPage"]');
				if (scrollCount >= scrollCountLimit) {

					$nextPage.val(-1);
					var div = document.createElement('DIV');
					$(div).attr('class', 'text-center text-xs mt-4');
					$(div).append('沒有通知囉！');
					$('.activities').append(div);
					window.removeEventListener('scroll', handler);
					return;
				}
				window.removeEventListener('scroll', handler);
				var p = $nextPage.val();
				var load = document.createElement('DIV');
				$(load).attr('class', 'loadMore');
				$('.activities').append(load);
				if (p > 0) {
					$.post(
						'/loadMoreActivities.json',
						{
							p: p
						},
						function (data) {
							$(load).remove();
							if (data.length !== 0) {
								$nextPage.val(parseInt(p) + 1);
								appendData(data);
								window.addEventListener('scroll', handler);
							} else if (data.length == 0) {
								$nextPage.val(-1);
								var div = document.createElement('DIV');
								$(div).attr('class', 'text-center text-xs mt-4');
								$(div).append('沒有通知囉！');
								$('.activities').append(div);
								window.removeEventListener('scroll', handler);
							}
						},
						'json'
						);
				}
			}
		}, 50);
	}

	function appendData(data) {
		data.forEach(function (activity) {
			var cardDiv = document.createElement('DIV');
			$(cardDiv).attr('class', 'card card-frame mb-2');
			$('.activities').append(cardDiv);
			var cardBodyDiv = document.createElement('DIV');
			$(cardBodyDiv).attr('class', 'card-body d-flex align-items-center justify-content-start py-2');
			$(cardDiv).append(cardBodyDiv);
			var profileDiv = document.createElement('DIV');
			$(cardBodyDiv).append(profileDiv);
			var profileA = document.createElement('A');
			$(profileA).attr('href', activity.identifier);
			$(profileDiv).append(profileA);
			var profileImg = document.createElement('IMG');
			$(profileImg).attr({
				'alt': 'profile_photo',
				'src': activity.profileImage,
				'width': '55'
			});
			$(profileA).append(profileImg);
			var msgMainDiv = document.createElement('DIV');
			$(msgMainDiv).attr('class', 'ms-4 w-100 d-flex flex-column flex-md-row');
			$(cardBodyDiv).append(msgMainDiv);
			var div = document.createElement('DIV');
			$(msgMainDiv).append(div);
			var timeSpan = document.createElement('SPAN');
			$(timeSpan).attr('class', 'text-xs font-weight-bold my-2');
			$(timeSpan).append(activity.time);
			$(div).append(timeSpan);
			var msgDiv = document.createElement('DIV');
			$(msgDiv).attr('class', 'text-dark text-xs text-bold');
			$(msgDiv).append(activity.message);
			$(div).append(msgDiv);

			if (activity.pixAuthBtn) {
				var pixAuthBtnDiv = document.createElement('DIV');
				$(pixAuthBtnDiv).attr('class', 'ms-0 ms-md-auto d-flex align-items-center my-1');
				$(msgMainDiv).append(pixAuthBtnDiv);
				var input = document.createElement('INPUT');
				$(input).attr({
					'name': 'whom',
					'type': 'hidden',
					'value': activity.identifier
				});
				$(pixAuthBtnDiv).append(input);
				var pixAuthBtn = document.createElement('BUTTON');
				$(pixAuthBtn).attr({
					'class': 'btn btn-sm btn-outline-primary px-2 py-1 p-md-2 m-0 me-1 acceptPixAuth',
					'type': 'button'
				});
				$(pixAuthBtn).append('同意');
				$(pixAuthBtnDiv).append(pixAuthBtn);
				$(pixAuthBtn).dblclick(function (e) {
					e.preventDefault();
				});
				$(pixAuthBtn).click(function (event) {
					event.preventDefault();
					let btn = this;
					pixAuth(btn);
				});
			}
		});
	}
});