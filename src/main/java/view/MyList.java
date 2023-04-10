package view;

import java.awt.*;
import java.util.ArrayList;

public class MyList {
    private Rectangle rect;
    private ArrayList<ListElem> list;
    private Data selected;

    class Data {
        public String name;
        public String time;
        public String funds;

        public Data(String name, String time, String funds) {
            this.name = name;
            this.time = time;
            this.funds = funds;
        }

    }

    class ListElem {
        public Data data;
        public Rectangle rect;

        public ListElem(Data data, Rectangle rect) {
            this.data = data;
            this.rect = rect;
        }
    }

    public MyList(int x, int y, int width, int height) {
        selected = null;
        rect = new Rectangle(x, y, width, height);
        list = new ArrayList<>();
        //list.add(new Data("Mentés 1","1:12","50.000$"));
        //list.add(new Data("Mentés 2","3:01","1.000$"));
        //list.add(new Data("Mentés 3","0:59","16.250$"));
    }

    public void draw(Graphics2D gr, Point cursorPos) {
        gr.setColor(Color.white);
        gr.fillRect(rect.x, rect.y, rect.width, rect.height);
        /*
        for(int i=0;i<data.size();++i){
            Data curr = data.get(i);
            gr.setColor(Color.DARK_GRAY);
            gr.fillRect(rect.x, rect.y + i*76, rect.width, 76);
            gr.setColor(Color.white);
            gr.drawRect(rect.x, rect.y + i*76, rect.width, 76);
            
            gr.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            gr.drawString(curr.name + "   " + curr.time + "   " + curr.funds,rect.x + 5,rect.y + i*76 + 50);
        }
        */

    }

}
