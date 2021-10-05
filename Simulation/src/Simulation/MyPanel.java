package Simulation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MyPanel extends JPanel implements ActionListener{

    //start and stop buttons
    JButton start,stop;
    //choice fields
    MyJcomboBox length,rate,size;
    //to simulate time
    Thread timerThread;
    TimerTask timerTask;
    boolean simulationRunning=false;
    //communication line
    DrawLine myLine;
    // x coordinate
    int x = 120;
    int xLine=20;

    public MyPanel() {
        this.setBorder(BorderFactory.createEmptyBorder(150, 150, 150, 150));
        this.setLayout(null);
        this.setPreferredSize(new Dimension(580,500));


        String[] ChoseLength = {"10 km", "100 km", "1000 km"};
        JLabel LengthLabel = new JLabel("Length");
        LengthLabel.setBounds(x+120,70,80,30);
        length = new MyJcomboBox(ChoseLength, new double[]{10E3, 100E3, 1E6});
        length.setBounds(x+120,100,80,25);
        length.setSelectedIndex(0 );
        add(length);
        add(LengthLabel);

        String[] ChooseRate = {"512 kps","1 Mbps","10 Mbps","100 Mbps"};
        JLabel LengthRate = new JLabel("Rate");
        LengthRate.setBounds(x,70,80,30);
        rate = new MyJcomboBox(ChooseRate, new double[]{512E3, 1E6, 10E6, 100E6});
        rate.setBounds(x,100,80,25);
        rate.setSelectedIndex(0);
        add(rate);
        add(LengthRate);

        String[] chooseSize = {"100 Bytes","500 Bytes","1 kBytes"};
        JLabel labelSize = new JLabel("Size");
        labelSize.setBounds(x+230,70,80,30);
        size = new MyJcomboBox(chooseSize, new double[]{8E2, 4E3, 8E3});
        size.setBounds(x+230,100,80,25);
        size.setSelectedIndex(0);
        add(labelSize);
        add(size);

        final TextField tf=new TextField();
        tf.setBounds(50,50, 150,20);

        start = new JButton("Start ");
        start.setBounds(x+60,50,80,25);
        start.setActionCommand("Start");
        start.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {

                launchSim();
            }

        });
        add(start);

        stop = new JButton("Reset ");
        stop.setBounds(x+170,50,80,25);
        stop.setActionCommand("Reset");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopSim();
                //clear line
                myLine.setTime(0);
                //redraw cleared line
                MyPanel.this.repaint();
            }
        });
        add(stop);

        myLine= new DrawLine(xLine+40,210,450,10);

    }
    @Override
    public void paintComponent(Graphics g) {
    	
        super.paintComponent(g);
        Graphics2D packets =(Graphics2D) g;
        myLine.drawLine(packets);

        //sender
        packets.setColor(Color.blue);
        packets.fillOval(xLine+10,200,30,30);
        packets.setColor(Color.black);
        packets.drawString("Sender",xLine+5,250);
        packets.drawOval(xLine+10,200,30,30);

        //receiver
        packets.setColor(Color.blue);
        packets.fillOval(xLine+490,200,30,30);
        packets.setColor(Color.black);
        packets.drawString("Receiver",xLine+485,250);
        packets.drawOval(xLine+490,200,30,30);
        
        
        packets.setFont(new Font("default", Font.BOLD, 13));

        packets.drawString("Propagation speed : 2.8 x 10^8 m/sec",175,265);
       


    }


    private void launchSim()
    {
    	//Code below disables all the selections fields and start button
    	setupEnabled(false);
        //setup line
        myLine.init(length.getVal(), rate.getVal(), size.getVal(),0 );
        //setup timer
        timerTask=new TimerTask(1E-5,myLine.getTotalTime());
        timerThread=new Thread(timerTask);
        //start simulation
        simulationRunning=true;
        timerThread.start();
    }

    private void stopSim()
    {
        timerTask.endNow();
        simulationRunning=false;
        setupEnabled(true);
    }

    public void setupEnabled(boolean value)
    {
        start.setEnabled(value);
        length.setEnabled(value);
        rate.setEnabled(value);
        size.setEnabled(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }


    static class MyJcomboBox extends JComboBox{
        private final double[] vals;
        MyJcomboBox(String[] items,double[] values){
            for (String item : items) {
                super.addItem(item);
            }
            vals= values;
        }
        public double getVal(){
            return vals[super.getSelectedIndex()];
        }
    }

    class TimerTask implements Runnable
    {
        private double counter;
        private double maxTime;
        private final double timeIncrementor;

        public TimerTask(double t,double l)
        {
            maxTime=l;
            timeIncrementor=t;
            counter=0;
        }

		@SuppressWarnings("deprecation")
		public void run()
        {
            while (simulationRunning)
            {
                counter+=timeIncrementor;
               myLine.setTime(counter);
                repaint();
                if (counter>=maxTime)
                {
                    myLine.removePackets();
                    timerThread.stop();
                }
                try  {
                    Thread.sleep(50);
                }
                catch (Exception ignored) { }
            }
        }

        @SuppressWarnings("deprecation")
		public void endNow() {
            maxTime=counter;
            timerThread.stop();
        }
    }

}
