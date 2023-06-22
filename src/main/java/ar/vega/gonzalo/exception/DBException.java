/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.vega.gonzalo.exception;

/**
 *
 * @author Gonzalo Vega <ar.vega.gonzalo>
 */
public class DBException extends Exception {
    
    private String message;

    public DBException(String message) {
        this.message = message;
    }

    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
