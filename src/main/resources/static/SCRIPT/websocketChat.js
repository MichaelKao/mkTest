$(document).ready(function () {

	var self = $('INPUT[name="identifier"]').val();
	var friend = $('INPUT[name="friendIdentifier"]').val();
	var isMale = $('INPUT[name="gender"]').val();
	var hostName = location.hostname;
	var messagesArea = document.getElementById("messagesArea");
	var $nextMsgsPage = $('INPUT[name="nextMsgsPage"]');

	var websocket;
	if (typeof (WebSocket) == 'undefined') {
		console.log('[Chatroom]Not support WebSocket');
	} else {
		console.log('[Chatroom]Support WebSocket');
		connect();
	}

	function connect() {
		websocket = new WebSocket('wss://' + hostName + '/webSocket/chat/' + self + friend);
		//開啟事件
		websocket.onopen = function () {
			console.log('[Chatroom]Chat WebSocket is open!');
			var jsonObj = {
				"type": "history",
				"sender": self,
				"receiver": friend,
				"message": "",
				"page": 0
			};
			$nextMsgsPage.val(1);
			websocket.send(JSON.stringify(jsonObj));
		};

		//獲得訊息事件
		websocket.onmessage = function (msg) {
			var jsonObj = JSON.parse(msg.data);
			if ('history' === jsonObj.type) {
				messagesArea.innerHTML = '';
				$('.floatBtn').remove();
				$('.textareaBox').empty();
				// 這行的jsonObj.message是從DB撈出跟對方的歷史訊息，再parse成JSON格式處理
				var messages = JSON.parse(jsonObj.historyMsgs);
				var friendStatus = JSON.parse(jsonObj.friendStatus);
				var chatStatus = JSON.parse(jsonObj.chatStatus);
				appendHistoryData(messages, $('#messagesArea'));

				var floatDiv = document.createElement('DIV');
				$(floatDiv).attr('class', 'd-flex justify-content-center floatBtn');
				var floatWrap = document.createElement('DIV');
				friendStatus.forEach(function (friendStatus) {
					$('.inputContainer').prepend(floatDiv);
					if (friendStatus === 'decideBtn') {
						$(floatWrap).attr('class', 'border border-primary border-radius-xl text-xs px-3 py-1 shadow wordBreak text-center floatWrap');
						$(floatDiv).prepend(floatWrap);
						$(floatWrap).append(`<DIV class="text-primary">
									<DIV><I class="fad fa-user-plus"></I><SPAN>接受後可於聊天室暢聊</SPAN></DIV>
									<DIV>同時提供綁定的通訊軟體</DIV></DIV>`);
						var acceptBtn = document.createElement('BUTTON');
						$(acceptBtn).attr({
							'class': 'btn btn-outline-primary btn-round px-2 py-1 m-0 me-1 accept',
							'type': 'button'
						});
						$(acceptBtn).append('接受');
						$(floatWrap).append(acceptBtn);
						var refuseBtn = document.createElement('BUTTON');
						$(refuseBtn).attr({
							'class': 'btn btn-outline-dark btn-round px-2 py-1 m-0 border-radius-xl refuse',
							'type': 'button'
						});
						$(refuseBtn).append('拒絕');
						$(floatWrap).append(refuseBtn);
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
										$('DIV.floatBtn').empty();
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
										$('DIV.floatBtn').empty();
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
					if (friendStatus === 'femaleRateBtn') {
						$(floatWrap).attr('class', 'border border-primary border-radius-xl text-xs px-3 py-1 shadow wordBreak text-center floatWrap');
						$(floatDiv).prepend(floatWrap);
						$(floatWrap).append(`<DIV class="text-primary">
									<DIV><I class="fad fa-star-half"></I>
									<SPAN>已成為好友</SPAN></DIV></DIV>`);
						var rateBtn = document.createElement('BUTTON');
						$(rateBtn).attr({
							'class': 'btn btn-sm btn-primary btn-round px-2 py-1 m-0 rate',
							'data-bs-target': '#rateModal',
							'data-bs-toggle': 'modal',
							'type': 'button'
						});
						$(rateBtn).append('好友評價');
						$(floatWrap).append(rateBtn);
					}
					if (friendStatus === 'reqSocialMediaBtn') {
						$(floatWrap).attr('class', 'border border-primary border-radius-xl text-primary text-xs px-3 py-1 shadow wordBreak text-center floatWrap');
						$(floatDiv).prepend(floatWrap);
						$(floatWrap).append(`<DIV><I class="far fa-user-plus ms-1"></I>
									<SPAN>加入好友</SPAN></DIV><DIV>解鎖聊天室限制同時獲得私人通訊軟體</DIV>`);
						var reqSocialMediaBtnBtn = document.createElement('BUTTON');
						$(reqSocialMediaBtnBtn).attr({
							'class': 'btn btn-primary btn-round px-2 py-1 m-0',
							'id': 'giveMeLine',
							'type': 'button'
						});
						$(reqSocialMediaBtnBtn).append('送出邀請');
						$(floatWrap).append(reqSocialMediaBtnBtn);
						$(reqSocialMediaBtnBtn).click(function (event) {
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
										$('DIV.floatBtn').empty();
										var borderDiv = document.createElement('DIV');
										$(borderDiv).attr({
											'class': 'border border-primary border-radius-xl text-xs px-3 py-1 shadow wordBreak text-center floatWrap'
										});
										$('DIV.floatBtn').append(borderDiv);
										var msgDiv = document.createElement('DIV');
										$(msgDiv).attr('class', 'text-primary');
										$(borderDiv).append(msgDiv);
										var firstLineDiv = document.createElement('DIV');
										$(msgDiv).append(firstLineDiv);
										var firstLineI = document.createElement('I');
										$(firstLineI).attr('class', 'fad fa-user-plus');
										$(firstLineDiv).append(firstLineI);
										var firstLineSpan = document.createElement('SPAN');
										$(firstLineSpan).append('您已送出好友邀請，');
										$(firstLineDiv).append(firstLineSpan);
										var secondLineDiv = document.createElement('DIV');
										$(secondLineDiv).append('請等待對方回應。');
										$(msgDiv).append(secondLineDiv);
										floatWrapResize();
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
					}
					if (friendStatus === 'waitingForRes') {
						$(floatWrap).attr('class', 'border border-primary border-radius-xl text-xs px-3 py-1 shadow wordBreak text-center floatWrap');
						$(floatDiv).prepend(floatWrap);
						$(floatWrap).append(`<DIV class="text-primary"><DIV><I class="fad fa-user-plus"></I>
									<SPAN>您已送出好友邀請，</SPAN></DIV><DIV>請等待對方回應。</DIV></DIV>`);
					}
					if (friendStatus === 'maleAddLineBtn') {
						$(floatWrap).attr('class', 'border border-primary border-radius-xl text-xs px-3 py-1 shadow wordBreak text-center floatWrap');
						$(floatDiv).prepend(floatWrap);
						$(floatWrap).append(`<DIV class="text-primary"><DIV><I class="fad fa-star-half"></I>
									<SPAN>已接受您的好友邀請</SPAN></DIV></DIV>`);
						var openSocialMediaBtn = document.createElement('BUTTON');
						$(openSocialMediaBtn).attr({
							'class': 'btn btn-sm btn-primary btn-round px-2 py-1 m-0 openSocialMedia',
							'type': 'button'
						});
						$(openSocialMediaBtn).append('加通訊軟體');
						$(floatWrap).append(openSocialMediaBtn);
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
									if (data.response) {
										var jsonObj = {
											'type': 'button',
											'sender': self,
											'receiver': friend,
											'behavior': 'LAI_KOU_DIAN'
										};
										websocket.send(JSON.stringify(jsonObj));
									}
									if (data.response && data.result === 'isLine') {
										location.href = data.redirect;
									} else if (data.response && data.result === 'isWeChat') {
										var src = 'https://' + location.hostname + data.redirect;
										$('IMG.weChatQRcode').attr('src', src);
										$('A.weChatQRcode').attr('href', src);
										$('#weChatModel').modal('show');
										$('BUTTON.openSocialMedia').removeAttr('disabled');
									} else {
										$('.toast-body').html(data.reason);
										$('.toast').toast('show');
										$('.toast').on('hidden.bs.toast', function () {
											location.href = data.redirect;
										});
									}
								},
								'json'
								);
							return false;
						});
					}
					if (friendStatus === 'maleRateBtn') {
						var rateBtn = document.createElement('BUTTON');
						$(rateBtn).attr({
							'class': 'btn btn-sm btn-dark btn-round px-2 py-1 m-0 ms-1 rate',
							'data-bs-target': '#rateModal',
							'data-bs-toggle': 'modal',
							'type': 'button'
						});
						$(rateBtn).append('好友評價');
						$(floatWrap).append(rateBtn);
					}
				});
				console.log(chatStatus)
				if (chatStatus === 'able') {
					var sendContainer = document.createElement('DIV');
					$(sendContainer).attr('class', 'sendContainer');
					$('.textareaBox').append(sendContainer);
					var div = document.createElement('DIV');
					$(div).attr('class', 'textareaContainer');
					$(sendContainer).append(div);
					var textarea = document.createElement('TEXTAREA');
					$(textarea).attr('id', 'chatInput');
					if (isMale === 'true' && !friendStatus.some(item => item === 'maleAddLineBtn')) {
						$(textarea).attr('placeholder', '用3句話打動meQUEEN...')
					} else {
						$(textarea).attr('placeholder', '說點什麼吧...')
					}
					$(div).append(textarea);
					var sendBtn = document.createElement('BUTTON');
					$(sendBtn).attr({
						'class': 'btn btn-link m-0 p-0 fontSize25 sendMsgBtn',
						'disabled': 'true'
					});
					(sendContainer).append(sendBtn);
					var i = document.createElement('I');
					$(i).attr('class', 'fa fa-paper-plane');
					$(sendBtn).append(i);
					$(sendBtn).click(function () {
						sendMessage();
					});
					// 輸入訊息後按鈕才能送出
					$(textarea).on('keyup', function () {
						var value = $(this).val().trim();
						$(sendBtn).removeAttr('disabled', 'false');
						if (value.length == 0) {
							$(sendBtn).attr('disabled', 'true');
						}
					});
					$(textarea).on('keydown', function (e) {
						if (e.keyCode === 13 && e.altKey) {
							sendMessage();
							return false;
						}
					});
				} else if (chatStatus === 'blocking') {
					var div = document.createElement('DIV');
					$(div).attr('class', 'ps-3 py-2');
					$('.textareaBox').append(div);
					var span = document.createElement('SPAN');
					$(span).append('您已封鎖對方');
					$(div).append(span);
				} else if (chatStatus === 'blocked') {
					var div = document.createElement('DIV');
					$(div).attr('class', 'ps-3 py-2');
					$('.textareaBox').append(div);
					var span = document.createElement('SPAN');
					$(span).append('此用戶已不存在');
					$(div).append(span);
				} else if (chatStatus === 'exceedSentencesLimit') {
					var div = document.createElement('DIV');
					$(div).attr('class', 'ps-3 py-2');
					$('.textareaBox').append(div);
					var span = document.createElement('SPAN');
					$(span).append('12小時後繼續聊天!!');
					$(div).append(span);
				} else if (chatStatus === 'exceedFemaleLimit') {
					var div = document.createElement('DIV');
					$(div).attr('class', 'ps-3 py-2');
					$('.textareaBox').append(div);
					var span = document.createElement('SPAN');
					$(span).append('升級與更多meQUEEN聊天吧!!');
					$(div).append(span);
					var div1 = document.createElement('DIV');
					$(div).append(div1);
					var a = document.createElement('A');
					$(a).attr({
						'class': 'btn btn-link m-0 p-0',
						'href': '/upgrade.asp'
					});
					$(div1).append(a);
					var i = document.createElement('I');
					$(i).attr('class', 'fad fa-crown me-1');
					$(a).append(i);
					var s = document.createElement('SPAN');
					$(s).append('馬上升級');
					$(a).append(s);
				}
				scrollToEnd();
			} else if ('chat' === jsonObj.type) {
				if (parseInt(jsonObj.msgCount) === 3 && isMale === 'true') {
					$('.textareaBox').empty();
					var div = document.createElement('DIV');
					$(div).attr('class', 'ps-3 py-2');
					$('.textareaBox').append(div);
					var span = document.createElement('SPAN');
					$(span).html('12小時後繼續聊天!!');
					$(div).append(span);
				}
				var divWrap = document.createElement('DIV');
				var divParent = document.createElement('DIV');
				var divChild = document.createElement('DIV');
				var dateSpan = document.createElement('SPAN');
				divParent.className += 'd-flex flex-column maxWidth80';
				dateSpan.innerHTML = dateFormat(new Date());
				messagesArea.appendChild(divWrap);
				divWrap.appendChild(divParent);
				divParent.appendChild(divChild);
				divParent.appendChild(dateSpan);
				// 根據發送者是自己還是對方來給予不同的class名, 以達到訊息左右區分
				self === jsonObj.sender ? divWrap.className += 'd-flex justify-content-end mb-2' : divWrap.className += 'd-flex justify-content-start mb-2';
				self === jsonObj.sender ? divChild.className += 'bg-primary text-light border-radius-xl px-3 py-1 me-1 align-self-end shadow wordBreak' : divChild.className += 'bg-dark text-white border-radius-xl px-3 py-1 ms-1 align-self-start shadow wordBreak';
				self === jsonObj.sender ? dateSpan.className += 'text-xs align-self-end me-2' : dateSpan.className += 'text-xs align-self-start ms-2';
				switch (jsonObj.behavior) {
					case 'YAO_CHE_MA_FEI':
						var points = jsonObj.points;
						self === jsonObj.sender ? divChild.innerHTML += '您已和對方要求 💗 ' + points + ' ME 點' : divChild.innerHTML += '對方和您要求 💗 ' + points + ' ME 點';
						if (isMale === 'true') {
							var div = document.createElement('DIV');
							$(divChild).attr('id', jsonObj.id);
							$(divChild).append(div);
							var btn1 = document.createElement('BUTTON');
							$(btn1).attr({
								'class': 'btn btn-light btn-round px-2 py-1 m-0 me-1 border-radius-lg acceptFare resBtn',
								'type': 'button'
							});
							$(div).append(btn1);
							$(btn1).html('給出');
							var btn2 = document.createElement('BUTTON');
							$(btn2).attr({
								'class': 'btn btn-light btn-round px-2 py-1 m-0 border-radius-lg refuseFare resBtn',
								'type': 'button'
							});
							$(btn2).html('下次');
							$(div).append(btn2);

							var result;
							$('BUTTON.acceptFare').click(function () {
								result = true;
								$(this).attr('disabled', true);
								$('BUTTON.refuseFare').attr('disabled', true);
							});
							$('BUTTON.acceptFare').dblclick(function (e) {
								e.preventDefault();
							});
							$('BUTTON.refuseFare').click(function () {
								result = false;
								$(this).attr('disabled', true);
								$('BUTTON.acceptFare').attr('disabled', true);
							});
							$('BUTTON.refuseFare').dblclick(function (e) {
								e.preventDefault();
							});

							$('BUTTON.resBtn').click(function () {
								event.preventDefault();
								let btn = this;
								$.post(
									'/resFare.json',
									{
										historyId: $(btn).closest('DIV.wordBreak').attr('id'),
										result: result,
										whom: friend
									},
									function (data) {
										if (data.response) {
											if (data.resultStatus) {
												var jsonObj = {
													'type': 'chat',
													'sender': self,
													'receiver': friend,
													'behavior': 'CHE_MA_FEI',
													'points': points,
													'ableToReturnFare': true
												};
												websocket.send(JSON.stringify(jsonObj));
											}
											$(btn).closest('DIV').remove();
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
						}
						break;
					case 'CHE_MA_FEI':
						self === jsonObj.sender ? divChild.innerHTML += '您已給 💗 ' + jsonObj.points + ' ME 點' : divChild.innerHTML += '對方給了您 💗 ' + jsonObj.points + ' ME 點';
						if (isMale === 'false' && jsonObj.ableToReturnFare == true) {
							var div = document.createElement('DIV');
							$(divChild).attr('id', jsonObj.id);
							$(divChild).append(div);
							var btn = document.createElement('BUTTON');
							$(btn).attr({
								'class': 'btn btn-light btn-round px-2 py-1 m-0 me-1 border-radius-lg returnFare',
								'type': 'button'
							});
							$(div).append(btn);
							$(btn).html('退回');

							$('BUTTON.returnFare').dblclick(function (e) {
								e.preventDefault();
							});
							$('BUTTON.returnFare').click(function (e) {
								e.preventDefault();
								var btn = this;
								$(btn).attr('disabled', true);
								$.post(
									'/returnFare.json',
									{
										history: $(btn).closest('DIV.wordBreak').attr('id')
									},
									function (data) {
										if (data.response) {
											var jsonObj = {
												'type': 'chat',
												'sender': self,
												'receiver': friend,
												'behavior': 'TUI_HUI_CHE_MA_FEI',
												'points': msg.points
											};
											websocket.send(JSON.stringify(jsonObj));
											$(btn).closest('DIV').remove();
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
						}
						break;
					case 'TUI_HUI_CHE_MA_FEI':
						self === jsonObj.sender ? divChild.innerHTML += '您已退回對方給您的 💗 ' + jsonObj.points + ' ME 點' : divChild.innerHTML += '對方退回您給的 💗 ' + jsonObj.points + ' ME 點';
						break;
					default:
						divChild.innerHTML = jsonObj.message;
				}
				scrollToEnd();
				updateInbox();
			} else if ('button' === jsonObj.type) {
				var divWrap = document.createElement('DIV');
				var divParent = document.createElement('DIV');
				var divChild = document.createElement('DIV');
				divParent.className += 'd-flex flex-column maxWidth80';
				messagesArea.appendChild(divWrap);
				divWrap.appendChild(divParent);
				divParent.appendChild(divChild);
				var contentDiv = document.createElement('DIV');
				divChild.appendChild(contentDiv);
				var dateDiv = document.createElement('DIV');
				dateDiv.className += 'text-xs';
				dateDiv.innerHTML = dateFormat(new Date());
				divChild.appendChild(dateDiv);
				divWrap.className += 'd-flex justify-content-center mb-4';
				divChild.className += 'text-sm wordBreak text-center';
				var icon = document.createElement('I');
				switch (jsonObj.behavior) {
					case 'JI_WO_LAI':
						icon.className += 'fad fa-user-plus';
						contentDiv.appendChild(icon);
						self === jsonObj.sender ? contentDiv.append('您已送出好友邀請') : contentDiv.append('收到好友邀請');
						if (isMale === 'false') {
							var floatDiv = document.createElement('DIV');
							$(floatDiv).attr({
								'class': 'd-flex justify-content-center floatBtn'
							});
							$('DIV.inputContainer').append(floatDiv);
							var borderDiv = document.createElement('DIV');
							$(borderDiv).attr({
								'class': 'border border-primary border-radius-xl text-xs px-3 py-1 shadow wordBreak text-center floatWrap'
							});
							$(floatDiv).append(borderDiv);
							var msgDiv = document.createElement('DIV');
							$(msgDiv).attr('class', 'text-primary');
							$(borderDiv).append(msgDiv);
							var firstLineDiv = document.createElement('DIV');
							$(msgDiv).append(firstLineDiv);
							var firstLineI = document.createElement('I');
							$(firstLineI).attr('class', 'fad fa-user-plus');
							$(firstLineDiv).append(firstLineI);
							var firstLineSpan = document.createElement('SPAN');
							$(firstLineSpan).append('接受後可於聊天室暢聊');
							$(firstLineDiv).append(firstLineSpan);
							var secondLineDiv = document.createElement('DIV');
							$(secondLineDiv).append('同時提供綁定的通訊軟體');
							$(msgDiv).append(secondLineDiv);
							var acceptBtn = document.createElement('BUTTON');
							$(acceptBtn).attr({
								'class': 'btn btn-outline-primary btn-round px-2 py-1 m-0 me-1 accept',
								type: 'button'
							});
							$(acceptBtn).html('接受');
							$(borderDiv).append(acceptBtn);
							var refuseBtn = document.createElement('BUTTON');
							$(refuseBtn).attr({
								'class': 'btn btn-outline-dark btn-round px-2 py-1 m-0 refuse',
								type: 'button'
							});
							$(refuseBtn).html('拒絕');
							$(borderDiv).append(refuseBtn);
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
											$('DIV.floatBtn').empty();
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
											$('DIV.floatBtn').empty();
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
						icon.className += 'fas fa-smile';
						contentDiv.appendChild(icon);
						self === jsonObj.sender ? contentDiv.append('已接受好友邀請') : contentDiv.append('對方已接受您的好友邀請');

						$('DIV.floatBtn').empty();
						var borderDiv = document.createElement('DIV');
						$(borderDiv).attr({
							'class': 'border border-primary border-radius-xl text-xs px-3 py-1 shadow wordBreak text-center floatWrap'
						});
						$('DIV.floatBtn').append(borderDiv);
						var msgDiv = document.createElement('DIV');
						$(msgDiv).attr('class', 'text-primary');
						$(borderDiv).append(msgDiv);

						if (isMale === 'false') {
							var I = document.createElement('I');
							$(I).attr('class', 'fad fa-star-half');
							$(msgDiv).append(I);
							var SPAN = document.createElement('SPAN');
							$(SPAN).append('已成為好友');
							$(msgDiv).append(SPAN);
							var rateBtn = document.createElement('BUTTON');
							$(rateBtn).attr({
								'class': 'btn btn-sm btn-primary btn-round px-2 py-1 m-0 rate',
								'data-bs-target': '#rateModal',
								'data-bs-toggle': 'modal',
								type: 'button'
							});
							$(rateBtn).html('好友評價');
							$(borderDiv).append(rateBtn);
							floatWrapResize();
						}
						if (isMale === 'true') {
							var I = document.createElement('I');
							$(I).attr('class', 'fad fa-star-half');
							$(msgDiv).append(I);
							var SPAN = document.createElement('SPAN');
							$(SPAN).append('已接受您的好友邀請');
							$(msgDiv).append(SPAN);

							var openSocialMediaBtn = document.createElement('BUTTON');
							$(openSocialMediaBtn).attr({
								'class': 'btn btn-sm btn-primary btn-round px-2 py-1 m-0 openSocialMedia',
								type: 'button'
							});
							$(openSocialMediaBtn).html('加通訊軟體');
							$(borderDiv).append(openSocialMediaBtn);
							var rateBtn = document.createElement('BUTTON');
							$(rateBtn).attr({
								'class': 'btn btn-sm btn-dark btn-round px-2 py-1 m-0 ms-1 rate',
								'data-bs-target': '#rateModal',
								'data-bs-toggle': 'modal',
								type: 'button'
							});
							$(rateBtn).html('好友評價');
							$(borderDiv).append(rateBtn);
							floatWrapResize();
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
										if (data.response) {
											var jsonObj = {
												'type': 'button',
												'sender': self,
												'receiver': friend,
												'behavior': 'LAI_KOU_DIAN'
											};
											websocket.send(JSON.stringify(jsonObj));
										}
										if (data.response && data.result === 'isLine') {
											location.href = data.redirect;
										} else if (data.response && data.result === 'isWeChat') {
											var src = 'https://' + location.hostname + data.redirect;
											$('IMG.weChatQRcode').attr('src', src);
											$('A.weChatQRcode').attr('href', src);
											$('#weChatModel').modal('show');
											$('BUTTON.openSocialMedia').removeAttr('disabled');
										} else {
											$('.toast-body').html(data.reason);
											$('.toast').toast('show');
											$('.toast').on('hidden.bs.toast', function () {
												location.href = data.redirect;
											});
										}
									},
									'json'
									);
								return false;
							});
						}
						break;
					case 'BU_JI_LAI':
						icon.className += 'fas fa-frown';
						contentDiv.appendChild(icon);
						if (self === jsonObj.sender) {
							contentDiv.append('您已拒絕對方的好友邀請');
						} else {
							var span = document.createElement('SPAN');
							span.append('對方拒絕您的好友邀請');
							contentDiv.appendChild(span);
							var div = document.createElement('DIV');
							div.append('12小時後才能再要求');
							contentDiv.appendChild(div);
						}
						$('DIV.floatBtn').empty();
						break;
					case 'LAI_KOU_DIAN':
						icon.className += 'fad fa-user-check';
						contentDiv.appendChild(icon);
						self === jsonObj.sender ? contentDiv.append('您開啟了對方的通訊軟體QRcode') : contentDiv.append('對方已開啟了您的通訊軟體QRcode');
						break;
					case 'PING_JIA':
						icon.className += 'far fa-comment-alt-smile';
						contentDiv.appendChild(icon);
						self === jsonObj.sender ? contentDiv.append('您評價了對方') : contentDiv.append('對方給了您評價');
						break;
					case 'KE_FANG_XING':
						icon.className += 'fad fa-images';
						contentDiv.appendChild(icon);
						self === jsonObj.sender ? contentDiv.append('您同意給對方看生活照') : contentDiv.append('對方同意給您看生活照');
						break;
					default:
						console.log(jsonObj.behavior);
				}
				floatWrapResize();
				scrollToEnd();
				updateInbox();
				return;
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
			console.log("[Chatroom]Chat WebSocket is close because : " + reason);
			setTimeout(function () {
				connect();
			}, 1000);
		};

		//發生了錯誤事件
		websocket.onerror = function () {
			console.log("[Chatroom]Chat WebSocket is error");
		}
	}

	// 關閉或離開頁面後關閉連接
	$(window).on("unload", function () {
		websocket.close();
	});


	function sendMessage() {
		var chatInput = document.getElementById("chatInput");
		var message = chatInput.value.trim();
		$('.sendMsgBtn').attr('disabled', 'true');

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

		return yr + '-' + ('0' + mth).substr(-2) + '-' + ('0' + date).substr(-2) + ' ' + ('0' + hour).substr(-2) + ':' + ('0' + minute).substr(-2);
	}

	function scrollToEnd() {
		var scrollHeight = $('.messages').prop('scrollHeight');
		$('.messages').scrollTop(scrollHeight);
		$('DIV#loadingChat').css('display', 'none');
	}

	// 送出評價
	$('BUTTON.commentBtn').click(function (event) {
		event.preventDefault();
		var btn = this;
		$(btn).attr('disabled', true);
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
					var jsonObj = {
						'type': 'button',
						'sender': self,
						'receiver': friend,
						'behavior': 'PING_JIA'
					};
					websocket.send(JSON.stringify(jsonObj));
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$(btn).attr('disabled', false);
				}
			},
			'json'
			);
		return false;
	});

	var url;
	var behavior;
	$('BUTTON#fare').on('click', function () {
		url = '/fare.json';
		behavior = 'CHE_MA_FEI';
	});

	$('BUTTON#reqFare').on('click', function () {
		url = '/reqFare.json';
		behavior = 'YAO_CHE_MA_FEI';
	});

	$('BUTTON.confirmFare').dblclick(function (e) {
		e.preventDefault();
	});
	$('BUTTON.confirmFare').click(function (event) {
		event.preventDefault();
		let howMany = $('INPUT[name="howMany"]').val();
		$.post(
			url,
			{
				howMany: howMany,
				whom: friend
			},
			function (data) {
				if (data.response) {
					var jsonObj = {
						'type': 'chat',
						'sender': self,
						'receiver': friend,
						'behavior': behavior,
						'points': howMany,
						'ableToReturnFare': true
					};
					websocket.send(JSON.stringify(jsonObj));
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
					window.location.reload();
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
	});

	function updateInbox() {
		$('.conversationWrap.active').remove();
		$.post(
			'/updateInbox.json',
			{
				friend: friend
			},
			function (data) {
				var conversationDiv = document.createElement('DIV');
				$(conversationDiv).attr({
					'class': 'conversationWrap position-relative cursor-pointer',
					'id': data.identifier
				});
				if (data.identifier === friend) {
					$(conversationDiv).addClass('active');
				}
				$('#listContent').prepend(conversationDiv);
				$(conversationDiv).click(function () {
					openChatRoom(conversationDiv);
				});
				var contentDiv = document.createElement('DIV');
				$(contentDiv).attr('class', 'd-flex justify-content-between align-items-center p-2');
				$(conversationDiv).append(contentDiv);
				var imgDiv = document.createElement('DIV');
				$(imgDiv).attr('class', 'position-relative');
				$(contentDiv).append(imgDiv);
				if (data.isMatchedOrIsVip === 'true') {
					var icon = document.createElement('I');
					$(icon).attr({
						'class': 'fad text-shadow position-absolute',
						'style': 'right: -5px;bottom: 1px;'
					});
					if (isMale === 'true') {
						$(icon).addClass('fa-users text-primary');
					}
					if (isMale === 'false') {
						$(icon).addClass('fa-crown text-yellow');
					}
					$(imgDiv).append(icon);
				}
				var img = document.createElement('IMG');
				$(img).attr({
					'alt': '大頭照',
					'class': 'rounded-circle shadow',
					'src': data.profileImage,
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
				$(nameA).attr('class', 'font-weight-bold text-dark text-sm mb-1 name');
				$(nameA).append(data.nickname);
				$(nameAndMsgSubDiv).append(nameA);
				var msgP = document.createElement('P');
				$(msgP).attr('class', 'text-sm mb-0 content');
				$(msgP).append(data.content);
				$(nameAndMsgSubDiv).append(msgP);
				var timeAndSeenDiv = document.createElement('DIV');
				$(timeAndSeenDiv).attr('class', 'col-3 d-flex');
				$(contentDiv).append(timeAndSeenDiv);
				var timeAndSeenSubDiv = document.createElement('DIV');
				$(timeAndSeenSubDiv).attr('class', 'ms-auto d-flex flex-column');
				$(timeAndSeenDiv).append(timeAndSeenSubDiv);
				var timeSpan = document.createElement('SPAN');
				$(timeSpan).attr('class', 'text-xs mb-1');
				$(timeSpan).append(data.occurredTime);
				$(timeAndSeenSubDiv).append(timeSpan);
				if (data.notSeenCount) {
					var notSeenDiv = document.createElement('DIV');
					$(notSeenDiv).attr('class', 'd-flex justify-content-center');
					$(timeAndSeenSubDiv).append(notSeenDiv);
					var notSeenCountSpan = document.createElement('SPAN');
					$(notSeenCountSpan).attr('class', 'text-xs text-light bg-danger border-radius-md px-1');
					$(notSeenCountSpan).append(data.notSeenCount);
					$(notSeenDiv).append(notSeenCountSpan);
				}
			},
			'json'
			);

	}

	function floatWrapResize() {
		var size = document.getElementsByClassName("floatWrap").length;
		if (size > 0) {
			$('.messages').css('padding-bottom', $('.floatBtn').height() + 10);
		} else {
			$('.messages').css('padding-bottom', '40px');
		}
	}

	floatWrapResize();

	function appendHistoryData(messages, messagesArea) {
		console.log('messages.len' + messages.length);
		if (messages.length === 0) {
			$nextMsgsPage.val(-1);
		}
		messages.forEach(function (msg) {
			var behavior = msg.behavior;
			var divWrap = document.createElement('DIV');
			var divParent = document.createElement('DIV');
			var divChild = document.createElement('DIV');
			divParent.className += 'd-flex flex-column maxWidth80';
			messagesArea.append(divWrap);
			divWrap.appendChild(divParent);
			divParent.appendChild(divChild);
			if (behavior !== 'LIAO_LIAO' && behavior !== 'DA_ZHAO_HU' && behavior !== 'QUN_FA'
				&& behavior !== 'YAO_CHE_MA_FEI' && behavior !== 'CHE_MA_FEI' && behavior !== 'TUI_HUI_CHE_MA_FEI') {
				var contentDiv = document.createElement('DIV');
				divChild.appendChild(contentDiv);
				var dateDiv = document.createElement('DIV');
				dateDiv.className += 'text-xs';
				dateDiv.innerHTML = msg.occurredStr;
				divChild.appendChild(dateDiv);
				divWrap.className += 'd-flex justify-content-center mb-4';
				divChild.className += 'text-sm wordBreak text-center';
				var icon = document.createElement('I');
				switch (behavior) {
					case 'JI_WO_LAI':
						icon.className += 'fad fa-user-plus';
						contentDiv.appendChild(icon);
						self === msg.sender ? contentDiv.append('您已送出好友邀請') : contentDiv.append('收到好友邀請');
						break;
					case 'JI_NI_LAI':
						icon.className += 'fas fa-smile';
						contentDiv.appendChild(icon);
						self === msg.sender ? contentDiv.append('已接受好友邀請') : contentDiv.append('對方已接受您的好友邀請');
						break;
					case 'BU_JI_LAI':
						icon.className += 'fas fa-frown';
						contentDiv.appendChild(icon);
						if (self === msg.sender) {
							contentDiv.append('您已拒絕對方的好友邀請');
						} else {
							var span = document.createElement('SPAN');
							span.append('對方拒絕您的好友邀請');
							contentDiv.appendChild(span);
							var div = document.createElement('DIV');
							div.append('12小時後才能再要求');
							contentDiv.appendChild(div);
						}
						break;
					case 'LAI_KOU_DIAN':
						icon.className += 'fad fa-user-check';
						contentDiv.appendChild(icon);
						self === msg.sender ? contentDiv.append('您開啟了對方的通訊軟體QRcode') : contentDiv.append('對方已開啟了您的通訊軟體QRcode');
						break;
					case 'PING_JIA':
						icon.className += 'far fa-comment-alt-smile';
						contentDiv.appendChild(icon);
						self === msg.sender ? contentDiv.append('您評價了對方') : contentDiv.append('對方給了您評價');
						break;
					case 'FANG_XING_SHENG_HUO_ZHAO':
						icon.className += 'fad fa-images';
						contentDiv.appendChild(icon);
						self === msg.sender ? contentDiv.append('您向對方要求生活照授權') : contentDiv.append('對方向您要求生活照授權');
						if (msg.showAllPictures === true && self !== msg.sender) {
							var acceptBtn = document.createElement('BUTTON');
							$(acceptBtn).attr({
								'class': 'btn btn-sm btn-warning btn-round px-2 py-1 m-0',
								'type': 'button'
							});
							$(acceptBtn).append('同意');
							$(contentDiv).after(acceptBtn);
							$(acceptBtn).dblclick(function (e) {
								e.preventDefault();
							});
							$(acceptBtn).click(function (event) {
								event.preventDefault();
								$(acceptBtn).attr('disabled', true);
								$.post(
									"/acceptPixAuth.json",
									{
										whom: friend
									},
									function (data) {
										if (data.response) {
											$(acceptBtn).remove();
											var jsonObj = {
												'type': 'button',
												'sender': self,
												'receiver': friend,
												'behavior': 'KE_FANG_XING'
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
							});
						}
						break;
					case 'KE_FANG_XING':
						icon.className += 'fad fa-images';
						contentDiv.appendChild(icon);
						self === msg.sender ? contentDiv.append('您同意給對方看生活照') : contentDiv.append('對方同意給您看生活照');
						break;
					case 'BU_FANG_XING':
						icon.className += 'fad fa-images';
						contentDiv.appendChild(icon);
						self === msg.sender ? contentDiv.append('您不同意給對方看生活照') : contentDiv.append('對方不同意給您看生活照');
						break;
					default:
						console.log(behavior);
				}
				return;
			}
			var dateSpan = document.createElement('SPAN');
			dateSpan.innerHTML = msg.occurredStr;
			divParent.appendChild(dateSpan);
			// 根據發送者是自己還是對方來給予不同的class名, 以達到訊息左右區分
			self === msg.sender ? divWrap.className += 'd-flex justify-content-end mb-2' : divWrap.className += 'd-flex justify-content-start mb-2';
			self === msg.sender ? divChild.className += 'bg-primary text-light border-radius-xl px-3 py-1 me-1 align-self-end shadow wordBreak' : divChild.className += 'bg-dark text-white border-radius-xl px-3 py-1 ms-1 align-self-start shadow wordBreak';
			self === msg.sender ? dateSpan.className += 'text-xs align-self-end me-2' : dateSpan.className += 'text-xs align-self-start ms-2';
			if (behavior === 'YAO_CHE_MA_FEI') {
				self === msg.sender ? divChild.innerHTML = '您已和對方要求 💗 ' + msg.points + ' ME 點' : divChild.innerHTML = '對方和您要求 💗 ' + msg.points + ' ME 點';
				if (isMale === 'true' && msg.reply == null) {
					var div = document.createElement('DIV');
					$(divChild).attr('id', msg.id);
					$(divChild).append(div);
					var btn1 = document.createElement('BUTTON');
					$(btn1).attr({
						'class': 'btn btn-light btn-round px-2 py-1 m-0 me-1 border-radius-lg acceptFare resBtn',
						'type': 'button'
					});
					$(div).append(btn1);
					$(btn1).html('給出');
					var btn2 = document.createElement('BUTTON');
					$(btn2).attr({
						'class': 'btn btn-light btn-round px-2 py-1 m-0 border-radius-lg refuseFare resBtn',
						'type': 'button'
					});
					$(btn2).html('下次');
					$(div).append(btn2);

					var result;
					$('BUTTON.acceptFare').click(function () {
						result = true;
						$(this).attr('disabled', true);
						$(this).siblings('BUTTON.refuseFare').attr('disabled', true);
					});
					$('BUTTON.acceptFare').dblclick(function (e) {
						e.preventDefault();
					});
					$('BUTTON.refuseFare').click(function () {
						result = false;
						$(this).attr('disabled', true);
						$(this).siblings('BUTTON.acceptFare').attr('disabled', true);
					});
					$('BUTTON.refuseFare').dblclick(function (e) {
						e.preventDefault();
					});

					$('BUTTON.resBtn').click(function () {
						event.preventDefault();
						let btn = this;
						$.post(
							'/resFare.json',
							{
								historyId: $(btn).closest('DIV.wordBreak').attr('id'),
								result: result,
								whom: friend
							},
							function (data) {
								if (data.response) {
									if (data.resultStatus) {
										var jsonObj = {
											'type': 'chat',
											'sender': self,
											'receiver': friend,
											'behavior': 'CHE_MA_FEI',
											'points': msg.points,
											'ableToReturnFare': true
										};
										websocket.send(JSON.stringify(jsonObj));
									}
									$(btn).closest('DIV').remove();
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
				}
				return;
			}
			if (behavior === 'CHE_MA_FEI') {
				self === msg.sender ? divChild.innerHTML = '您已給 💗 ' + msg.points + ' ME 點' : divChild.innerHTML = '對方給了您 💗 ' + msg.points + ' ME 點';
				if (isMale === 'false' && msg.ableToReturnFare == true) {
					var div = document.createElement('DIV');
					$(divChild).attr('id', msg.id);
					$(divChild).append(div);
					var btn = document.createElement('BUTTON');
					$(btn).attr({
						'class': 'btn btn-light btn-round px-2 py-1 m-0 me-1 border-radius-lg returnFare',
						'type': 'button'
					});
					$(div).append(btn);
					$(btn).html('退回');

					$('BUTTON.returnFare').dblclick(function (e) {
						e.preventDefault();
					});
					$('BUTTON.returnFare').click(function (e) {
						e.preventDefault();
						var btn = this;
						$(btn).attr('disabled', true);
						$.post(
							'/returnFare.json',
							{
								history: $(btn).closest('DIV.wordBreak').attr('id')
							},
							function (data) {
								if (data.response) {
									var jsonObj = {
										'type': 'chat',
										'sender': self,
										'receiver': friend,
										'behavior': 'TUI_HUI_CHE_MA_FEI',
										'points': msg.points
									};
									websocket.send(JSON.stringify(jsonObj));
									$(btn).closest('DIV').remove();
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
				}
				return;
			}
			if (behavior === 'TUI_HUI_CHE_MA_FEI') {
				self === msg.sender ? divChild.innerHTML = '您已退回對方給您的 💗 ' + msg.points + ' ME 點' : divChild.innerHTML = '對方退回您給的 💗 ' + msg.points + ' ME 點';
				return;
			}
			divChild.innerHTML = msg.greeting;
		});
	}

	var timeout;
	var listContent = document.getElementById("listContent");
	listContent.addEventListener('scroll', handler);
	function handler() {
		clearTimeout(timeout);
		timeout = setTimeout(function () {
			if (listContent.scrollHeight - listContent.scrollTop === listContent.clientHeight) {
				listContent.removeEventListener('scroll', handler);
				var $nextPage = $('INPUT[name="nextPage"]');
				var p = $nextPage.val();
				var load = document.createElement('DIV');
				$(load).attr('class', 'loadMore');
				$('#listContent').append(load);
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
								appendInboxData(data);
								listContent.addEventListener('scroll', handler);
							} else if (data.length == 0) {
								$nextPage.val(-1);
								var div = document.createElement('DIV');
								$(div).attr('class', 'text-center text-xs mt-4');
								$(div).append('沒有更多訊息囉！');
								$('#listContent').append(div);
								listContent.removeEventListener('scroll', handler);
							}
						},
						'json'
						);
				}
			}
		}, 50);
	}

	var chatTimeOut;
	$('.conversationWrap').click(function () {
		var conversation = this;
		openChatRoom(conversation);
	});

	function openChatRoom(conversation) {
		friend = $(conversation).attr('id');
		$('.floatBtn').remove();
		$('.textareaBox').empty();
		$('DIV#loadingChat').css('display', 'block');
		$('.conversationWrap').each(function () {
			$(this).removeClass('active');
		});
		$(conversation).addClass('active');
		$.post(
			'/openChatRoom.json',
			{
				'friend': friend
			}
		);
		chatTimeOut = setTimeout(function () {
			$('#messagesArea').html('');
			connect();
			var img = $(conversation).find('IMG').attr('src');
			var name = $(conversation).find('A.name').html();
			$('#friendLink').attr('href', '/profile/' + friend + '/');
			$('#friendImg').attr('src', img);
			$('#friendName').html(name);
			messages.addEventListener('scroll', messagesHandler);
		}, 1000);
	}

	function appendInboxData(data) {
		data.forEach(function (item) {
			var conversationDiv = document.createElement('DIV');
			$(conversationDiv).attr({
				'class': 'conversationWrap position-relative cursor-pointer',
				'id': item.identifier
			});
			if (item.identifier === friend) {
				$(conversationDiv).addClass('active');
			}
			$('#listContent').append(conversationDiv);
			$(conversationDiv).click(function () {
				openChatRoom(conversationDiv);
			});
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
				if (isMale === 'true') {
					$(icon).addClass('fa-users text-primary');
				}
				if (isMale === 'false') {
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
			$(nameA).attr('class', 'font-weight-bold text-dark text-sm mb-1 name');
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

	var messagesTimeOut;
	var messages = document.getElementById("messages");
	messages.addEventListener('scroll', messagesHandler);
	function messagesHandler() {
		clearTimeout(messagesTimeOut);
		messagesTimeOut = setTimeout(function () {
			if (messages.scrollTop === 0) {
				messages.removeEventListener('scroll', messagesHandler);
				var nextMsgsPage = $nextMsgsPage.val();
				var load = document.createElement('DIV');
				$(load).attr('class', 'loadMore mb-3');
				$('#messagesArea').prepend(load);
				if (nextMsgsPage > 0) {
					$.post(
						'/loadMoreMsgs.json',
						{
							'friend': friend,
							'p': nextMsgsPage
						},
						function (data) {
							var json = JSON.parse(data);
							$(load).remove();
							if (json.length !== 0) {
								$nextMsgsPage.val(parseInt($nextMsgsPage.val()) + 1);
								var historyDiv = document.createElement('DIV');
								$(historyDiv).attr('class', 'historyDiv');
								$('#messagesArea').prepend(historyDiv);
								appendHistoryData(json, historyDiv);
								messages.addEventListener('scroll', messagesHandler);
							} else if (json.length == 0) {
								$nextMsgsPage.val(-1);
								var div = document.createElement('DIV');
								$(div).attr('class', 'text-center text-xs my-3');
								$(div).append('沒有更多訊息囉！');
								$('#messagesArea').prepend(div);
								messages.removeEventListener('scroll', messagesHandler);
							}
							messages.scrollTop = 50;
						}
					);
				}
			}
		}, 50);
	}
});