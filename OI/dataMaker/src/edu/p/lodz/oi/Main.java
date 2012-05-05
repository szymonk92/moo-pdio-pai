package edu.p.lodz.oi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.filechooser.FileFilter;


public class Main {
	
	public static void main(String[] args) {
		
		FileInputStream fin=null;
		FileInputStream finXml=null;
		File fout = null;
		
		
		if ( args.length < 1) {
			System.err.println("Needed args are :\n"+
								"\t input.png\n"+
								"\t input.png.con.xml(optional)"+
								"\t out/ (opional - outputdir)");
			return;
		}
		
		
		//PARSE ARGUMENTS
		try {
			
			//judge is it directory or single file
			File input = new File(args[0]);
			File[] files  = null;
			if ( input.isDirectory()) {
				files  = input.listFiles(new FilenameFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
//						System.out.println(name);
					String[] namesplit=  name.split("\\.");
						return namesplit[namesplit.length-1].equalsIgnoreCase("png");
					}
				});
			} else {
				files = new File[1];
				files[0] = input;
			}
			
			
			switch ( args.length) {
			case 1:
				fout = new File("./output/");
				if ( input.isFile())
					finXml = new FileInputStream(args[0]+".con.xml");
				
				break;
			case 2: 
				if ( args[1].contains("xml") ) {
					if ( input.isFile())
						finXml = new FileInputStream(args[1]);
					fout = new File("./ouput/");
				}
				else { 
					fout = new File(args[1]);
					if ( input.isFile())
						finXml = new FileInputStream(args[0].split(".")[0]+".con.xml");
				}
				break;
			case 3: 
				if ( input.isFile())
					finXml = new FileInputStream(args[1]);
				fout = new File(args[2]);
				break;
				
			default :
				System.out.println("bad args :"+args.toString());
				break;
				
			}
			
			
			if ( ! fout.exists() )
				fout.mkdir();
			
			
		
			
			int n=0,o=0,i=0,z=0;
			for ( int j =0; j< files.length; ++j) {
				fin = new FileInputStream(files[j]);
				//RUN SignWrapper
				finXml = new FileInputStream(files[j].getAbsolutePath()+".con.xml");
				SignWrapper sw = new SignWrapper(fin, fout, finXml);
				sw.counter=n;
				sw.getSigns("znak nakazu", "nakaz");
				n=sw.counter;
				sw.counter=0;
				sw.counter=o;
				sw.getSigns("znak ostrzegawczy", "ostrzegawczy");
				o=sw.counter;
				sw.counter=0;
				sw.counter=i;
				sw.getSigns("znak informacyjny", "informacyjny");
				i=sw.counter;
				sw.counter=0;
				sw.counter=z;
				sw.getSigns("znak zakazu","zakaz");
				z=sw.counter;
				sw.counter=0;
				
				fin.close();
			}	
		
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

}
