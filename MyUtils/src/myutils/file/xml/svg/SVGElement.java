package myutils.file.xml.svg;

public abstract class SVGElement {

	public static final String TYPE_PATH = "path";

	private String type;

	protected SVGElement(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

}
