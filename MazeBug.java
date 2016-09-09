package info.gridworld.maze;

import info.gridworld.actor.*;
import info.gridworld.grid.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.swing.JOptionPane;

/**
 * A <code>MazeBug</code> can find its way in a maze. <br />
 * The implementation of this class is testable on the AP CS A and AB exams.
 */
public class MazeBug extends Bug {
    private Location next;
    private boolean isEnd = false;
    private Stack<Location> crossLocation = new Stack<Location>();
    private Integer stepCount = 0;
    //final message has been shown.
    private boolean hasShown = false;
    private int proir[] = {1, 1, 1, 1};
    private int direc[] = {0, 0, 0, 0};

    /**
    * Constructs a box bug that traces a square of a given side length
    * 
    * @param length
    *            the side length
    */
    public MazeBug() {
        setColor(Color.GREEN);
    }

    /**
     * Moves to the next location of the square.
     */
    public void act() {
        ArrayList<Location> nextlist = getValid(getLocation());
        if (isEnd) {
            //to show step count when reach the goal        
            if (!hasShown) {
                String msg = stepCount.toString() + " steps";
                JOptionPane.showMessageDialog(null, msg);
                hasShown = true;
            }
        } else {
            if (nextlist.size() != 0) {
                getNext();
                crossLocation.push(getLocation());
                move();
                stepCount++;
            } else {
                //back tracking.
                next =  crossLocation.pop(); 
                //descrease the count
                int a = getLocation().getDirectionToward(next)/90;
                a+=2;
                if(a>3) {
                  a%=4;
                }
                proir[a]--;
                move();
                stepCount++;
            }
        }
    }

    /**
     * Find all positions that can be move to.
     * 
     * @param loc
     *            the location to detect.
     * @return List of positions.
     */
    public ArrayList<Location> getValid(Location loc) {
        Grid<Actor> gr = getGrid();
        if (gr == null) {
            return null;
        }
        ArrayList<Location> valid = new ArrayList<Location>();
        for (int i = 0; i < 4; i++) {
            direc[i] = 0;
        }
        for (int i = 0; i < 4; i++) {
            Location adjloc = loc.getAdjacentLocation(i*90);
            if (gr.isValid(adjloc)) {
                Actor temp = gr.get(adjloc);
                if (temp == null) {
                    valid.add(adjloc);
                    direc[i] = 1;
                }
                if (temp instanceof Rock && (temp.getColor()).equals(Color.RED) ) {
                    isEnd = true;
                }
            }
        }

        return valid;
    }
    /**
     * Moves the bug forward, putting a flower into the location it previously
     * occupied.
     */
    public void move() {
        Grid<Actor> gr = getGrid();
        if (gr == null) {
            return;
        }
        Location loc = getLocation();
        if (gr.isValid(next)) {
            setDirection(getLocation().getDirectionToward(next));
            moveTo(next);
            Flower flower = new Flower(getColor());
            flower.putSelfInGrid(gr, loc);
        } else {
            removeSelfFromGrid();
            Flower flower = new Flower(getColor());
            flower.putSelfInGrid(gr, loc);
        }
    }

    /*
     * Get the next location by count.
     */
    public void getNext() {
        int a = 0;
        int max = 0;
        for (int i = 0; i < 4; i++) {
            if (direc[i] == 1 && proir[i] > max) {
                max = proir[i];
                a = i;
            }
        }
        next = getLocation().getAdjacentLocation(a*90);
        proir[a]++;
    }
}
