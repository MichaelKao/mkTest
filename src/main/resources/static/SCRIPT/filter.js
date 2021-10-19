$(document).ready(function () {
        const setLabel = (lbl, val, $slider) => {
                const label = $slider.closest('DIV#slider-div').find('DIV.' + lbl + '-slider-handle');
                $(label).empty();

                let div = document.createElement('DIV');
                $(div).attr('class', 'sliderLabel');
                $(div).append(val);
                $(label).append(div);
        }

        const setLabels = (values, $slider) => {
                setLabel("min", values[0], $slider);
                setLabel("max", values[1], $slider);
        }

        $('#age').slider().on('slide', function (e) {
                setLabels(e.value, $('#age'));
        });
        setLabels($('#age').attr("data-value").split(","), $('#age'));

        $('#height').slider().on('slide', function (e) {
                setLabels(e.value, $('#height'));
        });
        setLabels($('#height').attr("data-value").split(","), $('#height'));

        $('#weight').slider().on('slide', function (e) {
                setLabels(e.value, $('#weight'));
        });
        setLabels($('#weight').attr("data-value").split(","), $('#weight'));


        let $searchArea = $('SECTION.searchArea');
        let $searchAreaI = $('DIV.toggleSearchArea I.openCloseBtn');
        let $searchAreaSPAN = $('DIV.toggleSearchArea SPAN');
        $('DIV.toggleSearchArea').click(function () {
                $searchArea.slideToggle();
                $searchArea.toggleClass('open');
                if ($searchArea.hasClass('open')) {
                        $searchAreaI.attr('class', 'fad fa-chevron-double-up me-1 openCloseBtn');
                        $searchAreaSPAN.html('隱藏搜尋區塊');
                } else {
                        $searchAreaI.attr('class', 'fad fa-chevron-double-down me-1 openCloseBtn');
                        $searchAreaSPAN.html('展開搜尋區塊');
                }
        });


        var wrap = $('DIV.filterResult');
        var pageBtnWrap = $('DIV#pageBtnWrap');

        $('BUTTON.filterBtn').click(function () {
                var annualIncome;
                var allowance;
                if (typeof ($('SELECT[name="annualIncome"]').val()) != 'undefined') {
                        annualIncome = $('SELECT[name="annualIncome"]').val();
                        allowance = 0;
                } else {
                        allowance = $('SELECT[name="allowance"]').val();
                        annualIncome = 0;
                }
                $.post(
                        '/filter.json',
                        {
                                p: 0,
                                nickname: $('INPUT[name="nickname"]').val(),
                                maximumAge: $('INPUT#age').val().split(',')[1],
                                minimumAge: $('INPUT#age').val().split(',')[0],
                                maximumHeight: $('INPUT#height').val().split(',')[1],
                                minimumHeight: $('INPUT#height').val().split(',')[0],
                                maximumWeight: $('INPUT#weight').val().split(',')[1],
                                minimumWeight: $('INPUT#weight').val().split(',')[0],
                                bodyType: $('SELECT[name="bodyType"]').val(),
                                education: $('SELECT[name="education"]').val(),
                                marriage: $('SELECT[name="marriage"]').val(),
                                smoking: $('SELECT[name="smoking"]').val(),
                                drinking: $('SELECT[name="drinking"]').val(),
                                annualIncome: annualIncome,
                                allowance: allowance,
                                location: $('SELECT[name="location"]').val(),
                                service: $('SELECT[name="service"]').val()
                        },
                        function (data) {
                                if (data.response) {
                                        $searchArea.slideToggle();
                                        $searchArea.toggleClass('open');
                                        if ($searchArea.hasClass('open')) {
                                                $searchAreaI.attr('class', 'fad fa-chevron-double-up me-1 openCloseBtn');
                                                $searchAreaSPAN.html('隱藏搜尋區塊');
                                        } else {
                                                $searchAreaI.attr('class', 'fad fa-chevron-double-down me-1 openCloseBtn');
                                                $searchAreaSPAN.html('展開搜尋區塊');
                                        }
                                        filterPagination(data);
                                } else {
                                        $('.toast-body').html(data.reason);
                                        $('.toast').toast('show');
                                }
                        },
                        'json'
                        );
        });

        function filterPagination(data) {
                wrap.empty();
                pageBtnWrap.empty();
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
                        var pageBtn = document.createElement('BUTTON');
                        $(pageBtn).attr({
                                'class': 'btn btn-primary btn-round pageBtn text-lg mx-1 m-0 px-2 py-1',
                                'data-page': data.pagination.hasPrev
                        });
                        pageBtnWrap.append(pageBtn);
                        var pageI = document.createElement('I');
                        $(pageI).attr('class', 'fad fa-angle-double-left ms-1');
                        $(pageBtn).append(pageI);
                        $(pageBtn).append('上一頁');
                }
                if (typeof (data.pagination.hasNext) != 'undefined') {
                        var pageBtn = document.createElement('BUTTON');
                        $(pageBtn).attr({
                                'class': 'btn btn-primary btn-round pageBtn text-lg mx-1 m-0 px-2 py-1',
                                'data-page': data.pagination.hasNext
                        });
                        pageBtnWrap.append(pageBtn);
                        $(pageBtn).append('下一頁');
                        var pageI = document.createElement('I');
                        $(pageI).attr('class', 'fad fa-angle-double-right ms-1');
                        $(pageBtn).append(pageI);
                }
                $(pageBtn).click(function () {
                        var btn = this;
                        let p = $(btn).data('page');
                        var annualIncome;
                        var allowance;
                        if (typeof ($('SELECT[name="annualIncome"]').val()) != 'undefined') {
                                annualIncome = $('SELECT[name="annualIncome"]').val();
                                allowance = 0;
                        } else {
                                allowance = $('SELECT[name="allowance"]').val();
                                annualIncome = 0;
                        }
                        $.post(
                                '/filter.json',
                                {
                                        p: p,
                                        nickname: $('INPUT[name="nickname"]').val(),
                                        maximumAge: $('INPUT#age').val().split(',')[1],
                                        minimumAge: $('INPUT#age').val().split(',')[0],
                                        maximumHeight: $('INPUT#height').val().split(',')[1],
                                        minimumHeight: $('INPUT#height').val().split(',')[0],
                                        maximumWeight: $('INPUT#weight').val().split(',')[1],
                                        minimumWeight: $('INPUT#weight').val().split(',')[0],
                                        bodyType: $('SELECT[name="bodyType"]').val(),
                                        education: $('SELECT[name="education"]').val(),
                                        marriage: $('SELECT[name="marriage"]').val(),
                                        smoking: $('SELECT[name="smoking"]').val(),
                                        drinking: $('SELECT[name="drinking"]').val(),
                                        annualIncome: annualIncome,
                                        allowance: allowance,
                                        location: $('SELECT[name="location"]').val(),
                                        service: $('SELECT[name="service"]').val()
                                },
                                function (data) {
                                        if (data.response) {
                                                filterPagination(data);
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
});