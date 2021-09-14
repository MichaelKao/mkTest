$(document).ready(function () {

	var self = $('INPUT[name="identifier"]').val();
	var friend = $('INPUT[name="friendIdentifier"]').val();
	var hostName = location.hostname;

	var socket;
	if (typeof (WebSocket) == "undefined") {
		console.log("[Inbox]Not support WebSocket");
	} else {
		console.log("[Inbox]Support WebSocket");
		connect();
	}

	function connect() {

		socket = new WebSocket("wss://" + hostName + "/webSocket/" + self);

		//開啟事件
		socket.onopen = function () {
			console.log("[Inbox]WebSocket is open");
		};

		//獲得訊息事件
		socket.onmessage = function (msg) {
			var count;

			if (msg.data.slice(0, 5) !== 'inbox') {
				$('.toast-body').html(msg.data);
				$('.toast').toast('show');
			}

			// 增加'通知數'和'訊息數'
			if (isNaN(parseInt($('.announcement').html()))) {
				count = 1;
				$('.announcement').attr('style', 'display: inline;');
			} else {
				count = parseInt($('.announcement').html()) + 1;
			}
			$('.announcement').html(count);

			$.post(
				'/updateInbox.json',
				function (data) {
					var first = $('DIV#first');
					var second = $('DIV#second');
					first.empty();
					second.empty();
					data.result.chatList.forEach(function (item) {
						var conversationDiv = document.createElement('DIV');
						$(conversationDiv).attr('class', 'card col-12 col-md-7 my-1 mx-auto conversationWrap position-relative shadow');
						if (friend === item.identifier) {
							$(conversationDiv).attr('class', 'card my-2 px-2 mx-auto conversationWrap position-relative shadow active');
						} else if (typeof (friend) !== 'undefined') {
							$(conversationDiv).attr('class', 'card my-2 px-2 mx-auto conversationWrap position-relative shadow');
						}
						if (item.isMatchedOrIsVip === 'true') {
							first.append(conversationDiv);
						} else {
							second.append(conversationDiv);
						}
						var linkA = document.createElement('A');
						$(linkA).attr({
							'class': 'inboxLink',
							'href': '/chatroom/' + item.identifier + '/'
						});
						$(conversationDiv).append(linkA);
						var contentDiv = document.createElement('DIV');
						$(contentDiv).attr('class', 'd-flex justify-content-between align-items-center py-2');
						$(conversationDiv).append(contentDiv);
						var imgDiv = document.createElement('DIV');
						$(contentDiv).append(imgDiv);
						var img = document.createElement('IMG');
						$(img).attr({
							'alt': '大頭照',
							'class': 'rounded-circle',
							'src': item.profileImage,
							'width': '60px'
						});
						if (typeof (friend) !== 'undefined') {
							$(img).attr('width', '50px');
						}
						$(imgDiv).append(img);
						var nameAndMsgDiv = document.createElement('DIV');
						$(nameAndMsgDiv).attr({
							'class': 'me-auto',
							'style': 'overflow: hidden'
						});
						$(contentDiv).append(nameAndMsgDiv);
						var nameAndMsgSubDiv = document.createElement('DIV');
						$(nameAndMsgSubDiv).attr('class', 'd-flex flex-column align-items-start ms-3');
						if (typeof (friend) !== 'undefined') {
							$(nameAndMsgSubDiv).attr('class', 'd-flex flex-column align-items-start ms-2');
						}
						$(nameAndMsgDiv).append(nameAndMsgSubDiv);
						var nameA = document.createElement('A');
						$(nameA).attr('class', 'font-weight-bold text-dark text-sm mb-1');
						$(nameA).append(item.nickname);
						$(nameAndMsgSubDiv).append(nameA);
						var msgP = document.createElement('P');
						$(msgP).attr('class', 'text-sm mb-0 content');
						if (friend === item.identifier) {
							$(msgP).attr('class', 'text-sm mb-0 content currentContent');
						}
						$(msgP).append(item.content);
						$(nameAndMsgSubDiv).append(msgP);
						var timeAndSeenDiv = document.createElement('DIV');
						$(timeAndSeenDiv).attr('class', 'col-2 d-flex');
						$(contentDiv).append(timeAndSeenDiv);
						var timeAndSeenSubDiv = document.createElement('DIV');
						$(timeAndSeenSubDiv).attr('class', 'ms-auto d-flex flex-column');
						$(timeAndSeenDiv).append(timeAndSeenSubDiv);
						var timeSpan = document.createElement('SPAN');
						$(timeSpan).attr('class', 'text-xs mb-1');
						$(timeSpan).append(item.occurredTime);
						$(timeAndSeenSubDiv).append(timeSpan);
						if (item.notSeenCount && friend !== item.identifier) {
							var notSeenDiv = document.createElement('DIV');
							$(notSeenDiv).attr('class', 'd-flex justify-content-center');
							$(timeAndSeenSubDiv).append(notSeenDiv);
							var notSeenCountSpan = document.createElement('SPAN');
							$(notSeenCountSpan).attr('class', 'text-xs text-light bg-primary border-radius-md px-1');
							$(notSeenCountSpan).append(item.notSeenCount);
							$(notSeenDiv).append(notSeenCountSpan);
						}
					});

					if (data.result.matchedOrVipNotSeenCount) {
						var firstNotSeen = $('SPAN.firstNotSeen');
						if (firstNotSeen.length < 1) {
							var firstNotSeenSpan = document.createElement('SPAN');
							$(firstNotSeenSpan).attr('class', 'text-xs border-radius-md px-1 ms-1 firstNotSeen notSeen');
							$(firstNotSeenSpan).append(data.result.matchedOrVipNotSeenCount);
							$('A[href="#first"]').append(firstNotSeenSpan);
						} else {
							firstNotSeen.html(data.result.matchedOrVipNotSeenCount);
						}
					}
					if (data.result.notMatchedOrNotVipNotSeenCount) {
						var secondNotSeen = $('SPAN.secondNotSeen');
						if (secondNotSeen.length < 1) {
							var secondNotSeenSpan = document.createElement('SPAN');
							$(secondNotSeenSpan).attr('class', 'text-xs border-radius-md px-1 ms-1 secondNotSeen notSeen');
							$(secondNotSeenSpan).append(data.result.notMatchedOrNotVipNotSeenCount);
							$('A[href="#second"]').append(secondNotSeenSpan);
						} else {
							secondNotSeen.html(data.result.notMatchedOrNotVipNotSeenCount);
						}
					}
				},
				'json'
				);
			return false;
		};

		//關閉事件
		socket.onclose = function () {
			console.log("[Inbox]WebSocket is close");
			setTimeout(function () {
				connect();
			}, 1000);
		};

		//發生了錯誤事件
		socket.onerror = function () {
			console.log("[Inbox]WebSocket is error");
		}
	}

	// 關閉或離開頁面後關閉連接
	$(window).on("unload", function () {
		socket.close();
	});
});