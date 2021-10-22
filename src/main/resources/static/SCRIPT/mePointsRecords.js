$(document).ready(function () {
        $('BUTTON.returnFareBtn').click(function () {
                var btn = this;
                var id = $(btn).data('id');
                console.log(id);

                $.post(
                        '/dashboard/returnFare.json',
                        {
                                history: id
                        },
                        function (data) {
                                console.log(data.reason);
                                if (data.response) {
                                        $('.toast-body').html(data.reason);
                                        $('.toast').toast('show');
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