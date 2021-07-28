$(document).ready(function () {

	$('.imgContainer').css({'max-height': $(window).height() - 250 + "px"});

	var image = document.getElementById('image');
	var input;
	var $cropModal = $('#cropModal');
	var cropper;
	var photoType;

	$('INPUT[name="image"]').on("change", function (e) {
		var files = e.target.files;
		input = this;
		photoType = $(input).data('type');

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
			aspectRatio: 1 / 1,
			autoCropArea: 1,
			restore: false,
			guides: false,
			center: false,
			highlight: false,
			cropBoxMovable: false,
			cropBoxResizable: false,
			toggleDragModeOnDblclick: false
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

			if (photoType === 'profileImage') {
				$('#profileImageImg').attr('src', canvas.toDataURL());
			}

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
		if (photoType === 'picture') {
			postUrl = '/uploadPicture';
		}
		if (photoType === 'profileImage') {
			postUrl = '/uploadProfileImage';
		}

		$.ajax({
			url: postUrl,
			cache: false,
			contentType: false,
			processData: false,
			data: file,
			type: 'post',
			success: function (data) {
				if (photoType === 'picture') {
					window.location.reload();
				}
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

	var identifier;

	$('.btnDel').click(function () {
		identifier = $(this).data('id');
		$('#confirmModal').modal('show');
	});

	$('.btnDelConfirm').click(function () {
		deleteFile(identifier);
	});

	function deleteFile(identifier) {
		$.post(
			'/deletePicture',
			{
				identifier: identifier
			},
			(data) => {
			try {
				if (data.response) {
					window.location.reload();
				} else {
					alert("刪除失敗");
				}
			} catch (error) {
				alert('刪除失敗');
			}
		},
			'json'
			);
	}
});