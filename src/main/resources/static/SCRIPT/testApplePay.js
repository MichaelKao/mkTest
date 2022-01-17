$(document).ready(function () {
	ECPay.initialize(
		ECPay.ServerType.Prod,
		1,
		function (errMsg) {
			if (errMsg !== null) {
				alert(errMsg);
			}
			$('DIV.loadingWrap').css('display', 'none');
			$.post(
				'/inpay2/getTokenByTradeApplePay.json',
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
				$('DIV.loadingWrap').css('display', 'block');
				$.post(
					`/inpay2/createPayment/${payToken}.json`,
					function (data) {
						console.log(data);
						if (data.ThreeDInfo.ThreeDURL) {
							location.replace(data.ThreeDInfo.ThreeDURL);
						} else if (data.CVSInfo.PaymentURL) {
							location.replace(data.CVSInfo.PaymentURL);
						} else if (data.ATMInfo.BankCode && data.ATMInfo.vAccount && data.ATMInfo.ExpireDate) {
							let bank = $('SELECT[name="BankCode"] option:selected').html();
							let points = $('INPUT[name="points"]').val();
							$('.card-body').empty();
							$('.card-body').append(`
										<span class="text-primary text-sm font-weight-bold my-2">養蜜 Young me</span>
										<div class="my-3 col-12 col-sm-8 col-lg-7 col-xl-6 mx-auto text-center">
											<h4>ATM 付款資訊</h4>
											<hr class="my-2">
											<div class="d-flex flex-column align-items-center justify-content-center">
												<span class="text-sm text-dark">繳費銀行：</span>
												<span class="text-primary text-bolder text-lg">${bank}</span>
											</div>
											<hr class="my-2">
											<div class="d-flex flex-column align-items-center justify-content-center">
												<span class="text-sm text-dark">繳費銀行代碼：</span>
												<span class="text-primary text-bolder text-lg">${data.ATMInfo.BankCode}</span>
											</div>
											<hr class="my-2">
											<div class="d-flex flex-column align-items-center justify-content-center">
												<span class="text-sm text-dark">繳費虛擬帳號：</span>
												<span class="text-primary text-bolder text-lg">${data.ATMInfo.vAccount}</span>
											</div>
											<hr class="my-2">
											<div class="d-flex flex-column align-items-center justify-content-center">
												<span class="text-sm text-dark">繳費期限：</span>
												<span class="text-primary text-bolder text-lg">${data.ATMInfo.ExpireDate}</span>
											</div>
											<hr class="my-2">
											<div class="d-flex flex-column align-items-center justify-content-center">
												<span class="text-sm text-dark">需支付金額：</span>
												<span class="text-primary text-bolder text-lg">
													<div class="">NT$ ${data.OrderInfo.TradeAmt}</div>
													<div class="text-xs text-dark">(${points} ME點)</div>
												</span>
											</div>
											<hr class="my-2">
										</div>
										<div class="text-center text-sm text-bold my-3">
											<div class="text-dark">請於繳費期限內付款。</div>
											<div class="text-danger">
												<i class="fas fa-exclamation-circle me-1"></i>提醒： 為避免操作重複造成系統錯誤，請螢幕截圖方便進行交易。
											</div>
										</div>`)
							let btnDiv = document.createElement('DIV');
							$(btnDiv).attr('class', 'text-center');
							$('.card-body').append(btnDiv);
							let btn = document.createElement('BUTTON');
							$(btn).attr('class', 'btn btn-dark btn-round px-3 py-2 m-0');
							$(btn).append('重選付費方式');
							$(btnDiv).append(btn);
							$(btn).click(function () {
								window.location.reload();
							});
						} else {
							alert(`{"RtnCode":${data.RtnCode},"RtnMsg":"${data.RtnMsg}"}`);
						}
						$('DIV.loadingWrap').css('display', 'none');
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