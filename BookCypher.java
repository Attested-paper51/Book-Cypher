package BookCypher;


import BasicIO.*;                // for IO classes
import static BasicIO.Formats.*; // for field formats
import static java.lang.Math.*;  // for math constants and functions
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.PrintStream;  //to write what is in the console to a new .txt file


/** This assignment randomly generates the code book and simulates an encoding and decoding process as proof of the concept of book ciphering.
  *  
  * @author <Muhammed Bilal>
  * 
  * @version 1.0 (<22/02/2021>)                                                        */

public class BookCypher {
  // instance variables
  int data;  //declares an int variable
  Node [] codeBook; //declares "codeBook" as a 1-D array of Node type
  char [] letters;  //declares "letters" as a 1-D array of Char type
  int [] numbers;  //declares "numbers" as a 1-D array of int type 
  private ASCIIDataFile in;  //to read from a .txt file
  private ASCIIDataFile input;  //to read from a .txt file
  public class Node {               //declares a class of creating a node
    public int item;  //need to be public
    public Node next;  //need to be public
    public Node (int numberPassed, Node nextNodePassed) {
      item = numberPassed;
      next = nextNodePassed;
    }
  }
  
  /** This constructor creates an array of 128 indices, with each node contained a header node
    *  fills nodes with numbers from 1 - 2000, where indices are chosen at random
       encrypts the text file "message.txt" by reading each character, converting it into int, passing the int as the index, and printing a random node
       decrypts the the encrypted file by doing the opposite of encryption. It reads the encryption file. collects each int, finds the int in the nodes,
       and prints the index in which the node is located in
       End result: Decryption.txt must be identical to message.txt*/
  
  public BookCypher ( ) {
    in = new ASCIIDataFile();  //selects .txt file to read from
    int counter = 0;
    int min = 0;     //lines 40-42 are variables used for the Math.random() number generator
    int max = 127;
    int range = max - min + 1;
    codeBook = new Node [128];  //initializes codeBook with 128 indices
    letters = new char [3282];  //initializes letters with 3282 indices
    numbers = new int [3282];  //initializes numbers with 3282 indices
    // statements   
    createEmptyArr(codeBook, counter);
    fillNodes(max, min);
    createBookCode();   
    createEncrypted();
    createDecrypted();
    
  }; // constructor
  
  
  //methods
  private void addTo(Node list, int aCode)  //allows you to insert a node at the end of the list (linkedlist)
  {                                                             //passed with the content of the new node to be the int aCode passed.
    Node p = list;
    while(p.next != null)
    {
      p = p.next;
    }
    p.next = new Node (aCode, null);
    list.item++;  //the item in the header node increases every time a single node is added in the linked list for a specific index.
    //System.out.print("  " + p.next.item);
  }
  
  private void encrypt(Node list, PrintStream output)  //allows you to access a specific index of codeBook array and print the item of a randomly selected node from the linked list.
  {                                                                            //passed with the header node in a specific index and a PrintStream variable for output
    int min = 1;
    int max = list.item;
    //System.out.print("Has " + list.item + " nodes | ");
    int range = max - min + 1;
    int r = (int)(Math.random() * (max - min + 1) + min); //generates a random number from 1 to list.item
    //System.out.print("node:" + r + " is chosen | ");
    
    Node p = list;
    for(int i=0; i<r; i++)
    {
      p = p.next; 
    }
    output.println(p.item);  //prints p.item in a .txt file
    //System.out.println("It contains the number:" + p.item);
  }
  
  private void decrypt(Node list, int number, int index, PrintStream output)  //reads a specific number from Encrypted.txt and prints the index it is located in, in a new file
  {                                                                                                            //passed with the header node, the number being searched, index of array, and a PrintStream variable
    Node p = list;
    //System.out.println(p.item);
    while(p.next != null)
    {
      //System.out.print(number);
      if(p.next.item == number)
      {
        char x = (char) index;
        if(index == 10)
        {
          output.print(x);  //prints index in a .txt file following a jump to the next line - will happen whenever a char from "message.txt" represents a new line
        }
        else
        {
          output.print(x + "");  //prints index in a .txt file
        }
        break;
      }
      else
      {
        p = p.next; 
      }
    }
  }
  
  private void createEmptyArr(Node [] codeBook, int counter)  //creates an array of 128 indices with only header nodes
  {                                                                                          //passed with the header node and counter representing the item inside it
    for(int i=0; i<codeBook.length; i++)
    {
      codeBook[i] = new Node (counter, null);
      //System.out.print(i);
      //System.out.println("  total: " + codeBook[i].item);
    }
  }
  
  private void fillNodes (int max, int min) //chooses a random index and uses addTo method to create a node containing the item of the iteration(1-2000) 
  {
    for(int j=1; j<=2000; j++)
    {
      int r = (int)(Math.random() * (max - min + 1) + min); //generates a random number from 0 to 127
      //System.out.print(r);
      addTo(codeBook[r], j);
      //System.out.println("  total: " + codeBook[r].item);
    }
  }
  
  private void createBookCode()  //creates a .txt file called "BookCode" that contains all the items from the fillNodes method
  {
    try 
    {
      PrintStream output = new PrintStream("BookCode.txt");  //a new .txt file is created
      
      for(int i=0; i<codeBook.length; i++)
      {
        System.out.print(i);
        Node p = codeBook[i];
        while(p.next != null)
        {
          p = p.next;
          data = p.item;
          output.print(data + "  ");  //prints the item in the linked lists
          System.out.print("  " + data); 
        }
        if(i != codeBook.length - 1)
        {
          output.println();
          System.out.println();
        }
      }
      output.close();
    }
    catch(Exception e)
    {
      e.getStackTrace(); 
    }
  }
  
  private void createEncrypted()  //reads each character from "message.txt", converts it into an int, uses "encrypt" method to access that int as an index to randomly select a node
  {
    try 
    {
      PrintStream output = new PrintStream("Encrypted.txt");
      
      for(int i=0; i<letters.length; i++)
      {
        letters[i] = in.readC();
        //System.out.print(letters[i] + " | index:");
        int x2 = (int) letters[i];
        //System.out.print(x2 + " | ");
        encrypt(codeBook[x2], output);
      }
      output.close();
    }
    catch(IOException e)
    {
      e.getStackTrace();
    }
  }
  
  private void createDecrypted()  //reads an int from "Encrypted.txt", calls "decrypt" to access it in the linked list and finds the index it is located in
  {
    try 
    {
      PrintStream output = new PrintStream("Decrypted.txt");
      input = new ASCIIDataFile();  //select .txt file to read from
      for(int i=0; i<numbers.length; i++)
      {
        numbers[i] = input.readInt(); 
        //System.out.println(numbers[i]);
        for(int j=0; j<codeBook.length; j++)
        {
          decrypt(codeBook[j], numbers[i], j, output);
        }
      }
      output.close();
    }
    catch(Exception e)
    {
      System.out.println("an error occurred");
      e.getStackTrace();
    }
  }
  
  public static void main ( String[] args )
  { 
    BookCypher s = new BookCypher();
  };
  
} // <COSC 1P03>