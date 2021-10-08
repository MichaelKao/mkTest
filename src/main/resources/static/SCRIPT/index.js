$(document).ready(function () {
        let p;
        let type;
        let wrap;

        if ($('INPUT[name="signIn"]').val() === 'true') {
                showModal();
        }

        if (isMobile() && $('INPUT[name="signIn"]').val() === 'true') {
                if ($('INPUT[name="gender"]').val() === 'male') {
                        $('A.reliefA').addClass('active');
                        document.querySelector("DIV.mobileMode").innerHTML = document.querySelector("DIV.relief").innerHTML;
                } else {
                        $('A.vipA').addClass('active');
                        document.querySelector("DIV.mobileMode").innerHTML = document.querySelector("DIV.vip").innerHTML;
                }
        }

        $('A.mobileModeA').click(function () {
                let a = this;
                type = $(a).data('type');
                $('BUTTON#mobileMode').data('type', type);
                $('BUTTON#mobileMode').data('page', 0);
                wrap = $('DIV.mobileMode');
                $.post(
                        '/seeMoreLover.json',
                        {
                                p: 0,
                                type: type
                        },
                        (data) => {
                        if (data.response) {
                                $('A.mobileModeA').each(function (i, obj) {
                                        $(this).removeClass('active');
                                });
                                $(a).addClass('active');
                                wrap.empty();
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
                                });
                        }
                },
                        'json'
                        );
                return false;
        });

        $('BUTTON.seeMoreBtn').click(function () {
                let button = this;
                p = $(button).data('page');
                type = $(button).data('type');
                if ($(button).attr('id') === 'mobileMode') {
                        wrap = $('DIV.mobileMode');
                } else {
                        wrap = $('DIV.' + type);
                }
                $.post(
                        '/seeMoreLover.json',
                        {
                                p: parseInt(p) + 1,
                                type: type
                        },
                        (data) => {
                        if (data.response) {
                                wrap.empty();
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
                                });
                        }
                        if (data.lastPage) {
                                $(button).data('page', -1);
                        } else {
                                $(button).data('page', parseInt(p) + 1);
                        }
                },
                        'json'
                        );
                return false;
        });

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

        function setCookie(cname, cvalue, expireMinute) {
                var d = new Date();
                d.setTime(d.getTime() + (expireMinute * 60 * 1000));
                var expires = "expires=" + d.toGMTString();
                document.cookie = cname + "=" + cvalue + "; " + expires;
        }

        function showModal() {
                if (getCookie('youngMeLineNotifyModal') === '') {
                        $('#lineNotifyModal').modal('show');
                        setCookie('youngMeLineNotifyModal', 'NO', 1 * 60 * 24);
                        return;
                }
        }

        $('.carousel').flickity({
                'autoPlay': 2500
        });


        console.log('getCookie', document.cookie);
});