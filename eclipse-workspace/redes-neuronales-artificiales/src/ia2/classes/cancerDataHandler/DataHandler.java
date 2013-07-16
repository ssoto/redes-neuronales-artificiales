package ia2.classes.cancerDataHandler;

import java.awt.List;
import java.io.File;
import java.util.Iterator;

import javax.activation.FileDataSource;

import ia2.interfaces.Problem_data;

public class DataHandler implements Problem_data{
	
	private File example_file;
	
	public DataHandler() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Iterator<List> iterator() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public void read_examples(String file_name) {
		try{
			this.example_file = new File(file_name);
		}
		catch (Exception e) {
			System.out.println("Imposible leer el ejemplo: " + file_name);
		}
	}

	@Override
	public Integer get_input_size() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get_input_type() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer get_ouput_size() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get_output_type() {
		// TODO Auto-generated method stub
		return null;
	}

}
