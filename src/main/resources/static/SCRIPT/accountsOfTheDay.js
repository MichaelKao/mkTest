$(document).ready(function () {
        var defaultDate = $('.registered').html();
        console.log(defaultDate);

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
});