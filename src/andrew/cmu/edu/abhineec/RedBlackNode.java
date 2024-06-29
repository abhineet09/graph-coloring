/*
 * Submission Details:
 *      Name - Abhineet Chaudhary
 *      andrewId = abhineec
 *      Course - 95-771 Data Structure and Algorithms for Information Processing
 *      Section - A
 * */

package andrew.cmu.edu.abhineec;

/********************************************************************************************************
 * RedBlackNode class represents a node in a RedBlackTree with properties
 * key - node's identifier
 * index - index of the key in vertices array
 * color - RED or BLACK based on node's position in tree
 * p - node's parent
 * lc - node's left child
 * rc - node's right child
 ********************************************************************************************************/

public class RedBlackNode {
    public static final int RED = 0;
    public static final int BLACK = 1;
    private String key;
    private Integer index;
    private int color;
    private RedBlackNode p;
    private RedBlackNode lc;
    private RedBlackNode rc;

    /**
        Parameterized constructor of RedBlackNode class
     */
    public RedBlackNode(String key, int index, int color, RedBlackNode p, RedBlackNode lc, RedBlackNode rc) {
        this.key = key;
        this.index = index;
        this.color = color;
        this.p = p;
        this.lc = lc;
        this.rc = rc;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        if(this.key != null)
            this.color = color;
    }

    public RedBlackNode getP() {
        return p;
    }

    public void setP(RedBlackNode p) {
        this.p = p;
    }

    public RedBlackNode getLc() {
        return lc;
    }

    public void setLc(RedBlackNode lc) {
        this.lc = lc;
    }

    public RedBlackNode getRc() {
        return rc;
    }

    public void setRc(RedBlackNode rc) {
        this.rc = rc;
    }

    public String getKey() {
        return key;
    }

    public int getIndex() {
        return index;
    }

}
