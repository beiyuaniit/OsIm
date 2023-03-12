package Process;
import Structure.*;

/**
 * @author: beiyuan
 * @className: PCB
 * @date: 2021/11/10  16:19
 */

//进程PCB
public class proThread extends Thread implements Comparable<proThread>{
 private
        String Name;//进程名
        int pri; //优先级
        int runTime; //需要运行时间
        int size;//进程需要内存
        int startTime;//记录到来时间
        myQueues myqueues;//用于添加到不同的队列
        int endtime=-1;//结束时间
        boolean isRun=false;//运行时的优先队列排列规则相反


    //状态
    //cerat:刚创建  zero:正在被使用  wait:就绪 run:运行  pend:挂起
    public enum Status{
            creat,zero
    }
    public Status status;

    @Override
    public void run() {
        if(this.status==Status.creat){
            //刚到来的进程放入后备队列
            this.setName(Name);//设置进程名
            myqueues.BackQueue.add(this);
            this.status=Status.zero;
            System.out.println(Name+"->放入后备");

        }

        while (true){
            //目前该进程啥也没做
            System.out.print("");//加上这句话就开始干活
            if(endtime!=-1){
                System.out.println("\n进程->"+getName()+ "运行结束");
                System.out.println("startTime:"+startTime);
                System.out.println("endTime："+endtime+"\n");
                this.stop();//结束
            }

        }
    }

    //构造函数
    public proThread(String name, int pri, int runTime, int psize, myQueues myqueues) {
        Name = name;
        this.pri = pri;
        this.runTime = runTime;
        size = psize;
        this.status=Status.creat;
        this.myqueues=myqueues;
    }




    public void setStartTime(int startTime){ this.startTime=startTime;}
    //外界设置进程是否为运行
    public void setRun(boolean isRun){this.isRun=isRun;}
    //设置剩余运行时间
    public void setRunTime(int runTime){
        this.runTime=runTime;
    }
    //设置优先级
    public void setPri(int pri) {
        this.pri = pri;
    }
    //结束时间
    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    //获取PCB

    public int getRunTime() {
        return runTime;
    }

    public int getPri() {
        return pri;
    }

    public int getPsize() {
        return size;
    }


    //实现比较器接口
    @Override
    public int compareTo(proThread o) {
        if(isRun==false)
            return o.pri -this.pri;
        else
            return this.pri-o.pri;
    }
}
