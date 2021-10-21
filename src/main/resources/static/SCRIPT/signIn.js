$(document).ready(function () {
        $('#cellularPhoneNumber').keyup(function () {
                let that = this;
                $(this).val(
                        $(this).val().replace(/\D/gi, '')
                        );
                $('INPUT[name="username"]').val(
                        $('#country').val().replace(/\D/g, '') +
                        $(this).val().replace(/^0/g, '')
                        );
        });

        var isAndroid = /android/i.test(navigator.userAgent);
        var isSafari = !!navigator.userAgent.match(/Version\/[\d\.]+.*Safari/);
        var isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
        var isNotStandalone = !window.matchMedia('(display-mode: standalone)').matches;

        console.log(isNotStandalone)

        if (isIOS && isNotStandalone && isSafari) {
                $('DIV.iosAddToDesktop').css('display', 'block');
        }
        if (isAndroid && isNotStandalone) {
                $('DIV.androidAddToDesktop').css('display', 'block');
        }

        $('BUTTON.addDeskColse').click(function () {
                $('DIV.addToDeskTop').each(function () {
                        $(this).css('display', 'none')
                });
        });
});