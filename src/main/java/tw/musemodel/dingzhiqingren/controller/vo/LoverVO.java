package tw.musemodel.dingzhiqingren.controller.vo;

import tw.musemodel.dingzhiqingren.entity.Lover;
import tw.musemodel.dingzhiqingren.service.vo.CompanionshipWithInfo;

import java.util.ArrayList;

public class LoverVO extends Lover {

//    private ArrayList<CompanionshipWithInfo> services=new ArrayList<>();
//
//    public ArrayList<CompanionshipWithInfo> getServices() {
//        return services;
//    }
//
//    public void setServices(ArrayList<CompanionshipWithInfo> services) {
//        this.services = services;
//    }

    private ArrayList<Integer> serviceChecked=new ArrayList<>();
    private ArrayList<Integer> serviceId=new ArrayList<>();
    private ArrayList<Integer> hour=new ArrayList<>();
    private ArrayList<Integer> point=new ArrayList<>();


    public ArrayList<Integer> getServiceChecked() {
        return serviceChecked;
    }

    public void setServiceChecked(ArrayList<Integer> serviceChecked) {
        this.serviceChecked = serviceChecked;
    }

    public ArrayList<Integer> getServiceId() {
        return serviceId;
    }

    public void setServiceId(ArrayList<Integer> serviceId) {
        this.serviceId = serviceId;
    }

    public ArrayList<Integer> getHour() {
        return hour;
    }

    public void setHour(ArrayList<Integer> hour) {
        this.hour = hour;
    }

    public ArrayList<Integer> getPoint() {
        return point;
    }

    public void setPoint(ArrayList<Integer> point) {
        this.point = point;
    }
}
