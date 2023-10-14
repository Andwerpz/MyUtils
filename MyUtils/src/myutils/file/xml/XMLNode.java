package myutils.file.xml;

import java.util.ArrayList;
import java.util.HashMap;

import myutils.misc.Pair;

public class XMLNode {

	private boolean isProlog = false;

	private String name;
	private ArrayList<XMLNode> children;
	private HashMap<String, String> attributes;

	private ArrayList<String> content;

	public XMLNode(String name) {
		this.name = name;
		this.children = new ArrayList<>();
		this.attributes = new HashMap<>();
		this.content = new ArrayList<>();
	}

	public String getName() {
		return this.name;
	}

	protected void addChild(XMLNode child) {
		this.children.add(child);
	}

	public ArrayList<XMLNode> getChildren() {
		return this.children;
	}

	protected void addAttribute(String name, String content) {
		this.attributes.put(name, content);
	}

	public String getAttributeContent(String attrName) {
		return this.attributes.get(attrName);
	}

	protected void setIsProlog(boolean b) {
		this.isProlog = b;
	}

	public boolean isProlog() {
		return this.isProlog;
	}

	protected void addContent(String content) {
		this.content.add(content);
	}

	public ArrayList<String> getContent() {
		return this.content;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append(this.name);
		if (this.attributes.size() != 0) {
			ArrayList<Pair<String, String>> arr = new ArrayList<>();
			for (String attrName : this.attributes.keySet()) {
				arr.add(new Pair<String, String>(attrName, this.attributes.get(attrName)));
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
