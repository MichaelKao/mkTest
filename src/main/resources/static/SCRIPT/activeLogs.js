$(document).ready(function () {
	$('BUTTON.accept').dblclick(function (e) {
		e.preventDefault();
	});
	$('BUTTON.accept').click(function (event) {
		event.preventDefault();
		$(this).attr('disabled', true);
		$(this).siblings('BUTTON.refuse').attr('disabled', true);
		var whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();

		$.post(
			"/stalked.json",
			{
				whom: whom
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$('.toast').on('hidden.bs.toast', function () {
						window.location.reload();
					});
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
		var whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();

		$.post(
			"/notStalked.json",
			{
				whom: whom
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$('.toast').on('hidden.bs.toast', function () {
						window.location.reload();
					});
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	var $modal = $('#modal');
	var $rateModal = $('#rateModal');
	var whom;
	var commentBtn;

	$('.requestLine').click(function () {
		$modal.modal('show');
		whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();
	});
	$('.rate').click(function () {
		$rateModal.modal('show');
		whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();
		commentBtn = $(this);
	});

	$('BUTTON.requestLineBtn').click(function (event) {
		event.preventDefault();
		$.post(
			'/stalking.json',
			{
				whom: whom,
				what: $('TEXTAREA[name="what"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
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
				whom: whom,
				rate: rate,
				comment: $('TEXTAREA[name="comment"]').val()
			},
			function (data) {
				if (data.response) {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
					$rateModal.modal('hide');
					commentBtn.css('display', 'none');
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('BUTTON.openLine').dblclick(function (e) {
		e.preventDefault();
	});
	$('BUTTON.openLine').click(function () {
		$(this).attr('disabled', true);
		whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();
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
	$('BUTTON.acceptPixAuth').dblclick(function (e) {
		e.preventDefault();
	});
	$('BUTTON.acceptPixAuth').click(function (event) {
		event.preventDefault();
		let btn = this;
		$(btn).attr('disabled', true);
		var whom = $(this).closest('DIV.card-body').find('INPUT[name="whom"]').val();

		$.post(
			"/acceptPixAuth.json",
			{
				whom: whom
			},
			function (data) {
				if (data.response) {
					$(btn).remove();
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
});