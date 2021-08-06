$(document).ready(function () {

	var self = $('INPUT[name="selfIdentifier"]').val();
	var friend = $('INPUT[name="friendIdentifier"]').val();
	var hostName = location.hostname;
	var statusOutput = document.getElementById("statusOutput");
	var messagesArea = document.getElementById("messagesArea");

	var socket;
	if (typeof (WebSocket) == 'undefined') {
		console.log('Not support WebSocket');
	} else {
		console.log('Support WebSocket');
	}

	socket = new WebSocket('wss://' + hostName + '/webSocket/chat/' + '8757455e-b032-4dd4-8392-69bee9194bad');

	//開啟事件
	socket.onopen = function () {
		console.log('Chat WebSocket is open!');
		var jsonObj = {
			"type": "history",
			"sender": '8757455e-b032-4dd4-8392-69bee9194bad',
			"receiver": 'bbcb1fe6-1d5b-48f8-b804-f0486353f8bc',
			"message": ""
		};
		socket.send(JSON.stringify(jsonObj));
	};



	//獲得訊息事件
	socket.onmessage = function (msg) {
		console.log(msg.data);
		var jsonObj = JSON.parse(msg.data);
		console.log(jsonObj.type)
		if ('open' === jsonObj.type) {
			refreshFriendList(jsonObj);
		} else if ('history' === jsonObj.type) {
			console.log(jsonObj)
			messagesArea.innerHTML = '';
			var ul = document.createElement('ul');
			ul.id = 'area';
			messagesArea.appendChild(ul);
			// 這行的jsonObj.message是從DB撈出跟對方的歷史訊息，再parse成JSON格式處理
			var messages = JSON.parse(jsonObj.message);
			for (var i = 0; i < messages.length; i++) {
				var historyData = JSON.parse(messages[i]);
				var showMsg = historyData.message;
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
	socket.onclose = function () {
		console.log("Chat WebSocket is close");
	};

	//發生了錯誤事件
	socket.onerror = function () {
		console.log("Chat WebSocket is error");
	}

	// 關閉或離開頁面後關閉連接
	$(window).on("unload", function () {
		socket.close();
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
			webSocket.send(JSON.stringify(jsonObj));
			chatInput.value = "";
			chatInput.focus();
		}
	}

	// 註冊列表點擊事件並抓取好友名字以取得歷史訊息
	function addListener() {
		var container = document.getElementById("row");
		container.addEventListener("click", function (e) {
			var friend = e.srcElement.textContent;
			updateFriendName(friend);
			var jsonObj = {
				"type": "history",
				"sender": '8757455e-b032-4dd4-8392-69bee9194bad',
				"receiver": 'bbcb1fe6-1d5b-48f8-b804-f0486353f8bc',
				"message": ""
			};
			webSocket.send(JSON.stringify(jsonObj));
		});
	}

	// 有好友上線或離線就更新列表
	function refreshFriendList(jsonObj) {
		var friends = jsonObj.users;
		var row = document.getElementById("row");
		row.innerHTML = '';
		for (var i = 0; i < friends.length; i++) {
			if (friends[i] === self) {
				continue;
			}
			row.innerHTML += '<div id=' + i + ' class="column" name="friendName" value=' + friends[i] + ' ><h2>' + friends[i] + '</h2></div>';
		}
		addListener();
	}
});