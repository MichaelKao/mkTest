$(document).ready(function () {
        $(window).scrollTop(0);

        $('BUTTON.scrollTopFloatBtn').click(function () {
                $(window).scrollTop(0);
        });

        imgUpload({
                inputId: 'file', //input框id
                imgBox: 'imgBox' //圖片容器id
        });
        var imgSrc = []; //圖片路徑
        var imgFile = []; //文件流
        var imgName = []; //圖片名字

        $(postBtn).on('click', function () {
                newPost(imgFile);
        });
        //選擇圖片
        function imgUpload(obj) {
                var oInput = '#' + obj.inputId;
                var imgBox = '#' + obj.imgBox;
                $(oInput).on('change', function () {
                        var fileImg = $(oInput)[0];
                        var fileList = fileImg.files;
                        for (var i = 0; i < fileList.length; i++) {
                                var imgSrcI = getObjectURL(fileList[i]);
                                imgName.push(fileList[i].name);
                                imgSrc.push(imgSrcI);
                                imgFile.push(fileList[i]);
                        }
                        addNewContent(imgBox);
                });
        }

        //圖片展示
        function addNewContent(obj) {
                $(imgBox).html('');
                for (var a = 0; a < imgSrc.length; a++) {
                        var imgContainer = document.createElement('DIV');
                        $(imgContainer).attr('class', 'imgContainer m-1 position-relative');
                        $(obj).append(imgContainer);
                        var delBtn = document.createElement('BUTTON');
                        $(delBtn).attr({
                                'class': 'btn btn-link delBtn',
                                'data-index': a,
                                'type': 'button'
                        });
                        $(imgContainer).append(delBtn);
                        $(delBtn).click(function () {
                                var del = this;
                                removeImg(del, $(del).data('index'));
                        });
                        var delIcon = document.createElement('I');
                        $(delIcon).attr('class', 'fas fa-times-circle text-primary fontSize25');
                        $(delBtn).append(delIcon);
                        var img = document.createElement('IMG');
                        $(img).attr({
                                'alt': imgName[a],
                                'class': 'border-radius-md imageShadow',
                                'src': imgSrc[a],
                                'title': imgName[a]
                        });
                        $(imgContainer).append(img);
                        $(img).click(function () {
                                imgDisplay(this);
                        });
                }
        }

        //删除
        function removeImg(obj, index) {
                imgSrc.splice(index, 1);
                imgFile.splice(index, 1);
                imgName.splice(index, 1);
                var boxId = '#' + $(obj).parent('.imgContainer').parent().attr('id');
                addNewContent(boxId);
        }

        //上傳(將文件流數組傳到後台)
        function newPost(files) {
                var hashTags = [];
                $('INPUT[name="hashTag"]:checked').each(function () {
                        hashTags.push($(this).val());
                });
                var formData = new FormData();
                formData.append('title', $('INPUT[name="title"]').val());
                formData.append('markdown', $('TEXTAREA[name="markdown"]').val());
                formData.append('hashTags', hashTags);
                for (var i = 0; i < files.length; i++) {
                        formData.append('illustrations', files[i]);
                }
                if (files) {
                        $('DIV.loadingWrap').css('display', 'block');
                        $.ajax({
                                method: 'post',
                                url: '/forum/add.asp',
                                data: formData,
                                dataType: 'json',
                                cache: false,
                                processData: false,
                                contentType: false,
                                traditional: true,
                                success: function (data) {
                                        if (data.response) {
                                                location.reload();
                                        }
                                        if (!data.response) {
                                                $('.toast-body').html(data.reason);
                                                $('.toast').toast('show');
                                                $('DIV.loadingWrap').css('display', 'none');
                                        }
                                },
                                error: function (err) {
                                        console.log(err);
                                        $('DIV.loadingWrap').css('display', 'none');
                                }
                        });
                }
        }

        //圖片燈箱
        function imgDisplay(obj) {
                var src = $(obj).attr('src');
                var lightBox = document.createElement('DIV');
                $(lightBox).attr('class', 'lightBox');
                $('BODY').append(lightBox);
                var lightBoxImg = document.createElement('IMG');
                $(lightBoxImg).attr({
                        'class': 'lightBoxImg',
                        'src': src
                });
                $(lightBox).append(lightBoxImg);
                var lightBoxClose = document.createElement('DIV');
                $(lightBoxClose).attr('class', 'lightBoxClose');
                $(lightBoxClose).append('×');
                $(lightBox).append(lightBoxClose);
                $(lightBoxClose).click(function () {
                        closePicture(this);
                });
        }

        //關閉
        function closePicture(obj) {
                $(obj).parent('DIV').remove();
        }

        //圖片預覽路徑
        function getObjectURL(file) {
                var url = null;
                if (window.createObjectURL != undefined) { // basic
                        url = window.createObjectURL(file);
                } else if (window.URL != undefined) { // mozilla(firefox)
                        url = window.URL.createObjectURL(file);
                } else if (window.webkitURL != undefined) { // webkit or chrome
                        url = window.webkitURL.createObjectURL(file);
                }
                return url;
        }

        $('.carousel').each(function () {
                $(this).slick({
                        dots: true,
                        infinite: true,
                        arrows: false,
                        centerMode: false,
                        slidesToShow: 4,
                        slidesToScroll: 4,
                        lazyLoad: 'ondemand',
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
                                        centerMode: false,
                                        settings: {
                                                slidesToShow: 2,
                                                slidesToScroll: 2
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
                var profileImage = $(btn).closest('DIV').siblings('DIV.avatar').find('IMG').attr('src');
                var forumIdentifier = $(btn).closest('DIV').siblings('INPUT[name="forumIdentifier"]').val();
                $.post(
                        '/forum/comment.asp',
                        {
                                forumThread: forumIdentifier,
                                content: value
                        },
                        function (data) {
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
                                        'class': 'avatarImg',
                                        'src': profileImage
                                });
                                $(avatar).append(avatarImg);
                                var flexComment = document.createElement('DIV');
                                $(flexComment).attr('class', 'flex-grow-1 ms-2 ms-sm-3');
                                $(div).append(flexComment);
                                var commentMeta = document.createElement('DIV');
                                $(commentMeta).attr('class', 'commentMeta d-flex align-items-baseline');
                                $(flexComment).append(commentMeta);
                                var name = document.createElement('DIV');
                                $(name).attr('class', 'me-1 text-bold');
                                $(name).append('艾瑪');
                                $(commentMeta).append(name);
                                var i = document.createElement('I');
                                $(i).attr('class', 'fas fa-shield-check me-1');
                                if (data.relief === 'true') {
                                        $(i).addClass('text-success');
                                }
                                $(commentMeta).append(i);
                                var date = document.createElement('SPAN');
                                $(date).attr('class', 'text-xs');
                                $(date).append(data.created);
                                $(commentMeta).append(date);
                                var commentBody = document.createElement('DIV');
                                $(commentBody).attr('class', 'commentBody text-dark text-sm text-white');
                                $(commentBody).append(data.content);
                                $(flexComment).append(commentBody);
                                $textarea.val('');
                                if (!($comments.hasClass('open'))) {
                                        $comments.fadeToggle();
                                        $comments.toggleClass('open');
                                }
                                $commentCount.html(parseInt($commentCount.html()) + 1);
                        },
                        'json'
                        );
        });

        var timeout;
        window.addEventListener('scroll', handler);

        function handler() {
                clearTimeout(timeout);
                timeout = setTimeout(function () {
                        if ($(window).scrollTop() + $(window).height() > $(document).height() - 300) {
                                window.removeEventListener('scroll', handler);
                                var $nextPage = $('INPUT[name="nextPage"]');
                                var sort = $('INPUT[name="sort"]').val();
                                var p = $nextPage.val();
                                var load = document.createElement('DIV');
                                $(load).attr('class', 'loadMore');
                                $('.posts').append(load);
                                if (p > 0) {
                                        $.post(
                                                '/forum/loadMore.json',
                                                {
                                                        p: p,
                                                        sort: sort

                                                },
                                                function (data) {
                                                        $(load).remove();
                                                        if (data.forumThread.length !== 0) {
                                                                $nextPage.val(parseInt(p) + 1);
                                                                appendData(data);
                                                                window.addEventListener('scroll', handler);
                                                        } else if (data.forumThread.length == 0) {
                                                                $nextPage.val(-1);
                                                                var div = document.createElement('DIV');
                                                                $(div).attr('class', 'text-center text-xs mt-4');
                                                                $(div).append('沒有更多文章囉！');
                                                                $('.posts').append(div);
                                                                window.removeEventListener('scroll', handler);
                                                        }
                                                },
                                                'json'
                                                );
                                }
                        }
                }, 50);
        }

        function appendData(data) {
                data.forumThread.forEach(function (forum) {
                        var postDiv = document.createElement('DIV');
                        $(postDiv).attr('class', 'post');
                        $('.posts').append(postDiv);

                        var headerDiv = document.createElement('DIV');
                        $(headerDiv).attr('class', 'header');
                        $(postDiv).append(headerDiv);

                        var leftDiv = document.createElement('DIV');
                        $(leftDiv).attr('class', 'left');
                        $(headerDiv).append(leftDiv);
                        var avatarImg = document.createElement('IMG');
                        $(avatarImg).attr({
                                'alt': 'avatarImg',
                                'class': 'avatar shadow me-2',
                                'src': forum.authorProfileImage
                        });
                        $(leftDiv).append(avatarImg);
                        var userDiv = document.createElement('DIV');
                        $(userDiv).attr('class', 'username');
                        $(leftDiv).append(userDiv);
                        var titleDiv = document.createElement('DIV');
                        $(titleDiv).attr('class', 'title text-dark');
                        $(titleDiv).append(forum.title);
                        $(userDiv).append(titleDiv);
                        var nameDiv = document.createElement('DIV');
                        $(nameDiv).attr('class', 'name text-xs');
                        $(userDiv).append(nameDiv);
                        var nameSpan = document.createElement('SPAN');
                        $(nameSpan).append(forum.authorNickname);
                        $(nameDiv).append(nameSpan);
                        var reliefI = document.createElement('I');
                        $(reliefI).attr('class', 'fas fa-shield-check ms-1');
                        if (forum.relief === 'true') {
                                $(reliefI).addClass('text-success');
                        }
                        $(nameDiv).append(reliefI);

                        var hashTagsDiv = document.createElement('DIV');
                        $(hashTagsDiv).attr('class', 'hashtags');
                        $(postDiv).append(hashTagsDiv);
                        forum.hashTags.forEach(function (hashtag) {
                                var badgeDiv = document.createElement('DIV');
                                $(badgeDiv).attr('class', 'me-1 badge bg-dark m-0 px-2 py-1');
                                $(badgeDiv).append('#' + hashtag);
                                $(hashTagsDiv).append(badgeDiv);
                        });

                        var contentDiv = document.createElement('DIV');
                        $(contentDiv).attr('class', 'content');
                        $(postDiv).append(contentDiv);
                        if (forum.illustrations) {
                                var carouselDiv = document.createElement('DIV');
                                $(carouselDiv).attr('class', 'carousel');
                                $(contentDiv).append(carouselDiv);
                                forum.illustrations.forEach(function (illu) {
                                        var singleDiv = document.createElement('DIV');
                                        $(singleDiv).attr('class', 'single');
                                        $(carouselDiv).append(singleDiv);
                                        var a = document.createElement('A');
                                        $(a).attr('href', illu);
                                        $(singleDiv).append(a);
                                        var img = document.createElement('IMG');
                                        $(img).attr('class', 'resizeImg');
                                        $(img).attr({
                                                'alt': 'illustration',
                                                'data-lazy': illu
                                        });
                                        $(a).append(img);
                                });
                                $(carouselDiv).each(function () {
                                        $(this).slick({
                                                dots: true,
                                                infinite: true,
                                                arrows: false,
                                                centerMode: false,
                                                slidesToShow: 4,
                                                slidesToScroll: 4,
                                                lazyLoad: 'ondemand',
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
                                                                centerMode: false,
                                                                settings: {
                                                                        slidesToShow: 2,
                                                                        slidesToScroll: 2
                                                                }
                                                        }
                                                ]
                                        });
                                });
                                $(carouselDiv).slickLightbox({
                                        itemSelector: 'a'
                                });
                                $('.resizeImg').css('height', $('.resizeImg').width());
                        }

                        var articleBoxDiv = document.createElement('DIV');
                        $(articleBoxDiv).attr('class', 'articleBox');
                        $(contentDiv).append(articleBoxDiv);
                        var articleDiv = document.createElement('DIV');
                        $(articleDiv).attr('class', 'article');
                        $(articleDiv).append(forum.markdown);
                        $(articleBoxDiv).append(articleDiv);
                        $(articleDiv).each(function () {
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
                        var dateDiv = document.createElement('DIV');
                        $(dateDiv).attr('class', 'date text-xs text-right');
                        $(dateDiv).append(forum.date);
                        $(contentDiv).append(dateDiv);

                        var postFooterDiv = document.createElement('DIV');
                        $(postFooterDiv).attr('class', 'postFooter primary-gradient');
                        $(postDiv).append(postFooterDiv);
                        var commentDiv = document.createElement('DIV');
                        $(commentDiv).attr('class', 'd-flex');
                        $(postFooterDiv).append(commentDiv);
                        var seeCommentDiv = document.createElement('DIV');
                        $(seeCommentDiv).attr('class', 'seeComment text-white me-auto cursor-pointer');
                        $(postFooterDiv).append(seeCommentDiv);
                        $(seeCommentDiv).click(function () {
                                var $comments = $(this).closest('DIV.postFooter').find('DIV.comments');
                                $comments.fadeToggle();
                                $comments.toggleClass('open');
                        });
                        var commentI = document.createElement('I');
                        $(commentI).attr('class', 'fas fa-comment me-2');
                        $(seeCommentDiv).append(commentI);
                        var commentSpan = document.createElement('SPAN');
                        $(commentSpan).attr('class', 'commentCount');
                        $(commentSpan).html(forum.commentCount);
                        $(seeCommentDiv).append(commentSpan);

                        var commentsDiv = document.createElement('DIV');
                        $(commentsDiv).attr({
                                'class': 'comments mt-3',
                                'style': 'display: none;'
                        });
                        $(postFooterDiv).append(commentsDiv);
                        forum.comment.forEach(function (comment) {
                                var commentDiv = document.createElement('DIV');
                                $(commentDiv).attr('class', 'comment d-flex mb-2');
                                $(commentsDiv).append(commentDiv);
                                var avatarFlexDiv = document.createElement('DIV');
                                $(avatarFlexDiv).attr('class', 'flex-shrink-0');
                                $(commentDiv).append(avatarFlexDiv);
                                var avatarDiv = document.createElement('DIV');
                                $(avatarDiv).attr('class', 'avatar rounded-circle');
                                $(avatarFlexDiv).append(avatarDiv);
                                var avatarImg = document.createElement('IMG');
                                $(avatarImg).attr({
                                        'alt': 'avatarImg',
                                        'class': 'avatarImg',
                                        'src': comment.profileImage
                                });
                                $(avatarDiv).append(avatarImg);
                                var metaFlexDiv = document.createElement('DIV');
                                $(metaFlexDiv).attr('class', 'flex-grow-1 ms-2 ms-sm-3');
                                $(commentDiv).append(metaFlexDiv);
                                var commentMetaDiv = document.createElement('DIV');
                                $(commentMetaDiv).attr('class', 'commentMeta d-flex align-items-baseline');
                                $(metaFlexDiv).append(commentMetaDiv);
                                var nameDiv = document.createElement('DIV');
                                $(nameDiv).attr('class', 'me-1 text-bold');
                                $(nameDiv).append(comment.nickname);
                                $(commentMetaDiv).append(nameDiv);
                                var reliefI = document.createElement('I');
                                $(reliefI).attr('class', 'fas fa-shield-check me-1');
                                if (comment.relief === 'true') {
                                        $(reliefI).addClass('text-success');
                                }
                                $(commentMetaDiv).append(reliefI);
                                var dateSpan = document.createElement('SPAN');
                                $(dateSpan).attr('class', 'text-xs');
                                $(dateSpan).append(comment.date);
                                $(commentMetaDiv).append(dateSpan);

                                var commentBodyDiv = document.createElement('DIV');
                                $(commentBodyDiv).attr('class', 'commentBody text-dark text-sm text-white');
                                $(commentBodyDiv).append(comment.content);
                                $(metaFlexDiv).append(commentBodyDiv);
                        });

                        var commentArea = document.createElement('DIV');
                        $(commentArea).attr('class', 'd-flex align-items-center mt-3');
                        $(postFooterDiv).append(commentArea);
                        var forumIdentifier = document.createElement('INPUT');
                        $(forumIdentifier).attr({
                                'name': 'forumIdentifier',
                                'type': 'hidden',
                                'value': forum.identifier
                        });
                        $(commentArea).append(forumIdentifier);
                        var commentAvatar = document.createElement('DIV');
                        $(commentAvatar).attr('class', 'avatar rounded-circle');
                        $(commentArea).append(commentAvatar);
                        var commentAvatarImg = document.createElement('IMG');
                        $(commentAvatarImg).attr({
                                'alt': 'self-profileImage',
                                'class': 'avatarImg',
                                'src': data.seenerProfileImage
                        });
                        $(commentAvatar).append(commentAvatarImg);

                        var commentTextarea = document.createElement('DIV');
                        $(commentTextarea).attr('class', 'w-100 ms-2 position-relative');
                        $(commentArea).append(commentTextarea);

                        var textarea = document.createElement('TEXTAREA');
                        $(textarea).attr({
                                'class': 'form-control text-white commentTextarea',
                                'rows': '1',
                                'placeholder': '新增留言...'
                        });
                        $(commentTextarea).append(textarea);
                        $(textarea).on('keyup', function () {
                                var textarea = this;
                                var value = $(textarea).val().trim();
                                var $btn = $(textarea).siblings('BUTTON.commentBtn');
                                $btn.removeAttr('disabled', 'false');
                                if (value.length == 0) {
                                        $btn.attr('disabled', 'true');
                                }
                        });
                        var button = document.createElement('BUTTON');
                        $(button).attr({
                                'class': 'btn btn-link m-0 p-0 commentBtn text-sm text-bold',
                                'disabled': 'true'
                        });
                        $(button).append('提交');
                        $(commentTextarea).append(button);
                        $(button).click(function (e) {
                                e.preventDefault(e);
                                var btn = this;
                                $(btn).attr('disabled', 'true');
                                var $textarea = $(btn).siblings('TEXTAREA');
                                var value = $textarea.val();
                                var $comments = $(btn).closest('DIV.postFooter').find('DIV.comments');
                                var $commentCount = $(btn).closest('DIV.postFooter').find('SPAN.commentCount');
                                var profileImage = $(btn).closest('DIV').siblings('DIV.avatar').find('IMG').attr('src');
                                var forumIdentifier = $(btn).closest('DIV').siblings('INPUT[name="forumIdentifier"]').val();
                                $.post(
                                        '/forum/comment.asp',
                                        {
                                                forumThread: forumIdentifier,
                                                content: value
                                        },
                                        function (data) {
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
                                                        'class': 'avatarImg',
                                                        'src': profileImage
                                                });
                                                $(avatar).append(avatarImg);
                                                var flexComment = document.createElement('DIV');
                                                $(flexComment).attr('class', 'flex-grow-1 ms-2 ms-sm-3');
                                                $(div).append(flexComment);
                                                var commentMeta = document.createElement('DIV');
                                                $(commentMeta).attr('class', 'commentMeta d-flex align-items-baseline');
                                                $(flexComment).append(commentMeta);
                                                var name = document.createElement('DIV');
                                                $(name).attr('class', 'me-1 text-bold');
                                                $(name).append('艾瑪');
                                                $(commentMeta).append(name);
                                                var i = document.createElement('I');
                                                $(i).attr('class', 'fas fa-shield-check me-1');
                                                if (data.relief === 'true') {
                                                        $(i).addClass('text-success');
                                                }
                                                $(commentMeta).append(i);
                                                var date = document.createElement('SPAN');
                                                $(date).attr('class', 'text-xs');
                                                $(date).append(data.created);
                                                $(commentMeta).append(date);
                                                var commentBody = document.createElement('DIV');
                                                $(commentBody).attr('class', 'commentBody text-dark text-sm text-white');
                                                $(commentBody).append(data.content);
                                                $(flexComment).append(commentBody);
                                                $textarea.val('');
                                                if (!($comments.hasClass('open'))) {
                                                        $comments.fadeToggle();
                                                        $comments.toggleClass('open');
                                                }
                                                $commentCount.html(parseInt($commentCount.html()) + 1);
                                        },
                                        'json'
                                        );
                        });
                });
        }

        $('SPAN.sort').click(function () {
                window.removeEventListener('scroll', handler);
                var span = this;
                var anotherSpan = $(span).siblings('SPAN.sort');
                var sort = $(span).attr('id');
                var $nextPage = $('INPUT[name="nextPage"]');
                var $sort = $('INPUT[name="sort"]');
                $.post(
                        '/forum/loadMore.json',
                        {
                                p: 0,
                                sort: sort
                        },
                        function (data) {
                                $('.posts').empty();
                                anotherSpan.removeClass('active');
                                if (!$(span).hasClass('active')) {
                                        $(span).addClass('active');
                                }
                                $nextPage.val(1);
                                $sort.val(sort);
                                appendData(data);
                        },
                        'json'
                        );
                window.addEventListener('scroll', handler);
        });
});