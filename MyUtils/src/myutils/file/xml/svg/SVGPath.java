package myutils.file.xml.svg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import myutils.math.Vec2;
import myutils.misc.Pair;

public class SVGPath extends SVGElement {

	//upper case means absolute coordinates
	//lower case means relative coordinates to the previous cursor position. 

	//TODO support elliptical arc curves

	//command length in number of arguments
	private static HashMap<Character, Integer> cmdLength = new HashMap<Character, Integer>() {
		{
			put('M', 2);
			put('m', 2);
			put('L', 2);
			put('l', 2);
			put('H', 1);
			put('h', 1);
			put('V', 1);
			put('v', 1);
			put('Q', 4);
			put('q', 4);
			put('T', 2);
			put('t', 2);
			put('C', 6);
			put('c', 6);
			put('S', 4);
			put('s', 4);
			put('A', 7);
			put('a', 7);
			put('Z', 0);
			put('z', 0);
		}
	};

	private List<Pair<Character, List<Float>>> pathElements;
	private List<Vec2[]> cubicCurves;

	protected SVGPath(String content) {
		super(SVGElement.TYPE_PATH);

		//parse the content
		//convert all relative commands to absolute ones. 
		this.pathElements = new ArrayList<>();
		{
			//get rid of commas
			content = content.replace(',', ' ');

			int ptr = 0;

			//current position
			float cx = 0;
			float cy = 0;

			//subline start coords
			float sx = 0;
			float sy = 0;
			while (ptr != content.length()) {
				//traverse to next non-whitespace
				while (ptr != content.length() && Character.isWhitespace(content.charAt(ptr))) {
					ptr++;
				}
				if (ptr == content.length()) {
					break;
				}

				char parse_cmd = content.charAt(ptr);

				//look for next command character
				int r = ptr + 1;
				while (r != content.length() && !cmdLength.containsKey(content.charAt(r))) {
					r++;
				}

				//parse all coordinates belonging to current command
				String[] tokens = content.substring(ptr + 1, r).split(" ");
				int token_ptr = 0;
				while (token_ptr != tokens.length) {
					char cmd = parse_cmd;
					List<Float> coords = new ArrayList<>();
					for (int i = 0; i < cmdLength.get(cmd); i++) {
						try {
							Float val = Float.parseFloat(tokens[token_ptr + i]);
							coords.add(val);
						}
						catch (NumberFormatException e) {
							//something went wrong with parsing the int. 
						}
					}
					token_ptr += cmdLength.get(cmd);

					//if the current command is lowercase, convert it to an uppercase one
					switch (cmd) {
					case 'm': {
						float x0 = cx + coords.get(0);
						float y0 = cy + coords.get(1);
						coords = Arrays.asList(x0, y0);
						cmd = 'M';
						break;
					}

					case 'l': {
						float x0 = cx + coords.get(0);
						float y0 = cy + coords.get(1);
						coords = Arrays.asList(x0, y0);
						cmd = 'L';
						break;
					}

					case 'h': {
						float x0 = cx + coords.get(0);
						coords = Arrays.asList(x0);
						cmd = 'H';
						break;
					}

					case 'v': {
						float y0 = cy + coords.get(0);
						coords = Arrays.asList(y0);
						cmd = 'V';
						break;
					}

					case 'q': {
						float x0 = cx + coords.get(0);
						float y0 = cy + coords.get(1);
						float x1 = cx + coords.get(2);
						float y1 = cy + coords.get(3);
						coords = Arrays.asList(x0, y0, x1, y1);
						cmd = 'Q';
						break;
					}

					case 't': {
						float x0 = cx + coords.get(0);
						float y0 = cy + coords.get(1);
						coords = Arrays.asList(x0, y0);
						cmd = 'T';
						break;
					}

					case 'c': {
						float x0 = cx + coords.get(0);
						float y0 = cy + coords.get(1);
						float x1 = cx + coords.get(2);
						float y1 = cy + coords.get(3);
						float x2 = cx + coords.get(4);
						float y2 = cy + coords.get(5);
						coords = Arrays.asList(x0, y0, x1, y1, x2, y2);
						cmd = 'C';
						break;
					}

					case 's': {
						float x0 = cx + coords.get(0);
						float y0 = cy + coords.get(1);
						float x1 = cx + coords.get(2);
						float y1 = cy + coords.get(3);
						coords = Arrays.asList(x0, y0, x1, y1);
						cmd = 'S';
						break;
					}

					case 'a': {
						float rx = coords.get(0);
						float ry = coords.get(1);
						float angle = coords.get(2);
						float large_arc_flag = coords.get(3);
						float sweep_flag = coords.get(4);
						float x0 = cx + coords.get(5);
						float y0 = cy + coords.get(6);
						coords = Arrays.asList(rx, ry, angle, large_arc_flag, sweep_flag, x0, y0);
						cmd = 'A';
						break;
					}

					case 'z': {
						cmd = 'Z';
						break;
					}
					}

					//if the current command is T or S, convert it into Q or C respectively
					if (cmd == 'T') {
						//smooth quadratic bezier
						float c_x0 = cx;
						float c_y0 = cy;
						if (this.pathElements.size() != 0 && this.pathElements.get(this.pathElements.size() - 1).first == 'Q') {
							c_x0 = cx + (cx - this.pathElements.get(this.pathElements.size() - 1).second.get(0));
							c_y0 = cy + (cy - this.pathElements.get(this.pathElements.size() - 1).second.get(1));
						}
						float c_x1 = coords.get(0);
						float c_y1 = coords.get(1);
						coords = Arrays.asList(c_x0, c_y0, c_x1, c_y1);
						cmd = 'Q';
					}
					else if (cmd == 'S') {
						//smooth cubic bezier
						float c_x0 = cx;
						float c_y0 = cy;
						if (this.pathElements.size() != 0 && this.pathElements.get(this.pathElements.size() - 1).first == 'C') {
							c_x0 = cx + (cx - this.pathElements.get(this.pathElements.size() - 1).second.get(2));
							c_y0 = cy + (cy - this.pathElements.get(this.pathElements.size() - 1).second.get(3));
						}
						float c_x1 = coords.get(0);
						float c_y1 = coords.get(1);
						float c_x2 = coords.get(2);
						float c_y2 = coords.get(3);
						coords = Arrays.asList(c_x0, c_y0, c_x1, c_y1, c_x2, c_y2);
						cmd = 'C';
					}

					//if the current command is H or V, convert into L
					else if (cmd == 'H') {
						float x0 = coords.get(0);
						float y0 = cy;
						coords = Arrays.asList(x0, y0);
						cmd = 'L';
					}
					else if (cmd == 'V') {
						float x0 = cx;
						float y0 = coords.get(0);
						coords = Arrays.asList(x0, y0);
						cmd = 'L';
					}

					//if the current command is Z, convert into L
					//note: normally Z has special line cap handling compared to L, but this is fine for now. 
					else if (cmd == 'Z') {
						float x0 = sx;
						float y0 = sy;
						coords = Arrays.asList(x0, y0);
						cmd = 'L';
					}

					//update p0 position
					switch (cmd) {
					case 'M': {
						cx = coords.get(0);
						cy = coords.get(1);
						sx = cx;
						sy = cy;
						break;
					}

					case 'L': {
						cx = coords.get(0);
						cy = coords.get(1);
						break;
					}

					case 'Q': {
						cx = coords.get(2);
						cy = coords.get(3);
						break;
					}

					case 'C': {
						cx = coords.get(4);
						cy = coords.get(5);
						break;
					}

					case 'A': {
						cx = coords.get(5);
						cy = coords.get(6);
						break;
					}
					}

					//insert command into list
					this.pathElements.add(new Pair<>(cmd, coords));

					//there are no tokens associated with Z
					if (parse_cmd == 'Z' || parse_cmd == 'z') {
						break;
					}
				}
				ptr = r;
			}
		}

		//convert all commands into cubic bezier curves
		this.cubicCurves = new ArrayList<>();
		{
			float cx = 0;
			float cy = 0;
			for (int i = 0; i < this.pathElements.size(); i++) {
				List<Float> coords = pathElements.get(i).second;
				switch (pathElements.get(i).first) {
				//move
				case 'M': {
					cx = coords.get(0);
					cy = coords.get(1);
					break;
				}

				//line segment
				case 'L': {
					float x = coords.get(0);
					float y = coords.get(1);
					Vec2 c0 = new Vec2(cx, cy);
					Vec2 c1 = new Vec2(cx, cy);
					Vec2 c2 = new Vec2(x, y);
					Vec2 c3 = new Vec2(x, y);
					this.cubicCurves.add(new Vec2[] { c0, c1, c2, c3 });
					cx = x;
					cy = y;
					break;
				}

				//quadratic bezier
				case 'Q': {
					float x0 = cx;
					float y0 = cy;
					float x1 = coords.get(0);
					float y1 = coords.get(1);
					float x2 = coords.get(2);
					float y2 = coords.get(3);
					Vec2 c0 = new Vec2(x0, y0);
					Vec2 c1 = new Vec2(x0 + (2.0f / 3.0f) * (x1 - x0), y0 + (2.0f / 3.0f) * (y1 - y0));
					Vec2 c2 = new Vec2(x2 + (2.0f / 3.0f) * (x1 - x2), y2 + (2.0f / 3.0f) * (y1 - y2));
					Vec2 c3 = new Vec2(x2, y2);
					this.cubicCurves.add(new Vec2[] { c0, c1, c2, c3 });
					cx = x2;
					cy = y2;
					break;
				}

				//cubic bezier
				case 'C': {
					float x0 = cx;
					float y0 = cy;
					float x1 = coords.get(0);
					float y1 = coords.get(1);
					float x2 = coords.get(2);
					float y2 = coords.get(3);
					float x3 = coords.get(4);
					float y3 = coords.get(5);
					Vec2 c0 = new Vec2(x0, y0);
					Vec2 c1 = new Vec2(x1, y1);
					Vec2 c2 = new Vec2(x2, y2);
					Vec2 c3 = new Vec2(x3, y3);
					this.cubicCurves.add(new Vec2[] { c0, c1, c2, c3 });
					cx = x3;
					cy = y3;
					break;
				}

				//elliptical arc segment
				case 'A': {
					//TODO find a good approximation of an elliptical arc using cubic bezier
					break;
				}
				}
			}
		}
	}

	public List<Vec2[]> getCubicCurves() {
		return this.cubicCurves;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("Path : \n");
		for (int i = 0; i < pathElements.size(); i++) {
			char cmd = pathElements.get(i).first;
			res.append(cmd + " ");
			List<Float> coords = pathElements.get(i).second;
			for (int j = 0; j < coords.size(); j++) {
				res.append(coords.get(j));
				res.append(" ");
			}
			if (i != pathElements.size() - 1) {
				res.append("\n");
			}
		}
		return res.toString();
	}

}
