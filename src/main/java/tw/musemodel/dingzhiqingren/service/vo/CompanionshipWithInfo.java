package tw.musemodel.dingzhiqingren.service.vo;

import tw.musemodel.dingzhiqingren.entity.Companionship;

public class CompanionshipWithInfo  {
    private int serviceId;
    private int hour;
    private int point;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
