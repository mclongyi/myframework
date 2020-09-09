package com.odianyun.search.whale.data.model.hotword;

/**
 * @author hs
 * @date 2018/9/15.
 */
public enum WordStatus {

    UNKNOWN(0, "未知"),
    FRONT_HOTWORD(1, "被选中前台展示的热词"),
    HOTWORD(2, "热词"),
    NOT_HOTWORD(3, "不是热词");

    WordStatus(Integer code, String content) {
        this.code = code;
        this.content = content;
    }

    Integer code;
    String content;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WordStatus{" +
                "code=" + code +
                ", content='" + content + '\'' +
                '}';
    }
}
