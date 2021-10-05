package Simulation;

import java.awt.*;

//DrawLine class
public class DrawLine
{
    private int gX;
    private int gY;
    private int gWidth;
    private int gHeight;
    final double propagation = 2.8E+8;
    private double length;
    private double rate;
    private double time;
    private Packet myPacket;

    public DrawLine(int x, int y, int w, int h)
    {
        //graphic init
        gX=x;
        gY=y;
        gWidth=w;
        gHeight=h;
        
    }

    public void init(double l, double r,  double s, double eT)
    {
        length=l;
        rate=r;
        myPacket= new Packet(s,eT);
    }

    void setTime(double currentTime)
    {
        time=currentTime; //update time
      
        if (!(myPacket==null))
        {
            if ( currentTime>=myPacket.emissionTime+(myPacket.size/rate)+length/propagation )
            {
                removePackets();
            }
        }
    }

  
    public void removePackets()
    {
        myPacket=null;
    }

    public double getTotalTime()
    {
        double transmissionDelay=(myPacket.size/rate); //Tt = (size of packet / speed of transmission)
        double propDelay=(length/propagation); //Tp = (distance / propagation speed)
        System.out.printf("Transmission Delay = "+transmissionDelay+"\nProp Delay = "+propDelay+"\n");
        return (transmissionDelay+propDelay);
    }

    public void drawLine(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(gX,gY+1,gWidth,gHeight-2);
        g.setColor(Color.black);
        g.drawRect(gX,gY,gWidth,gHeight);
        g.setColor(Color.red);
        g.drawString(timeToString(time),gX+gWidth/2-10,gY+gHeight+15);
        drawPackets(g);
    }

    private void drawPackets(Graphics g)
    {
        if (!(myPacket==null))
        {
            double xfirst;
            double xlast;
            xfirst=time-myPacket.emissionTime;
            xlast=xfirst-(myPacket.size/rate);
            xfirst=xfirst*propagation*gWidth/length;
            xlast=xlast*propagation*gWidth/length;
            if (xlast<0) {xlast=0;}
            if (xfirst>gWidth ) {xfirst=gWidth;}
            g.setColor(Color.red);
            g.fillRect(gX+(int)(xlast),gY+1,(int)(xfirst-xlast),gHeight-2);
        }
    }

    static private String timeToString(double now)
    {
        String res=Double.toString(now*1000);
        int dot=res.indexOf('.');
        String deci=res.substring(dot+1)+"000";
        deci=deci.substring(0,3);
        String inte=res.substring(0,dot);
        return inte+"."+deci+" ms";
    }
}