$(document).ready(function () {

        $('#expDate').datepicker({
                format: 'yyyy/mm/dd',
                startDate: new Date() + 1,
                todayHighlight: true
        });

        $('#expDate').datepicker().on('change', function () {
                $('BUTTON.upgradeVip').attr('disabled', false);
        });

        var $myReferralCode = $('DIV.myReferralCode');
        var $invitedCode = $('DIV.invitedCode');
        var $descendants = $('DIV.descendants');
        var $referralPage = $('DIV.referralPage');
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
                $referralPage.empty();
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
                        $referralPage.append(span);
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
                        $referralPage.append(span);
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
                                                $referralPage.empty();
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
        let pageSearch;

        $('.searchBtn').click(function () {
                var btn = this;
                searchValue = $(btn).siblings('INPUT[name="searchValue"]').val().replace(/^0/g, '');
                searchGender = $(btn).siblings('INPUT[name="searchGender"]').val();
                pageSearch = 1;
                $.post(
                        '/dashboard/searchMember.json',
                        {
                                searchValue: searchValue,
                                searchGender: searchGender,
                                pageSearch: pageSearch
                        },
                        function (data) {
                                search(data);
                                createPagination(
                                        $(btn).closest('DIV.tab-pane').find('DIV.pagination UL'),
                                        data.totalPages,
                                        pageSearch
                                        );
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
                pageSearch = 1;
                clearTimeout(delayTimer);
                delayTimer = setTimeout(function () {
                        $.post(
                                '/dashboard/searchMember.json',
                                {
                                        searchValue: searchValue,
                                        searchGender: searchGender,
                                        pageSearch: pageSearch
                                },
                                function (data) {
                                        search(data);
                                        createPagination(
                                                $(input).closest('DIV.tab-pane').find('DIV.pagination UL'),
                                                data.totalPages,
                                                pageSearch
                                                );
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
                        data.result.forEach(function (member, index) {
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
                                $(referralCodeDiv).attr('class', 'col-2 p-0');
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

                                let privilegeDiv = document.createElement('DIV');
                                $(privilegeDiv).attr('class', 'col-1 p-0');
                                $(eachMainDiv).append(privilegeDiv);

                                let privilegeBtn = document.createElement('BUTTON');
                                $(privilegeBtn).attr({
                                        'class': 'btn btn-link m-0 px-0 py-1 privilegeBtn',
                                        'data-bs-target': '#privilege',
                                        'data-bs-toggle': 'modal',
                                        'data-id': member.id,
                                        'type': 'button'
                                });
                                $(privilegeDiv).append(privilegeBtn);

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

                                let privilegeI = document.createElement('I');
                                $(privilegeI).attr('class', 'fal fa-user-shield fontSize22');
                                $(privilegeBtn).append(privilegeI);
                        });
                } else {
                        var $femaleMembers = $('.femaleMembers')
                        $femaleMembers.empty();
                        data.result.forEach(function (member, index) {
                                let eachMainDiv = document.createElement('DIV');
                                $(eachMainDiv).attr('class', 'row text-center align-items-center border-radius-xl py-2');
                                if (index % 2 == 0) {
                                        $(eachMainDiv).attr('class', 'row text-center align-items-center bg-light border-radius-xl py-2');
                                }
                                $femaleMembers.append(eachMainDiv);

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

                                let privilegeDiv = document.createElement('DIV');
                                $(privilegeDiv).attr('class', 'col-3');
                                $(eachMainDiv).append(privilegeDiv);

                                let privilegeBtn = document.createElement('BUTTON');
                                $(privilegeBtn).attr({
                                        'class': 'btn btn-link m-0 px-0 py-1 privilegeBtn',
                                        'data-bs-target': '#privilege',
                                        'data-bs-toggle': 'modal',
                                        'data-id': member.id,
                                        'type': 'button'
                                });
                                $(privilegeDiv).append(privilegeBtn);

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

                                let privilegeI = document.createElement('I');
                                $(privilegeI).attr('class', 'fal fa-user-shield fontSize22');
                                $(privilegeBtn).append(privilegeI);
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

        // pagination 初始化
        var $malePagination = $('DIV.malePagination UL');
        var $femalePagination = $('DIV.femalePagination UL');
        createPagination($malePagination, $('DIV.malePagination').data('page'), 1);
        createPagination($femalePagination, $('DIV.femalePagination').data('page'), 1);

        function createPagination(pagination, totalPages, page) {
                pagination.empty();

                let active;
                let beforePage = page - 1;
                let afterPage = page + 1;
                if (page > 1) {
                        let prevBtn = document.createElement('LI');
                        $(prevBtn).attr({
                                'class': 'botton prev paginationBtn',
                                'data-page': page - 1
                        });
                        pagination.append(prevBtn);

                        let prevBtnSpan = document.createElement('SPAN');
                        $(prevBtnSpan).attr('class', 'text-bolder text-sm');
                        $(prevBtn).append(prevBtnSpan);

                        let prevBtnI = document.createElement('I');
                        $(prevBtnI).attr('class', 'fas fa-angle-left me-1');
                        $(prevBtnSpan).append(prevBtnI);
                        $(prevBtnSpan).append('上一頁');
                }

                if (page > 3) {
                        let firstLi = document.createElement('LI');
                        $(firstLi).attr({
                                'class': 'first numb paginationBtn',
                                'data-page': 1
                        });
                        pagination.append(firstLi);

                        let firstLiSpan = document.createElement('SPAN');
                        $(firstLiSpan).append(1);
                        $(firstLi).append(firstLiSpan);

                        if (page > 3 && totalPages > 5) {
                                let dotsLi = document.createElement('LI');
                                $(dotsLi).attr('class', 'dots');
                                pagination.append(dotsLi);

                                let dotsLiSpan = document.createElement('SPAN');
                                $(dotsLiSpan).append('...');
                                $(dotsLi).append(dotsLiSpan);
                        }
                }

                if (page == totalPages && totalPages > 2) {
                        beforePage = beforePage - 2;
                } else if (page == totalPages - 1 && totalPages > 2) {
                        beforePage = beforePage - 1;
                }
                if (page == 1) {
                        afterPage = afterPage + 2;
                } else if (page == 2) {
                        afterPage = afterPage + 1;
                }

                for (var plength = beforePage; plength <= afterPage; plength++) {
                        if (plength > totalPages) { //if plength is greater than totalPage length then continue
                                continue;
                        }
                        if (plength == 0) { //if plength is 0 than add +1 in plength value
                                plength = plength + 1;
                        }
                        if (page == plength) { //if page is equal to plength than assign active string in the active variable
                                active = "active";
                        } else { //else leave empty to the active variable
                                active = "";
                        }
                        let numbLi = document.createElement('LI');
                        $(numbLi).attr({
                                'class': 'numb paginationBtn ' + active,
                                'data-page': plength
                        });
                        pagination.append(numbLi);

                        let plengthSpan = document.createElement('SPAN');
                        $(plengthSpan).append(plength);
                        $(numbLi).append(plengthSpan);
                }

                if (page < totalPages - 2) {
                        if (page < totalPages - 2 && totalPages > 5) {
                                let dotsLi = document.createElement('LI');
                                $(dotsLi).attr('class', 'dots');
                                pagination.append(dotsLi);

                                let dotsLiSpan = document.createElement('SPAN');
                                $(dotsLiSpan).append('...');
                                $(dotsLi).append(dotsLiSpan);
                        }
                        let lastLi = document.createElement('LI');
                        $(lastLi).attr({
                                'class': 'last numb paginationBtn',
                                'data-page': totalPages
                        });
                        pagination.append(lastLi);

                        let totalPagesSpan = document.createElement('SPAN');
                        $(totalPagesSpan).append(totalPages);
                        $(lastLi).append(totalPagesSpan);
                }

                if (page < totalPages) {
                        let nextBtn = document.createElement('LI');
                        $(nextBtn).attr({
                                'class': 'botton next paginationBtn',
                                'data-page': page + 1
                        });
                        pagination.append(nextBtn);

                        let nextBtnSpan = document.createElement('SPAN');
                        $(nextBtnSpan).attr('class', 'text-bolder text-sm');
                        $(nextBtn).append(nextBtnSpan);

                        let nextBtnI = document.createElement('I');
                        $(nextBtnI).attr('class', 'fas fa-angle-right ms-1');
                        $(nextBtnSpan).append('下一頁');
                        $(nextBtnSpan).append(nextBtnI);
                }

                $('Li.paginationBtn').click(function () {
                        var btn = this;
                        page = $(btn).data('page');
                        var pagination = $(btn).closest('DIV.pagination UL');
                        searchValue = $(btn).closest('DIV.tab-pane').find('INPUT[name="searchValue"]').val().replace(/^0/g, '');
                        searchGender = $(btn).closest('DIV.tab-pane').find('INPUT[name="searchGender"]').val();
                        $.post(
                                '/dashboard/searchMember.json',
                                {
                                        searchValue: searchValue,
                                        searchGender: searchGender,
                                        pageSearch: page
                                },
                                function (data) {
                                        console.log(data.totalPages)
                                        createPagination(pagination, data.totalPages, page);
                                        search(data);
                                },
                                'json'
                                );
                        return false;
                });
        }

        $('Li.paginationBtn').click(function () {
                var btn = this;
                page = $(btn).data('page');
                var pagination = $(btn).closest('DIV.pagination UL');
                searchValue = $(btn).closest('DIV.tab-pane').find('INPUT[name="searchValue"]').val().replace(/^0/g, '');
                searchGender = $(btn).closest('DIV.tab-pane').find('INPUT[name="searchGender"]').val();
                $.post(
                        '/dashboard/searchMember.json',
                        {
                                searchValue: searchValue,
                                searchGender: searchGender,
                                pageSearch: page
                        },
                        function (data) {
                                createPagination(pagination, data.totalPages, page);
                                search(data);
                        },
                        'json'
                        );
                return false;
        });

        $('BUTTON.upgradeVipBtn').click(function () {
                var btn = this;
                lover = $(btn).data('id');
        });

        $('BUTTON.upgradeVip').click(function () {
                var date = $('INPUT[name="expDate"]').val();
                $.post(
                        '/dashboard/upgradeVip.json',
                        {
                                lover: lover,
                                date: date
                        },
                        function (data) {
                                $('#upgradeModal').modal('hide');
                                var $upgradeVipWrap = $('BUTTON.upgradeVipBtn[data-id="' + lover + '"]').closest('DIV').siblings('DIV.upgradeVipWrap');
                                $upgradeVipWrap.empty();
                                var span = document.createElement('SPAN');
                                $upgradeVipWrap.append(span);
                                var i = document.createElement('I');
                                $(i).attr('class', 'fad fa-crown me-1');
                                $(span).append(i);
                                var span2 = document.createElement('SPAN');
                                $(span2).append('1288');
                                $(span).append(span2);
                                var div = document.createElement('DIV');
                                $(div).append(date);
                                $upgradeVipWrap.append(div);
                        },
                        'json'
                        );
                return false;
        });
});