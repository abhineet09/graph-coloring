/*
 * Submission Details:
 *      Name - Abhineet Chaudhary
 *      andrewId = abhineec
 *      Course - 95-771 Data Structure and Algorithms for Information Processing
 *      Section - A
 * */

package andrew.cmu.edu.abhineec;

/********************************************************************************************************
 * RedBlackTree class implements a Red Black tree as described in the javadoc:
 * https://www.andrew.cmu.edu/user/mm6/95-771/examples/DSARedBlackTreeProject/dist/javadoc/index.html
 ********************************************************************************************************/

public class RedBlackTree {
    private RedBlackNode root;
    private RedBlackNode nil;

    private int countNodes = 0;

    public RedBlackTree(){
        this.nil = new RedBlackNode(null, -1, RedBlackNode.BLACK, null, null, null);
        this.root = this.nil;
    }

    /**
     * Insert a key (course) in tree if it is not already present
     * else return the index attribute for the key
     * @param key
     *      course name to be inserted in the dictionary
     * @precondition
     *  RedBlackTree has been initialized
     * @postcondition
     *   The key is added to tree if it was new and
     *   key's index has been returned
     **/
    public int getOrInsert(String key){
        //check if key  is already added to tree
        int keyIndexInTree = find(key);
        if(keyIndexInTree == -1) {
            //if key is not present
            keyIndexInTree = this.countNodes++;
            //initialize a new RedBlackNode
            RedBlackNode z = new RedBlackNode(key, keyIndexInTree, RedBlackNode.RED, null, null, null);
            RedBlackNode y = this.nil;
            RedBlackNode x = this.root;
            //Find an appropriate position in tree for key
            while (x != this.nil) {
                y = x;
                //if new node's key is less than current node's key
                //move to left subtree of current node
                if(z.getKey().compareTo(x.getKey()) < 0)
                    x = x.getLc();
                //else move to right subtree of current node
                else
                    x = x.getRc();
            }
            //set new node's parent
            z.setP(y);
            //if new node's parent is nil
            //mark new node as root
            if (y == this.nil)
                this.root = z;
            //if new node's key is less than current node's key
            //add to left subtree of current node
            else if(z.getKey().compareTo(y.getKey()) < 0)
                y.setLc(z);
            //else move to right subtree of current node
            else
                y.setRc(z);
            //set new node's children to nil
            z.setLc(this.nil);
            z.setRc(this.nil);
            //set new node's color as RED
            z.setColor(RedBlackNode.RED);
            RBInsertFixup(z);
        }
        return keyIndexInTree;
    }

    /**
     * Find a key (course) in tree and return its index
     * else return -1
     * @param key
     *      course name(key) to be searched in tree
     * @precondition
     *  RedBlackTree has been initialized
     * @postcondition
     *   key's index is returned if it was present
     *   else -1 is returned
     **/
    public int find(String key){
        RedBlackNode iterator = this.root;
        while(iterator != this.nil){
            if(iterator.getKey().equals(key))
                return iterator.getIndex();
            else if(iterator.getKey().compareTo(key) > 0)
                iterator = iterator.getLc();
            else
                iterator = iterator.getRc();
        }
        return -1;
    }

    /**
     * Ensure all RedBlack Tree properties are satisfied
     * else fix the tree
     * @param z
     *      node that has been added to tree
     * @precondition
     *  RedBlackTree has been initialized
     *  and node has been inserted into the tree
     * @postcondition
     *  All RedBlack Tree properties are satisfied
     *  in the tree
     **/
    public void  RBInsertFixup(RedBlackNode z){
        //loop until two consecutive nodes in tree are of the color red
        //which violates RedBlack tree's property
        while(z.getP().getColor() == RedBlackNode.RED){
            //if z's parent is a left child of its grandparent
            if(z.getP() == z.getP().getP().getLc()){
                //y -> z's uncle
                RedBlackNode y = z.getP().getP().getRc();
                //if z's uncle is RED, push blackness from Grandparent
                if(y.getColor() == RedBlackNode.RED){
                    z.getP().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    z = z.getP().getP();
                }
                //if z's uncle is BLACK, ROTATE
                else{
                    //if zig-zig is identified
                    //left rotate z
                    if(z == z.getP().getRc()){
                        z = z.getP();
                        leftRotate(z);
                    }
                    z.getP().setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    //right rotate z's grandparent
                    rightRotate(z.getP().getP());
                }
            }
            //if z.parent is a right child of z.grandparent
            else {
                //y -> z's uncle
                RedBlackNode y = z.getP().getP().getLc();
                //if z's uncle is RED, push blackness from Grandparent
                if(y.getColor() == RedBlackNode.RED){
                    z.getP().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    z = z.getP().getP();
                }
                //if z's uncle is BLACK, ROTATE
                else{
                    //if zig-zig is identified
                    //right rotate z
                    if(z == z.getP().getLc()){
                        z = z.getP();
                        rightRotate(z);
                    }
                    z.getP().setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    //left rotate z's grandparent
                    leftRotate(z.getP().getP());
                }
            }
        }
        //enforce root as BLACK
        this.root.setColor(RedBlackNode.BLACK);
    }

    /**
     * Rotate a node to its left
     * @param x
     *      node that has to be rotated to its left
     * @precondition
     *  RedBlackTree has been initialized
     *  and node is present in the tree
     * @postcondition
     *  Node x has been rotated to its left
     **/
    public void leftRotate(RedBlackNode x){
        //ensure x can rotated to left
        if(x.getRc() != this.nil && this.root.getP() == this.nil){
            //y->x's right  child
            RedBlackNode y = x.getRc();
            //set x's right child as y's left child
            x.setRc(y.getLc());
            y.getLc().setP(x);
            //set y's parent as x's parent
            y.setP(x.getP());

            //if x was root -> make y as root
            if(x.getP() == this.nil)
                this.root = y;
            //if x is a left child
            //make y as left child of x's parent
            else if(x==x.getP().getLc())
                x.getP().setLc(y);
            //else make y as right child of x's parent
            else
                x.getP().setRc(y);

            //interchange x and y's position
            y.setLc(x);
            x.setP(y);
        }
    }

    /**
     * Rotate a node to its right
     * @param x
     *      node that has to be rotated to its right
     * @precondition
     *  RedBlackTree has been initialized
     *  and node is present in the tree
     * @postcondition
     *  Node x has been rotated to its right
     **/
    public void rightRotate(RedBlackNode x){
        //y->x's left  child
        RedBlackNode y =x.getLc();
        //set x's left child as y's right child
        x.setLc(y.getRc());
        y.getRc().setP(x);
        //set y's parent as x's parent
        y.setP(x.getP());

        //if x was root -> make y as root
        if(x.getP() == this.nil) this.root = y;
        //if x is a left child
        //make y as left child of x's parent
        else if(x == x.getP().getLc())
            x.getP().setLc(y);
        //else make y as right child of x's parent
        else
            x.getP().setRc(y);

        //interchange x and y's position
        y.setRc(x);
        x.setP(y);
    }

    /**
     * Print the RedBlack Tree's inorder traversal
     * @precondition
     *  RedBlackTree has been initialized
     * @postcondition
     *  The RedBlack tree is displayed with an in-order
     *  traversal.
     **/
    public void inOrderTraversal(){
        inOrderTraversal(this.root);
        System.out.println();
    }

    /**
     * Helper recursive function for inorder traversal
      * @param current
     *    node being currently processed
     */
    private void inOrderTraversal(RedBlackNode current){
        if(current == this.nil)
            return;
        inOrderTraversal(current.getLc());
        String color = current.getColor()==RedBlackNode.RED?"RED":"BLACK";
        System.out.println(current.getKey()+", "+color);
        inOrderTraversal(current.getRc());
    }

    /**
     * Helper function to return the total
     * count of nodes present in the tree
     */
    public int getCountNodes() {
        return countNodes;
    }

}
