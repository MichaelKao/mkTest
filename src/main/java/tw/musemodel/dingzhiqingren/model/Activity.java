package tw.musemodel.dingzhiqingren.model;

import java.util.Date;
import tw.musemodel.dingzhiqingren.entity.History.Behavior;
import tw.musemodel.dingzhiqingren.entity.Lover;

/**
 * 动态记录
 *
 * @author m@musemodel.tw
 */
public class Activity implements Comparable<Activity> {

        private Lover initiative;

        private Lover passive;

        private Behavior behavior;

        private Date occurred;

        private String occurredStr;

        private Short points;

        private String greeting;

        private Date seen;

        private Date reply;

        // 放行生活照
        private Boolean showAllPictures;

        // 用於聊天室：紀錄寄送人
        private String sender;

        // 用於聊天室：要求車馬費的歷程 id
        private Long id;

        // 用於聊天室：可否退回
        private Boolean ableToReturnFare;

        @Override
        public int compareTo(Activity other) {
                return occurred.compareTo(other.occurred);
        }

        public Activity() {
        }

        /**
         * 通知歷程
         *
         * @param id
         * @param initiative
         * @param passive
         * @param behavior
         * @param occurred
         * @param points
         * @param greeting
         * @param seen
         * @param reply
         */
        public Activity(Long id, Lover initiative, Lover passive, Behavior behavior,
                Date occurred, Short points, String greeting, Date seen, Date reply) {
                this.id = id;
                this.initiative = initiative;
                this.passive = passive;
                this.behavior = behavior;
                this.occurred = occurred;
                this.points = points;
                this.greeting = greeting;
                this.seen = seen;
                this.reply = reply;
        }

        /**
         * 聊天歷史紀錄
         *
         * @param sender
         * @param occurred
         * @param greeting
         * @param seen
         */
        public Activity(String sender, Behavior behavior, Date occurred, String occurredStr, String greeting, Date seen) {
                this.sender = sender;
                this.behavior = behavior;
                this.occurred = occurred;
                this.occurredStr = occurredStr;
                this.greeting = greeting;
                this.seen = seen;
        }

        /**
         * 群發 GroupBy
         *
         * @param initiative
         * @param behavior
         * @param occurred
         */
        public Activity(Lover initiative, Behavior behavior, Date occurred) {
                this.initiative = initiative;
                this.behavior = behavior;
                this.occurred = occurred;
        }

        public Lover getInitiative() {
                return initiative;
        }

        public void setInitiative(Lover initiative) {
                this.initiative = initiative;
        }

        public Lover getPassive() {
                return passive;
        }

        public void setPassive(Lover passive) {
                this.passive = passive;
        }

        public Behavior getBehavior() {
                return behavior;
        }

        public void setBehavior(Behavior behavior) {
                this.behavior = behavior;
        }

        public Date getOccurred() {
                return occurred;
        }

        public void setOccurred(Date occurred) {
                this.occurred = occurred;
        }

        public Short getPoints() {
                return points;
        }

        public void setPoints(Short points) {
                this.points = points;
        }

        public String getGreeting() {
                return greeting;
        }

        public void setGreeting(String greeting) {
                this.greeting = greeting;
        }

        public Date getSeen() {
                return seen;
        }

        public void setSeen(Date seen) {
                this.seen = seen;
        }

        public Date getReply() {
                return reply;
        }

        public void setReply(Date reply) {
                this.reply = reply;
        }

        public Boolean getShowAllPictures() {
                return showAllPictures;
        }

        public void setShowAllPictures(Boolean showAllPictures) {
                this.showAllPictures = showAllPictures;
        }

        public String getSender() {
                return sender;
        }

        public void setSender(String sender) {
                this.sender = sender;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Boolean getAbleToReturnFare() {
                return ableToReturnFare;
        }

        public void setAbleToReturnFare(Boolean ableToReturnFare) {
                this.ableToReturnFare = ableToReturnFare;
        }
}
