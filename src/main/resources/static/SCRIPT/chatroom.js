$(document).ready(function () {
	// 解決在手機版瀏覽器沒辦法呈現完整高度
	let vh = window.innerHeight * 0.01;
	document.querySelector('.chatContainer').style.setProperty('--vh', `${vh}px`);
	window.addEventListener('resize', () => {
		let vh = window.innerHeight * 0.01;
		document.querySelector('.chatContainer').style.setProperty('--vh', `${vh}px`);
	});

	// 目前聊天的是哪個tab
	var whichTab;
	$('.conversationWrap').each(function () {
		var item = this;
		if ($(item).hasClass('active')) {
			$(item).closest('DIV.tab-pane').addClass('active');
			whichTab = $(item).parent('DIV.tab-pane').attr('id');
		}
	});
	if (whichTab === 'first') {
		$('A[href="#first"]').addClass('active');
	} else if (whichTab === 'second') {
		$('A[href="#second"]').addClass('active');
	} else if (typeof (whichTab) === 'undefined') {
		$('A[href="#first"]').addClass('active');
		$('DIV#first').addClass('active');
	}

	// 手機版時可以拉出聊天列表
	$('DIV.showSideBar').click(function () {
		$('DIV.chatList').addClass('showFromTheSide');
		$(this).addClass('d-none');
	});
	$('DIV.hideSideBar').click(function () {
		$('DIV.chatList').removeClass('showFromTheSide');
		$('DIV.showSideBar').removeClass('d-none');
	});
	$(window).resize(function () {
		$('DIV.chatList').removeClass('showFromTheSide');
		$('DIV.showSideBar').removeClass('d-none');
	});
});