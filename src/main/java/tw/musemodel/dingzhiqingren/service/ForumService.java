package tw.musemodel.dingzhiqingren.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import tw.musemodel.dingzhiqingren.entity.ForumThread;
import tw.musemodel.dingzhiqingren.entity.ForumThreadHashTag;
import tw.musemodel.dingzhiqingren.entity.ForumThreadIllustration;
import tw.musemodel.dingzhiqingren.entity.ForumThreadTag;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.repository.ForumThreadHashTagRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadIllustrationRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadRepository;

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
        private ForumThreadIllustrationRepository forumThreadIllustrationRepository;

        @Autowired
        private ForumThreadRepository forumThreadRepository;

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
