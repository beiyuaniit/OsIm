package Structure;

import java.util.Vector;

/**
 * @author: beiyuan
 * @className: RAM
 * @date: 2021/11/18  20:40
 */
public class RAM {
    public int size;
    //动态数据作为内存分配表
    public Vector<RAMUnit> busyRAM=new Vector<>();
    public Vector<RAMUnit> spareRAM=new Vector<>();

    //设置内存大小
    public RAM(int size) {
        this.size = size;
        int endIndex=size-1;
        spareRAM.add(new RAMUnit(0,endIndex));//添加初始的空闲分区
        busyRAM.add(new RAMUnit(0,0,null));//无忙碌分区则添加0
    }

}
