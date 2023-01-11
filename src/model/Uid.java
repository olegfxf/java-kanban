package model;

public class Uid {
    static int uid = 0;
    public static Integer getUid() {
        return uid++; // new task id
    }
}
