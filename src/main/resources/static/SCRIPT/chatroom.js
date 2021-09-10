$(document).ready(function () {
	$('BUTTON.locationBack').click(function () {
		if (!document.referrer || document.referrer === location.href || document.referrer.indexOf(location.origin) < 0) {
			location.href = '/';
		} else {
			window.history.back();
		}
	});

	let vh = window.innerHeight * 0.01;
	document.querySelector('.chatContainer').style.setProperty('--vh', `${vh}px`);
	window.addEventListener('resize', () => {
		let vh = window.innerHeight * 0.01;
		document.querySelector('.chatContainer').style.setProperty('--vh', `${vh}px`);
	});
});