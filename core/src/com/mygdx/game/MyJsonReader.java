/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

/**
 *
 * @author 3200g
 */
public class MyJsonReader {
    public MyJsonReader(String json){
        this.json=json;
    }
    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
    String json;
    
    
}
