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
					var behavior = msg.behavior;
					var divWrap = document.createElement('DIV');
					var divParent = document.createElement('DIV');
					var divChild = document.createElement('DIV');
					divParent.className += 'd-flex flex-column maxWidth80';
					messagesArea.appendChild(divWrap);
					divWrap.appendChild(divParent);
					divParent.appendChild(divChild);
					if (behavior !== 'LIAO_LIAO' && behavior !== 'DA_ZHAO_HU' && behavior !== 'YAO_CHE_MA_FEI') {
						var dateDiv = document.createElement('DIV');
						dateDiv.className += 'text-xs';
						dateDiv.innerHTML = dateFormat(msg.occurred);
						divChild.appendChild(dateDiv);
						var contentDiv = document.createElement('DIV');
						divChild.appendChild(contentDiv);
						divWrap.className += 'd-flex justify-content-center mb-4';
						divChild.className += 'bg-dark border-radius-md text-sm px-3 shadow wordBreak text-light text-center';
						switch (behavior) {
							case 'JI_WO_LAI':
								self === msg.sender ? contentDiv.innerHTML += '您已發出要求通訊軟體' : contentDiv.innerHTML += '收到對方要求通訊軟體';
								break;
							case 'JI_NI_LAI':
								self === msg.sender ? contentDiv.innerHTML += '您已接受給對方通訊軟體' : contentDiv.innerHTML += '對方同意給您通訊軟體';
								break;
							case 'BU_JI_LAI':
								self === msg.sender ? contentDiv.innerHTML += '您已拒絕給對方通訊軟體' : contentDiv.innerHTML += '對方拒絕給您通訊軟體<br/>12小時後才能再要求';
								break;
							case 'LAI_KOU_DIAN':
								self === msg.sender ? contentDiv.innerHTML += '您已加了對方的通訊軟體' : contentDiv.innerHTML += '對方已加了您的通訊軟體';
								break;
							default:
								console.log(behavior);
						}
						return;
					}
					var dateSpan = document.createElement('SPAN');
					dateSpan.innerHTML = dateFormat(msg.occurred);
					divParent.appendChild(dateSpan);
					// 根據發送者是自己還是對方來給予不同的class名, 以達到訊息左右區分
					self === msg.sender ? divWrap.className += 'd-flex justify-content-end mb-2' : divWrap.className += 'd-flex justify-content-start mb-2';
					self === msg.sender ? divChild.className += 'bg-primary text-light border-radius-xl px-3 py-2 me-1 align-self-end shadow wordBreak' : divChild.className += 'bg-light border-radius-xl px-3 py-2 ms-1 align-self-start shadow wordBreak';
					self === msg.sender ? dateSpan.className += 'text-xs align-self-end me-2' : dateSpan.className += 'text-xs align-self-start ms-2';
					console.log('msg.behavior' + msg.behavior)
					console.log('msg.points' + msg.points)
					if (behavior === 'YAO_CHE_MA_FEI') {
						console.log('yes')
						self === msg.sender ? divChild.innerHTML = '您已和對方要求車馬費' + msg.points : divChild.innerHTML = '對方和您要求車馬費' + msg.points;
						return;
					}
					divChild.innerHTML = msg.greeting;
				});
				scrollToEnd();
			} else if ('chat' === jsonObj.type) {
				console.log(jsonObj.msgCount);
				if (parseInt(jsonObj.msgCount) === 3 && isMale === 'true') {
					$('TEXTAREA#chatInput').remove();
					$('BUTTON.sendMsgBtn').remove();
					var span = document.createElement('SPAN');
					$(span).html('12小時內僅能發送3句話給甜心');
					$('DIV.footerWrap').append(span);
				}
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
				self === jsonObj.sender ? divChild.className += 'bg-primary text-light border-radius-xl px-3 py-2 me-1 align-self-end shadow wordBreak' : divChild.className += 'bg-light border-radius-xl px-3 py-2 ms-1 align-self-start shadow wordBreak';
				self === jsonObj.sender ? dateSpan.className += 'text-xs align-self-end me-2' : dateSpan.className += 'text-xs align-self-start ms-2';
				scrollToEnd();
			} else if ('button' === jsonObj.type) {
				console.log(jsonObj.behavior);
				var divWrap = document.createElement('DIV');
				var divParent = document.createElement('DIV');
				var divChild = document.createElement('DIV');
				divParent.className += 'd-flex flex-column maxWidth80';
				messagesArea.appendChild(divWrap);
				divWrap.appendChild(divParent);
				divParent.appendChild(divChild);
				var dateDiv = document.createElement('DIV');
				dateDiv.className += 'text-xs';
				dateDiv.innerHTML = dateFormat(new Date());
				divChild.appendChild(dateDiv);
				var contentDiv = document.createElement('DIV');
				divChild.appendChild(contentDiv);
				divWrap.className += 'd-flex justify-content-center mb-4';
				divChild.className += 'bg-dark border-radius-md text-sm px-3 shadow wordBreak text-light text-center';
				switch (jsonObj.behavior) {
					case 'JI_WO_LAI':
						self === jsonObj.sender ? contentDiv.innerHTML += '您已發出要求通訊軟體' : contentDiv.innerHTML += '收到對方要求通訊軟體';
						if (isMale === 'false') {
							var div = document.createElement('DIV');
							$(div).attr({
								'class': 'd-flex align-items-center justify-content-center femaleBtn floatBtn'
							});
							$('DIV.footerWrap').append(div);
							var acceptBtn = document.createElement('BUTTON');
							$(acceptBtn).attr({
								'class': 'btn btn-sm btn-outline-primary px-3 py-2 m-0 me-1 border-radius-xl accept',
								type: 'button'
							});
							$(acceptBtn).html('接受');
							$(div).append(acceptBtn);
							var refuseBtn = document.createElement('BUTTON');
							$(refuseBtn).attr({
								'class': 'btn btn-sm btn-outline-dark px-3 py-2 m-0 me-1 border-radius-xl refuse',
								type: 'button'
							});
							$(refuseBtn).html('拒絕');
							$(div).append(refuseBtn);
							$('BUTTON.accept').dblclick(function (e) {
								e.preventDefault();
							});
							$('BUTTON.accept').click(function (event) {
								event.preventDefault();
								$(this).attr('disabled', true);
								$(this).siblings('BUTTON.refuse').attr('disabled', true);

								$.post(
									"/stalked.json",
									{
										whom: friend
									},
									function (data) {
										if (data.response) {
											$('.toast-body').html(data.reason);
											$('.toast').toast('show');
											$('DIV.femaleBtn').empty();
											var jsonObj = {
												'type': 'button',
												'sender': self,
												'receiver': friend,
												'behavior': 'JI_NI_LAI'
											};
											websocket.send(JSON.stringify(jsonObj));
										} else {
											$('.toast-body').html(data.reason);
											$('.toast').toast('show');
											if (data.redirect) {
												$('.toast').on('hidden.bs.toast', function () {
													location.href = data.redirect;
												});
											}
										}
									},
									'json'
									);
								return false;
							});
							$('BUTTON.refuse').dblclick(function (e) {
								e.preventDefault();
							});
							$('BUTTON.refuse').click(function (event) {
								event.preventDefault();
								$(this).attr('disabled', true);
								$(this).siblings('BUTTON.accept').attr('disabled', true);

								$.post(
									"/notStalked.json",
									{
										whom: friend
									},
									function (data) {
										if (data.response) {
											$('.toast-body').html(data.reason);
											$('.toast').toast('show');
											$('DIV.femaleBtn').empty();
											var jsonObj = {
												'type': 'button',
												'sender': self,
												'receiver': friend,
												'behavior': 'BU_JI_LAI'
											};
											websocket.send(JSON.stringify(jsonObj));
										} else {
											$('.toast-body').html(data.reason);
											$('.toast').toast('show');
										}
									},
									'json'
									);
								return false;
							});
						}
						break;
					case 'JI_NI_LAI':
						self === jsonObj.sender ? contentDiv.innerHTML += '您已接受給對方通訊軟體' : contentDiv.innerHTML += '對方同意給您通訊軟體';
						if (isMale === 'false') {
							$('DIV.floatBtn').empty();
							var rateBtn = document.createElement('BUTTON');
							$(rateBtn).attr({
								'class': 'btn btn-sm btn-warning px-3 py-2 m-0 border-radius-xl rate',
								'data-bs-target': '#rateModal',
								'data-bs-toggle': 'modal',
								type: 'button'
							});
							$(rateBtn).html('評價');
							$('DIV.floatBtn').append(rateBtn);
						}
						if (isMale === 'true') {
							$('DIV.floatBtn').empty();
							var openSocialMediaBtn = document.createElement('BUTTON');
							$(openSocialMediaBtn).attr({
								'class': 'btn btn-sm btn-success px-3 py-2 m-0 border-radius-xl openSocialMedia',
								type: 'button'
							});
							$(openSocialMediaBtn).html('加入好友');
							$('DIV.floatBtn').append(openSocialMediaBtn);
							var rateBtn = document.createElement('BUTTON');
							$(rateBtn).attr({
								'class': 'btn btn-sm btn-warning px-3 py-2 m-0 ms-1 border-radius-xl rate',
								'data-bs-target': '#rateModal',
								'data-bs-toggle': 'modal',
								type: 'button'
							});
							$(rateBtn).html('評價');
							$('DIV.floatBtn').append(rateBtn);
							$('BUTTON.openSocialMedia').dblclick(function (e) {
								e.preventDefault();
							});
							$('BUTTON.openSocialMedia').click(function () {
								$(this).attr('disabled', true);
								$.post(
									'/maleOpenLine.json',
									{
										whom: friend
									},
									function (data) {
										if (data.response && data.result === 'isLine') {
											location.href = data.redirect;
										} else if (data.response && data.result === 'isWeChat') {
											var src = 'https://' + location.hostname + data.redirect;
											$('IMG.weChatQRcode').attr('src', src);
											$('A.weChatQRcode').attr('href', src);
											$('#weChatModel').modal('show');
										} else {
											$('.toast-body').html(data.reason);
											$('.toast').toast('show');
										}
									},
									'json'
									);
								return false;
							});
						}
						break;
					case 'BU_JI_LAI':
						self === jsonObj.sender ? contentDiv.innerHTML += '您已拒絕給對方通訊軟體' : contentDiv.innerHTML += '對方拒絕給您通訊軟體<br/>12小時後才能再要求';
						$('DIV.floatBtn').empty();
						break;
					case 'LAI_KOU_DIAN':
						self === jsonObj.sender ? contentDiv.innerHTML += '您開啟了對方的通訊軟體QRcode' : contentDiv.innerHTML += '對方已開啟了您的通訊軟體QRcode';
						break;
					default:
						console.log(jsonObj.behavior);
				}
				return;
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
		$('.chatroom').scrollTop(scrollHeight);
	}

	var $textarea = $('#chatInput');
	$textarea
		.on('keydown', function (e) {
			if (e.keyCode === 13 && e.altKey) {
				$(this).val($(this).val() + '\n');
			}
		})
		.on('keypress', function (e) {
			if (e.keyCode === 13 && !e.ctrlKey) {
				sendMessage();
				return false;
			}
		});

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
					$('DIV.maleBtn').empty();
					let btn = document.createElement('BUTTON');
					$(btn).attr({
						'class': 'btn btn-sm btn-dark px-3 py-2 m-0 border-radius-xl',
						disabled: 'true',
						type: 'button'
					});
					$('DIV.maleBtn').append(btn);
					let span = document.createElement('SPAN');
					$(span).html('已要求通訊軟體, 等待甜心回應');
					$(btn).append(span);
					var jsonObj = {
						'type': 'button',
						'sender': self,
						'receiver': friend,
						'behavior': 'JI_WO_LAI'
					};
					websocket.send(JSON.stringify(jsonObj));
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('BUTTON.accept').dblclick(function (e) {
		e.preventDefault();
	});
	$('BUTTON.accept').click(function (event) {
		event.preventDefault();
		$(this).attr('disabled', true);
		$(this).siblings('BUTTON.refuse').attr('disabled', true);

		$.post(
			"/stalked.json",
			{
				whom: friend
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$('DIV.femaleBtn').empty();
					var jsonObj = {
						'type': 'button',
						'sender': self,
						'receiver': friend,
						'behavior': 'JI_NI_LAI'
					};
					websocket.send(JSON.stringify(jsonObj));
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					if (data.redirect) {
						$('.toast').on('hidden.bs.toast', function () {
							location.href = data.redirect;
						});
					}
				}
			},
			'json'
			);
		return false;
	});
	$('BUTTON.refuse').dblclick(function (e) {
		e.preventDefault();
	});
	$('BUTTON.refuse').click(function (event) {
		event.preventDefault();
		$(this).attr('disabled', true);
		$(this).siblings('BUTTON.accept').attr('disabled', true);

		$.post(
			"/notStalked.json",
			{
				whom: friend
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$('DIV.femaleBtn').empty();
					var jsonObj = {
						'type': 'button',
						'sender': self,
						'receiver': friend,
						'behavior': 'BU_JI_LAI'
					};
					websocket.send(JSON.stringify(jsonObj));
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('BUTTON.commentBtn').click(function (event) {
		event.preventDefault();
		$(this).attr('disabled', true);
		var rate = $('INPUT[name="rating"]:checked').val();
		if (rate === undefined) {
			rate = null;
		}
		$.post(
			'/rate.json',
			{
				whom: friend,
				rate: rate,
				comment: $('TEXTAREA[name="comment"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$('#rateModal').modal('hide');
					$('BUTTON.rate').remove();
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('BUTTON.openSocialMedia').dblclick(function (e) {
		e.preventDefault();
	});
	$('BUTTON.openSocialMedia').click(function () {
		$(this).attr('disabled', true);
		$.post(
			'/maleOpenLine.json',
			{
				whom: friend
			},
			function (data) {
				var jsonObj = {
					'type': 'button',
					'sender': self,
					'receiver': friend,
					'behavior': 'LAI_KOU_DIAN'
				};
				websocket.send(JSON.stringify(jsonObj));
				if (data.response && data.result === 'isLine') {
					location.href = data.redirect;
				} else if (data.response && data.result === 'isWeChat') {
					var src = 'https://' + location.hostname + data.redirect;
					$('IMG.weChatQRcode').attr('src', src);
					$('A.weChatQRcode').attr('href', src);
					$('#weChatModel').modal('show');
					$('BUTTON.openSocialMedia').attr('disabled', 'false');
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	var url;
	$('BUTTON#fare').on("click", function () {
		url = '/fare.json';
	});

	$('BUTTON#reqFare').on("click", function () {
		url = '/reqFare.json';
	});

	$('.confirmFare').click(function (event) {
		event.preventDefault();
		let btn = this;
		$.post(
			url,
			{
				howMany: $('INPUT[name="howMany"]').val(),
				whom: friend
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$('#fareModal').modal('hide');
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('BUTTON.block').click(function () {
		$.post(
			'/block.json',
			{
				identifier: friend
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
	});
});