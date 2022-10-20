public class Uid {
    static int uid = 0;

    static Integer getUid() {
        return uid++; // new task id
    }
}
