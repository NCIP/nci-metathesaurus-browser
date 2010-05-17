package gov.nih.nci.evs.browser.test.lexevs;

import gov.nih.nci.evs.browser.utils.RemoteServerUtil;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.lexevs.metabrowser.MetaBrowserService;
import org.LexGrid.lexevs.metabrowser.MetaBrowserService.Direction;
import org.LexGrid.lexevs.metabrowser.model.RelationshipTabResults;
import org.apache.log4j.Logger;


public class TestExtension {
    private static Logger _logger = Logger.getLogger(TestExtension.class);
	private static String url = "http://ncias-d177-v.nci.nih.gov:19480/lexevsapi51";
	
	
	public static void main(String[] args) throws Exception {
//		LexBIGService lbs = 
//			(LexEVSApplicationService)ApplicationServiceProvider.getApplicationServiceFromUrl(url, "EvsServiceInfo");
		LexBIGService lbs = RemoteServerUtil.createLexBIGService();
		
		MetaBrowserService mbs = (MetaBrowserService)lbs.getGenericExtension("metabrowser-extension");

		
		_logger.debug("Count: " + mbs.getCount("C1140162", null, Direction.SOURCEOF));
		
		
		_logger.debug("Showing Source Of:");
		
		Map<String,List<RelationshipTabResults>> map = mbs.getRelationshipsDisplay("C1140162", null, Direction.SOURCEOF);
	
		for(String rel : map.keySet()){
			_logger.debug("Relations for REL: " + rel);
			List<RelationshipTabResults> relations = map.get(rel);
			for(RelationshipTabResults result : relations){
				_logger.debug(" - CUI: " + result.getCui());
				_logger.debug("   - Name: " + result.getName());
				_logger.debug("   - REL: " + result.getRel());
				_logger.debug("   - RELA: " + result.getRela());
				_logger.debug("   - Source: " + result.getSource());
			}
		}
		
		_logger.debug("Showing Target Of:");
		
		map = mbs.getRelationshipsDisplay("C1140162", null, Direction.TARGETOF);
		
		for(String rel : map.keySet()){
			_logger.debug("Relations for REL: " + rel);
			List<RelationshipTabResults> relations = map.get(rel);
			for(RelationshipTabResults result : relations){
				_logger.debug(" - CUI: " + result.getCui());
				_logger.debug("   - Name: " + result.getName());
				_logger.debug("   - REL: " + result.getRel());
				_logger.debug("   - RELA: " + result.getRela());
				_logger.debug("   - Source: " + result.getSource());
			}
		}
	}		
}
