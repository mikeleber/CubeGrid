package ch.m1m.config.api;

public class Config {

    private String applId;
    private String key;
    private String value;
    private String type;
    private String description;

    public Config() {}

    public Config(String applId, String key, String value, String type) {
        this.applId = applId;
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public Config(String applId, String key, String value, String type, String description) {
        this.applId = applId;
        this.key = key;
        this.value = value;
        this.type = type;
        this.description = description;
    }

    public String getApplId() {
        return applId;
    }

    public void setApplId(String applId) {
        this.applId = applId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
