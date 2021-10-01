$(document).ready(function () {

        var $myReferralCode = $('DIV.myReferralCode');
        var $invitedCode = $('DIV.invitedCode');
        var $descendants = $('DIV.descendants');
        var $pagination = $('DIV.pagination');
        var lover;

        $('.referralCodeBtn').click(function () {
                var btn = this;
                lover = $(btn).data('id');
                $.post(
                        '/dashboard/referralCode.json',
                        {
                                p: 0,
                                s: 5,
                                lover: lover
                        },
                        function (data) {
                                if (data.response) {
                                        let myReferralCodeSpan = document.createElement('SPAN');
                                        $(myReferralCodeSpan).attr('class', 'code');
                                        $(myReferralCodeSpan).append(': ' + data.result.referralCode);
                                        $myReferralCode.append(myReferralCodeSpan);

                                        let invitedCodeSpan = document.createElement('SPAN');
                                        $(invitedCodeSpan).attr('class', 'code');
                                        $(invitedCodeSpan).append(': ' + data.result.invitedCode);
                                        if (data.result.invitedCode !== '無') {
                                                $(invitedCodeSpan).append('(' + data.result.invitedUserName + ')');
                                        }
                                        $invitedCode.append(invitedCodeSpan);

                                        descendants(data);
                                } else {
                                        $('.toast-body').html(data.reason);
                                        $('.toast').toast('show');
                                }
                        },
                        'json'
                        );
                return false;
        });

        $('#referralCode').on('hidden.bs.modal', function () {
                $descendants.empty();
                $pagination.empty();
                $('.code').empty();
        });

        // 顯示下線的名單
        function descendants(data) {

                data.result.descendants.forEach(function (item) {
                        var d = new Date(item.timestamp.replace(" ", "T"));
                        var year = d.getFullYear();
                        var month = (d.getMonth() + 1) < 10 ? '0' + (d.getMonth() + 1) : d.getMonth() + 1;
                        var date = d.getDate() < 10 ? '0' + d.getDate() : d.getDate();
                        var hour = d.getHours() < 10 ? '0' + d.getHours() : d.getHours();
                        var minute = d.getMinutes() < 10 ? '0' + d.getMinutes() : d.getMinutes();

                        let div = document.createElement('DIV');
                        $(div).attr('class', 'd-flex align-items-center w-80 mx-auto');
                        $descendants.append(div);
                        let i = document.createElement('I');
                        if (item.vip == true) {
                                $(i).attr('class', 'fas fa-check-circle me-1 text-primary');
                        } else {
                                $(i).attr('class', 'fas fa-check-circle me-1');
                        }
                        $(div).append(i);
                        let name = document.createElement('SPAN');
                        $(name).attr('class', 'text-sm');
                        $(name).html(item.nickname);
                        $(div).append(name);
                        let timestamp = document.createElement('SPAN');
                        $(timestamp).attr('class', 'ms-auto text-xs');
                        $(timestamp).html(year + '-' + month + '-' + date + ' ' + hour + ':' + minute);
                        $(div).append(timestamp);
                });

                // 有上一頁
                if (typeof data.result.pagination.previous !== 'undefined') {
                        let span = document.createElement('SPAN');
                        $(span).attr({
                                'class': 'mx-2 position-absolute top-0 bottom-0 d-flex align-items-center pageBtn',
                                'data-page': data.result.pagination.previous
                        });
                        $pagination.append(span);
                        let i = document.createElement('I');
                        $(i).attr('class', 'fal fa-chevron-left fontSize25');
                        $(span).append(i);
                }

                // 有下一頁
                if (typeof data.result.pagination.next !== 'undefined') {
                        let span = document.createElement('SPAN');
                        $(span).attr({
                                'class': 'mx-2 position-absolute top-0 bottom-0 right-0 d-flex align-items-center pageBtn',
                                'data-page': data.result.pagination.next
                        });
                        $pagination.append(span);
                        let i = document.createElement('I');
                        $(i).attr('class', 'fal fa-chevron-right fontSize25');
                        $(span).append(i);
                }

                $('SPAN.pageBtn').click(function (event) {
                        event.preventDefault();
                        var div = this;
                        $.post(
                                '/dashboard/referralCode.json',
                                {
                                        p: $(div).data('page'),
                                        s: 5,
                                        lover: lover
                                },
                                function (data) {
                                        if (data.response) {
                                                $descendants.empty();
                                                $pagination.empty();
                                                descendants(data);
                                        } else {
                                                $('.toast-body').html(data.reason);
                                                $('.toast').toast('show');
                                        }
                                },
                                'json'
                                );
                        return false;
                });
        }

        let searchValue;
        let searchGender;

        $('.searchBtn').click(function () {
                var btn = this;
                searchValue = $(btn).siblings('INPUT[name="searchValue"]').val().replace(/^0/g, '');
                searchGender = $(btn).siblings('INPUT[name="searchGender"]').val();
                $.post(
                        '/dashboard/searchMember.json',
                        {
                                searchValue: searchValue,
                                searchGender: searchGender
                        },
                        function (data) {
                                search(data);
                        },
                        'json'
                        );
                return false;
        });

        var delayTimer;
        $('INPUT[name="searchValue"]').keyup(function () {
                let input = this;
                searchValue = $(input).val().replace(/^0/g, '');
                searchGender = $(input).siblings('INPUT[name="searchGender"]').val();
                clearTimeout(delayTimer);
                delayTimer = setTimeout(function () {
                        $.post(
                                '/dashboard/searchMember.json',
                                {
                                        searchValue: searchValue,
                                        searchGender: searchGender
                                },
                                function (data) {
                                        search(data);
                                },
                                'json'
                                );
                        return false;
                }, 800);
        });

        function search(data) {
                if (searchGender === 'true') {
                        var $maleMembers = $('.maleMembers')
                        $maleMembers.empty();
                        data.forEach(function (member, index) {
                                let eachMainDiv = document.createElement('DIV');
                                $(eachMainDiv).attr('class', 'row text-center align-items-center border-radius-xl py-2');
                                if (index % 2 == 0) {
                                        $(eachMainDiv).attr('class', 'row text-center align-items-center bg-light border-radius-xl py-2');
                                }
                                $maleMembers.append(eachMainDiv);

                                let nicknameDiv = document.createElement('DIV');
                                $(nicknameDiv).attr('class', 'col-3 d-flex justify-content-start');
                                $(eachMainDiv).append(nicknameDiv);

                                let nicknameA = document.createElement('A');
                                $(nicknameA).attr({
                                        'class': 'd-flex flex-column align-items-start',
                                        'href': '/profile/' + member.identifier + '/'
                                });
                                $(nicknameDiv).append(nicknameA);

                                let nicknameSPAN = document.createElement('SPAN');
                                $(nicknameSPAN).attr('class', 'text-primary');
                                $(nicknameSPAN).append(member.nickname);
                                $(nicknameA).append(nicknameSPAN);

                                let loginDiv = document.createElement('DIV');
                                $(loginDiv).attr('class', 'text-secondary');
                                $(loginDiv).append(member.login);
                                $(nicknameA).append(loginDiv);

                                let registeredDiv = document.createElement('DIV');
                                $(registeredDiv).attr('class', 'col-3');
                                $(eachMainDiv).append(registeredDiv);

                                let registeredSPAN = document.createElement('SPAN');
                                $(registeredSPAN).append(member.registered);
                                $(registeredDiv).append(registeredSPAN);

                                let vipDiv = document.createElement('DIV');
                                $(vipDiv).attr('class', 'col-3');
                                $(eachMainDiv).append(vipDiv);

                                var vipWrapSpan = document.createElement('SPAN');
                                var vipI = document.createElement('I');
                                $(vipI).attr('class', 'fad fa-crown me-1');
                                $(vipWrapSpan).append(vipI);
                                var vipSpan = document.createElement('SPAN');
                                $(vipWrapSpan).append(vipSpan);
                                var vipDateDiv = document.createElement('DIV');

                                if (member.isVIP === true) {
                                        $(vipDiv).append(vipWrapSpan);
                                        $(vipDiv).append(vipDateDiv);
                                        $(vipSpan).append('1688');
                                        $(vipDateDiv).append(member.vipExpiration);
                                }

                                if (member.isVVIP === true) {
                                        $(vipDiv).append(vipWrapSpan);
                                        $(vipDiv).append(vipDateDiv);
                                        $(vipSpan).append('1288');
                                        $(vipDateDiv).append(member.vipExpiration);
                                }

                                if (member.isTrial === true) {
                                        $(vipDiv).append(vipWrapSpan);
                                        $(vipDiv).append(vipDateDiv);
                                        $(vipSpan).append('單日');
                                        $(vipDateDiv).append(member.vipExpiration);
                                }

                                let referralCodeDiv = document.createElement('DIV');
                                $(referralCodeDiv).attr('class', 'col-3');
                                $(eachMainDiv).append(referralCodeDiv);

                                let referralCodeBtn = document.createElement('BUTTON');
                                $(referralCodeBtn).attr({
                                        'class': 'btn btn-link m-0 px-2 py-1 referralCodeBtn',
                                        'data-bs-target': '#referralCode',
                                        'data-bs-toggle': 'modal',
                                        'data-id': member.id,
                                        'type': 'button'
                                });
                                $(referralCodeDiv).append(referralCodeBtn);

                                $(referralCodeBtn).click(function () {
                                        var btn = this;
                                        lover = $(btn).data('id');
                                        $.post(
                                                '/dashboard/referralCode.json',
                                                {
                                                        p: 0,
                                                        s: 5,
                                                        lover: lover
                                                },
                                                function (data) {
                                                        if (data.response) {
                                                                let myReferralCodeSpan = document.createElement('SPAN');
                                                                $(myReferralCodeSpan).attr('class', 'code');
                                                                $(myReferralCodeSpan).append(': ' + data.result.referralCode);
                                                                $myReferralCode.append(myReferralCodeSpan);

                                                                let invitedCodeSpan = document.createElement('SPAN');
                                                                $(invitedCodeSpan).attr('class', 'code');
                                                                $(invitedCodeSpan).append(': ' + data.result.invitedCode);
                                                                if (data.result.invitedCode !== '無') {
                                                                        $(invitedCodeSpan).append('(' + data.result.invitedUserName + ')');
                                                                }
                                                                $invitedCode.append(invitedCodeSpan);

                                                                descendants(data);
                                                        } else {
                                                                $('.toast-body').html(data.reason);
                                                                $('.toast').toast('show');
                                                        }
                                                },
                                                'json'
                                                );
                                        return false;
                                });

                                let referralCodeI = document.createElement('I');
                                $(referralCodeI).attr('class', 'fal fa-users-crown mb-1 fontSize22');
                                $(referralCodeBtn).append(referralCodeI);
                        });
                } else {
                        var $femaleMembers = $('.femaleMembers')
                        $femaleMembers.empty();
                        data.forEach(function (member, index) {
                                let eachMainDiv = document.createElement('DIV');
                                $(eachMainDiv).attr('class', 'row text-center align-items-center border-radius-xl py-2');
                                if (index % 2 == 0) {
                                        $(eachMainDiv).attr('class', 'row text-center align-items-center bg-light border-radius-xl py-2');
                                }
                                $femaleMembers.append(eachMainDiv);

                                let nicknameDiv = document.createElement('DIV');
                                $(nicknameDiv).attr('class', 'col-4 d-flex justify-content-start');
                                $(eachMainDiv).append(nicknameDiv);

                                let nicknameA = document.createElement('A');
                                $(nicknameA).attr({
                                        'class': 'd-flex flex-column align-items-start',
                                        'href': '/profile/' + member.identifier + '/'
                                });
                                $(nicknameDiv).append(nicknameA);

                                let nicknameSPAN = document.createElement('SPAN');
                                $(nicknameSPAN).attr('class', 'text-primary');
                                $(nicknameSPAN).append(member.nickname);
                                $(nicknameA).append(nicknameSPAN);

                                let loginDiv = document.createElement('DIV');
                                $(loginDiv).attr('class', 'text-secondary');
                                $(loginDiv).append(member.login);
                                $(nicknameA).append(loginDiv);

                                let registeredDiv = document.createElement('DIV');
                                $(registeredDiv).attr('class', 'col-4');
                                $(eachMainDiv).append(registeredDiv);

                                let registeredSPAN = document.createElement('SPAN');
                                $(registeredSPAN).append(member.registered);
                                $(registeredDiv).append(registeredSPAN);

                                let referralCodeDiv = document.createElement('DIV');
                                $(referralCodeDiv).attr('class', 'col-4');
                                $(eachMainDiv).append(referralCodeDiv);

                                let referralCodeBtn = document.createElement('BUTTON');
                                $(referralCodeBtn).attr({
                                        'class': 'btn btn-link m-0 px-2 py-1 referralCodeBtn',
                                        'data-bs-target': '#referralCode',
                                        'data-bs-toggle': 'modal',
                                        'data-id': member.id,
                                        'type': 'button'
                                });
                                $(referralCodeDiv).append(referralCodeBtn);

                                $(referralCodeBtn).click(function () {
                                        var btn = this;
                                        lover = $(btn).data('id');
                                        $.post(
                                                '/dashboard/referralCode.json',
                                                {
                                                        p: 0,
                                                        s: 5,
                                                        lover: lover
                                                },
                                                function (data) {
                                                        if (data.response) {
                                                                let myReferralCodeSpan = document.createElement('SPAN');
                                                                $(myReferralCodeSpan).attr('class', 'code');
                                                                $(myReferralCodeSpan).append(': ' + data.result.referralCode);
                                                                $myReferralCode.append(myReferralCodeSpan);

                                                                let invitedCodeSpan = document.createElement('SPAN');
                                                                $(invitedCodeSpan).attr('class', 'code');
                                                                $(invitedCodeSpan).append(': ' + data.result.invitedCode);
                                                                if (data.result.invitedCode !== '無') {
                                                                        $(invitedCodeSpan).append('(' + data.result.invitedUserName + ')');
                                                                }
                                                                $invitedCode.append(invitedCodeSpan);

                                                                descendants(data);
                                                        } else {
                                                                $('.toast-body').html(data.reason);
                                                                $('.toast').toast('show');
                                                        }
                                                },
                                                'json'
                                                );
                                        return false;
                                });

                                let referralCodeI = document.createElement('I');
                                $(referralCodeI).attr('class', 'fal fa-users-crown mb-1 fontSize22');
                                $(referralCodeBtn).append(referralCodeI);
                        });
                }
        }

        $('.privilegeBtn').click(function () {
                var btn = this;
                lover = $(btn).data('id');
                $.post(
                        '/dashboard/privilege.json',
                        {
                                p: 0,
                                s: 5,
                                lover: lover
                        },
                        function (data) {
                                if (data.response) {
                                        data.result.forEach(function (item) {
                                                $('INPUT[name="role"]').each(function () {
                                                        var role = this;
                                                        if ($(role).val() == item) {
                                                                console.log(item)
                                                                $(role).prop('checked', true);
                                                        }
                                                });
                                        });
                                } else {
                                        $('.toast-body').html('error');
                                        $('.toast').toast('show');
                                }
                        },
                        'json'
                        );
                return false;
        });

        $('#privilege').on('hidden.bs.modal', function () {
                $('INPUT[name="role"]').each(function () {
                        var role = this;
                        if ($(role).prop('checked')) {
                                $(role).prop('checked', false);
                        }
                });
        });

        $('INPUT[name="role"]').change(function () {
                let input = this;
                $.post(
                        '/dashboard/updatePrivilege.json',
                        {
                                role: $(this).val(),
                                lover: lover
                        },
                        function (data) {
                                if (data.response) {
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