package tw.musemodel.dingzhiqingren.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tw.musemodel.dingzhiqingren.entity.ForumThread;
import tw.musemodel.dingzhiqingren.repository.ForumThreadRepository;

/**
 * 服务层：论坛
 *
 * @author p@musemodel.tw
 */
@Service
public class ForumService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForumService.class);

	private static final String READ_THREADS_ACTIVELY;

	static {
		READ_THREADS_ACTIVELY = new StringBuilder().
			append("SELECT lun_tan.id").append("\n").
			append("FROM lun_tan").append("\n").
			append("LEFT JOIN qing_ren ON qing_ren.id=lun_tan.zuo_zhe").append("\n").
			append("LEFT JOIN lun_tan_liu_yan ON lun_tan_liu_yan.lun_tan=lun_tan.id").append("\n").
			append("WHERE qing_ren.xing_bie=?").append("\n").
			append("GROUP BY lun_tan.id").append("\n").
			append("ORDER BY min(lun_tan_liu_yan.shi_chuo)").
			toString();
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ForumThreadRepository forumThreadRepository;

	public Page<ForumThread> getAll(boolean gender, int page, int size) {
		return forumThreadRepository.findByIdIn(
			jdbcTemplate.query(
				(Connection connection) -> {
					PreparedStatement preparedStatement = connection.prepareStatement(
						READ_THREADS_ACTIVELY
					);
					preparedStatement.setBoolean(1, gender);
					return preparedStatement;
				},
				(ResultSet resultSet, int rowNum) -> {
					return resultSet.getLong(1);
				}
			),
			PageRequest.of(page, size)
		);
	}

	public ForumThread getOneThread(UUID identifier) {
		return forumThreadRepository.findOneByIdentifier(identifier);
	}
}
