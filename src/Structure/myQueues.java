package Structure;

import java.util.PriorityQueue;
import java.util.Vector;

import Process.*;
/**
 * @author: beiyuan
 * @className: RAM
 * @date: 2021/11/18  20:16
 */
public class myQueues {

    //创建后备队列(刚来的进程--内存满足可以放进就绪队列
    public PriorityQueue<proThread>  BackQueue=new PriorityQueue<>();

    //创建就绪队列(内存满足的进程
    public PriorityQueue<proThread> WaitQueue=new PriorityQueue<>();

    //创建挂起队列(被抢占后的进程--内存满足可以放进就绪队列
    public  PriorityQueue<proThread> PendingQueue=new PriorityQueue<>();

    //创建运行队列--大小代表处理机数量--可以从就绪队列中找到优先级最高的运行（可抢占原有的
    public PriorityQueue<proThread> RunQueue=new PriorityQueue<>();

}
