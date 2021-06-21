package cn.demomaster.huan.quickdeveloplibrary.util.terminal;

public class DeviceModel {
    private String name;
    private String alias;

    public DeviceModel(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "name=" + name + ",alias=" + alias;
    }
}
