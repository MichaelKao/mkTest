$(document).ready(function () {
        const setLabel = (lbl, val, $slider, unit) => {
                const label = $slider.closest('DIV#slider-div').find('DIV.' + lbl + '-slider-handle');
                $(label).empty();

                let div = document.createElement('DIV');
                $(div).attr('class', 'sliderLabel');
                $(div).append(val);
                if (lbl === 'max') {
                        $(div).append(unit);
                }
                $(label).append(div);
        }

        const setLabels = (values, $slider, unit) => {
                setLabel("min", values[0], $slider, unit);
                setLabel("max", values[1], $slider, unit);
        }

        $('#age').slider().on('slide', function (e) {
                setLabels(e.value, $('#age'), '歲');
        });
        setLabels($('#age').attr("data-value").split(","), $('#age'), '歲');

        $('#height').slider().on('slide', function (e) {
                setLabels(e.value, $('#height'), '公分');
        });
        setLabels($('#height').attr("data-value").split(","), $('#height'), '公分');

        $('#weight').slider().on('slide', function (e) {
                setLabels(e.value, $('#weight'), '公斤');
        });
        setLabels($('#weight').attr("data-value").split(","), $('#weight'), '公斤');


        $('DIV.toggleSearchArea').click(function () {
                let $searchArea = $('SECTION.searchArea');
                let $searchAreaI = $('DIV.toggleSearchArea I');
                let $searchAreaSPAN = $('DIV.toggleSearchArea SPAN');
                $searchArea.slideToggle();
                $searchArea.toggleClass('open');
                if ($searchArea.hasClass('open')) {
                        $searchAreaI.attr('class', 'fad fa-chevron-double-down me-1');
                        $searchAreaSPAN.html('展開搜尋區塊');
                } else {
                        $searchAreaI.attr('class', 'fad fa-chevron-double-up me-1');
                        $searchAreaSPAN.html('隱藏搜尋區塊');
                }
        });
});