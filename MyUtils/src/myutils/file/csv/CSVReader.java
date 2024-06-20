package myutils.file.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Stack;

public class CSVReader {
	//reads the top row as the headers, and the rest of the stuff goes into data

	//the data array might not be well formed, as data[i].length == data[j].length for i != j might not be true. 
	//this depends on the formatting of the csv. 

	private String[] header;
	private String[][] data;

	//if true, will strip the whitespace off of the front and back of every entry in headers and data. 
	private boolean stripWhitespace = true;

	//if false, will instead treat the header as the first row of data. 
	private boolean readHeader = true;

	private boolean transpose = false;

	public CSVReader(File file) throws IOException {
		this.init();
		this.readFileAsCSV(file);
	}

	public CSVReader() {
		this.init();
	}

	private void init() {
		//nothing for now :P
	}

	public void readFileAsCSV(File file) throws IOException {
		//read in stuff from csv
		BufferedReader fin = new BufferedReader(new FileReader(file));
		ArrayList<String[]> data_list = new ArrayList<>();
		while (true) {
			String line = fin.readLine();
			if (line == null) {
				break;
			}
			data_list.add(line.split(","));
		}
		fin.close();

		if (this.stripWhitespace) {
			for (int i = 0; i < data_list.size(); i++) {
				for (int j = 0; j < data_list.get(i).length; j++) {
					data_list.get(i)[j] = data_list.get(i)[j].strip();
				}
			}
		}

		if (this.transpose) {
			ArrayList<String[]> t_list = new ArrayList<>();

			int max_c = 0;
			for (int i = 0; i < data_list.size(); i++) {
				max_c = Math.max(max_c, data_list.get(i).length);
			}

			for (int i = 0; i < max_c; i++) {
				String[] arr = new String[data_list.size()];
				for (int j = 0; j < data_list.size(); j++) {
					arr[j] = data_list.get(j)[i];
				}
				t_list.add(arr);
			}

			data_list = t_list;
		}

		//put arrays into queue, so we can quickly move them into array form
		Queue<String[]> data_q = new ArrayDeque<>();
		for (int i = 0; i < data_list.size(); i++) {
			data_q.add(data_list.get(i));
		}

		this.header = null;
		this.data = null;

		if (this.readHeader) {
			this.header = data_q.peek();
			data_q.poll();
		}

		//the remaining rows are data
		this.data = new String[data_q.size()][];
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = data_q.poll();
		}
	}

	public void setStripWhitespace(boolean b) {
		this.stripWhitespace = b;
	}

	public void setReadHeader(boolean b) {
		this.readHeader = b;
	}

	public void setTranspose(boolean b) {
		this.transpose = b;
	}

	public String[] getHeader() {
		return this.header;
	}

	public String[][] getData() {
		return this.data;
	}

}
