$(document).ready(function () {

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
			(data) => {
			if (data.response) {
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
});