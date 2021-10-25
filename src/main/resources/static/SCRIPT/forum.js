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
                var $comments = $(this).closest('DIV.postFooter').find('DIV.comments');
                $comments.fadeToggle();
                $comments.toggleClass('open');
        });

        $('TEXTAREA').on('keyup', function () {
                var textarea = this;
                var value = $(textarea).val().trim();
                var $btn = $(textarea).siblings('BUTTON.commentBtn');
                $btn.removeAttr('disabled', 'false');
                if (value.length == 0) {
                        $btn.attr('disabled', 'true');
                }
        });

        $('.commentBtn').click(function (e) {
                e.preventDefault(e);
                var btn = this;
                $(btn).attr('disabled', 'true');
                var $textarea = $(btn).siblings('TEXTAREA');
                var value = $textarea.val();
                var $comments = $(btn).closest('DIV.postFooter').find('DIV.comments');
                var $commentCount = $(btn).closest('DIV.postFooter').find('SPAN.commentCount');

                var div = document.createElement('DIV');
                $(div).attr('class', 'comment d-flex mb-2');
                $comments.prepend(div);

                var flexAvatar = document.createElement('DIV');
                $(flexAvatar).attr('class', 'flex-shrink-0');
                $(div).append(flexAvatar);
                var avatar = document.createElement('DIV');
                $(avatar).attr('class', 'avatar rounded-circle');
                $(flexAvatar).append(avatar);
                var avatarImg = document.createElement('IMG');
                $(avatarImg).attr({
                        'alt': 'avatarImg',
                        'class': 'avatar-img',
                        'src': 'https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b'
                });
                $(avatar).append(avatarImg);

                var flexComment = document.createElement('DIV');
                $(flexComment).attr('class', 'flex-shrink-flex-grow-1 ms-2 ms-sm-30');
                $(div).append(flexComment);
                var commentMeta = document.createElement('DIV');
                $(commentMeta).attr('class', 'commentMeta d-flex align-items-baseline');
                $(flexComment).append(commentMeta);
                var name = document.createElement('DIV');
                $(name).attr('class', 'me-2 text-bold');
                $(name).append('艾瑪');
                $(commentMeta).append(name);
                var date = document.createElement('SPAN');
                $(date).attr('class', 'text-xs');
                $(date).append('2021/10/25');
                $(commentMeta).append(date);
                var commentBody = document.createElement('DIV');
                $(commentBody).attr('class', 'commentBody text-dark text-sm text-white');
                $(commentBody).append(value);
                $(flexComment).append(commentBody);

                $textarea.val('');
                if (!($comments.hasClass('open'))) {
                        $comments.fadeToggle();
                        $comments.toggleClass('open');
                }
                $commentCount.html(parseInt($commentCount.html()) + 1);
        });
});