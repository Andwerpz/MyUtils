package myutils.file.xml.svg;

import java.util.ArrayList;

import myutils.file.xml.XMLNode;
import myutils.file.xml.XMLReader;

public class SVGReader {

	public static ArrayList<SVGElement> parseStringAsSVG(String s) {
		return parseXMLAsSVG(XMLReader.parseStringAsXML(s));
	}

	public static ArrayList<SVGElement> parseXMLAsSVG(XMLNode root) {
		System.out.println(root);
		ArrayList<SVGElement> ans = new ArrayList<>();
		parseXMLAsSVG(root, ans);
		return ans;
	}

	private static void parseXMLAsSVG(XMLNode root, ArrayList<SVGElement> ans) {
		//for now, just look for all path elements. 
		//TODO add support for more svg elements. 

		if (root.getName().equals("path")) {
			SVGPath path = new SVGPath(root.getAttributeContent("id"), root.getAttributeContent("d"));
			ans.add(path);
		}

		for (XMLNode child : root.getChildren()) {
			parseXMLAsSVG(child, ans);
		}
	}

}
