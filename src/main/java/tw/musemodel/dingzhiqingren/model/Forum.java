package tw.musemodel.dingzhiqingren.model;

import java.util.Date;
import lombok.Data;

/**
 * 論壇
 *
 * @author m@musemodel.tw
 */
@Data
public class Forum implements Comparable<Forum> {

        private static final long serialVersionUID = -8397689167558711132L;

        private Long id;

        private Date created;

        @Override
        public int compareTo(Forum other) {
                return created.compareTo(other.created);
        }

        public Forum() {
        }

        public Forum(Long id, Date created) {
                this.id = id;
                this.created = created;
        }
}
