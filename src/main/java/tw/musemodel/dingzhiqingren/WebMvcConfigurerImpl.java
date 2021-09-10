package tw.musemodel.dingzhiqingren;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import javax.servlet.ServletContext;
import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;

/**
 * @author p@musemodel.tw
 */
@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebMvcConfigurerImpl.class);

	/**
	 * 从中提取档案的 Servlet Context
	 */
	@Autowired
	private ServletContext servletContext;

	private static class URIResolverImpl implements URIResolver {

		private static final Logger LOGGER = LoggerFactory.getLogger(URIResolverImpl.class);

		/**
		 * 从中提取档案的 Servlet Context
		 */
		private ServletContext servletContext;

		/**
		 * 查找档案的默认路径
		 */
		private String prefix;

		public URIResolverImpl() {
			prefix = "classpath:/templates/";
			LOGGER.debug(
				String.format(
					new StringBuilder("默认构造器%n").
						append("%s();%n").
						append("%s = {}%n").
						append("%s = {}").
						toString(),
					getClass(),
					ServletContext.class,
					String.class
				),
				servletContext,
				prefix
			);
		}

		public URIResolverImpl(ServletContext servletContext) {
			this.servletContext = servletContext;
			this.prefix = "classpath:/templates/";
			LOGGER.debug(
				String.format(
					new StringBuilder("构造器%n").
						append("%s.URIResolverImpl(%n").
						append("\t%s = {}%n").
						append(");%n").
						append("%s = {}%n").
						append("%s = {}").
						toString(),
					getClass(),
					ServletContext.class,
					ServletContext.class,
					String.class
				),
				servletContext,
				this.servletContext,
				this.prefix
			);
		}

		/**
		 * @param href 相对路径
		 * @param base 基底路径
		 * @return 流源
		 */
		@Override
		@SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UnusedAssignment", "null"})
		public Source resolve(String href, String base) {
			Source source = null;
			InputStream inputStream = null;
			ServletContextResource servletContextResource = null;
			try {
				servletContextResource = new ServletContextResource(
					servletContext,
					prefix.concat(href)
				);
				inputStream = servletContextResource.getInputStream();
				source = new StreamSource(inputStream);

				if (Objects.nonNull(inputStream)) {
					inputStream.close();
				}
			} catch (IOException ioException) {
			}

			LOGGER.debug(
				String.format(
					new StringBuilder("处理器在遇到 xsl:include、xsl:import 或 document() 时调用%n").
						append("%s.resolve(%n").
						append("\t%s = {},%n").
						append("\t%s = {}%n").
						append(");%n").
						append("%s = {}").
						toString(),
					getClass().getName(),
					String.class,
					String.class,
					Source.class
				),
				href,
				base,
				source
			);
			return source;
		}

		/**
		 * @return 基底路径
		 */
		public String getPrefix() {
			LOGGER.debug(
				String.format(
					new StringBuilder("基底路径%n").
						append("%s.getPrefix();%n").
						append("%s = {}").
						toString(),
					getClass(),
					String.class
				),
				prefix
			);
			return prefix;
		}

		/**
		 * @param prefix 基底路径
		 */
		public void setPrefix(String prefix) {
			this.prefix = prefix;
			LOGGER.debug(
				String.format(
					new StringBuilder("基底路径%n").
						append("%s.setPrefix(%n").
						append("\t%s = {}%n").
						append(");%n").
						append("%s = {}").
						toString(),
					getClass(),
					String.class,
					String.class
				),
				prefix,
				this.prefix
			);
		}
	}

	@Bean
	public ViewResolver viewResolver() {
		final String PREFIX = "classpath:/templates/";
		XsltViewResolver xsltViewResolver = new XsltViewResolver();
		xsltViewResolver.setIndent(false);
		xsltViewResolver.setPrefix(PREFIX);
		xsltViewResolver.setSuffix(".xsl");
		xsltViewResolver.setUriResolver(new URIResolverImpl(servletContext));
		LOGGER.debug(
			String.format(
				new StringBuilder("设置在转换中使用的 URIResolver，它可以处理对 XSLT document() 函数的调用%n").
					append("%s.viewResolver();%n").
					append("%s = {}").
					toString(),
				getClass(),
				XsltViewResolver.class
			),
			xsltViewResolver
		);
		return xsltViewResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers) {
		handlerMethodArgumentResolvers.add(
			deviceHandlerMethodArgumentResolver()
		);
		LOGGER.debug(
			String.format(
				new StringBuilder("添加解析器以支持自定义控制器方法参数类型%n").
					append("%s.addArgumentResolvers(%n").
					append("\t%s = {}%n").
					append(");").
					toString(),
				getClass(),
				List.class
			),
			handlerMethodArgumentResolvers
		);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
		resourceHandlerRegistry.
			addResourceHandler("/**").
			addResourceLocations("classpath:/static/");
		LOGGER.debug(
			String.format(
				new StringBuilder("服务静态资源%n").
					append("%s.addResourceHandlers(%n").
					append("\t%s = {}%n").
					append(");").
					toString(),
				getClass(),
				ResourceHandlerRegistry.class
			),
			resourceHandlerRegistry
		);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {
		defaultServletHandlerConfigurer.enable();
		LOGGER.debug(
			String.format(
				new StringBuilder("启用转发到 \"default\" Servlet 的功能%n").
					append("%s.configureDefaultServletHandling(%n").
					append("\t%s = {}%n").
					append(");").
					toString(),
				getClass(),
				DefaultServletHandlerConfigurer.class
			),
			defaultServletHandlerConfigurer
		);
	}

	@Bean
	public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
		LOGGER.debug(String.format(
			new StringBuilder("一个 Spring MVC 拦截器，它在调用任何请求处理程序之前解析发起 Web 请求的设备%n").
				append("%s.deviceResolverHandlerInterceptor();").
				toString(),
			getClass()
		));
		return new DeviceResolverHandlerInterceptor();
	}

	@Bean
	public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
		LOGGER.debug(String.format(
			new StringBuilder("Spring MVC HandlerMethodArgumentResolver 将类型为 Device 的 @Controller MethodParameters 解析为 Web 请求的当前设备属性的值%n").
				append("%s.deviceHandlerMethodArgumentResolver();").
				toString(),
			getClass()
		));
		return new DeviceHandlerMethodArgumentResolver();
	}

	@Override
	public void addInterceptors(InterceptorRegistry interceptorRegistry) {
		interceptorRegistry.addInterceptor(deviceResolverHandlerInterceptor());
		LOGGER.debug(
			String.format(
				new StringBuilder("添加 Spring MVC 生命周期拦截器，用于控制器方法调用和资源处理程序请求的预处理和后处理；拦截器可以注册以应用于所有请求或仅限于 URL 模式的子集%n").
					append("%s.addInterceptors(%n").
					append("\t%s = {}%n").
					append(");").
					toString(),
				getClass(),
				InterceptorRegistry.class
			),
			interceptorRegistry
		);
	}
}
