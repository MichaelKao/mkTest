$(document).ready(function () {

	var self = $('INPUT[name="selfIdentifier"]').val();
	var friend = $('INPUT[name="friendIdentifier"]').val();
	var isMale = $('INPUT[name="gender"]').val();
	var hostName = location.hostname;
	var statusOutput = document.getElementById("statusOutput");
	var messagesArea = document.getElementById("messagesArea");

	var websocket;
	if (typeof (WebSocket) == 'undefined') {
		console.log('Not support WebSocket');
	} else {
		console.log('Support WebSocket');
		connect();
	}

	function connect() {

		websocket = new WebSocket('wss://' + hostName + '/webSocket/chat/' + self);

		//開啟事件
		websocket.onopen = function () {
			console.log('Chat WebSocket is open!');
			var jsonObj = {
				"type": "history",
				"sender": self,
				"receiver": friend,
				"message": ""
			};
			websocket.send(JSON.stringify(jsonObj));
		};

		//獲得訊息事件
		websocket.onmessage = function (msg) {
			var jsonObj = JSON.parse(msg.data);
			if ('history' === jsonObj.type) {
				messagesArea.innerHTML = '';
				// 這行的jsonObj.message是從DB撈出跟對方的歷史訊息，再parse成JSON格式處理
				var messages = JSON.parse(jsonObj.historyMsgs);
				console.log('messages.len' + messages.length);
				messages.forEach(function (msg) {
					var divWrap = document.createElement('DIV');
					var divParent = document.createElement('DIV');
					var divChild = document.createElement('DIV');
					var dateSpan = document.createElement('SPAN');
					divParent.className += 'd-flex flex-column maxWidth80';
					divChild.innerHTML = msg.greeting;
					dateSpan.innerHTML = dateFormat(msg.occurred);
					messagesArea.appendChild(divWrap);
					divWrap.appendChild(divParent);
					divParent.appendChild(divChild);
					divParent.appendChild(dateSpan);
					// 根據發送者是自己還是對方來給予不同的class名, 以達到訊息左右區分
					self === msg.sender ? divWrap.className += 'd-flex justify-content-end mb-2' : divWrap.className += 'd-flex justify-content-start mb-2';
					self === msg.sender ? divChild.className += 'bg-primary text-light border-radius-xl px-3 py-2 me-1 align-self-end shadow' : divChild.className += 'bg-light border-radius-xl px-3 py-2 ms-1 align-self-start shadow';
					self === msg.sender ? dateSpan.className += 'text-xs align-self-end me-2' : dateSpan.className += 'text-xs align-self-start ms-2';
				});
				scrollToEnd();
			} else if ('chat' === jsonObj.type) {
				console.log(jsonObj.message);
				var divWrap = document.createElement('DIV');
				var divParent = document.createElement('DIV');
				var divChild = document.createElement('DIV');
				var dateSpan = document.createElement('SPAN');
				divParent.className += 'd-flex flex-column maxWidth80';
				divChild.innerHTML = jsonObj.message;
				dateSpan.innerHTML = dateFormat(new Date());
				messagesArea.appendChild(divWrap);
				divWrap.appendChild(divParent);
				divParent.appendChild(divChild);
				divParent.appendChild(dateSpan);
				// 根據發送者是自己還是對方來給予不同的class名, 以達到訊息左右區分
				self === jsonObj.sender ? divWrap.className += 'd-flex justify-content-end mb-2' : divWrap.className += 'd-flex justify-content-start mb-2';
				self === jsonObj.sender ? divChild.className += 'bg-primary text-light border-radius-xl px-3 py-2 me-1 align-self-end shadow' : divChild.className += 'bg-light border-radius-xl px-3 py-2 ms-1 align-self-start shadow';
				self === jsonObj.sender ? dateSpan.className += 'text-xs align-self-end me-2' : dateSpan.className += 'text-xs align-self-start ms-2';
				scrollToEnd();
			}
		};

		//關閉事件
		websocket.onclose = function (event) {
			if (event.code === 1000)
				reason = "Normal closure, meaning that the purpose for which the connection was established has been fulfilled.";
			else if (event.code === 1001)
				reason = "An endpoint is \"going away\", such as a server going down or a browser having navigated away from a page.";
			else if (event.code === 1002)
				reason = "An endpoint is terminating the connection due to a protocol error";
			else if (event.code === 1003)
				reason = "An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).";
			else if (event.code === 1004)
				reason = "Reserved. The specific meaning might be defined in the future.";
			else if (event.code === 1005)
				reason = "No status code was actually present.";
			else if (event.code === 1006)
				reason = "The connection was closed abnormally, e.g., without sending or receiving a Close control frame";
			else if (event.code === 1007)
				reason = "An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [http://tools.ietf.org/html/rfc3629] data within a text message).";
			else if (event.code === 1008)
				reason = "An endpoint is terminating the connection because it has received a message that \"violates its policy\". This reason is given either if there is no other sutible reason, or if there is a need to hide specific details about the policy.";
			else if (event.code === 1009)
				reason = "An endpoint is terminating the connection because it has received a message that is too big for it to process.";
			else if (event.code === 1010) // Note that this status code is not used by the server, because it can fail the WebSocket handshake instead.
				reason = "An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn't return them in the response message of the WebSocket handshake. <br /> Specifically, the extensions that are needed are: " + event.reason;
			else if (event.code === 1011)
				reason = "A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.";
			else if (event.code === 1015)
				reason = "The connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).";
			else
				reason = "Unknown reason";
			console.log("Chat WebSocket is close because : " + reason);
			setTimeout(function () {
				connect();
			}, 1000);
		};

		//發生了錯誤事件
		websocket.onerror = function () {
			console.log("Chat WebSocket is error");
		}
	}

	// 關閉或離開頁面後關閉連接
	$(window).on("unload", function () {
		websocket.close();
	});

	$('.sendMsgBtn').click(function () {
		sendMessage();
	});

	function sendMessage() {
		var chatInput = document.getElementById("chatInput");
		var message = chatInput.value.trim();

		if (message === "") {
			$('.toast-body').html('請輸入訊息');
			$('.toast').toast('show');
			chatInput.focus();
		} else {
			var jsonObj = {
				'type': 'chat',
				'sender': self,
				'receiver': friend,
				'message': message
			};
			websocket.send(JSON.stringify(jsonObj));
			chatInput.value = "";
			chatInput.focus();
		}
	}

	function dateFormat(dbDate) {
		var d = new Date(dbDate);
		var yr = d.getFullYear();
		var mth = d.getMonth() + 1;
		var date = d.getDate();
		var hour = d.getHours();
		var minute = d.getMinutes();

		return yr + '/' + ('0' + mth).substr(-2) + '/' + ('0' + date).substr(-2) + ' ' + ('0' + hour).substr(-2) + ':' + ('0' + minute).substr(-2);
	}

	function scrollToEnd() {
		var scrollHeight = $('.chatroom').prop('scrollHeight');
		$('.chatroom').scrollTop(scrollHeight, 200);
	}

	$('#giveMeLine').click(function (event) {
		event.preventDefault();
		let btn = this;
		$.post(
			'/stalking.json',
			{
				whom: friend
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
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