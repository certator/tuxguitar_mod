package org.herac.tuxguitar.app.system.tuning;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.herac.tuxguitar.app.util.TGMusicKeyUtils;
import org.herac.tuxguitar.song.models.TGString;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TuningTemplateReader {
	private static final String TUNING_TAG = "tuning";
	private static final String TUNING_ATTRIBUTE_NAME = "name";
	private static final String TUNING_ATTRIBUTE_INSTRUMENT = "instrument";
	private static final String TUNING_ATTRIBUTE_STRINGS = "strings";

	public static List<TuningTemplate> getTuningTemplates(String fileName) {
		try{
			File file = new File(fileName);
			if (file.exists()){
				return getTunings(getDocument(new FileInputStream(file)).getFirstChild());
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		return null;
	}

	public static List<TuningTemplate> getTuningTemplates(InputStream is) {
		try{
			if (is!=null){
				return getTunings(getDocument(is).getFirstChild());
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		return null;
	}

	private static Document getDocument(InputStream is) {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(is);
			document.normalize();
		} catch (SAXException sxe) {
			sxe.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return document;
	}

	/**
	 * Read tuning templates from XML file
	 * 
	 * @param shortcutsNode
	 * @return
	 */
	private static List<TuningTemplate> getTunings(Node tuningNode){
		List<TuningTemplate> list = new ArrayList<TuningTemplate>();

		NodeList nodeList = tuningNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);

			if (child.getNodeName().equals(TUNING_TAG)) {
				NamedNodeMap params = child.getAttributes();

				try {
					Node nodeInstrument = params.getNamedItem(TUNING_ATTRIBUTE_INSTRUMENT);
					Node nodeName = params.getNamedItem(TUNING_ATTRIBUTE_NAME);
					Node nodeStrings = params.getNamedItem(TUNING_ATTRIBUTE_STRINGS);
					if( nodeInstrument == null ) {
						throw new IllegalArgumentException("Atribute '" + TUNING_ATTRIBUTE_INSTRUMENT + "' expected");
					}
					if( nodeName == null ) {
						throw new IllegalArgumentException("Atribute '" + TUNING_ATTRIBUTE_NAME + "' expected");
					}
					if( nodeStrings == null ) {
						throw new IllegalArgumentException("Atribute '" + TUNING_ATTRIBUTE_STRINGS + "' expected");
					}
					
					String instrument = nodeInstrument.getNodeValue();
					String name = nodeName.getNodeValue();
					String stringsSyntax = nodeStrings.getNodeValue();

					TGString[] strings = parseStrings(stringsSyntax);
					if (strings != null) {
						list.add(new TuningTemplate(instrument, name, strings));
					}
				} catch (IllegalArgumentException e) {
					System.err.println("Invalid tuning template in XML file, element skiped");
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	private static TGString[] parseStrings(String rawInput) {
		String rawStrings[] = rawInput.split("[ \t]+");
		TGString[] strings = new TGString[rawStrings.length];
		for (int i = 0; i < rawStrings.length; i++) {
			int value = TGMusicKeyUtils.getTgValueFromName(rawStrings[i]);
			TGString string = new TGString() {};
			string.setNumber(i);
			string.setValue(value);
			strings[i] = string;
		}
		return strings;
	}
}
