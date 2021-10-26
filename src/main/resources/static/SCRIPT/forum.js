$(document).ready(function () {


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

        $('INPUT[name="forumTag"]').on('change', function () {

                console.log(forumTag);
        });

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
                                        console.log(data);
                                        location.reload();
                                },
                                error: function (err) {
                                        console.log(err);
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