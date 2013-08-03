package org.herac.tuxguitar.app.tools.template;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.util.TGFileUtils;
import org.herac.tuxguitar.io.base.TGFileFormatManager;
import org.herac.tuxguitar.song.models.TGSong;

public class TGTemplateManager {

	private static final String TEMPLATE_DEFAULT_RESOURCE = "template-default.tg";

	private static final String TEMPLATES_PREFIX = "templates/";
	private static final String TEMPLATES_CONFIG_PATH = (TEMPLATES_PREFIX + "templates.xml");

	private final List<TGTemplate> templates;

	public TGTemplateManager(){
		this.templates = new ArrayList<TGTemplate>();
		this.loadTemplates();
	}

	public int countTemplates(){
		return this.templates.size();
	}

	public Iterator<TGTemplate> getTemplates(){
		return this.templates.iterator();
	}

	public void loadTemplates(){
		try{
			InputStream templateInputStream = TGFileUtils.getResourceAsStream(TEMPLATES_CONFIG_PATH);
			if( templateInputStream != null ){
				TGTemplateReader tgTemplateReader = new TGTemplateReader();
				tgTemplateReader.loadTemplates(this.templates, templateInputStream);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public TGTemplate getDefatulTemplate(){
		TGTemplate tgTemplate = new TGTemplate();
		tgTemplate.setName(new String());
		tgTemplate.setResource(TEMPLATE_DEFAULT_RESOURCE);
		return tgTemplate;
	}

	public TGSong getTemplateAsSong(TGTemplate tgTemplate){
		try{
			if( tgTemplate != null && tgTemplate.getResource() != null ){
				InputStream stream = TGFileUtils.getResourceAsStream(TEMPLATES_PREFIX + tgTemplate.getResource());
				return TGFileFormatManager.instance().getLoader().load(TuxGuitar.instance().getSongManager().getFactory(),stream);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

}
