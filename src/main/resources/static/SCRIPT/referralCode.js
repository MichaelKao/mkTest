$(document).ready(function () {
	const hostName = location.hostname;
	const referralCode = document.getElementById('referralCode').innerHTML;

	const shareData = {
		title: 'MDN',
		text: 'Youngme 養蜜之旅, 註冊時輸入我的專屬邀請碼：' + $('#referralCode').html(),
		url: 'https://' + hostName + '/i/' + referralCode
	}

	const btn = document.querySelector('button.share');
	const resultPara = document.querySelector('.result');

	// 分享
	btn.addEventListener('click', async () => {
		try {
			await navigator.share(shareData);
		} catch (err) {
			console.log(err);
		}
	});

	// 複製
	$('BUTTON#referralCodeCopy').click(function () {
		navigator.clipboard.writeText(referralCode)
			.then(() => {
				$('.toast-body').html('已複製');
				$('.toast').toast('show');
			})
			.catch(err => {
				console.log('Something went wrong', err);
			})
	});


	var $descendants = $('DIV.descendants');
	var $pagination = $('DIV.pagination');

	$.post(
		'/descendants.json',
		{
			p: 0,
			s: 10
		},
		function (data) {
			if (data.response) {
				descendants(data);
			} else {
				$('.toast-body').html(data.reason);
				$('.toast').toast('show');
			}
		},
		'json'
		);
	return false;

	// 顯示下線的名單
	function descendants(data) {
		data.result.descendants.forEach(function (item) {
			var d = new Date(item.timestamp.replace(" ", "T"));
			var year = d.getFullYear();
			var month = (d.getMonth() + 1) < 10 ? '0' + (d.getMonth() + 1) : d.getMonth() + 1;
			var date = d.getDate() < 10 ? '0' + d.getDate() : d.getDate();
			var hour = d.getHours() < 10 ? '0' + d.getHours() : d.getHours();
			var minute = d.getMinutes() < 10 ? '0' + d.getMinutes() : d.getMinutes();

			let div = document.createElement('DIV');
			$(div).attr('class', 'd-flex align-items-center w-80 mx-auto');
			$descendants.append(div);
			let i = document.createElement('I');
			if (item.vip == true) {
				$(i).attr('class', 'fas fa-check-circle me-1 text-primary');
			} else {
				$(i).attr('class', 'fas fa-check-circle me-1');
			}
			$(div).append(i);
			let name = document.createElement('SPAN');
			$(name).attr('class', 'text-sm');
			$(name).html(item.nickname);
			$(div).append(name);
			let timestamp = document.createElement('SPAN');
			$(timestamp).attr('class', 'ms-auto text-xs');
			$(timestamp).html(year + '-' + month + '-' + date + ' ' + hour + ':' + minute);
			$(div).append(timestamp);
		});

		// 有上一頁
		if (typeof data.result.pagination.previous !== 'undefined') {
			let span = document.createElement('SPAN');
			$(span).attr({
				'class': 'mx-2 position-absolute top-0 bottom-0 d-flex align-items-center pageBtn',
				'data-page': data.result.pagination.previous
			});
			$pagination.append(span);
			let i = document.createElement('I');
			$(i).attr('class', 'fal fa-chevron-left fontSize25');
			$(span).append(i);
		}

		// 有下一頁
		if (typeof data.result.pagination.next !== 'undefined') {
			let span = document.createElement('SPAN');
			$(span).attr({
				'class': 'mx-2 position-absolute top-0 bottom-0 right-0 d-flex align-items-center pageBtn',
				'data-page': data.result.pagination.next
			});
			$pagination.append(span);
			let i = document.createElement('I');
			$(i).attr('class', 'fal fa-chevron-right fontSize25');
			$(span).append(i);
		}

		$('SPAN.pageBtn').click(function (event) {
			event.preventDefault();
			var div = this;
			$.post(
				'/descendants.json',
				{
					p: $(div).data('page'),
					s: 10
				},
				function (data) {
					if (data.response) {
						$descendants.empty();
						$pagination.empty();
						descendants(data);
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
});