$(document).ready(function () {
	ECPay.initialize(
		ECPay.ServerType.Stage,
		1,
		function (errMsg) {
			if (errMsg !== null) {
				alert(errMsg);
			}
			$.post(
				'/inpay2/getTokenByTrade.json',
				{
					me: $('#me').val(),
					plan: $('#plan').val()
				},
				function (data) {
					if (data.RtnCode === 1) {
						try {
							ECPay.createPayment(
								data.Token,
								null === navigator.language.match(/^zh/gi) ? ECPay.Language.enUS : ECPay.Language.zhTW,
								function (errMsg) {
									if (errMsg !== null) {
										alert(errMsg);
									}
								}
							);
						} catch (err) {
							alert(err);
						}
					} else {
						alert(data.RtnMsg);
					}
				},
				'json'
				);
		}
	);

	$('FORM[name="payment"]').submit(function (event) {
		event.preventDefault();
		$('DIV.loadingWrap').css('display', 'block');
		let form = this;
		try {
			ECPay.getPayToken(function (paymentInfo, errMsg) {
				if (errMsg !== null) {
					alert(errMsg);
					return false;
				}
				let payToken = paymentInfo.PayToken;
				$(form.payToken).val(
					payToken
					);
				$(form.paymentType).val(
					paymentInfo.PaymentType
					);
				$.post(
					`/inpay2/createPayment/${payToken}.json`,
					function (data) {
						console.log(data);
						if (data.ThreeDInfo.ThreeDURL) {
							location.replace(data.ThreeDInfo.ThreeDURL);
						} else {
							alert('呃!?');
						}
					},
					'json'
					);
			});
		} catch (error) {
			alert(error);
		}
		return false;
	});
});