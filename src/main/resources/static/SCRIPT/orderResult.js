$(document).ready(function () {
	var totalAmount = $(".totalAmount").html();
	
	fbq('track', 'Purchase', {value: totalAmount, currency: 'TWD'});
});