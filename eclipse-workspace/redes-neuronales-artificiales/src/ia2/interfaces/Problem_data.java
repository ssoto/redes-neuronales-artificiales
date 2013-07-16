package ia2.interfaces;

import java.awt.List;
import java.io.File;


public interface Problem_data extends Iterable<List>{
	
	public void read_examples(String file_name);
	
	public Integer get_input_size();
	public String get_input_type();
	
	public Integer get_ouput_size();
	public String get_output_type();
	
	
	

}
