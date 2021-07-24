$(document).ready(function () {

	$('DIV.star').each(function () {
		var div = this;
		var starCount = $(div).data('star');
		var starred = '&#9733;';
		var star = '&#9734;';

		$(div).html(starred.repeat(starCount) + star.repeat(5 - starCount));
	});

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
	var certification = document.querySelector('.certification');

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
			console.log(cropper.getCroppedCanvas())
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
				$(certification).attr('disabled', 'ture');
				$(certification).text('安心認證審核中');
				$cropModal.modal('hide');
			},
			error: function (error) {
				$('.toast-body').html(error);
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

	$('BUTTON.openLine').click(function () {
		$.post(
			'/maleOpenLine.json',
			{
				whom: $('INPUT[name="whom"]').val(),
			},
			function (data) {
				if (data.response) {
					location.href = data.redirect;
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});
});