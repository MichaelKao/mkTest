$(document).ready(function () {

        $('#registeredDate').datepicker({
                format: 'yyyy/mm/dd',
                endDate: new Date(),
                todayHighlight: true
        });

        var hour = '08';
        var minute = '30';
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
                                $input.val($('#hour').val() + ':' + $('#minute').val());
                                $input.removeAttr('disabled');
                                $(timePicker).remove();
                        });
                });
        }

        timePicker('timePicker');
});