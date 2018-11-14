/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

import Model.ClientNode;
import Model.Color;
import java.util.LinkedList;

/**
 *
 * @author HP
 */
public class Graph {

    // Java Program to demonstrate adjacency list 
    // representation of graphs 
    // A user define class to represent a graph. 
    // A graph is an array of adjacency lists. 
    // Size of array will be V (number of vertices in graph) 
    int V;
    LinkedList<ClientNode> adjListArray[];

    // constructor 
    Graph(int V) {
        this.V = V;

        // define the size of array as 
        // number of vertices 
        adjListArray = new LinkedList[V];

        // Create a new list for each vertex 
        // such that adjacent nodes can be stored 
        for (int i = 0; i < V; i++) {
            adjListArray[i] = new LinkedList<>();
        }
    }

    // Adds an edge to an undirected graph 
    static boolean addEdge(Graph graph, ClientNode src, ClientNode dest) {

        // Add an edge from src to dest. 
        graph.adjListArray[src.getClientNumber()].add(dest);

        // Since graph is undirected, add an edge from dest 
        // to src also 
        graph.adjListArray[dest.getClientNumber()].add(src);
        return true;
        //   if (isValidVertex(graph, src, dest)) {   }
        //     return false;
    }

    static boolean isValidColor(Graph graph, ClientNode clientNode, Color color) {
        for (ClientNode node : graph.adjListArray[clientNode.getClientNumber()]) {
            if (node.getColor() == color) {
                return false;
            }
        }
        return true;
    }

    // A utility function to print the adjacency list 
    // representation of graph 
    static void printGraph(Graph graph) {
        for (int v = 0; v < graph.V; v++) {
            System.out.println("Adjacency list of client " + (v + 1));
            System.out.print("head");

            for (ClientNode node : graph.adjListArray[v]) {
                System.out.print(" -> " + (node.getClientNumber() + 1) + "," + node.getColor());
            }
            System.out.println("\n");
        }
    }

    static boolean coloring(Graph graph, ClientNode node, Color color) {
        if (isValidColor(graph, node, color)) {
            node.setColor(color);
            return true;
        }
        return false;
    }

}
