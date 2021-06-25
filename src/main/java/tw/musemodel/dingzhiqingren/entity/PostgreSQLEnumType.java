package tw.musemodel.dingzhiqingren.entity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

/**
 * @author m@musemodel.tw
 */
public class PostgreSQLEnumType extends EnumType {

	private static final long serialVersionUID = 1097952200271396339L;

	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
		if (Objects.isNull(value)) {
			preparedStatement.setNull(index, Types.OTHER);
		} else {
			preparedStatement.setObject(
				index,
				value.toString(),
				Types.OTHER
			);
		}
	}
}
