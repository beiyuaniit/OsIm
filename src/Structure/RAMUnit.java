package Structure;

/**
 * @author: beiyuan
 * @className: RAMUnit
 * @date: 2021/11/18  20:43
 */
public class RAMUnit{
    public int startIndex;
    public int endIndex;
    public int size;
    public String proName;//当前正在占用该内存的进程

    public RAMUnit(int startIndex, int endIndex, String proName) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.size = endIndex-startIndex+1;
        this.proName=proName;
    }

    public RAMUnit(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.size = endIndex-startIndex+1;
    }
}
