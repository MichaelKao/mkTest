$(document).ready(function () {
        $('BUTTON.toggleEdit').click(function () {
                var toggle = this;
                $(toggle).addClass('d-none');
                $(toggle).siblings('BUTTON.editDone').removeClass('d-none');
                $(toggle).siblings('INPUT[type="text"]').attr('disabled', false);
        });

        $('BUTTON.editDone').click(function () {
                var btn = this;
                $.post(
                        '/dashboard/updateTrialCode.json',
                        {
                                editedCode: $(btn).siblings('INPUT[name="editedCode"]').val(),
                                trialCode: $(btn).siblings('INPUT[name="trialCodeID"]').val()
                        },
                        function (data) {
                                if (data.response) {
                                        $(btn).addClass('d-none');
                                        $(btn).siblings('BUTTON.toggleEdit').removeClass('d-none');
                                        $(btn).siblings('INPUT[type="text"]').attr('disabled', true);
                                } else {
                                        $('.toast-body').html(data.reason);
                                        $('.toast').toast('show');
                                }
                        },
                        'json'
                        );
                return false;
        });

        $('BUTTON.addDone').click(function () {
                event.preventDefault();
                var btn = this;
                $.post(
                        '/dashboard/addTrialCode.json',
                        {
                                code: $('INPUT[name="code"]').val(),
                                keyOpinionLeader: $('INPUT[name="keyOpinionLeader"]').val()
                        },
                        function (data) {
                                if (data.response) {
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

        $('BUTTON.trialCodeList').click(function () {
                event.preventDefault();
                var btn = this;
                var $users = $('DIV.users');
                $users.empty();
                $.post(
                        '/dashboard/trialCodeList.json',
                        {
                                trialCode: $(btn).closest('DIV.trial').find('INPUT[name="trialCodeID"]').val()
                        },
                        function (data) {
                                console.log(data);
                                data.list.forEach(function (item, index) {
                                        var div = document.createElement('DIV');
                                        $(div).attr('class', 'd-flex align-items-center w-90 mx-auto p-1');
                                        if (index % 2 == 0) {
                                                $(div).addClass('oddList');
                                        }
                                        $users.append(div);
                                        var a = document.createElement('A');
                                        $(a).attr({
                                                'class': 'text-sm',
                                                'href': '/profile/' + item.identifier + '/'
                                        });
                                        $(div).append(a);
                                        var i = document.createElement('I');
                                        $(i).attr('class', 'far fa-user me-1');
                                        $(a).append(i);
                                        var span = document.createElement('SPAN');
                                        $(span).append(item.nickname);
                                        $(a).append(span);
                                        var dateSpan = document.createElement('SPAN');
                                        $(dateSpan).attr('class', 'ms-auto text-xs text-dark');
                                        $(dateSpan).append(item.date);
                                        $(div).append(dateSpan);
                                });
                        },
                        'json'
                        );
                return false;
        });
});