package view;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import util.ResourceLoader;

public class Tutorial {
    
    public class TutorialData {
        
        public TutorialData(String imgText, Image img,int imgNumber){
            this.imgText = imgText;
            this.img = img;
            this.imgNumber = imgNumber;
        }
        
        public String imgText;
        public Image img;
        public int imgNumber;
    }
    
    private Panel panel;
    
    private int minImg;
    private int maxImg;
    private int currentImg;
    
    private Image background;
    
    private MyButton leftBtn;
    private MyButton rightBtn;
    private MyButton backBtn;
    private ArrayList<TutorialData> allData;
    private ArrayList<String> allText;
    
    //ugrade: read min,max,text,imagename from CSV file :)
    //(delete imgnumber
    
    public Tutorial(Panel panel){
        this.panel = panel;
        
        minImg = 0;
        maxImg = 2;
        currentImg = 0;
        allText = new ArrayList<>();
        allData = new ArrayList<>();
        
        allText.add("első lépés dummy text");
        allText.add("második lépés dummy text");
        allText.add("harmadik lépés dummy text");
        
        
        try {

            background = ResourceLoader.loadImage("tutbg.png");
            
            for(int i=minImg;i<=maxImg;++i){
                String imgText = allText.get(i);
                Image img = ResourceLoader.loadImage("tutorialImg" + i + ".png");
                TutorialData newData = new TutorialData(imgText, img,i);
                allData.add(newData);
            }
           
            leftBtn = new MyButton(0,350,100,100,"arrowLeft");
            rightBtn = new MyButton(1436,350,100,100,"arrowRight");
            backBtn = new MyButton(0,0,75,75,"back");
        } catch (IOException ex) { }
    }
    
    public void draw(Panel panel, Graphics2D gr){
        gr.drawImage(background,0,0,1536 + 15,793,null);
        leftBtn.draw(gr,panel.getMousePosition());
        rightBtn.draw(gr,panel.getMousePosition());
        backBtn.draw(gr,panel.getMousePosition());
        
        gr.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
        gr.drawImage(allData.get(currentImg).img,143,100,1250,675,null);
        gr.drawString( (currentImg+1) + ". Lépés: " + allData.get(currentImg).imgText,143,75);
    }
    
    public void click(Point p){
        System.out.println(currentImg);
        if(leftBtn.isHovered(p)){
            currentImg--;
            if(currentImg < minImg){
                currentImg = maxImg;
            }
            
        }
        else if(rightBtn.isHovered(p)){
            currentImg++;
            if(currentImg > maxImg){
                currentImg = minImg;
            }      
        }
        else if(backBtn.isHovered(p)){
            panel.setState(MenuState.MAINMENU);
        }
    }
}
