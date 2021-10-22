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
							alert(`繳費銀行代碼：${data.ATMInfo.BankCode}\n繳費虛擬帳號：${data.ATMInfo.vAccount}\n繳費期限：${data.ATMInfo.ExpireDate}`);
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