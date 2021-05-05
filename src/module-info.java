module dependencies {
	exports ferramentasDesign;
	exports gui;
	exports recursos;
	exports comunicacao;

	requires java.base;
	requires java.datatransfer;
	requires transitive java.desktop;
}