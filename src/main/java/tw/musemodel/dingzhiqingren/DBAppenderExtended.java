package tw.musemodel.dingzhiqingren;

import ch.qos.logback.access.db.DBAppender;
import ch.qos.logback.access.spi.IAccessEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Enumeration;

/**
 * @author p@musemodel.tw
 */
public final class DBAppenderExtended extends DBAppender {

	private static final String ACCESS_EVENT_SQL = "INSERT INTO access_event(epoch,request_uri,request_url,remote_host,remote_user,remote_addr,protocol,method,server_name,post_content)VALUES(?,?,?,?,?,?,?,?,?,?)";

	private static final String ACCESS_EVENT_HEADER_SQL = "INSERT INTO access_event_header(event_id,header_key,header_value)VALUES(?,?,?)";

	@Override
	protected void subAppend(IAccessEvent accessEvent, Connection connection, PreparedStatement preparedStatement) throws Throwable {
		preparedStatement.setLong(1, accessEvent.getTimeStamp());
		preparedStatement.setString(2, accessEvent.getRequestURI());
		preparedStatement.setString(3, accessEvent.getRequestURL());
		preparedStatement.setString(4, accessEvent.getRemoteHost());
		preparedStatement.setString(5, accessEvent.getRemoteUser());
		preparedStatement.setString(6, accessEvent.getRemoteAddr());
		preparedStatement.setString(7, accessEvent.getProtocol());
		preparedStatement.setString(8, accessEvent.getMethod());
		preparedStatement.setString(9, accessEvent.getServerName());
		preparedStatement.setString(10, accessEvent.getRequestContent());

		if (preparedStatement.executeUpdate() != 1) {
			addWarn("插入访问事件失败。");
		}
	}

	@Override
	@SuppressWarnings("ConvertToTryWithResources")
	protected void secondarySubAppend(IAccessEvent event, Connection connection, long eventId) throws Throwable {
		Enumeration names = event.getRequestHeaderNames();
		if (names.hasMoreElements()) {
			PreparedStatement preparedStatement = connection.prepareStatement(ACCESS_EVENT_HEADER_SQL);

			while (names.hasMoreElements()) {
				String key = (String) names.nextElement();
				String value = (String) event.getRequestHeader(key);

				preparedStatement.setLong(1, eventId);
				preparedStatement.setString(2, key);
				preparedStatement.setString(3, value);

				if (cnxSupportsBatchUpdates) {
					preparedStatement.addBatch();
				} else {
					preparedStatement.execute();
				}
			}

			if (cnxSupportsBatchUpdates) {
				preparedStatement.executeBatch();
			}

			preparedStatement.close();
		}
	}

	@Override
	protected String getInsertSQL() {
		return ACCESS_EVENT_SQL;
	}
}
