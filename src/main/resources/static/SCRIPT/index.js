$(document).ready(function () {

	if ($('INPUT[name="signIn"]').val() === 'false') {
		let vh = window.innerHeight * 0.01;
		document.querySelector('HEADER.container').style.setProperty('--vh', `${vh}px`);
		window.addEventListener('resize', () => {
			let vh = window.innerHeight * 0.01;
			document.querySelector('HEADER.container').style.setProperty('--vh', `${vh}px`);
		});
	}

	// 登入後 Line Notify 提醒
	if ($('INPUT[name="signIn"]').val() === 'true' && $('INPUT[name="guidance"]').val() === 'true') {
		showModal();
	}

	let $mobileRefreshBtn = $('BUTTON#mobileRefreshBtn');

	if (isMobile() && $('INPUT[name="signIn"]').val() === 'true') {
		var currentPageType = getCookie('currentPageType');
		if ($('INPUT[name="gender"]').val() === 'male') {
			if (currentPageType !== '') {
				// 手機版按照上一次看的頁面類型 cookie 來 highlight tabs
				var a = $('A.' + currentPageType + 'A');
				a.addClass('active');
				loverMobilePage(a);
				$mobileRefreshBtn.data('type', currentPageType);
			} else {
				var a = $('A.reliefA');
				a.addClass('active');
				loverMobilePage(a);
				$mobileRefreshBtn.data('type', 'relief');
			}
		} else {
			if (currentPageType !== '') {
				var a = $('A.' + currentPageType + 'A');
				a.addClass('active');
				loverMobilePage(a);
				$mobileRefreshBtn.data('type', currentPageType);
			} else {
				var a = $('A.vipA');
				a.addClass('active');
				loverMobilePage(a);
				$mobileRefreshBtn.data('type', 'vip');
			}
		}
	}

	// 網頁版
	if (!isMobile() && $('INPUT[name="signIn"]').val() === 'true') {
		$('.loadingWrap').css('display', 'block');
		$.post(
			'/indexWebView.json',
			(data) => {
			if (data.vip) {
				appendData(data.vip, 'vip', $('DIV.vip'), $('.vipPageBtn'));
			}
			if (data.relief) {
				appendData(data.relief, 'relief', $('DIV.relief'), $('.reliefPageBtn'));
			}
			if (data.active) {
				appendData(data.active, 'active', $('DIV.active'), $('.activePageBtn'));
			}
			if (data.register) {
				appendData(data.register, 'register', $('DIV.register'), $('.registerPageBtn'));
			}
			$('.loadingWrap').css('display', 'none');
		},
			'json'
			);
	}

	function appendData(data, memberType, memberWrap, pageBtnWrap) {
		data.result.forEach(function (item) {
			var relief = item.relief;
			var vvip = item.vvip;
			var relationship = item.relationship;

			var a = document.createElement('A');
			$(a).attr({
				'class': 'position-relative m-1',
				'href': 'profile/' + item.identifier + '/'
			});
			memberWrap.append(a);
			$(a).append(`<DIV class="infos"><IMG class="border-radius-md" loading="lazy" src="${item.profileImage}"/></DIV>`);

			if (vvip || relief) {
				var badgeDiv = document.createElement('DIV');
				$(badgeDiv).attr({
					'class': 'position-absolute right-0 text-center',
					'style': 'width: 32px; top: 5px;'
				});
				$(a).append(badgeDiv);
				if (vvip) {
					$(badgeDiv).append(`<I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>`);
				}
				if (relief) {
					$(badgeDiv).append(`<I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>`);
				}
			}
			if (item.following) {
				$(a).append(`<DIV class="position-absolute left-0 text-center" style="width: 32px; top: 5px;">
						<I class="fas fa-heart-circle text-pink fontSize22"></I>
						</DIV>`);
			}
			var infoDiv = document.createElement('DIV');
			$(infoDiv).attr(
				'class',
				'position-absolute imageShadow bottom-0 left-0 right-0 mx-3 mb-1 py-0 text-bolder text-dark bg-white opacity-7 border-radius-md p-1 text-xs text-center'
				);
			$(a).append(infoDiv);
			$(infoDiv).append(`<DIV><SPAN>${item.nickname}</SPAN>
					<SPAN class="ms-2">${item.age}</SPAN></DIV>`);
			if (relationship) {
				$(infoDiv).append(`<DIV><SPAN>尋找</SPAN><SPAN>${relationship}</SPAN></DIV>`);
			}
			var locDiv = document.createElement('DIV');
			$(infoDiv).append(locDiv);
			item.location.forEach(function (item) {
				$(locDiv).append(`<SPAN class="me-1">${item}</SPAN>`);
			});
		});

		if (typeof (data.hasPrev) != 'undefined') {
			var prevBtn = document.createElement('BUTTON');
			$(prevBtn).attr({
				'class': 'btn btn-primary btn-round pageBtn text-lg mx-1 m-0 px-2 py-1',
				'data-type': memberType,
				'data-page': data.hasPrev
			});
			pageBtnWrap.append(prevBtn);
			var prevI = document.createElement('I');
			$(prevI).attr('class', 'fad fa-angle-double-left ms-1');
			$(prevBtn).append(prevI);
			$(prevBtn).append('上一頁');
			$(prevBtn).click(function () {
				$('.loadingWrap').css('display', 'block');
				let button = this;
				let wrap;
				var pageBtnWrap;
				let p = $(button).data('page');
				let type = $(button).data('type');
				if ($(button).parent('DIV').attr('id') === 'mobilePageBtn') {
					wrap = $('DIV.mobileMode');
					pageBtnWrap = $('DIV#mobilePageBtn');
				} else {
					wrap = $('DIV.' + type);
					pageBtnWrap = $('DIV.' + type + 'PageBtn');
				}
				$.post(
					'/seeMoreLover.json',
					{
						p: p,
						type: type,
						mobile: isMobile() ? true : false
					},
					(data) => {
					wrap.empty();
					pageBtnWrap.empty();
					appendData(data, type, wrap, pageBtnWrap);
					$('.loadingWrap').css('display', 'none');
				},
					'json'
					);
				return false;
			});
		}
		if (typeof (data.hasNext) != 'undefined') {
			var nextBtn = document.createElement('BUTTON');
			$(nextBtn).attr({
				'class': 'btn btn-primary btn-round pageBtn text-lg mx-1 m-0 px-2 py-1',
				'data-type': memberType,
				'data-page': data.hasNext
			});
			pageBtnWrap.append(nextBtn);
			$(nextBtn).append('下一頁');
			var nextI = document.createElement('I');
			$(nextI).attr('class', 'fad fa-angle-double-right ms-1');
			$(nextBtn).append(nextI);
			$(nextBtn).click(function () {
				$('.loadingWrap').css('display', 'block');
				let button = this;
				let wrap;
				var pageBtnWrap;
				let p = $(button).data('page');
				let type = $(button).data('type');
				if ($(button).parent('DIV').attr('id') === 'mobilePageBtn') {
					wrap = $('DIV.mobileMode');
					pageBtnWrap = $('DIV#mobilePageBtn');
				} else {
					wrap = $('DIV.' + type);
					pageBtnWrap = $('DIV.' + type + 'PageBtn');
				}
				$.post(
					'/seeMoreLover.json',
					{
						p: p,
						type: type,
						mobile: isMobile() ? true : false
					},
					(data) => {
					wrap.empty();
					pageBtnWrap.empty();
					appendData(data, type, wrap, pageBtnWrap);
					$('.loadingWrap').css('display', 'none');
				},
					'json'
					);
				return false;
			});
		}
	}

	$('A.mobileModeA').click(function () {
		let a = this;
		loverMobilePage(a);
		$('.loadingWrap').css('display', 'block');
	});

	function loverMobilePage(a) {
		let type = $(a).data('type');
		$mobileRefreshBtn.data('type', type);
		let p = 0;
		if (getCookie(type + 'Page') !== '') {
			p = getCookie(type + 'Page');
		}
		let wrap = $('DIV.mobileMode');
		let pageBtnWrap = $('DIV#mobilePageBtn');
		$.post(
			'/seeMoreLover.json',
			{
				p: p,
				type: type,
				mobile: true
			},
			(data) => {
			wrap.empty();
			pageBtnWrap.empty();
			$('A.mobileModeA').each(function (i, obj) {
				$(this).removeClass('active');
			});
			$(a).addClass('active');
			appendData(data, type, wrap, pageBtnWrap);
			$('.loadingWrap').css('display', 'none');
		},
			'json'
			);
		return false;
	}

	$('BUTTON.refreshBtn').click(function () {
		$('.loadingWrap').css('display', 'block');
		let button = this;
		let wrap;
		var pageBtnWrap;
		let type = $(button).data('type');
		if ($(button).attr('id') === 'mobileRefreshBtn') {
			wrap = $('DIV.mobileMode');
			pageBtnWrap = $('DIV#mobilePageBtn');
		} else {
			wrap = $('DIV.' + type);
			pageBtnWrap = $('DIV.' + type + 'PageBtn');
		}
		$.post(
			'/seeMoreLover.json',
			{
				p: 0,
				type: type,
				mobile: isMobile() ? true : false
			},
			(data) => {
			wrap.empty();
			pageBtnWrap.empty();
			appendData(data, type, wrap, pageBtnWrap);
			$('.loadingWrap').css('display', 'none');
		},
			'json'
			);
		return false;
	});

	// 判斷是否為手機裝置
	function isMobile() {
		var mobileAgents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
		var mobileFlag = false;
		// 根據 userAgent 判斷是否是手機
		for (var v = 0; v < mobileAgents.length; v++) {
			if (navigator.userAgent.indexOf(mobileAgents[v]) > 0) {
				mobileFlag = true;
				break;
			}
		}
		var screenWidth = $(window).width();
		var screenHeight = $(window).height();
		// 根據 screen 判斷
		if (screenWidth < 769 && screenHeight < 950) {
			mobileFlag = true;
		}
		return mobileFlag;
	}

	// 取得 cookie
	function getCookie(cname) {
		var name = cname + '=';
		var ca = document.cookie.split(';');
		for (var i = 0; i < ca.length; i++) {
			var c = ca[i].trim();
			if (c.indexOf(name) === 0) {
				return c.substring(name.length, c.length);
			}
		}
		return '';
	}

	// 設定 cookie
	function setCookie(cname, cvalue, expireMinute) {
		var d = new Date();
		d.setTime(d.getTime() + (expireMinute * 60 * 1000));
		var expires = "expires=" + d.toGMTString();
		document.cookie = cname + "=" + cvalue + "; " + expires;
	}
	// LINE Notify 的提醒 modal
	function showModal() {
		if (getCookie('youngMeLineNotifyModal') === '') {
			$('#lineNotifyModal').modal('show');
			setCookie('youngMeLineNotifyModal', 'NO', 1 * 60 * 24);
			return;
		}
	}

	if ($('INPUT[name="guidance"]').val() === 'false') {
		$('#guide').modal('show');
		$('#guide').on('hidden.bs.modal', function () {
			showModal();
		});
	}
	$('#guide').on('shown.bs.modal', function (event) {
		$('.carousel').flickity({
			'autoPlay': 2500,
			'prevNextButtons': false,
			'pageDots': false
		});
	});

	var result = document.querySelector('.result');
	var cropBtn = document.querySelector('#cropBtn');
	var input;
	var $cropModal = $('#cropModal');
	var $reliefBtn = $('.reliefBtn');
	var cropper;

	$('INPUT[name="image"]').on("change", function (e) {
		var files = e.target.files;
		input = this;

		var done = function (url) {
			this.value = '';
			let img = document.createElement('img');
			img.id = 'image';
			img.src = url;
			result.innerHTML = '';
			result.appendChild(img);
			cropper = new Cropper(img, {
				viewMode: 1,
				dragMode: 'move',
				autoCropArea: 1
			});
			$(cropBtn).css('display', 'inline');
		};

		var reader;
		var file;
		if (files && files.length > 0) {
			file = files[0];
			if (URL) {
				done(URL.createObjectURL(file));
			} else if (FileReader) {
				reader = new FileReader();
				reader.onload = function (e) {
					done(reader.result);
				};
				reader.readAsDataURL(file);
			}
		}
	});

	$cropModal.on('hidden.bs.modal', function () {
		if (cropper) {
			cropper.destroy();
			cropper = null;
			input.value = '';
			result.innerHTML = '';
			$(cropBtn).css('display', 'none');
			$('.progress-bar').css('width', '0%').attr('aria-valuenow', '0%');
			$('.progress-percentage > SPAN').html('0%');
			$(cropBtn).html('上傳');
			$(cropBtn).removeAttr('disabled')
		}
	});

	$(cropBtn).click(function () {
		var canvas;
		let CutWidth;
		let CutHeight;
		$(cropBtn).html('請稍後...');
		$(cropBtn).attr('disabled', 'true');

		if (image.width < image.height && image.width < 600) {
			CutWidth = image.width;
			CutHeight = image.width;
		} else if (image.height < image.width && image.height < 600) {
			CutWidth = image.height;
			CutHeight = image.height;
		} else if (image.width < 100 && image.height < 100) {
			alert('解析度太低');
			return;
		} else {
			CutWidth = 600;
			CutHeight = 600;
		}
		if (cropper) {
			canvas = cropper.getCroppedCanvas({
				width: CutWidth,
				height: CutHeight,
			});
			canvas.toBlob(function (blob) {
				var OK = uploadpic(blob);
				if (!OK && typeof (OK) != 'undefined') {
					alert('上傳失敗');
					$cropModal.modal('hide');
				}

				input.value = '';
			});
		}
	});

	function uploadpic(blob) {
		var file = new FormData();
		file.append('file', blob);

		$.ajax({
			url: '/uploadIdentity',
			cache: false,
			contentType: false,
			processData: false,
			data: file,
			type: 'post',
			success: function (data) {
				$('.toast-body').html('上傳成功，等待人員審核');
				$('.toast').toast('show');
				$cropModal.modal('hide');
				$reliefBtn.remove();
			},
			error: function (request, status, error) {
				$('.toast-body').html('上傳失敗');
				$('.toast').toast('show');
				$cropModal.modal('hide');
				return false;
			}, xhr: function () {
				var xhr = new window.XMLHttpRequest();
				xhr.upload.addEventListener("progress", function (progressEvent) {
					if (progressEvent.lengthComputable) {
						var percentComplete = progressEvent.loaded / progressEvent.total;
						var percentVal = Math.round(percentComplete * 100) + '%';
						$('.progress-bar').css('width', percentVal).attr('aria-valuenow', percentVal);
						$('.progress-percentage > SPAN').html(percentVal);
					}
				}, false);
				return xhr;
			}
		}).fail(function () {
			$cropModal.modal('hide');
		});
	}

	$(window).scroll(function () {
		$('.hideMe').each(function () {
			var bottom_of_object = $(this).offset().top + $(this).outerHeight();
			var bottom_of_window = $(window).scrollTop() + $(window).height();

			if (bottom_of_window > bottom_of_object - 180) {
				$(this).animate({'opacity': '1'}, 200);
			}
		});

	});

	function hasScrollbar() {
		return document.body.scrollHeight > (window.innerHeight || document.documentElement.clientHeight);
	}

	function getScrollbarWidth() {

		var scrollDiv = document.createElement("div");
		scrollDiv.style.cssText = 'width: 99px; height: 99px; overflow: scroll; position: absolute; top: -9999px;';
		document.body.appendChild(scrollDiv);
		var scrollbarWidth = scrollDiv.offsetWidth - scrollDiv.clientWidth;
		document.body.removeChild(scrollDiv);

		return scrollbarWidth;

	}

	$('.modal').on('shown.bs.modal', function (e) {
//		document.body.style = 'padding-right: 0;'

	});
});