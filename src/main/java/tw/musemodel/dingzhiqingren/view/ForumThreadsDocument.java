package tw.musemodel.dingzhiqingren.view;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import lombok.Data;
import tw.musemodel.dingzhiqingren.entity.ForumThread;

/**
 * 浏览论坛绪
 *
 * @author p@musemodel.tw
 */
@Data
@JacksonXmlRootElement(localName = "document")
public class ForumThreadsDocument {

	@JacksonXmlProperty(isAttribute = true)
	private int elementsOfCurrentPage;

	@JacksonXmlProperty(isAttribute = true)
	private boolean first;

	@JacksonXmlProperty(isAttribute = true)
	private boolean last;

	@JacksonXmlProperty(isAttribute = true)
	private boolean next;

	@JacksonXmlProperty(isAttribute = true)
	private int numberOfCurrentPage;

	@JacksonXmlProperty(isAttribute = true)
	private boolean previous;

	@JacksonXmlProperty(isAttribute = true)
	private int sizeOfPage;

	/**
	 * 论坛绪
	 */
	private List<ForumThread> forumThreads;
}
