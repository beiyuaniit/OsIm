/**
 * @author: beiyuan
 * @className: main
 * @date: 2021/11/18  19:28
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import Structure.*;
import Process.*;

public class main {
    static RAM ram;//内存
    static myQueues myqueues;//所有队列
    static proThread newProThread;//进程
    static CPU cpu;//系统CPU
    static int ramsize=100;//内存大小

    //主函数
    public static void main(String[] args) {
        //初始化
        init();

        //添加进程的图形界面
        windows();

    }

    //初始化
    public static void init() {
        //初始化内存
        ram = new RAM(ramsize);
        //初始化队列
        myqueues = new myQueues();
        //创建CPU
        cpu = new CPU(myqueues, ram);
        cpu.start();
    }

    //登录界面初始化
    public static void windows() {
        JFrame frame = new JFrame("添加进程");
        frame.setLayout(null);

        //01
        JLabel name = new JLabel("进程名");
        name.setBounds(250, 200, 100, 25);
        frame.add(name);
        //
        JTextField Name = new JTextField();
        Name.setBounds(300, 200, 150, 25);
        frame.add(Name);

        //02
        JLabel pri = new JLabel("优先级");
        pri.setBounds(250, 250, 100, 25);
        frame.add(pri);
        //
        JTextField Pri = new JTextField();
        Pri.setBounds(300, 250, 150, 25);
        frame.add(Pri);

        //03
        JLabel runTime = new JLabel("运行时间");
        runTime.setBounds(250, 300, 100, 25);
        frame.add(runTime);
        //
        JTextField RunTime = new JTextField();
        RunTime.setBounds(300, 300, 150, 25);
        frame.add(RunTime);

        //04
        JLabel Ramsize = new JLabel("所需内存");
        Ramsize.setBounds(250, 350, 100, 25);
        frame.add(Ramsize);
        //
        JTextField RamSize = new JTextField();
        RamSize.setBounds(300, 350, 150, 25);
        frame.add(RamSize);

        //05添加按钮
        JButton buttonlogin = new JButton("添加");
        buttonlogin.setBounds(375, 400, 70, 25);
        frame.add(buttonlogin);


        //06挂起
        JLabel IsHang= new JLabel("挂起进程");
        IsHang.setBounds(500, 200, 100, 25);
        frame.add(IsHang);

        JTextField isHang= new JTextField();
        isHang.setBounds(550, 200, 150, 25);
        frame.add(isHang);




        //08挂起按钮
        JButton buttonIsHang = new JButton(" 挂起");
        buttonIsHang.setBounds(625, 250, 70, 25);
        frame.add(buttonIsHang);


        //07解除挂起
        JLabel NotHang= new JLabel("解除进程");
        NotHang.setBounds(500, 300, 100, 25);
        frame.add(NotHang);

        JTextField notHang= new JTextField();
        notHang.setBounds(550, 300, 150, 25);
        frame.add(notHang);


        //09解除挂起按钮
        JButton buttonNotHang = new JButton(" 解除");
        buttonNotHang.setBounds(625, 350, 70, 25);
        frame.add(buttonNotHang);

        //总窗口
        frame.setBounds(400, 100, 1100, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //为添加按钮添加监听器
        buttonlogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String proName = Name.getText();
                int proPri = Integer.parseInt(Pri.getText());
                int proRuntime = Integer.parseInt(RunTime.getText());
                int size = Integer.parseInt(RamSize.getText());

                if(size>=ramsize){
                    JOptionPane.showMessageDialog(null,
                            "大于cpu总内存,请重新输入", "错误",
                            JOptionPane.WARNING_MESSAGE);
                }
                else {
                    //创建进程
                    newProThread = new proThread(proName, proPri, proRuntime ,size , myqueues);
                    newProThread.start();

                    //弹出成功窗口
                    JOptionPane.showMessageDialog(null,
                            "添加成功", "",
                            JOptionPane.WARNING_MESSAGE);
                }
                //清除进程信息框
                Name.setText("");
                Pri.setText("");
                RunTime.setText("");
                RamSize.setText("");
            }
        });

        //为登录按钮添加监听器
        buttonIsHang.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String IsHang = isHang.getText();
                cpu.setIsHangPro(IsHang);
                //弹出成功窗口
                JOptionPane.showMessageDialog(null,
                        "挂起成功", "",
                        JOptionPane.WARNING_MESSAGE);
                //清除进程信息框
                isHang.setText("");
            }
        });

        //为登录按钮添加监听器
        buttonNotHang.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String NotHang=notHang.getText();
                cpu.setNotHangPro(NotHang);
                //弹出成功窗口
                JOptionPane.showMessageDialog(null,
                        "解除挂起", "",
                        JOptionPane.WARNING_MESSAGE);
                notHang.setText("");
            }
        });

    }
}

