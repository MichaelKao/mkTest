$(document).ready(function () {

        // 登入後 Line Notify 提醒
        if ($('INPUT[name="signIn"]').val() === 'true') {
                showModal();
        }

        let $mobileRefreshBtn = $('BUTTON#mobileRefreshBtn');
        if (isMobile() && $('INPUT[name="signIn"]').val() === 'true') {
                var currentPageType = getCookie('currentPageType');
                if ($('INPUT[name="gender"]').val() === 'male') {
                        if (currentPageType !== '') {
                                // 手機版按照上一次看的頁面類型 cookie 來 highlight tabs
                                var a = $('A.' + currentPageType + 'A');
                                a.addClass('active');
                                loverMobilePage(a);
                                $mobileRefreshBtn.data('type', currentPageType);
                        } else {
                                var a = $('A.reliefA');
                                a.addClass('active');
                                loverMobilePage(a);
                                $mobileRefreshBtn.data('type', 'relief');
                        }
                } else {
                        if (currentPageType !== '') {
                                var a = $('A.' + currentPageType + 'A');
                                a.addClass('active');
                                loverMobilePage(a);
                                $mobileRefreshBtn.data('type', currentPageType);
                        } else {
                                var a = $('A.vipA');
                                a.addClass('active');
                                loverMobilePage(a);
                                $mobileRefreshBtn.data('type', 'vip');
                        }
                }
        }

        $('A.mobileModeA').click(function () {
                let a = this;
                loverMobilePage(a);
        });

        function loverMobilePage(a) {
                let type = $(a).data('type');
                $mobileRefreshBtn.data('type', type);
                let p = 0;
                if (getCookie(type + 'Page') !== '') {
                        p = getCookie(type + 'Page');
                }
                let wrap = $('DIV.mobileMode');
                let pageBtnWrap = $('DIV#mobilePageBtn');
                $.post(
                        '/seeMoreLover.json',
                        {
                                p: p,
                                type: type
                        },
                        (data) => {
                        if (data.response) {
                                wrap.empty();
                                pageBtnWrap.empty();
                                $('A.mobileModeA').each(function (i, obj) {
                                        $(this).removeClass('active');
                                });
                                $(a).addClass('active');
                                loverWebPage(type, wrap, data, pageBtnWrap);
                        }
                },
                        'json'
                        );
                return false;
        }

        $('BUTTON.pageBtn').click(function () {
                let button = this;
                let wrap;
                var pageBtnWrap;
                let p = $(button).data('page');
                let type = $(button).data('type');
                if ($(button).parent('DIV').attr('id') === 'mobilePageBtn') {
                        wrap = $('DIV.mobileMode');
                        pageBtnWrap = $('DIV#mobilePageBtn');
                        $mobileRefreshBtn.data('type', type);
                } else {
                        wrap = $('DIV.' + type);
                        pageBtnWrap = $('DIV.' + type + 'PageBtn');
                }
                $.post(
                        '/seeMoreLover.json',
                        {
                                p: p,
                                type: type
                        },
                        (data) => {
                        if (data.response) {
                                wrap.empty();
                                pageBtnWrap.empty();
                                loverWebPage(type, wrap, data, pageBtnWrap);
                        }
                },
                        'json'
                        );
                return false;
        });

        $('BUTTON.refreshBtn').click(function () {
                let button = this;
                let wrap;
                var pageBtnWrap;
                let type = $(button).data('type');
                console.log('mobiletype', type);
                if ($(button).attr('id') === 'mobileRefreshBtn') {
                        wrap = $('DIV.mobileMode');
                        pageBtnWrap = $('DIV#mobilePageBtn');
                } else {
                        wrap = $('DIV.' + type);
                        pageBtnWrap = $('DIV.' + type + 'PageBtn');
                }
                $.post(
                        '/seeMoreLover.json',
                        {
                                p: 0,
                                type: type
                        },
                        (data) => {
                        if (data.response) {
                                wrap.empty();
                                pageBtnWrap.empty();
                                loverWebPage(type, wrap, data, pageBtnWrap);
                        }
                },
                        'json'
                        );
                return false;
        });

        function loverWebPage(type, wrap, data, pageBtnWrap) {
                data.result.forEach(function (item) {
                        let outterA = document.createElement('A');
                        $(outterA).attr({
                                'class': 'position-relative m-1',
                                'href': '/profile/' + item.identifier + '/'
                        });
                        wrap.append(outterA);

                        let IMG = document.createElement('IMG');
                        $(IMG).attr({
                                'class': 'border-radius-md',
                                'src': item.profileImage,
                                'width': '152'
                        });
                        $(outterA).append(IMG);
                        let iconDiv = document.createElement('DIV');
                        $(iconDiv).attr({
                                'class': 'position-absolute right-0 text-center',
                                'style': 'width: 32px; top: 5px;'
                        });
                        $(outterA).append(iconDiv);
                        if (item.vip) {
                                let vipI = document.createElement('I');
                                $(vipI).attr('class', 'fad fa-crown fontSize22 text-yellow text-shadow');
                                $(iconDiv).append(vipI);
                        }

                        if (item.relief) {
                                let reliefI = document.createElement('I');
                                $(reliefI).attr('class', 'fas fa-shield-check fontSize22 text-success text-shadow');
                                $(iconDiv).append(reliefI);
                        }

                        if (item.following) {
                                let followingDiv = document.createElement('DIV');
                                $(followingDiv).attr({
                                        'class': 'position-absolute left-0 text-center',
                                        'style': 'width: 32px; top: 5px;'
                                });
                                $(outterA).append(followingDiv);
                                let followingI = document.createElement('I');
                                $(followingI).attr('class', 'fas fa-heart-circle text-primary fontSize22');
                                $(followingDiv).append(followingI);
                        }

                        let infoDiv = document.createElement('DIV');
                        $(infoDiv).attr('class', 'position-absolute imageShadow bottom-0 left-0 right-0 mx-3 mb-1 py-0 text-bolder text-dark bg-white opacity-7 border-radius-md p-1 text-xs text-center');
                        $(outterA).append(infoDiv);
                        let nameAgeDiv = document.createElement('DIV');
                        $(infoDiv).append(nameAgeDiv);
                        let infoSpan = document.createElement('SPAN');
                        $(infoSpan).html(item.nickname + ' ' + item.age);
                        $(nameAgeDiv).append(infoSpan);
                        if (item.relationship) {
                                let relationshipDiv = document.createElement('DIV');
                                $(infoDiv).append(relationshipDiv);
                                let relationshipSpan = document.createElement('SPAN');
                                $(relationshipSpan).html('尋找' + item.relationship);
                                $(relationshipDiv).append(relationshipSpan);
                        }
                        let locationDiv = document.createElement('DIV');
                        $(infoDiv).append(locationDiv);
                        if (typeof (item.location) != 'undefined') {
                                item.location.forEach(function (item, i, array) {
                                        if (i === array.length - 1) {
                                                let locationSpan = document.createElement('SPAN');
                                                $(locationSpan).html(item.location);
                                                $(locationDiv).append(locationSpan);
                                                return;
                                        }
                                        let locationSpan = document.createElement('SPAN');
                                        $(locationSpan).attr('class', 'me-1');
                                        $(locationSpan).html(item.location);
                                        $(locationDiv).append(locationSpan);
                                });
                        }
                });

                if (typeof (data.pagination.hasPrev) != 'undefined') {
                        var prevBtn = document.createElement('BUTTON');
                        $(prevBtn).attr({
                                'class': 'btn btn-primary btn-round pageBtn text-lg mx-1 m-0 px-2 py-1',
                                'data-type': type,
                                'data-page': data.pagination.hasPrev
                        });
                        pageBtnWrap.append(prevBtn);
                        var prevI = document.createElement('I');
                        $(prevI).attr('class', 'fad fa-angle-double-left ms-1');
                        $(prevBtn).append(prevI);
                        $(prevBtn).append('上一頁');
                        $(prevBtn).click(function () {
                                let button = this;
                                let wrap;
                                var pageBtnWrap;
                                let p = $(button).data('page');
                                let type = $(button).data('type');
                                if ($(button).parent('DIV').attr('id') === 'mobilePageBtn') {
                                        wrap = $('DIV.mobileMode');
                                        pageBtnWrap = $('DIV#mobilePageBtn');
                                } else {
                                        wrap = $('DIV.' + type);
                                        pageBtnWrap = $('DIV.' + type + 'PageBtn');
                                }
                                $.post(
                                        '/seeMoreLover.json',
                                        {
                                                p: p,
                                                type: type
                                        },
                                        (data) => {
                                        if (data.response) {
                                                wrap.empty();
                                                pageBtnWrap.empty();
                                                loverWebPage(type, wrap, data, pageBtnWrap);
                                        }
                                },
                                        'json'
                                        );
                                return false;
                        });
                }
                if (typeof (data.pagination.hasNext) != 'undefined') {
                        var nextBtn = document.createElement('BUTTON');
                        $(nextBtn).attr({
                                'class': 'btn btn-primary btn-round pageBtn text-lg mx-1 m-0 px-2 py-1',
                                'data-type': type,
                                'data-page': data.pagination.hasNext
                        });
                        pageBtnWrap.append(nextBtn);
                        $(nextBtn).append('下一頁');
                        var nextI = document.createElement('I');
                        $(nextI).attr('class', 'fad fa-angle-double-right ms-1');
                        $(nextBtn).append(nextI);
                        $(nextBtn).click(function () {
                                let button = this;
                                let wrap;
                                var pageBtnWrap;
                                let p = $(button).data('page');
                                let type = $(button).data('type');
                                if ($(button).parent('DIV').attr('id') === 'mobilePageBtn') {
                                        wrap = $('DIV.mobileMode');
                                        pageBtnWrap = $('DIV#mobilePageBtn');
                                } else {
                                        wrap = $('DIV.' + type);
                                        pageBtnWrap = $('DIV.' + type + 'PageBtn');
                                }
                                $.post(
                                        '/seeMoreLover.json',
                                        {
                                                p: p,
                                                type: type
                                        },
                                        (data) => {
                                        if (data.response) {
                                                wrap.empty();
                                                pageBtnWrap.empty();
                                                loverWebPage(type, wrap, data, pageBtnWrap);
                                        }
                                },
                                        'json'
                                        );
                                return false;
                        });
                }
        }

        // 判斷是否為手機裝置
        function isMobile() {
                var mobileAgents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
                var mobileFlag = false;
                // 根據 userAgent 判斷是否是手機
                for (var v = 0; v < mobileAgents.length; v++) {
                        if (navigator.userAgent.indexOf(mobileAgents[v]) > 0) {
                                mobileFlag = true;
                                break;
                        }
                }
                var screenWidth = $(window).width();
                var screenHeight = $(window).height();
                // 根據 screen 判斷
                if (screenWidth < 769 && screenHeight < 950) {
                        mobileFlag = true;
                }

                return mobileFlag;
        }

        // 取得 cookie
        function getCookie(cname) {
                var name = cname + '=';
                var ca = document.cookie.split(';');
                for (var i = 0; i < ca.length; i++) {
                        var c = ca[i].trim();
                        if (c.indexOf(name) === 0) {
                                return c.substring(name.length, c.length);
                        }
                }
                return '';
        }

        // 設定 cookie
        function setCookie(cname, cvalue, expireMinute) {
                var d = new Date();
                d.setTime(d.getTime() + (expireMinute * 60 * 1000));
                var expires = "expires=" + d.toGMTString();
                document.cookie = cname + "=" + cvalue + "; " + expires;
        }

        // LINE Notify 的提醒 modal
        function showModal() {
                if (getCookie('youngMeLineNotifyModal') === '') {
                        $('#lineNotifyModal').modal('show');
                        setCookie('youngMeLineNotifyModal', 'NO', 1 * 60 * 24);
                        return;
                }
        }

        // 首頁的輪播
        $('.carousel').flickity({
                'autoPlay': 2500
        });
});