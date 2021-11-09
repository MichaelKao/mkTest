package tw.musemodel.dingzhiqingren.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;

/**
 * 會員
 *
 * @author m@musemodel.tw
 */
@Data
public class Member implements Serializable {

        private static final long serialVersionUID = 5416297105764712727L;

        /**
         * 男士(或甜心)的ID
         */
        private Integer id;

        /**
         * 男士(或甜心)的识别码
         */
        private UUID identifier;

        /**
         * 昵称
         */
        private String nickname;

        /**
         * 昵称
         */
        private String login;

        /**
         * 贵宾到期日
         */
        private String vipExpiration;

        /**
         * 注册时戳
         */
        private String registered;

        /**
         * 是否為短期貴賓
         */
        private boolean isVIP;

        /**
         * 是否為長期貴賓
         */
        private boolean isVVIP;

        /**
         * 是否為體驗貴賓
         */
        private boolean isTrial;

        /**
         * 體驗碼
         */
        private String trialCode;

        public Member() {
                isVIP = Boolean.FALSE;
                isVVIP = Boolean.FALSE;
                isTrial = Boolean.FALSE;
        }

        /**
         * 构造器
         *
         * @param id 男士(或甜心)的id
         * @param identifier 男士(或甜心)的识别码
         * @param nickname 昵称
         * @param login 註冊時間
         * @param vipExpiration 贵宾到期日
         * @param registered 注册时戳(未来会改为最后付费成为贵宾的时戳)
         */
        public Member(Integer id, UUID identifier, String nickname, String login, String vipExpiration, String registered) {
                this();
                this.id = id;
                this.identifier = identifier;
                this.nickname = nickname;
                this.login = login;
                this.vipExpiration = vipExpiration;
                this.registered = registered;
        }

        @Override
        public String toString() {
                try {
                        return new JsonMapper().writeValueAsString(this);
                } catch (JsonProcessingException ignore) {
                        return Objects.isNull(identifier) ? "null" : identifier.toString();
                }
        }

        public boolean isIsVIP() {
                return isVIP;
        }

        public void setIsVIP(boolean isVIP) {
                this.isVIP = isVIP;
        }

        public boolean isIsVVIP() {
                return isVVIP;
        }

        public void setIsVVIP(boolean isVVIP) {
                this.isVVIP = isVVIP;
        }

        public boolean isIsTrial() {
                return isTrial;
        }

        public void setIsTrial(boolean isTrial) {
                this.isTrial = isTrial;
        }

        public String getTrialCode() {
                return trialCode;
        }

        public void setTrialCode(String trialCode) {
                this.trialCode = trialCode;
        }
}
