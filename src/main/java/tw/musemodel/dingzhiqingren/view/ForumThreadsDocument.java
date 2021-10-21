package tw.musemodel.dingzhiqingren.view;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import lombok.Data;
import tw.musemodel.dingzhiqingren.entity.ForumThread;

/**
 * 论坛绪的索引
 *
 * @author p@musemodel.tw
 */
@Data
@JacksonXmlRootElement(localName = "document")
public class ForumThreadsDocument {

	private int elementsOfCurrentPage;

	private boolean first;

	private boolean last;

	private boolean next;

	private int numberOfCurrentPage;

	private boolean previous;

	private int sizeOfPage;

	/**
	 * 论坛绪
	 */
	private List<ForumThread> forumThreads;
}
