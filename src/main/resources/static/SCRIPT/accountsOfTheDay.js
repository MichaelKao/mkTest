$(document).ready(function () {
        var defaultDate = $('.registered').html();

        $('#registeredDate').datepicker({
                format: 'yyyy/mm/dd',
                endDate: new Date(),
                todayHighlight: true
        });

        $('#registeredDate').datepicker('setDate', defaultDate);

        $('#registeredDate').datepicker().on('change', function () {
                var date = $('INPUT[name="registeredDate"]').val();
                location.href = '/dashboard/' + date + '/newAccounts.asp';
        });

        $('INPUT[name="genuine"]').on('change', function () {
                var input = this;
                var id = $(input).siblings('INPUT[name="id"]').val();

                $.post(
                        '/dashboard/updateGenuineMemebr.json',
                        {
                                lover: id
                        },
                        function (data) {
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