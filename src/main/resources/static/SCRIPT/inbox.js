$(document).ready(function () {
	var timeout;
	window.addEventListener('scroll', handler);
	function handler() {
		clearTimeout(timeout);
		timeout = setTimeout(function () {
			if ($(window).scrollTop() + $(window).height() > $(document).height() - 300) {
				window.removeEventListener('scroll', handler);
				var $nextPage = $('INPUT[name="nextPage"]');
				var p = $nextPage.val();
				var load = document.createElement('DIV');
				$(load).attr('class', 'loadMore');
				$('#chatList').append(load);
				if (p > 0) {
					$.post(
						'/loadMoreInboxList.json',
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
								$(div).append('沒有更多訊息囉！');
								$('#chatList').append(div);
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
		data.forEach(function (item) {
			var conversationDiv = document.createElement('DIV');
			$(conversationDiv).attr('class', 'conversationWrap position-relative');
			if (item.isMatchedOrIsVip === 'true') {
				$('#chatList').append(conversationDiv);
			} else {
				$('#chatList').append(conversationDiv);
			}
			var linkA = document.createElement('A');
			$(linkA).attr({
				'class': 'inboxLink',
				'href': '/chatroom/' + item.identifier + '/'
			});
			$(conversationDiv).append(linkA);
			var contentDiv = document.createElement('DIV');
			$(contentDiv).attr('class', 'd-flex justify-content-between align-items-center p-2');
			$(conversationDiv).append(contentDiv);
			var imgDiv = document.createElement('DIV');
			$(imgDiv).attr('class', 'position-relative');
			$(contentDiv).append(imgDiv);
			if (item.isMatchedOrIsVip === 'true') {
				var icon = document.createElement('I');
				$(icon).attr({
					'class': 'fad text-shadow position-absolute',
					'style': 'right: -5px;bottom: 1px;'
				});
				if ($('INPUT[name="gender"]').val() == 'true') {
					$(icon).addClass('fa-users text-primary');
				}
				if ($('INPUT[name="gender"]').val() == 'false') {
					$(icon).addClass('fa-crown text-yellow');
				}
				$(imgDiv).append(icon);
			}
			var img = document.createElement('IMG');
			$(img).attr({
				'alt': '大頭照',
				'class': 'rounded-circle shadow',
				'src': item.profileImage,
				'width': '60px'
			});
			$(imgDiv).append(img);
			var nameAndMsgDiv = document.createElement('DIV');
			$(nameAndMsgDiv).attr({
				'class': 'me-auto',
				'style': 'overflow: hidden'
			});
			$(contentDiv).append(nameAndMsgDiv);
			var nameAndMsgSubDiv = document.createElement('DIV');
			$(nameAndMsgSubDiv).attr('class', 'd-flex flex-column align-items-start ms-3');
			$(nameAndMsgDiv).append(nameAndMsgSubDiv);
			var nameA = document.createElement('A');
			$(nameA).attr('class', 'font-weight-bold text-dark text-sm mb-1');
			$(nameA).append(item.nickname);
			$(nameAndMsgSubDiv).append(nameA);
			var msgP = document.createElement('P');
			$(msgP).attr('class', 'text-sm mb-0 content');
			$(msgP).append(item.content);
			$(nameAndMsgSubDiv).append(msgP);
			var timeAndSeenDiv = document.createElement('DIV');
			$(timeAndSeenDiv).attr('class', 'col-3 d-flex');
			$(contentDiv).append(timeAndSeenDiv);
			var timeAndSeenSubDiv = document.createElement('DIV');
			$(timeAndSeenSubDiv).attr('class', 'ms-auto d-flex flex-column');
			$(timeAndSeenDiv).append(timeAndSeenSubDiv);
			var timeSpan = document.createElement('SPAN');
			$(timeSpan).attr('class', 'text-xs mb-1');
			$(timeSpan).append(item.occurredTime);
			$(timeAndSeenSubDiv).append(timeSpan);
			if (item.notSeenCount) {
				var notSeenDiv = document.createElement('DIV');
				$(notSeenDiv).attr('class', 'd-flex justify-content-center');
				$(timeAndSeenSubDiv).append(notSeenDiv);
				var notSeenCountSpan = document.createElement('SPAN');
				$(notSeenCountSpan).attr('class', 'text-xs text-light bg-danger border-radius-md px-1');
				$(notSeenCountSpan).append(item.notSeenCount);
				$(notSeenDiv).append(notSeenCountSpan);
			}
		});
	}
});