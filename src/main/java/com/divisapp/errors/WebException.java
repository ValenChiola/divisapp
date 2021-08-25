/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divisapp.errors;

/**
 *
 * @author Usuario
 */
public class WebException extends Exception {

    /**
     * Creates a new instance of <code>WebException</code> without detail
     * message.
     */
    public WebException() {
    }

    /**
     * Constructs an instance of <code>WebException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public WebException(String msg) {
        super(msg);
    }
}
