$(document).ready(function () {

	$('DIV.star').each(function () {
		var div = this;
		showStar(this);
	});

	function showStar(div) {
		var starCount = $(div).data('star');
		var starred = '&#9733;';
		var star = '&#9734;';

		$(div).html(starred.repeat(starCount) + star.repeat(5 - starCount));
	}

	if (window.matchMedia('screen and (min-width:768px)')) {
		$('DIV.lessThan768').html($('DIV.moreThan768').html());
	}

	var mm = window.matchMedia('screen and (max-width:768px)');
	resizeWidth(mm);
	mm.addListener(resizeWidth);

	function resizeWidth(pMatchMedia) {
		if (pMatchMedia.matches) {
			$('DIV.lessThan768').html($('DIV.moreThan768').html());
			$('DIV.moreThan768').empty()
		} else {
			$('DIV.moreThan768').html($('DIV.lessThan768').html());
			$('DIV.lessThan768').empty()
		}
	}

	$('.imgContainer').css({'max-height': $(window).height() - 250 + "px"});

	var result = document.querySelector('.result');
	var cropBtn = document.querySelector('#cropBtn');
	var input;
	var $cropModal = $('#cropModal');
	var cropper;
	var relief = document.querySelector('.relief');

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

		if (image.width < image.height && image.width < 800) {
			CutWidth = image.width;
			CutHeight = image.width;
		} else if (image.height < image.width && image.height < 800) {
			CutWidth = image.height;
			CutHeight = image.height;
		} else if (image.width < 100 && image.height < 100) {
			alert('解析度太低');
			return;
		} else {
			CutWidth = 800;
			CutHeight = 800;
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
				var i = document.createElement('I');
				$(i).attr('class', 'fad fa-shield-check fontSize25');
				$('DIV.reliefBadge').empty().append(i);
				$cropModal.modal('hide');
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

	$.post(
		'/peek.json',
		{
			whom: $('INPUT[name="whom"]').val()
		});

	$('BUTTON.fav').click(function () {
		$(this).toggleClass('liked');
		$.post(
			'/favorite.json',
			{
				identifier: $('INPUT[name="whom"]').val()
			},
			'json'
			);
	});

	$('BUTTON.block').click(function () {
		$.post(
			'/block.json',
			{
				identifier: $('INPUT[name="whom"]').val()
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

	var $giftModal = $('#giftModal');
	var $modal = $('#modal');
	var url;

	$('.gift').click(function () {
		$giftModal.modal('show');
		url = '/fare.json';
	});

	$('A#giveMeLine').on("click", function () {
		$modal.modal('show');
		url = '/stalking.json';
	});

	$('A#greeting').on("click", function () {
		$modal.modal('show');
		url = '/greet.json';
	});

	$('.confirmBtn').click(function (event) {
		event.preventDefault();
		let form = this;
		$.post(
			url,
			{
				howMany: $('INPUT[name="howMany"]').val(),
				whom: $('INPUT[name="whom"]').val(),
				what: $('TEXTAREA[name="what"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$giftModal.modal('hide');
					$modal.modal('hide');
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('A#openLine').click(function () {
		whom = $('INPUT[name="whom"]').val();
		$.post(
			'/maleOpenLine.json',
			{
				whom: whom
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

	$('#blockedModal').modal('show');
	$('#blockedModal').on('hidden.bs.modal', function () {
		location.href = '/';
	});


	$('BUTTON.picturesAuth').dblclick(function (e) {
		e.preventDefault();
	});
	$('BUTTON.picturesAuth').click(function () {
		whom = $('INPUT[name="whom"]').val();
		let btn = this;
		$('.picturesAuth').each(function () {
			$(this).attr('disabled', true);
		});
		$.post(
			'/picturesAuth.json',
			{
				whom: whom
			},
			function (data) {
				if (data.response) {
					$('.picturesAuth').each(function () {
						$(this).html('等待回覆');
					});
				} else if (!data.response && data.reason === 'non-vip') {
					$('#upgradeVIP').modal('show');
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('TEXTAREA[name="comment"]').append($('DIV.selfComment').html());
	$('INPUT[name="rating"]').each(function () {
		if ($(this).val() == $('DIV.selfStar').data('star')) {
			$(this).attr('checked', true);
		}
	});

	$('BUTTON.commentBtn').click(function (event) {
		event.preventDefault();
		var btn = this;
		$(btn).attr('disabled', true);
		var rate = $('INPUT[name="rating"]:checked').val();
		if (rate === undefined) {
			rate = null;
		}
		var comment = $('TEXTAREA[name="comment"]').val();
		$.post(
			'/rate.json',
			{
				whom: $('INPUT[name="whom"]').val(),
				rate: rate,
				comment: comment
			},
			function (data) {
				if (data.response) {
					$('DIV.selfComment').html(comment);
					$('DIV.selfStar').data('star', rate);
					showStar($('DIV.selfStar'));
					$('#rateModal').modal('hide');
					$(btn).attr('disabled', false);
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('BUTTON.moreRate').click(function () {
		let btn = this;
		p = $(btn).data('page');
		console.log(p)
		$.post(
			'/moreRate.json',
			{
				p: parseInt(p) + 1,
				whom: $('INPUT[name="whom"]').val()
			},
			(data) => {
			if (data.response) {
				var rateBox = $('DIV.rateBox');
				rateBox.empty();
				data.result.forEach(function (item) {
					let outterDiv = document.createElement('DIV');
					$(outterDiv).attr('class', 'card flex-row px-3 py-2 mb-2 align-items-center');
					rateBox.append(outterDiv);

					let imgDiv = document.createElement('DIV');
					$(imgDiv).attr('class', 'col-3 col-sm-2');
					outterDiv.append(imgDiv);

					let img = document.createElement('IMG');
					$(img).attr({
						'class': 'border-radius-sm',
						'src': item.profileImage,
						'width': '50'
					});
					imgDiv.append(img);

					let contentDiv = document.createElement('DIV');
					$(contentDiv).attr('class', 'ms-2');
					outterDiv.append(contentDiv);

					let nameDiv = document.createElement('DIV');
					$(nameDiv).attr('class', 'text-xs');
					$(nameDiv).append(item.nickname);
					contentDiv.append(nameDiv);

					let rate = item.rate;
					let starDiv = document.createElement('DIV');
					$(starDiv).attr({
						'class': 'star text-lg',
						'data-star': rate
					});
					contentDiv.append(starDiv);
					showStar($(starDiv));

					let commentDiv = document.createElement('DIV');
					$(commentDiv).attr({
						'class': 'text-sm',
						'data-star': item.comment
					});
					$(commentDiv).append(item.comment);
					contentDiv.append(commentDiv);

					if ($('INPUT[name="identifier"]').val() === item.identifier) {
						$(commentDiv).addClass('selfComment');
						$(starDiv).addClass('selfStar');

						let editI = document.createElement('I');
						$(editI).attr({
							'class': 'fad fa-edit fontSize22 col-1 position-absolute top-1 right-1 cursor-pointer',
							'data-bs-target': '#rateModal',
							'data-bs-toggle': 'modal'
						});
						outterDiv.append(editI);

						$('TEXTAREA[name="comment"]').empty().append(item.comment);
						$('INPUT[name="rating"]').each(function () {
							console.log(rate);
							console.log($(this).val() == rate);
							if ($(this).val() == rate) {
								console.log('rate' + rate);
								$(this).attr('checked', true);
							}
						});
					}
				});
			}
			if (data.lastPage) {
				$(btn).data('page', -1);
			} else {
				$(btn).data('page', parseInt(p) + 1);
			}
		},
			'json'
			);
		return false;
	});
});