$(document).ready(function () {
        $('.portfolio-slides').slick({
                slidesToShow: 2,
                slidesToScroll: 1,
                centerMode: true,
                autoplay: true,
                autoplaySpeed: 2000,
                arrows: true,
        });
        $('.portfolio-slides').slickLightbox({
                itemSelector: 'a',
                navigateByKeyboard: true
        });

        $('.single img').css({
                width: Math.floor($('.portfolio-slides').width() / 2.8),
                height: Math.floor($('.portfolio-slides').width() / 2.8)
        });
        $(window).resize(function () {
                $('.single img').css({
                        width: Math.floor($('.portfolio-slides').width() / 2.8),
                        height: Math.floor($('.portfolio-slides').width() / 2.8)
                });
        });
});