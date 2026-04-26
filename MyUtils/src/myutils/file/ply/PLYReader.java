package myutils.file.ply;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class PLYReader {
	// PLY file consists of header and payload. 
	
	// header always comes first and is whitespace separated plaintext 
	// it describes the information in the payload. 
	// currently, the parser only supports these types of header lines:
	// - ply ; always the first line of the header
	// - format <fmt> <version> ; tells you how the payload is formatted
	// - element <name> <count> ; tells you there is <count> of this element in the payload
	// - property <type> <name> ; tells you this property exists for the most recent element in the header.
	// - comment <plaintext> ; human readable comment text
	// - end_header ; always the last line of the header
	
	// directly after the header ends, the payload begins. 
	
	private PLYHeader header = null;
	private PLYPayload payload = null;
	
	public PLYReader(File file) throws IOException {
		this.readFileAsPLY(file);
	}
	
	public void readFileAsPLY(File file) throws IOException {
		BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
		PLYHeader _header = this.parseHeader(fin);
		PLYPayload _payload;
		switch(_header.format) {
		case "binary_little_endian":
			_payload = this.parsePayloadLittleEndian(fin, _header);
			break;
			
		default: 
			throw new IOException("unrecognized format : " + _header.format);
		}
		fin.close();
		
		this.header = _header;
		this.payload = _payload;
	}
	
	private String parseLine(BufferedInputStream fin) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    while(true) {
	        int b = fin.read();
	        if(b == -1) {
	            if (sb.length() == 0) {
	                return null; 		//EOF, no more lines
	            }
	            return sb.toString(); 	//last line without trailing newline
	        }
	        if(b == '\n') {
	            return sb.toString();	//end of line
	        }
	        if(b == '\r') {
	            continue; 				//ignore CR to handle "\r\n"
	        }
	        sb.append((char) b);
	    }
	}
	
	private PLYHeader parseHeader(BufferedInputStream fin) throws IOException {
		String magic = this.parseLine(fin);
		if(!magic.equals("ply")) {
			throw new IOException("not a ply file");
		}
		
		String format = null;
		String version = null;
		ArrayList<PLYElementHeader> elements = new ArrayList<>();
		ArrayList<String> comments = new ArrayList<>();
		
		String element_name = null;
		int element_count = -1;
		ArrayList<PLYPropertyHeader> element_properties = null;
		while(true) {
			String line = this.parseLine(fin);
			
			//see if header is done
			if(line.equals("end_header")) {
				//add trailing element
				if(element_name != null) {
					if(element_properties.size() == 0) {
						throw new IOException("element with 0 properties");
					}
					elements.add(new PLYElementHeader(element_name, element_count, element_properties));
				}				

				break;
			}
			
			//parse header line
			StringTokenizer st = new StringTokenizer(line);
			String type = st.nextToken();
			switch(type) {
			case "format": {
				if(format != null) {
					throw new IOException("more than one format line");
				}
				format = st.nextToken();
				version = st.nextToken();
				break;
			}
			
			case "element": {
				if(element_name != null) {
					if(element_properties.size() == 0) {
						throw new IOException("element with 0 properties");
					}
					elements.add(new PLYElementHeader(element_name, element_count, element_properties));
				}
				element_name = st.nextToken();
				element_count = Integer.parseInt(st.nextToken());
				element_properties = new ArrayList<>();
				break;
			}
			
			case "property": {
				String property_type = st.nextToken();
				String property_name = st.nextToken();
				element_properties.add(new PLYPropertyHeader(property_type, property_name));
				break;
			}
			
			case "comment": {
				StringBuilder sb = new StringBuilder();
				while(st.hasMoreTokens()) {
					sb.append(st.nextToken());
					if(st.hasMoreTokens()) sb.append(" ");
				}
				comments.add(sb.toString());
				break;
			}
			
			default: 
				throw new IOException("unexpected header line : " + type);
			}
			
			if(st.hasMoreTokens()) {
				throw new IOException("malformed header line, has extra tokens : " + type);
			}
		}
		
		if(format == null) {
			throw new IOException("missing format line");
		}
		
		return new PLYHeader(format, version, elements, comments);
	}
	
	private void readFully(BufferedInputStream fin, byte[] data, int off, int amt) throws IOException {
		int read_amt = fin.read(data, off, amt);
		if(read_amt != amt) {
			throw new IOException("unexpected EOF");
		}
	}
	
	private PLYPayload parsePayloadLittleEndian(BufferedInputStream fin, PLYHeader _header) throws IOException {
		ArrayList<PLYElement> elements = new ArrayList<>();
		for(int i = 0; i < _header.elements.size(); i++) {
			PLYElementHeader element_header = _header.elements.get(i);
			
			//allocate storage for properties
			byte[][] data = new byte[element_header.properties.size()][];
			for(int j = 0; j < element_header.properties.size(); j++) {
				PLYPropertyHeader property_header = element_header.properties.get(j);
				switch(property_header.type) {
				case "float":
					data[j] = new byte[4 * element_header.count];
					break;
					
				default: 
					throw new IOException("unrecognized property type : " + property_header.type);
				}
			}
			
			//parse properties
			for(int j = 0; j < element_header.count; j++) {
				for(int k = 0; k < element_header.properties.size(); k++) {
					PLYPropertyHeader property_header = element_header.properties.get(k);
					switch(property_header.type) {
					case "float":
						this.readFully(fin, data[k], j * 4, 4);
						break;
						
					default: 
						throw new IOException("unrecognized property type : " + property_header.type);
					}
				}
			}
			
			//create structs
			HashMap<String, PLYProperty> properties = new HashMap<>();
			for(int j = 0; j < element_header.properties.size(); j++) {
				PLYPropertyHeader property_header = element_header.properties.get(j);
				properties.put(property_header.name, new PLYProperty(data[j]));
			}
			elements.add(new PLYElement(element_header.count, properties));
		}
		return new PLYPayload(elements);
	}
	
	public void printHeader() {
		System.out.println("Format : " + header.format + ", Version : " + header.version);
		for(int i = 0; i < this.header.elements.size(); i++) {
			PLYReader.PLYElementHeader element = this.header.elements.get(i);
			System.out.println("Element : " + element.name + ", Count : " + element.count);
			for(int j = 0; j < element.properties.size(); j++) {
				PLYReader.PLYPropertyHeader property = element.properties.get(j);
				System.out.println("    Property : " + property.name + ", Type : " + property.type);
			}
		}
	}
	
	public PLYHeader getHeader() {
		return this.header;
	}
	
	public PLYPayload getPayload() {
		return this.payload;
	}
	
	
	public class PLYHeader {
		public final String format;		//original format of payload
		public final String version;
		public final ArrayList<PLYElementHeader> elements;
		public final ArrayList<String> comments;
		public PLYHeader(String _format, String _version, ArrayList<PLYElementHeader> _elements, ArrayList<String> _comments) {
			this.format = _format;
			this.version = _version;
			this.elements = _elements;
			this.comments = _comments;
		}
	}
	
	public class PLYElementHeader {
		public final String name;
		public final int count;
		public final ArrayList<PLYPropertyHeader> properties;
		public PLYElementHeader(String _name, int _count, ArrayList<PLYPropertyHeader> _properties) {
			this.name = _name;
			this.count = _count;
			this.properties = _properties;
		}
	}
	
	public class PLYPropertyHeader {
		public final String type;
		public final String name;
		public PLYPropertyHeader(String _type, String _name) {
			this.type = _type;
			this.name = _name;
		}
	}
	
	public class PLYPayload {
		public final ArrayList<PLYElement> elements;
		public PLYPayload(ArrayList<PLYElement> _elements) {
			this.elements = _elements;
		}
	}
	
	public class PLYElement {
		public final int count;
		public final HashMap<String, PLYProperty> properties;
		public PLYElement(int _count, HashMap<String, PLYProperty> _properties) {
			this.count = _count;
			this.properties = _properties;
		}
	}
	
	public class PLYProperty {
		public final byte[] data;
		public PLYProperty(byte[] _data) {
			this.data = _data;
		}
		
		public byte[] getData() {
			return this.data;
		}
		
		public float getFloat(int ind) {
			//float stored as little endian
		    int bits =
	            ((data[ind * 4 + 0] & 0xFF) << 0)  |
	            ((data[ind * 4 + 1] & 0xFF) << 8)  |
	            ((data[ind * 4 + 2] & 0xFF) << 16) |
	            ((data[ind * 4 + 3] & 0xFF) << 24)
		    ;
		    return Float.intBitsToFloat(bits);
		}
		
		public int getInt(int ind) {
			//int stored as little endian
		    int bits =
	            ((data[ind * 4 + 0] & 0xFF) << 0)  |
	            ((data[ind * 4 + 1] & 0xFF) << 8)  |
	            ((data[ind * 4 + 2] & 0xFF) << 16) |
	            ((data[ind * 4 + 3] & 0xFF) << 24)
		    ;
		    return bits;
		}
	}
}
