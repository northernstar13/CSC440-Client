import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import org.omg.CORBA.portable.OutputStream;


//client side of things

public class Driver
{
public static void main(String[] args) throws Exception
{
//simulating DoS... will open connection to destination host and infinitely request index.html

//connect to the server
Socket s = new Socket("localhost", 1234);
//inputStream associated with our socket; ability to read from the server
Scanner input = new Scanner(s.getInputStream());
//read from standard input, the keyboard; read in the answer from user
Scanner terminalInput = new Scanner(System.in);

//give us the ability to write back out to the server
PrintWriter output = new PrintWriter(s.getOutputStream(), true);
//the + s will show us that it came from 2 different socket connections
output.println("blah" + s);
//print out share/download question from the server;
System.out.println(input.nextLine());
//read in the answer
String theAnswer = terminalInput.nextLine();
//now we send it over the socket; write out over the server
output.println(theAnswer);
if(theAnswer.equals("share"))
{
File myFilesDir = new File("./myFiles");
String[] theFiles = myFilesDir.list();
int pos = 0;
//go through and spit out the files in the newly created local screen to the client
for(String fn : theFiles)
{
System.out.println(pos + ": " + fn);
pos++;
}
System.out.print("Which file would you like to share?");
theAnswer = terminalInput.nextLine();
System.out.println("You chose to share: " + theFiles[Integer.parseInt(theAnswer)]);
File theFile = new File("./myFiles/" + theFiles[Integer.parseInt(theAnswer)]);
// FileInputStream fis = new FileInputStream(theFile);
File theClone = new File("./myFiles/clone" + theFiles[Integer.parseInt(theAnswer)]);
FileOutputStream fos = new FileOutputStream(theClone);
FileInputStream fis = new FileInputStream(theFile);
System.out.println("num bytes: " + fis.available());
output.println(fis.read());
//new code


       ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
       ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
 
       
       
      
 
       
       byte [] buffer = new byte[100];
       Integer bytesRead = 0;
 
       while ((bytesRead = fis.read(buffer)) > 0) {
           oos.writeObject(bytesRead);
           oos.writeObject(Arrays.copyOf(buffer, buffer.length));
       }
 
       oos.close();
       ois.close();
       System.exit(0);    

   
/* 
       ObjectInputStream ois = new ObjectInputStream(s.getInputStream());  
       ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());  
 
       oos.writeObject(theFile.getName());  
 
       byte [] buffer = new byte[1000];  
       Integer bytesRead = 0;  
 
       while ((bytesRead = fis.read(buffer)) > 0) {  
           oos.writeObject(bytesRead);  
           oos.writeObject(Arrays.copyOf(buffer, buffer.length));  
       }  
 
       oos.close();  
       ois.close();  
*/
//end new code
//want to read in all of the bytes and display them to the screen
//going to read in fis.available number of bytes
//
while(fis.available() > 0)
{
//0-255 is the size of bytes; unsigned
//how do we convert 140 decimal to binary? 
//fis.read() will never give us anything >255 or <0 because it's UNSIGNED
fos.write(fis.read());
}
System.out.println("DONE");
output.println("DONE");
//close output stream otherwise it won't put end of file on there
fos.close();
}
}


}
