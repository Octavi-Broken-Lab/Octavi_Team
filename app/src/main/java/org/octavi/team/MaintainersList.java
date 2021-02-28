package org.octavi.team;

public class MaintainersList {
    public String name;
    public String deviceName;
    public int pic;

    public MaintainersList(String name, int pic, String deviceName) {
        this.name = name;
        this.deviceName = deviceName;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getPic() {
        return pic;
    }
}
