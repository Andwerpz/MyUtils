package myutils.file.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import myutils.misc.Pair;

public class XMLNode {

	private boolean isProlog = false;
	private boolean isDoctype = false;

	//if is true, then tag is of form <name/>
	private boolean isSelfEnding = false;

	private String name;
	private ArrayList<XMLNode> children;
	private ArrayList<String> attributes;
	private HashMap<String, String> attributeContent;

	private ArrayList<String> content;

	public XMLNode(String name) {
		this.name = name;
		this.children = new ArrayList<>();
		this.attributes = new ArrayList<>();
		this.attributeContent = new HashMap<>();
		this.content = new ArrayList<>();
	}

	public String getName() {
		return this.name;
	}

	public void addChild(XMLNode child) {
		this.children.add(child);
	}

	public ArrayList<XMLNode> getChildren() {
		return this.children;
	}

	public void addAttribute(String name, String content) {
		this.attributes.add(name);
		this.attributeContent.put(name, content);
	}

	public ArrayList<String> getAttributes() {
		return this.attributes;
	}

	public String getAttributeContent(String attrName) {
		return this.attributeContent.get(attrName);
	}

	public void setIsProlog(boolean b) {
		this.isProlog = b;
	}

	public boolean isProlog() {
		return this.isProlog;
	}

	public void setIsDoctype(boolean b) {
		this.isDoctype = b;
	}

	public boolean isDoctype() {
		return this.isDoctype;
	}

	public void setIsSelfEnding(boolean b) {
		this.isSelfEnding = b;
	}

	public boolean isSelfEnding() {
		return this.isSelfEnding;
	}

	public void addContent(String content) {
		this.content.add(content);
	}

	public ArrayList<String> getContent() {
		return this.content;
	}

	/**
	 * Overwrites the file's contents with the contents of the children of this node. 
	 * Assumes that this node is the "root" of the file. 
	 * @param file
	 * @throws IOException
	 */
	public void saveToFile(File file) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}

		BufferedWriter fout = new BufferedWriter(new FileWriter(file));
		Stack<Pair<XMLNode, Integer>> node_stack = new Stack<>();
		node_stack.push(new Pair<>(this, 0));
		String tab_str = "    ";
		while (node_stack.size() != 0) {
			XMLNode node = node_stack.peek().first;
			int child_ind = node_stack.peek().second;
			node_stack.pop();
			if (child_ind == -1) {
				//write tabs
				for (int i = 0; i < node_stack.size() - 1; i++) {
					fout.append(tab_str);
				}

				//we have to write this node 
				fout.append("<");
				if (node.isDoctype()) {
					fout.append("!");
				}
				else if (node.isProlog()) {
					fout.append("?");
				}
				fout.append(node.getName());

				for (String attr : node.getAttributes()) {
					fout.append(" " + attr + " = " + "\"" + node.getAttributeContent(attr) + "\"");
				}

				if (node.isDoctype()) {
					for (String s : node.getContent()) {
						fout.append(" " + s);
					}
				}

				if (node.isProlog()) {
					fout.append("?");
				}
				else if (node.isSelfEnding()) {
					fout.append("/");
				}
				fout.append(">");
				fout.append("\n");

				if (!node.isSelfEnding() && !node.isProlog() && !node.isDoctype()) {
					node_stack.push(new Pair<>(node, child_ind + 1));

					//write all contents sequentially
					for (String s : node.getContent()) {
						//write tabs
						for (int i = 0; i < node_stack.size() - 1; i++) {
							fout.append(tab_str);
						}
						fout.append(s + "\n");
					}
				}
				continue;
			}
			else if (child_ind == node.getChildren().size()) {
				if (node_stack.size() == 0) {
					//don't write end tag for root node
					break;
				}

				//write tabs
				for (int i = 0; i < node_stack.size() - 1; i++) {
					fout.append(tab_str);
				}

				//write this nodes end tag
				fout.append("</" + node.getName() + ">");
				fout.append("\n");
				continue;
			}

			//push this node back onto the stack
			node_stack.push(new Pair<>(node, child_ind + 1));

			//pick the next child and push it onto the stack
			node_stack.push(new Pair<>(node.getChildren().get(child_ind), -1));

		}
		fout.close();
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append(this.name);
		if (this.attributes.size() != 0) {
			ArrayList<Pair<String, String>> arr = new ArrayList<>();
			for (String attrName : this.attributes) {
				arr.add(new Pair<String, String>(attrName, this.getAttributeContent(attrName)));
			}
			res.append("(");
			for (int i = 0; i < arr.size(); i++) {
				String name = arr.get(i).first;
				String content = arr.get(i).second;
				res.append(name + " : " + "\"" + content + "\"");
				if (i != arr.size() - 1) {
					res.append(", ");
				}
			}
			res.append(")");
		}
		if (this.content.size() != 0) {
			res.append("[");
			for (int i = 0; i < this.content.size(); i++) {
				res.append(this.content.get(i));
				if (i != this.content.size() - 1) {
					res.append(", ");
				}
			}
			res.append("]");
		}
		if (this.children.size() != 0) {
			res.append("{");
			for (int i = 0; i < this.children.size(); i++) {
				res.append(this.children.get(i).toString());
				if (i != this.children.size() - 1) {
					res.append(", ");
				}
			}
			res.append("}");
		}
		return res.toString();
	}

}
