import java.io.*;
import java.util.*;
import java.lang.*;

class Node{
    String hashtag;
    int key;
    int degree;
    Node parent, child, left, right;
    int child_cut;

    //constructor for Node
    Node(int key, String hashtag) {
        this.right=this;
        this.left=this;
        this.key=key;
        this.degree=0;
        this.hashtag=hashtag;
        this.parent=null;
        this.child=null;
        this.child_cut=0;
    }

    Node(){

    }
}

class keywordcounter{
    public static void main(String[] args){
        
        FibonacciHeap fibheap=new FibonacciHeap();
        HashMap<String,Node> map=new HashMap<>();
    
        File file_input=new File(args[0]);
        file_input.setReadable(true);
        
        String pathname="output_file.txt";
        File file_output=new File(pathname);
        Scanner sc=null;
        
        BufferedWriter writer=null;
        try{
            
            sc=new Scanner(file_input);
            writer=new BufferedWriter(new FileWriter(file_output));
            while(sc.hasNextLine()){
                String command=sc.nextLine();
                String arr[]=command.split(" ");
               
                if (command.substring(0,1).equals("$")){        //adding hashtags
                    String hashtag=arr[0].substring(1);
                    int key=Integer.parseInt(arr[1]);
                    if (map.containsKey(hashtag)){   
                        Node x=map.get(hashtag);
                        fibheap.increase_key(x,x.key+key);
                    }
                    else{
                        Node new_node=new Node(key, hashtag);
                        map.put(hashtag,new_node);
                        fibheap.insert(new_node);
                    }
                }
                else if(command.matches("\\d+")){       // condition to check for deleting entries input
                
                    int n=Integer.parseInt(arr[0]);     //extract max and write to file
                   
                    ArrayList<Node> removed=new ArrayList<Node>(n);
                    
                    for (int i=0; i<n; i++){
                        String ans=fibheap.max.hashtag;
                        if (i==n-1){
                            writer.write(ans);
                        }
                        else{
                            writer.write(ans+",");
                        }
                        int key=fibheap.max.key;
                        fibheap.delete_max();
                        map.remove(ans);
                        Node t=new Node(key,ans);
                        removed.add(i,t);
                    }
                    
                    for (int i=0; i<n; i++){
                        Node x=removed.get(i);
                        map.put(x.hashtag,x);
                        fibheap.insert(x);
                    }
    
                    writer.newLine();
                    
                }
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e3) {
            e3.printStackTrace();
        }
    
        finally {
            if (writer!=null) {
                try {
                    writer.close();
                    sc.close();
                } catch (IOException e2) {
                     e2.printStackTrace();
                }
            }
        }
    }
    

}


class FibonacciHeap
{

    Node max;
    int no_of_nodes;
    private static final double val=1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);

    FibonacciHeap(){            //contructor for new heap
        this.max=null;
        no_of_nodes=0;
    }

    //function to remove from circular linked list
    public void remove_fromlist(Node node){
        node.left.right=node.right;
        node.right.left=node.left;
    }

    //function to insert a new node
    public void insert(Node node)
    {
        
        if (max==null) {                //check if max node is null
            max=node;            
        } 
        else{
            node.left=max;              //insert to right of max
            node.right=max.right;
            max.right=node;

            if(node.right!=null){                               
                node.right.left=node;
            }

            if (node.right==null)
            {
                node.right=max;
                max.left=node;
            }

            if (node.key>max.key) {
                max=node;              //update max if new node is greater
            }
        }
        no_of_nodes++;
    }

    //function to cut child node from parent
    public void cut(Node node, Node parent)
    {
         
        if(node==node.right){
            parent.child=null;  
        } 
        remove_fromlist(node);
        parent.degree--;

        if(node==parent.child){       // reset child if necessary
            parent.child=node.right;
        }

        if(parent.degree==0){
            parent.child=null;
        }
        
        add_tolist(max,node);   // add x to root list of heap

        node.child_cut=0;      // set child cut to false
        node.parent=null;      //remove parent

    }

    //function to add to root list
    public void add_tolist(Node max, Node node){
        node.left=max;
        node.right=max.right;
        max.right=node;
        node.right.left=node;
    }

    //function to increase the value of key for the given hashtag
    public void increase_key(Node node, int key)
    {
        node.key=key;
        Node parent=node.parent;

        if((parent!=null) && (node.key>parent.key)){
            cut(node,parent);
            cascading_cut(parent);
        }
        
        if(node.key>max.key){
            max=node;
        }
    }

    
    //function to link child and parent
    public void link(Node child, Node parent)
    {
        remove_fromlist(child);      // remove child from root list of heap
        child.parent = parent;

        if(parent.child!=null){
            child.left=parent.child;
            child.right=parent.child.right;
            parent.child.right=child;
            child.right.left=child;
        } 
        else{
            parent.child=child;
            child.right=child;
            child.left=child;
        }
        if(child.key>(parent.child).key){
            parent.child=child; //point to larger child
        }
        child.child_cut=0;     // make mark of y as false
        parent.degree++;        // increase degree of x by 1
    }

    //function to perform cascading cuts based on child cut value 
    public void cascading_cut(Node node)
    {
        Node parent=node.parent;
        
        if(parent!=null){                 // if there is a parent
            if(node.child_cut==1){
                cut(node, parent);
                cascading_cut(parent); 
            } 
            else{
                node.child_cut=1;
            }
        }
    }

    //function to delete the maximum from the heap
    public void delete_max()
    {
        Node temp_max=max;
        if(temp_max!=null){
            int no_of_children=temp_max.degree;
            Node right;
            Node child=temp_max.child;
          
            for(int i=0; i<no_of_children; i++){          //for all the children of max
                right=child.right;

                remove_fromlist(child);     // remove child from child list
                add_tolist(max,child);     //add child to root list of heap
        
                child.parent=null;    //make parent null
                child=right;
                
            }

            remove_fromlist(temp_max);

            if(temp_max!=temp_max.right){
                max=temp_max.right;
                combine();
            }
            else{
                max=null;
            }
           no_of_nodes--;
       }
    }

    //function to combine 2 trees of same degree after delete_max
    public void combine()
    {
        int n=((int) Math.floor(Math.log(no_of_nodes)*val))+1;
        Node temp=max;
        int no_of_roots=0;
        List<Node> degree_to_node=new ArrayList<Node>(n);

        if(temp!=null){             //To get the number of root nodes
            temp=temp.right;           //exchanged these
            no_of_roots++;                     
            while (temp!=max){
                temp=temp.right;
                no_of_roots++;
            }
        }

        for(int i=0; i<n; i++){
            degree_to_node.add(null);
        }

        Node x=temp;

        // For each node in root list 
        for(int i=0; i<no_of_roots; i++){
            Node neighbour=x.right;
            int degree=x.degree;

            // check for duplicate degree
            while(degree_to_node.get(degree)!=null){
                Node y=degree_to_node.get(degree);
                if(x.key<y.key){
                    Node temp1=x;
                    x=y;
                    y=temp1;
                }
                if (y==max){
                    max=x;
                } 
                link(y, x);      //make y the child of x
                if (x.right==x){
                    max=x;
                }
                degree_to_node.set(degree, null);      //set the degree to null as x and y are combined now
                degree++;
            }
            degree_to_node.set(degree, x);
            x=neighbour;
        }
        
        max=null;      //Deleting the max node

        for(int i=0; i<n; i++){                 // combine entries of the degree table
            Node y=degree_to_node.get(i);
            if(y==null){
                continue;
            }

            if(max==null){
                max=y;
            } 
            else{
                remove_fromlist(y);   //Remove node from root list
                add_tolist(max, y);   // Add to root list
                
                if (y.key>max.key) {
                    max=y;            //Update max
                }                
            }
        }
    }

}