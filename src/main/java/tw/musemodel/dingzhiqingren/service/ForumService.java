package tw.musemodel.dingzhiqingren.service;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tw.musemodel.dingzhiqingren.entity.ForumThread;
import tw.musemodel.dingzhiqingren.entity.ForumThreadComment;
import tw.musemodel.dingzhiqingren.entity.ForumThreadHashTag;
import tw.musemodel.dingzhiqingren.entity.ForumThreadIllustration;
import tw.musemodel.dingzhiqingren.entity.ForumThreadTag;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.model.Forum;
import tw.musemodel.dingzhiqingren.model.JavaScriptObjectNotation;
import tw.musemodel.dingzhiqingren.repository.ForumThreadCommentRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadHashTagRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadIllustrationRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadTagRepository;
import static tw.musemodel.dingzhiqingren.service.AmazonWebServices.AMAZON_S3;
import static tw.musemodel.dingzhiqingren.service.AmazonWebServices.BUCKET_NAME;

/**
 * 服务层：论坛
 *
 * @author p@musemodel.tw
 */
@Service
public class ForumService {

        private static final Logger LOGGER = LoggerFactory.getLogger(ForumService.class);

        private static final String DIRECTORY = "forum";

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Autowired
        private Servant servant;

        @Autowired
        private ForumThreadIllustrationRepository forumThreadIllustrationRepository;

        @Autowired
        private ForumThreadCommentRepository forumThreadCommentRepository;

        @Autowired
        private ForumThreadRepository forumThreadRepository;

        @Autowired
        private ForumThreadTagRepository forumThreadTagRepository;

        @Autowired
        private ForumThreadHashTagRepository forumThreadHashTagRepository;

        @Value("classpath:sql/男女论坛_熱門.sql")
        private Resource forumThreadsSortByPopularResource;

        @Value("classpath:sql/男女论坛_最新.sql")
        private Resource forumThreadsSortByLatestResource;

        /**
         * 创建论坛绪及插图。
         *
         * @param author 作者
         * @param title 标题
         * @param markdown 内容
         * @param multipartFiles 插图
         * @return 论坛绪
         */
        public JSONObject createThreadWithIllustrations(Lover author, String title, String markdown, ForumThreadTag[] hashTags, Collection<MultipartFile> multipartFiles) {
                if (title.isBlank()) {
                        return new JavaScriptObjectNotation().
                                withResponse(false).
                                withReason("請填寫文章標題").toJSONObject();
                }
                if (markdown.isBlank()) {
                        return new JavaScriptObjectNotation().
                                withResponse(false).
                                withReason("請填寫文章內容").toJSONObject();
                }
                if (hashTags.length == 0) {
                        return new JavaScriptObjectNotation().
                                withResponse(false).
                                withReason("請選擇至少一種標籤").toJSONObject();
                }
                ForumThread forumThread = forumThreadRepository.saveAndFlush(
                        new ForumThread(
                                author,
                                title.trim(),
                                markdown.trim()
                        )
                );
                final String threadIdentifier = forumThread.getIdentifier().toString();

                for (ForumThreadTag forumThreadTag : hashTags) {
                        forumThreadHashTagRepository.saveAndFlush(
                                new ForumThreadHashTag(
                                        forumThreadTag,
                                        forumThread
                                )
                        );
                }

                if (Objects.nonNull(multipartFiles)) {
                        for (MultipartFile multipartFile : multipartFiles) {
                                UUID identifier = UUID.randomUUID();
                                File file = new File(
                                        Servant.TEMPORARY_DIRECTORY,
                                        identifier.toString()
                                );

                                try {
                                        multipartFile.transferTo(file);

                                        PutObjectRequest putObjectRequest = new PutObjectRequest(
                                                AmazonWebServices.BUCKET_NAME,
                                                String.format(
                                                        "%s/%s/%s",
                                                        DIRECTORY,
                                                        threadIdentifier,
                                                        identifier.toString()
                                                ),
                                                file
                                        );
                                        ObjectMetadata objectMetadata = new ObjectMetadata();
                                        objectMetadata.setContentType(
                                                multipartFile.getContentType()
                                        );
                                        putObjectRequest.setMetadata(objectMetadata);
                                        AmazonWebServices.AMAZON_S3.putObject(
                                                putObjectRequest
                                        );

                                        forumThreadIllustrationRepository.saveAndFlush(
                                                new ForumThreadIllustration(
                                                        identifier,
                                                        forumThread
                                                )
                                        );
                                } catch (IOException | IllegalStateException exception) {
                                        LOGGER.info(
                                                "创建论坛绪插图时发生异常❗️",
                                                exception
                                        );
                                }

                                file.delete();
                        }
                }

                return new JavaScriptObjectNotation().
                        withResponse(true).
                        withResult(forumThread.getId()).toJSONObject();
        }

        /**
         * 浏览(索引)并分页论坛绪。(熱門順序；按照最新留言排序)
         *
         * @param gender 性别
         * @param page 第几页
         * @param size 每页几笔
         * @return 分页响应
         */
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        public List<ForumThread> readAllThreadsSortByPopular(boolean gender, int page, int size) {
                List<Forum> forums = jdbcTemplate.query(
                        (Connection connection) -> {
                                PreparedStatement preparedStatement;
                                try {
                                        preparedStatement = connection.prepareStatement(
                                                FileCopyUtils.copyToString(
                                                        new InputStreamReader(
                                                                forumThreadsSortByPopularResource.getInputStream(),
                                                                Servant.UTF_8
                                                        )
                                                )
                                        );
                                        preparedStatement.setBoolean(1, gender);
                                        return preparedStatement;
                                } catch (IOException ignore) {
                                        return null;
                                }
                        },
                        (ResultSet resultSet, int rowNum) -> {
                                Forum forum = new Forum();
                                if (Objects.isNull(resultSet.getDate("shi_chuo"))) {
                                        forum.setId(resultSet.getLong("id"));
                                        forum.setCreated(new Date(0));
                                } else {
                                        forum.setId(resultSet.getLong("id"));
                                        forum.setCreated(resultSet.getDate("shi_chuo"));
                                }
                                return forum;
                        }
                );

                Collections.sort(forums, Comparator.reverseOrder());
                Pageable paging = PageRequest.of(page, size);
                int start = Math.min((int) paging.getOffset(), forums.size());
                int end = Math.min((start + paging.getPageSize()), forums.size());
                Page<Forum> forumPage = new PageImpl<>(forums.subList(start, end), paging, forums.size());

                List<ForumThread> forumThreads = new ArrayList<>();
                for (Forum forum : forumPage) {
                        forumThreads.add(
                                forumThreadRepository.findById(forum.getId()).get()
                        );
                }

                return forumThreads;
        }

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        public List<ForumThread> readAllThreadsSortByLatest(boolean gender, int page, int size) {
                List<Long> forumIds = jdbcTemplate.query(
                        (Connection connection) -> {
                                PreparedStatement preparedStatement;
                                try {
                                        preparedStatement = connection.prepareStatement(
                                                FileCopyUtils.copyToString(
                                                        new InputStreamReader(
                                                                forumThreadsSortByLatestResource.getInputStream(),
                                                                Servant.UTF_8
                                                        )
                                                )
                                        );
                                        preparedStatement.setBoolean(1, gender);
                                        return preparedStatement;
                                } catch (IOException ignore) {
                                        return null;
                                }
                        },
                        (ResultSet resultSet, int rowNum) -> {
                                return resultSet.getLong("id");
                        }
                );

                Page<ForumThread> forumThreads = forumThreadRepository.findByIdInOrderByCreatedDesc(
                        forumIds,
                        PageRequest.of(page, size)
                );

                return forumThreads.getContent();
        }

        /**
         * 論壇 document
         *
         * @param self
         * @param pagination
         * @return
         * @throws IOException
         */
        public Document forumToDocument(Lover self, List<ForumThread> list) throws IOException {

                Document document = Servant.parseDocument();
                Element documentElement = document.getDocumentElement();

                // 本人
                Element selfElement = document.createElement("self");
                documentElement.appendChild(selfElement);
                selfElement.setAttribute("nickname", self.getNickname());
                selfElement.setAttribute("identifier", self.getIdentifier().toString());
                selfElement.setAttribute(
                        "profileImage",
                        String.format(
                                "https://%s/profileImage/%s",
                                Servant.STATIC_HOST,
                                self.getProfileImage()
                        ));
                selfElement.setAttribute(
                        "relief",
                        Objects.nonNull(self.getRelief()) ? self.getRelief().toString() : "false"
                );

                // 標籤關鍵詞
                for (ForumThreadTag forumThreadTag : forumThreadTagRepository.findAll()) {
                        Element forumThreadTagElement = document.createElement("forumThreadTag");
                        forumThreadTagElement.setAttribute("id", forumThreadTag.getId().toString());
                        forumThreadTagElement.setTextContent(forumThreadTag.getPhrase());
                        documentElement.appendChild(forumThreadTagElement);
                }

                // 論壇文章
                Element forumThreadsElement = document.createElement("forumThreads");
                documentElement.appendChild(forumThreadsElement);
                for (ForumThread forumThread : list) {
                        Element forumThreadElement = document.createElement("forumThread");
                        forumThreadsElement.appendChild(forumThreadElement);
                        forumThreadElement.setAttribute(
                                "identifier",
                                forumThread.getIdentifier().toString()
                        );

                        Lover author = forumThread.getAuthor();
                        Element authorElement = document.createElement("author");
                        forumThreadElement.appendChild(authorElement);
                        authorElement.setAttribute(
                                "nickname",
                                author.getNickname()
                        );
                        authorElement.setAttribute(
                                "identifier",
                                author.getIdentifier().toString()
                        );
                        authorElement.setAttribute(
                                "profileImage",
                                String.format(
                                        "https://%s/profileImage/%s",
                                        Servant.STATIC_HOST,
                                        author.getProfileImage()
                                ));
                        authorElement.setAttribute(
                                "relief",
                                Objects.nonNull(author.getRelief()) ? author.getRelief().toString() : "false"
                        );

                        Element titleElement = document.createElement("title");
                        forumThreadElement.appendChild(titleElement);
                        titleElement.setTextContent(forumThread.getTitle());

                        Element markdownElement = document.createElement("markdown");
                        forumThreadElement.appendChild(markdownElement);
                        String html = servant.markdownToHtml(forumThread.getMarkdown());
                        CDATASection cDATASection = document.createCDATASection(html);
                        markdownElement.appendChild(cDATASection);

                        Element dateElement = document.createElement("date");
                        dateElement.setTextContent(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
                                        Servant.toTaipeiZonedDateTime(
                                                forumThread.getCreated()
                                        ).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
                                )
                        );
                        forumThreadElement.appendChild(dateElement);

                        for (ForumThreadHashTag forumThreadHashTag : forumThreadHashTagRepository.findByForumThread(forumThread)) {
                                Element hashTagElement = document.createElement("hashTag");
                                hashTagElement.setTextContent(forumThreadHashTag.getForumThreadTag().getPhrase());
                                forumThreadElement.appendChild(hashTagElement);
                        }

                        Collection<ForumThreadIllustration> forumThreadIllustrations = forumThreadIllustrationRepository.findByForumThread(forumThread);
                        if (forumThreadIllustrations.size() > 0) {
                                Element illustrationsElement = document.createElement("illustrations");
                                forumThreadElement.appendChild(illustrationsElement);
                                for (ForumThreadIllustration forumThreadIllustration : forumThreadIllustrations) {
                                        Element illustrationElement = document.createElement("illustration");
                                        illustrationElement.setTextContent(
                                                String.format(
                                                        "https://%s/forum/%s/%s",
                                                        Servant.STATIC_HOST,
                                                        forumThread.getIdentifier(),
                                                        forumThreadIllustration.getIdentifier()
                                                ));
                                        illustrationsElement.appendChild(illustrationElement);
                                }
                        }
                        // 留言
                        int commentCount = forumThreadCommentRepository.countByForumThread(forumThread);
                        Element commentsElement = document.createElement("comments");
                        forumThreadElement.appendChild(commentsElement);
                        commentsElement.setAttribute(
                                "commentCount",
                                Integer.toString(commentCount)
                        );
                        for (ForumThreadComment forumThreadComment : forumThreadCommentRepository.findByForumThreadOrderByCreatedDesc(forumThread)) {
                                Element commentElement = document.createElement("comment");
                                commentsElement.appendChild(commentElement);
                                commentElement.setAttribute(
                                        "identifier",
                                        forumThreadComment.getIdentifier().toString()
                                );
                                // 留言內容
                                String commenthtml = servant.markdownToHtml(forumThreadComment.getContent());
                                CDATASection commentCDATASection = document.createCDATASection(commenthtml);
                                commentElement.appendChild(commentCDATASection);
                                commentElement.setAttribute(
                                        "date",
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
                                                Servant.toTaipeiZonedDateTime(
                                                        forumThreadComment.getCreated()
                                                ).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
                                        )
                                );

                                // 留言方
                                Lover commenter = forumThreadComment.getCommenter();
                                Element commenterElement = document.createElement("commenter");
                                commentElement.appendChild(commenterElement);
                                commenterElement.setAttribute(
                                        "commenterNickname",
                                        commenter.getNickname()
                                );
                                commenterElement.setAttribute(
                                        "commenterProfileImage",
                                        String.format(
                                                "https://%s/profileImage/%s",
                                                Servant.STATIC_HOST,
                                                commenter.getProfileImage()
                                        ));
                                commenterElement.setAttribute(
                                        "commenterRelief",
                                        Objects.nonNull(commenter.getRelief()) ? commenter.getRelief().toString() : "false"
                                );
                                commenterElement.setAttribute(
                                        "commenterIdentifier",
                                        commenter.getIdentifier().toString()
                                );
                        }
                }

                return document;
        }

        /**
         * 論壇 load more
         *
         * @param self
         * @param pagination
         * @return
         * @throws IOException
         */
        public JSONObject loadMoreForumThread(Lover self, List<ForumThread> list) throws IOException {

                JSONObject json = new JSONObject();

                json.put(
                        "seenerProfileImage",
                        String.format(
                                "https://%s/profileImage/%s",
                                Servant.STATIC_HOST,
                                self.getProfileImage()
                        )
                ).put(
                        "seenerIdentifier",
                        self.getIdentifier()
                );

                // 論壇文章
                JSONArray forumArray = new JSONArray();
                for (ForumThread forumThread : list) {
                        JSONObject forumJson = new JSONObject();
                        forumJson.put(
                                "identifier",
                                forumThread.getIdentifier()
                        );

                        Lover author = forumThread.getAuthor();
                        forumJson.
                                put(
                                        "identifier",
                                        forumThread.getIdentifier()
                                ).
                                put(
                                        "authorNickname",
                                        author.getNickname()
                                ).
                                put(
                                        "authorIdentifier",
                                        author.getIdentifier()
                                ).
                                put(
                                        "authorProfileImage",
                                        String.format(
                                                "https://%s/profileImage/%s",
                                                Servant.STATIC_HOST,
                                                author.getProfileImage()
                                        )
                                ).
                                put(
                                        "relief",
                                        Objects.nonNull(author.getRelief()) ? author.getRelief().toString() : "false"
                                );

                        forumJson.
                                put(
                                        "title",
                                        forumThread.getTitle()
                                ).
                                put(
                                        "markdown",
                                        servant.markdownToHtml(forumThread.getMarkdown())
                                ).
                                put(
                                        "date",
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
                                                Servant.toTaipeiZonedDateTime(
                                                        forumThread.getCreated()
                                                ).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
                                        )
                                );

                        List<String> hashTags = new ArrayList();
                        for (ForumThreadHashTag forumThreadHashTag : forumThreadHashTagRepository.findByForumThread(forumThread)) {
                                JSONArray tagArray = new JSONArray();
                                hashTags.add(forumThreadHashTag.getForumThreadTag().getPhrase());
                        }
                        forumJson.put(
                                "hashTags",
                                hashTags
                        );

                        Collection<ForumThreadIllustration> forumThreadIllustrations = forumThreadIllustrationRepository.findByForumThread(forumThread);
                        if (forumThreadIllustrations.size() > 0) {
                                List<String> illustrations = new ArrayList();
                                for (ForumThreadIllustration forumThreadIllustration : forumThreadIllustrations) {
                                        illustrations.add(
                                                String.format(
                                                        "https://%s/forum/%s/%s",
                                                        Servant.STATIC_HOST,
                                                        forumThread.getIdentifier(),
                                                        forumThreadIllustration.getIdentifier()
                                                ));
                                }
                                forumJson.put(
                                        "illustrations",
                                        illustrations
                                );
                        }

                        // 留言
                        int commentCount = forumThreadCommentRepository.countByForumThread(forumThread);
                        forumJson.put(
                                "commentCount",
                                commentCount
                        );
                        JSONArray commentArray = new JSONArray();
                        for (ForumThreadComment forumThreadComment : forumThreadCommentRepository.findByForumThreadOrderByCreatedDesc(forumThread)) {
                                JSONObject comment = new JSONObject();
                                // 留言方
                                Lover commenter = forumThreadComment.getCommenter();
                                comment.
                                        put(
                                                "identifier",
                                                forumThreadComment.getIdentifier()
                                        ).
                                        put(
                                                "content",
                                                servant.markdownToHtml(forumThreadComment.getContent())
                                        ).
                                        put(
                                                "date",
                                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
                                                        Servant.toTaipeiZonedDateTime(
                                                                forumThreadComment.getCreated()
                                                        ).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
                                                )
                                        ).
                                        put(
                                                "nickname",
                                                commenter.getNickname()
                                        ).
                                        put(
                                                "commenterIdentifier",
                                                commenter.getIdentifier()
                                        ).
                                        put(
                                                "profileImage",
                                                String.format(
                                                        "https://%s/profileImage/%s",
                                                        Servant.STATIC_HOST,
                                                        commenter.getProfileImage()
                                                )
                                        ).
                                        put(
                                                "relief",
                                                Objects.nonNull(commenter.getRelief()) ? commenter.getRelief().toString() : "false"
                                        );
                                commentArray.put(comment);
                        }
                        forumJson.put("comment", commentArray);
                        forumArray.put(forumJson);
                }
                json.put(
                        "forumThread",
                        forumArray
                );
                return json;
        }

        /**
         * 读取某则论坛绪。
         *
         * @param identifier 识别码
         * @return 论坛绪
         */
        public ForumThread readOneThread(UUID identifier) {
                return forumThreadRepository.findOneByIdentifier(identifier);
        }

        @Data
        private class Illustration implements Serializable {

                private static final long serialVersionUID = -7679921167558711132L;

                private final Long id;

                private final String illustrationPath;

                public Illustration(Long id, String illustrationPath) {
                        this.id = id;
                        this.illustrationPath = illustrationPath;
                }

                @Override
                public String toString() {
                        try {
                                return new JsonMapper().writeValueAsString(this);
                        } catch (JsonProcessingException ignore) {
                                return "null";
                        }
                }
        }

        /**
         * 读取某则论坛绪。
         *
         * @param identifier 识别码
         * @return 论坛绪
         */
        public JSONObject readOneThreadInJson(UUID identifier) {
                JSONObject json = new JSONObject();
                ForumThread forumThread = forumThreadRepository.findOneByIdentifier(identifier);
                List<Short> hashtags = new ArrayList<>();
                for (ForumThreadHashTag forumThreadHashTag : forumThreadHashTagRepository.findByForumThread(forumThread)) {
                        hashtags.add(forumThreadHashTag.getForumThreadTag().getId());
                }
                JSONArray illuArray = new JSONArray();
                for (ForumThreadIllustration forumThreadIllustration : forumThreadIllustrationRepository.findByForumThread(forumThread)) {
                        JSONObject illuJson = new JSONObject();
                        illuJson.put(
                                "id",
                                forumThreadIllustration.getId()
                        );
                        illuJson.put(
                                "path",
                                String.format(
                                        "https://%s/forum/%s/%s",
                                        Servant.STATIC_HOST,
                                        forumThread.getIdentifier(),
                                        forumThreadIllustration.getIdentifier()
                                )
                        );
                        illuArray.put(illuJson);
                }

                json.
                        put(
                                "identifier",
                                identifier
                        ).
                        put(
                                "markdown",
                                forumThread.getMarkdown()
                        ).
                        put(
                                "title",
                                forumThread.getTitle()
                        ).
                        put(
                                "hashtags",
                                hashtags
                        ).
                        put(
                                "illustrations",
                                illuArray
                        );

                return json;
        }

        /**
         * 留言
         *
         * @param forumThread
         * @param commenter
         * @param content
         * @return
         */
        public JSONObject comment(ForumThread forumThread, Lover commenter, String content) {
                ForumThreadComment forumThreadComment
                        = new ForumThreadComment(forumThread, commenter, content);
                forumThreadCommentRepository.saveAndFlush(forumThreadComment);

                return new JSONObject().put(
                        "content",
                        servant.markdownToHtml(forumThreadComment.getContent())
                ).put(
                        "created",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(
                                Servant.toTaipeiZonedDateTime(
                                        forumThreadComment.getCreated()
                                ).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
                        )
                ).put(
                        "relief",
                        Objects.nonNull(commenter.getRelief()) ? commenter.getRelief().toString() : "false"
                ).put(
                        "identifier",
                        forumThreadComment.getIdentifier()
                );
        }

        public JSONObject editComment(Lover commenter, ForumThreadComment forumThreadComment, String content) {
                if (content.isBlank()) {
                        return new JavaScriptObjectNotation().
                                withResponse(false).
                                withReason("請填留言內容").toJSONObject();
                }
                if (!Objects.equals(commenter, forumThreadComment.getCommenter())) {
                        return new JavaScriptObjectNotation().
                                withResponse(false).
                                withReason("您不是留言的人唷~").toJSONObject();
                }

                forumThreadComment.setContent(content);
                forumThreadCommentRepository.saveAndFlush(forumThreadComment);

                return new JavaScriptObjectNotation().
                        withResponse(true).
                        withResult(
                                servant.markdownToHtml(forumThreadComment.getContent())
                        ).toJSONObject();
        }

        public JSONObject editThread(Lover author, ForumThread forumThread, String title, String markdown,
                ForumThreadTag[] hashTags, ForumThreadIllustration[] delIllustrations, Collection<MultipartFile> multipartFiles) {
                if (title.isBlank()) {
                        return new JavaScriptObjectNotation().
                                withResponse(false).
                                withReason("請填寫文章標題").toJSONObject();
                }
                if (markdown.isBlank()) {
                        return new JavaScriptObjectNotation().
                                withResponse(false).
                                withReason("請填寫文章內容").toJSONObject();
                }
                if (hashTags.length == 0) {
                        return new JavaScriptObjectNotation().
                                withResponse(false).
                                withReason("請選擇至少一種標籤").toJSONObject();
                }
                if (!Objects.equals(author, forumThread.getAuthor())) {
                        return new JavaScriptObjectNotation().
                                withResponse(false).
                                withReason("您不是作者唷~").toJSONObject();
                }
                final String threadIdentifier = forumThread.getIdentifier().toString();

                List<ForumThreadHashTag> forumThreadHashTags = forumThreadHashTagRepository.findByForumThread(forumThread);
                for (ForumThreadHashTag forumThreadHashTag : forumThreadHashTags) {
                        forumThreadHashTagRepository.delete(forumThreadHashTag);
                        forumThreadHashTagRepository.flush();
                }
                for (ForumThreadTag forumThreadTag : hashTags) {
                        forumThreadHashTagRepository.saveAndFlush(
                                new ForumThreadHashTag(
                                        forumThreadTag,
                                        forumThread
                                )
                        );
                }
                for (ForumThreadIllustration forumThreadIllustration : delIllustrations) {
                        forumThreadIllustrationRepository.delete(forumThreadIllustration);
                        forumThreadIllustrationRepository.flush();
                        AMAZON_S3.deleteObject(new DeleteObjectRequest(
                                String.format(
                                        "%s/forum/%s",
                                        BUCKET_NAME,
                                        threadIdentifier
                                ),
                                forumThreadIllustration.getIdentifier().toString()
                        ));
                }
                if (Objects.nonNull(multipartFiles)) {
                        for (MultipartFile multipartFile : multipartFiles) {
                                UUID identifier = UUID.randomUUID();
                                File file = new File(
                                        Servant.TEMPORARY_DIRECTORY,
                                        identifier.toString()
                                );

                                try {
                                        multipartFile.transferTo(file);

                                        PutObjectRequest putObjectRequest = new PutObjectRequest(
                                                AmazonWebServices.BUCKET_NAME,
                                                String.format(
                                                        "%s/%s/%s",
                                                        DIRECTORY,
                                                        threadIdentifier,
                                                        identifier.toString()
                                                ),
                                                file
                                        );
                                        ObjectMetadata objectMetadata = new ObjectMetadata();
                                        objectMetadata.setContentType(
                                                multipartFile.getContentType()
                                        );
                                        putObjectRequest.setMetadata(objectMetadata);
                                        AmazonWebServices.AMAZON_S3.putObject(
                                                putObjectRequest
                                        );

                                        forumThreadIllustrationRepository.saveAndFlush(
                                                new ForumThreadIllustration(
                                                        identifier,
                                                        forumThread
                                                )
                                        );
                                } catch (IOException | IllegalStateException exception) {
                                        LOGGER.info(
                                                "创建论坛绪插图时发生异常❗️",
                                                exception
                                        );
                                }

                                file.delete();
                        }
                }

                forumThread.setTitle(title);
                forumThread.setMarkdown(markdown);
                forumThreadRepository.saveAndFlush(forumThread);

                return new JavaScriptObjectNotation().
                        withResponse(true).
                        withResult(forumThread.getId()).toJSONObject();
        }
}
