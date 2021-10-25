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
        $('FORM').submit(function (event) {
                event.preventDefault();
                if (!$('INPUT#cellularPhoneNumber').val().match(/^09[0-9]{8}$/)
                        && !$('INPUT#cellularPhoneNumber').val().match(/^9[0-9]{8}$/)) {
                        $('.toast-body').html('手機格式不符;例: 0912345678');
                        $('.toast').toast('show');
                        return;
                }
                let form = this;
                $.post(
                        $(form).attr('action'),
                        $(form).serialize(),
                        function (data) {
                                if (data.response) {
                                        $('.toast-body').html(data.reason);
                                        $('.toast').toast('show');
                                        if (data.redirect) {
                                                $('.toast').on('hidden.bs.toast', function () {
                                                        location.href = data.redirect;
                                                });
                                        }
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