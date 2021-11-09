$(document).ready(function () {
	$('BUTTON.toggleEdit').click(function () {
		var toggle = this;
		$(toggle).addClass('d-none');
		$(toggle).siblings('BUTTON.editDone').removeClass('d-none');
		$(toggle).siblings('INPUT[type="text"]').attr('disabled', false);
	});

	$('BUTTON.editDone').click(function () {
		var btn = this;
		$.post(
			'/dashboard/updateHashtag.json',
			{
				phrase: $(btn).siblings('INPUT[name="editedPhrase"]').val(),
				forumThreadTag: $(btn).siblings('INPUT[name="hashtagID"]').val()
			},
			function (data) {
				if (data.response) {
					$(btn).addClass('d-none');
					$(btn).siblings('BUTTON.toggleEdit').removeClass('d-none');
					$(btn).siblings('INPUT[type="text"]').attr('disabled', true);
				} else {
					$('.toast-body').html(data.reason);
					$('.toast').toast('show');
				}
			},
			'json'
			);
		return false;
	});

	$('BUTTON.addDone').click(function () {
		event.preventDefault();
		var btn = this;
		$.post(
			'/dashboard/addHashtag.json',
			{
				phrase: $('INPUT[name="phrase"]').val()
			},
			function (data) {
				if (data.response) {
					window.location.reload();
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