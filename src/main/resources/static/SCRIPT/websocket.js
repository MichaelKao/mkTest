$(document).ready(function () {

	var identifier = $('INPUT[name="identifier"]').val();
	console.log(identifier)

	var socket;
	if (typeof (WebSocket) == "undefined") {
		console.log("Not support WebSocket");
	} else {
		console.log("Support WebSocket");
	}

	socket = new WebSocket("wss://jkfans.ngrok.io/webSocket/" + identifier);

	//開啟事件
	socket.onopen = function () {
		console.log("WebSocket is open");
	};

	//獲得訊息事件
	socket.onmessage = function (msg) {
		$('.toast-body').html(msg.data);
		$('.toast').toast('show');
	};

	//關閉事件
	socket.onclose = function () {
		console.log("WebSocket is close");
	};

	//發生了錯誤事件
	socket.onerror = function () {
		console.log("WebSocket is error");
	}

	// 關閉或離開頁面後關閉連接
	$(window).on("unload", function () {
		socket.close();
	});
});