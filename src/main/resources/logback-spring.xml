<?xml version="1.0" encoding="UTF-8"?>
<configuration  debug="true">
	<include resource="org/springframework/boot/logging/logback/defaults.xml"/>

	<springProperty
		scope="context"
		name="datasourceDefaultAutoCommit"
		source="spring.datasource.tomcat.default-auto-commit"
	/>
	<springProperty
		scope="context"
		name="datasourceDriverClassName"
		source="spring.datasource.tomcat.driver-class-name"
	/>
	<springProperty
		scope="context"
		name="datasourceInitialSize"
		source="spring.datasource.tomcat.initial-size"
	/>
	<springProperty
		scope="context"
		name="datasourceMaxActive"
		source="spring.datasource.tomcat.max-active"
	/>
	<springProperty
		scope="context"
		name="datasourceMaxIdle"
		source="spring.datasource.tomcat.max-idle"
	/>
	<springProperty
		scope="context"
		name="datasourceMinIdle"
		source="spring.datasource.tomcat.min-idle"
	/>
	<springProperty
		scope="context"
		name="datasourcePassword"
		source="spring.datasource.password"
	/>
	<springProperty
		scope="context"
		name="datasourceType"
		source="spring.datasource.type"
		defaultValue="org.apache.tomcat.jdbc.pool.DataSource"
	/>
	<springProperty
		scope="context"
		name="datasourceUrl"
		source="spring.datasource.url"
	/>
	<springProperty
		scope="context"
		name="datasourceUsername"
		source="spring.datasource.username"
	/>

	<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>${CONSOLE_LOG_PATTERN}</pattern>
			<charset>utf8</charset>
		</encoder>
	</appender>

	<appender name="DATABASE" class="ch.qos.logback.classic.db.DBAppender">
		<connectionSource class="ch.qos.logback.core.db.DataSourceConnectionSource">
			<dataSource class="${datasourceType}">
				<param name="defaultAutoCommit" value="${datasourceDefaultAutoCommit}"/>
				<param name="defaultReadOnly" value="false"/>
				<param name="driverClassName" value="${datasourceDriverClassName}"/>
				<param name="initialSize" value="${datasourceInitialSize}"/>
				<param name="maxActive" value="${datasourceMaxActive}"/>
				<param name="maxIdle" value="${datasourceMaxIdle}"/>
				<param name="minIdle" value="${datasourceMinIdle}"/>
				<param name="password" value="${datasourcePassword}"/>
				<param name="url" value="${datasourceUrl}"/>
				<param name="username" value="${datasourceUsername}"/>
			</dataSource>
		</connectionSource>
	</appender>

	<root level="INFO">
		<appender-ref ref="CONSOLE"/>
		<appender-ref ref="DATABASE"/>
	</root>
</configuration>