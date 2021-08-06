$(document).ready(function () {

	var self = $('INPUT[name="selfIdentifier"]').val();
	var friend = $('INPUT[name="friendIdentifier"]').val();
	var hostName = location.hostname;
	var statusOutput = document.getElementById("statusOutput");
	var messagesArea = document.getElementById("messagesArea");

	var websocket;
	if (typeof (WebSocket) == 'undefined') {
		console.log('Not support WebSocket');
	} else {
		console.log('Support WebSocket');
	}

	websocket = new WebSocket('wss://' + hostName + '/webSocket/chat/' + '8757455e-b032-4dd4-8392-69bee9194bad');

	//開啟事件
	websocket.onopen = function () {
		console.log('Chat WebSocket is open!');
		var jsonObj = {
			"type": "history",
			"sender": '8757455e-b032-4dd4-8392-69bee9194bad',
			"receiver": 'bbcb1fe6-1d5b-48f8-b804-f0486353f8bc',
			"message": ""
		};
		websocket.send(JSON.stringify(jsonObj));
	};


	//獲得訊息事件
	websocket.onmessage = function (msg) {
		var jsonObj = JSON.parse(msg.data);
		console.log(jsonObj.type)
		if ('history' === jsonObj.type) {
			console.log(jsonObj)
			messagesArea.innerHTML = '';
			var ul = document.createElement('ul');
			ul.id = 'area';
			messagesArea.appendChild(ul);
			// 這行的jsonObj.message是從DB撈出跟對方的歷史訊息，再parse成JSON格式處理
			var messages = JSON.parse(jsonObj.message);
			console.log('messages.len' + messages.length);
			for (var i = 0; i < messages.length; i++) {
				console.log('messages[i]' + messages[i]);
				var historyData = messages[i];
				var showMsg = historyData;
				var li = document.createElement('li');
				// 根據發送者是自己還是對方來給予不同的class名, 以達到訊息左右區分
				historyData.sender === '8757455e-b032-4dd4-8392-69bee9194bad' ? li.className += 'me' : li.className += 'friend';
				li.innerHTML = showMsg;
				ul.appendChild(li);
			}
			messagesArea.scrollTop = messagesArea.scrollHeight;
		} else if ('chat' === jsonObj.type) {
			var li = document.createElement('li');
			jsonObj.sender === self ? li.className += 'me' : li.className += 'friend';
			li.innerHTML = jsonObj.message;
			console.log(li);
			document.getElementById('area').appendChild(li);
			messagesArea.scrollTop = messagesArea.scrollHeight;
		} else if ('close' === jsonObj.type) {
			refreshFriendList(jsonObj);
		}
	};

	//關閉事件
	websocket.onclose = function (event) {
		if (event.code == 1000)
			reason = "Normal closure, meaning that the purpose for which the connection was established has been fulfilled.";
		else if (event.code == 1001)
			reason = "An endpoint is \"going away\", such as a server going down or a browser having navigated away from a page.";
		else if (event.code == 1002)
			reason = "An endpoint is terminating the connection due to a protocol error";
		else if (event.code == 1003)
			reason = "An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).";
		else if (event.code == 1004)
			reason = "Reserved. The specific meaning might be defined in the future.";
		else if (event.code == 1005)
			reason = "No status code was actually present.";
		else if (event.code == 1006)
			reason = "The connection was closed abnormally, e.g., without sending or receiving a Close control frame";
		else if (event.code == 1007)
			reason = "An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [http://tools.ietf.org/html/rfc3629] data within a text message).";
		else if (event.code == 1008)
			reason = "An endpoint is terminating the connection because it has received a message that \"violates its policy\". This reason is given either if there is no other sutible reason, or if there is a need to hide specific details about the policy.";
		else if (event.code == 1009)
			reason = "An endpoint is terminating the connection because it has received a message that is too big for it to process.";
		else if (event.code == 1010) // Note that this status code is not used by the server, because it can fail the WebSocket handshake instead.
			reason = "An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn't return them in the response message of the WebSocket handshake. <br /> Specifically, the extensions that are needed are: " + event.reason;
		else if (event.code == 1011)
			reason = "A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.";
		else if (event.code == 1015)
			reason = "The connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).";
		else
			reason = "Unknown reason";
		console.log("Chat WebSocket is close because : " + reason);
	};

	//發生了錯誤事件
	websocket.onerror = function () {
		console.log("Chat WebSocket is error");
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
			alert("Input a message");
			chatInput.focus();
		} else {
			alert(message);
			var jsonObj = {
				'type': 'chat',
				'sender': '8757455e-b032-4dd4-8392-69bee9194bad',
				'receiver': 'bbcb1fe6-1d5b-48f8-b804-f0486353f8bc',
				'message': message
			};
			websocket.send(JSON.stringify(jsonObj));
			chatInput.value = "";
			chatInput.focus();
		}
	}
});