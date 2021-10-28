package tw.musemodel.dingzhiqingren.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import tw.musemodel.dingzhiqingren.repository.ForumThreadCommentRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadHashTagRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadIllustrationRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadTagRepository;

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

        @Value("classpath:sql/男女论坛.sql")
        private Resource forumThreadsResource;

        /**
         * 创建论坛绪及插图。
         *
         * @param author 作者
         * @param title 标题
         * @param markdown 内容
         * @param multipartFiles 插图
         * @return 论坛绪
         */
        public ForumThread createThreadWithIllustrations(Lover author, String title, String markdown, ForumThreadTag[] hashTags, Collection<MultipartFile> multipartFiles) {
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

                return forumThread;
        }

        /**
         * 浏览(索引)并分页论坛绪。
         *
         * @param gender 性别
         * @param page 第几页
         * @param size 每页几笔
         * @return 分页响应
         */
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        public Page<ForumThread> readAllThreads(boolean gender, int page, int size) {
                List<Long> threadIds = jdbcTemplate.query(
                        (Connection connection) -> {
                                PreparedStatement preparedStatement;
                                try {
                                        preparedStatement = connection.prepareStatement(
                                                FileCopyUtils.copyToString(
                                                        new InputStreamReader(
                                                                forumThreadsResource.getInputStream(),
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
                                return resultSet.getLong(1);
                        }
                );

                List<ForumThread> forumThreads = new ArrayList<>();
                for (Long id : threadIds) {
                        forumThreads.add(
                                forumThreadRepository.findById(id).get()
                        );
                }

                return forumThreadRepository.findByIdIn(
                        threadIds,
                        PageRequest.of(page, size)
                );
        }

        public Document forumToDocument(Lover self, Page<ForumThread> pagination) throws IOException {

                Document document = Servant.parseDocument();
                Element documentElement = document.getDocumentElement();

                // 本人
                Element selfElement = document.createElement("self");
                documentElement.appendChild(selfElement);
                selfElement.setAttribute("nickname", self.getNickname());
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

                // 分頁 pagination
                Element paginationElement = document.createElement("pagination");
                documentElement.appendChild(paginationElement);
                paginationElement.setAttribute(
                        "first",
                        Boolean.toString(pagination.isFirst())
                );
                paginationElement.setAttribute(
                        "last",
                        Boolean.toString(pagination.isLast())
                );
                if (pagination.hasNext()) {
                        paginationElement.setAttribute(
                                "next",
                                Integer.toString(pagination.nextOrLastPageable().getPageNumber())
                        );
                }
                if (pagination.hasPrevious()) {
                        paginationElement.setAttribute(
                                "prev",
                                Integer.toString(pagination.previousOrFirstPageable().getPageNumber())
                        );
                }
                paginationElement.setAttribute(
                        "numberOfCurrentPage",
                        Integer.toString(pagination.getNumber())
                );
                paginationElement.setAttribute(
                        "sizeOfPage",
                        Integer.toString(pagination.getSize())
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
                for (ForumThread forumThread : pagination.getContent()) {
                        Element forumThreadElement = document.createElement("forumThread");
                        forumThreadsElement.appendChild(forumThreadElement);

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

                        for (ForumThreadHashTag forumThreadHashTag : forumThread.getForumHashTags()) {
                                Element hashTagElement = document.createElement("hashTag");
                                hashTagElement.setTextContent(forumThreadHashTag.getForumThreadTag().getPhrase());
                                forumThreadElement.appendChild(hashTagElement);
                        }
                        for (ForumThreadIllustration forumThreadIllustration : forumThread.getForumThreadIllustrations()) {
                                Element illustrationElement = document.createElement("illustration");
                                illustrationElement.setTextContent(
                                        String.format(
                                                "https://%s/forum/%s/%s.jpg",
                                                Servant.STATIC_HOST,
                                                forumThread.getIdentifier(),
                                                forumThreadIllustration.getIdentifier()
                                        ));
                                forumThreadElement.appendChild(illustrationElement);
                        }
                        // 留言
                        for (ForumThreadComment forumThreadComment : forumThread.getForumThreadComments()) {
                                Element commentElement = document.createElement("comment");
                                forumThreadElement.appendChild(commentElement);
                                commentElement.setAttribute(
                                        "identifier",
                                        forumThreadComment.getIdentifier().toString()
                                );
                                commentElement.setAttribute(
                                        "content",
                                        forumThreadComment.getIdentifier().toString()
                                );
                                commentElement.setAttribute(
                                        "content",
                                        forumThreadComment.getIdentifier().toString()
                                );
                                commentElement.setAttribute(
                                        "date",
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(
                                                Servant.toTaipeiZonedDateTime(
                                                        forumThreadComment.getCreated()
                                                ).withZoneSameInstant(Servant.ASIA_TAIPEI_ZONE_ID)
                                        )
                                );

                                // 留言方
                                Lover commenter = forumThreadComment.getCommenter();
                                Element commenterElement = document.createElement("commenter");
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
                                commentElement.appendChild(commenterElement);

                        }
                }

                return document;
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
}
