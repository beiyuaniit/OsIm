package Process;

import Structure.*;

/**
 * @author: beiyuan
 * @className: CPU
 * @date: 2021/11/25  21:17
 */
public class CPU extends Thread {
    private myQueues myqueues;//管理队列
    private RAM ram;//管理内存
    private int time = 0;//初始时间
    private int cpuNum = 2;//cpu核心数,多个的话要重新设置运行队列的优先级即可

    private boolean isHang=false;
    private boolean notHang=false;

    proThread outPro;//取出的进程
    proThread inPro;//放进去的进程
    String isHangPro;//挂起的进程
    String  notHangPro;//解除挂起的进程

    proThread outPro01;//运行队列的第二低优先级进程

    //构造方法初始化
    public CPU(myQueues myqueues, RAM ram) {
        this.myqueues = myqueues;
        this.ram = ram;
    }

    //通知cpu挂起
    public void setIsHangPro(String  isHangPro) {
        this.isHangPro = isHangPro;
        this.isHang=true;
    }

    //通知cpu挂起
    public void setNotHangPro(String  notHangPro) {
        this.notHangPro = isHangPro;
        this.notHang=true;
    }


    @Override
    public void run() {
        System.out.println("初始化完成，请输入进程...");

        while (true) {

            //设置系统时间
            try {
                sleep(1000);
                time++;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //运行->挂起
            if (isHang) {
                outPro = myqueues.RunQueue.remove();


                //挂起第一个
                if (outPro.getName().equals(isHangPro)) {
                    System.out.println(outPro.getName() + "运行->挂起");
                    outPro.setRun(false);
                    myqueues.PendingQueue.add(outPro);
                    //回收内存
                    recycleRam(outPro);
                }
                //挂起第二个
                else {

                    inPro = myqueues.RunQueue.remove();
                    myqueues.RunQueue.add(outPro);

                    System.out.println(inPro.getName() + "运行->挂起");
                    inPro = myqueues.RunQueue.remove();
                    inPro.setRun(false);
                    myqueues.PendingQueue.add(inPro);
                    recycleRam(inPro);
                }

                isHang=false;

            }

            //从后备队列->放入就绪队列
            if (!myqueues.BackQueue.isEmpty()) {//非空
                outPro = myqueues.BackQueue.remove();

                if (haveWait(outPro)) {
                    System.out.println(outPro.getName() + "后备->就绪");
                    outPro.setStartTime(time);
                    myqueues.WaitQueue.add(outPro);
                } else {
                    myqueues.BackQueue.add(outPro);//没内存放回去
                }

            }

            //挂起->就绪
            if (notHang) {
                outPro = myqueues.PendingQueue.remove();
                if (haveWait(outPro)) {
                    System.out.println(outPro.getName() + "挂起->就绪");
                    myqueues.WaitQueue.add(outPro);
                    notHang=false;
                } else {
                    myqueues.PendingQueue.add(outPro);//没内存放回去
                }

            }

            //就绪->运行
            if (!myqueues.WaitQueue.isEmpty()) {

                if (myqueues.RunQueue.size() < cpuNum) {
                    outPro = myqueues.WaitQueue.remove();
                    System.out.println(outPro.getName() + "就绪->运行");
                    outPro.setRun(true);//运行态
                    myqueues.RunQueue.add(outPro);
                }
            }

            //运行...
            if (!myqueues.RunQueue.isEmpty()) {
                outPro = myqueues.RunQueue.remove();//优先级最低
                outPro.setRunTime(outPro.getRunTime() - 1);//运行时间减1
                outPro.setPri(outPro.getPri() - 1);//优先权减1

                if (myqueues.RunQueue.size() == 1) {//还有一个的话
                    outPro01 = myqueues.RunQueue.remove();
                    outPro01.setRunTime(outPro01.getRunTime() - 1);
                    outPro01.setPri(outPro01.getPri() - 1);
                    if (outPro01.getRunTime() == 0) {
                        recycleRam(outPro01);
                        outPro01.setEndtime(time);//结束
                    }else {
                        myqueues.RunQueue.add(outPro01);//放回去
                    }

                }


                if (outPro.getRunTime() == 0) {
                    recycleRam(outPro);
                    outPro.setEndtime(time);//结束
                } else if (!myqueues.WaitQueue.isEmpty()) {
                    inPro = myqueues.WaitQueue.remove();
                    if (outPro.getPsize() < inPro.getPsize()) {//被抢占
                        System.out.println(outPro.getName() + "运行->就绪");

                        outPro.setRun(false);
                        myqueues.WaitQueue.add(outPro);//out放到就绪抢占

                    } else if (outPro.getPsize() >= inPro.getPsize()) {
                        myqueues.RunQueue.add(outPro);//放回运行队列

                    }
                    myqueues.WaitQueue.add(inPro);//in放入就绪继续抢占

                } else {
                    myqueues.RunQueue.add(outPro);//放回运行队列
                }


            }


        }

    }

    //判断是否能添加到就绪队列
    public boolean haveWait(proThread prothread) {
        for (int i = 0; i < ram.spareRAM.size(); i++) {
            //有则进行分配
            if (ram.spareRAM.elementAt(i).size >= prothread.getPsize()) {
                int startIndex = ram.spareRAM.elementAt(i).startIndex;
                int size = prothread.getPsize();
                String name = prothread.getName();

                System.out.println("\n" + name + "占用内存：" + startIndex + "~" + (startIndex + size - 1));

                RAMUnit ramUnit = new RAMUnit(startIndex, startIndex + size - 1, name);
                ram.busyRAM.add(ramUnit);//添加一个到忙碌
                if (ram.spareRAM.elementAt(i).size == prothread.getPsize()) {
                    ram.spareRAM.remove(i);//删除此处空闲
                    return true;
                } else {
                    //对空闲区处理
                    ram.spareRAM.elementAt(i).startIndex += size;
                    ram.spareRAM.elementAt(i).size-=size;
                    return true;
                }
            }
        }

        //无内存
        System.out.println("内存不足以运行进程：" + prothread.getName() + "...继续等待");
        return false;
    }


    //回收内存
    public void recycleRam(proThread pro) {
        int lindex = -1;//左相连下标
        int rindex = -1;//右相连下标

        int newsta = -1;//空闲分区新增内存单元索引
        int newend = -1;
        for (int i = 0; i < ram.busyRAM.size(); i++) {
            if (ram.busyRAM.elementAt(i).proName == pro.getName()) {
                RAMUnit Reram = ram.busyRAM.elementAt(i);//进程要回收的内存

                System.out.println("回收了内存：" + Reram.startIndex + "~" + Reram.endIndex);
                for (int j = 0; j < ram.spareRAM.size(); j++) {
                    RAMUnit spa = ram.spareRAM.elementAt(j);

                    //左
                    if (Reram.startIndex == spa.endIndex + 1) {
                        lindex = j;
                        newsta = spa.startIndex;
                    }
                    //右
                    if (Reram.endIndex == spa.startIndex - 1) {
                        rindex = j;
                        newend = spa.endIndex;
                    }
                }

                //左右进行处理
                if (rindex == -1) {
                    newend = Reram.endIndex;
                }
                if (lindex == -1) {
                    newsta = Reram.startIndex;
                }

                //回收
                ram.busyRAM.removeElementAt(i);
                if (lindex != -1) {
                    ram.spareRAM.removeElementAt(lindex);
                }
                if (rindex != -1) {
                    ram.spareRAM.removeElementAt(rindex);
                }

                //新增
                System.out.println("新增空闲内存：" + newsta + "~" + newend);
                RAMUnit newram = new RAMUnit(newsta, newend);

                //插入
                if (ram.spareRAM.size() == 0) {
                    ram.spareRAM.add(newram);
                } else {
                    for (int k = 0; k < ram.spareRAM.size(); k++) {
                        if (newram.startIndex < ram.spareRAM.elementAt(k).size) {
                            ram.spareRAM.insertElementAt(newram, k);
                        }
                    }
                }
                break;
            }
        }
    }
}
