package gov.nih.nci.evs.browser.utils;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.Constructors;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.commonTypes.EntityDescription;

public class MetaTreeUtils {
	private LexBIGService lbs;
	private static String NCI_META_THESAURUS = "NCI MetaThesaurus";

	public static void main(String[] args) throws Exception {
		MetaTreeUtils getRoots = new MetaTreeUtils();

		Long startTime = System.currentTimeMillis();
		getRoots.getRoots("NCI");
		System.out.println("Call took: " + (System.currentTimeMillis() - startTime) + "ms");
	}

	public MetaTreeUtils(){
		init();
	}

	private void init(){
		//lbs = LexBIGServiceImpl.defaultInstance();
		lbs = RemoteServerUtil.createLexBIGService();
	}

    ///////////////////
    // Source Roots  //
    ///////////////////

	/**
	 * Finds the root node of a given sab.
	 *
	 * @param sab
	 * @throws Exception
	 */


	public void getRoots(String sab) throws Exception {
		ResolvedConceptReference root = resolveReferenceGraphForward(getCodingSchemeRoot(sab));
		AssociationList assocList = root.getSourceOf();
		for(Association assoc : assocList.getAssociation()){
			for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()){
				if(this.isSabQualifiedAssociation(ac, sab)){
					displayRoot(ac);
				}
			}
		}
	}

	public ResolvedConceptReferenceList getSourceRoots(String sab) throws Exception {
		ResolvedConceptReferenceList rcrl = new ResolvedConceptReferenceList();
		try {
			ResolvedConceptReference root = resolveReferenceGraphForward(getCodingSchemeRoot(sab));
			AssociationList assocList = root.getSourceOf();
			for(Association assoc : assocList.getAssociation()){
				for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()){
					if(this.isSabQualifiedAssociation(ac, sab)){
						ResolvedConceptReference r = new ResolvedConceptReference();
						EntityDescription entityDescription = new EntityDescription();
						entityDescription.setContent(ac.getEntityDescription().getContent());
						r.setEntityDescription(entityDescription);
						r.setCode(ac.getCode());
						rcrl.addResolvedConceptReference(r);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR - Roots for source " + sab + " not found.");
		}

		return rcrl;
	}


	/**
	 * Displays the root node.
	 *
	 * @param ac
	 */
	protected void displayRoot(AssociatedConcept ac){
		System.out.println(ac.getCode() + " - " + ac.getEntityDescription().getContent());
	}

	/**
	 * Gets the UMLS root node of a given SAB.
	 *
	 * @param sab
	 * @return
	 * @throws LBException
	 */
	private ResolvedConceptReference getCodingSchemeRoot(String sab) throws LBException {
		CodedNodeSet cns = lbs.getCodingSchemeConcepts(NCI_META_THESAURUS, null);
		cns.restrictToProperties(null, new PropertyType[] {PropertyType.PRESENTATION}, Constructors.createLocalNameList("SRC"), null, Constructors.createNameAndValueList("source-code", "V-"+sab));
		ResolvedConceptReference[] refs = cns.resolveToList(null, null, new PropertyType[] {PropertyType.PRESENTATION}, -1).getResolvedConceptReference();

		if(refs.length > 1){
			throw new LBException("Found more than one Root for SAB: " + sab);
		}
		if(refs.length == 0){
			throw new LBException("Didn't find a Root for SAB: " + sab);
		}
		return refs[0];
	}

	/**
     * Resolve the relationships of a ResolvedConceptReference forward one level.
     *
     * @param ref
     * @return
     * @throws Exception
     */
    private ResolvedConceptReference resolveReferenceGraphForward(ResolvedConceptReference ref) throws Exception {
        CodedNodeGraph cng = lbs.getNodeGraph(NCI_META_THESAURUS, null, null);
        cng.restrictToAssociations(Constructors.createNameAndValueList(new String[]{"CHD", "hasSubtype"}), null);
        ResolvedConceptReference[] refs = cng.resolveAsList(ref, true, false, 1, 1, null, null, null, -1).getResolvedConceptReference();
        return refs[0];
    }

    /**
     * Determines whether or not the given reference is a root Concept for the given Coding Scheme.
     *
     * @param reference
     * @param sourceCodingScheme
     * @return
     */
    private boolean isSabQualifiedAssociation(AssociatedConcept ac, String sab){
    	NameAndValue[] nvl = ac.getAssociationQualifiers().getNameAndValue();
    	for(NameAndValue nv : nvl){
    		if(nv.getName().equals(sab) &&
    				nv.getContent().equals("Source")){
    			return true;
    		}
    	}
    	return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}