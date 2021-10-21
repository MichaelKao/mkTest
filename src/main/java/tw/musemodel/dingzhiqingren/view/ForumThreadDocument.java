package tw.musemodel.dingzhiqingren.view;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import tw.musemodel.dingzhiqingren.entity.ForumThread;

/**
 * 论坛绪的创建、读取及更新
 * @author p@musemodel.tw
 */
@Data
@JacksonXmlRootElement(localName = "document")
public class ForumThreadDocument {

	/**
	 * 论坛绪
	 */
	private ForumThread forumThread;
}
