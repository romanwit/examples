package web2; 



import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static void main(String[] args) throws Throwable 
    {
    	
    	//wait at port 8080
        ServerSocket ss = new ServerSocket(8080);
        while (true) 
        {
    	
           	Socket s = ss.accept();
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }
    }

    //define input and output stream
    private static class SocketProcessor implements Runnable 
    {

        private Socket s;
        private InputStream is;
        private OutputStream os;

        private SocketProcessor(Socket s) throws Throwable 
        {
            this.s = s;
            this.is = s.getInputStream();
            this.os = s.getOutputStream();
        }

        //create response of server around variable Mes from readInputHeaders()
        public void run() 
        {
        	
        	//temp - generate files
        	/*        	
        	String ToW = "";
        	
        	for (int i=100; i<=199;++i)
        	 
        	{
        		ToW += "id=" + (i+1) + ", name=" + "name" + i + 
    				", gender=" + "robot" + ", grade=" + "5" + "\r\n";
        		 //ToW += "id=" + (i+1) + "\r\n";
        	}
        	
        	 try (FileWriter fw = new FileWriter("c:\\shit\\students1.txt"))
         	{
         		
         		fw.write(ToW);
         		fw.flush();         		
         	}
        	catch (Throwable t)
        	 {
        		
        	 }
        	 
        	 */
        	
        	/*
        	String ToW = "";
        	
        	for (int i=0; i<=999;++i)
        	 
        	{
        		ToW += "id=" + (i+1) + ", partition=" + Math.floor(i/100)+ "\r\n";
        		 
        	}
        	
        	 try (FileWriter fw = new FileWriter("c:\\shit\\ids.txt"))
         	{
         		
         		fw.write(ToW);
         		fw.flush();         		
         	}
        	catch (Throwable t)
        	 {
        		
        	 }

        	 */
          	 
            try {
                String Mes = readInputHeaders();
                writeResponse("<html><body><h1>" + Mes + "</h1></body></html>");
            } catch (Throwable t) {
                /*do nothing*/
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
            System.err.println("Client processing finished");
        }

        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: RomanServer/2016-06-03\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            os.write(result.getBytes());
            os.flush();
        }

        //parsing string like 'id=25" to find a value of field
        private String findField(String s, String fName)
        {
        	int iFN = s.indexOf(fName);
        	int iAmp = s.indexOf("&", iFN);
        	
        	String sF = "";
        	if (iFN>0)
        	{
        		if (iAmp!=-1)
        			sF = s.substring(iFN+fName.length()+1, iAmp);
        		else
        			sF=s.substring(iFN+fName.length()+1);
        	}
        	
        	sF = sF.replaceAll(" HTTP/1.1", "");
        	
        	return sF;
        }
        
        //write file partition_count.txt with quantities of records per partition, based on array partCount
        private boolean writePartCount(String partCountName, int[] partCount)
        {
        	boolean res = false;
        	String toW = "";
        	
        	for (int i=0;i<partCount.length;i++)
        	{
        		toW += "id=" + i + ", count=" + partCount[i] + "\r\n";
        	}
        	
        	try (FileWriter fw = new FileWriter(partCountName))
        	{
        		
        		fw.write(toW);
        		fw.flush();
        		res = true;
        	}
        	catch(Throwable t)
        	{
        		res = false;
        	}
        	
        	return res;
        }
        
        //most work here
        private String readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String res = "";
            
            String sID = "";
            String sName = "";
            String sGender = "";
            String sGrade = "";
            
            String basefileName = "C:\\test\\students";
            
                       
            boolean SuccessOp = false;
            
            String partCountName = "C:\\test\\partition_count.txt";            
                 
            int[] partCount = new int[10];
            String partLine = "";
            int currentPartion = -1;
            
            String idFileName = "c:\\test\\ids.txt";
            
            for (int i=0;i<partCount.length;++i)
            {
            
            	partLine = findField(findLine(partCountName, String.valueOf(i)), "count");
            	if (partLine != "")
            	{
            		partCount[i] = Integer.parseInt(partLine);
            	}
           		
            	if ((currentPartion==-1) && (partCount[i]<100))
            	{
            		currentPartion = i;
            	}
           
            }
            
            while(true) {
                String s = br.readLine();
                
                if ((s.indexOf("/students")!=-1) && (s.indexOf("GET /")!=-1)) 
                {
                
	                //we received command "/students/add?..."
                	sID = findField(s,"id");
                	if (s.indexOf("add")!=-1) 
	                {
	                	res = "Add";
	                
	                	sName = findField(s,"name");
	                	sGender = findField(s,"gender");
	                	sGrade = findField(s,"grade");
	                	
	                	res += ", id=" + sID + ", name=" + sName + ", gender=" + sGender + ", grade=" + sGrade;
	                	
	                	if (! findId(idFileName, sID))
	                	{
	                		
	                		
	                		if ((currentPartion==-1) ||  (((currentPartion==9) && (partCount[currentPartion]==100))))
	                		{
	                			//overflow
	                			res += "\r\nOVERFLOW";
	                		}
	                		else
	                		{
	                			//write to studentsX.txt, X is number of partition
		                		SuccessOp = writeID(basefileName + currentPartion + ".txt", "id=" + sID + ", name=" + sName + 
		                				", gender=" + sGender + ", grade=" + sGrade);
		                		//write to ids.txt
		                		if (! writeID(idFileName,"id="+sID+", partition="+currentPartion) )
		                			SuccessOp = false;
		                		//write to partition_count.txt
		                		partCount[currentPartion]++;
		                		if (partCount[currentPartion]==100)
		                		{
		                			currentPartion++;
		                		}
		                		if (! writePartCount(partCountName, partCount) )
		                			SuccessOp = false;
	                		}
	                	}
	                	else
	                	{
	                		SuccessOp = doUpdate(basefileName, idFileName, sID, sName, sGender, sGrade);
	                	}
	
	                }
	                
	                if (s.indexOf("update")!=-1) 
	                {
	                	res = "Update";
	                	if (findId(idFileName, sID))
	                	{
		                	sID = findField(s,"id");
		                	sName = findField(s,"name");
		                	sGender = findField(s,"gender");
		                	sGrade = findField(s,"grade");
		                	SuccessOp = doUpdate(basefileName, idFileName, sID, sName, sGender, sGrade);
	                			                	
	                	}
	                }
	                
	                if (s.indexOf("remove")!=-1) 
	                {
	                	res = "Remove";	                	
	                	SuccessOp = removeID(getFullFileName(idFileName, sID, basefileName), sID);
	                	String thisPartion = findField(findLine(idFileName, sID), "partition");
	                	if (! removeID(idFileName, sID))
	                		SuccessOp = false;
	               	
	                	partCount[Integer.parseInt(thisPartion)]--;
	                	if (! writePartCount(partCountName, partCount))
	                		SuccessOp = false;
                		
	                }
	                
	                if (s.indexOf("show")!=-1) 
	                {
	                	
	                	String temp = findLine(getFullFileName(idFileName, sID, basefileName), sID);
	                	
	                	res ="SHOW:" + temp;
	                	if (temp!="")
	                	{
	                		SuccessOp = true;
	                	}
	                }
	                

	                res += "\r\nOperation Success = " + SuccessOp;
	                
	            
                }
                
                if(s == null || s.trim().length() == 0) 
                {                	
                    break;
                }
            }
            
            return res;
        }
        
        //get complete file name with students, with number of partion in filename
        private String getFullFileName(String idFileName, String sID, String baseFileName)
        {
        	String res = "";
        	String currentPartion = findField(findLine(idFileName, sID), "partition");
        	
        	if (currentPartion != "")
        		res = baseFileName + currentPartion + ".txt";
        	
        	        	
        	return res;
        }
        
        //update record in file with students
        private boolean doUpdate(String basefileName, String idFileName, String sID, String sName, String sGender, String sGrade)
        {   
			boolean res = false;        	
			
			String fileName = getFullFileName(idFileName, sID, basefileName);
						
			if (! removeID(fileName, sID)) 
				{return res;}
			
			if (writeID(fileName, "id=" + sID + ", name=" + sName + 
					", gender=" + sGender + ", grade=" + sGrade))
				{res = true;}
			
			
			return res;
        }
        
        //reading all content of file (fileName)
        private String getFileContent(String fileName) 
        {
        	String res = "";
        	
        	try (FileReader fr = new FileReader(fileName))
        	{
        		int c;
        		while ((c=fr.read())!=-1)
        		{
        			res += (char)c;
        		}
        	}
        	catch (Throwable t)
        	{
        		
        	}
        	
        	
        	return res;
        	
        }
        
        //check, do id exist in file with students or not
        private boolean findId(String fileName, String id)
        {
        	boolean res = false;
        	String content = getFileContent(fileName);       	
        	
        	if (content!="")
        	{
        		if (content.indexOf(id+",")!=-1)
        			res = true;
        	}
        	
        	return res;
        }
        
        //write student to file
        private boolean writeID(String fileName, String toWrite)
        {
                	
        	boolean res = false;
        	
        	try (FileWriter fw = new FileWriter(fileName, true))
        	{
        		
        		fw.write(toWrite+"\r\n");
        		fw.flush();
        		res = true;
        	}
        	catch(Throwable t)
        	{
        		res = false;
        	}
        	
        	return res;
        	
        	
        }
        
        //remove one line from file
        private boolean removeID(String fileName, String ID)
        {
         	boolean res = false;
        	String content = getFileContent(fileName);
        	
        	int i1 = content.indexOf("id="+ID+",");
        
        	if (i1==-1)
        	{
        		return res;
        	}
        	
        	int i2 = content.indexOf("\r\n", i1);
        	
        	content = content.substring(0, i1) + content.substring(i2+2);
        	
        	        	
        	try (FileWriter fw = new FileWriter(fileName))
        	{
        		fw.write(content);
        		fw.flush();
        		res = true;
        	}
        	catch(Throwable t)
        	{
        		
        	}
        	
        	return res;
        }
        
        //return whole record from file with students (as string)
        private String findLine(String fileName, String id)
        {
        	String res = "";
        	String content = getFileContent(fileName);
        	
        	if (content=="")
        	{
        		return res;
        	}
        	
        	int i1 = content.indexOf("id="+id+",");
        	        	
        	if (i1==-1)
        	{
        		return res;
        	}
        	
        	int i2 = content.indexOf("\r\n", i1);
        	
        	return content.substring(i1,i2);
        }
    }
}