/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author HP
 */
public class ClientNode {

    int clientNumber;
    Color color;

    public ClientNode(int clientNumber, Color color) {
        this.clientNumber = clientNumber;
        this.color = color;
    }

    public ClientNode() {
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ClientNode(int clientNumber) {
        this.clientNumber = clientNumber;
        this.color = Color.notSet;
    }

}
