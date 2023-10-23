package myutils.file.xml;

import java.util.Stack;

public class XMLReader {

	private static XMLNode parseElementHeader(String s) {
		//[\s=]+(?=([^"]*"[^"]*")*[^"]*$)
		//the regex above will find any whitespace or equals sign that is not contained within quotes. 
		//i don't support mixing single and double quotes for now.

		//this is really jank. 
		String regex = "[\\s=]+(?=([^\']*\'[^\']*\')*[^\']*$)";
		if (s.indexOf('\"') != -1) {
			regex = "[\\s=]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
		}

		String[] tokens = s.split(regex);

		String name = tokens[0];
		XMLNode node = new XMLNode(name);

		if (name.equals("DOCTYPE")) {
			for (int i = 1; i < tokens.length; i++) {
				node.addContent(tokens[i]);
			}
			return node;
		}

		//i assume that everything in the header is an attribute. 
		for (int i = 1; i < tokens.length; i += 2) {
			String attrName = tokens[i];
			String content = tokens[i + 1].substring(1, tokens[i + 1].length() - 1);
			node.addAttribute(attrName, content);
		}

		return node;
	}

	//the reserved characters are going to be '<' and '>'. We need these to determine when a tag starts and ends. 
	//if the reserved characters are used anywhere in the text, this will break. 

	//also, it's assumed that tags are closed properly. This doesn't detect xml syntax errors. 
	public static XMLNode parseStringAsXML(String s) {
		XMLNode root = new XMLNode("root");
		Stack<XMLNode> nodeStack = new Stack<>();
		nodeStack.push(root);

		for (int i = s.indexOf('<'); i < s.length(); i++) {
			if (s.charAt(i) != '<') {
				//parse everything until the next '<' as a content string for the current node
				int r = i;
				while (r != s.length() && s.charAt(r) != '<') {
					r++;
				}
				if (r == s.length()) {
					//reached the end of the file, this string probably isn't contained within anything. 
					break;
				}
				String text = s.substring(i, r);
				if (!text.isBlank()) {
					nodeStack.peek().addContent(text);
				}
				i = r - 1;
				continue;
			}

			//find the next '>' char
			int tl = i;
			int tr = i;
			while (s.charAt(tr) != '>') {
				tr++;
			}
			tr++;
			int cl = tl;
			int cr = tr;
			cl++;
			cr--;
			if (s.charAt(cl) == '?') {
				//prolog tag
				cl++;
				cr--;
				XMLNode node = parseElementHeader(s.substring(cl, cr));
				node.setIsProlog(true);
			}
			else if (s.charAt(cl) == '/') {
				//end tag
				nodeStack.pop();
			}
			else if (s.charAt(cr - 1) == '/') {
				//self ending tag
				cr--;
				XMLNode node = parseElementHeader(s.substring(cl, cr));
				nodeStack.peek().addChild(node);
			}
			else if (s.charAt(cl) == '!') {
				if (s.charAt(cl + 1) == '-') {
					//just assume it's a comment. this is jank. 
				}
				else {
					//this is probably a DOCTYPE tag. idk what to do with these for now
					cl++;
					XMLNode node = parseElementHeader(s.substring(cl, cr));
				}
			}
			else {
				//normal start tag
				XMLNode node = parseElementHeader(s.substring(cl, cr));
				nodeStack.peek().addChild(node);
				nodeStack.push(node);
			}
			i = tr - 1;
		}

		return root.getChildren().get(0);
	}

}
