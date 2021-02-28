package org.octavi.team;

public class CoreTeamList {
    public String name;
    public String role;
    public String desc;
    public int picResId;

    public CoreTeamList(String name, String role, String desc, int picResId) {
        this.name = name;
        this.role = role;
        this.desc = desc;
        this.picResId = picResId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getDesc() {
        return desc;
    }

    public int getPicResId() {
        return picResId;
    }
}
