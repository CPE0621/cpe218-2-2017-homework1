
import javax.swing.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Stack;

public class Homework1 extends JPanel
        implements TreeSelectionListener {
    private JEditorPane htmlPane;
    private static String lineStyle = "Horizontal";
    public static Tree t;
    private static boolean playWithLineStyle = false;
    private JTree myJTree;
    public static String Out_Screen;
    private static boolean useSystemLookAndFeel = false;
    public Homework1() {
        super(new GridLayout(1,0));
        //Create the nodes.
        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode(t.root);
        HelloJTree(t.root,top);
        //Create a tree that allows one selection at a time.
        myJTree = new JTree(top);
        myJTree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        myJTree.addTreeSelectionListener(this);
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            myJTree.putClientProperty("JTree.lineStyle", lineStyle);
        }
        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(myJTree);
        //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        JScrollPane htmlView = new JScrollPane(htmlPane);
        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);
        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(500, 300));
        // Icon
        ImageIcon leafIcon = createImageIcon("middle.gif");
        if (leafIcon != null) {
            DefaultTreeCellRenderer renderer =
                    new DefaultTreeCellRenderer();
            renderer.setClosedIcon(leafIcon);
            renderer.setOpenIcon(leafIcon);
            myJTree.setCellRenderer(renderer);
        }
        add(splitPane);
    }
    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Homework1.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.out.println("Couldn't find file: "+ path);
            return null;
        }
    }
    public void Out_To_Screen(Node n)
    {
        Out_Screen = "";
        if(isOperator(n.key))
        {
            Out_Screen = inorder(n).substring(1,inorder(n).length()-1) + "=" + calculate(n);
        }else Out_Screen = n.key+"";
        htmlPane.setText(Out_Screen);
    }
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                myJTree.getLastSelectedPathComponent();
        if (node == null) return;
        Object nodeInfo = node.getUserObject();
        Out_To_Screen((Node)nodeInfo);
    }
    static public boolean isOperator(char x) {
        if (x == '+') {
            return true;
        } else if (x == '-') {
            return true;
        } else if (x == '*') {
            return true;
        } else if (x == '/') {
            return true;
        } else return false;
    }
    public static void HelloJTree(Node n, DefaultMutableTreeNode top){
        if(n.left!=null)
        {
            DefaultMutableTreeNode left=new DefaultMutableTreeNode(n.left);
            top.add(left);
            HelloJTree(n.left, left);
        }
        if(n.right!=null)
        {
            DefaultMutableTreeNode Right=new DefaultMutableTreeNode(n.right);
            top.add(Right);
            HelloJTree(n.right,Right);
        }
    }
    public static void main(String[] args) {
        // Begin of arguments input sample
        if (args.length > 0) {
            String input = args[0];
            Stack stack = new Stack();
            for (int i = 0; i < input.length(); i++) {
                char a = input.charAt(i);
                if (Character.isDigit(a)) {
                    // push
                    Node newNode = new Node(a);
                    stack.push(newNode);
                } else {
                    Node firstPopNode, secondPopNode;
                    firstPopNode = (Node) stack.pop();
                    secondPopNode = (Node) stack.pop();

                    Node newNode = new Node(a);
                    newNode.left = secondPopNode;
                    newNode.right = firstPopNode;
                    stack.push(newNode);
                }
            }
            t = new Tree((Node)stack.pop());
            String y = inorder(t.root).substring(1,inorder(t.root).length()-1) + "=" + calculate(t.root);
            System.out.println(y);
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }
        //Create and set up the window.
        JFrame frame = new JFrame("Binary Tree Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Add content to the window.
        frame.add(new Homework1());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    public static int calculate(Node n) {
        if (Character.isDigit(n.key)) {
            return Integer.parseInt(n.key + "");
        }
        else {
            switch (n.key) {
                case '+':
                    return calculate(n.left) + calculate(n.right);
                case '-':
                    return calculate(n.left) - calculate(n.right);
                case '*':
                    return calculate(n.left) * calculate(n.right);
                default:
                    return calculate(n.left) / calculate(n.right);
            }
        }
    }
    public static void infix(Node n) {
    }
    public static String inorder(Node n) {
        if(Character.isDigit(n.key)) {
            return n.key + "";
        }
        else {
            return "(" + inorder(n.left) + n.key + inorder(n.right) + ")";
        }
    }
    public static class Tree {
             Node root;

        public Tree(Node root) {
            this.root = root;
        }
    }
    public static class Node {
        char key;
        Node left;
        Node right;
        public String toString(){
            return key+"";
        }

        public Node(char key) {
            this.key = key;
        }
    }
}



