package gov.nih.nci.evs.browser.utils.test;

import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSApplicationService;
import org.LexGrid.lexevs.metabrowser.MetaBrowserService;
import org.LexGrid.lexevs.metabrowser.MetaBrowserService.Direction;
import org.LexGrid.lexevs.metabrowser.model.RelationshipTabResults;


public class TestExtension {

	private static String url = "http://ncias-d177-v.nci.nih.gov:19480/lexevsapi51";
	
	
	public static void main(String[] args) throws Exception {
		LexBIGService lbs = 
			(LexEVSApplicationService)ApplicationServiceProvider.getApplicationServiceFromUrl(url, "EvsServiceInfo");
		
		MetaBrowserService mbs = (MetaBrowserService)lbs.getGenericExtension("metabrowser-extension");

		
		System.out.println("Count: " + mbs.getCount("C1140162", null, Direction.SOURCEOF));
		
		
		System.out.println("Showing Source Of:");
		
		Map<String,List<RelationshipTabResults>> map = mbs.getRelationshipsDisplay("C1140162", null, Direction.SOURCEOF);
	
		for(String rel : map.keySet()){
			System.out.println("Relations for REL: " + rel);
			List<RelationshipTabResults> relations = map.get(rel);
			for(RelationshipTabResults result : relations){
				System.out.println(" - CUI: " + result.getCui());
				System.out.println("   - Name: " + result.getName());
				System.out.println("   - REL: " + result.getRel());
				System.out.println("   - RELA: " + result.getRela());
				System.out.println("   - Source: " + result.getSource());
			}
		}
		
		System.out.println("Showing Target Of:");
		
		map = mbs.getRelationshipsDisplay("C1140162", null, Direction.TARGETOF);
		
		for(String rel : map.keySet()){
			System.out.println("Relations for REL: " + rel);
			List<RelationshipTabResults> relations = map.get(rel);
			for(RelationshipTabResults result : relations){
				System.out.println(" - CUI: " + result.getCui());
				System.out.println("   - Name: " + result.getName());
				System.out.println("   - REL: " + result.getRel());
				System.out.println("   - RELA: " + result.getRela());
				System.out.println("   - Source: " + result.getSource());
			}
		}
	}		
}
