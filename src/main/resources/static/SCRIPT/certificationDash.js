$(document).ready(function () {

        var id;

        $('BUTTON.seePic').click(function () {
                let button = this;
                id = $(button).closest('DIV.reliefWrap').find('INPUT[name="id"]').val();
                console.log('seePic', id)
                $.post(
                        '/dashboard/seeCetificationPic.json',
                        {
                                id: id
                        },
                        function (data) {
                                if (data.response) {
                                        $('DIV.modal-body>IMG').attr('src', data.result);
                                } else {
                                        $('.toast-body').html(data.reason);
                                        $('.toast').toast('show');
                                }
                        },
                        'json'
                        );
                return false;
        });

        $('BUTTON.fail').click(function () {
                let btn = this;
                id = $(btn).closest('DIV.reliefWrap').find('INPUT[name="id"]').val();
                $.post(
                        '/dashboard/identityFailed.json',
                        {
                                id: id,
                                reason: $(btn).data('reason')
                        },
                        function (data) {
                                if (data.response) {
                                        $('.toast-body').html(data.reason);
                                        $('.toast').toast('show');
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

        $('BUTTON.success').click(function () {
                let button = this;
                id = $(button).closest('DIV.reliefWrap').find('INPUT[name="id"]').val();
                console.log('id', id);
                $.post(
                        '/dashboard/identityPassed.json',
                        {
                                id: id
                        },
                        function (data) {
                                if (data.response) {
                                        $('.toast-body').html(data.reason);
                                        $('.toast').toast('show');
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
