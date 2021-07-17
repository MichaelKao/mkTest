$(document).ready(function () {

	var identifier = $('INPUT[name="identifier"]').val();
	var hostName = location.hostname;

	var socket;
	if (typeof (WebSocket) == "undefined") {
		console.log("Not support WebSocket");
	} else {
		console.log("Support WebSocket");
	}

	socket = new WebSocket("wss://" + hostName + "/webSocket/" + identifier);

	//開啟事件
	socket.onopen = function () {
		console.log("WebSocket is open");
	};

	//獲得訊息事件
	socket.onmessage = function (msg) {
		$('.toast-body').html(msg.data);
		$('.toast').toast('show');
		var count;
		var $announcement = $('.announcement');
		// 增加通知數
		if (isNaN(parseInt($announcement.html()))) {
			count = 1;
			$announcement.attr('style', 'display: inline;');
		} else {
			count = parseInt($('.announcement').html()) + 1;
		}
		$('.announcement').html(count);
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