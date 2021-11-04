package tw.musemodel.dingzhiqingren.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tw.musemodel.dingzhiqingren.entity.ForumThread;
import tw.musemodel.dingzhiqingren.entity.ForumThreadIllustration;
import tw.musemodel.dingzhiqingren.entity.ForumThreadTag;
import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.repository.ForumThreadCommentRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadRepository;
import tw.musemodel.dingzhiqingren.repository.ForumThreadTagRepository;
import tw.musemodel.dingzhiqingren.service.ForumService;
import tw.musemodel.dingzhiqingren.service.LoverService;
import tw.musemodel.dingzhiqingren.service.Servant;
import tw.musemodel.dingzhiqingren.view.ForumThreadDocument;
import tw.musemodel.dingzhiqingren.view.ForumThreadsDocument;

/**
 * 控制器：论坛
 *
 * @author p@musemodel.tw
 */
@Controller
@RequestMapping("/forum")
public class ForumController {

        private static final Logger LOGGER = LoggerFactory.getLogger(ForumController.class);

        @Autowired
        private ForumService forumService;

        @Autowired
        private LoverService loverService;

        @Autowired
        private Servant servant;

        @Autowired
        private MessageSource messageSource;

        @Autowired
        private ForumThreadTagRepository forumThreadTagRepository;

        @Autowired
        private ForumThreadRepository forumThreadRepository;

        @Autowired
        private ForumThreadCommentRepository forumThreadCommentRepository;

        /**
         * 論壇首頁
         *
         * @param p
         * @param s
         * @param authentication
         * @param locale
         * @return
         * @throws TransformerException
         * @throws IOException
         */
        @GetMapping(path = "/")
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        ModelAndView index(@RequestParam(defaultValue = "1") int p, @RequestParam(defaultValue = "8") int s, Authentication authentication, Locale locale) throws TransformerException, IOException {
                Lover me = loverService.loadByUsername(
                        authentication.getName()
                );

                if (!loverService.isEligible(me)) {
                        //补齐个人资料
                        return Servant.redirectToProfile();
                }

                List<ForumThread> list = forumService.readAllThreadsSortByPopular(
                        me.getGender(),
                        p < 1 ? 0 : p - 1,
                        s
                );

                Document document = forumService.forumToDocument(me, list);
                Element documentElement = servant.documentElement(
                        document,
                        authentication
                );

                documentElement.setAttribute(
                        "title",
                        messageSource.getMessage(
                                "title.forum",
                                null,
                                locale
                        )
                );//网页标题

                ModelAndView modelAndView = new ModelAndView("forum");
                modelAndView.getModelMap().addAttribute(document);
                return modelAndView;
        }

        @PostMapping(path = "/loadMore.json")
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        String loadMore(@RequestParam int p, @RequestParam(defaultValue = "8") int s, @RequestParam String sort,
                Authentication authentication, Locale locale) throws TransformerException, IOException {

                Lover me = loverService.loadByUsername(
                        authentication.getName()
                );

                List<ForumThread> list = new ArrayList<>();
                if (Objects.equals(sort, "popular")) {
                        list = forumService.readAllThreadsSortByPopular(
                                me.getGender(),
                                p,
                                s
                        );
                }

                if (Objects.equals(sort, "latest")) {
                        list = forumService.readAllThreadsSortByLatest(
                                me.getGender(),
                                p,
                                s
                        );
                }

                if (Objects.equals(sort, "mine")) {
                        list = forumThreadRepository.findByAuthorOrderByCreatedDesc(
                                me,
                                PageRequest.of(p, s)
                        ).getContent();
                }

                return forumService.loadMoreForumThread(
                        me,
                        list
                ).toString();
        }

        @GetMapping(path = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        void index(@RequestParam(defaultValue = "1") int p, @RequestParam(defaultValue = "8") int s, HttpServletResponse response, Authentication authentication) throws TransformerException, IOException {
                Lover me = loverService.loadByUsername(
                        authentication.getName()
                );

                List<ForumThread> list = forumService.readAllThreadsSortByPopular(
                        me.getGender(),
                        p < 1 ? 0 : p - 1,
                        s
                );

                ForumThreadsDocument forumThreadsDocument = new ForumThreadsDocument();
//                forumThreadsDocument.setElementsOfCurrentPage(
//                        pagination.getNumberOfElements()
//                );
//                forumThreadsDocument.setFirst(
//                        pagination.isFirst()
//                );
//                forumThreadsDocument.setLast(
//                        pagination.isLast()
//                );
//                forumThreadsDocument.setNext(
//                        pagination.hasNext()
//                );
//                forumThreadsDocument.setNumberOfCurrentPage(
//                        pagination.getNumber()
//                );
//                forumThreadsDocument.setPrevious(
//                        pagination.hasPrevious()
//                );
//                forumThreadsDocument.setSizeOfPage(
//                        pagination.getSize()
//                );
//                forumThreadsDocument.setForumThreads(
//                        pagination.getContent()
//                );
                forumThreadsDocument.setForumThreadTags(
                        forumThreadTagRepository.findAll()
                );

                Servant.newTransformer().transform(
                        new DOMSource(Servant.parseDocument(
                                forumThreadsDocument
                        )),
                        new StreamResult(response.getOutputStream())
                );
        }

        @GetMapping(path = "/add.asp", produces = MediaType.APPLICATION_XML_VALUE)
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        void add(HttpServletResponse response, Authentication authentication) throws TransformerException, IOException {
                ForumThreadDocument forumThreadDocument = new ForumThreadDocument();
                forumThreadDocument.setForumThread(
                        new ForumThread()
                );

                Servant.newTransformer().transform(
                        new DOMSource(Servant.parseDocument(
                                forumThreadDocument
                        )),
                        new StreamResult(response.getOutputStream())
                );
        }

        /**
         * 发布讨论。
         *
         * @param title 标题
         * @param markdown 内容
         * @param multipartFiles 插图
         * @param authentication 认证
         * @return 杰森格式对象
         */
        @PostMapping(path = "/add.asp", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        String add(@RequestParam String title, @RequestParam String markdown, @RequestParam ForumThreadTag[] hashTags,
                @RequestParam(name = "illustrations", required = false) Collection<MultipartFile> multipartFiles, Authentication authentication) {
                return forumService.createThreadWithIllustrations(
                        loverService.loadByUsername(
                                authentication.getName()
                        ),
                        title,
                        markdown,
                        hashTags,
                        multipartFiles
                ).toString();
        }

        @GetMapping(path = "/{identifier:^[0-9a-z]{8}-([0-9a-z]{4}-){3}[0-9a-z]{12}$}.asp", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        ForumThread getOne(@PathVariable UUID identifier) {
                return forumService.readOneThread(identifier);
        }

        @GetMapping(path = "/{identifier}.json", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        String getOneThread(@PathVariable UUID identifier) {
                return forumService.readOneThreadInJson(identifier).toString();
        }

        @PostMapping(path = "/comment.asp", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        String comment(@RequestParam String forumThread, @RequestParam String content, Authentication authentication) {
                return forumService.comment(
                        forumThreadRepository.findOneByIdentifier(
                                UUID.fromString(forumThread)
                        ),
                        loverService.loadByUsername(
                                authentication.getName()
                        ),
                        content
                ).toString();
        }

        @GetMapping(path = "/comment/{identifier}.json", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        String getOneComment(@PathVariable UUID identifier) {
                return forumThreadCommentRepository.findByIdentifier(identifier).toString();
        }

        @PostMapping(path = "/editComment.asp", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        String editComment(@RequestParam UUID forumCommentIdentifier, @RequestParam String content, Authentication authentication) {

                return forumService.editComment(
                        loverService.loadByUsername(
                                authentication.getName()
                        ),
                        forumThreadCommentRepository.findByIdentifier(
                                forumCommentIdentifier
                        ),
                        content
                ).toString();
        }

        @PostMapping(path = "/editThread.asp", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        @Secured({Servant.ROLE_ADVENTURER})
        String editThread(@RequestParam UUID forumIdentifier, @RequestParam String title,
                @RequestParam String markdown, @RequestParam ForumThreadTag[] hashTags, @RequestParam ForumThreadIllustration[] delIllustrations,
                @RequestParam(name = "illustrations", required = false) Collection<MultipartFile> multipartFiles, Authentication authentication) {

                return forumService.editThread(
                        loverService.loadByUsername(
                                authentication.getName()
                        ),
                        forumThreadRepository.findOneByIdentifier(
                                forumIdentifier
                        ),
                        title,
                        markdown,
                        hashTags,
                        delIllustrations,
                        multipartFiles
                ).toString();
        }
}
