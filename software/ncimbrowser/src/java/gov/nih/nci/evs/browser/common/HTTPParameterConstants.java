package gov.nih.nci.evs.browser.common;

import java.util.*;

public class HTTPParameterConstants {

    public static final String[] HTTP_REQUEST_PARAMETER_NAMES = {
       "action",
       "adv_matchText",
       "adv_search_algorithm",
       "adv_search_source",
       "adv_search_type",
       "algorithm",
       "answer",
       "btn",
       "captcha_option",
       "checkmultiplicity",
       "code",
       "dictionary",
       "emailaddress",
       "key",
       "id",
       "matchText",
       "message",
       "ontology_display_name",
       "ontology_node_id",
       "ontology_source",
       "ontology_sab",
       "opt",
       "page_number",
       "prop",
       "ranking",
       "refresh",
       "rel",
       "rel_search_association",
       "rel_search_rela",
       "rela",
       "sab",
       "scheme",
       "searchTarget",
       "searchTerm",
       "searchTerm:search.x",
       "searchTerm:search.y",
       "searchTerm:source",
       "searchText",
       "selectProperty",
       "selectPropertyType",
       "selectSearchOption",
       "sort",
       "sort0","sort1","sort2","sort3","sort4","sort5",
       "sortBy",
       "sortBy2",
       "source",
       "sourcecode",
       "subject",
       "text",
       "type",
       "advancedSearchForm",
       "advancedSearchForm:adv_search.x",
       "advancedSearchForm:adv_search.y",
       "javax.faces.ViewState",
       "referer",
       "version",
       "cartAction",
       "cartAction.x",
       "cartAction.y",
       "cartAction1.x",
       "cartAction1.y",
       "cartAction2.x",
       "cartAction2.y",
       "cartAction3.x",
       "cartAction3.y",
       "cartAction4.x",
       "cartAction4.y",
       "cartAction5.x",
       "cartAction5.y",
       "ans",
       "addtocart"
    };

    public static final List HTTP_REQUEST_PARAMETER_NAME_LIST = Arrays.asList(HTTP_REQUEST_PARAMETER_NAMES);

	public static final String[] adv_search_algorithm_values = new String[] {"contains", "exactMatch", "lucene", "startsWith"};
	public static final String[] algorithm_values = new String[] {"contains", "exactMatch", "startsWith"};
	public static final String[] direction_values = new String[] {"source", "target"};
	public static final String[] searchTarget_values = new String[] {"codes", "names", "properties", "relationships"};
	public static final String[] selectSearchOption_values = new String[] {"Code", "Name", "Property", "Relationship"};

	public static List adv_search_algorithm_value_list = Arrays.asList(adv_search_algorithm_values);
	public static List algorithm_value_list = Arrays.asList(algorithm_values);
	public static List direction_value_list = Arrays.asList(direction_values);
	public static List searchTarget_value_list = Arrays.asList(searchTarget_values);
	public static List selectSearchOption_value_list = Arrays.asList(selectSearchOption_values);

    /**
     * Constructor
     */
    private HTTPParameterConstants() {
        // Prevent class from being explicitly instantiated
    }


} // Class HTTPParameterConstants
