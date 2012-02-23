package gov.nih.nci.evs.browser.test;

import java.util.*;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.log4j.Logger;

import gov.nih.nci.evs.browser.bean.OntologyBean;
import gov.nih.nci.evs.browser.utils.RemoteServerUtil;
import gov.nih.nci.evs.browser.utils.ResolvedConceptReferencesIteratorWrapper;

public class ContainsRelationTest {
    // --------------------------------------------------------------------------
    private static Logger _logger =
        Logger.getLogger(ContainsRelationTest.class);
    private static final String SEPARATOR =
        "----------------------------------------"
            + "----------------------------------------";

    // --------------------------------------------------------------------------
    private static int RESOLVE_SOURCE = 1;
    private static int RESOLVE_TARGET = -1;
    private static int RESTRICT_SOURCE = -1;
    private static int RESTRICT_TARGET = 1;

    // --------------------------------------------------------------------------
    public ContainsRelationTest() {
        String scheme = "NCI Metathesaurus";
        // String matchText = "single dose vial";
        String matchText = "single dose";
        String source = "ALL";
        String matchAlgorithm = "contains";
        String version = null;
        String rel = null;
        String rela = null;
        int maxToReturn = 1000;
        search(scheme, matchText, source, matchAlgorithm, version, rel, rela,
            maxToReturn);
    }

    private void search(String scheme, String matchText, String source,
        String matchAlgorithm, String version, String rel, String rela,
        int maxToReturn) {
        try {
            _logger.debug(SEPARATOR);
            _logger.debug("Calling new SearchUtils().searchByRELA");
            _logger.debug("  * scheme: " + scheme);
            _logger.debug("  * matchText: " + matchText);
            _logger.debug("  * source: " + source);
            _logger.debug("  * matchAlgorithm: " + matchAlgorithm);
            _logger.debug("  * version: " + version);
            _logger.debug("  * rel: " + rel);
            _logger.debug("  * rela: " + rela);
            _logger.debug("  * maxToReturn: " + maxToReturn);
            ResolvedConceptReferencesIteratorWrapper wrapper =
                searchByRELA(scheme, version, matchText, source,
                    matchAlgorithm, rel, rela, maxToReturn);

            ResolvedConceptReferencesIterator iterator = wrapper.getIterator();
            printConcepts(iterator, maxToReturn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printConcepts(ResolvedConceptReferencesIterator iterator,
        int maxToReturn) throws Exception {
        _logger.debug(SEPARATOR);
        int i = 0, size = iterator.numberRemaining();
        while (iterator.hasNext()) {
            ResolvedConceptReference[] rcrs =
                iterator.next(maxToReturn).getResolvedConceptReference();
            for (ResolvedConceptReference rcr : rcrs) {
                String code = rcr.getConceptCode();
                String name = rcr.getEntityDescription().getContent();
                _logger.debug(++i + ") " + code + ": " + name);
            }
        }
        _logger.debug("size: " + size);
    }

    public ResolvedConceptReferencesIteratorWrapper searchByRELA(String scheme,
        String version, String matchText, String source, String matchAlgorithm,
        String rel, String rela, int maxToReturn) {

        if (matchText == null || matchText.length() == 0)
            return null;

        matchText = matchText.trim();
        if (matchAlgorithm.compareToIgnoreCase("contains") == 0) {
            matchAlgorithm = findBestContainsAlgorithm(matchText);
        }

        ResolvedConceptReferencesIterator iterator = null;
        CodedNodeSet cns = null;
        CodedNodeSet.PropertyType[] propertyTypes =
            new CodedNodeSet.PropertyType[1];
        propertyTypes[0] = PropertyType.PRESENTATION;
        LexBIGService lbSvc = null;
        try {
            lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null) {
                _logger.warn("lbSvc = null");
                return null;
            }
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
            if (cns == null) {
                _logger.warn("cns = null");
                return null;
            }

            try {
                LocalNameList sourceList = null;
                if (source != null && source.compareToIgnoreCase("ALL") != 0) {
                    sourceList = new LocalNameList();
                    sourceList.addEntry(source);
                }

                cns =
                    cns.restrictToMatchingDesignations(matchText,
                        SearchDesignationOption.ALL, matchAlgorithm, null);

                if (source != null && source.compareTo("ALL") != 0) {
                    cns =
                        cns.restrictToProperties(null, propertyTypes,
                            sourceList, null, null);
                }

                try {
                    ResolvedConceptReferencesIterator it =
                        cns.resolve(null, null, null, null, false);
                    int num = it.numberRemaining();
                    if (num > 1000) {
                        System.out.println("Matching target concepts size: "
                            + num + " > 1000 -- method aborts.");
                        return null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } catch (Exception ex) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        if (cns == null) {
            return null;
        }

        try {
            CodedNodeGraph cng =
                getRestrictedCodedNodeGraph(lbSvc, scheme, version, rel, rela,
                    cns, 1);

            if (cng == null) {
                return null;
            }

            boolean resolveForward = false;
            boolean resolveBackward = true;
            int resolveAssociationDepth = 0;

            System.out.println("Calling resolveCodedNodeGraph");
            iterator =
                resolveCodedNodeGraph(lbSvc, scheme, version, cng,
                    RESOLVE_SOURCE, -1);

            if (iterator != null) {
                return new ResolvedConceptReferencesIteratorWrapper(iterator);
            }

        } catch (Exception ex) {

        }
        return null;
    }

    private String findBestContainsAlgorithm(String matchText) {
        if (matchText == null)
            return "nonLeadingWildcardLiteralSubString";
        matchText = matchText.trim();
        if (matchText.length() == 0)
            return "nonLeadingWildcardLiteralSubString"; // or null
        if (matchText.length() > 1)
            return "nonLeadingWildcardLiteralSubString";
        char ch = matchText.charAt(0);
        if (Character.isDigit(ch))
            return "literal";
        else if (Character.isLetter(ch))
            return "LuceneQuery";
        else
            return "literalContains";
    }

    public ResolvedConceptReferencesIterator resolveCodedNodeGraph(
        LexBIGService lbSvc, String scheme, String version, CodedNodeGraph cng,
        int direction, int maxToReturn) {
        ConceptReferenceList codeList = new ConceptReferenceList();
        try {
            ResolvedConceptReferenceList rcrl = null;
            try {
                boolean resolveForward = true;
                boolean resolveBackward = false;
                int resolveAssociationDepth = 0;
                int resolveCodedEntryDepth = 0;

                rcrl =
                    cng.resolveAsList(null, resolveForward,
                        resolveBackward, // graphFocus, resolveForward,
                        // resolveBackward
                        resolveCodedEntryDepth, resolveAssociationDepth, null,
                        null, null, maxToReturn);

            } catch (Exception ex) {
                System.out.println("Exception thrown -- cng.resolveAsList.");
                return null;
            }

            ResolvedConceptReference[] rcrArray =
                rcrl.getResolvedConceptReference();
            org.LexGrid.concepts.Concept ce = null;

            for (int i = 0; i < rcrArray.length; i++) {
                if (direction == RESOLVE_SOURCE) {
                    codeList.addConceptReference(rcrArray[i]);

                } else if (direction == RESOLVE_TARGET) {
                    AssociationList al = rcrArray[i].getSourceOf();
                    Association[] assos = al.getAssociation();
                    if (assos != null) {
                        for (int j = 0; j < assos.length; j++) {
                            Association asso = (Association) assos[j];
                            AssociatedConceptList acl =
                                asso.getAssociatedConcepts();
                            AssociatedConcept[] ac_array =
                                acl.getAssociatedConcept();
                            for (int k = 0; k < ac_array.length; k++) {
                                AssociatedConcept ac =
                                    (AssociatedConcept) ac_array[k];
                                codeList.addConceptReference(ac);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CodedNodeSet cns = null;
        try {
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            if (version != null)
                versionOrTag.setVersion(version);

            cns = lbSvc.getNodeSet(scheme, versionOrTag, null);
            cns = cns.restrictToCodes(codeList);
            if (cns == null) {
                return null;
            }
            ResolvedConceptReferencesIterator it =
                cns.resolve(null, null, null, null, false);
            return it;
        } catch (Exception ex) {

        }
        return null;
    }

    public CodedNodeGraph getRestrictedCodedNodeGraph(LexBIGService lbSvc,
        String scheme, String version, String rel, String rela,
        CodedNodeSet cns, int direction) {
        CodedNodeGraph cng = null;
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(version);

        _logger.warn("getRestrictedCodedNodeGraph rel: " + rel);
        _logger.warn("getRestrictedCodedNodeGraph rela: " + rela);

        try {
            cng = lbSvc.getNodeGraph(scheme, versionOrTag, null);
            NameAndValueList asso_list = null;
            String[] associationsToNavigate = null;

            if (rel == null) {
                Vector w = OntologyBean.getAssociationNames();
                if (w == null || w.size() == 0) {
                    _logger
                        .warn("OntologyBean.getAssociationNames() returns null, or nothing???");
                } else {
                    associationsToNavigate = new String[w.size()];
                    for (int i = 0; i < w.size(); i++) {
                        String nm = (String) w.elementAt(i);
                        associationsToNavigate[i] = nm;
                    }
                }
            } else {
                associationsToNavigate = new String[] { rel };
            }
            asso_list = createNameAndValueList(associationsToNavigate, null);

            NameAndValueList qualifier_list = null;
            if (rela != null) {
                qualifier_list =
                    createNameAndValueList(new String[] { "rela" },
                        new String[] { rela });
            }

            cng = cng.restrictToAssociations(asso_list, qualifier_list);

            if (cns != null) {
                if (direction == -1) {
                    _logger.warn("restrictToSourceCodes... ");

                    cng = cng.restrictToSourceCodes(cns);
                } else if (direction == 1) {
                    _logger.warn("restrictToTargetCodes... ");

                    cng = cng.restrictToTargetCodes(cns);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cng;
    }

    public static NameAndValueList createNameAndValueList(String[] names,
        String[] values) {
        NameAndValueList nvList = null;
        if (names != null && names.length > 0) {
            nvList = new NameAndValueList();
            for (int i = 0; i < names.length; i++) {
                NameAndValue nv = new NameAndValue();
                nv.setName(names[i]);
                if (values != null) {
                    nv.setContent(values[i]);
                }
                nvList.addNameAndValue(nv);
            }
        }
        return nvList;
    }

    private static String[] parse(String[] args) {
        String prevArg = "";
        ArrayList<String> newArgs = new ArrayList<String>();
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-propertyFile")) {
                prevArg = arg;
            } else if (prevArg.equals("-propertyFile")) {
                System.setProperty(
                    "gov.nih.nci.evs.browser.NCImBrowserProperties", arg);
                prevArg = "";
            } else {
                newArgs.add(arg);
            }
        }
        return newArgs.toArray(new String[newArgs.size()]);
    }

    public static void main(String[] args) {
        parse(args);
        new ContainsRelationTest();
    }
}
