$(document).ready(function () {
        $('.carousel').each(function () {
                $(this).slick({
                        dots: false,
                        infinite: true,
                        arrows: false,
                        centerMode: true,
                        slidesToShow: 4,
                        slidesToScroll: 4,
                        responsive: [
                                {
                                        breakpoint: 1024,
                                        settings: {
                                                slidesToShow: 3,
                                                slidesToScroll: 3
                                        }
                                },
                                {
                                        breakpoint: 600,
                                        settings: {
                                                slidesToShow: 2,
                                                slidesToScroll: 2
                                        }
                                },
                                {
                                        breakpoint: 480,
                                        settings: {
                                                slidesToShow: 1,
                                                slidesToScroll: 1
                                        }
                                }
                        ]
                });
        });
        $('.carousel').slickLightbox({
                itemSelector: 'a'
        });

        $('.single img').each(function () {
                $(this).css('height', this.width);
        });

        function countLines(ele) {
                var styles = window.getComputedStyle(ele);
                var lh = parseInt(styles.lineHeight);
                var h = parseInt(styles.height);
                var lc = Math.round(h / lh);
                console.log('line count:', lc, 'line-height:', lh, 'height:', h);
                return lc;
        }

        function lineHeight(ele) {
                var styles = window.getComputedStyle(ele);
                var lh = parseInt(styles.lineHeight);
                return lh;
        }

        $('.article').each(function () {
                var article = this;
                var lineCount = countLines(this);
                var maxHeight = lineHeight(article) * 5;
                $(article).css('max-height', maxHeight);
                if (lineCount > 5) {
                        var div = document.createElement('DIV');
                        $(div).attr('class', 'more text-xs cursor-pointer d-inline');
                        $(div).append('...查看更多');
                        $(article).closest('DIV.articleBox').append(div);

                        $(div).click(function () {
                                var $more = $(this);
                                var $article = $more.siblings('.article');
                                if ($article.css('max-height') == maxHeight + 'px') {
                                        $article.css('max-height', '100%')
                                        $more.text('隱藏內容');
                                } else {
                                        $article.css('max-height', maxHeight)
                                        $more.text('...查看更多');
                                }
                        });
                }
        });

        $('.seeComment').click(function () {
                $(this).closest('DIV.postFooter').find('DIV.comments').fadeToggle();
        });
});