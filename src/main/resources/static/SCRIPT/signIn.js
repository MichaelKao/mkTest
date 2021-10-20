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

        const usernameInput = document.getElementById("username");
        function lsRememberMe() {
                localStorage.username = usernameInput.value;
        }
});