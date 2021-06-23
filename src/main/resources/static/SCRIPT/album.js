$(document).ready(function () {

	$('.imgContainer').css({'max-height': $(window).height() - 250 + "px"});
	var avatar = document.getElementById('avatar1');
	var image = document.getElementById('image');
	var input;
	var $modal = $('#modal');
	var cropper;
	$('INPUT[name="image"]').on("change", function (e) {
		var files = e.target.files;
		input = this;

		var done = function (url) {
			this.value = '';
			image.src = url;
			$modal.modal('show');
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
		avatar = document.getElementById('avatar' + this.id.replace("input", ""));
	});

	$modal.on('shown.bs.modal', function () {
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
			toggleDragModeOnDblclick: false,
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
		var initialAvatarURL;
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
			initialAvatarURL = avatar.src;
			avatar.src = canvas.toDataURL();
			canvas.toBlob(function (blob) {
				var OK = uploadpic(avatar.id.replace("avatar", ""), blob);
				if (!OK && typeof (OK) != "undefined") {
					alert('上傳失敗');
					$modal.modal('hide');
				}

				input.value = '';
			});
		}
	});

	function uploadpic(num, blob) {

		var file = new FormData();
		var filename = num + '.jpg';
		file.append('file', blob, filename);

		$.ajax({
			url: '/uploadfile',
			cache: false,
			contentType: false,
			processData: false,
			data: file,
			type: 'post',
			success: function (data) {
				if (data == 0) {
					alert('上傳失敗');
					window.location.reload();
				} else {
					$('#fileDiv' + num).removeClass();
					$('#fileDiv' + num + 'WithoutPic').addClass('d-none');
					console.log('#fileDiv' + num + '上傳成功');
				}
				$modal.modal('hide');
			},
			error: function () {
				alert('上傳失敗');
				$modal.modal('hide');
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
			$modal.modal('hide');
		});
	}

	$('.btnDel').click(function () {
		var index = $(this).attr('id').replace("delete", "");
		$('#confirmModal').modal('show');
		$('#confirmModal').find('INPUT[type="hidden"]').val(index);
	});

	$('.btnDelConfirm').click(function () {
		var index = $('INPUT[name="delete"]').val();
		deleteFile(index);
	});

	function deleteFile(index) {
		$.post(
			'/deletefile',
			{
				index: index
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