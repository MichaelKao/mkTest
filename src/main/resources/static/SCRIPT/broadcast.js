$(document).ready(function () {

        $('#registeredDate').datepicker({
                format: 'yyyy-mm-dd',
                endDate: new Date(),
                todayHighlight: true
        });

        var hour = '08';
        var minute = '30';
        timePicker('timePicker');

        var $date = $('INPUT[name="date"]');
        var registeredDate;
        var timePickerVal = $('INPUT#timePicker').val();

        $('INPUT#specificDate').click(function () {
                $('INPUT[name="registeredDate"]').prop('disabled', !$(this).prop('checked'));
                $('INPUT#timePicker').prop('disabled', !$(this).prop('checked'));
                if ($(this).prop('checked')) {
                        if (typeof (registeredDate) == 'undefined') {
                                var d = new Date();
                                var yy = d.getFullYear();
                                var mm = (d.getMonth() + 1 < 10 ? '0' + (d.getMonth() + 1) : d.getMonth() + 1);
                                var dd = (d.getDate() < 10 ? '0' : 0) + d.getDate();
                                registeredDate = yy + '-' + mm + '-' + dd;
                        }
                        $date.val(registeredDate + ' ' + timePickerVal);
                } else {
                        $date.val('');
                }
                $('DIV.open').remove();
        });

        $('#registeredDate').datepicker().on('change', function () {
                registeredDate = $('INPUT[name="registeredDate"]').val();
                $date.val(registeredDate + ' ' + timePickerVal);
        });

        function timePicker(id) {
                var $input = $('#' + id);
                var $formGroup = $input.closest('DIV.form-group');
                $input.val(hour + ':' + minute);

                $input.click(function () {
                        var that = this;
                        var timePicker = document.createElement('div');
                        $(timePicker).toggleClass('open');
                        $(that).attr('disabled', true);
                        $(timePicker).html(
                                `<div class="d-flex justify-content-center mb-2">
                                        <div class="label">
                                                <a id="plusH" >+</a>
                                                <input class="set form-control" type="text" id="hour" value="` + hour + `">
                                                <a id="minusH">-</a>
                                        </div>
                                        <div class="label">
                                                <a id="plusM">+</a>
                                                <input class="set form-control" type="text" id="minute" value="` + minute + `">
                                                <a id="minusM">-</a>
                                        </div>
                                </div>
                                <div id="submitTime">確定</div>`
                                );
                        $formGroup.after(timePicker);

                        var plusH = $('#plusH');
                        var minusH = $('#minusH');
                        var plusM = $('#plusM');
                        var minusM = $('#minusM');
                        var h = parseInt($('#hour').val());
                        var m = parseInt($('#minute').val());
                        //increment hour
                        plusH.click(function () {
                                h = isNaN(h) ? 0 : h;
                                if (h === 23) {
                                        h = -1;
                                }
                                h++;
                                hour = (h < 10 ? '0' : 0) + h;
                                $('#hour').val(hour);
                        });
                        //decrement hour
                        minusH.click(function () {
                                h = isNaN(h) ? 0 : h;
                                if (h === 0) {
                                        h = 24;
                                }
                                h--;
                                hour = (h < 10 ? '0' : 0) + h;
                                $('#hour').val(hour);
                        });
                        //increment minute
                        plusM.click(function () {
                                m = isNaN(m) ? 0 : m;
                                if (m === 45) {
                                        m = -15;
                                }
                                m = m + 15;
                                minute = (m < 10 ? '0' : 0) + m;
                                $('#minute').val(minute);
                        });
                        //decrement minute
                        minusM.click(function () {
                                m = isNaN(m) ? 0 : m;
                                if (m === 0) {
                                        m = 60;
                                }
                                m = m - 15;
                                minute = (m < 10 ? '0' : 0) + m;
                                $('#minute').val(minute);
                        });
                        //submit timepicker
                        var submit = $('#submitTime');
                        submit.click(function () {
                                timePickerVal = $('#hour').val() + ':' + $('#minute').val();
                                $input.val(timePickerVal);
                                $input.removeAttr('disabled');
                                $(timePicker).remove();
                                $('INPUT[name="date"]').val(registeredDate + ' ' + timePickerVal);
                        });
                });
        }

        $('FORM').submit(function (e) {
                e.preventDefault();
                var gender = $('INPUT[name="gender"]:checked').val();
                if (typeof (gender) == 'undefined') {
                        $('.toast-body').html('請選擇性別~');
                        $('.toast').toast('show');
                        return;
                }
                $.post(
                        '/dashboard/broadcast.asp',
                        {
                                'gender': gender,
                                'date': $('INPUT[name="date"]').val(),
                                'content': $('TEXTAREA[name="content"]').val()
                        },
                        function (data) {
                                console.log(data);
                                if (data.reason) {
                                        $('.toast-body').html(data.reason);
                                        $('.toast').toast('show');
                                } else {
                                        window.location.reload();
                                }
                        },
                        'json'
                        )
        });
});