$(document).ready(function () {

	$('.imgContainer').css({'max-height': $(window).height() - 250 + "px"});

	var image = document.getElementById('image');
	var input;
	var $cropModal = $('#cropModal');
	var cropper;
	var photoType;
	var modalRemarks = $('P.modalRemarks');

	$('INPUT[name="image"]').on("change", function (e) {
		var files = e.target.files;
		input = this;
		photoType = $(input).data('type');
		if (photoType === 'certification') {
			modalRemarks.text('註：須本人自拍並且手持證件');
		}
		if (photoType === 'qrcode') {
			modalRemarks.text('註：僅能上傳 LINE QR code');
		}

		var done = function (url) {
			this.value = '';
			image.src = url;
			$cropModal.modal('show');
		};
		var reader;
		var file;
		var url;
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

	$cropModal.on('shown.bs.modal', function () {
		cropper = new Cropper(image, {
			viewMode: 1,
			dragMode: 'move',
			autoCropArea: 1,
		});
	}).on('hidden.bs.modal', function () {
		cropper.destroy();
		cropper = null;
		input.value = '';
		$('.progress-bar').css('width', '0%').attr('aria-valuenow', '0%');
		$('.progress-percentage > SPAN').html('0%');
		$('#crop').html('完成');
		$('#crop').removeAttr('disabled')
	});

	$('#crop').click(function () {

		var canvas;
		let CutWidth;
		let CutHeight;
		$('#crop').html('請稍後...');
		$('#crop').attr('disabled', 'true');

		if (image.width < image.height && image.width < 900) {
			CutWidth = image.width;
			CutHeight = image.width;
		} else if (image.height < image.width && image.height < 900) {
			CutWidth = image.height;
			CutHeight = image.height;
		} else if (image.width < 100 && image.height < 100) {
			alert('解析度太低');
			return;
		} else {
			CutWidth = 900;
			CutHeight = 900;
		}
		if (cropper) {
			canvas = cropper.getCroppedCanvas({
				width: CutWidth,
				height: CutHeight,
			});

			canvas.toBlob(function (blob) {
				var OK = uploadpic(blob);
				if (!OK && typeof (OK) != "undefined") {
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

		var postUrl;
		if (photoType === 'certification') {
			postUrl = '/uploadIdentity';
		}
		if (photoType === 'qrcode') {
			postUrl = '/isLine.json';
		}

		$.ajax({
			url: postUrl,
			cache: false,
			contentType: false,
			processData: false,
			data: file,
			type: 'post',
			success: function (data) {
				$cropModal.modal('hide');
				if (photoType === 'certification') {
					$('A.certification').css('display', 'none');
					$('BUTTON.certification').attr('style', 'inline');
				}
				if (photoType === 'qrcode') {
					$('INPUT[name="lineLink"]').val(data.result);
					$('INPUT[name="lineLink"]').css('display', 'inline');
					$('DIV.uploadQrcode').css('display', 'none');
				}
			},
			error: function (data) {
				$('.toast-body').html(data.reason);
				$('.toast').toast('show');
				alert('上傳失敗');
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

	$('BUTTON.confirmBtn').click(function () {
		$.post(
			'/deleteAccount',
			(data) => {
			if (data.response) {
				location.href = data.redirect;
			}
		},
			'json'
			);
	});

	$('FORM').submit(function (event) {
		event.preventDefault();
		let form = this;

		$.post(
			$(form).attr('action'),
			$(form).serialize(),
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

	$('INPUT.service').change(function () {
		let input = this;
		$.post(
			'service.json',
			{
				service: $(this).val()
			},
			function (data) {
				if (data.response) {
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('INPUT.location').change(function () {
		let input = this;
		$.post(
			'location.json',
			{
				location: $(this).val()
			},
			function (data) {
				if (data.response) {
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