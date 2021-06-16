// BINARY SEARCH TREE CLASS
// based off of https://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/ with some alterations


public class BST {

	class Node	{
		String data;
		String lines;
		Node left, right;
		
		public Node(String d, String lineNumber)	{
			data = d;
			left = right = null;
			lines = lineNumber;
		}
	}
	
	// root of binary search tree
	private Node root;
	
	// constructor
	BST()	{
		root = null;
	}
	
	// insert a new node
	void insert(String key, String lineNumber)	{
		root = insertRec(root, key, lineNumber);
	}
	
	// find where to insert the new node
	Node insertRec(Node root, String d, String lineNumber)	{
	
		// if tree is empty, return a new node
		if(root == null) {
			root = new Node(d, lineNumber);
			return root;
		}
		
		// else recursive calls down the tree
		if(d.compareTo(root.data) < 0)	{
			root.left = insertRec(root.left, d, lineNumber);
		}
		
		else if(d.compareTo(root.data) > 0)	{
			root.right = insertRec(root.right, d, lineNumber);
		}
		
		// if the current word matches a word already in the tree
		else if(d.compareTo(root.data) == 0 )	{
			// append the different line numbers if needed
			root.lines += " " + lineNumber;
		}
		
		return root;
		
	}
	
	// used to call inorderRec mainly
	void inorder()	{
		inorderRec(root);
	}
	
	// print tree in order
	void inorderRec(Node root)	{
		
		if(root != null) {
			inorderRec(root.left);
			System.out.println(root.data + "\t" + root.lines);
			inorderRec(root.right);
		}
		
	}
	
}
