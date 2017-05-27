package com.dangbei.apk.label;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public class LinkLabel extends JLabel {  
    /** 
     *  
     */  
    private static final long serialVersionUID = 1L;  
    private String text, url,color;  
    private boolean isSupported;  
    
    public LinkLabel(String text, String url,String color) {  
        this.text = text;  
        this.url = url;  
        this.color = color;
        try {  
            this.isSupported = Desktop.isDesktopSupported()  
                    && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);  
        } catch (Exception e) {  
            this.isSupported = false;  
        }  
          
        setText(false);  
          
        addMouseListener(new MouseAdapter() {  
            public void mouseEntered(MouseEvent e) {  
                setText(isSupported);  
                if (isSupported)  
                    setCursor(new Cursor(Cursor.HAND_CURSOR));  
            }  
              
            public void mouseExited(MouseEvent e) {  
                setText(false);  
            }  
              
            public void mouseClicked(MouseEvent e) {  
                try {  
                    Desktop.getDesktop().browse(  
                            new java.net.URI(LinkLabel.this.url));  
                } catch (Exception ex) {  
                }  
            }  
        });  
    }  
      
    private void setText(boolean b) {  
        if (!b)  
            setText("<html><font color="+color+"><u>" + text);  
        else  
            setText("<html><font color="+color+"><u>" + text);  
    }  
}  
