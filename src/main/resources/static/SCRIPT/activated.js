$(document).ready(function () {
        $('FORM').submit(function (event) {
                event.preventDefault();
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
                                                        fbq('track', 'CompleteRegistration');
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

        $("SPAN.togglePwd").click(function (e) {
                e.preventDefault();
                var span = this;
                var $input = $(span).siblings('INPUT[name="shadow"]');
                var $icon = $(span).find('I');
                if ($input.attr('type') == 'password') {
                        $input.attr("type", 'text');
                        $icon.attr('class', 'fad fa-eye-slash');
                } else {
                        $input.attr('type', 'password');
                        $icon.attr('class', 'fad fa-eye');
                }
        });
});