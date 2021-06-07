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
			LOGGER.debug(
				String.format(
					"%s.URIResolverImpl(\n);//默认构造函数。",
					getClass().getName()
				)
			);

			this.prefix = "classpath:/templates/";
		}

		public URIResolverImpl(ServletContext servletContext) {
			LOGGER.debug(
				String.format(
					"%s.URIResolverImpl(\n\tServletContext = {}\n);//默认构造函数。",
					getClass().getName()
				),
				servletContext
			);
			this.servletContext = servletContext;
			this.prefix = "classpath:/templates/";
		}

		/**
		 * @param href 相对路径
		 * @param base 基底路径
		 * @return 流源
		 */
		@Override
		@SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UnusedAssignment", "null"})
		public Source resolve(String href, String base) {
			LOGGER.debug(
				String.format(
					"%s.resolve(\n\tString = \"{}\",\n\tString = \"{}\"\n);",
					getClass().getName()
				),
				href,
				base
			);

			StreamSource source = null;
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
				LOGGER.debug(
					getClass().getName() + ".resolve(\n\t\"{}\",\n\t\"{}\"\n);//IOException",
					href,
					base,
					ioException
				);
			}

			LOGGER.debug(
				String.format(
					"%s.resolve(\n\tString = \"{}\",\n\tString = \"{}\"\n);\nSource = {}",
					getClass().getName()
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
					"%s.getPrefix(\n);",
					getClass().getName()
				)
			);
			return prefix;
		}

		/**
		 * @param prefix 基底路径
		 */
		public void setPrefix(String prefix) {
			LOGGER.debug(
				String.format(
					"%s.setPrefix(\n\tString = \"{}\"\n);",
					getClass().getName()
				)
			);
			this.prefix = prefix;
		}
	}

	@Bean
	public ViewResolver viewResolver() {
		LOGGER.debug(
			String.format(
				"%s.viewResolver();//设置在转换中使用的 URIResolver，它可以处理对 XSLT document() 函数的调用。",
				getClass().getName()
			)
		);

		final String PREFIX = "classpath:/templates/";
		XsltViewResolver xsltViewResolver = new XsltViewResolver();
		xsltViewResolver.setIndent(false);
		xsltViewResolver.setPrefix(PREFIX);
		xsltViewResolver.setSuffix(".xsl");
		xsltViewResolver.setUriResolver(new URIResolverImpl(servletContext));
		return xsltViewResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers) {
		LOGGER.debug(
			String.format(
				"%s.addArgumentResolvers(\n\tList<HandlerMethodArgumentResolver> = {}\n);//添加解析器以支持自定义控制器方法参数类型。",
				getClass().getName()
			),
			handlerMethodArgumentResolvers
		);
		handlerMethodArgumentResolvers.add(
			deviceHandlerMethodArgumentResolver()
		);
		//handlerMethodArgumentResolvers.add(
		//	new LuJieChanShengDingDanHandlerMethodArgumentResolver()
		//);
		//handlerMethodArgumentResolvers.add(
		//	new LuJieFuKuanJieGuoHandlerMethodArgumentResolver()
		//);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
		LOGGER.debug(
			String.format(
				"%s.addResourceHandlers(\n\tResourceHandlerRegistry = {}\n);//服务静态资源。",
				getClass().getName()
			),
			resourceHandlerRegistry
		);
		resourceHandlerRegistry.
			addResourceHandler("/**").
			addResourceLocations("classpath:/static/");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {
		LOGGER.debug(
			String.format(
				"%s.configureDefaultServletHandling(\n\tDefaultServletHandlerConfigurer = {}\n);//启用转发到 \"default\" Servlet的功能。",
				getClass().getName()
			),
			defaultServletHandlerConfigurer
		);
		defaultServletHandlerConfigurer.enable();
	}

	@Bean
	public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
		LOGGER.debug(
			String.format(
				"%s.deviceResolverHandlerInterceptor(\n);//一个 Spring MVC 拦截器，它在调用任何请求处理程序之前解析发起 Web 请求的设备。",
				getClass().getName()
			)
		);
		return new DeviceResolverHandlerInterceptor();
	}

	@Bean
	public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
		LOGGER.debug(
			String.format(
				"%s.deviceHandlerMethodArgumentResolver(\n);//Spring MVC HandlerMethodArgumentResolver 将类型为 Device 的 @Controller MethodParameters 解析为 Web 请求的当前设备属性的值。",
				getClass().getName()
			)
		);
		return new DeviceHandlerMethodArgumentResolver();
	}

	@Override
	public void addInterceptors(InterceptorRegistry interceptorRegistry) {
		LOGGER.debug(
			String.format(
				"%s.addInterceptors(\n\tInterceptorRegistry = {}\n);//添加 Spring MVC 生命周期拦截器，用于控制器方法调用和资源处理程序请求的预处理和后处理；拦截器可以注册以应用于所有请求或仅限于 URL 模式的子集。",
				getClass().getName()
			),
			interceptorRegistry
		);
		interceptorRegistry.addInterceptor(deviceResolverHandlerInterceptor());
	}
}
